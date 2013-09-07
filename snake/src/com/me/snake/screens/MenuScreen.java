package com.me.snake.screens;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.me.snake.ResourseManager;
import com.me.snake.RootGame;

public class MenuScreen implements Screen {

	private Sprite  snMenuSp,starSp;
	private Sprite background;
	private RootGame rootGame;
	private OrthographicCamera camera;
	private float w = Gdx.graphics.getWidth();
	private float h = Gdx.graphics.getHeight();
	private TextButton selectLevelButt;
	private TextButton fastGame,exitButt;
	private ArrayList scoresArr;
	private String bestScore;
	private Image musicOn, musicOff, soundOn, soundOff;
	private Stage stage;
	private boolean ifSound=true, ifMusic;

	public MenuScreen(RootGame rootGame) {
		this.rootGame = rootGame;
	}

	@Override
	public void show() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		ifSound=rootGame.ifSound();
		ifMusic=rootGame.ifMusic();
		camera = new OrthographicCamera(320, 480);
		stage=new Stage(0,0,false);
		stage.addAction(Actions.color(new Color(1, 1, 1, 0))); //задали макс прозорість
		stage.addAction(Actions.color(new Color(1, 1, 1, 1), 0.5f)); //запустили екшн
		
	/*	if(ifMusic)
			if(!ResourseManager.getInstance().bgMusic.isPlaying()) {
				ResourseManager.getInstance().bgMusic.play();
				ResourseManager.getInstance().bgMusic.setLooping(true);
			}*/
				
		
		ResourseManager.getInstance().fontSc.setScale(0.35f * w / 480, 0.35f * h / 320);
		
		Json json = new Json();
		FileHandle handle = Gdx.files.local("scores.txt");
	    if (!handle.exists()) {
			   try {
			    handle.file().createNewFile();
			    String jsonText="[0,0,0,0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,7]"; //змінити коли зміниться к-сть лвлів
			    handle.writeString(jsonText, true);
			   } catch (IOException e) {
			    e.printStackTrace();
			   }
	    }
		String newText = handle.readString(); // read Json from file // sec
		scoresArr = json.fromJson(ArrayList.class, newText);
		int i=((Float) scoresArr.get(0)).intValue();
		bestScore=bestScore.valueOf(i);
		
		final TextButtonStyle buttonStyle = new TextButtonStyle();


		
		background = new Sprite(ResourseManager.getInstance().backgroundTx);
		background.setSize(w, h);
		background.setPosition(0,0);
		
		
		snMenuSp=new Sprite(ResourseManager.getInstance().atlasParts.createSprite("snakeLogo"));
		snMenuSp.setSize(0.6f*512*w/480, 0.6f*512*h/320);
		snMenuSp.setPosition(0.05f*w, 0.15f*h);
		
		starSp = new Sprite(ResourseManager.getInstance().atlasParts.createSprite("record"));
		starSp.setSize(1.05f*128*w/480, 1.1f*128*h/320);
		starSp.setPosition(0.72f*w, 0.62f*h);
		
		
		musicOn=new Image(ResourseManager.getInstance().atlasSound.findRegion("music_on"));
		musicOn.setSize(0.22f*256*w/480, 0.21f*256*h/320);
		musicOn.setPosition(0.4f*w, 0.83f*h);
		musicOn.setName("music_on");
		musicOn.addListener(musicListener);
		musicOff=new Image(ResourseManager.getInstance().atlasSound.findRegion("music_off"));
		musicOff.setSize(0.22f*256*w/480, 0.21f*256*h/320);
		musicOff.setPosition(0.4f*w, 0.83f*h);
		musicOff.setName("music_off");
		musicOff.addListener(musicListener);
		soundOn=new Image(ResourseManager.getInstance().atlasSound.findRegion("sound_on"));
		soundOn.setSize(0.22f*256*w/480, 0.21f*256*h/320);
		soundOn.setPosition(0.55f*w, 0.83f*h);
		soundOn.setName("sound_on");
		soundOn.addListener(musicListener);
		soundOff=new Image(ResourseManager.getInstance().atlasSound.findRegion("sound_off"));
		soundOff.setSize(0.22f*256*w/480, 0.21f*256*h/320);
		soundOff.setPosition(0.55f*w, 0.83f*h);
		soundOff.setName("sound_off");
		soundOff.addListener(musicListener);
		if(rootGame.ifMusic()) musicOff.setVisible(false); else musicOn.setVisible(false);
		if(rootGame.ifSound()) soundOff.setVisible(false); else soundOn.setVisible(false);
		
