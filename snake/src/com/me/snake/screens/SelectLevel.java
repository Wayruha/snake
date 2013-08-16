package com.me.snake.screens;

import java.util.ArrayList;
import javax.swing.text.StyledEditorKit.ForegroundAction;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Json;
import com.me.snake.PagedScrollPane;
import com.me.snake.ResourseManager;
import com.me.snake.RootGame;

public class SelectLevel implements Screen { 
	private Skin skin;
	private Stage stage;
	private SpriteBatch batch;
	private Table container;
	private Image ctrlPanel;
	float w,h;
	private Sprite bgSp;
	private RootGame rootGame;
	private GameScreen gameScreen;
	private ArrayList scoresArr;
	private int unlockedLvl;
	private int checkedLvl;
	private ArrayList<Button> buttonArr;
	private Label score;

	public SelectLevel(RootGame rootGame) {
		this.rootGame = rootGame;
	}
	
	

	@Override
	public void show() {
		
		stage=new Stage(0,0, false);
		//stage.addAction(Actions.color(new Color(1, 1, 1, 0))); //задали макс прозорість
	//	stage.addAction(Actions.color(new Color(1, 1, 1, 1), 0.5f)); //запустили екшн
		
		 //ResourseManager.getInstance();
		batch= new SpriteBatch();
		 w = Gdx.graphics.getWidth();
		 h = Gdx.graphics.getHeight();
		 getScore(0);
		 unlockedLvl=((Float) scoresArr.get(scoresArr.size()-1)).intValue();
		 buttonArr=new ArrayList<Button>();
		 
		bgSp = new Sprite(ResourseManager.getInstance().background);
		bgSp.setPosition(0, 0);
		bgSp.setSize(w, h);
		
		
		ctrlPanel=new Image(ResourseManager.getInstance().controlPanelTx);
		ctrlPanel.setPosition(0.495f*w, 0);
		ctrlPanel.setSize(0.5f*512*w/480, 0.45f*512*h/320);
		ctrlPanel.addListener(buttonClickListener);
		
		Gdx.input.setInputProcessor(stage);
	
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		skin.add("star", ResourseManager.getInstance().starTx);
		skin.add("tile", ResourseManager.getInstance().tileTx);
		skin.add("locked", ResourseManager.getInstance().lockedTx);
		skin.add("completed", ResourseManager.getInstance().completedTx);
		
		score = new Label("",skin);
		score.setFontScale(0.3f);
		score.setPosition(0.8f*w, 0.1f*h);
		
		
		container = new Table();
		stage.addActor(container);
		stage.addActor(ctrlPanel);
		stage.addActor(score);
		
		container.setFillParent(true);
		PagedScrollPane scroll = new PagedScrollPane();
		scroll.setFlingTime(0.1f);
		scroll.setPageSpacing(25);
		int c = 1;
		for (int l = 0; l < 2; l++) {
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
	//	font.draw(batch, String.valueOf(getScore(checkedLvl)), 0.3f*w, 0.2f*h);
		batch.end();
		stage.draw();
	}


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
		style.up = 	style.down =null;
	
		// Create the label to show the level number
	//	Label label = new Label(Integer.toString(level), skin);
		Label label = new Label(String.valueOf(level), skin);
		label.setFontScale(0.3f);
		label.setAlignment(Align.center);
		
		// Stack the image and the label at the top of our button
		Image img=new Image(skin.getDrawable("tile"));
		Image lockedImg=new Image(skin.getDrawable("locked"));
		img.setScale(0.9f, 1.6f);
		lockedImg.setSize(img.getWidth(),img.getHeight());
		
		if(unlockedLvl>=level) button.stack(img,label);	else   button.stack(img,label, lockedImg);
	
		// Randomize the number of stars earned for demonstration purposes
		////////////////////////////////////
		int stars =getStar(level);
		Table starTable = new Table();
		starTable.defaults().pad(5);
		if (stars > 0) {
			for (int star = 0; star < 3; star++) {
				if (stars > star)  starTable.add(new Image(skin.getDrawable("star"))).width(20).height(20); 
			} 
		} else if(level<unlockedLvl){
			starTable.add(new Image(skin.getDrawable("completed"))).width(20).height(20);
		}
		
		button.row();
		button.add(starTable).height(20); //Висоту нада задавати!
		button.setName(Integer.toString(level));
		button.addListener(levelClickListener);		
		buttonArr.add(button);
		return button;
	}
	
	/**
	 * Handle the click - in real life, we'd go to the level
	 */
	public ClickListener levelClickListener = new ClickListener() {
		@Override
		public void clicked (InputEvent event, float x, float y) {	
			if(Integer.parseInt(event.getListenerActor().getName())<=unlockedLvl) {
			if(checkedLvl>=0)          
				for (Button but : buttonArr) 	
					if(Integer.parseInt(but.getName())==checkedLvl) but.setColor(1, 1, 1, 1f);
		
				checkedLvl=Integer.parseInt(event.getListenerActor().getName());
				event.getListenerActor().setColor(1, 1, 1, 0.6f);
				score.setText(String.valueOf(getScore(checkedLvl)));
				//score.setAlignment(Align.center);
			}
			
		}
	};
	
	public ClickListener buttonClickListener = new ClickListener() {
		@Override
		public void clicked (InputEvent event, float x, float y) {
			if(x>=100 && y>=60 && y<=210) { 
				//start
				if(checkedLvl<=unlockedLvl) {	
		    				rootGame.setLevel(checkedLvl);
		    	    		dispose();
		    	    		rootGame.setScreen(rootGame.gameScreen);
		    			}
			} else if(x<=110 && y<=80 ){ 
				//menu
	    	    		dispose();
	    	    		rootGame.setScreen(rootGame.menuScreen);
			}
		}
	};

	
	
public int getScore(int level) {
		
		FileHandle handle = Gdx.files.local("scores.txt");
		Json json = new Json();
		String newText = handle.readString(); // read Json from file // sec
		scoresArr = json.fromJson(ArrayList.class, newText);
		int score=((Float) scoresArr.get(level)).intValue();
		return score;
	}

	
	private int getStar(int level){
		int score=getScore(level);
		int stars=0;
		if(score>=23) {
			stars++;
		}
		if(score>=26){
			stars++;
		}
		if(score>=29){
			stars++; 
		} //Тіпа крутий парсер зірок
		return stars;
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