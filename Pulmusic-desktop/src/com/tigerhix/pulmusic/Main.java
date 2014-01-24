package com.tigerhix.pulmusic;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Pulmusic";
		cfg.useGL20 = false;
		cfg.width = 1280;
		cfg.height = 800;
		cfg.fullscreen = false;  
		cfg.useGL20 = false;
		cfg.foregroundFPS = 0;
		cfg.backgroundFPS = 0;
		cfg.forceExit = true;  
		cfg.vSyncEnabled = false;
		new LwjglApplication(new Pulmusic(), cfg);
	}
}
