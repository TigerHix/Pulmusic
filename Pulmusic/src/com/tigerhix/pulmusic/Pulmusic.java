package com.tigerhix.pulmusic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tigerhix.pulmusic.model.Beatmap;
import com.tigerhix.pulmusic.model.ScoreCounter;
import com.tigerhix.pulmusic.screens.BeatmapSelectionScreen;
import com.tigerhix.pulmusic.screens.MatchScreen;
import com.tigerhix.pulmusic.screens.ScoreScreen;

public class Pulmusic extends Game {
	
	private static AssetManager manager;
	private static LabelStyle labelStyle; 
	private static Skin skin;
	
	private static BeatmapSelectionScreen beatmapSelectionScreen;
	private static MatchScreen matchScreen;
	private static ScoreScreen scoreScreen;
	
	private Beatmap lastBeatmap;
	private boolean filterSet;
	
	public Pulmusic() {
		super();
		Texture.setEnforcePotImages(false);
		manager = new AssetManager();
	}
	
	public void startMatch(Beatmap beatmap) {
		if (scoreScreen != null) {
			scoreScreen.dispose();
			scoreScreen = null;
		}
		if (matchScreen == null) {
			matchScreen = new MatchScreen(this, beatmap);
			setScreen(matchScreen);
			lastBeatmap = beatmap;
		}
	}
	
	public void retryMatch() {
		startMatch(lastBeatmap);
	}
	
	public void resultMatch(ScoreCounter counter) {
		if (matchScreen != null) {
			matchScreen.dispose();
			matchScreen = null;
		}
		if (scoreScreen == null) {
			scoreScreen = new ScoreScreen(this, counter);
			setScreen(scoreScreen);
		}
	}
	
	public void endMatch() {
		if (scoreScreen != null) {
			scoreScreen.dispose();
			scoreScreen = null;
		}
		setScreen(beatmapSelectionScreen);
	}
	
	@Override
	public void create() {
		new Settings();
		// Load resources
		manager.load("ui/scanner.png", Texture.class);
		manager.load("ui/noteUp.png", Texture.class);
		manager.load("ui/noteDown.png", Texture.class);
		manager.load("ui/chainUp.png", Texture.class);
		manager.load("ui/chainDown.png", Texture.class);
		manager.load("ui/shadow.png", Texture.class);
		manager.load("ui/perfect.png", Texture.class);
		manager.load("ui/excellent.png", Texture.class);
		manager.load("ui/good.png", Texture.class);
		manager.load("ui/bad.png", Texture.class);
		manager.load("ui/miss.png", Texture.class);
		labelStyle = new LabelStyle(new BitmapFont(), Color.BLACK);
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		// Screen
		beatmapSelectionScreen = new BeatmapSelectionScreen(this);
		setScreen(beatmapSelectionScreen);
	}
	
	@Override
	public void render() {
		super.render();
		if (manager.update() && !filterSet) {
			filterSet = true;
			// Set filters
			manager.get("ui/scanner.png", Texture.class).setFilter(TextureFilter.Linear, TextureFilter.Linear);
			manager.get("ui/noteUp.png", Texture.class).setFilter(TextureFilter.Linear, TextureFilter.Linear);
			manager.get("ui/noteDown.png", Texture.class).setFilter(TextureFilter.Linear, TextureFilter.Linear);
			manager.get("ui/chainUp.png", Texture.class).setFilter(TextureFilter.Linear, TextureFilter.Linear);
			manager.get("ui/chainDown.png", Texture.class).setFilter(TextureFilter.Linear, TextureFilter.Linear);
			manager.get("ui/shadow.png", Texture.class).setFilter(TextureFilter.Linear, TextureFilter.Linear); 
			manager.get("ui/perfect.png", Texture.class).setFilter(TextureFilter.Linear, TextureFilter.Linear);
			manager.get("ui/excellent.png", Texture.class).setFilter(TextureFilter.Linear, TextureFilter.Linear);
			manager.get("ui/good.png", Texture.class).setFilter(TextureFilter.Linear, TextureFilter.Linear);
			manager.get("ui/bad.png", Texture.class).setFilter(TextureFilter.Linear, TextureFilter.Linear); 
			manager.get("ui/miss.png", Texture.class).setFilter(TextureFilter.Linear, TextureFilter.Linear); 
		}
		if (this.getScreen() != null) return;
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}

	public static AssetManager getManager() {
		return manager;
	}

	public static LabelStyle getLabelStyle() {
		return labelStyle;
	}

	public static void setLabelStyle(LabelStyle labelStyle) {
		Pulmusic.labelStyle = labelStyle;
	}

	public static Skin getSkin() {
		return skin;
	}

	public static void setSkin(Skin skin) {
		Pulmusic.skin = skin;
	}
	
}
