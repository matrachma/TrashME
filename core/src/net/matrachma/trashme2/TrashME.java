package net.matrachma.trashme2;

import net.matrachma.trashme2.screen.GameScreen;
import net.matrachma.trashme2.screen.HighscoresScreen;
import net.matrachma.trashme2.screen.MainMenuScreen;
import net.matrachma.trashme2.screen.SplashScreen;
import net.matrachma.trashme2.screen.StoryScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TrashME extends Game {
	
	public static final String TITLE = "TrashME";
	public static final int WIDTH = 450;
	public static final int HEIGHT = 600;

    public SpriteBatch batcher;
    MainMenuScreen mainMenuScreen;
    StoryScreen storyScreen;
    GameScreen gameScreen;
    HighscoresScreen highScoresScreen;
    SplashScreen splashScreen;
	
	@Override
	public void create() {
        batcher = new SpriteBatch();
    	Settings.load();
		splashScreen = new SplashScreen(this);
		setScreen(splashScreen);
	}
	

}