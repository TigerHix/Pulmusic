package com.tigerhix.pulmusic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.tigerhix.pulmusic.model.ScoreCounter;
import com.tigerhix.pulmusic.screens.MatchScreen;
import com.tigerhix.pulmusic.screens.BeatmapSelectionScreen;
import com.tigerhix.pulmusic.screens.ScoreScreen;

public class Pulmusic extends Game {
	
	private static AssetManager manager;
	
	private static BeatmapSelectionScreen beatmapSelectionScreen;
	private static MatchScreen matchScreen;
	private static ScoreScreen scoreScreen;
	
	public Pulmusic() {
		super();
		Texture.setEnforcePotImages(false);
		manager = new AssetManager();
	}
	
	public void startMatch(String path) {
		//if (matchScreen != null) {
			matchScreen = new MatchScreen(this, path);
			setScreen(matchScreen);
		//}
	}
	
	public void endMatch(ScoreCounter counter) {
		if (matchScreen != null) {
			matchScreen.dispose();
			matchScreen = null;
		}
		if (scoreScreen != null) {
			scoreScreen = new ScoreScreen(this, counter);
			setScreen(scoreScreen);
		}
	}
	
	@Override
	public void create() {
		new Settings();
		beatmapSelectionScreen = new BeatmapSelectionScreen(this);
		setScreen(beatmapSelectionScreen);
	}
	
	@Override
	public void render() {
		super.render();
		if (this.getScreen() != null) return;
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}

	public static AssetManager getManager() {
		return manager;
	}
	
}
