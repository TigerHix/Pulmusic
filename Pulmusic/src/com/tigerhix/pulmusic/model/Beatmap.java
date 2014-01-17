package com.tigerhix.pulmusic.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.tigerhix.pulmusic.screens.MatchScreen;

public class Beatmap {
	
	private MatchScreen matchScreen;
	
	private String level;
	private String name;
	private float speed;
	private float offset;
	
	private Map<Integer, Note> notes = new HashMap<Integer, Note>();
	private Map<Integer, Chain> chains = new HashMap<Integer, Chain>();
	private Image background;
	private Music music;
	
	public Beatmap(MatchScreen matchScreen, FileHandle fileHandle) {
		this.matchScreen = matchScreen;
		if (Gdx.app.getType() != ApplicationType.Android) {
			fileHandle = Gdx.files.internal("./bin/" + fileHandle.path());
		}
		for (FileHandle entry: fileHandle.list()) {
			if (entry.name().equalsIgnoreCase("beatmap.txt")) {
				loadBeatmap(Arrays.asList(entry.readString().split("\n")));
			} else if (entry.nameWithoutExtension().equalsIgnoreCase("background")) {
				Texture texture = new Texture(entry);
				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				background = new Image(texture);
			} else if (entry.name().equalsIgnoreCase("music.mp3")) {
				music = Gdx.audio.newMusic(entry);
			}
		}
		
	}
	
	private void loadBeatmap(List<String> lines) {
		for (String line : lines) {
    		String[] data = line.split("\t");
    		String type = data[0];
    		if (type.equals("LEVEL")) {
    			level = data[1];
    		} else if (type.equals("NAME")) {
    			name = data[1];
    		} else if (type.equals("SPEED")) {
    			speed = Float.valueOf(data[1]);
    		} else if (type.equals("OFFSET")) {
    			offset = Float.valueOf(data[1]) + speed;
    		} else if (type.equals("NOTE")) {
    			notes.put(Integer.valueOf(data[1]), new Note(this, Integer.valueOf(data[1]), Float.valueOf(data[2]), Float.valueOf(data[3])));
    		} else if (type.equals("LINK")) {
    			List<Note> notes = new ArrayList<Note>();
    			for (int i = 1; i < data.length; i++) {
    				Note note = null;
    				try {
    					note = getNoteById(Integer.valueOf(data[i]));
    				} catch (NumberFormatException ignore) {
    					
    				}
    				if (note != null) notes.add(note);
    			}
    			Chain chain = new Chain(notes, notes.get(0));
    			chains.put(notes.get(0).getId(), chain);
    		}
    	}
	}

	public MatchScreen getMatchScreen() {
		return matchScreen;
	}

	public String getLevel() {
		return level;
	}

	public String getName() {
		return name;
	}

	public float getSpeed() {
		return speed;
	}

	public float getOffset() {
		return offset;
	}
	
	public Note getNoteById(int id) {
		return notes.get(id);
	}

	public Collection<Note> getNotes() {
		return notes.values();
	}
	
	public Collection<Chain> getChains() {
		return chains.values();
	}

	public Image getBackground() {
		return background;
	}

	public Music getMusic() {
		return music;
	}

}
