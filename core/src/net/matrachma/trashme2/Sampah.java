package net.matrachma.trashme2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Sampah extends Rectangle {

	private static final long serialVersionUID = 7489554435827692114L;
	
	private Texture sampahImage;		
	private Type tipe;					
	
	public enum Type {
		Organik,
		Anorganik
	}

	
	public Sampah(String imageName, Type tipe) {
		sampahImage = new Texture(Gdx.files.internal(imageName));
		this.tipe = tipe;
	}
	
	
	public void draw(SpriteBatch batch) {
		batch.draw(sampahImage, x, y);
	}
	
	
	public Texture getImage() {
		return sampahImage;
	}
	
	
	public Type getType() {
		return tipe;
	}
	
	
	public void dispose() {
		sampahImage.dispose();
	}
}