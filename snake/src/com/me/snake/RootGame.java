package com.me.snake;

import com.badlogic.gdx.Game;
import com.me.snake.screens.GameOver;
import com.me.snake.screens.GameScreen;
import com.me.snake.screens.MenuScreen;
import com.me.snake.screens.SelectLevel;

public class RootGame extends Game {
	public GameScreen gameScreen;
	public MenuScreen menuScreen;
	public GameOver gameOver;
	public SelectLevel selectLevel;
	public static final int NEED_POINTS = 17;
	private int level;
	private boolean ifSound=true,ifMusic=true;

	public boolean ifMusic() {
		return ifMusic;
	}

	public void setIfMusic(boolean ifMusic) {
		this.ifMusic = ifMusic;
	}

	public void setIfSound(boolean ifSound) {
		this.ifSound = ifSound;
	}

	public boolean ifSound(){
		return ifSound;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public void create() {
		new ResourseManager();  
		menuScreen = new MenuScreen(this);
		gameOver = new GameOver(this);
		selectLevel = new SelectLevel(this);
		gameScreen=new GameScreen(this);
		setScreen(menuScreen);
	}

}
