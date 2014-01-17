package com.tigerhix.pulmusic;

import com.badlogic.gdx.Gdx;

public class Settings {
	
	public static double NOTEAREA_HEIGHT;
	public static double NOTEAREA_WIDTH;
	public static double NOTEAREA_HORIZONTAL_MARGIN;
	public static double NOTEAREA_VERTICAL_MARGIN;
	
	public static float NOTE_SIZE;
	public static float CHAIN_SIZE;
	
	public static boolean SHOW_SCANNER = true;
	public static double BEATMAP_DELAY = 0.08;
	
	public Settings() {
		NOTEAREA_HEIGHT = Gdx.graphics.getHeight() / 1 * 0.8;
		NOTEAREA_WIDTH = Gdx.graphics.getWidth();
		NOTEAREA_HORIZONTAL_MARGIN = 0;
		NOTEAREA_VERTICAL_MARGIN = Gdx.graphics.getHeight() / 1 * 0.1;
		NOTE_SIZE = (float) (Gdx.graphics.getHeight() / 1 * 0.22);
		CHAIN_SIZE = (float) (Gdx.graphics.getHeight() / 1 * 0.12);
	}

}
