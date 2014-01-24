package com.tigerhix.pulmusic.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tigerhix.pulmusic.screens.MatchScreen;

public class Pattern {

	private float speed;
	private float offset;
	
	private Map<Integer, Note> notes = new HashMap<Integer, Note>();
	private Map<Integer, Chain> chains = new HashMap<Integer, Chain>();
	
	private MatchScreen matchScreen;
	
	public Pattern(List<String> lines) {
		for (String line : lines) {
    		String[] data = line.split("\t");
    		String type = data[0];
    		if (type.equals("SPEED")) {
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
	
	public void setMatchScreen(MatchScreen matchScreen) {
		this.matchScreen = matchScreen;
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

}
