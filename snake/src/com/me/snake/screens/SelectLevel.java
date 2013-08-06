package com.me.snake.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.me.snake.PagedScrollPane;
import com.me.snake.RootGame;

public class SelectLevel implements Screen {// extends ApplicationAdapter { //TODO:імплемент Screen 
	private Skin skin;
	private Stage stage;
	private Table container;
	private Texture tileTx, starTx, background, lockedTx;
	float w,h;
	private SpriteBatch batch;
	private Sprite bgSp, tileSp, lockedSp;
	private RootGame rootGame;

	public SelectLevel(RootGame rootGame) {
		this.rootGame = rootGame;
	}
	
	

	@Override
	public void show() {
		batch = new SpriteBatch();
		 w = Gdx.graphics.getWidth();
		 h = Gdx.graphics.getHeight();
		tileTx=new Texture(Gdx.files.internal("data/lvlTile.png"));
		tileTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		tileSp=new Sprite(tileTx);
	//	tileSp.setSize(0.3f*w, 0.1f*h);
		
		starTx=new Texture(Gdx.files.internal("data/star.png"));
		starTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		lockedTx=new Texture(Gdx.files.internal("data/lockedImg.png"));
		lockedTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		background = new Texture(Gdx.files.internal("data/bg.png"));
		background.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		bgSp = new Sprite(background);
		bgSp.setPosition(0, 0);
		bgSp.setSize(w, h);

		stage = new Stage(0, 0, false);
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		skin.add("star", starTx);
		skin.add("tile", tileTx);
		skin.add("locked", lockedTx);
		 skin.add("star-filled", skin.newDrawable("white", Color.YELLOW), Drawable.class);
		   skin.add("star-unfilled", skin.newDrawable("white", Color.GRAY), Drawable.class);
		
		Gdx.input.setInputProcessor(stage);

		container = new Table();
		stage.addActor(container);
		container.setFillParent(true);

		PagedScrollPane scroll = new PagedScrollPane();
		scroll.setFlingTime(0.1f);
		scroll.setPageSpacing(25);
		int c = 1;
		for (int l = 0; l < 3; l++) {
			Table levels = new Table();
			levels.defaults().padTop(10).height(0.2f*h);
		    levels.row();
		     levels.add(getLevelButton(c++)).expand().fill();
		     levels.add(getLevelButton(c++)).expand().fill();
		     levels.add(getLevelButton(c++)).expand().fill();
		     levels.add(getLevelButton(c++)).expand().fill();
			levels.row();
			 levels.add(getLevelButton(c++)).expand().fill();
			 levels.add(getLevelButton(c++)).expand().fill();
			levels.row();
			 levels.add(getLevelButton(c++)).expand().fill();
			scroll.addPage(levels);
		}

		 container.add(scroll).expand().fill();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		batch.begin();
		bgSp.draw(batch);
		batch.end();
		stage.draw();
		
	}


/*	public void create () {
		
	}
  
  public void render () {
		
	}
	
	*/

	public void resize (int width, int height) {
		stage.setViewport(width, height, false);
	}

	public void dispose () {
		stage.dispose();
		skin.dispose();
	}

	public boolean needsGL20 () {
		return false;
	}

	 
	/**
	 * Creates a button to represent the level
	 * 
	 * @param level
	 * @return The button to use for the level
	 */
	public Button getLevelButton(int level) {
		Button button = new Button(skin);
		ButtonStyle style = button.getStyle();
		style.up = 	style.down = null;
		
		// Create the label to show the level number
		Label label = new Label(Integer.toString(level), skin);
		label.setFontScale(0.3f);
		label.setAlignment(Align.center);
		
		// Stack the image and the label at the top of our button
		Image img=new Image(skin.getDrawable("tile"));
		Image lockedImg=new Image(skin.getDrawable("locked"));
		img.setScale(0.9f, 1.6f);
		lockedImg.setScale(1f, 0.9f);
		button.stack(img,label, lockedImg);
		//button.stack(img,label);

		// Randomize the number of stars earned for demonstration purposes
		////////////////////////////////////
		int stars = 3;
		Table starTable = new Table();
		starTable.defaults().pad(5);
		if (stars >= 0) {
			for (int star = 0; star < 3; star++) {
				if (stars > star)  starTable.add(new Image(skin.getDrawable("star"))).width(20).height(20); 
			}			
		}///////////////////////////////////////////////////////
		
		button.row();
		button.add(starTable).height(20); //Висоту нада задавати!
		
		button.setName("Level" + Integer.toString(level));
		button.addListener(levelClickListener);		
		return button;
	}
	
	/**
	 * Handle the click - in real life, we'd go to the level
	 */
	public ClickListener levelClickListener = new ClickListener() {
		@Override
		public void clicked (InputEvent event, float x, float y) {
			System.out.println("Click: " + event.getListenerActor().getName());
		}
	};

	


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