package com.me.snake;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.habds.snake.RootGame;
import com.habds.snake.screens.GameScreen;
import com.habds.snake.screens.SelectLevel;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "snake";
		cfg.useGL20 = false;
		cfg.width = 1920;
		cfg.height = 1080;
		cfg.vSyncEnabled = true;
		
		new LwjglApplication(new RootGame(), cfg);
	}
}
