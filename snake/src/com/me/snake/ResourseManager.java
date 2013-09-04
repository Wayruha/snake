package com.me.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ResourseManager {
	private static ResourseManager instance;

	public Texture eatTx, backgroundGame, backgroundTx, backgroundSelectLvl,
			wallTx, borderTx, controlBut, fontScTx, fontTx, exitTx, pauseTx,
			headTx, tailTx, bodyTx, tileTx, lockedTx, starTx, ctrlStart,
			ctrlBack, menuTx, completedTx, recordTx, backgroundOver, nextLvlTx, musicOnTx,musicOffTx, soundOnTx, soundOffTx;
	public BitmapFont fontSc, font, fontDone;
	public SpriteBatch batch;
	public Skin skin;
	//public Stage stage;
	public Sound buttonSound, pickUpSound;
	public Music bgMusic;

	public ResourseManager() {
		load();
		instance = this;
	}

	public void resourseManager() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		batch = new SpriteBatch();
		//stage=new Stage(0,0, false);

		backgroundGame = new Texture(Gdx.files.internal("data/bgGame.jpg"));
		backgroundGame.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		backgroundTx = new Texture(Gdx.files.internal("data/bg.png"));
		backgroundTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		backgroundSelectLvl = new Texture(
				Gdx.files.internal("data/bgSelectLvl.png"));
		backgroundSelectLvl.setFilter(TextureFilter.Linear,
				TextureFilter.Linear);

		backgroundOver = new Texture(Gdx.files.internal("data/bgGameOver.png"));
		backgroundOver.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		menuTx = new Texture(Gdx.files.internal("data/snakeLogo.png"));
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

		headTx = new Texture(
				Gdx.files.internal("data/parts/snakePartHeadR.png"));
		tailTx = new Texture(Gdx.files.internal("data/parts/snakePartTail.png"));
		bodyTx = new Texture(Gdx.files.internal("data/parts/snakePart.png"));
		headTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		tailTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		bodyTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		tileTx = new Texture(Gdx.files.internal("data/lvlTile.png"));
		tileTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		starTx = new Texture(Gdx.files.internal("data/star.png"));
		starTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		lockedTx = new Texture(Gdx.files.internal("data/lockedImg.png"));
		lockedTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		completedTx = new Texture(Gdx.files.internal("data/complete.png"));
		completedTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		ctrlStart = new Texture(Gdx.files.internal("data/control/start.png"));
		ctrlStart.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		ctrlBack = new Texture(Gdx.files.internal("data/control/back.png"));
		ctrlBack.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		nextLvlTx = new Texture(Gdx.files.internal("data/control/nextLvl.png"));
		nextLvlTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		recordTx = new Texture(Gdx.files.internal("data/record.png"));
		recordTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		musicOnTx= new Texture(Gdx.files.internal("data/music_on.png"));
		musicOnTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		musicOffTx= new Texture(Gdx.files.internal("data/music_off.png"));
		musicOffTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		soundOnTx= new Texture(Gdx.files.internal("data/sound_on.png"));
		soundOnTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		soundOffTx= new Texture(Gdx.files.internal("data/sound_off.png"));
		soundOffTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		
		fontScTx = new Texture(Gdx.files.internal("data/font/wal.png"));
		fontScTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		fontSc = new BitmapFont(Gdx.files.internal("data/font/wal.fnt"),
				new TextureRegion(fontScTx), false);

		fontTx = new Texture(Gdx.files.internal("data/font/neucha.png"));
		fontTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font = new BitmapFont(Gdx.files.internal("data/font/neucha.fnt"),
				new TextureRegion(fontTx), false);
		fontDone = new BitmapFont(Gdx.files.internal("data/font/neucha.fnt"),
				new TextureRegion(fontTx), false);
		fontDone.setScale(0.5f * w / 480, 0.5f * h / 320);
		
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		
		buttonSound = Gdx.audio.newSound(Gdx.files.internal("data/sounds/button.wav"));
		pickUpSound=Gdx.audio.newSound(Gdx.files.internal("data/sounds/pickUp.wav"));
		bgMusic = Gdx.audio.newMusic(Gdx.files.internal("data/sounds/snakeMusic.mp3"));

	}

	public void dispose() {
		batch.dispose();
		eatTx.dispose();
		backgroundGame.dispose();
		backgroundTx.dispose();
		backgroundSelectLvl.dispose();
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
		starTx.dispose();
		ctrlStart.dispose();
		ctrlBack.dispose();
		completedTx.dispose();
		recordTx.dispose();
		menuTx.dispose();
		backgroundOver.dispose();
		nextLvlTx.dispose();
		fontSc.dispose();
		font.dispose();
		fontDone.dispose();
		skin.dispose();
		buttonSound.dispose();
		pickUpSound.dispose();
		bgMusic.dispose();
		musicOnTx.dispose();
		musicOffTx.dispose();
		soundOnTx.dispose();
		soundOffTx.dispose();
	//	stage.dispose();
	}

	public void load() {
		if (menuTx == null || menuTx.getTextureObjectHandle() == 0)
			resourseManager();
	}

	public static ResourseManager getInstance() {
		return instance;
	}
}