package com.me.snake;

import com.badlogic.gdx.Game;
import com.me.snake.screens.GameOver;
import com.me.snake.screens.GameScreen;
import com.me.snake.screens.MenuScreen;
import com.me.snake.screens.Scores;
import com.me.snake.screens.SelectLevel;

public class RootGame extends Game {
	public GameScreen gameScreen;
	public MenuScreen menuScreen;
	public GameOver gameOver;
	public SelectLevel selectLevel;
	public static final int NEED_POINTS = 20;
	private int level;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public void create() {
		 ResourseManager.getInstance(); // ��� �� �����������, ��� Singleton
		gameScreen = new GameScreen(this);
		menuScreen = new MenuScreen(this);
		gameOver = new GameOver(this);
		selectLevel = new SelectLevel(this);

		setScreen(selectLevel);
	}

}
