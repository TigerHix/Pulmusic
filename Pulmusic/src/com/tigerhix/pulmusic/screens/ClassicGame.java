package com.tigerhix.pulmusic.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.tigerhix.pulmusic.Pulmusic;
import com.tigerhix.pulmusic.model.AbstractScreen;
import com.tigerhix.pulmusic.model.Beatmap;

public class ClassicGame extends AbstractScreen {
	
	private Stage stage;
	private Group actors;
	
	private Beatmap beatmap;
	
	public ClassicGame(Pulmusic game, Beatmap beatmap) {
		super(game);
	}

	@Override
	public void render(float delta) {
		Gdx.app.log(Pulmusic.LOG, "Hello");
		// Clear the screen
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        // Act
        stage.act();
        stage.draw();
		// Update info label
        Label info = (Label) stage.getRoot().findActor("info");
        info.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
        info.setX(stage.getWidth() - info.getTextBounds().width);
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		// Initialize
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		actors = new Group();
		stage.addActor(actors);
		
		Gdx.input.setInputProcessor(stage);
        Texture.setEnforcePotImages(false);
        
        Pulmusic.MANAGER.load("data/scanner.png", Texture.class);

		// Info label
        Label info = new Label("Initializing", new LabelStyle(new BitmapFont(), Color.GRAY));
        info.setName("info");
        info.setY(0); 
        info.setX(stage.getWidth() - info.getTextBounds().width); 
        stage.addActor(info);
        
        // Load assets
        Texture texture = new Texture(Gdx.files.internal("data/scanner.png"));
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        
        Image scanner = new Image(texture);
        scanner.setName("scanner");
        scanner.setOrigin(scanner.getWidth() / 2, scanner.getHeight() / 2);
        scanner.setSize(stage.getWidth(), stage.getHeight() * 0.05f);
        scanner.setTouchable(Touchable.disabled);
        
        stage.addActor(scanner);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
