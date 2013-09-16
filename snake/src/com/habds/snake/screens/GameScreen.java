package com.habds.snake.screens;

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
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.habds.snake.PickedUpPos;
import com.habds.snake.ResourseManager;
import com.habds.snake.RootGame;
import com.habds.snake.SnakePart;

public class GameScreen implements Screen, InputProcessor {
	private OrthographicCamera camera;
	private Sprite snakeEat, bgSp, mob, visibleApple, bgGame;
	private Image ctrlUp, ctrlDown, ctrlLeft, ctrlRight, pause, exit, bigPause;
	public static float SQUARE_WIDTH, SQUARE_HEIGHT;
	int pixCountWid = 20, pixCountHei = 14, eatArrX, eatArrY, wayOld, wayNew;
	private Random random = new Random();
	private int[][] map = new int[pixCountWid][pixCountHei];
	private boolean ifAccelerate;
	private float time = 0, mobsTime = 0;
	private SnakePart headPart, snakeTail;
	private ArrayList<SnakePart> parts;
	private ArrayList<PickedUpPos> pickedUp;
	private ArrayList<Sprite> wallsSp;
	ArrayList<ArrayList<Byte>> mobsWay;
	private char[] charArray;
	private float speed, accelerate, mobSpeed = 0.5f;
	private RootGame rootGame;
	float w = Gdx.graphics.getWidth();
	float h = Gdx.graphics.getHeight();
	private int level;
	private boolean ifPause;
	private int score;
	private int i;
	private float alpha, appleAlpha;
	private float timeAfterShowCompleted;
	private boolean visible, ifSound, isReadArr;
	private Stage stage;
	private ArrayList scoresArr;
	private int unlockedLvl;

	public GameScreen(RootGame rootGame) {
		this.rootGame = rootGame;
	}

