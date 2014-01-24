package com.tigerhix.pulmusic.model;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Beatmap {
	
	private float level;
	private String name;
	private String composer;
	private String patterner;
	private String description;
	private List<String> patternLines;
	private Image background;
	private FileHandle musicFileHandle;
	
	public Beatmap(FileHandle fileHandle) {
		for (FileHandle entry: fileHandle.list()) {
			if (entry.nameWithoutExtension().equalsIgnoreCase("info")) {
				for (String line : Arrays.asList(entry.readString().split("\n"))) {
					String[] data = line.split("\t");
		    		String type = data[0];
		    		if (type.equals("LEVEL")) {
		    			level = Float.parseFloat(data[1].trim());
		    		} else if (type.equals("NAME")) {
		    			name = data[1].trim();
		    		} else if (type.equals("COMPOSER")) {
		    			composer = data[1].trim();
		    		} else if (type.equals("PATTERNER")) {
		    			patterner = data[1].trim();
		    		} else if (type.equals("DESCRIPTION")) {
		    			setDescription(data[1].trim());
		    		}
				}
			} else if (entry.nameWithoutExtension().equalsIgnoreCase("pattern")) {
				patternLines = Arrays.asList(entry.readString().split("\n"));
			} else if (entry.nameWithoutExtension().equalsIgnoreCase("background")) {
				Texture texture = new Texture(entry);
				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				background = new Image(texture);
			} else if (entry.nameWithoutExtension().equalsIgnoreCase("music")) {
				musicFileHandle = entry;
			}
		}
	}

	public List<String> getPatternLines() {
		return patternLines;
	}

	public void setPatternLines(List<String> patternLines) {
		this.patternLines = patternLines;
	}

	public Image getBackground() {
		return background;
	}

	public void setBackground(Image background) {
		this.background = background;
	}

	public float getLevel() {
		return level;
	}

	public void setLevel(float level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComposer() {
		return composer;
	}

	public void setComposer(String composer) {
		this.composer = composer;
	}

	public String getPatterner() {
		return patterner;
	}

	public void setPatterner(String patterner) {
		this.patterner = patterner;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public FileHandle getMusicFileHandle() {
		return musicFileHandle;
	}

	public void setMusicFileHandle(FileHandle musicFileHandle) {
		this.musicFileHandle = musicFileHandle;
	}

}
