package com.tetris.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.tetris.classes.Block;
import com.tetris.classes.TetrisBlock;
import com.tetris.controller.TetrisController;
import com.tetris.network.GameClient;
import com.tetris.shape.CenterUp;
import com.tetris.shape.LeftTwoUp;
import com.tetris.shape.LeftUp;
import com.tetris.shape.Line;
import com.tetris.shape.Nemo;
import com.tetris.shape.RightTwoUp;
import com.tetris.shape.RightUp;

public class TetrisBoard extends JPanel implements Runnable, KeyListener, MouseListener, ActionListener{
	private static final long serialVersionUID = 1L;
	
	private Tetris tetris;
	private GameClient client;

	public static final int BLOCK_SIZE = 20;
	public static final int BOARD_X = 120;
	public static final int BOARD_Y = 50;
	private int minX=1, minY=0, maxX=10, maxY=21, down=50, up=0
			;
	private final int MESSAGE_X = 2;
	private final int MESSAGE_WIDTH = BLOCK_SIZE * (7 + minX);
	private final int MESSAGE_HEIGHT = BLOCK_SIZE * (6 + minY);
	private final int PANEL_WIDTH = maxX*BLOCK_SIZE + MESSAGE_WIDTH + BOARD_X;
	private final int PANEL_HEIGHT = maxY*BLOCK_SIZE + MESSAGE_HEIGHT + BOARD_Y;
	
	private SystemMessageArea systemMsg = new SystemMessageArea(BLOCK_SIZE*1,BOARD_Y + BLOCK_SIZE + BLOCK_SIZE*7, BLOCK_SIZE*5, BLOCK_SIZE*12);
	private MessageArea messageArea = new MessageArea(this,2, PANEL_HEIGHT - (MESSAGE_HEIGHT-MESSAGE_X), PANEL_WIDTH-BLOCK_SIZE*7-2, MESSAGE_HEIGHT-2);
	private JButton btnStart = new JButton("시작하기");
	private JButton btnExit = new JButton("나가기");
	private JCheckBox checkGhost = new JCheckBox("고스트모드",true);
	private JCheckBox checkGrid  = new JCheckBox("격자 표시",true);
	private Integer[] lv = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
	private JComboBox<Integer> comboSpeed = new JComboBox<Integer>(lv);
	
	private String ip;
	private int port;
	private String nickName;
	private Thread th;
	private ArrayList<Block> blockList;
	private ArrayList<TetrisBlock> nextBlocks;
	private TetrisBlock shap;
	private TetrisBlock ghost;
	private TetrisBlock hold;
	private Block[][] map;
	private TetrisController controller;
	private TetrisController controllerGhost;
	
	private boolean isPlay = false;
	private boolean isHold = false;
	private boolean usingGhost = true;
	private boolean usingGrid = true;
	private int removeLineCount = 0;
	private int removeLineCombo = 0;
	
