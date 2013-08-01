package com.me.snake.screens;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
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

public class SelectLevel implements Screen, InputProcessor {
	private RootGame rootGame;
	private GameScreen gameScreen;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture, controlTx, fontTx, completedTx, lockTx, starTx;
	private Stage stage;
	private Actor actor;
	private Sprite background,starSp, closedSp, completedSp;
	private BitmapFont font;
	private TextButton playButton, menuButton, fastGame;
	private int level;
	float w = Gdx.graphics.getWidth();
	float h = Gdx.graphics.getHeight();
	private Sprite controlSp;
	private ArrayList scoresArr = new ArrayList();
	private int unlockedLvl;

	public SelectLevel(RootGame rootGame) {
		this.rootGame = rootGame;
	}

	public void resize(int width, int height) {
		stage.setViewport(width, height, true);
	}

	@Override
	public void show() {
		rootGame.setLevel(level);
		camera = new OrthographicCamera(320, 480);
		batch = new SpriteBatch();
		stage = new Stage(0, 0, true);
		actor = new Actor();
		final TextButtonStyle buttonStyle = new TextButtonStyle();

		texture = new Texture(Gdx.files.internal("data/bgMenu.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		background = new Sprite(texture);
		background.setSize(w,h);
		background.setPosition(0, 0);
		
		starTx= new Texture(Gdx.files.internal("data/star.png"));
		starTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		starSp = new Sprite(starTx);
		starSp.setScale(0.3f);
		
		lockTx=new Texture(Gdx.files.internal("data/lock.png"));
		lockTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		closedSp=new Sprite(lockTx);
		closedSp.setPosition(0, 100);
		
		completedTx=new Texture(Gdx.files.internal("data/completed.png"));
		completedTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		completedSp=new Sprite(completedTx);
		completedSp.setPosition(0.38f*w, 1/16f*h);
		completedSp.setSize(0.16f*w, 0.19f*h);
		
		fontTx = new Texture(Gdx.files.internal("data/font/neucha.png"));
		fontTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font = new BitmapFont(Gdx.files.internal("data/font/neucha.fnt"),
				new TextureRegion(fontTx), false);
		font.setScale(0.6f);
		
		controlTx = new Texture(Gdx.files.internal("data/control.png"));
		controlTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		controlSp = new Sprite(controlTx);
		buttonStyle.font = font;
		buttonStyle.downFontColor = new Color(toRGB(2, 1, 1));

		playButton = new TextButton("Play", buttonStyle);
		playButton.setPosition(0.75f*w, 0);
		playButton.addActor(actor);
		playButton.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if(level<=unlockedLvl) {  //якшо лвл відкритий то кнопка працює
				rootGame.setLevel(level);
				rootGame.setScreen(rootGame.gameScreen);
				} 
				}
		
		});
		menuButton = new TextButton("Back", buttonStyle);
		menuButton.setPosition(20, -2);
		menuButton.addActor(actor);
		menuButton.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				rootGame.setScreen(rootGame.menuScreen);
			}
		});

		stage.addActor(playButton);
		stage.addActor(menuButton);
		Gdx.input.setInputProcessor(stage);

		stage.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if (x >= 0.14f * w && x <= 0.35f * w && y >= 0.4f * h
						&& y <= 0.6f * h)
					if (level > 0)
						level--;
				if (x >= 0.7f * w && x <= 0.9f * w && y >= 0.4f * h
						&& y <= 0.6f * h)
					if (level <= 15)
						level++;
				return super.touchDown(event, x, y, pointer, button);
			}
		});
		
		getScore();
		level=unlockedLvl;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.begin();
		background.draw(batch);
		drawLvlInfo();
		drawButton();
		
		batch.end();
		stage.act(delta);
		stage.draw();
	}

	public int getScore() {
		
		FileHandle handle = Gdx.files.local("scores.txt");
		Json json = new Json();
		String newText = handle.readString(); // read Json from file // sec
		scoresArr = json.fromJson(ArrayList.class, newText);
		int score=((Float) scoresArr.get(level)).intValue();
		unlockedLvl=((Float) scoresArr.get(scoresArr.size()-1)).intValue();
		return score;

	}
	
	@Override
	public void dispose() {
		font.dispose();
		fontTx.dispose();
		batch.dispose();
		texture.dispose();
		controlTx.dispose();
		completedTx.dispose();
		lockTx.dispose();
		starTx.dispose();
		stage.dispose();
	}

	
	private Color toRGB(int r, int g, int b) {
        float RED = r / 255.0f;
        float GREEN = g / 255.0f;
        float BLUE = b / 255.0f;
        return new Color(RED, GREEN, BLUE, 1);
}
	
	private void drawLvlInfo(){
		if(getScore()>=23) {
			starSp.setPosition(0.42f*w, 0.52f*h);
			starSp.draw(batch);
			
		}
		if(getScore()>=26){
			starSp.setPosition(0.57f*w, 0.52f*h);
			starSp.draw(batch);
		}
		if(getScore()>=29){
			starSp.setPosition(0.72f*w, 0.52f*h);
			starSp.draw(batch); 
		} //Тіпа крутий парсер зірок   // 400 266   240 202
		
		if(unlockedLvl>level){  //Малюємо "Пройдено"
			completedSp.draw(batch);	
		}
		
		if(level>unlockedLvl) { //малюємо цепки якшо  лвл закритий
		closedSp.draw(batch);
		}
		
		font.draw(batch, "Level: " + level, 0.35f * w, 0.65f * h);
		font.draw(batch, "Record: " + getScore(), 40, 0.92f*h);
	}
	
	private void drawButton(){
		batch.draw(controlSp, 0.15f * w, 0.45f * h, controlSp.getWidth() / 2,
				controlSp.getHeight() / 2, controlSp.getWidth(),
				controlSp.getHeight(), 1f, 1.2f, 90, false);
		batch.draw(controlSp, 0.75f * w, 0.5f * h, controlSp.getWidth() / 2,
				controlSp.getHeight() / 2, controlSp.getWidth(),
				controlSp.getHeight(), 1f, 1.3f, 90, true);
		
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
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
