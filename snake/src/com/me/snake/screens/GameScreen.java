package com.me.snake.screens;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.me.snake.PickedUpPos;
import com.me.snake.ResourseManager;
import com.me.snake.RootGame;
import com.me.snake.SnakePart;

public class GameScreen implements Screen, InputProcessor {
	private OrthographicCamera camera;
	private Actor actor;
	private Sprite snakeEat, bgSp,pauseSp, bigPause, mob, exitSp, controlSp, visibleApple;
	private Image controlUp,controlDown, controlLeft, controlRight;
	public static float SQUARE_WIDTH, SQUARE_HEIGHT;
	int pixCountWid = 20, pixCountHei = 14, eatArrX, eatArrY, wayOld,
			wayNew;
	private Random random = new Random();
	private int[][] map = new int [pixCountWid][pixCountHei];
	private boolean ifAccelerate;
	private float time = 0, mobsTime=0;
	private SnakePart headPart, snakeTail;
	private ArrayList<SnakePart> parts;
	private ArrayList<PickedUpPos> pickedUp;
	private ArrayList<Sprite> wallsSp;
	ArrayList<ArrayList<Byte>> mobsWay;  
	private  char[] charArray;
	private float speed, accelerate, mobSpeed=0.5f;
	private RootGame rootGame;
	private BitmapFont font, fontDone;
	float w = Gdx.graphics.getWidth();
	float h = Gdx.graphics.getHeight();
	private int level;
	private boolean ifPause;
	private int score;
	private int i;
	private float alpha, appleAlpha;
	private float timeAfterShowCompleted;
	private boolean visible;
	
	public GameScreen(RootGame rootGame) {
		this.rootGame = rootGame;
	}
	
	
	@Override
	public void resize(int width, int height) {

	}



	@Override
	public void show() {
		SQUARE_WIDTH = w / pixCountWid; // задаємо ш\в клітинки
		SQUARE_HEIGHT = h / pixCountHei;
		//stage=new Stage(0,0,true);
		//stage.addAction(Actions.color(new Color(1, 1, 1, 0))); //задали макс прозорість
		//stage.addAction(Actions.color(new Color(1, 1, 1, 1), 0.5f)); //запустили екшн
		ifAccelerate=false;
		speed=0.5f;
		accelerate=0.04f;
		level=rootGame.getLevel();
		i=0;
		score=0;
		alpha=0f;
		appleAlpha=0f;
		timeAfterShowCompleted=0;
		visible=false;
		ifPause=false;
		for (int i = 0; i < pixCountWid; i++) {
			for (int j = 0; j < pixCountHei; j++) {
				map[i][j] = 0;
			}
		}
		mobsWay= new ArrayList();
		parts = new ArrayList<SnakePart>();
		pickedUp= new ArrayList<PickedUpPos>();
		wallsSp=new ArrayList<Sprite>();
		wayNew=3;
		wayOld=3;
				
			
		camera = new OrthographicCamera(320, 480);
		
		bgSp=new Sprite(ResourseManager.getInstance().backgroundGame);
		bgSp.setPosition(0, 0);
		bgSp.setSize(w, h);
		headPart = new SnakePart(pixCountWid / 2, pixCountHei / 2, "head");
		parts.add(headPart);
		
		controlSp=new Sprite(ResourseManager.getInstance().controlBut);
		controlSp.setSize(0.1f*128*w/480, 2f*128*h/320);
		
		drawCtrlBut();
		
		pauseSp=new Sprite(ResourseManager.getInstance().pauseTx);
		pauseSp.setSize(0.1f*w, 0.15f*h);
		pauseSp.setPosition(0, 13*SQUARE_HEIGHT);
		
		bigPause=new Sprite(ResourseManager.getInstance().pauseTx);
		bigPause.setSize(0.5f*w, 0.6f*h);
		bigPause.setPosition(0.27f*w, 0.38f*h);
	
		exitSp=new Sprite(ResourseManager.getInstance().exitTx);
		exitSp.setSize(0.1f*w, 0.15f*h);
		exitSp.setPosition(18*SQUARE_WIDTH, 13*SQUARE_HEIGHT);
		
		snakeEat = new Sprite(ResourseManager.getInstance().eatTx);
		snakeEat.setSize(SQUARE_WIDTH, SQUARE_HEIGHT);
		
		visibleApple=new Sprite(ResourseManager.getInstance().eatTx);
		visibleApple.setSize(SQUARE_WIDTH, SQUARE_HEIGHT);
		
		font = new BitmapFont(Gdx.files.internal("data/font/neucha.fnt"), new TextureRegion(ResourseManager.getInstance().fontScTx), false);
		font.setScale(0.35f);
		
		fontDone = new BitmapFont(Gdx.files.internal("data/font/neucha.fnt"), new TextureRegion(ResourseManager.getInstance().fontScTx), false);
		fontDone.setScale(0.5f);
		
		mob=new Sprite(ResourseManager.getInstance().borderTx);
		mob.setSize(SQUARE_WIDTH, SQUARE_HEIGHT);
		
		readArr();
		mobMove();
		
		drawWalls();
		
		Gdx.input.setInputProcessor(this);
		pickUp();
	}

	
	private void drawCtrlBut(){
		controlUp=new Image(ResourseManager.getInstance().controlBut);
		controlUp.rotate(270);
		controlUp.setName("ctrlUp");
		controlUp.setPosition(-2, 7*SQUARE_HEIGHT);
		
		controlUp=new Image(ResourseManager.getInstance().controlBut);
		controlUp.rotate(90);
		controlUp.setName("ctrlDown");
		controlUp=new Image(ResourseManager.getInstance().controlBut);
		controlUp.rotate(180);
		controlUp.setName("ctrlLeft");
		controlUp=new Image(ResourseManager.getInstance().controlBut);
		controlUp.setName("ctrlRight");
	
	}
	
