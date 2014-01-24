package com.tigerhix.pulmusic.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tigerhix.pulmusic.Pulmusic;
import com.tigerhix.pulmusic.Settings;
import com.tigerhix.pulmusic.model.AbstractScreen;
import com.tigerhix.pulmusic.model.ScoreCounter;

public class ScoreScreen extends AbstractScreen {
	
	private Pulmusic game;
	private ScoreCounter counter;
	
	private Stage stage;
	private Skin skin;

	public ScoreScreen(Pulmusic game, ScoreCounter counter) {
		super(game);
		// Initialize
		this.game = game;
		this.counter = counter;
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		// Clear the screen
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		skin = Pulmusic.getSkin();
		Label info = new Label("Info", skin);
		info.setText("PERFECT: " + (counter.getPerfect() + counter.getExcellent()) + "\nGOOD: " + counter.getGood() + "\nBAD: "
				+ counter.getBad() + "\nMISS: " + counter.getMiss() + "\nACCURACY: " + Settings.ACCURACY_FORMATTER.format(counter.getAccuracy()) + "\nSCORE: " + Settings.SCORE_FORMATTER.format(counter.getScore())
				+ "\nMAX COMBO: " + counter.getMaxCombo());
		info.setSize(Gdx.graphics.getWidth() * 0.8f, info.getHeight());
		info.setPosition(Gdx.graphics.getWidth() / 2 - info.getWidth() / 2, Gdx.graphics.getHeight() / 2 - info.getHeight() / 3);
		TextButton back = new TextButton("Back", skin);
		TextButton retry = new TextButton("Retry", skin);
		back.setSize(200, retry.getHeight());
		back.setPosition(info.getX() + info.getWidth() - back.getWidth(), info.getY() - 50);
		retry.setSize(200, retry.getHeight());
		retry.setPosition(back.getX(), back.getY() + retry.getHeight() + 10);
		stage.addActor(info);
		stage.addActor(retry);
		stage.addActor(back);
		// Listeners
		retry.addListener(new ClickListener() {
			@Override
		    public void clicked(InputEvent event, float x, float y) {
				game.retryMatch();
		    };
		});	
		back.addListener(new ClickListener() {
			@Override
		    public void clicked(InputEvent event, float x, float y) {
				game.endMatch();
		    };
		});	
	}

	@Override
	public void hide() {
		
	}

}
