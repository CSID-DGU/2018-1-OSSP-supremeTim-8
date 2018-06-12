package com.tetris.rhythm;

import java.awt.*;

import javax.swing.*;

public class Note {

	public static final int NOTE_SPEED = 7;
	public static final int SLEEP_TIME = 10;
	
	private Image noteImage = new ImageIcon("./src/image/note.png").getImage();
	private int x, y= 470 - 1000 / SLEEP_TIME * NOTE_SPEED;
	private int time;
	

	public Note(int x, int y, int time) {
		this.x = x;
		//this.y = y;
		this.time = time;
	}

	public void srcreenDraw(Graphics g) {
		g.drawImage(noteImage, x, y, null);
	}
	
	public void drop() {
		y += NOTE_SPEED;;
	}
	
	public int getTime() {
		return time;
	}
	public int getY() {
		return y;
	}
	
}
