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

	public Texture background, fontScTx, fontTx, snGame ;
	//private Texture tileTx,lockedTx;
	public BitmapFont fontSc, font, fontDone;
	public TextureAtlas atlas;
	public SpriteBatch batch;
	public Sound buttonSound, pickUpSound;
	public Music bgMusic;
	public boolean isBig;

	public ResourseManager() {
		load();
		instance = this;
	}

	public void resourseManager() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		if(w>=540 && h>=360) isBig=true; else isBig=false;
		
		batch = new SpriteBatch();

		background =new Texture(Gdx.files.internal("data/bg3.jpg"));
		background.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		snGame=new Texture(Gdx.files.internal("data/snGame.png"));
		snGame.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		/*tileTx = new Texture(Gdx.files.internal("data/tile.png"));
		tileTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		lockedTx=new Texture(Gdx.files.internal("data/lockedImg.png"));
		lockedTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);*/
		
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
				
		buttonSound = Gdx.audio.newSound(Gdx.files.internal("data/sounds/button.wav"));
		pickUpSound=Gdx.audio.newSound(Gdx.files.internal("data/sounds/pickUp.wav"));
		bgMusic = Gdx.audio.newMusic(Gdx.files.internal("data/sounds/snakeMusic.mp3"));
		
		if(isBig) atlas = new TextureAtlas(Gdx.files.internal("data/bigPack/art.pack")); else
			atlas = new TextureAtlas(Gdx.files.internal("data/smallPack/art.pack"));
		
	}

	public void dispose() {
		batch.dispose();
		background.dispose();
		//tileTx.dispose();
		//lockedTx.dispose();
		fontScTx.dispose();
		fontTx.dispose();
		snGame.dispose();
		fontSc.dispose();
		font.dispose();
		fontDone.dispose();
		buttonSound.dispose();
		pickUpSound.dispose();
		bgMusic.dispose();
		atlas.dispose();
	//	stage.dispose();
	}

	public void load() {
		if (background == null || background.getTextureObjectHandle() == 0)
			resourseManager();
	}

	public static ResourseManager getInstance() {
		return instance;
	}
}