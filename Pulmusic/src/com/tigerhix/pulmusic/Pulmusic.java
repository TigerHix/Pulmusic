package com.tigerhix.pulmusic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.tigerhix.pulmusic.model.Beatmap;
import com.tigerhix.pulmusic.screens.ClassicGame;

public class Pulmusic extends Game {
	
	public static String LOG = "Log";
	public static AssetManager MANAGER;
	
	private ClassicGame classicGame;
	
	@Override
	public void create() {	
		MANAGER = new AssetManager();
		classicGame = new ClassicGame(this, new Beatmap(Gdx.files.internal("assets/biotonic")));
		this.setScreen(classicGame);
	}
	
}
