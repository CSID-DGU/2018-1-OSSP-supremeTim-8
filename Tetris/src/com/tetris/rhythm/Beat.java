package com.tetris.rhythm;

public class Beat {
	private int time;
	private String noteName;
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getNoteName() {
		return noteName;
	}
	public void setNoteName(String noteName) {
		this.noteName = noteName;
	}
	
	public Beat(int time) {
		super();
		this.time = time;
	}
	
}
