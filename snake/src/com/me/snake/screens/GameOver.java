package com.me.snake.screens;

import java.io.BufferedWriter;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.me.snake.ResourseManager;
import com.me.snake.RootGame;

public class GameOver implements Screen, InputProcessor {
	private OrthographicCamera camera;
	private Image nextLvl;
	private Sprite background, recordSp;
	private int score,record;
	private float w,h;
	private RootGame rootGame;
	private Image ctrlStart, ctrlBack;
	private String message;
	BufferedWriter out = null;
	private Stage stage;
	private int level, unlockedLvl;
	private ArrayList scoresArr;
	private boolean ifSound;
	
	public GameOver(RootGame rootGame) {
		this.rootGame = rootGame;
	}
	
	public void setScore(int score){
		this.score=score;
	}
	
	private void setRecord(){
		FileHandle handle = Gdx.files.local("scores.txt");
		Json json = new Json();
		String newText = handle.readString(); // read Json from file
		scoresArr = json.fromJson(ArrayList.class, newText);
		record = ((Float) scoresArr.get(level)).intValue();
		unlockedLvl= ((Float) scoresArr.get(scoresArr.size()-1)).intValue();
		 
		if(unlockedLvl==level){     
			if(score<RootGame.NEED_POINTS){
				message="N00b!Level failed";
				if(score>record){
					message="Nice!New record!";
					scoresArr.set(level,score);
					record=score;
				}
			} else {
				scoresArr.set(level,score); 
				scoresArr.set(scoresArr.size()-1, unlockedLvl+1);
				message="Nice!Level completed";
			}
		} else {
			if(score>record){
				message="Nice!New record!";
				scoresArr.set(level,score);
				record=score;
			} else {
				message="Your score: ";
			}
		}
		
		String jsonText= json.toJson(scoresArr);
	    handle.writeString(jsonText, false);    //Записуємо новий рекорд
	}
	
	@Override
	public void show() {
		 w = Gdx.graphics.getWidth();
		 h = Gdx.graphics.getHeight();
		 ifSound=rootGame.ifSound();
	     level=rootGame.getLevel();
 
		
		camera = new OrthographicCamera(320, 480);
		stage = new Stage(0, 0, true);
		stage.addAction(Actions.color(new Color(1, 1, 1, 0))); //задали макс прозорість
		stage.addAction(Actions.color(new Color(1, 1, 1, 1), 0.5f)); //запустили екшн
		
		final TextButtonStyle buttonStyle = new TextButtonStyle();
		
		
		background=new Sprite(ResourseManager.getInstance().backgroundOver);
		background.setPosition(0, 0);
		background.setSize(w,h);
		
		recordSp=new Sprite(ResourseManager.getInstance().recordTx);
		recordSp.setPosition(0.7f*w, 0.72f*h);
		recordSp.setSize(1.05f*128*w/480, 1.1f*128*h/320);
		
		ResourseManager.getInstance().font.setScale(0.6f*w/480,0.6f*h/320);
		
		ctrlStart=new Image(ResourseManager.getInstance().ctrlStart);
		ctrlStart.setSize(0.5f*256*w/480, 0.5f*256*h/320);
		ctrlStart.setPosition(0.73f*w, 0.15f*h);
		ctrlStart.setName("start");
		ctrlStart.addListener(buttonClickListener);
		
		
		ctrlBack=new Image(ResourseManager.getInstance().ctrlBack);
		ctrlBack.setSize(0.53f*256*w/480, 0.53f*256*h/320);
		ctrlBack.setPosition(0.5f*w, -0.225f*h);
		ctrlBack.setName("back");
		ctrlBack.addListener(buttonClickListener);
		
		nextLvl=new Image(ResourseManager.getInstance().nextLvlTx);
		nextLvl.setName("nextLvl");
		nextLvl.setPosition(0.75f*w, 0);
		nextLvl.setSize(0.25f*w, 0.2f*w/1.45f);
		nextLvl.addListener(buttonClickListener);
		
		setRecord();
		if(message=="Nice!Level completed") nextLvl.setVisible(true); else  nextLvl.setVisible(false);
		
		stage.addActor(nextLvl);
		stage.addActor(ctrlStart);
		stage.addActor(ctrlBack);
		stage.addListener(new ClickListener(){
		    	@Override
		    	public boolean keyDown(InputEvent event, int keycode) {
		    		if (keycode == Keys.BACK) {
		    			dispose();
		    			ResourseManager.getInstance().dispose();
		    		}
		    		return true;
		    	}
		    });
		
		 Gdx.input.setInputProcessor(stage);
	}
	
	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, true);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		ResourseManager.getInstance().batch.begin();
		background.draw(ResourseManager.getInstance().batch);
		recordSp.draw(ResourseManager.getInstance().batch);
		ResourseManager.getInstance().font.draw(ResourseManager.getInstance().batch,message,0.3f*w, 0.67f*h);
		ResourseManager.getInstance().font.draw(ResourseManager.getInstance().batch, "level "+level, 0.35f*w, 0.95f*h);
		ResourseManager.getInstance().fontSc.setScale(0.3f*w/480,0.3f*h/320);
		ResourseManager.getInstance().fontSc.draw(ResourseManager.getInstance().batch,""+record, 0.76f*w, 0.95f*h);
		ResourseManager.getInstance().fontSc.setScale(0.42f*w/480,0.42f*h/320);
		ResourseManager.getInstance().fontSc.draw(ResourseManager.getInstance().batch, String.valueOf(score), 0.3f*w, 0.5f*h);
		ResourseManager.getInstance().batch.end();
		stage.act(delta);
		stage.draw();
	}
	
	
	@Override
	public void dispose() {
		stage.dispose();
	}
	
	
	public ClickListener buttonClickListener = new ClickListener() {
		@Override
		public void clicked (InputEvent event, float x, float y) {
			if(event.getListenerActor().getName()=="start") {
				//start
				if(ifSound)	ResourseManager.getInstance().buttonSound.play(1f);
				ctrlStart.addAction(Actions.sequence(Actions.color(new Color(toRGB(2,1,1)),0.4f),Actions.run(new Runnable(){
					public void run () {
						rootGame.setLevel(level);
	    	    		dispose();
	    	    		rootGame.setScreen(rootGame.gameScreen);
					}})));    	
		    			
				}
			 if(event.getListenerActor().getName()=="back"){
				 //Menu
				 if(ifSound)	ResourseManager.getInstance().buttonSound.play(1f);
				 ctrlBack.addAction(Actions.sequence(Actions.color(new Color(toRGB(2,1,1)),0.4f),Actions.run(new Runnable(){
						public void run () {
							dispose();
		    	    		rootGame.setScreen(rootGame.menuScreen);
						}})));    	
		    	    		
		    			}
				 
				 if(event.getListenerActor().getName()=="nextLvl") {
					 if(ifSound) ResourseManager.getInstance().buttonSound.play(1f);
					 nextLvl.addAction(Actions.sequence(Actions.color(new Color(toRGB(2,1,1)),0.4f),Actions.run(new Runnable(){
							public void run () {
								rootGame.setLevel(level+1);
			    	    		rootGame.setScreen(rootGame.gameScreen);
							}}))); 
			    				
			    			}
				 }
	};

	
	
	private Color toRGB(int r, int g, int b) {
        float RED = r / 255.0f;
        float GREEN = g / 255.0f;
        float BLUE = b / 255.0f;
        return new Color(RED, GREEN, BLUE, 1);
}
	
	@Override
	public void hide() {
	//	ResourseManager.getInstance().dispose();
	}

	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}


	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	
}
