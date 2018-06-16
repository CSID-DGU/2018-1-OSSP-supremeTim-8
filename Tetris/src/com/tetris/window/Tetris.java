package com.tetris.window;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import com.tetris.network.DB;
import com.tetris.network.GameClient;
import com.tetris.network.GameServer;




public class Tetris extends JFrame implements ActionListener, MenuListener {

	private static final long serialVersionUID = 1L;
	private GameServer server;
	private GameClient client;
	private TetrisBoard board = new TetrisBoard(this, client);
	private JMenuItem itemServerStart = new JMenuItem("서버로 접속하기");
	private JMenuItem itemClientStart = new JMenuItem("클라이언트로 접속하기");

	private JMenu mnRank=new JMenu("랭킹보기") ;
	
	private boolean isNetwork;
	private boolean isServer;

	public Tetris() {
		JMenuBar mnBar = new JMenuBar();
		JMenu mnGame = new JMenu("게임하기");


		mnGame.add(itemServerStart);
		mnGame.add(itemClientStart);
		mnBar.add(mnGame);

		mnGame.add(itemServerStart);
		mnGame.add(itemClientStart);
		mnBar.add(mnGame);
		mnBar.add(mnRank);
		mnBar.setSize(10,100);

		this.setJMenuBar(mnBar);

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.getContentPane().add(board);

		this.setResizable(false);
		this.pack();
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((size.width - this.getWidth()) / 2, (size.height - this.getHeight()) / 2);
		this.setVisible(true);

		itemServerStart.addActionListener(this);
		itemClientStart.addActionListener(this);
		mnRank.addMenuListener(this);
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				if (client != null) {

					if (isNetwork) {
						client.closeNetwork(isServer);
					}
				} else {
					System.exit(0);
				}

			}

		});

	}

	@Override
	public void menuSelected(MenuEvent e) {
		if(e.getSource()==mnRank) {
			new menu_rank();
		}
	}
	public void menuDeselected(MenuEvent e) {
	}
	public void menuCanceled(MenuEvent e) {
	 }
	public void actionPerformed(ActionEvent e) {

		String ip = null;
		int port = 0;
		String nickName = null;
		if (e.getSource() == itemServerStart) { // 서버로 시작하기 눌렀을 때

			String sp = JOptionPane.showInputDialog("port번호를 입력해주세요", "9500");
			if (sp != null && !sp.equals(""))
				port = Integer.parseInt(sp);
			nickName = JOptionPane.showInputDialog("닉네임을 입력해주세요", "이름없음");

			if (port != 0) {
				if (server == null)
					server = new GameServer(port); // 서버 클라이언트 소켓 생성
				server.startServer();
				try {
					ip = InetAddress.getLocalHost().getHostAddress();
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				}
				if (ip != null) {
					client = new GameClient(this, ip, port, nickName);
					if (client.start()) {
						itemServerStart.setEnabled(false);
						itemClientStart.setEnabled(false);
						board.setClient(client); // 클라이언트 설정
						board.getBtnStart().setEnabled(true);
						board.startNetworking(ip, port, nickName); // 클라이언트의 보드에 각 정보 설정
						isNetwork = true;
						isServer = true;
					}
				}
			}
		} else if (e.getSource() == itemClientStart) { // 클라이언트로 시작하기 눌렀을 때
			try {
				ip = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			}

			ip = JOptionPane.showInputDialog("IP를 입력해주세요.", ip);
			String sp = JOptionPane.showInputDialog("port번호를 입력해주세요", "9500");
			if (sp != null && !sp.equals(""))
				port = Integer.parseInt(sp);
			nickName = JOptionPane.showInputDialog("닉네임을 입력해주세요", "이름없음");

			if (ip != null) {
				client = new GameClient(this, ip, port, nickName); // 클라이언트 소켓 생성
				if (client.start()) {
					itemServerStart.setEnabled(false);
					itemClientStart.setEnabled(false);
					board.setClient(client);
					board.startNetworking(ip, port, nickName);
					isNetwork = true;
					board.isMulti=true;
				}
			}
		}
	}

	public void closeNetwork() {
		isNetwork = false;
		client = null;
		itemServerStart.setEnabled(true);
		itemClientStart.setEnabled(true);
		board.setPlay(false);
		board.setClient(null);
	}

	public JMenuItem getItemServerStart() {
		return itemServerStart;
	}

	public JMenuItem getItemClientStart() {
		return itemClientStart;
	}

	public TetrisBoard getBoard() {
		return board;
	}

	public void gameStart(int speed) {
		board.gameStart(speed);
	}

	public boolean isNetwork() {
		return isNetwork;
	}

	public void setNetwork(boolean isNetwork) {
		this.isNetwork = isNetwork;
	}

	public void printSystemMessage(String msg) {
		board.printSystemMessage(msg);
	}

	public void printMessage(String msg) {
		board.printMessage(msg);
	}

	public boolean isServer() {
		return isServer;
	}

	public void setServer(boolean isServer) {
		this.isServer = isServer;
	}

	public void clientChangeSpeed(Integer speed) {
		client.reChangSpeed(speed);
	}

	public void changeSpeed(Integer speed) {
		board.changeSpeed(speed);
	}
	
	public int getLevel() {
		return board.getLevel();
	}
}
class menu_rank extends JFrame implements MenuListener{
	JMenu ch1 = new JMenu("일반게임");
	JMenu ch2 = new JMenu("젓가락 행진곡");
	JMenu ch3 = new JMenu("what is love?");
	public menu_rank() {
		JMenuBar chBar = new JMenuBar();
		chBar.add(ch1);
		chBar.add(ch2);
		chBar.add(ch3);
		ch1.addMenuListener(this);
		ch2.addMenuListener(this);
		ch3.addMenuListener(this);
		chBar.setSize(10,70);

		this.setJMenuBar(chBar);
		this.setSize(500,100);
		//화면 중앙에 띄우기
				Dimension size = Toolkit.getDefaultToolkit().getScreenSize(); //스크린전체사이즈
				this.setLocation((size.width-this.getWidth())/2,(size.height-this.getHeight())/2-200); //스크린 중앙에 위치 
				this.setVisible(true);
	}

	@Override
	public void menuSelected(MenuEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==ch1)
			new DB(0,0,false);
		else if(e.getSource()==ch2)
			new DB(1,0,false);
		else if(e.getSource()==ch3)
			new DB(2,0,false);
	}	
		@Override
		public void menuCanceled(MenuEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void menuDeselected(MenuEvent e) {
			// TODO Auto-generated method stub
	
	}
}
