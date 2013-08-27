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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.me.snake.ResourseManager;
import com.me.snake.RootGame;

public class MenuScreen implements Screen {

	private Sprite background, snMenuSp,starSp;
	private RootGame rootGame;
	private OrthographicCamera camera;
	private float w = Gdx.graphics.getWidth();
	private float h = Gdx.graphics.getHeight();
	private Stage stage;
	private Actor actor;
	private TextButton selectLevelButt;
	private Table table;
	private TextButton fastGame;
	private ArrayList scoresArr;
	private String bestScore;
	private Texture backgroundTx;

	public MenuScreen(RootGame rootGame) {
		this.rootGame = rootGame;
	}

	@Override
	public void show() {
		System.out.println("show Menu!");
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		
		stage = new Stage(0, 0, true);
	    table = new Table();
		actor = new Actor();
		stage.addAction(Actions.color(new Color(1, 1, 1, 0))); //задали макс прозорість
		stage.addAction(Actions.color(new Color(1, 1, 1, 1), 0.5f)); //запустили екшн
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

		camera = new OrthographicCamera(320, 480);
		
		//backgroundTx = new Texture(Gdx.files.internal("data/bg.png"));
		//backgroundTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		
		background = new Sprite(ResourseManager.getInstance().backgroundTx);
		background.setSize(w, h);
		background.setPosition(0,0);		
		
		
		snMenuSp=new Sprite(ResourseManager.getInstance().menuTx);
		snMenuSp.setSize(0.6f*512*w/480, 0.6f*512*h/320);
		snMenuSp.setPosition(0.05f*w, 0.15f*h);
		
		
		starSp = new Sprite(ResourseManager.getInstance().recordTx);
		starSp.setSize(1.05f*128*w/480, 1.1f*128*h/320);
		starSp.setPosition(0.72f*w, 0.62f*h);
		
		
		buttonStyle.font = ResourseManager.getInstance().font;
		buttonStyle.font.setScale(0.6f*w/480, 0.6f*h/320);
	    buttonStyle.downFontColor = new Color(toRGB(227, 112, 30));
		
		fastGame=new TextButton("Fast game", buttonStyle);
		fastGame.setPosition(0.4f*w, 0.47f*h);
	    fastGame.addListener(new ClickListener() {
	    	@Override
	    	public void touchUp(InputEvent event, float x, float y,
	    			int pointer, int button) {
	    				rootGame.setLevel(0);
	    	    		dispose();
	    	    		rootGame.setScreen(rootGame.gameScreen);
	    		
	    	}
		});
		
		selectLevelButt = new TextButton("Select level", buttonStyle);
		selectLevelButt.setPosition(0.4f*w, 0.28f*h);
	    selectLevelButt.addListener(new ClickListener() {
	    	@Override
	    	public void touchUp(InputEvent event, float x, float y,
	    			int pointer, int button) {
	    	    		dispose();
	    	    		rootGame.setScreen(rootGame.selectLevel);
	    	}
		});
	    stage.addActor(fastGame);
	    stage.addActor(selectLevelButt);
	    stage.addListener(new ClickListener(){
	    	@Override
	    	public boolean keyDown(InputEvent event, int keycode) {
	    		if (keycode == Keys.BACK) {
	    			System.out.println("Back pressed!");
	    			dispose();
	    			ResourseManager.getInstance().dispose();
	    		}
	    		return true;
	    	}
	    });
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
		System.out.println("DISPOSE!");
		stage.dispose();
		
	}

	private Color toRGB(int r, int g, int b) {
        float RED = r / 255.0f;
        float GREEN = g / 255.0f;
        float BLUE = b / 255.0f;
        return new Color(RED, GREEN, BLUE, 1);
}
	
	
	@Override
	public void resume() {
		System.out.println("RESUME!");
	}
	
	@Override
	public void hide() {
		System.out.println("HIDE!Menu");
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
}

