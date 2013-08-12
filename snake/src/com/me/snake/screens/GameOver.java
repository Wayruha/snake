package com.me.snake.screens;

import java.io.BufferedWriter;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.me.snake.RootGame;

public class GameOver implements Screen, InputProcessor {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture, imgTx, recordTx, menuTx;
	private Sprite background, rakSp, recordSp;
	private Texture fontTx;
	private BitmapFont font;
	private int score,record;
	private float w,h;
	private RootGame rootGame;
	private FileHandle file;
	private String message, img;
	BufferedWriter out = null;
	private Sprite snMenuSp;
	private BitmapFont fontSc;
	private Stage stage;
	private Actor actor;
	private TextButton playButton;
	private TextButton menuButton;
	private int level;
	private ArrayList scoresArr;
	
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
		int unlockedLvl= ((Float) scoresArr.get(scoresArr.size()-1)).intValue();
		
		if(unlockedLvl==level){
			if(score<RootGame.NEED_POINTS){
				message="N00b!Level failed";
				img="rak";
			} else {
				scoresArr.set(level,score); 
				scoresArr.set(scoresArr.size()-1, unlockedLvl+1);
				img="notRak";
				message="Nice!Level completed";
			}
			if(score>record){
				message="Nice!New record!";
				scoresArr.set(level,score);
				record=score;
				img="notRak";
			}
		} else {
			if(score>record){
				message="Nice!New record!";
				scoresArr.set(level,score);
				record=score;
				img="notRak";
			} else {
				message="Your score: ";
				img="rak";
			}
		}
		
		String jsonText= json.toJson(scoresArr);
	    handle.writeString(jsonText, false);    //Записуємо новий рекорд
	}
	
	@Override
	public void show() {
		 w = Gdx.graphics.getWidth();
		 h = Gdx.graphics.getHeight();
		
		level=rootGame.getLevel();
		img="rak";

		camera = new OrthographicCamera(320, 480);
		batch = new SpriteBatch();
		stage = new Stage(0, 0, true);
		actor = new Actor();
		final TextButtonStyle buttonStyle = new TextButtonStyle();
		
		texture=new Texture(Gdx.files.internal("data/bg.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		background=new Sprite(texture);
		background.setPosition(0, 0);
		background.setSize(w,h);
		
		recordTx=new Texture(Gdx.files.internal("data/record.png"));
		recordTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		recordSp=new Sprite(recordTx);
		recordSp.setPosition(1/48f*w, 0.69f*h);
		recordSp.setSize(0.88f*w, 1.1f*h);
		
		menuTx = new Texture(Gdx.files.internal("data/SnakeMenu.png"));
		menuTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		snMenuSp=new Sprite(menuTx);
		snMenuSp.setSize(0.73f*512*w/480, 0.73f*512*h/320);
		snMenuSp.setPosition(0.1f*w, 0.2f*h);
		
		imgTx=new Texture(Gdx.files.internal("data/"+img+".png"));
		imgTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		rakSp=new Sprite(imgTx);
		rakSp.setScale(0.7f);
		rakSp.setPosition(0.19f*w, -0.2f*h);
		
		fontTx = new Texture(Gdx.files.internal("data/font/neucha.png"));
		fontTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font = new BitmapFont(Gdx.files.internal("data/font/neucha.fnt"), new TextureRegion(fontTx), false);
		font.setScale(0.57f*w/480,0.57f*h/320);
		fontSc = new BitmapFont(Gdx.files.internal("data/font/neucha.fnt"), new TextureRegion(fontTx), false);
		fontSc.setScale(0.5f*w/480,0.5f*h/320);
		buttonStyle.font = font;
		buttonStyle.downFontColor = new Color(toRGB(2, 1, 1));
		
		playButton=new TextButton("Play again", buttonStyle);
		playButton.setPosition(0.6f*w, 0);
		playButton.addActor(actor);
		playButton.addListener(new ClickListener() {
		    	@Override
		    	public void touchUp(InputEvent event, float x, float y,
		    			int pointer, int button) {
		    		dispose();
		    		rootGame.setScreen(rootGame.gameScreen);
		    	}
			});
		menuButton=new TextButton("Menu", buttonStyle);
		menuButton.setPosition(0.04f*w, -2);
		menuButton.addActor(actor);
		menuButton.addListener(new ClickListener() {
		    	@Override
		    	public void touchUp(InputEvent event, float x, float y,
		    			int pointer, int button) {
		    		dispose();
		    		rootGame.setScreen(rootGame.menuScreen);
		    	}
			});
		
		
		stage.addActor(playButton);
		stage.addActor(menuButton);
		setRecord();
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
		
		batch.begin();
		background.draw(batch);
		font.draw(batch,message,0.4f*w, 0.75f*h);
		fontSc.draw(batch,""+record, 0.08f*w, 0.85f*h);
		fontSc.draw(batch, String.valueOf(score), 0.34f*w, 0.63f*h);
		snMenuSp.draw(batch);
		rakSp.draw(batch);
		recordSp.draw(batch);
		playButton.draw(batch, 1f);
		menuButton.draw(batch, 1f);
		batch.end();
		stage.act(delta);
		stage.draw();
	}
	
	
	@Override
	public void dispose() {
		fontSc.dispose();
		stage.dispose();
		batch.dispose();
		texture.dispose();
		fontTx.dispose();
		font.dispose();
		imgTx.dispose();
		recordTx.dispose();
		menuTx.dispose();
		
	}
	
	private Color toRGB(int r, int g, int b) {
        float RED = r / 255.0f;
        float GREEN = g / 255.0f;
        float BLUE = b / 255.0f;
        return new Color(RED, GREEN, BLUE, 1);
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
	public void hide() {
		// TODO Auto-generated method stub
		
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
