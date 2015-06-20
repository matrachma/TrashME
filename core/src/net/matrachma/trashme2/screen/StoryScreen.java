package net.matrachma.trashme2.screen;

import net.matrachma.trashme2.Settings;
import net.matrachma.trashme2.TrashME;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class StoryScreen extends ScreenAdapter{
		TrashME game;
		
		OrthographicCamera guiCam;
		Rectangle nextBounds;
		Vector3 touchPoint;
		String files[] = new String[]{"story1.png","story2.png","story3.png","story4.png","story5.png",
				"story6.png","story7.png","story8.png","story9.png","story10.png"};
		Texture[] storyImages = new Texture[10];
		int current;
		
		public StoryScreen(TrashME game) {
			this.game = game;
		    guiCam = new OrthographicCamera();
		    guiCam.setToOrtho(false, 450, 600);
		    nextBounds = new Rectangle(450 - 66, 600 - 66, 64, 64);
		    touchPoint = new Vector3();
		    for(int i = 0; i < 10 ; i++){
		        storyImages[i] = new Texture(Gdx.files.internal(files[i]));
		    }
		    current = 0;
		}
		
		public void update(){
		    if (Gdx.input.justTouched()){
		        guiCam.unproject(touchPoint.set(Gdx.input.getX(),Gdx.input.getY(),0));
		
		        if (nextBounds.contains(touchPoint.x, touchPoint.y)) {
		        	if (Settings.soundEnabled)MainMenuScreen.clickSound.play();
		            current ++;
		            if ( current == 10){
		                current = 0;
		                game.setScreen(new GameScreen(game));
		            }
		        }
		    }
		}
		
		public void draw(){
		    GL20 gl = Gdx.gl;
		    gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		    gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		    guiCam.update();
		    game.batcher.setProjectionMatrix(guiCam.combined);
		    game.batcher.disableBlending();
		    game.batcher.begin();
		    game.batcher.draw(storyImages[current], 0, 0);
		    game.batcher.end();
		}
		
		@Override
		public void render(float delta) {
		    super.render(delta);
		    update();
		    draw();
		}
		
		@Override
		public void dispose() {
		    super.dispose();
		    for(int i = 0; i < 10; i ++){
		        storyImages[i].dispose();
		    }
		}
		
	
}