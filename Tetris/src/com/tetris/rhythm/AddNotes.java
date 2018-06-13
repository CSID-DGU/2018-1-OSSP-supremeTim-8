package com.tetris.rhythm;

import java.util.*;

public class AddNotes {
	ArrayList<Note> noteList = new ArrayList<Note>();
	
	private String titleName;
	

	public AddNotes(String titleName, ArrayList<Note> noteList) {
		this.titleName = titleName;
		this.noteList = noteList;
		
		if(titleName == "젓가락 행진곡")
			chopNote();
		else if(titleName == "What is Love?")
			loveNote();
	}

	public void chopNote() {
		Note note1 = new Note(140, 20, 200);
		Note note2 = new Note(140, 20, 1200);
		Note note3 = new Note(140, 20, 2200);
		Note note4 = new Note(140, 20, 3200);
		Note note5 = new Note(140, 20, 4200);
		Note note6 = new Note(140, 20, 5200);
		Note note7 = new Note(140, 20, 7300);
		Note note8 = new Note(140, 20, 8300);
		Note note9 = new Note(140, 20, 9300);

		noteList.add(note1);
		noteList.add(note2);
		noteList.add(note3);
		noteList.add(note4);
		noteList.add(note5);
		noteList.add(note6);
		noteList.add(note7);
		noteList.add(note8);
		noteList.add(note9);
	}
	
	public void loveNote() {
		Note note1 = new Note(140, 20, 300);
		Note note2 = new Note(140, 20, 900);
		Note note3 = new Note(140, 20, 1200);
		Note note4 = new Note(140, 20, 2000);
		Note note5 = new Note(140, 20, 2500);
		Note note6 = new Note(140, 20, 3000);
		Note note7 = new Note(140, 20, 3500);
		Note note8 = new Note(140, 20, 4000);
		Note note9 = new Note(140, 20, 4500);

		noteList.add(note1);
		noteList.add(note2);
		noteList.add(note3);
		noteList.add(note4);
		noteList.add(note5);
		noteList.add(note6);
		noteList.add(note7);
		noteList.add(note8);
		noteList.add(note9);
	}
}
