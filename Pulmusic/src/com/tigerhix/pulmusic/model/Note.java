package com.tigerhix.pulmusic.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tigerhix.pulmusic.Pulmusic;
import com.tigerhix.pulmusic.Settings;
import com.tigerhix.pulmusic.enums.Rank;
import com.tigerhix.pulmusic.enums.Type;

public class Note extends Actor {
	
	private Beatmap beatmap;
	
	// Stored data
	
	private int id;
	private float time;
	
	// Real-time computation
	
	private int row;
	private Type type = Type.SINGLE;
	private float size = Settings.NOTE_SIZE;
	private float x;
	private float y;
	
	// For detection usages
	
	private boolean shown;
	private boolean touched;
	private boolean removed;
	private Rank rank;
	
	// Sprites
	
	private Sprite noteSprite;
	private Sprite shadowSprite;
	
	public Note(Beatmap beatmap, int id, float time, float x) {
		super();
		this.beatmap = beatmap;
		this.id = id;
		this.time = time;
		// Calculate the position, row of the note
		row = (int) ((time + beatmap.getOffset()) / beatmap.getSpeed()); // Row
		this.x = (float) (x * Settings.NOTEAREA_WIDTH);
		this.x += Settings.NOTEAREA_HORIZONTAL_MARGIN;
		if (row % 2 == 0) { // Up to down
			this.y = (float) (Settings.NOTEAREA_HEIGHT - Settings.NOTEAREA_HEIGHT * ((time + beatmap.getOffset()) % beatmap.getSpeed() / beatmap.getSpeed()));
        } else { // Down to up
        	this.y = (float) (Settings.NOTEAREA_HEIGHT * ((time + beatmap.getOffset()) % beatmap.getSpeed() / beatmap.getSpeed()));
        } 
		this.y += Settings.NOTEAREA_VERTICAL_MARGIN; // Position
		// Now setup the actor & sprite
		setVisible(false);
		noteSprite = new Sprite(Pulmusic.getManager().get("ui/note" + (row % 2 == 0 ? "Down" : "Up") + ".png", Texture.class));
		noteSprite.setOrigin(noteSprite.getWidth() / 2, noteSprite.getHeight() / 2);
		shadowSprite = new Sprite(Pulmusic.getManager().get("ui/shadow.png", Texture.class));
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		double timeDifference = this.getTime() - beatmap.getMatchScreen().getTotalTime();
		// Enlarging effect
		float newSize = type == Type.SINGLE
				? (float) (size - timeDifference / beatmap.getSpeed() * (size / 4))
				: (float) (size - timeDifference / beatmap.getSpeed() * (size / 8));
		if (newSize > size) newSize = size; // Otherwise the note will be enlarging infinitely..
		// Flashing effect
		if (timeDifference <= 0.05 && timeDifference > -0.25) {
			float size2 = (float) (newSize * 1.1);
			float alpha = (float) (1 - Math.abs(timeDifference + 0.05) / 0.3);
			shadowSprite.setPosition(getX() - size2 / 2, getY() - size2 / 2);
			shadowSprite.setSize(size2, size2);
			shadowSprite.draw(batch, alpha);
		}		
		if (removed) return;
		// If not removed, check can it be removed
		if (!removed && timeDifference < -0.3) {
			removeNote(Rank.MISS);
			return;
		}
		noteSprite.setPosition(getX() - newSize / 2, getY() - newSize / 2);
		noteSprite.setSize(newSize, newSize);
		noteSprite.draw(batch);
	}
	
	public void showNote() {
		shown = true;
		setVisible(true);
	}
	
	public void touchNote() {
		touched = true;
		double timing = Math.abs(this.getTime() - beatmap.getMatchScreen().getTotalTime());
		Rank rank = Rank.MISS;
		if (type == Type.SINGLE) { 
			if (timing >= 0.81) {
				rank = Rank.MISS;
			} else if (timing >= 0.31) {
				rank = Rank.BAD;
			} else if (timing >= 0.16) {
				rank = Rank.GOOD;
			} else if (timing >= 0.06) {
				rank = Rank.EXCELLENT;
			} else {
				rank = Rank.PERFECT;
			}
		} else if (type == Type.CHAIN) {
			if (timing >= 0.51 && this.getTime() - beatmap.getMatchScreen().getTotalTime() < 0) {
				rank = Rank.MISS;
			} else if (timing >= 0.16) {
				rank = Rank.EXCELLENT;
			} else {
				rank = Rank.PERFECT;
			}
		}
		removeNote(rank);
	}
	
	public void removeNote(Rank rank) {
		removed = true;
		if (id == beatmap.getNotes().size() - 1) { // Last note
			beatmap.getMatchScreen().end();
		}
		beatmap.getMatchScreen().getScoreCounter().add(rank);
		new RankShower(this, rank);
	}
	
	public void clean() {
		remove();
	}

	public Sprite getSprite() {
		return noteSprite;
	}

	public int getId() {
		return id;
	}

	public float getTime() {
		return time;
	}

	public int getRow() {
		return row;
	}

	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
		// Update sprite
		switch (type) {
		case SINGLE:
			size = Settings.NOTE_SIZE;
			break;
		case CHAIN:
			size = Settings.CHAIN_SIZE;
			noteSprite = new Sprite(Pulmusic.getManager().get("ui/chain" + (row % 2 == 0 ? "Down" : "Up") + ".png", Texture.class));
			noteSprite.setOrigin(noteSprite.getWidth() / 2, noteSprite.getHeight() / 2);
			break;
		default:
			break;
		}
	}
	
	public float getSize() {
		return size;
	}
	
	public void setSize(float size) {
		this.size = size;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public boolean isShown() {
		return shown;
	}

	public boolean isTouched() {
		return touched;
	}
	
	public boolean isRemoved() {
		return removed;
	}

	public Rank getRank() {
		return rank;
	}

}
