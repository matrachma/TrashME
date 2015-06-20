package net.matrachma.trashme2.screen;

import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import net.matrachma.trashme2.Sampah;
import net.matrachma.trashme2.Sampah.Type;
import net.matrachma.trashme2.TrashME;
import net.matrachma.trashme2.Settings;

public class GameScreen implements Screen {

	private Array<Sampah> sampahs = new Array<Sampah>(); 		
	private long dropTimeGap = 1900003000; 							
	private float dropSpeed = 0.5f;								
	private long lastDropTime;
	public BitmapFont font18;

	
	private Texture gamePlayBackground = new Texture(Gdx.files.internal("gamePlayBG.png"));
	private Texture tongO = new Texture(Gdx.files.internal("tongO.png")); 
	private Texture tongA = new Texture(Gdx.files.internal("tongA.png"));
	private Texture gerobakImage = new Texture(Gdx.files.internal("gerobak.png"));
	private Texture scoreImage = new Texture(Gdx.files.internal("score.png"));
	private Texture nyawaImage = new Texture(Gdx.files.internal("nyawa.png"));
	
	private Sound catchRightSound;
	private Sound catchSound;
	
	private Rectangle tongOrg = new Rectangle();
	private Rectangle tongAnorg = new Rectangle();
	private Rectangle gerobak = new Rectangle();
	private Vector3 touchPos = new Vector3();
	
	private ParticleEffect ijo = new ParticleEffect();
	private ParticleEffect oren = new ParticleEffect();

	private SpriteBatch batch = new SpriteBatch();		 		
	private OrthographicCamera camera = new OrthographicCamera(); 
	public BitmapFont fonts;										
	private String score = "0";
	private String nyawa = "5";
	
	private Stage stage = new Stage(new StretchViewport(TrashME.WIDTH, TrashME.HEIGHT), batch);
	
	private boolean gameOver = false;								
	private boolean paused;
	private TrashME game;
	private float tempA;
	private float tempB;
	private boolean space = false;
	
	
	
	public GameScreen(TrashME game) {
		this.game = game;
	}

