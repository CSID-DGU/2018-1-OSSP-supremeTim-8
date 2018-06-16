package com.tetris.rhythm;

public class Track {
	private String title;
	private String gameImage; 
	private String startMusic;
	private String gameMusic;
	private int endTime;
	
	public int getEndTime() {
		return endTime;
	}
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public void setGameImage(String gameImage) {
		String temp = "./src/com/tetris/image/"+gameImage;
		this.gameImage = temp;
	}
	public String getStartMusic() {
		return startMusic;
	}
	public void setStartMusic(String startMusic) {
		this.startMusic = startMusic;
	}
	public String getGameMusic() {
		return gameMusic;
	}
	public void setGameMusic(String gameMusic) {
		this.gameMusic = gameMusic;
	}

	public String getGameImage() {
		return gameImage;
	}
	public Track(String title, String gameImage, String startMusic, String gameMusic, int endTime) {
		super();
		this.title = title;
		this.gameImage = gameImage;
		this.startMusic = startMusic;
		this.gameMusic = gameMusic;
		this.endTime = endTime;
	}
	
	
	
}
