package net.matrachma.trashme2.screen;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import net.matrachma.trashme2.TrashME;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class SplashScreen implements Screen{
	TrashME game;
	private Texture texture = new Texture(Gdx.files.internal("splashlogo.png")); 
    private Image splashImage = new Image(texture);								
    private Stage stage = new Stage(new FitViewport(TrashME.WIDTH, TrashME.HEIGHT));
    
    public SplashScreen(TrashME game){
        this.game = game;
    }
    
    @Override
	public void show() {

		stage.addActor(splashImage);
		
		splashImage.setColor(1, 1, 1, 0);
		splashImage.setPosition(TrashME.WIDTH/2 - splashImage.getWidth()/2, TrashME.HEIGHT/2 - splashImage.getHeight()/2);
		splashImage.addAction(Actions.sequence(fadeIn(1f), delay(1f), fadeOut(2f), run(new Runnable() {
            @Override
            public void run() {
            	((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
            }
        })));
	}

	@Override
	public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        stage.act(delta);
        stage.draw();
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		texture.dispose();
		stage.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
	
	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
