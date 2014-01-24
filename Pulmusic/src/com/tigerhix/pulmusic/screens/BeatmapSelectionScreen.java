package com.tigerhix.pulmusic.screens;

import java.io.File;
import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tigerhix.pulmusic.Pulmusic;
import com.tigerhix.pulmusic.Settings;
import com.tigerhix.pulmusic.model.AbstractScreen;
import com.tigerhix.pulmusic.model.Beatmap;

public class BeatmapSelectionScreen extends AbstractScreen {
	
	private Pulmusic game;
	
	private Stage stage;
	
	private Skin skin;
	private ArrayList<Beatmap> beatmaps = new ArrayList<Beatmap>();
	
	public BeatmapSelectionScreen(Pulmusic game) {
		super(game);
		this.game = game;
		FileHandle fileHandle = Gdx.files.internal("data");
		if (Gdx.app.getType() != ApplicationType.Android) {
			fileHandle = Gdx.files.internal("./bin/" + fileHandle.path());
		}
		for (FileHandle entry: fileHandle.list()) {
			if (entry.isDirectory()) {
				boolean isBeatmap = false;
				for (FileHandle entry2 : entry.list()) {
					if (entry2.nameWithoutExtension().equalsIgnoreCase("pattern")) {
						isBeatmap = true;
						break;
					}
					if (isBeatmap) break;
				}
				if (isBeatmap) {
					beatmaps.add(new Beatmap(entry));
				}
			}
		}
		// Load external beatmaps
		if (Gdx.app.getType() == ApplicationType.Android && Gdx.files.isExternalStorageAvailable()) {
			fileHandle = Gdx.files.external("pulmusic" + File.separator + "beatmaps");
			if (!fileHandle.exists()) {
				File folder = new File(Gdx.files.getExternalStoragePath() + "/pulmusic/beatmaps");
				folder.mkdirs();
			}
			if (Gdx.app.getType() != ApplicationType.Android) {
				fileHandle = Gdx.files.internal("./bin/" + fileHandle.path());
			}
			for (FileHandle entry: fileHandle.list()) {
				if (entry.isDirectory()) {
					boolean isBeatmap = false;
					for (FileHandle entry2 : entry.list()) {
						if (entry2.nameWithoutExtension().equalsIgnoreCase("pattern")) {
							isBeatmap = true;
							break;
						}
						if (isBeatmap) break;
					}
					if (isBeatmap) {
						beatmaps.add(new Beatmap(entry));
					}
				}
			}
		}
	}
	
	@Override
	public void show() {
		// Initialize
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		Gdx.input.setInputProcessor(stage);
		// Beatmap list
		ArrayList<String> names = new ArrayList<String>();
		for (Beatmap beatmap : beatmaps) {
			names.add(beatmap.getName());
		}
		// Elements
		skin = Pulmusic.getSkin();
		final int padding = Gdx.graphics.getWidth() / 40;
		final Label info = new Label("", skin);
		final List list = new List(names.toArray(new String[names.size()]), skin);
		final CheckBox cb1 = new CheckBox(" Disable scanner", skin);
		final TextButton start = new TextButton("Start!", skin);
		final ScrollPane scrollPane = new ScrollPane(list, skin);
		scrollPane.setFlickScroll(true);
		scrollPane.setSize(Gdx.graphics.getWidth() / 1.5f - padding * 2, Gdx.graphics.getHeight() - padding * 2);
		scrollPane.setPosition(padding, padding);
		start.setSize(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 1.5f - padding, start.getHeight());
		start.setPosition(scrollPane.getWidth() + padding * 2, padding);
		cb1.setPosition(scrollPane.getWidth() + padding * 2, start.getY() + start.getHeight() + padding);
		stage.addActor(scrollPane);
		stage.addActor(cb1);
		stage.addActor(start);
		stage.addActor(info);
		if (beatmaps.size() > 0) {
			Beatmap beatmap = beatmaps.get(0);
			info.setText("Level: " + beatmap.getLevel() + "\nComposer: " + beatmap.getComposer() + "\nPatterner: " + beatmap.getPatterner() + "\nDescription: " + beatmap.getDescription());
			info.setPosition(scrollPane.getWidth() + padding * 2, Gdx.graphics.getHeight() - info.getHeight() - padding * 2);
		}
		// Listeners
		list.addListener(new ClickListener() {
			@Override
		    public void clicked(InputEvent event, float x, float y) {
				if (beatmaps.size() > 0) {
					Beatmap beatmap = beatmaps.get(list.getSelectedIndex());
					info.setText("Level: " + beatmap.getLevel() + "\nComposer: " + beatmap.getComposer() + "\nPatterner: " + beatmap.getPatterner() + "\nDescription: " + beatmap.getDescription());
					info.setPosition(scrollPane.getWidth() + padding * 2, Gdx.graphics.getHeight() - info.getHeight() - padding * 2);
				}
			};
		});
		start.addListener(new ClickListener() {
			@Override
		    public void clicked(InputEvent event, float x, float y) {
				if (beatmaps.size() == 0) return;
				if (cb1.isChecked()) Settings.SHOW_SCANNER = false;
				game.startMatch(beatmaps.get(list.getSelectedIndex()));
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
		
	}

	@Override
	public void hide() {
		
	}
	
}
