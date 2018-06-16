/*package com.tetris.rhythm;

import java.awt.*;
import java.util.*;

import com.tetris.window.*;

public class Game extends Thread{

	private String titleName;
	private String musicTitle;
	private Music gameMusic;
	
	ArrayList<Note> noteList = new ArrayList<Note>();
	/*
	public Game(String titelName, String musicTitle) {
		this.titleName = titleName;
		this.musicTitle = musicTitle;
		gameMusic = new Music(this.musicTitle, false);
		gameMusic.start();
		dropNotes(titleName);
	}
	
	public Game(String titelName, String musicTitle) {
		this.titleName = titleName;
		this.musicTitle = musicTitle;
		gameMusic = new Music(musicTitle, false);
		gameMusic.start();
		dropNotes(" ");
	}

	public void screenDraw(Graphics g) {
		for(int i = 0; i < noteList.size(); i++) {
			Note note = noteList.get(i);
			note.srcreenDraw(g);
		}
		System.out.println("check draw");
	}
	
	
	public void dropNotes(String titleName) {
		Note note = new Note(140, "");
		note.start();
		
		noteList.add(note);
	}
	
	
}
*/