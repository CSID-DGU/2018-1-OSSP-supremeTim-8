package com.tetris.rhythm;

public class Track {
	private String title;
	private String gameImage; 
	private String startMusic;
	private String gameMusic;
	
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
	public Track(String title, String gameImage, String startMusic, String gameMusic) {
		super();
		this.title = title;
		this.gameImage = gameImage;
		this.startMusic = startMusic;
		this.gameMusic = gameMusic;
	}
	
	
	
}
