package com.tetris.controller;

import com.tetris.classes.Block;
import com.tetris.classes.TetrisBlock;

public class TetrisController {

	private int rotation_index;
	private TetrisBlock block;
	private Block[][] map;
	
	private int maxX, maxY;
	
	
	public TetrisController(TetrisBlock block, int maxX, int maxY, Block[][] map) {
		this.block = block;
		
		this.maxX = maxX;
		this.maxY = maxY;
		
		this.map = map;
		this.rotation_index = block.getRotationIndex();
		
	}
	
	public void setBlock(TetrisBlock block){
		this.block = block;
		this.rotation_index = block.getRotationIndex();
	}
	
	
	
	public void showIndex(){
		for(Block blocks : block.getBlock()){
			if(blocks!=null)System.out.print("("+blocks.getX()+","+blocks.getY()+")");
		}
		System.out.println();
	}
	
	
	
	public boolean checkIndex(int maxX, int maxY){ // 영역을 벗어나는지 체크해주는 메소드
		for(Block blocks : block.getBlock()){
			if(blocks==null || blocks.getY()<0) continue;
			
			if(blocks.getX() < 0 || blocks.getY() < 0 
					|| blocks.getX() > maxX || blocks.getY() > maxY )
				return false;
			else{
				if(map[blocks.getY()][blocks.getX()]!=null)return false;
			}
		}
		return true;
	}
	
	
	public void moveLeft(){moveLeft(1);}
	public void moveLeft(int x){
		
		block.moveLeft(x);
				

		if(!checkIndex(maxX,maxY)) {
			block.moveLeft(-x);
		}
	}
	
	
	public void moveRight(){moveRight(1);}
	public void moveRight(int x){
	
		block.moveRight(x);
		
				
	
		if (!checkIndex(maxX, maxY)) {
			block.moveRight(-x);
		}
	}
	
	
	
	public boolean moveDown(){return moveDown(1);}

	public boolean moveDown(int y){ // 내려갈 수 있는지 판단해주는 메소드
		
		boolean moved = true;
		
		block.moveDown(y);
		
		if (!checkIndex(maxX, maxY)) { 
			block.moveDown(-y);
			moved = false;
		}
		return moved;
	}
	
	
	public boolean moveQuickDown(int startY, boolean moved){
		
		
		block.moveDown(1);
		
		if (!checkIndex(maxX, maxY)) {
			block.moveDown(-1);
			if(moved) return false;
		}
		return moveQuickDown(startY+1, true);
	}
	
	
	
	
	public void nextRotation(int rotation_direction){
		if(rotation_direction == TetrisBlock.ROTATION_LEFT) 
			this.nextRotationLeft();
		else if(rotation_direction == TetrisBlock.ROTATION_RIGHT) 
			this.nextRotationRight();
	}
	
	
	
	public void nextRotationLeft(){
		
		rotation_index++;
		if(rotation_index == TetrisBlock.ROTATION_270+1) rotation_index = TetrisBlock.ROTATION_0;
		block.rotation(rotation_index);
		
		
		if(!checkIndex(maxX,maxY)) {
			rotation_index--;
			if(rotation_index == TetrisBlock.ROTATION_0-1) rotation_index = TetrisBlock.ROTATION_270;
			block.rotation(rotation_index);
		}
	}
	
	
	
	public void nextRotationRight(){
		
		rotation_index--;
		if(rotation_index == TetrisBlock.ROTATION_0-1) rotation_index = TetrisBlock.ROTATION_270;
		block.rotation(rotation_index);
		
		
		if(!checkIndex(maxX,maxY)) {
			rotation_index++;
			if(rotation_index == TetrisBlock.ROTATION_270+1) rotation_index = TetrisBlock.ROTATION_0;
			block.rotation(rotation_index);
		}
	}
}