	private boolean readArr (){
		FileHandle handle = Gdx.files.internal("data/mobs/level"+level+".txt");
		if(handle.exists()){
		String jsonText=handle.readString();  //Зчитали массив з файлу
		JSONParser parser = new JSONParser();
		Object obj = null;
		try {
			obj = parser.parse(jsonText);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	
		JSONArray array=(JSONArray)obj;   //Конвертуємо стрічку в массив! 
		
		for (int i = 0; i < array.size(); i++) {
			JSONArray arrayList =  (JSONArray) array.get(i); //Робимо аррайліст ДЖСОНАрраїв
			ArrayList<Byte> bytes = new ArrayList<Byte>(); 
			for(int j = 0; j < arrayList.size(); j++){
				Byte point = Byte.valueOf(arrayList.get(j).toString()); //поштучно конвертуємо значення і записуємо в аррліст.
				bytes.add(point);
			}
			mobsWay.add(bytes);
		}
		return true;
      }
		return false;
	}

	private void pickUp() {
		score++;
		if(score>1)
		{
			visibleApple.setPosition(snakeEat.getX(), snakeEat.getY());
			appleAlpha=1f;
		}
		boolean goodPos = false;
		int randX;
		int randY;
		
		while(!goodPos){
			randX = random.nextInt(pixCountWid);
			randY = random.nextInt(pixCountHei);
			if(map[randX][randY]==0){
				pickedUp.add(new PickedUpPos(eatArrX, eatArrY));
				snakeEat.setPosition(randX*SQUARE_WIDTH, randY*SQUARE_HEIGHT);
				eatArrX = randX;
				eatArrY = randY;
				ifAccelerate=true;
				goodPos = true;     //flag to leave while cycle
			}
		}
	}

	private void createWallSp (int mapX, int mapY, String type){
		map[mapX][mapY]=1;
		Sprite sp = (type=="border") ? new Sprite(ResourseManager.getInstance().borderTx):new Sprite(ResourseManager.getInstance().wallTx);
		sp.setSize(SQUARE_WIDTH, SQUARE_HEIGHT);
		sp.setPosition(mapX*SQUARE_WIDTH, mapY*SQUARE_HEIGHT);
		wallsSp.add(sp);
	}
	
	private void mobMove(){
		if(readArr()){
		if( i>=mobsWay.size()) i=0;
		mob.setPosition(mobsWay.get(i).get(0) *SQUARE_WIDTH, mobsWay.get(i).get(1)*SQUARE_HEIGHT);
		if(map[mobsWay.get(i).get(0)][mobsWay.get(i).get(1)]==1) {
			 ///Програвв
			dispose();
			rootGame.gameOver.setScore(parts.size());
			rootGame.setScreen(rootGame.gameOver);
		}
	    i++;
	}
	}
	
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if(ifPause== false) {
			step(delta);
		}
		
		ResourseManager.getInstance().batch.begin();
		
		bgSp.draw(ResourseManager.getInstance().batch);
		for(Sprite wall:wallsSp){
			wall.draw(ResourseManager.getInstance().batch);
		}
		drawSnake();
		completedLvlText(delta);
		snakeEat.draw(ResourseManager.getInstance().batch);
		ateApple();
		pauseSp.draw(ResourseManager.getInstance().batch);
		exitSp.draw(ResourseManager.getInstance().batch);
		ctrlButDraw();
		font.draw(ResourseManager.getInstance().batch, "score: "+ score, 9*SQUARE_WIDTH, 14*SQUARE_HEIGHT);
		mob.draw(ResourseManager.getInstance().batch);
		if(ifPause==true) {
			bigPause.draw(ResourseManager.getInstance().batch, 0.6f);
		}
		
		ResourseManager.getInstance().batch.end();

	}
	
