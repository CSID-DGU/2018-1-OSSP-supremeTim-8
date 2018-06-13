package com.tetris.rhythm;

import java.awt.*;
import java.util.*;

import javax.swing.*;

public class Note extends Thread{

	public static final int NOTE_SPEED = 5;
	public static final int SLEEP_TIME = 10;
	Thread t;
	
	private Image noteImage = new ImageIcon("./src/image/note.png").getImage();
	public int x, count, y =0;
	private int time;
	public boolean drop_judge=false;
	

	public Note(int x, int y, int time) {
		this.x = x;
		//this.y = y;
		this.time = time;
		t=new Thread(this);
	}

	public void screenDraw(Graphics g) {
		g.drawImage(noteImage, x, y, null);
	}
	
	public void drop() {
		
		y += NOTE_SPEED;
	}
	
	public int getTime() {
		return time;
	}
	public int getY() {
		return y;
	}
	public void start_drop() {
		this.drop_judge=true;
		t.start();
	}
	public void stop_drop() {
		this.drop_judge=false;
		t.stop();
	}
	public void run() {
		 try {
			while(this.drop_judge) {
			          this.drop();
			          t.sleep(10);
			 }
			 
	      }catch(Exception e) {

	      }
	}
	
	
}
