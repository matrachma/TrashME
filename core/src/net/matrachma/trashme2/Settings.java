package net.matrachma.trashme2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Settings {
	public static boolean soundEnabled = true;
	public static int[] highscores = new int[] {100, 80, 50, 30, 10};
	public final static String file = ".trashme";
	
	public static void load() {
		try {
			FileHandle filehandle = Gdx.files.external(file);
			
			String[] strings = filehandle.readString().split("\n");
			
			soundEnabled = Boolean.parseBoolean(strings[0]);
			for (int i = 0; i < 5; i++){
				highscores[i] = Integer.parseInt(strings[i+1]);
			}
		} catch (Throwable e){
			// default
		}
	}
	
	public static void save () {
		try {
			FileHandle filehandle = Gdx.files.external(file);
			
			filehandle.writeString(Boolean.toString(soundEnabled)+"\n", false);
			for (int i = 0; i < 5; i++) {
				filehandle.writeString(Integer.toString(highscores[i])+"\n", true);
			}
		} catch (Throwable e) {
		}
	}
	public static void addScore (int score) {
		for (int i = 0; i < 5; i++) {
			if (highscores[i] < score) {
				for (int j = 4; j > i; j--)
					highscores[j] = highscores[j - 1];
				highscores[i] = score;
				break;
			}
		}
	}
}