		buttonStyle.font = ResourseManager.getInstance().font;
		buttonStyle.font.setScale(0.6f*w/480, 0.6f*h/320);
	    buttonStyle.downFontColor = new Color(toRGB(37,89,115));
		
		fastGame=new TextButton("Fast game", buttonStyle);
		fastGame.setPosition(0.4f*w, 0.57f*h);
	    fastGame.addListener(new ClickListener() {
	    	@Override
	    	public void touchUp(InputEvent event, float x, float y,
	    			int pointer, int button) {
	    				if(ifSound) ResourseManager.getInstance().buttonSound.play(1f);
	    				rootGame.setIfSound(ifSound);
	    				rootGame.setLevel(0);
	    	    		dispose();
	    	    		rootGame.setScreen(rootGame.gameScreen);
	    		
	    	}
		});
		
		selectLevelButt = new TextButton("Select level", buttonStyle);
		selectLevelButt.setPosition(0.4f*w, 0.38f*h);
	    selectLevelButt.addListener(new ClickListener() {
	    	@Override
	    	public void touchUp(InputEvent event, float x, float y,
	    			int pointer, int button) {
	    				if(ifSound) ResourseManager.getInstance().buttonSound.play(1f);
	    				rootGame.setIfSound(ifSound);
	    	    		dispose();
	    	    		rootGame.setScreen(rootGame.selectLevel);
	    	}
		});
	    
	    exitButt= new TextButton("Exit", buttonStyle);
	    exitButt.setPosition(0.4f*w, 0.19f*h);
	    exitButt.addListener(new ClickListener() {
	    	@Override
	    	public void touchUp(InputEvent event, float x, float y,
	    			int pointer, int button) {
	    		ResourseManager.getInstance().dispose();
	    		Gdx.app.exit();
	    	}
		});
	    
	    
	   stage.addActor(fastGame);
	    stage.addActor(selectLevelButt);
	    stage.addActor(exitButt);
	    	stage.addActor(soundOn);
	    	stage.addActor(soundOff);
	    	stage.addActor(musicOn);
	    	stage.addActor(musicOff);
	  stage.addListener(new ClickListener(){
	    	@Override
	    	public boolean keyDown(InputEvent event, int keycode) {
	    		if (keycode == Keys.BACK) {
	    			ResourseManager.getInstance().dispose();
	    		}
	    		return true;
	    	}
	    });
		Gdx.input.setInputProcessor(stage);
	} 
	

	public void resize(int width, int height) {
		stage.setViewport(width, height, true);
		}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		ResourseManager.getInstance().batch.begin();
		background.draw(ResourseManager.getInstance().batch);
		snMenuSp.draw(ResourseManager.getInstance().batch);
		starSp.draw(ResourseManager.getInstance().batch);
		ResourseManager.getInstance().fontSc.draw(ResourseManager.getInstance().batch,bestScore, 0.79f*w,0.86f*h);
		ResourseManager.getInstance().batch.end();
		stage.act(delta);
		stage.draw();
		}

	@Override
	public void dispose() {
		//ResourseManager.getInstance().stage.clear();
		stage.dispose();
	}
	
	private ClickListener musicListener=new ClickListener(){
		@Override
		public void clicked(InputEvent event, float x, float y) {
				if(event.getListenerActor().getName()=="music_on"){ musicOn.setVisible(false);musicOff.setVisible(true); rootGame.setIfMusic(false); ResourseManager.getInstance().bgMusic.stop(); }
				if(event.getListenerActor().getName()=="music_off"){  musicOn.setVisible(true) ;musicOff.setVisible(false); rootGame.setIfMusic(true);  ResourseManager.getInstance().bgMusic.play();}
				if(event.getListenerActor().getName()=="sound_on"){ soundOn.setVisible(false); soundOff.setVisible(true); ifSound=false; }
				if(event.getListenerActor().getName()=="sound_off"){ soundOn.setVisible(true); soundOff.setVisible(false); ifSound=true; }
			
	    	}
	};

	private Color toRGB(int r, int g, int b) {
        float RED = r / 255.0f;
        float GREEN = g / 255.0f;
        float BLUE = b / 255.0f;
        return new Color(RED, GREEN, BLUE, 1);
}
	
	
	@Override
	public void resume() {
	}
	
	@Override
	public void hide() {
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
}

