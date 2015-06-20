package net.matrachma.trashme2.screen;


import net.matrachma.trashme2.Settings;
import net.matrachma.trashme2.TrashME;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class HighscoresScreen extends ScreenAdapter {
	TrashME game;
	OrthographicCamera guiCam;
	Rectangle backBounds;
	Vector3 touchPoint;
	String[] highScores;
	float xOffset = 0;
	GlyphLayout glyphLayout = new GlyphLayout();
	BitmapFont font20, font24;
	public static Texture highscoreBG;

	public HighscoresScreen (TrashME game) {
		this.game = game;

		guiCam = new OrthographicCamera(450, 600);
		guiCam.position.set(450 / 2, 600 / 2, 0);
		backBounds = new Rectangle(0, 0, 64, 64);
		touchPoint = new Vector3();
		highScores = new String[5];
		highscoreBG = new Texture(Gdx.files.internal("backgroundHS.png"));
		FileHandle fontFile = Gdx.files.internal("GearsOfPeace.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        FreeTypeFontParameter param1 = new FreeTypeFontParameter();
        FreeTypeFontParameter param2 = new FreeTypeFontParameter();
        param1.size = 20;
        param2.size = 24;
        font20 = generator.generateFont(param1);
        font24 = generator.generateFont(param2);
        generator.dispose();
		for (int i = 0; i < 5; i++) {
			highScores[i] = i + 1 + ". " + Settings.highscores[i];
			glyphLayout.setText(font20, highScores[i]);
			xOffset = Math.max(glyphLayout.width, xOffset);
		}
		xOffset = 160 - xOffset / 2 + font20.getSpaceWidth() / 2;
	}

	public void update () {
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (backBounds.contains(touchPoint.x, touchPoint.y)) {
				if (Settings.soundEnabled) MainMenuScreen.clickSound.play();
				MainMenuScreen.music.stop();
				game.setScreen(new MainMenuScreen(game));
				return;
			}
		}
	}

	public void draw () {
		GL20 gl = Gdx.gl;
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		guiCam.update();

		game.batcher.setProjectionMatrix(guiCam.combined);
		game.batcher.disableBlending();
		game.batcher.begin();
		game.batcher.draw(highscoreBG, 0, 0, 450, 600);
		game.batcher.end();

		game.batcher.enableBlending();
		game.batcher.begin();
		font24.draw(game.batcher, "HIGHSCORES", 120, 400);
		float y = 230;
		for (int i = 4; i >= 0; i--) {
			font20.draw(game.batcher, highScores[i], xOffset, y);
			y += font20.getLineHeight();
		}
		game.batcher.end();
	}

	@Override
	public void render (float delta) {
		update();
		draw();
	}
}
