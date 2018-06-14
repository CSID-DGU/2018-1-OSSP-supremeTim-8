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
		noteList.add(new Note(140,20,100));
		for(int time=1200;time<=5200;time+=1000) {
			noteList.add(new Note(140,20,time));
		}
		for(int time=7300;time<=16300;time+=1000) {
			noteList.add(new Note(140,20,time));
		}
		for(int time=17600;time<=31600;time+=1000) {
			noteList.add(new Note(140,20,time));
		}
		for(int time=32900;time<=40900;time+=1000) {
			noteList.add(new Note(140,20,time));
		}
		for(int time=42200;time<=48200;time+=1000) {
			noteList.add(new Note(140,20,time));
		}
		for(int time=49500;time<=105500;time+=1000) {
			noteList.add(new Note(140,20,time));
		}
		/*Note note1 = new Note(140, 20, 100);
		Note note2 = new Note(140, 20, 1200);
		Note note3 = new Note(140, 20, 2200);
		Note note4 = new Note(140, 20, 3200);
		Note note5 = new Note(140, 20, 4200);
		Note note6 = new Note(140, 20, 5200);
		Note note7 = new Note(140, 20, 7300);
		Note note8 = new Note(140, 20, 8300);
		Note note9 = new Note(140, 20, 9300);
		Note note10 = new Note(140, 20, 10300);
		Note note11 = new Note(140, 20, 11300);
		Note note12 = new Note(140, 20, 12300);
		Note note13 = new Note(140, 20, 13300);
		Note note14 = new Note(140, 20, 14300);
		Note note15 = new Note(140, 20, 15300);
		Note note16 = new Note(140, 20, 16300);
		Note note17 = new Note(140, 20, 17700);
		Note note18 = new Note(140, 20, 18700);
		Note note19 = new Note(140, 20, 19700);
		Note note20 = new Note(140, 20, 20700);
		Note note21 = new Note(140, 20, 21700);
		Note note22 = new Note(140, 20, 22700);
		Note note23 = new Note(140, 20, 22700);
		Note note24 = new Note(140, 20, 23700);
		Note note25 = new Note(140, 20, 24700);
		Note note26 = new Note(140, 20, 25700);
		Note note27 = new Note(140, 20, 26700);
		Note note28 = new Note(140, 20, 27700);
		Note note29 = new Note(140, 20, 28700);
		Note note30 = new Note(140, 20, 29700);
		Note note31 = new Note(140, 20, 30700);
		Note note32 = new Note(140, 20, 31700);
		Note note33 = new Note(140, 20, 32700);
		Note note34 = new Note(140, 20, 32700);
		Note note35 = new Note(140, 20, 32700);
		Note note36 = new Note(140, 20, 32700);
		Note note37 = new Note(140, 20, 32700);
		Note note38 = new Note(140, 20, 32700);
		
		
		noteList.add(note1);
		noteList.add(note2);
		noteList.add(note3);
		noteList.add(note4);
		noteList.add(note5);
		noteList.add(note6);
		noteList.add(note7);
		noteList.add(note8);
		noteList.add(note9);
		noteList.add(note10);
		noteList.add(note11);
		noteList.add(note12);
		noteList.add(note13);
		noteList.add(note14);
		noteList.add(note15);
		noteList.add(note16);
		noteList.add(note17);
		noteList.add(note18);
		noteList.add(note19);
		noteList.add(note20);
		noteList.add(note21);
		noteList.add(note22);
		noteList.add(note23);
		noteList.add(note24);
		noteList.add(note25);
		noteList.add(note26);
		noteList.add(note27);
		noteList.add(note28);
		noteList.add(note29);
		noteList.add(note30);
		noteList.add(note31);
		noteList.add(note32);
		noteList.add(note33);*/
		
		
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
