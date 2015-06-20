package net.matrachma.trashme2.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.matrachma.trashme2.TrashME;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "TrashME";
		config.resizable = false;
		config.width = 450;
		config.height = 600;
		new LwjglApplication(new TrashME(), config);
	}
}
