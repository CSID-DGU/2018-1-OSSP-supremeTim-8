package com.tetris.rhythm;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import com.tetris.window.*;
import com.tetris.rhythm.*;

public class RhythmGame extends JDialog {

	private ImageIcon leftButtonEnteredImage = new ImageIcon("./src/image/left entered image.png");
	private ImageIcon leftButtonBasicImage = new ImageIcon("./src/image/left basic image.png");
	private ImageIcon rightButtonEnteredImage = new ImageIcon("./src/image/right entered image.png");
	private ImageIcon rightButtonBasicImage = new ImageIcon("./src/image/right basic image.png");
	private JButton leftButton = new JButton();
	private JButton rightButton = new JButton();
	private JButton selectButton = new JButton("곡 선택");

	ArrayList<Track> trackList = new ArrayList<Track>();

	private ImageIcon gameImage;
	private Music selectMusic; // 곡의 하이라이트
	private Music gameMusic; // 곡
	private int nowSelected = 0;
<<<<<<< HEAD
	
=======

>>>>>>> 921c52889ef6edf75c11f0b70a163dbeee66488a
	public RhythmGame() {
		
		selecteMusic(); // 곡선택
		
	}

	public void selecteMusic() {

		trackList.add(new Track("젓가락 행진곡", "./src/image/chopstick image.jpg", "chopstick start music.mp3",
				"chopstick music.mp3"));
		trackList.add(new Track("What is Love?", "./src/image/what is love image.jpg", "what is love start music.mp3",
				"what is love music.mp3"));
		selectTrack(0);

		this.setTitle("리듬게임 곡 선택");
		this.setSize(400, 400);
		this.setLocation(100, 100);
		this.setModal(true);

		//leftButton.setBounds(60, 60, 60, 60);
		leftButton.setBorderPainted(false);
		leftButton.setContentAreaFilled(false);
		leftButton.setFocusPainted(false);
		leftButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				leftButton.setIcon(leftButtonEnteredImage);
				leftButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			public void mouseExited(MouseEvent e) {
				leftButton.setIcon(leftButtonBasicImage);
				leftButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			public void mousePressed(MouseEvent e) {
				selectLeft();
			}
		});

		//rightButton.setBounds(140, 310, 60, 60);
		rightButton.setBorderPainted(false);
		rightButton.setContentAreaFilled(false);
		rightButton.setFocusPainted(false);
		rightButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				rightButton.setIcon(rightButtonEnteredImage);
				rightButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			public void mouseExited(MouseEvent e) {
				rightButton.setIcon(rightButtonBasicImage);
				rightButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			public void mousePressed(MouseEvent e) {
				selectRight();
			}
		});

		selectButton.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e) {
				selectMusic.close(); // 음악 종료
<<<<<<< HEAD
				
				dispose(); // JDialog 종료
=======
				dispose(); // JDialog 종료
				gameMusic.start();
>>>>>>> 921c52889ef6edf75c11f0b70a163dbeee66488a
				
			}
		});
		
		add(leftButton, BorderLayout.WEST);
		add(rightButton, BorderLayout.EAST);
		add(selectButton, BorderLayout.SOUTH);
		this.setVisible(true);
	}

	public void paint(Graphics g) {

		super.paintComponents(g);
		Image image = gameImage.getImage();
		g.drawImage(image, 100, 100, null);
		g.drawString(trackList.get(nowSelected).getTitle(), 160, 300);

<<<<<<< HEAD

=======
>>>>>>> 921c52889ef6edf75c11f0b70a163dbeee66488a
	}

	public void selectTrack(int nowSelected) {
		if (selectMusic != null)
			selectMusic.close();
		gameImage = new ImageIcon(trackList.get(nowSelected).getGameImage());
		selectMusic = new Music(trackList.get(nowSelected).getStartMusic(), true);
		selectMusic.start();
		gameMusic = new Music(trackList.get(nowSelected).getGameMusic(), true);

	}

	public void selectLeft() {
		if (nowSelected == 0)
			nowSelected = trackList.size() - 1;
		else
			nowSelected--;
		selectTrack(nowSelected);
		this.repaint();
	}

	public void selectRight() {
		if (nowSelected == trackList.size() - 1)
			nowSelected = 0;
		else
			nowSelected++;
		selectTrack(nowSelected);
		this.repaint();
	}
<<<<<<< HEAD
	
	public void gameStart(int nowSelected) {
		if(selectMusic != null) 
			selectMusic.close();
		
	}
	
	public Music getGameMusic(){
		return gameMusic;
	}
	
	public String getGameTitle() {
		return trackList.get(nowSelected).getTitle();
	}
=======
>>>>>>> 921c52889ef6edf75c11f0b70a163dbeee66488a
}
