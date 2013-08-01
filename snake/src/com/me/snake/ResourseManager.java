package com.me.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;


public class ResourseManager {
	private static volatile ResourseManager instance;
	
	public Texture eatTx, background, wallTx, borderTx, controlBut, fontTx,
			exitTx, pauseTx, headTx, tailTx, bodyTx;

	private ResourseManager() {  //можу пояснити розмовою биренько. скайп? тел? пох
		resourseManager();
	}
	
	public static ResourseManager getInstance() {
		ResourseManager localInstance = instance;
		if (localInstance == null) {
			synchronized (ResourseManager.class) {
				localInstance = instance;
				if (localInstance == null) {
					instance = localInstance = new ResourseManager();
				}
			}
		}
		return localInstance;
	}

	public void resourseManager() {

		background = new Texture(Gdx.files.internal("data/bgSnake.png"));
		background.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		borderTx = new Texture(Gdx.files.internal("data/parts/border.png"));
		borderTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		wallTx = new Texture(Gdx.files.internal("data/parts/wall.png"));
		wallTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		controlBut = new Texture(Gdx.files.internal("data/control.png"));
		controlBut.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		pauseTx = new Texture(Gdx.files.internal("data/pause.png"));
		pauseTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		exitTx = new Texture(Gdx.files.internal("data/exit.png"));
		exitTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		eatTx = new Texture(Gdx.files.internal("data/eat.png"));
		eatTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		fontTx = new Texture(Gdx.files.internal("data/font/neucha.png"));
		fontTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		headTx = new Texture(Gdx.files.internal("data/parts/snakePartHead.png"));
		tailTx = new Texture(Gdx.files.internal("data/parts/snakePartTail.png"));
		bodyTx = new Texture(Gdx.files.internal("data/parts/snakePart.png"));
		headTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		tailTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		bodyTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);

	}

	public void dispose() {
		eatTx.dispose();
		background.dispose();
		borderTx.dispose();
		wallTx.dispose();
		controlBut.dispose();
		fontTx.dispose();
		pauseTx.dispose();
		exitTx.dispose();
		headTx.dispose();
		tailTx.dispose();
		bodyTx.dispose();
	}
}