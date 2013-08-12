package com.me.snake.screens;

import java.io.IOException;
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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.me.snake.RootGame;

public class MenuScreen implements Screen, InputProcessor {

	private Texture fontTx ,bgTx, recordTx, menuTx;
	private Sprite background, snMenuSp,starSp;
	private RootGame rootGame;
	private SelectLevel   selectLevel;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private float w = Gdx.graphics.getWidth();
	private float h = Gdx.graphics.getHeight();
	private Stage stage;
	private Actor actor;
	private BitmapFont font, fontSc;
	private TextButton selectLevelButt;
	private Table table;
	private TextButton fastGame;
	private ArrayList scoresArr;
	private String bestScore;


	public MenuScreen(RootGame rootGame) {
		this.rootGame = rootGame;
	}

	@Override
	public void show() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		stage = new Stage(0, 0, true);
	    table = new Table();
		actor = new Actor();
		table.add(actor);
		
		Json json = new Json();
		FileHandle handle = Gdx.files.local("scores.txt");
	    if (!handle.exists()) {
			   try {
			    handle.file().createNewFile();
			    String jsonText="[0,0,0,0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0]";
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

		camera = new OrthographicCamera(320, 480);
		batch = new SpriteBatch();

		bgTx = new Texture(Gdx.files.internal("data/bg.png"));
		bgTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		background = new Sprite(bgTx);
		background.setSize(w, h);
		background.setPosition(0,0);
		
		recordTx= new Texture(Gdx.files.internal("data/record.png"));
		recordTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		starSp = new Sprite(recordTx);
		starSp.setSize(0.95f*w, 1.1f*h);
		starSp.setPosition(0.8f*w, 0.66f*h);
		
		menuTx = new Texture(Gdx.files.internal("data/SnakeMenu.png"));
		menuTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		snMenuSp=new Sprite(menuTx);

		snMenuSp.setSize(0.73f*512*w/480, 0.73f*512*h/320);
		snMenuSp.setPosition(0.13f*w, 0.2f*h);
		
		fontTx = new Texture(Gdx.files.internal("data/font/snake.png"));
		fontTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font = new BitmapFont(Gdx.files.internal("data/font/snake.fnt"),
				new TextureRegion(fontTx), false);
		buttonStyle.font = font;
		buttonStyle.font.setScale(1f*w/480, 1f*h/320);
	    buttonStyle.downFontColor = new Color(toRGB(2, 1, 1));
		
		fontSc = new BitmapFont(Gdx.files.internal("data/font/wal.fnt"),
				new TextureRegion(fontTx), false);
		fontSc.setScale(2f*w/480,2f*h/320);
		
		fastGame=new TextButton("Fast game", buttonStyle);
		fastGame.setPosition(0.38f*w, 0.47f*h);
	    fastGame.addListener(new ClickListener() {
	    	@Override
	    	public void touchUp(InputEvent event, float x, float y,
	    			int pointer, int button) {
	    		rootGame.setLevel(0);
	    		dispose();
	    		rootGame.setScreen(rootGame.gameScreen);
	    	//	return super.touchUp(event, x, y, pointer, button);
	    	}
		});
		
		
		selectLevelButt = new TextButton("Select level", buttonStyle);
		selectLevelButt.setPosition(190, 90);
	    selectLevelButt.addListener(new ClickListener() {
	    	@Override
	    	public void touchUp(InputEvent event, float x, float y,
	    			int pointer, int button) {
	    		dispose();
	    		rootGame.setScreen(rootGame.selectLevel);
	    		//return super.touchDown(event, x, y, pointer, button);
	    	}
		});
	    stage.addActor(fastGame);
	    stage.addActor(selectLevelButt);
		Gdx.input.setInputProcessor(stage);
	} 
	

	public void resize(int width, int height) {
		stage.setViewport(width, height, true);
		table.setPosition(stage.getWidth() / 2 - actor.getWidth() / 2, stage.getHeight() / 2 - actor.getHeight() / 2);
		}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
	
		background.draw(batch);
		snMenuSp.draw(batch);
		starSp.draw(batch);
		fontSc.draw(batch,bestScore, 0.86f*w,0.83f*h);
		
		batch.end();
		stage.act(delta);
		stage.draw();
		}

	@Override
	public void dispose() {
		font.dispose();
		stage.dispose();
		fontTx.dispose();
		bgTx.dispose();
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
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return true;

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