	@Override
	public void show() {
		Gdx.input.setCatchBackKey(true);

		
		if (Settings.soundEnabled) {
			catchSound = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));
			catchRightSound = Gdx.audio.newSound(Gdx.files.internal("rightCatch.mp3"));
		}

		camera.setToOrtho(false, TrashME.WIDTH, TrashME.HEIGHT);

		tongOrg.x = TrashME.WIDTH / 2 - tongO.getWidth();
		tongOrg.y = 25;
		tongOrg.width = tongO.getWidth();
		tongOrg.height = tongO.getHeight();
		tongAnorg.x = TrashME.WIDTH / 2;
		tongAnorg.y = 25;
		tongAnorg.width = tongA.getWidth();
		tongAnorg.height = tongA.getHeight();
		gerobak.x = TrashME.WIDTH / 2 - gerobakImage.getWidth() / 2;
		gerobak.y = 5;
		gerobak.width = gerobakImage.getWidth();
		gerobak.height = gerobakImage.getHeight();
		
		spawnSampah();
		FileHandle fontFile = Gdx.files.internal("GearsOfPeace.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        FreeTypeFontParameter param = new FreeTypeFontParameter();
        param.size = 18;
        font18 = generator.generateFont(param);
        generator.dispose();
        
        oren.load(Gdx.files.internal("oren.p"), Gdx.files.internal(""));
		ijo.load(Gdx.files.internal("ijo.p"), Gdx.files.internal(""));
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.input.setInputProcessor(stage);
		
		draw(delta);
		
		stage.act();
		stage.draw();
		
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			space = !space;
			pindah();
		}
		
		if (!gameOver && !paused){
			if (Gdx.input.isTouched() && !space) {
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				camera.unproject(touchPos);
				tongAnorg.x = touchPos.x - tongAnorg.width;
				tongOrg.x = touchPos.x;
				gerobak.x = touchPos.x - gerobak.width / 2;
			} else {
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				camera.unproject(touchPos);
				tongOrg.x = touchPos.x - tongOrg.width;
				tongAnorg.x = touchPos.x;
				gerobak.x = touchPos.x - gerobak.width / 2;
			}
		}
		
		
		if (TimeUtils.nanoTime() - lastDropTime > dropTimeGap && !paused) {
			spawnSampah();
		}

		dropSpeed += 0.00015f;
		dropTimeGap -= 95000;
	}
	
	
	private void spawnSampah() {
		Sampah sampah;
		
		Random rand = new Random();

	    int acak = rand.nextInt((12 - 1) + 1) + 1;
		if (acak == 1)		sampah = new Sampah("anor1.png", Type.Anorganik);
		else if (acak == 2) sampah = new Sampah("or1.png", Type.Organik);
		else if (acak == 3) sampah = new Sampah("anor2.png", Type.Anorganik);
		else if (acak == 4) sampah = new Sampah("or2.png", Type.Organik);
		else if (acak == 5)	sampah = new Sampah("anor3.png", Type.Anorganik);
		else if (acak == 6) sampah = new Sampah("or3.png", Type.Organik);
		else if (acak == 7) sampah = new Sampah("anor4.png", Type.Anorganik);
		else if (acak == 8) sampah = new Sampah("or4.png", Type.Organik);
		else if (acak == 9)	sampah = new Sampah("anor5.png", Type.Anorganik);
		else if (acak == 10) sampah = new Sampah("or5.png", Type.Organik);
		else if (acak == 11) sampah = new Sampah("anor6.png", Type.Anorganik);
		else				 sampah = new Sampah("or6.png", Type.Organik);

		sampah.x = MathUtils.random(0, TrashME.WIDTH - sampah.getImage().getWidth());
		sampah.y = TrashME.HEIGHT - 10;
		sampah.width = sampah.getImage().getWidth();
		sampah.height = sampah.getImage().getHeight();
		
		sampahs.add(sampah);
		lastDropTime = TimeUtils.nanoTime();
	}
	
	private void draw(float delta) {
		
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(gamePlayBackground, 0, 0);
		
		for (int i = 0; i < sampahs.size; i++) {
			sampahs.get(i).draw(batch);
		}
		
		
		
		batch.draw(gerobakImage, gerobak.x, gerobak.y);
		batch.draw(tongO, tongOrg.x, tongOrg.y);
		batch.draw(tongA, tongAnorg.x, tongAnorg.y);
		batch.draw(scoreImage, 5, 510);
		batch.draw(nyawaImage, 340, 510);
		ijo.draw(batch);
		oren.draw(batch);
		ijo.update(delta);
		oren.update(delta);
		
		font18.draw(batch, score, 30, TrashME.HEIGHT - 25);
		font18.draw(batch, nyawa, 400, TrashME.HEIGHT - 25);
		
		if (Gdx.input.isKeyPressed(Keys.P)) {
			pause();
		}
		batch.end();
		
		Iterator<Sampah> iter = sampahs.iterator();
		while (iter.hasNext()) {
			Sampah sampah = iter.next();
			
			if (!paused) {
				sampah.y -= (250 * dropSpeed) * Gdx.graphics.getDeltaTime();
			}
			
			if (sampah.y + sampah.getImage().getHeight() < 0) {
				sampah.dispose();
				iter.remove();
				int newNyawa = Integer.valueOf(nyawa) - 1;
				nyawa = "" + newNyawa;
				if (Integer.valueOf(nyawa) == 0) {
					gameOver();
				}
			}
			
			if (sampah.overlaps(tongOrg) && !gameOver) {
				catchObject1(sampah);
				sampah.dispose();
				iter.remove();
			} else if (sampah.overlaps(tongAnorg) && !gameOver) {
				catchObject2(sampah);
				sampah.dispose();
				iter.remove();
			}
		}
	}
	
	
	private void catchObject1(Sampah sampah) {
		if (sampah.getType() == Type.Organik) {
			if (Settings.soundEnabled) {
				catchRightSound.play();
			}
			int newScore = Integer.valueOf(score) + 20;
			score = "" + newScore;
			
			ijo.setPosition(tongOrg.x + tongOrg.width/2, tongOrg.y + tongOrg.height/2);
			ijo.start();
		} 	
		else {
			if(Integer.valueOf(score) > 0){
				if (Settings.soundEnabled) catchSound.play();
				int newScore = Integer.valueOf(score) - 5;
				score = "" + newScore;
			}
		}
	}
	
	private void catchObject2(Sampah sampah) {
		if (sampah.getType() == Type.Anorganik) {
			if (Settings.soundEnabled) {
				catchRightSound.play();
			}
			int newScore = Integer.valueOf(score) + 20;
			score = "" + newScore;
			
			oren.setPosition(tongAnorg.x + tongAnorg.width/2, tongAnorg.y + tongAnorg.height/2);
			oren.start();
		} 	
		else {
			if(Integer.valueOf(score) > 0){
				if (Settings.soundEnabled) catchSound.play();
				int newScore = Integer.valueOf(score) - 5;
				score = "" + newScore;
			}
		}
	}
	
	private void pindah(){
		
		tempA = tongAnorg.x;
		tempB = tongAnorg.y;
		tongAnorg.setPosition(tongOrg.x, tongOrg.y);
		tongOrg.setPosition(tempA, tempB);
	}
	
	private void gameOver() {
		InGameDialog d = new InGameDialog("Game Over", new Skin(						
				Gdx.files.internal("menuSkin.json"),
				new TextureAtlas(Gdx.files.internal("menuSkin.pack"))), false);
		d.pack();
		d.setPosition(TrashME.WIDTH/2 - d.getWidth()/2, TrashME.HEIGHT/2 - d.getHeight()/2);
		stage.addActor(d);
		if (Integer.valueOf(score) >= Settings.highscores[4]){
			Settings.addScore(Integer.valueOf(score));
			Settings.save();
		}
		paused = true;
		gameOver = true;
	}

	@Override
	public void dispose() {
		if (Settings.soundEnabled) {
			catchRightSound.dispose();
			catchSound.dispose();
			
		}
		gamePlayBackground.dispose();
		tongO.dispose();
		tongA.dispose();
		gerobakImage.dispose();
		scoreImage.dispose();
		nyawaImage.dispose();
		oren.dispose();
		ijo.dispose();
		batch.dispose();
		font18.dispose();
		stage.dispose();
		MainMenuScreen.music.dispose();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void hide() {}

	@Override
	public void pause() { 
		if (!paused && !gameOver) {
			InGameDialog d = new InGameDialog("Paused", new Skin(						
					Gdx.files.internal("menuSkin.json"),
					new TextureAtlas(Gdx.files.internal("menuSkin.pack"))), true);
			d.pack();
		  	d.setPosition(TrashME.WIDTH/2 - d.getWidth()/2, TrashME.HEIGHT/2 - d.getHeight()/2);
			stage.addActor(d);
			paused = true;
		}
	}
	

	@Override
	public void resume() {}
	
	private void myResume() {
		paused = false;
	}
	
	
	public class InGameDialog extends Dialog {

		public InGameDialog(String title, Skin skin, boolean isPaused) {
			super(title, skin);
			
			if (isPaused) {
				button("Yes", "Back");
				button("No", "Resume");
				text("\nReturn to main menu?\n\n");
			} else {
				button("Back", "Back");
				button("Try again", "Try again");
				text("\n      You lose!\nYour score: " + Integer.valueOf(score) + "\n\n");
			}
		}
		
		@Override 
		protected void result(Object object) {
			if (object != null) {
				if (((String)object).equals("Back")) {
					dispose();
					game.setScreen(new MainMenuScreen(game));
				}
				else if (((String)object).equals("Try again")) {
					dispose();
					if (Settings.soundEnabled) MainMenuScreen.music.play();
					game.setScreen(new GameScreen(game));
				}
				else if (((String)object).equals("Resume")) {
					remove();
					myResume();
				}
			}
		}

	}
}