	@Override
	public void show() {
		SQUARE_WIDTH = w / pixCountWid; // задаємо ш\в клітинки
		SQUARE_HEIGHT = h / pixCountHei;
		stage = new Stage(0, 0, false);

		ifAccelerate = false;
		speed = 0.515f;
		accelerate = 0.04f;
		level = rootGame.getLevel();
		i = 0;
		score = 0;
		alpha = 0f;
		appleAlpha = 0f;
		timeAfterShowCompleted = 0;
		visible = false;
		ifPause = false;
		ifSound = rootGame.ifSound();
		for (int i = 0; i < pixCountWid; i++) {
			for (int j = 0; j < pixCountHei; j++) {
				map[i][j] = 0;
			}
		}
		mobsWay = new ArrayList();
		parts = new ArrayList<SnakePart>();
		pickedUp = new ArrayList<PickedUpPos>();
		wallsSp = new ArrayList<Sprite>();
		wayNew = 3;
		wayOld = 3;

		FileHandle handle = Gdx.files.local("scores.txt");
		Json json = new Json();
		String newText = handle.readString(); // read Json from file
		scoresArr = json.fromJson(ArrayList.class, newText);
		unlockedLvl = ((Float) scoresArr.get(scoresArr.size() - 1)).intValue();

		camera = new OrthographicCamera(320, 480);

		bgSp = new Sprite(ResourseManager.getInstance().background);
		bgSp.setPosition(0, 0);
		bgSp.setSize(w, h);
		headPart = new SnakePart(pixCountWid / 2, pixCountHei / 2, "head");
		parts.add(headPart);

		bgGame = new Sprite(ResourseManager.getInstance().snGame);
		bgGame.setSize(w, h);
		bgGame.setPosition(0, 0);

		pause = new Image(ResourseManager.getInstance().atlas.findRegion("pause"));
		if (ResourseManager.getInstance().isBig) {
			pause.setRotation(-90);
			pause.setSize(0.17f * h, 0.12f * w);
			pause.setPosition(-5, 14.7f * SQUARE_HEIGHT);
		} else {
			pause.setSize(0.12f * w, 0.17f * h);
			pause.setPosition(-2, 12.4f * SQUARE_HEIGHT);
		}
		pause.setColor(1, 1, 1, 0.7f);
		pause.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (ifSound)
					ResourseManager.getInstance().buttonSound.play(1f);
				ifPause = true;
				bigPause.setVisible(true);
			}
		});

		bigPause = new Image(ResourseManager.getInstance().atlas.findRegion("pause"));
		if (ResourseManager.getInstance().isBig) {
			bigPause.setOrigin(bigPause.getWidth() / 2,bigPause.getHeight() / 2);
			bigPause.setRotation(90);
			bigPause.setSize(0.7f * h, 0.7f * w);
			bigPause.setPosition(0.57f * w, 0.12f * h);
		} else {
			bigPause.setSize(0.7f * w, 0.7f * h);
			bigPause.setPosition(0.18f * w, 0.2f * h);
		}
		bigPause.setColor(1, 1, 1, 0.5f);
		bigPause.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ifPause = false;
				bigPause.setVisible(false);
			}
		});
		bigPause.setVisible(false);

		exit = new Image(ResourseManager.getInstance().atlas.findRegion("exit"));
		exit.setSize(0.12f * w, 0.17f * h);
		exit.setPosition(18 * SQUARE_WIDTH, 12.5f * SQUARE_HEIGHT);
		exit.setColor(1, 1, 1, 0.7f);
		exit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (ifSound)
					ResourseManager.getInstance().buttonSound.play(1f);
				dispose();
				rootGame.setScreen(rootGame.menuScreen);
			}
		});

		snakeEat = new Sprite(ResourseManager.getInstance().atlas.createSprite("eat"));
		snakeEat.setSize(SQUARE_WIDTH, SQUARE_HEIGHT);
		visibleApple = new Sprite(ResourseManager.getInstance().atlas.createSprite("eat"));
		visibleApple.setSize(SQUARE_WIDTH, SQUARE_HEIGHT);
		mob = new Sprite(ResourseManager.getInstance().atlas.createSprite("gud"));
		mob.setSize(SQUARE_WIDTH, SQUARE_HEIGHT);

		ResourseManager.getInstance().font.setScale(0.35f * w / ResourseManager.getInstance().standartW, 0.35f * h/ ResourseManager.getInstance().standartH);
		readArr();
		mobMove();
		drawWalls();

		pickUp();
		drawCtrlBut();
		stage.addActor(pause);
		stage.addActor(bigPause);
		stage.addActor(exit);
		stage.addListener(new ClickListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == Keys.BACK) {
					ResourseManager.getInstance().dispose();
				}
				return true;
			}
		});
		Gdx.input.setInputProcessor(stage);
	}

	private void drawCtrlBut() {
		ctrlUp = new Image(ResourseManager.getInstance().atlas.findRegion("control"));
		ctrlUp.setOrigin(SQUARE_WIDTH / 2, SQUARE_HEIGHT / 2);
		ctrlUp.setRotation(90);
		ctrlUp.setName("ctrlUp");
		ctrlUp.setSize(0.5f * 128 * w / 480, 0.45f * 128 * h / 320);
		ctrlUp.setPosition(19*SQUARE_WIDTH+8, 6.5f * SQUARE_HEIGHT);
		ctrlUp.setColor(1, 1, 1, 0.6f);
		ctrlUp.addListener(ctrlClickListener);
		stage.addActor(ctrlUp);

		ctrlLeft = new Image(ResourseManager.getInstance().atlas.findRegion("control"));
		ctrlLeft.setName("ctrlLeft");
		ctrlLeft.setOrigin(SQUARE_WIDTH / 2, SQUARE_HEIGHT / 2);
		ctrlLeft.rotate(180);
		ctrlLeft.setPosition(4 * SQUARE_WIDTH, 1 * SQUARE_HEIGHT);
		ctrlLeft.setSize(0.5f * 128 * w / 480, 0.4f * 128 * h / 320);
		ctrlLeft.setColor(1, 1, 1, 0.6f);
		ctrlLeft.addListener(ctrlClickListener);
		stage.addActor(ctrlLeft);

		ctrlRight = new Image(ResourseManager.getInstance().atlas.findRegion("control"));
		ctrlRight.setName("ctrlRight");
		ctrlRight.setSize(0.5f * 128 * w / 480, 0.4f * 128 * h / 320);
		ctrlRight.setPosition(5.5f * SQUARE_WIDTH, -5);
		ctrlRight.setColor(1, 1, 1, 0.6f);
		ctrlRight.addListener(ctrlClickListener);
		stage.addActor(ctrlRight);

		ctrlDown = new Image(ResourseManager.getInstance().atlas.findRegion("control"));
		ctrlDown.setName("ctrlDown");
		ctrlDown.setRotation(270);
		ctrlDown.setOrigin(SQUARE_WIDTH / 2, SQUARE_HEIGHT / 2);
		ctrlDown.setSize(0.5f * 128 * w / 480, 0.4f * 128 * h / 320);
		ctrlDown.setPosition(18 * SQUARE_WIDTH, 5 * SQUARE_HEIGHT);
		ctrlDown.setColor(1, 1, 1, 0.6f);
		ctrlDown.addListener(ctrlClickListener);
		stage.addActor(ctrlDown);

	}

	private void readArr() {
		FileHandle handle = Gdx.files.internal("data/mobs/level" + level
				+ ".txt");
		if (handle.exists()) {
			String jsonText = handle.readString(); // Зчитали массив з файлу
			JSONParser parser = new JSONParser();
			Object obj = null;
			try {
				obj = parser.parse(jsonText);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			JSONArray array = (JSONArray) obj; // Конвертуємо стрічку в массив!

			for (int i = 0; i < array.size(); i++) {
				JSONArray arrayList = (JSONArray) array.get(i); // Робимо аррайліст ДЖСОНАрраїв
				ArrayList<Byte> bytes = new ArrayList<Byte>();
				for (int j = 0; j < arrayList.size(); j++) {
					Byte point = Byte.valueOf(arrayList.get(j).toString()); // поштучно конвертуємозначення і записуємо варрліст.
					bytes.add(point);
				}
				mobsWay.add(bytes);
			}
			isReadArr = true;
			; // Тру якшо моб є в цьому лвлі
		} else
			isReadArr = false;
	}

	private void pickUp() {
		score++;
		if (score > 1) {
			visibleApple.setPosition(snakeEat.getX(), snakeEat.getY());
			appleAlpha = 1f;
			if (ifSound)
				ResourseManager.getInstance().pickUpSound.play(0.8f);
		}
		boolean goodPos = false;
		int randX;
		int randY;

		while (!goodPos) {
			randX = random.nextInt(pixCountWid);
			randY = random.nextInt(pixCountHei);
			if (map[randX][randY] == 0) {
				pickedUp.add(new PickedUpPos(eatArrX, eatArrY));
				snakeEat.setPosition(randX * SQUARE_WIDTH, randY
						* SQUARE_HEIGHT);
				eatArrX = randX;
				eatArrY = randY;
				ifAccelerate = true;
				goodPos = true; // flag to leave while cycle
			}
		}
	}

	private void createWallSp(int mapX, int mapY, String type) {
		map[mapX][mapY] = 1;
		Sprite sp = (type == "border") ? new Sprite(
				ResourseManager.getInstance().atlas.createSprite("border"))
				: new Sprite(
						ResourseManager.getInstance().atlas
								.createSprite("wall"));
		sp.setSize(SQUARE_WIDTH, SQUARE_HEIGHT);
		sp.setPosition(mapX * SQUARE_WIDTH, mapY * SQUARE_HEIGHT);
		wallsSp.add(sp);
	}

	private void mobMove() {
		if (isReadArr) {

			if (i >= mobsWay.size())
				i = 0;
			mob.setPosition(mobsWay.get(i).get(0) * SQUARE_WIDTH, mobsWay.get(i).get(1) * SQUARE_HEIGHT);
			if (map[mobsWay.get(i).get(0)][mobsWay.get(i).get(1)] == 1) {
				// /Програв
				dispose();
				rootGame.gameOver.setScore(score);
				rootGame.setScreen(rootGame.gameOver);
			}
			i++;
			// System.out.println("x: "+ mob.getX()+ " y : "+ mob.getY());

		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		if (ifPause == false) { // майже нічо не займає!
			step(delta);
		}
		ResourseManager.getInstance().batch.begin();
		bgSp.draw(ResourseManager.getInstance().batch);
		bgGame.draw(ResourseManager.getInstance().batch);
		for (Sprite wall : wallsSp) {
			wall.draw(ResourseManager.getInstance().batch);
		}
		drawSnake();
		completedLvlText(delta);
		snakeEat.draw(ResourseManager.getInstance().batch);
		ateApple();
		ResourseManager.getInstance().font.draw(ResourseManager.getInstance().batch,"score "+score,9 * SQUARE_WIDTH, 14 * SQUARE_HEIGHT);
		if (isReadArr)
			mob.draw(ResourseManager.getInstance().batch);
		ResourseManager.getInstance().batch.end();

		stage.draw();
	}

	public void move() {
		if (wayNew - wayOld == 2 || wayNew - wayOld == -2)
			wayNew = wayOld;
		else
			wayOld = wayNew;
		// Міняти координати екземплярів на попередні ( вище по списку)
		// (переміщення)
		if (parts.size() > 1) {
			map[parts.get(parts.size() - 1).getMapX()][parts.get(
					parts.size() - 1).getMapY()] = 0;
			for (int i = parts.size() - 1; i > 0; i--) {
				parts.get(i).setNewPos(parts.get(i - 1).getMapX(),
						parts.get(i - 1).getMapY());
				map[parts.get(i).getMapX()][parts.get(i).getMapY()] = 1;

			}
		}

		// System.out.println("HD: MapX: "+ headPart.getMapX()+" MapY: "+
		// headPart.getMapY());

		moveHead(wayNew);
	}

	private void drawWalls() {
		FileHandle file = Gdx.files.internal("data/levels/level" + level
				+ ".txt");
		String charString = file.readString();
		charString = charString.replace("\r\n", "");
		charArray = charString.toCharArray();

		for (int index = 0; index < charArray.length; index++) {
			int i = index / pixCountWid;
			int j = index % pixCountWid;
			if (charArray[index] == '1') {
				createWallSp(j, pixCountHei - i - 1, "wall"); // Магія)))
			}
		}

		for (int i = 0; i < pixCountWid; i++) {
			createWallSp(i, 0, "border");
			createWallSp(i, pixCountHei - 1, "border");
		}
		for (int j = 0; j < pixCountHei; j++) {
			createWallSp(0, j, "border");
			createWallSp(pixCountWid - 1, j, "border");
		}
	}

	public ClickListener ctrlClickListener = new ClickListener() {
		@Override
		public boolean touchDown(InputEvent event, float x, float y,
				int pointer, int button) {
			event.getListenerActor().addAction(
					Actions.color(new Color(toRGB(37, 89, 115, 0.7f))));
			if (event.getListenerActor().getName() == "ctrlUp")
				wayNew = 2;
			if (event.getListenerActor().getName() == "ctrlLeft")
				wayNew = 1;
			if (event.getListenerActor().getName() == "ctrlRight")
				wayNew = 3;
			if (event.getListenerActor().getName() == "ctrlDown")
				wayNew = 4;
			return true;
		};

		public void touchUp(InputEvent event, float x, float y, int pointer,
				int button) {
			event.getListenerActor().addAction(
					Actions.color(toRGB(204, 255, 255, 0.7f)));
		};

	};

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// system.out.println("x: " + screenX/SQUARE_WIDTH +
		// ";  Y: "+screenY/SQUARE_HEIGHT);
		if (screenX <= 2 * SQUARE_WIDTH && screenY > 4 * SQUARE_HEIGHT
				&& screenY <= 8 * SQUARE_HEIGHT)
			this.wayNew = 2;
		if (screenX >= 3 * SQUARE_WIDTH && screenX <= 6 * SQUARE_WIDTH
				&& screenY >= h - 4 * SQUARE_HEIGHT)
			this.wayNew = 1;
		if (screenX >= 13 * SQUARE_WIDTH && screenX <= 17 * SQUARE_WIDTH
				&& screenY >= 12 * SQUARE_HEIGHT)
			this.wayNew = 3;
		if (screenX >= 18 * SQUARE_WIDTH && screenY >= 4 * SQUARE_HEIGHT
				&& screenY <= 8 * SQUARE_HEIGHT)
			this.wayNew = 4;
		if (screenX <= 2 * SQUARE_WIDTH && screenY <= 0.2f * h)
			ifPause = true; // Пауза
		if (screenX >= 17 * SQUARE_WIDTH && screenY <= 2 * SQUARE_HEIGHT) {
			// вертаємось в меню.
			dispose();
			rootGame.setScreen(rootGame.menuScreen);
		}
		if (ifPause == true)
			if (screenX < 0.8f * w && screenX > 0.3f * w && screenY > 0.2f * h
					&& screenY < 0.6f * h)
				ifPause = false;
		return true;
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	private void step(float delta) {
		if (parts.size() % 8 == 0 & ifAccelerate == true) {
			accelerate -= 0.004f;
			ifAccelerate = false;
		}

		if (parts.size() % 3 == 0 & ifAccelerate == true) {
			speed -= accelerate;
			ifAccelerate = false;
		}
		time += delta;
		mobsTime += delta;

		if (time > speed) {
			move(); // крок
			if (parts.get(0).getMapX() == eatArrX
					&& parts.get(0).getMapY() == eatArrY) {
				pickUp(); // з`їли
			}
			if (mobsTime > mobSpeed) {
				mobMove();
				mobsTime = 0;
			}

			// Розмноження
			for (Iterator<PickedUpPos> it = pickedUp.iterator(); it.hasNext();) {
				PickedUpPos pos = it.next();
				if (pos.addToBack) { // Додаємо новий кусок
					parts.add(new SnakePart(pos.mapX, pos.mapY, ""));
					it.remove();
					continue;
				}

				if (parts.get(parts.size() - 1).getMapX() == pos.mapX
						&& parts.get(parts.size() - 1).getMapY() == pos.mapY) {
					pos.addToBack = true;
				}

			}

			time = 0;
		}
	}

	private void drawSnake() {
		snakeTail = new SnakePart(parts.get(parts.size() - 1).getMapX(), parts
				.get(parts.size() - 1).getMapY(), "tail"); // малюємо хвіст
		headPart.getSp().draw(ResourseManager.getInstance().batch);
		for (int i = 1; i < parts.size() - 1; i++) {
			parts.get(i).getSp().draw(ResourseManager.getInstance().batch);
		}
		if (parts.size() > 1)
			snakeTail.getSp().draw(ResourseManager.getInstance().batch);
	}

	private void completedLvlText(float delta) {
		if (score >= RootGame.NEED_POINTS && level == unlockedLvl) {
			timeAfterShowCompleted += delta;
			if (timeAfterShowCompleted < 6.3f) {
				if (alpha < 1 && !visible)
					ResourseManager.getInstance().fontDone.setColor(1, 1, 1,
							alpha += 0.005f);
				if (alpha > 1)
					visible = true;
				if (visible && alpha >= 0)
					ResourseManager.getInstance().fontDone.setColor(1, 1, 1,
							alpha -= 0.005f);
				ResourseManager.getInstance().fontDone
						.draw(ResourseManager.getInstance().batch,
								"Level completed!", 6 * SQUARE_WIDTH,
								8 * SQUARE_HEIGHT);
			}
		}
	}

	private void ateApple() {
		if (appleAlpha > 0.05f) {
			visibleApple.setY(visibleApple.getY() + 2.1f);
			visibleApple.draw(ResourseManager.getInstance().batch,
					appleAlpha -= 0.048f);
		}
	}

	private void moveHead(int wayNew) {
		// TODO: фтв?!!!
		/*
		 * if(map[headPart.getMapX()][headPart.getMapY()]==1 ||
		 * (Math.round(mob.getX()/SQUARE_WIDTH)==headPart.getMapX() &&
		 * Math.round(mob.getY()/SQUARE_HEIGHT)==headPart.getMapY())) {
		 * //System.out.println("Head hit"); ///Програв dispose();
		 * rootGame.gameOver.setScore(score);
		 * rootGame.setScreen(rootGame.gameOver); }
		 */
		headPart.getSp().setOrigin(SQUARE_WIDTH / 2, SQUARE_HEIGHT / 2);
		switch (wayNew) { // Повертаємо голову
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
			headPart.getSp().setX(headPart.getMapX() * SQUARE_WIDTH);
			break;
		case 2:
			headPart.setMapY(1);
			headPart.getSp().setY(headPart.getMapY() * SQUARE_HEIGHT);
			break;
		case 3:
			headPart.setMapX(1);
			headPart.getSp().setX(headPart.getMapX() * SQUARE_WIDTH);
			break;
		case 4:
			headPart.setMapY(-1);
			headPart.getSp().setY(headPart.getMapY() * SQUARE_HEIGHT);
			break;
		}
		if (map[headPart.getMapX()][headPart.getMapY()] == 1
				|| (Math.round(mob.getX() / SQUARE_WIDTH) == headPart.getMapX() && Math
						.round(mob.getY() / SQUARE_HEIGHT) == headPart
						.getMapY())) {
			// System.out.println("Head hit");
			// /Програв
			dispose();
			rootGame.gameOver.setScore(score);
			rootGame.setScreen(rootGame.gameOver);
		}
		map[headPart.getMapX()][headPart.getMapY()] = 2;

	}

	@Override
	public void hide() {

	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, true);
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

	private Color toRGB(int r, int g, int b, float alpha) {
		float RED = r / 255.0f;
		float GREEN = g / 255.0f;
		float BLUE = b / 255.0f;
		return new Color(RED, GREEN, BLUE, alpha);
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

}
