package com.tetris.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class GameHandler extends Thread {
	private static boolean isStartGame;
	private static int maxRank; // 핸들러의 총 개수
	private int rank; // 순위

	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos; // 객체단위 입출력
	private String ip;
	private String name;
	private int index; // 사용자 번호
	private int totalAdd = 0; // 상대에게 추가한 블럭 수

	private ArrayList<GameHandler> list; 
	private ArrayList<Integer> indexList;

	public GameHandler(Socket socket, ArrayList<GameHandler> list, int index, ArrayList<Integer> indexList) {
		this.index = index;
		this.indexList = indexList;
		this.socket = socket;
		this.list = list; // 각 필드 초기화
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream()); // 객체스트림 생성
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			DataShip data = (DataShip) ois.readObject(); // 역직렬화 -> 객체의 내용을 바이트 단위로 나눈 것을 다시 합침
			ip = data.getIp();
			name = data.getName(); // 필드초기화

			data = (DataShip) ois.readObject();
			printSystemOpenMessage(); // 처음 open하고 메세지 출력
			printMessage(ip + ":" + name + "is in"); // 사용자 관련 메세지 출력
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}// GameHandler

	public void run() { // 새로운 스레드가 할 일 기술
		DataShip data = null;
		while (true) {
			try {
				data = (DataShip) ois.readObject(); // 역직렬화
			} catch (IOException e) {
				e.printStackTrace();
				break;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			if (data == null) // null이라면 다시 읽음
				continue;

			if (data.getCommand() == DataShip.CLOSE_NETWORK) { // 연결을 끊는 상황 -> 모두에게 알림
				printSystemMessage("<" + index + "P> EXIT");
				printMessage(ip + ":" + name + "is out");
				closeNetwork();
				break;
			} else if (data.getCommand() == DataShip.SERVER_EXIT) {
				exitServer();
			} else if (data.getCommand() == DataShip.PRINT_SYSTEM_OPEN_MESSAGE) {
				printSystemOpenMessage();
			} else if (data.getCommand() == DataShip.PRINT_SYSTEM_ADDMEMBER_MESSAGE) {
				printSystemAddMemberMessage();
			} else if (data.getCommand() == DataShip.ADD_BLOCK) {
				addBlock(data.getNumOfBlock());
			} else if (data.getCommand() == DataShip.GAME_START) {
				gameStart(data.getSpeed());
			} else if (data.getCommand() == DataShip.SET_INDEX) {
				setIndex();
			} else if (data.getCommand() == DataShip.GAME_OVER) {
				rank = maxRank--; // 현재 핸들러 개수를 rank에 저장하고 현재 핸들러 개수 감소
				gameover(rank); 
			} else if (data.getCommand() == DataShip.PRINT_MESSAGE) {
				printMessage(data.getMsg());
			} else if (data.getCommand() == DataShip.PRINT_SYSTEM_MESSAGE) {
				printSystemMessage(data.getMsg());
			}

		} // while(true)

		try {
			list.remove(this);
			ois.close();
			oos.close();
			socket.close(); // 끝내는 상황
		} catch (IOException e) {
			e.printStackTrace();
		}

	}// run

	public void printMessage(String msg) { // 아래박스에 메세지 전달
		DataShip data = new DataShip(DataShip.PRINT_MESSAGE);
		data.setMsg(name + "(" + index + "P)>" + msg);
		broadcast(data);
	}

	public void closeNetwork() { // 연결을 끊는 상황 -> 리스트에서 제거
		DataShip data = new DataShip(DataShip.CLOSE_NETWORK);
		indexList.add(index);

		int tmp;
		if (indexList.size() > 1) {
			for (int i = 0; i < indexList.size() - 1; i++) {
				if (indexList.get(i) > indexList.get(i + 1)) {
					tmp = indexList.get(i + 1);
					indexList.remove(i + 1);
					indexList.add(i, new Integer(tmp));
				}
			}
		}
		send(data);
	}

	public void exitServer() { // ?
		DataShip data = new DataShip(DataShip.SERVER_EXIT);
		broadcast(data);
	}

	public void gameStart(int speed) { // 게임시작
		isStartGame = true;
		totalAdd = 0;
		maxRank = list.size(); // 핸들러의 개수 반환 -> 의미?
		DataShip data = new DataShip(DataShip.GAME_START);
		data.setPlay(true); // 시작 설정
		data.setSpeed(speed); // 속도 설정
		data.setMsg("<Game Start>"); // 메세지 설정
		broadcast(data); // 관련정보 모두 전송
		for (int i = 0; i < list.size(); i++) {
			GameHandler handler = list.get(i);
			handler.setRank(0); // 핸들러마다 랭크 0으로 설정
		}
	}

	public void printSystemOpenMessage() { // 처음 open했을 때 메세지 전달
		DataShip data = new DataShip(DataShip.PRINT_SYSTEM_MESSAGE); // 시스템 메세지 창에 출력
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) { // list에 담긴 정보를 버퍼에 추가
			sb.append("<" + list.get(i).index + "P> " + list.get(i).ip + ":" + list.get(i).name);
			if (i < list.size() - 1)
				sb.append("\n");
		}
		data.setMsg(sb.toString()); // 버퍼에 담긴 정보를 스트링으로 변환해 data에 추가
		send(data); // 출력
	}

	public void printSystemAddMemberMessage() { // 클라이언트 추가 시
		DataShip data = new DataShip(DataShip.PRINT_SYSTEM_MESSAGE); // 시스템 메세지 창에 출력
		data.setMsg("<" + index + "P> " + ip + ":" + name);
		broadcast(data);
	}

	public void printSystemWinMessage(int index) {
		DataShip data = new DataShip(DataShip.PRINT_SYSTEM_MESSAGE);
		data.setMsg(index + "P> WIN");
		broadcast(data);
	}

	public void printSystemMessage(String msg) { // 시스템메세지 공간에 전체 공지
		DataShip data = new DataShip(DataShip.PRINT_SYSTEM_MESSAGE);
		data.setMsg(msg);
		broadcast(data);
	}

	public void addBlock(int numOfBlock) { // 상대에게 추가한 블럭에 관한 메소드
		DataShip data = new DataShip(DataShip.ADD_BLOCK);
		data.setNumOfBlock(numOfBlock); // 블럭 수 설정
		data.setMsg(index + "P -> ADD:" + numOfBlock); // 관련 메세지 설정
		data.setIndex(index);
		totalAdd += numOfBlock; // 여태까지 추가한 블럭 수
		broadcast(data);
	}

	public void setIndex() {
		DataShip data = new DataShip(DataShip.SET_INDEX);
		data.setIndex(index);
		send(data);
	}

	public void gameover(int rank) {
		DataShip data = new DataShip(DataShip.GAME_OVER);
		data.setMsg(index + "P -> OVER:" + rank); // 종료되기전까지 있었던 플레이어 수 출력 -> 등수
		data.setIndex(index);
		data.setPlay(false);
		data.setRank(rank);
		data.setTotalAdd(totalAdd);
		broadcast(data);

		if (rank == 2) { // 게임이 끝난 상황
			isStartGame = false;
			for (int i = 0; i < list.size(); i++) {
				GameHandler handler = list.get(i);
				if (handler.getRank() == 0) { // rank 변하지 않은 상태 -> game over 되지 않은 상태
					handler.win();
				}
			}
		}
	}

	public void win() { // 이긴 경우
		DataShip data = new DataShip(DataShip.GAME_WIN);
		data.setMsg(index + "P -> WIN");
		data.setTotalAdd(totalAdd);
		broadcast(data);
	}

	private void send(DataShip dataShip) { // 객체 하나에만 dataShip 출력
		try {
			oos.writeObject(dataShip); // DataShip 객체 출력
			oos.flush(); // 버퍼의 모든 내용 출력
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void broadcast(DataShip dataShip) { // list에 있는 handler마다 dataShip 출력
		for (int i = 0; i < list.size(); i++) {
			GameHandler handler = list.get(i);
			if (handler != null) {
				try {
					handler.getOOS().writeObject(dataShip);
					handler.getOOS().flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}// broadcast

	public ObjectOutputStream getOOS() {
		return oos;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public boolean isPlay() {
		return isStartGame;
	}
}// GameHandler

public class GameServer implements Runnable {
	private ServerSocket ss;
	private ArrayList<GameHandler> list = new ArrayList<GameHandler>();
	private ArrayList<Integer> indexList = new ArrayList<Integer>();
	private int index = 1;

	public GameServer(int port) {
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}// GameServer()

	public void startServer() {
		System.out.println("server is working");
		index = 1;
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			while (true) {
				synchronized (GameServer.class) {

					Socket socket = ss.accept();
					int index;
					if (indexList.size() > 0) {
						index = indexList.get(0);
						indexList.remove(0);
					} else
						index = this.index++;
					GameHandler handler = new GameHandler(socket, list, index, indexList);
					list.add(handler);
					handler.start();

				}
			} // while(true)

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}// GameServer