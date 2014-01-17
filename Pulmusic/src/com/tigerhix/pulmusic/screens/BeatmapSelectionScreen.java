package com.tigerhix.pulmusic.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tigerhix.pulmusic.Pulmusic;
import com.tigerhix.pulmusic.Settings;
import com.tigerhix.pulmusic.model.AbstractScreen;

public class BeatmapSelectionScreen extends AbstractScreen {
	
	private Pulmusic game;
	
	private Stage stage;
	
	private Skin skin;
	private String[] names = 
			{
			" C.0 / Blue Eyes / Persona / 9",
			" C.0 / Megaera / switchworks / 8.5",
			" C.5 / Biotonic / ani / 9",
			" C.7 / L2 (Ver. B) / ICE / 10",
			" C.S / Requiem / eyemedia / 9"
			};
	private String[] entries =
			{
			"blueeyes",
			"megaera",
			"biotonic",
			"l2b",
			"requiem"
			};
	private String path = entries[0];
	
	public BeatmapSelectionScreen(Pulmusic game) {
		super(game);
		this.game = game;
	}
	
	@Override
	public void show() {
		// Initialize
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		Gdx.input.setInputProcessor(stage);
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		final CheckBox cb1 = new CheckBox(" Disable scanner", skin);
		TextButton start = new TextButton("Start!", skin);
		final List list = new List(names, skin);
		ScrollPane scrollPane = new ScrollPane(list, skin);
		scrollPane.setFlickScroll(false);
		scrollPane.setSize(Gdx.graphics.getWidth() / 1.5f, Gdx.graphics.getHeight() / 1.5f);
		scrollPane.setPosition(Gdx.graphics.getWidth() / 2 - scrollPane.getWidth() / 2, Gdx.graphics.getHeight() / 2 - scrollPane.getHeight() / 3);
		cb1.setPosition(scrollPane.getX(), scrollPane.getY() - 50);
		start.setSize(200, start.getHeight());
		start.setPosition(scrollPane.getX() + scrollPane.getWidth() - start.getWidth(), scrollPane.getY() - 50);
		stage.addActor(scrollPane);
		stage.addActor(cb1);
		stage.addActor(start);
		// Listeners
		start.addListener(new ClickListener() {
			@Override
		    public void clicked(InputEvent event, float x, float y) {
				if (cb1.isChecked()) Settings.SHOW_SCANNER = false;
				path = entries[list.getSelectedIndex()];
				game.startMatch(path);
		    };
		});	
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
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	
}
