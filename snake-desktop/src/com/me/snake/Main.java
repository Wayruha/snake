package com.me.snake;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.habds.snake.IActivityRequestHandler;
import com.habds.snake.RootGame;

public class Main implements IActivityRequestHandler {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "snake";
		cfg.useGL20 = false;
		cfg.width = 540;
		cfg.height = 360;
		cfg.vSyncEnabled = true;

		new LwjglApplication(new RootGame(new Main()), cfg);
	}

	@Override
	public void showAds(boolean show) {
	}
}
