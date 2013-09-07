package com.me.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ResourseManager {
	private static ResourseManager instance;

	public Texture  backgroundGame, backgroundTx, backgroundSelectLvl,
			 fontScTx, fontTx,  lockedTx, completedTx, tileTx, starTx;
	public BitmapFont fontSc, font, fontDone;
	public TextureAtlas atlasSound, atlasParts, atlasControl;
	public SpriteBatch batch;
	public Skin skin;
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

		backgroundGame = new Texture(Gdx.files.internal("data/bgGame.jpg"));
		backgroundGame.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		backgroundTx = new Texture(Gdx.files.internal("data/bg.png"));
		backgroundTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		backgroundSelectLvl = new Texture(Gdx.files.internal("data/bgSelectLvl.png"));
		backgroundSelectLvl.setFilter(TextureFilter.Linear,
				TextureFilter.Linear);

		tileTx = new Texture(Gdx.files.internal("data/lvlTile.png"));
		tileTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		starTx = new Texture(Gdx.files.internal("data/star.png"));
		starTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		lockedTx = new Texture(Gdx.files.internal("data/lockedImg.png"));
		lockedTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		completedTx = new Texture(Gdx.files.internal("data/complete.png"));
		completedTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);

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
		
		atlasSound = new TextureAtlas(Gdx.files.internal("data/art.pack"));
		atlasParts=new TextureAtlas(Gdx.files.internal("data/snakeArt.pack"));
		atlasControl=new TextureAtlas(Gdx.files.internal("data/artControl.txt"));
	}

	public void dispose() {
		batch.dispose();
		backgroundGame.dispose();
		backgroundTx.dispose();
		backgroundSelectLvl.dispose();
		fontScTx.dispose();
		fontTx.dispose();
		tileTx.dispose();
		lockedTx.dispose();
		starTx.dispose();
		completedTx.dispose();
		fontSc.dispose();
		font.dispose();
		fontDone.dispose();
		skin.dispose();
		buttonSound.dispose();
		pickUpSound.dispose();
		bgMusic.dispose();
		atlasSound.dispose();
		atlasParts.dispose();
		atlasControl.dispose();
	//	stage.dispose();
	}

	public void load() {
		if (backgroundGame == null || backgroundGame.getTextureObjectHandle() == 0)
			resourseManager();
	}

	public static ResourseManager getInstance() {
		return instance;
	}
}