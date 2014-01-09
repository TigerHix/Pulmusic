package com.tigerhix.pulmusic.model;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.tigerhix.pulmusic.enums.Rank;
import com.tigerhix.pulmusic.enums.Type;

public class Note extends Actor {
	
	// Stored data
	
	private int id;
	private double time;
	
	// Real-time computation
	
	private int row;
	private Type type;
	private Stage stage;
	
	// For detection usages
	
	private boolean shown;
	private boolean touched;
	private Rank rank;
	
	private Note(int id, double time, float x, float length) {
		super();
		
		this.id = id;
		this.time = time;
		
		// Calculate
		
		
		
		if (length > 0) {
			this.type = Type.HOLD;
			addListener(new HoldNoteListener());
		} else {
			this.type = Type.SINGLE;
			addListener(new SingleNoteListener());
		}
		
	}
	
	public class SingleNoteListener extends InputListener {
		
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			
			return true;
        }
		
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            
        }
        
	}
	
	public class HoldNoteListener extends InputListener {
		
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			
			return true;
        }
		
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            
        }
        
	}

}
