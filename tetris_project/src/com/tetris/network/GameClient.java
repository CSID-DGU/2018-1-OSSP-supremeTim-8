package com.tetris.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.tetris.window.Tetris;
import com.tetris.shape.Line; // 아이템 사용을 위해 import

public class GameClient implements Runnable {
	private Tetris tetris;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	private String ip;
	private int port;
	private String name;
	private int index;
	private boolean isPlay;

	public GameClient(Tetris tetris, String ip, int port, String name) { // 생성자
		this.tetris = tetris;
		this.ip = ip;
		this.port = port;
		this.name = name;
	}// GameClient()

	public boolean start() {
		return this.execute();
	}

	public boolean execute() {
		try {
			socket = new Socket(ip, port);
			ip = InetAddress.getLocalHost().getHostAddress(); // IP 주소 얻기
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			System.out.println("client is working");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		tetris.getBoard().clearMessage(); // 모든 창 초기화

		DataShip data = new DataShip();
		data.setIp(ip);
		data.setName(name);
		send(data); // 핸들러 생성자에서 필드 초기화하기 위해 보냄

		printSystemMessage(DataShip.PRINT_SYSTEM_OPEN_MESSAGE); // 새 클라이언트 오픈했다고 알림

		printSystemMessage(DataShip.PRINT_SYSTEM_ADDMEMBER_MESSAGE); // 추가 클라이언트 알림 -> 핸들러 스레드가 시작하고 읽음

		setIndex(); // 클라이언트에게 사용자 번호 부여(ex. 1P)

		Thread t = new Thread(this);
		t.start(); // 스레드 시작

		return true; // 처음엔 여기서부터 서버, 서버 클라이언트, 서버 클라이언트 핸들러의 스레드 실행 시작 / 이 후 클라이언트 추가되면 계속 스레드 추가
	}

	public void run() {
		DataShip data = null;
		while (true) {
			try {
				data = (DataShip) ois.readObject();
			} catch (IOException e) {
				e.printStackTrace();
				break;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			if (data == null)
				continue;
			if (data.getCommand() == DataShip.CLOSE_NETWORK) { // 연결종료
				reCloseNetwork();
				break;
			} else if (data.getCommand() == DataShip.SERVER_EXIT) { // cmd 정보 바꾼 뒤 연결 종료
				closeNetwork(false);
			} else if (data.getCommand() == DataShip.GAME_START) { // 게임 시작
				reGameStart(data.isPlay(), data.getMsg(), data.getSpeed());
			} else if (data.getCommand() == DataShip.ADD_BLOCK) { // 블럭 추가
				if (isPlay)
					reAddBlock(data.getMsg(), data.getNumOfBlock(), data.getIndex());
			} else if (data.getCommand() == DataShip.SET_INDEX) {
				reSetIndex(data.getIndex());
			} else if (data.getCommand() == DataShip.GAME_OVER) { // 게임 종료
				if (index == data.getIndex())
					isPlay = data.isPlay(); // 현재 플레이 상태 가져옴
				reGameover(data.getMsg(), data.getTotalAdd());
			} else if (data.getCommand() == DataShip.PRINT_MESSAGE) {
				rePrintMessage(data.getMsg());
			} else if (data.getCommand() == DataShip.PRINT_SYSTEM_MESSAGE) {
				rePrintSystemMessage(data.getMsg());
			} else if (data.getCommand() == DataShip.GAME_WIN) { // 이긴 경우
				rePrintSystemMessage(data.getMsg() + "\nTOTAL ADD : " + data.getTotalAdd());
				tetris.getBoard().setPlay(false);
			}

			else if (data.getCommand() == DataShip.USE_ITEM) { // 아이템 사용 시
				if (isPlay) // 클라이언트가 플레이 중이라면
					reUseItem(data.getMsg(), data.getItemNum(), data.getIndex(), data.getOtherIndex());
			}

		}

	}

	public void send(DataShip data) {
		try {
			oos.writeObject(data);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeNetwork(boolean isServer) {
		DataShip data = new DataShip(DataShip.CLOSE_NETWORK);
		if (isServer)
			data.setCommand(DataShip.SERVER_EXIT);
		send(data);
	}

	public void reCloseNetwork() {

		tetris.closeNetwork();
		try {
			ois.close();
			oos.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void gameStart(int speed) {
		DataShip data = new DataShip(DataShip.GAME_START);
		data.setSpeed(speed);
		send(data);
	}

	public void reGameStart(boolean isPlay, String msg, int speed) {
		this.isPlay = isPlay;
		tetris.gameStart(speed); // 속도를 기반으로 게임 시작 -> 여러가지 설정
		rePrintSystemMessage(msg);
	}

	public void printSystemMessage(int cmd) {
		DataShip data = new DataShip(cmd);
		send(data);
	}

	public void rePrintSystemMessage(String msg) {
		tetris.printSystemMessage(msg);
	}

	public void addBlock(int numOfBlock) {
		DataShip data = new DataShip(DataShip.ADD_BLOCK);
		data.setNumOfBlock(numOfBlock);
		send(data);
	}

	public void reAddBlock(String msg, int numOfBlock, int index) {
		if (index != this.index)
			tetris.getBoard().addBlockLine(numOfBlock); // 본인을 제외하고 나머지 블럭 추가
		rePrintSystemMessage(msg);
	}

	public void setIndex() {
		DataShip data = new DataShip(DataShip.SET_INDEX);
		send(data);
	}

	public void reSetIndex(int index) {
		this.index = index;
	}

	public void gameover() {
		DataShip data = new DataShip(DataShip.GAME_OVER);
		send(data);
	}

	public void reGameover(String msg, int totalAdd) { // 총 추가한 블럭라인 수 출력
		tetris.printSystemMessage(msg);
		tetris.printSystemMessage("TOTAL ADD : " + totalAdd);
	}

	public void printMessage(String msg) {
		DataShip data = new DataShip(DataShip.PRINT_MESSAGE);
		data.setMsg(msg);
		send(data);
	}

	public void rePrintMessage(String msg) {
		tetris.printMessage(msg);
	}

	public void reChangSpeed(Integer speed) { // 속도변화 메소드
		tetris.changeSpeed(speed);
	}

	public void useItem(int itemNum) { // 아이템을 사용하겠다는 메세지를 핸들러에게 보냄
		DataShip data = new DataShip(DataShip.USE_ITEM);
		data.setItemNum(itemNum);
		send(data);
	}

	public void reUseItem(String msg, int itemNum, int index, int otherIndex) { // 핸들러에게 메세지를 받고 실질적인 액션 취함

		rePrintSystemMessage(msg); // 아이템사용 관련 메세지는 모든 클라이언트에게 출력
		switch (itemNum) { // 아이템의 종류에 따라 결정
		case 1: {
			if (index != this.index && otherIndex == this.index) { // 본인에게는 미적용하고 상대 한명에게만 적용
				// 속도 아이템
				tetris.changeSpeed(20); // 속도 20으로 변경
				try {
					Thread.sleep(5000); // 5초동안
				} catch (InterruptedException e) {
					// TODO 자동 생성된 catch 블록
					e.printStackTrace();
				}
				tetris.changeSpeed(2 * tetris.getLevel()); // 그 전 스피드로 조정
				//tetris.changeSpeed(1); // 다시 1로 조정
			}
			break;
		}
		case 2: {
			// 블럭 지우는 아이템
			if (index == this.index) {
				tetris.getBoard().removeBlockLine(19);
				tetris.getBoard().removeBlockLine(20); // 맨아래 두 줄 삭제
			}
			break;
		}
		case 3: {
			// 일자블럭 아이템
			if(index == this.index) {
				tetris.getBoard().nextBlocks.clear(); // 대기열 초기화
				for(int i=0;i<5;i++) {
					tetris.getBoard().nextBlocks.add(new Line(4,1)); // 5개의 line 블럭 추가
				}
			}
			break;
		}
		case 4: {
			// 구름 아이템
			break;
		}
		default:
			break;
		}
	}
}
