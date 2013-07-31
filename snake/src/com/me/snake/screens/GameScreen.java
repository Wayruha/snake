package com.me.snake.screens;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;


import sun.io.CharToByteASCII;
import sun.security.util.Length;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.me.snake.PickedUpPos;
import com.me.snake.RootGame;
import com.me.snake.SnakePart;
import com.sun.org.apache.bcel.internal.generic.GOTO;

public class GameScreen implements Screen, InputProcessor {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture eatTx, background, wallTx, borderTx,controlBut, fontTx, exitTx, pauseTx;
	private Sprite snakeEat, bgSp,pauseSp, bigPause, mob, exitSp;
	public static float SQUARE_WIDTH, SQUARE_HEIGHT;
	int pixCountWid = 20, pixCountHei = 14, eatArrX, eatArrY, wayOld,
			wayNew;
	private Random random = new Random();
	private int[][] map = new int [pixCountWid][pixCountHei];
	private boolean ifAccelerate=false;
	private float time = 0;
	private SnakePart headPart, snakeTail;
	private ArrayList<SnakePart> parts;
	private ArrayList<PickedUpPos> pickedUp;
	private ArrayList<Sprite> wallsSp;
	ArrayList<ArrayList<Byte>> mobsWay;  
	private  char[] charArray;
	private float speed, accelerate=0.04f, mobSpeed=0.58f;
	private RootGame rootGame;
	private Sprite controlSp;
	private Actor actor;
	private BitmapFont font;
	float w = Gdx.graphics.getWidth();
	float h = Gdx.graphics.getHeight();
	private int level;
	private boolean ifPause;
	private int score;
	private int i =0;
	
	public GameScreen(RootGame rootGame) {
		this.rootGame = rootGame;
	}
	
	
	@Override
	public void resize(int width, int height) {

	}



	@Override
	public void show() {
		SQUARE_WIDTH = w / pixCountWid; // ������ �\� �������
		SQUARE_HEIGHT = h / pixCountHei;
		speed=0.58f;
		level=rootGame.getLevel();
		score=0;
		
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
		batch = new SpriteBatch();

		background = new Texture(Gdx.files.internal("data/bgSnake.png"));
		background.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		bgSp = new Sprite(background);
		bgSp.setPosition(0, 0);
		bgSp.setSize(w, h);
		headPart = new SnakePart(pixCountWid / 2, pixCountHei / 2, "head");
		parts.add(headPart);
		
		borderTx=new Texture(Gdx.files.internal("data/parts/border.png"));
		borderTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		wallTx=new Texture(Gdx.files.internal("data/parts/wall.png"));
		wallTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		controlBut = new Texture(Gdx.files.internal("data/control.png"));
		controlBut.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		controlSp=new Sprite(controlBut);
		
		pauseTx=new Texture(Gdx.files.internal("data/pause.png"));
		pauseTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		pauseSp=new Sprite(pauseTx);
		pauseSp.setSize(0.1f*w, 0.15f*h);
		pauseSp.setPosition(0, 13*SQUARE_HEIGHT);
		
		bigPause=new Sprite(pauseTx);
		bigPause.setSize(0.5f*w, 0.6f*h);
		bigPause.setPosition(0.27f*w, 0.38f*h);
		
		exitTx=new Texture(Gdx.files.internal("data/exit.png"));
		exitTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		exitSp=new Sprite(exitTx);
		exitSp.setSize(0.1f*w, 0.15f*h);
		exitSp.setPosition(18*SQUARE_WIDTH, 13*SQUARE_HEIGHT);
		
		eatTx = new Texture(Gdx.files.internal("data/eat.png"));
		eatTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		snakeEat = new Sprite(eatTx);
		snakeEat.setSize(SQUARE_WIDTH, SQUARE_HEIGHT);
		
		fontTx = new Texture(Gdx.files.internal("data/font/neucha.png"));
		fontTx.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font = new BitmapFont(Gdx.files.internal("data/font/neucha.fnt"), new TextureRegion(fontTx), false);
		font.setScale(0.35f);
		
		mob=new Sprite(borderTx);
		mob.setSize(SQUARE_WIDTH, SQUARE_HEIGHT);
		
		readArr();
		mobMove();
		
		drawWalls();
		
		Gdx.input.setInputProcessor(this);
		pickUp();
	}

