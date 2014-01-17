package com.tigerhix.pulmusic.model;

import java.util.ArrayList;
import java.util.List;

import com.tigerhix.pulmusic.Settings;
import com.tigerhix.pulmusic.enums.Type;

public class Chain {
	
	private List<Note> notes = new ArrayList<Note>();
	private Note head;
	
	public Chain(List<Note> notes, Note head) {
		this.head = head;
		this.notes = notes;
		for (Note note : this.notes) {
			note.setType(Type.CHAIN);
			if (note == head) note.setSize(Settings.CHAIN_SIZE * 1.5f);
		}
	}

	public List<Note> getNotes() {
		return notes;
	}

	public Note getHead() {
		return head;
	}

}