	public TetrisBoard(Tetris tetris, GameClient client) {
		this.tetris = tetris;
		this.client = client;
		this.setPreferredSize(new Dimension(PANEL_WIDTH,PANEL_HEIGHT));
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.setLayout(null);
		this.setFocusable(true);
		
		btnStart.setBounds(PANEL_WIDTH - BLOCK_SIZE*7, PANEL_HEIGHT - messageArea.getHeight(), BLOCK_SIZE*7, messageArea.getHeight()/2);
		btnStart.setFocusable(false);
		btnStart.setEnabled(false);
		btnStart.addActionListener(this);
		btnExit.setBounds(PANEL_WIDTH - BLOCK_SIZE*7, PANEL_HEIGHT - messageArea.getHeight()/2, BLOCK_SIZE*7, messageArea.getHeight()/2);
		btnExit.setFocusable(false);	
		btnExit.addActionListener(this);
		checkGhost.setBounds(PANEL_WIDTH - BLOCK_SIZE*7+35,5,95,20);
		checkGhost.setBackground(new Color(0,87,102));
		checkGhost.setForeground(Color.WHITE);
		checkGhost.setFont(new Font("굴림", Font.BOLD,13));
		checkGhost.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				usingGhost = checkGhost.isSelected();
				TetrisBoard.this.setRequestFocusEnabled(true);
				TetrisBoard.this.repaint();
			}
		});
		checkGrid.setBounds(PANEL_WIDTH - BLOCK_SIZE*7+35,25,95,20);
		checkGrid.setBackground(new Color(0,87,102));
		checkGrid.setForeground(Color.WHITE);
		checkGrid.setFont(new Font("굴림", Font.BOLD,13));
		checkGrid.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				usingGrid = checkGrid.isSelected();
				TetrisBoard.this.setRequestFocusEnabled(true);
				TetrisBoard.this.repaint();
			}
		});
		comboSpeed.setBounds(PANEL_WIDTH - BLOCK_SIZE*8, 5, 45, 20);
		this.add(comboSpeed);
		
		this.add(systemMsg);
		this.add(messageArea);
		this.add(btnStart);		
		this.add(btnExit);
		this.add(checkGhost);
		this.add(checkGrid);
	}
	
	public void startNetworking(String ip, int port, String nickName){
		this.ip = ip;
		this.port = port;
		this.nickName = nickName;
		this.repaint();
	}
	
	
	
	public void gameStart(int speed){
		comboSpeed.setSelectedItem(new Integer(speed));
		
		if(th!=null){
			try {isPlay = false;th.join();} 
			catch (InterruptedException e) {e.printStackTrace();}
		}
		
		
		map = new Block[maxY][maxX];
		blockList = new ArrayList<Block>();
		nextBlocks = new ArrayList<TetrisBlock>();
		
		
		shap = getRandomTetrisBlock();
		ghost = getBlockClone(shap,true);
		hold = null;
		isHold = false;
		controller = new TetrisController(shap,maxX-1,maxY-1,map);
		controllerGhost = new TetrisController(ghost,maxX-1,maxY-1,map);
		this.showGhost();
		for(int i=0 ; i<5 ; i++){
			nextBlocks.add(getRandomTetrisBlock());
		}
		
		
		isPlay = true;
		th = new Thread(this);
		th.start();
	}
	
	
	
	@Override
	protected void paintComponent(Graphics g) {
		g.clearRect(0, 0, this.getWidth(), this.getHeight()+1);
		

		g.setColor(new Color(0,87,102));
		g.fillRect(0, 0, (maxX+minX+13)*BLOCK_SIZE+1, BOARD_Y);
		
		g.setColor(new Color(92,109,129));
		g.fillRect(0, BOARD_Y, (maxX+minX+13)*BLOCK_SIZE+1, maxY*BLOCK_SIZE+1);
		g.setColor(Color.WHITE);
		
		
		g.drawString("ip : "+ip+"     port : "+port, 20, 20);
		
		
		g.drawString("닉네임 : "+nickName, 20, 40);
		
		
		Font font= g.getFont();
		g.setFont(new Font("굴림", Font.BOLD,13));
		g.drawString("속도", PANEL_WIDTH - BLOCK_SIZE*10, 20);
		g.setFont(font);
		
		g.setColor(Color.BLACK);
		g.fillRect(BOARD_X + BLOCK_SIZE*minX, BOARD_Y, maxX*BLOCK_SIZE+1, maxY*BLOCK_SIZE+1);
		g.fillRect(BLOCK_SIZE*minX ,BOARD_Y + BLOCK_SIZE, BLOCK_SIZE*5,BLOCK_SIZE*5);
		g.fillRect(BOARD_X + BLOCK_SIZE*minX + (maxX+1)*BLOCK_SIZE+1,BOARD_Y + BLOCK_SIZE, BLOCK_SIZE*5,BLOCK_SIZE*5);
		g.fillRect(BOARD_X + BLOCK_SIZE*minX + (maxX+1)*BLOCK_SIZE+1,BOARD_Y + BLOCK_SIZE + BLOCK_SIZE*7, BLOCK_SIZE*5,BLOCK_SIZE*12);
		
		
		g.setFont(new Font(font.getFontName(),font.getStyle(),20));
		g.drawString("H O L D", BLOCK_SIZE + 12, BOARD_Y + BLOCK_SIZE + BLOCK_SIZE*5 + 20);
		g.drawString("N E X T", BOARD_X + BLOCK_SIZE + (maxX+1)*BLOCK_SIZE+1 + 12, BOARD_Y + BLOCK_SIZE + BLOCK_SIZE*5 + 20);
		g.setFont(font);
		
		
		if(usingGrid){
			g.setColor(Color.darkGray);
			for(int i=1;i<maxY;i++) g.drawLine(BOARD_X + BLOCK_SIZE*minX, BOARD_Y+BLOCK_SIZE*(i+minY), BOARD_X + (maxX+minX)*BLOCK_SIZE, BOARD_Y+BLOCK_SIZE*(i+minY));
			for(int i=1;i<maxX;i++) g.drawLine(BOARD_X + BLOCK_SIZE*(i+minX), BOARD_Y+BLOCK_SIZE*minY, BOARD_X + BLOCK_SIZE*(i+minX), BOARD_Y+BLOCK_SIZE*(minY+maxY));
			for(int i=1;i<5;i++) g.drawLine(BLOCK_SIZE*minX ,BOARD_Y + BLOCK_SIZE*(i+1), BLOCK_SIZE*(minX+5)-1,BOARD_Y + BLOCK_SIZE*(i+1));
			for(int i=1;i<5;i++) g.drawLine(BLOCK_SIZE*(minY+i+1) ,BOARD_Y + BLOCK_SIZE, BLOCK_SIZE*(minY+i+1),BOARD_Y + BLOCK_SIZE*(minY+6)-1);
			for(int i=1;i<5;i++) g.drawLine(BOARD_X + BLOCK_SIZE*minX + (maxX+1)*BLOCK_SIZE+1, BOARD_Y + BLOCK_SIZE*(i+1), BOARD_X + BLOCK_SIZE*minX + (maxX+1)*BLOCK_SIZE+BLOCK_SIZE*5,BOARD_Y + BLOCK_SIZE*(i+1));
			for(int i=1;i<5;i++) g.drawLine(BOARD_X + BLOCK_SIZE*minX + (maxX+1+i)*BLOCK_SIZE+1, BOARD_Y + BLOCK_SIZE, BOARD_X + BLOCK_SIZE*minX + BLOCK_SIZE+BLOCK_SIZE*(10+i)+1,BOARD_Y + BLOCK_SIZE*6-1);	
		}
		
		int x=0,y=0,newY=0;
		if(hold!=null){
			x=0; y=0; newY=3;
			x = hold.getPosX();
			y = hold.getPosY();
			hold.setPosX(-4+minX);
			hold.setPosY(newY+minY);
			hold.drawBlock(g);
			hold.setPosX(x);
			hold.setPosY(y);
		}
		
		if(nextBlocks!=null){
			x=0; y=0; newY=3;
			for(int i = 0 ; i<nextBlocks.size() ; i++){
				TetrisBlock block = nextBlocks.get(i);
				x = block.getPosX();
				y = block.getPosY();
				block.setPosX(13+minX);
				block.setPosY(newY+minY);
				if(newY==3) newY=6;
				block.drawBlock(g);
				block.setPosX(x);
				block.setPosY(y);
				newY+=3;
			}
		}
		
		if(blockList!=null){
			x=0; y=0;
			for(int i = 0 ; i<blockList.size() ; i++){
				Block block = blockList.get(i);
				x = block.getPosGridX();
				y = block.getPosGridY();
				block.setPosGridX(x+minX);
				block.setPosGridY(y+minY);
				block.drawColorBlock(g);
				block.setPosGridX(x);
				block.setPosGridY(y);
			}
		}

		if(ghost!=null){

			if(usingGhost){
				x=0; y=0;
				x = ghost.getPosX();
				y = ghost.getPosY();
				ghost.setPosX(x+minX);
				ghost.setPosY(y+minY);
				ghost.drawBlock(g);
				ghost.setPosX(x);
				ghost.setPosY(y);
			}
		}
		
		if(shap!=null){
			x=0; y=0;
			x = shap.getPosX();
			y = shap.getPosY();
			shap.setPosX(x+minX);
			shap.setPosY(y+minY);
			shap.drawBlock(g);
			shap.setPosX(x);
			shap.setPosY(y);
		}
	}
	
	@Override
	public void run() {
		int countMove = (21-(int)comboSpeed.getSelectedItem())*5;
		int countDown = 0;
		int countUp = up;
		
		while(isPlay){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(countDown!=0){
				countDown--;
				if(countDown==0){
					
					if(controller!=null && !controller.moveDown()) this.fixingTetrisBlock();
				}
				this.repaint();
				continue;
			}
			
			countMove--;
			if (countMove == 0) {
				countMove = (21-(int)comboSpeed.getSelectedItem())*5;
				if (controller != null && !controller.moveDown()) countDown = down;
				else this.showGhost();
			}
			
			if (countUp != 0) {
				countUp--;
				if (countUp == 0) {
					countUp = up;
					addBlockLine(1);
				}
			}
			
			this.repaint();
		}//while()
	}//run()

	
	
	public void dropBoard(int lineNumber, int num){
		
		
		this.dropMap(lineNumber,num);
		
		
		this.changeTetrisBlockLine(lineNumber,num);
		
		
		this.checkMap();
		
		
		this.showGhost();
	}
	
	
	
	private void dropMap(int lineNumber, int num) {
		if(num==1){
			
			for(int i= lineNumber ; i>0 ;i--){
				for(int j=0 ; j<map[i].length ;j++){
					map[i][j] = map[i-1][j];
				}
			}
			
			
			for(int j=0 ; j<map[0].length ;j++){
				map[0][j] = null;
			}
		}
		else if(num==-1){
			
			for(int i= 1 ; i<=lineNumber ;i++){
				for(int j=0 ; j<map[i].length ;j++){
					map[i-1][j] = map[i][j];
				}
			}
			
			
			for(int j=0 ; j<map[0].length ;j++){
				map[lineNumber][j] = null;
			}
		}
	}
	
	
	
	private void changeTetrisBlockLine(int lineNumber, int num){
		int y=0, posY=0;
		for(int i=0 ; i<blockList.size() ; i++){
			y = blockList.get(i).getY();
			posY = blockList.get(i).getPosGridY();
			if(y<=lineNumber)blockList.get(i).setPosGridY(posY + num);
		}
	}

	
	
	private void fixingTetrisBlock() {
		synchronized (this) {
			if(stop){
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		boolean isCombo = false;
		removeLineCount = 0;
		
		
		for (Block block : shap.getBlock()) {
			blockList.add(block);
		}
		
		isCombo = checkMap();

		if(isCombo) removeLineCombo++;
		else removeLineCombo = 0;
		
		
		this.getFixBlockCallBack(blockList,removeLineCombo,removeLineCount);
		
		
		this.nextTetrisBlock();
		
		
		isHold = false;
	}//fixingTetrisBlock()
	
	
	
	private boolean checkMap(){
		boolean isCombo = false;
		int count = 0;
		Block mainBlock;
		
		for(int i=0 ; i<blockList.size() ;i++){
			mainBlock = blockList.get(i);
			
			
			if(mainBlock.getY()<0 || mainBlock.getY() >=maxY) continue;
			
			if(mainBlock.getY()<maxY && mainBlock.getX()<maxX) 
				map[mainBlock.getY()][mainBlock.getX()] = mainBlock;

			
			if (mainBlock.getY() == 1 && mainBlock.getX() > 2 && mainBlock.getX() < 7) {
				this.gameEndCallBack();
				break;
			}
			
			
			count = 0;
			for (int j = 0; j < maxX; j++) {
				if(map[mainBlock.getY()][j] != null) count++;
				
			}
			
			
			if (count == maxX) {
				removeLineCount++;
				this.removeBlockLine(mainBlock.getY());
				isCombo = true;
			}
		}
		return isCombo;
	}
	
	
	public void nextTetrisBlock(){
		shap = nextBlocks.get(0);
		this.initController();
		nextBlocks.remove(0);
		nextBlocks.add(getRandomTetrisBlock());
	}
	private void initController(){
		controller.setBlock(shap);
		ghost = getBlockClone(shap,true);
		controllerGhost.setBlock(ghost);
	}
	
	
	
	private void removeBlockLine(int lineNumber) {
		
		for (int j = 0; j < maxX ; j++) {
			for (int s = 0; s < blockList.size(); s++) {
				Block b = blockList.get(s);
				if (b == map[lineNumber][j])
					blockList.remove(s);
			}
			map[lineNumber][j] = null;
		}// for(j)

		this.dropBoard(lineNumber,1);
	}
	
	
	
	public void gameEndCallBack(){
		client.gameover();
		this.isPlay = false;
	}
	
	
	
	private void showGhost(){
		ghost = getBlockClone(shap,true);
		controllerGhost.setBlock(ghost);
		controllerGhost.moveQuickDown(shap.getPosY(), true);
	}	
	
	
	
	public TetrisBlock getRandomTetrisBlock(){
		switch((int)(Math.random()*7)){
		case TetrisBlock.TYPE_CENTERUP : return new CenterUp(4, 1);
		case TetrisBlock.TYPE_LEFTTWOUP : return new LeftTwoUp(4, 1);
		case TetrisBlock.TYPE_LEFTUP : return new LeftUp(4, 1);
		case TetrisBlock.TYPE_RIGHTTWOUP : return new RightTwoUp(4, 1);
		case TetrisBlock.TYPE_RIGHTUP : return new RightUp(4, 1);
		case TetrisBlock.TYPE_LINE : return new Line(4, 1);
		case TetrisBlock.TYPE_NEMO : return new Nemo(4, 1);
		}
		return null;
	}
	
	
	
	public TetrisBlock getBlockClone(TetrisBlock tetrisBlock, boolean isGhost){
		TetrisBlock blocks = null;
		switch(tetrisBlock.getType()){
		case TetrisBlock.TYPE_CENTERUP : blocks =  new CenterUp(4, 1); break;
		case TetrisBlock.TYPE_LEFTTWOUP : blocks =  new LeftTwoUp(4, 1); break;
		case TetrisBlock.TYPE_LEFTUP : blocks =  new LeftUp(4, 1); break;
		case TetrisBlock.TYPE_RIGHTTWOUP : blocks =  new RightTwoUp(4, 1); break;
		case TetrisBlock.TYPE_RIGHTUP : blocks =  new RightUp(4, 1); break;
		case TetrisBlock.TYPE_LINE : blocks =  new Line(4, 1); break;
		case TetrisBlock.TYPE_NEMO : blocks =  new Nemo(4, 1); break;
		}
		if(blocks!=null && isGhost){
			blocks.setGhostView(isGhost);
			blocks.setPosX(tetrisBlock.getPosX());
			blocks.setPosY(tetrisBlock.getPosY());
			blocks.rotation(tetrisBlock.getRotationIndex());
		}
		return blocks;
	}	
	
	
	
	public void getFixBlockCallBack(ArrayList<Block> blockList, int removeCombo, int removeMaxLine){
		if(removeCombo<3){
			if(removeMaxLine==3)client.addBlock(1);
			else if(removeMaxLine==4)client.addBlock(3);
		}else if(removeCombo<10){
			if(removeMaxLine==3)client.addBlock(2);
			else if(removeMaxLine==4)client.addBlock(4);
			else client.addBlock(1);
		}else{
			if(removeMaxLine==3)client.addBlock(3);
			else if(removeMaxLine==4)client.addBlock(5);
			else client.addBlock(2);
		}
	}
	
	
	public void playBlockHold(){
		if(isHold) return;
		
		if(hold==null){
			hold = getBlockClone(shap,false);
			this.nextTetrisBlock();
		}else{
			TetrisBlock tmp = getBlockClone(shap,false);
			shap = getBlockClone(hold,false);
			hold = getBlockClone(tmp,false);
			this.initController();
		}
		
		isHold = true;
	}
	
	
	
	boolean stop = false;
	public void addBlockLine(int numOfLine){
		stop = true;
		
		Block block;
		int rand = (int) (Math.random() * maxX);
		for (int i = 0; i < numOfLine; i++) {
			this.dropBoard(maxY - 1, -1);
			for (int col = 0; col < maxX; col++) {
				if (col != rand) {
					block = new Block(0, 0, Color.GRAY, Color.GRAY);
					block.setPosGridXY(col, maxY - 1);
					blockList.add(block);
					map[maxY - 1][col] = block;
				}
			}
			
			boolean up = false;
			for(int j=0 ; j<shap.getBlock().length ; j++){
				Block sBlock = shap.getBlock(j);
				if(map[sBlock.getY()][sBlock.getX()]!=null){
					up = true;
					break;
				}
			}
			if(up){
				controller.moveDown(-1);
			}
		}
		
		
		
		
		this.showGhost();
		this.repaint();
		synchronized (this) {
			stop = false;
			this.notify();
		}
	}
	
	
	
	
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			messageArea.requestFocus();
		}
		if(!isPlay) return;
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			controller.moveLeft();
			controllerGhost.moveLeft();
		}else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			controller.moveRight();
			controllerGhost.moveRight();
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN){
			controller.moveDown();
		}else if(e.getKeyCode() == KeyEvent.VK_UP){
			controller.nextRotationLeft();
			controllerGhost.nextRotationLeft();
		}else if(e.getKeyCode() == KeyEvent.VK_SPACE){
			controller.moveQuickDown(shap.getPosY(), true);
			this.fixingTetrisBlock();
		}else if(e.getKeyCode() == KeyEvent.VK_SHIFT){ 
			playBlockHold();
		}
		this.showGhost();
		this.repaint();
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		this.requestFocus();
	}
	public void mouseReleased(MouseEvent e) {}
	
	
	
	

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnStart){
			if(client!=null){
				client.gameStart((int)comboSpeed.getSelectedItem());
			}else{
				this.gameStart((int)comboSpeed.getSelectedItem());
			}
		}else if(e.getSource() == btnExit){
			if(client!=null ){
				if(tetris.isNetwork()){
					client.closeNetwork(tetris.isServer());
				}
			}else{
				System.exit(0);
			}
			
		}
	}

	public boolean isPlay(){return isPlay;}
	public void setPlay(boolean isPlay){this.isPlay = isPlay;}
	public JButton getBtnStart() {return btnStart;}
	public JButton getBtnExit() {return btnExit;}
	public void setClient(GameClient client) {this.client = client;}
	public void printSystemMessage(String msg){systemMsg.printMessage(msg);}
	public void printMessage(String msg){messageArea.printMessage(msg);}
	public GameClient getClient(){return client;}
	public void changeSpeed(Integer speed) {comboSpeed.setSelectedItem(speed);}
	public void clearMessage() {
		messageArea.clearMessage();
		systemMsg.clearMessage();
	}

}