	public void move() {
		if (wayNew-wayOld==2 || wayNew-wayOld==-2)
			wayNew=wayOld; else wayOld=wayNew;
			// Міняти координати екземплярів на попередні ( вище по списку) (переміщення)
					if (parts.size() > 1) {
						map[parts.get(parts.size()-1).getMapX()][parts.get(parts.size()-1).getMapY()]=0;
						for (int i=parts.size()-1; i>0; i--) {
							parts.get(i).setNewPos(parts.get(i - 1).getMapX(), parts.get(i-1).getMapY());
							map[parts.get(i).getMapX()][parts.get(i).getMapY()]=1;
						}
					}
					
		turnHead(wayNew);

		if(map[headPart.getMapX()][headPart.getMapY()]==1 || (Math.round(mob.getX()/SQUARE_WIDTH)==headPart.getMapX() && Math.round(mob.getY()/SQUARE_HEIGHT)==headPart.getMapY())) {
		 ///Програв
			dispose();
			rootGame.gameOver.setScore(score);
			rootGame.setScreen(rootGame.gameOver);
		}
	}

	private void drawWalls (){
		FileHandle file= Gdx.files.internal("data/levels/level"+level+".txt");
		String charString=file.readString();
		charString=charString.replace("\r\n", "");
		charArray=charString.toCharArray();
		
		for(int index=0; index < charArray.length; index++){
			  int i=index/pixCountWid;
			  int j=index%pixCountWid;
			  if(charArray[index]=='1') {
				  createWallSp(j, pixCountHei-i-1, "wall");   //Магія)))
			  }
		} 
		
		for (int i=0; i<pixCountWid;i++){
			createWallSp(i, 0, "border");
			createWallSp(i, pixCountHei-1,  "border");
		}
		for (int j=0; j<pixCountHei;j++){
			createWallSp(0,j, "border");
			createWallSp(pixCountWid-1, j, "border");
		}
	}
	
	@Override
	public  boolean touchDown(int screenX, int screenY, int pointer, int button) {
	//  system.out.println("x: " + screenX/SQUARE_WIDTH + ";  Y: "+screenY/SQUARE_HEIGHT);
		if (screenX<=2*SQUARE_WIDTH && screenY>4*SQUARE_HEIGHT && screenY<=8*SQUARE_HEIGHT)  this.wayNew = 2;
		if (screenX>= 3*SQUARE_WIDTH && screenX<=6*SQUARE_WIDTH && screenY>=h-4*SQUARE_HEIGHT)  this.wayNew = 1;
		if (screenX>= 13*SQUARE_WIDTH && screenX<= 17*SQUARE_WIDTH && screenY>=12*SQUARE_HEIGHT) this.wayNew=3;
		if (screenX>= 18*SQUARE_WIDTH && screenY>=4*SQUARE_HEIGHT && screenY<=8*SQUARE_HEIGHT) this.wayNew=4;
		if(screenX<=2*SQUARE_WIDTH && screenY<=0.2f*h) ifPause=true;     // Пауза
		if(screenX>=17*SQUARE_WIDTH && screenY<=2*SQUARE_HEIGHT) {
			 //вертаємось в меню.
			dispose();
			rootGame.setScreen(rootGame.menuScreen);
		}
		if(ifPause==true) if(screenX<0.8f*w && screenX>0.3f*w && screenY>0.2f*h && screenY<0.6f*h) ifPause=false;
		return false;
	}
	
	

	@Override
	public void dispose() {
		font.dispose();
		fontDone.dispose();
		//ResourseManager.getInstance().dispose();
		
	}

	private void step(float delta){
		if(parts.size()%8==0 & ifAccelerate==true ) {
			accelerate-=0.003f;
			ifAccelerate=false;
		}
		
		if(parts.size()%3==0 & ifAccelerate==true) {
			speed-=accelerate;
			ifAccelerate=false;
		}
		time +=delta;
		mobsTime+=delta;
		if(mobsTime>mobSpeed){
			mobMove();
			mobsTime=0;
		}
		
		if (time > speed) {
			move(); //крок	
			if (parts.get(0).getMapX() == eatArrX && parts.get(0).getMapY() == eatArrY) {
			pickUp(); //з`їли
		}	
		
		//Розмноження
		for (Iterator<PickedUpPos> it=pickedUp.iterator(); it.hasNext();) {
			PickedUpPos pos=it.next();
			if (pos.addToBack) {         //Додаємо новий кусок
				parts.add(new SnakePart(pos.mapX, pos.mapY,""));
				it.remove();
				continue;
			} 
		
			if (parts.get(parts.size() - 1).getMapX() == pos.mapX
					&& parts.get(parts.size()-1).getMapY() == pos.mapY) {
				pos.addToBack = true;
			}

		}
		
		time = 0;
	}
	}
	
