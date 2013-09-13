package com.me.snake;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.me.snake.screens.GameScreen;
import com.me.snake.screens.SelectLevel;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "snake";
		cfg.useGL20 = false;
		cfg.width = 600;
		cfg.height = 400;
		cfg.vSyncEnabled = true;
		
		new LwjglApplication(new RootGame(), cfg);
	}
}
