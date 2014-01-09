package com.tigerhix.pulmusic.model;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Beatmap {
	
	private List<Note> notes;
	private String level;
	private Image background;
	
	public Beatmap(String path) {
		readBeatmap(List<String> lines = Arrays.asList(Gdx.files.internal("data/songs/" + path + "/beatmap.txt").readString().split("\n"));)
	}

}