	private void drawSnake(){
		snakeTail=new SnakePart(parts.get(parts.size()-1).getMapX(),parts.get(parts.size()-1).getMapY(), "tail"); //малюємо хвіст
		headPart.getSp().draw(ResourseManager.getInstance().batch);
		for (int i=1; i<parts.size()-1;i++) {
			parts.get(i).getSp().draw(ResourseManager.getInstance().batch);
		}
		if (parts.size()>1) snakeTail.getSp().draw(ResourseManager.getInstance().batch);
	}
	
	private void completedLvlText(float delta){
		
		if(score>=RootGame.NEED_POINTS && level!=0) {
			timeAfterShowCompleted+=delta;
			if(timeAfterShowCompleted<6.3f) {
				if(alpha<1 && !visible) fontDone.setColor(1, 1, 1, alpha+=0.005f);
				if(alpha>1) visible=true;
				if (visible && alpha>=0) fontDone.setColor(1, 1, 1, alpha-=0.005f);
				fontDone.draw(ResourseManager.getInstance().batch, "Level completed!", 6*SQUARE_WIDTH, 8*SQUARE_HEIGHT);
			}
		}
	}
	
	private void ateApple(){
		if(appleAlpha>0.05f){
			visibleApple.setY(visibleApple.getY()+2.1f);
			visibleApple.draw(ResourseManager.getInstance().batch, appleAlpha-=0.048f);
		}
	}
	
	private void ctrlButDraw(){
		ResourseManager.getInstance().batch.draw(controlSp, -2, 7*SQUARE_HEIGHT, SQUARE_WIDTH/2, SQUARE_HEIGHT/2, SQUARE_WIDTH,SQUARE_HEIGHT, 1.5f, 2.1f, 180, true);
		ResourseManager.getInstance().batch.draw(controlSp, 4*SQUARE_WIDTH, -4, SQUARE_WIDTH/2, SQUARE_HEIGHT/2, SQUARE_WIDTH,SQUARE_HEIGHT, 1.6f, 2.2f, 270, true);
		ResourseManager.getInstance().batch.draw(controlSp, 15*SQUARE_WIDTH-5, 1, SQUARE_WIDTH/2,SQUARE_HEIGHT/2, SQUARE_WIDTH,SQUARE_HEIGHT, 1.6f, 2.2f, 90, true);
		ResourseManager.getInstance().batch.draw(controlSp, 19*SQUARE_WIDTH+2, 7*SQUARE_HEIGHT, SQUARE_WIDTH/2,SQUARE_HEIGHT/2, SQUARE_WIDTH,SQUARE_HEIGHT, 1.6f, 2.2f, 0, true);
	}
	
	
	private void turnHead(int wayNew){
		headPart.getSp().setOrigin(SQUARE_WIDTH/2, SQUARE_HEIGHT/2);
		switch (wayNew) { //Повертаємо голову
		case 1:
			headPart.getSp().setRotation(180);
			break;
		case 2:
			headPart.getSp().setRotation(90);
			break;
		case 3:
			headPart.getSp().setRotation(0);
			break;
		case 4:
			headPart.getSp().setRotation(-90);
			break;
		}
		
switch (wayNew) {
case 1:
headPart.setMapX(-1);
headPart.getSp().setX(headPart.getMapX()*SQUARE_WIDTH);
break;
case 2:
headPart.setMapY(1);
headPart.getSp().setY(headPart.getMapY()*SQUARE_HEIGHT);
break;
case 3:
headPart.setMapX(1);
headPart.getSp().setX(headPart.getMapX()*SQUARE_WIDTH);
break;
case 4:
headPart.setMapY(-1);
headPart.getSp().setY(headPart.getMapY()*SQUARE_HEIGHT);
break;
}
	}
	
	
	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public boolean keyDown(int keycode) {

		switch (keycode) {
		case Keys.A:
				this.wayNew = 1;
			break;
		case Keys.D:
				this.wayNew = 3;
			break;
		case Keys.W:
				this.wayNew = 2;
			break;
		case Keys.S:
				this.wayNew = 4;
			break;
		}
		return false;
	}

	
	@Override
	public boolean keyUp(int keycode) {
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
}
