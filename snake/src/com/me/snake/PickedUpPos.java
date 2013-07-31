package com.me.snake;
public class PickedUpPos {
public int mapX, mapY;
public boolean addToBack;
	public PickedUpPos(int mapX,int mapY) {
		this.mapX=mapX;
		this.mapY=mapY;
		addToBack = false;
	}
}
