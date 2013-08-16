package com.me.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class ResourseManager {
	private static volatile ResourseManager instance;
	
	public Texture eatTx, backgroundGame,background, wallTx, borderTx, controlBut, fontScTx,fontTx,
			exitTx, pauseTx, headTx, tailTx, bodyTx, tileTx, lockedTx, controlPanelTx, starTx, completedTx,recordTx, menuTx, backgroundOver,nextLvlTx;

	public BitmapFont fontSc,font;

	public SpriteBatch batch;

	private ResourseManager() { 
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
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
			
		batch = new SpriteBatch();
		
		backgroundGame = new Texture(Gdx.files.internal("data/bgGame.jpg"));
		backgroundGame.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		background = new Texture(Gdx.files.internal("data/bg.png"));
		background.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		backgroundOver=new Texture(Gdx.files.internal("data/bg2.jpg"));
		backgroundOver.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		menuTx=new Texture(Gdx.files.internal("data/snakeLogo.png"));
		menuTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		borderTx = new Texture(Gdx.files.internal("data/parts/border.png"));
		borderTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		wallTx = new Texture(Gdx.files.internal("data/parts/wall.png"));
		wallTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		controlBut = new Texture(Gdx.files.internal("data/control/control.png"));
		controlBut.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		pauseTx = new Texture(Gdx.files.internal("data/pause.png"));
		pauseTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		exitTx = new Texture(Gdx.files.internal("data/exit.png"));
		exitTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		eatTx = new Texture(Gdx.files.internal("data/eat.png"));
		eatTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		headTx = new Texture(Gdx.files.internal("data/parts/snakePartHeadR.png"));
		tailTx = new Texture(Gdx.files.internal("data/parts/snakePartTail.png"));
		bodyTx = new Texture(Gdx.files.internal("data/parts/snakePart.png"));
		headTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		tailTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		bodyTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		 
		tileTx=new Texture(Gdx.files.internal("data/lvlTile.png"));
		tileTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		starTx=new Texture(Gdx.files.internal("data/star.png"));
		starTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		lockedTx=new Texture(Gdx.files.internal("data/lockedImg.png"));
		lockedTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		completedTx=new Texture(Gdx.files.internal("data/complete.png"));
		completedTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		controlPanelTx=new Texture(Gdx.files.internal("data/control/ctrlPanel.png"));
		controlPanelTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		nextLvlTx=new Texture(Gdx.files.internal("data/control/nextLvl.png"));
		nextLvlTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		recordTx= new Texture(Gdx.files.internal("data/record.png"));
		recordTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		fontScTx = new Texture(Gdx.files.internal("data/font/wal.png"));
		fontScTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		fontSc = new BitmapFont(Gdx.files.internal("data/font/wal.fnt"),
				new TextureRegion(ResourseManager.getInstance().fontScTx), false);
		fontSc.setScale(0.35f*w/480,0.35f*h/320);
		
		fontTx = new Texture(Gdx.files.internal("data/font/neucha.png"));
		fontTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font = new BitmapFont(Gdx.files.internal("data/font/neucha.fnt"),
				new TextureRegion(ResourseManager.getInstance().fontTx), false);


	}

	public void dispose() {
		eatTx.dispose();
		backgroundGame.dispose();
		background.dispose();
		borderTx.dispose();
		wallTx.dispose();
		controlBut.dispose();
		fontScTx.dispose();
		fontTx.dispose();
		pauseTx.dispose();
		exitTx.dispose();
		headTx.dispose();
		tailTx.dispose();
		bodyTx.dispose();
		tileTx.dispose();
		lockedTx.dispose();
		controlPanelTx.dispose();
		starTx.dispose();
		completedTx.dispose();
		recordTx.dispose();
		menuTx.dispose();
		backgroundOver.dispose();
		nextLvlTx.dispose();
		
	}
}