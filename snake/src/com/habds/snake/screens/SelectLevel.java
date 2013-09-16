package com.habds.snake.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.habds.snake.PagedScrollPane;
import com.habds.snake.ResourseManager;
import com.habds.snake.RootGame;

public class SelectLevel implements Screen { 
	private Table container;
	private Image ctrlPanel;
	float w,h;
	private Sprite bgSp, osnova;
	private RootGame rootGame;
	private GameScreen gameScreen;
	private ArrayList scoresArr;
	private int unlockedLvl;
	private int checkedLvl;
	private ArrayList<Button> buttonArr;
	private Label score;
	private Image ctrlStart, ctrlBack;
	private boolean ifSound;
	private LabelStyle labelStyle;
	private Stage stage;


	public SelectLevel(RootGame rootGame) {
		this.rootGame = rootGame;
	}
	
	

	@Override
	public void show() {
		stage=new Stage(0,0,false);
		stage.addAction(Actions.color(new Color(1, 1, 1, 0))); //задали макс прозорість
		stage.addAction(Actions.color(new Color(1, 1, 1, 1), 0.5f)); //запустили екшн
		ResourseManager.getInstance().fontSc.setScale(1);
		labelStyle=new LabelStyle(ResourseManager.getInstance().fontSc, Color.WHITE);
		
		 w = Gdx.graphics.getWidth();
		 h = Gdx.graphics.getHeight();
		 ifSound=rootGame.ifSound();
		 getScore(0);
		 unlockedLvl=((Float) scoresArr.get(scoresArr.size()-1)).intValue();
		 buttonArr=new ArrayList<Button>();
		 
		 checkedLvl=-1;
		 
		bgSp = new Sprite(ResourseManager.getInstance().background);
		bgSp.setPosition(0, 0);
		bgSp.setSize(w, h);
		
		osnova=new Sprite(ResourseManager.getInstance().atlas.createSprite("osnova"));
		osnova.setSize(0.5f*w, 0.5f*w/1.61f);
		osnova.setPosition(0.5f*w, 0);
		
		ctrlStart=new Image(ResourseManager.getInstance().atlas.findRegion("start"));
		ctrlStart.setSize(0.45f*256*w/480, 0.45f*256*h/320);
		ctrlStart.setPosition(0.73f*w, 0.2f*h);
		ctrlStart.setName("start");
		ctrlStart.addListener(buttonClickListener);
		
		
		ctrlBack=new Image(ResourseManager.getInstance().atlas.findRegion("back"));
		ctrlBack.setOrigin(ctrlBack.getWidth()/2, ctrlBack.getHeight()/2);
		if(!ResourseManager.getInstance().isBig){
			//ctrlBack.setRotation(-90); 
			ctrlBack.setPosition(0.495f*w, 0);
		}else {
				ctrlBack.setSize(0.185f*w, 0.185f*w/1.35f);
				ctrlBack.setPosition(0.495f*w, 0);
			}
		
		ctrlBack.setName("back");
		ctrlBack.addListener(buttonClickListener);
		
		Gdx.input.setInputProcessor(stage);
		
		score = new Label("",labelStyle);
		score.setFontScale(0.3f*w/ResourseManager.getInstance().standartW, 0.3f*h/ResourseManager.getInstance().standartH);
		score.setPosition(0.8f*w, 0.1f*h);
			
		container = new Table();
		stage.addActor(container);
		stage.addActor(ctrlStart);
		stage.addActor(ctrlBack);
		//TODO
		stage.addActor(score);
		stage.addListener(new ClickListener(){
		    	@Override
		    	public boolean keyDown(InputEvent event, int keycode) {
		    		if (keycode == Keys.BACK) {
		    			ResourseManager.getInstance().dispose();
		    		}
		    		return true;
		    	}
		    });
		
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
		ResourseManager.getInstance().batch.begin();
		bgSp.draw(ResourseManager.getInstance().batch);
		osnova.draw(ResourseManager.getInstance().batch);
		ResourseManager.getInstance().batch.end();
		stage.act(delta);
		stage.draw();
	}


	public void resize (int width, int height) {
		stage.setViewport(width, height, true);
	}

