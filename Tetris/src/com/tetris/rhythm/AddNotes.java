package com.tetris.rhythm;

import java.io.*;
import java.util.*;

public class AddNotes {
	ArrayList<Note> noteList = new ArrayList<Note>();
	
	private String titleName;
	

	public AddNotes(String titleName, ArrayList<Note> noteList) throws IOException {
		this.titleName = titleName;
		this.noteList = noteList;
		
		if(titleName == "젓가락 행진곡")
			chopNote();
		else if(titleName == "What is Love?")
			loveNote();
	}

	public void chopNote() {
		try {
			BufferedReader bre = new BufferedReader(new FileReader("./src/music/chopstick.txt"));
			while(true) {
				String line = bre.readLine();
				if(line == null) break;
				noteList.add(new Note(140,20,Integer.parseInt(line)-1000, false));
			}
		}catch(Exception e) {
			
		}
		
	}
	
	public void loveNote() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("./src/music/what is love note.txt"));
		while(true) {
			String line = br.readLine();
			if(line == null) break;
			noteList.add(new Note(140,20,Integer.parseInt(line)-1000, false));
		}
	}
}