	private boolean readArr (){
		FileHandle handle = Gdx.files.internal("data/mobs/level"+level+".txt");
		if(handle.exists()){
		String jsonText=handle.readString();  //������� ������ � �����
		
		JSONParser parser = new JSONParser();
		Object obj = null;
		try {
			obj = parser.parse(jsonText);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	
		JSONArray array=(JSONArray)obj;   //���������� ������ � ������! 
		
		
		for (int i = 0; i < array.size(); i++) {
			JSONArray arrayList =  (JSONArray) array.get(i); //������ �������� ����������
			ArrayList<Byte> bytes = new ArrayList<Byte>(); 
			for(int j = 0; j < arrayList.size(); j++){
				Byte point = Byte.valueOf(arrayList.get(j).toString()); //�������� ���������� �������� � �������� � ������.
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
		Sprite sp = (type=="border") ? new Sprite(borderTx):new Sprite(wallTx);
		sp.setSize(SQUARE_WIDTH, SQUARE_HEIGHT);
		sp.setPosition(mapX*SQUARE_WIDTH, mapY*SQUARE_HEIGHT);
		wallsSp.add(sp);
	}
	
	private void mobMove(){
		if(readArr()){
			
		if( i>=mobsWay.size()) i=0;
		mob.setPosition(mobsWay.get(i).get(0) *SQUARE_WIDTH, mobsWay.get(i).get(1)*SQUARE_HEIGHT);
		if(map[mobsWay.get(i).get(0)][mobsWay.get(i).get(1)]==1) {
			 ///��������
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
		if(parts.size()%3==0 & ifAccelerate==true) {
			speed-=accelerate;
			ifAccelerate=false;
		}
		
		if(ifPause!= true) {
			time +=delta;
			
			if(time>mobSpeed){
				mobMove();
			}
			
			if (time > speed) {
		
				move(); //����	
				if (parts.get(0).getMapX() == eatArrX && parts.get(0).getMapY() == eatArrY) {
				pickUp();
			}	
			
			
			//�����������
			for (Iterator<PickedUpPos> it=pickedUp.iterator(); it.hasNext();) {
				PickedUpPos pos=it.next();
				if (pos.addToBack) {         //������ ����� �����
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
		snakeTail=new SnakePart(parts.get(parts.size()-1).getMapX(),parts.get(parts.size()-1).getMapY(), "tail"); //������� ����
		
		batch.begin();
		
		bgSp.draw(batch);
		for(Sprite wall:wallsSp){
			wall.draw(batch);
}
		headPart.getSp().draw(batch);
		for (int i=1; i<parts.size()-1;i++) {
			parts.get(i).getSp().draw(batch);
		}
		if (parts.size()>1) snakeTail.getSp().draw(batch);
		
		if(ifPause==true) {
			bigPause.draw(batch, 0.6f);
		}
		
		snakeEat.draw(batch);
		pauseSp.draw(batch);
		exitSp.draw(batch);
		batch.draw(controlSp, -2, 7*SQUARE_HEIGHT, SQUARE_WIDTH/2, SQUARE_HEIGHT/2, SQUARE_WIDTH,SQUARE_HEIGHT, 1.5f, 2.1f, 180, true);
		batch.draw(controlSp, 4*SQUARE_WIDTH, -7, SQUARE_WIDTH/2, SQUARE_HEIGHT/2, SQUARE_WIDTH,SQUARE_HEIGHT, 1.6f, 2.2f, 270, true);
		batch.draw(controlSp, 15*SQUARE_WIDTH-5, 4, SQUARE_WIDTH/2,SQUARE_HEIGHT/2, SQUARE_WIDTH,SQUARE_HEIGHT, 1.6f, 2.2f, 90, true);
		batch.draw(controlSp, 19*SQUARE_WIDTH+2, 7*SQUARE_HEIGHT, SQUARE_WIDTH/2,SQUARE_HEIGHT/2, SQUARE_WIDTH,SQUARE_HEIGHT, 1.6f, 2.2f, 0, true);
		font.draw(batch, "score: "+ score, 9*SQUARE_WIDTH, 14*SQUARE_HEIGHT);
		mob.draw(batch);
		batch.end();

	}
	
	public void move() {
		if (wayNew-wayOld==2 || wayNew-wayOld==-2)
			wayNew=wayOld; else wayOld=wayNew;
			// ̳���� ���������� ���������� �� ��������� ( ���� �� ������) (����������)
					if (parts.size() > 1) {
						map[parts.get(parts.size()-1).getMapX()][parts.get(parts.size()-1).getMapY()]=0;
						for (int i=parts.size()-1; i>0; i--) {
							parts.get(i).setNewPos(parts.get(i - 1).getMapX(), parts.get(i-1).getMapY());
							map[parts.get(i).getMapX()][parts.get(i).getMapY()]=1;
						}
					}
					
					headPart.getSp().setOrigin(SQUARE_WIDTH/2, SQUARE_HEIGHT/2);
					switch (wayNew) { //��������� ������
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
		
		if(map[headPart.getMapX()][headPart.getMapY()]==1 || (Math.round(mob.getX()/SQUARE_WIDTH)==headPart.getMapX() && Math.round(mob.getY()/SQUARE_HEIGHT)==headPart.getMapY())) {
		 ///��������
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
				  createWallSp(j, pixCountHei-i-1, "wall");   //����)))
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
		if(screenX<=2*SQUARE_WIDTH && screenY<=0.2f*h) ifPause=true;     // �����
		if(screenX>=17*SQUARE_WIDTH && screenY<=2*SQUARE_HEIGHT) {
			 //��������� � ����.
			dispose();
			rootGame.setScreen(rootGame.menuScreen);
		}
		if(ifPause==true) if(screenX<0.8f*w && screenX>0.3f*w && screenY>0.2f*h && screenY<0.6f*h) ifPause=false;
		return false;
	}
	
	

	@Override
	public void dispose() {
		font.dispose();
		batch.dispose();
		eatTx.dispose();
		background.dispose(); 
		borderTx.dispose();
		wallTx.dispose();		
		controlBut.dispose();
		fontTx.dispose();
		pauseTx.dispose();
		exitTx.dispose();
		
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

	
 /*	public void readMobsWay() {
		FileHandle handle = Gdx.files.internal("data/name.txt");
		Json json = new Json();
		String newText = handle.readString(); // read Json from file // sec
		mobsWay = json.fromJson(ArrayList.class, newText);
	}
*/	
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
