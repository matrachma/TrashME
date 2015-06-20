package net.matrachma.trashme2.screen;

import net.matrachma.trashme2.Settings;
import net.matrachma.trashme2.TrashME;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MainMenuScreen extends ScreenAdapter{
	TrashME game;
	public static Texture mainMenuBackground;
	public static Texture soundOn;
	public static Texture soundOff;
	public static Music music;
	public static Sound clickSound;
	public static BitmapFont font;
	OrthographicCamera guiCam;
	Rectangle soundBounds;
	Rectangle playBounds;
	Rectangle highscoresBounds;
	Rectangle exitBounds;
	Vector3 touchPoint;
	
	public MainMenuScreen (TrashME game) {
		this.game = game;
		mainMenuBackground = new Texture(Gdx.files.internal("mainMenuBG.png"));
		soundOn = new Texture(Gdx.files.internal("soundOn.png"));
		soundOff = new Texture(Gdx.files.internal("soundOff.png"));
		music = Gdx.audio.newMusic(Gdx.files.internal("main.mp3"));
		music.setLooping(true);
		music.setVolume(0.5f);
		if (Settings.soundEnabled) music.play();
		clickSound = Gdx.audio.newSound(Gdx.files.internal("click.wav"));
		guiCam = new OrthographicCamera(450, 600);
		guiCam.position.set(450 / 2, 600 / 2, 0);
		soundBounds = new Rectangle(5, 600 - 69, 64, 64);
		playBounds = new Rectangle(130, 273 + 42, 194, 83);
		highscoresBounds = new Rectangle(130, 273 - 42, 194, 83);
		exitBounds = new Rectangle(130, 273 - 42 -83, 194, 83);
		touchPoint = new Vector3();
	}
	
	public void update () {
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (playBounds.contains(touchPoint.x, touchPoint.y)) {
				if (Settings.soundEnabled) clickSound.play();
				Preferences prefs = Gdx.app.getPreferences("STORY");
				if (!prefs.getBoolean("Shown")) {
					game.setScreen(new StoryScreen(game));
					prefs.putBoolean("Shown",true);
					prefs.flush();
					return;
				} else {
					game.setScreen(new GameScreen(game));
					return;
				}
			}
			if (highscoresBounds.contains(touchPoint.x, touchPoint.y)) {
				if (Settings.soundEnabled) clickSound.play();
				game.setScreen(new HighscoresScreen(game));
				return;
			}
			if (exitBounds.contains(touchPoint.x, touchPoint.y)) {
				if (Settings.soundEnabled) clickSound.play();
				Gdx.app.exit();
				return;
			}
			if (soundBounds.contains(touchPoint.x, touchPoint.y)) {
				if (Settings.soundEnabled) clickSound.play();
				Settings.soundEnabled = !Settings.soundEnabled;
				if (Settings.soundEnabled)
					music.play();
				else
					music.pause();
			}
		}
	}

	public void draw () {
		GL20 gl = Gdx.gl;
		gl.glClearColor(1, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		game.batcher.setProjectionMatrix(guiCam.combined);

		game.batcher.disableBlending();
		game.batcher.begin();
		game.batcher.draw(mainMenuBackground, 0, 0, 450, 600);
		game.batcher.end();

		game.batcher.enableBlending();
		game.batcher.begin();
		game.batcher.draw(Settings.soundEnabled ? soundOn : soundOff, 5, 600 - 69, 64, 64);
		game.batcher.end();	
	}

	@Override
	public void render (float delta) {
		update();
		draw();
	}

	@Override
	public void pause () {
		Settings.save();
	}
	

}