	public void dispose () {
		//ResourseManager.getInstance().stage.clear();
		stage.dispose();
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
		ButtonStyle buttonStyle=new ButtonStyle();
		buttonStyle.up=buttonStyle.down=null;
		Button button = new Button(buttonStyle);
		Label label = null;
		if(ResourseManager.getInstance().isBig) 
			if(w<1700)
				if(level<10) label = new Label("  "+String.valueOf(level), labelStyle); else  label = new Label(" "+String.valueOf(level), labelStyle);
			else if(level<10) label = new Label(" "+String.valueOf(level), labelStyle); else  label = new Label(""+String.valueOf(level), labelStyle);
		else {
			label = new Label(String.valueOf(level), labelStyle); 
			label.setAlignment(Align.center);
		}// більше 1700 - нічо не давати на двойні. на одинарні давати 2
		
		label.setFontScale(0.3f*w/ResourseManager.getInstance().standartW, 0.3f*h/ResourseManager.getInstance().standartH);
		// Stack the image and the label at the top of our button
		Image img=new Image(ResourseManager.getInstance().atlas.findRegion("tile"));
		Image lockedImg=new Image(ResourseManager.getInstance().atlas.findRegion("lockedImg"));
		if(ResourseManager.getInstance().isBig) {
			img.setScale(0.8f,1.1f);
			lockedImg.setScale(0.8f,1.1f);
		} else{
			img.setScale(1f*w/480, 1.6f*h/320);
			lockedImg.setScale(1.3f*w/480,1.3f*h/320);
		}
		
		if(unlockedLvl>=level) button.stack(img,label);	else   button.stack(img,label, lockedImg);
	
		// Randomize the number of stars earned for demonstration purposes
		////////////////////////////////////
		int stars =getStar(level);
		Table starTable = new Table();
		starTable.defaults().pad(5);
		if (stars > 0) {
			for (int star = 0; star < 3; star++) {
				if (stars > star)  starTable.add(new Image(ResourseManager.getInstance().atlas.findRegion("star"))).width(20).height(20); 
			} 
		} else if(level<unlockedLvl){
			starTable.add(new Image(ResourseManager.getInstance().atlas.findRegion("completed"))).width(20).height(20);
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
			}
			
		}
	};
	
	public ClickListener buttonClickListener = new ClickListener() {
		@Override
		public void clicked (InputEvent event, float x, float y) {
			if(event.getListenerActor().getName()=="start") { 
				if(checkedLvl<=unlockedLvl && checkedLvl>-1) {
					if(ifSound)	ResourseManager.getInstance().buttonSound.play(1f);
					stage.addAction(Actions.color(new Color(1, 1, 1, 1))); //задали макс прозорість
					stage.addAction(Actions.color(new Color(1, 1, 1, 0), 0.5f)); //запустили екшн
		    				ctrlStart.addAction(Actions.sequence(Actions.color(new Color(toRGB(37,89,115)),0.4f),Actions.run(new Runnable(){
		    					public void run () {
		    						rootGame.setLevel(checkedLvl);
                                	dispose();
        		    	    		rootGame.setScreen(rootGame.gameScreen);
		    					}
		    				})));
                                	
                            }
		    	    		
		} else if(event.getListenerActor().getName()=="back"){ 
			if(ifSound)	ResourseManager.getInstance().buttonSound.play(1f);
			stage.addAction(Actions.color(new Color(1, 1, 1, 1))); //задали макс прозорість
			stage.addAction(Actions.color(new Color(1, 1, 1, 0), 0.5f)); //запустили екшн
			ctrlBack.addAction(Actions.sequence(Actions.color(new Color(toRGB(37,89,115)),0.4f),Actions.run(new Runnable(){
				public void run () {
					dispose();
    	    		rootGame.setScreen(rootGame.menuScreen);
				}})));    		
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
		if(score>=rootGame.NEED_POINTS+3) {
			stars++;
		}
		if(score>=rootGame.NEED_POINTS+6){
			stars++;
		}
		if(score>=rootGame.NEED_POINTS+9){
			stars++; 
		} //Тіпа крутий парсер зірок
		return stars;
	}

	
	private Color toRGB(int r, int g, int b) {
        float RED = r / 255.0f;
        float GREEN = g / 255.0f;
        float BLUE = b / 255.0f;
        return new Color(RED, GREEN, BLUE, 1);
}

	@Override
	public void hide() {
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