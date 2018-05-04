package com.tetris.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class GameHandler extends Thread {
	private static boolean isStartGame;
	private static int maxRank;
	private int rank;

	private Socket socket;
	private ObjectInputStream ois; 
	private ObjectOutputStream oos; // 객체단위 입출력
	private String ip;
	private String name;
	private int index;
	private int totalAdd = 0;

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
			DataShip data = (DataShip) ois.readObject(); // 역직렬화
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

			if (data.getCommand() == DataShip.CLOSE_NETWORK) {
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
				rank = maxRank--;
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
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}// run

	public void printMessage(String msg) { // 모든 사용자에게 사용자 관련 정보 전달
		DataShip data = new DataShip(DataShip.PRINT_MESSAGE); // cmd 설정
		data.setMsg(name + "(" + index + "P)>" + msg);
		broadcast(data);
	}

	public void closeNetwork() {
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

	public void exitServer() {
		DataShip data = new DataShip(DataShip.SERVER_EXIT);
		broadcast(data);
	}

	public void gameStart(int speed) {
		isStartGame = true;
		totalAdd = 0;
		maxRank = list.size();
		DataShip data = new DataShip(DataShip.GAME_START);
		data.setPlay(true);
		data.setSpeed(speed);
		data.setMsg("<Game Start>");
		broadcast(data);
		for (int i = 0; i < list.size(); i++) {
			GameHandler handler = list.get(i);
			handler.setRank(0);
		}
	}

	public void printSystemOpenMessage() { // 리스트에 담긴 각 사용자의 정보 출력
		DataShip data = new DataShip(DataShip.PRINT_SYSTEM_MESSAGE); // cmd에 관련 정보 전달
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) { // list에 담긴 정보를 버퍼에 추가
			sb.append("<" + list.get(i).index + "P> " + list.get(i).ip + ":" + list.get(i).name);
			if (i < list.size() - 1)
				sb.append("\n");
		}
		data.setMsg(sb.toString()); // 버퍼에 담긴 정보를 스트링으로 변환해 data에 추가
		send(data); // 출력
	}

	public void printSystemAddMemberMessage() {
		DataShip data = new DataShip(DataShip.PRINT_SYSTEM_MESSAGE);
		data.setMsg("<" + index + "P> " + ip + ":" + name);
		broadcast(data);
	}

	public void printSystemWinMessage(int index) {
		DataShip data = new DataShip(DataShip.PRINT_SYSTEM_MESSAGE);
		data.setMsg(index + "P> WIN");
		broadcast(data);
	}

	public void printSystemMessage(String msg) {
		DataShip data = new DataShip(DataShip.PRINT_SYSTEM_MESSAGE);
		data.setMsg(msg);
		broadcast(data);
	}

	public void addBlock(int numOfBlock) {
		DataShip data = new DataShip(DataShip.ADD_BLOCK);
		data.setNumOfBlock(numOfBlock);
		data.setMsg(index + "P -> ADD:" + numOfBlock);
		data.setIndex(index);
		totalAdd += numOfBlock;
		broadcast(data);
	}

	public void setIndex() {
		DataShip data = new DataShip(DataShip.SET_INDEX);
		data.setIndex(index);
		send(data);
	}

	public void gameover(int rank) {
		DataShip data = new DataShip(DataShip.GAME_OVER);
		data.setMsg(index + "P -> OVER:" + rank);
		data.setIndex(index);
		data.setPlay(false);
		data.setRank(rank);
		data.setTotalAdd(totalAdd);
		broadcast(data);

		if (rank == 2) {
			isStartGame = false;
			for (int i = 0; i < list.size(); i++) {
				GameHandler handler = list.get(i);
				if (handler.getRank() == 0) {
					handler.win();
				}
			}
		}
	}

	public void win() {
		DataShip data = new DataShip(DataShip.GAME_WIN);
		data.setMsg(index + "P -> WIN");
		data.setTotalAdd(totalAdd);
		broadcast(data);
	}

	private void send(DataShip dataShip) { // 출력 메소드
		try {
			oos.writeObject(dataShip); // DataShip 객체 출력
			oos.flush(); // 버퍼의 모든 내용 출력
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void broadcast(DataShip dataShip) {
		for (int i = 0; i < list.size(); i++) { // 각 handler마다 dataShip 출력
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
