package com.me.snake;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.me.snake.screens.GameScreen;

public class SnakePart {
	private Texture headTx,tailTx,bodyTx;
	private Sprite sp;
	private int mapX, mapY;
	private float x,y;

	public SnakePart(int mapX, int mapY, String type) {
		
		if(type=="head") sp=new Sprite(ResourseManager.getInstance().atlas.createSprite("snakeHead"));
		else if (type=="tail") sp=new Sprite(ResourseManager.getInstance().atlas.createSprite("snakeTail"));
		else sp=new Sprite(ResourseManager.getInstance().atlas.createSprite("snakePart"));
		sp.setPosition(mapX*GameScreen.SQUARE_WIDTH, mapY*GameScreen.SQUARE_HEIGHT);
		sp.setSize(GameScreen.SQUARE_WIDTH, GameScreen.SQUARE_HEIGHT);
		x=sp.getX();
		y=sp.getY();
		this.setMapX(mapX);
		this.mapY=mapY;
	}

	public float getX(){
		return x; 
	}
	
	public float getY(){
		return y; 
	}
	
	public Sprite getSp() {
		return sp;
	}

	public int getMapX() {
		return mapX;
	}
	
	public int getMapY() {
		return mapY;
	}

	public void setMapX(int mapX) {
		this.mapX += mapX;
	}

	public void setMapY(int mapY) {
		this.mapY+=mapY;
	}
	
	public void setNewPos(int mapX, int mapY){
		this.mapX=mapX;
		this.mapY=mapY;
		sp.setPosition(GameScreen.SQUARE_WIDTH*mapX, GameScreen.SQUARE_HEIGHT*mapY);
	}
}
