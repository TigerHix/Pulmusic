package com.tigerhix.pulmusic.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.tigerhix.pulmusic.Pulmusic;
import com.tigerhix.pulmusic.enums.Rank;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class RankShower {
	
	public RankShower(Note note, Rank rank) {
		Texture texture;
		switch (rank) {
		case MISS:
			texture = Pulmusic.getManager().get("ui/miss.png", Texture.class);
			break;
		case BAD:
			texture = Pulmusic.getManager().get("ui/bad.png", Texture.class);
			break;
		case GOOD:
			texture = Pulmusic.getManager().get("ui/good.png", Texture.class);
			break;
		case EXCELLENT:
			texture = Pulmusic.getManager().get("ui/excellent.png", Texture.class);
			break;
		case PERFECT:
			texture = Pulmusic.getManager().get("ui/perfect.png", Texture.class);
			break;
		default:
			texture = Pulmusic.getManager().get("ui/miss.png", Texture.class);
		}
		final Image image = new Image(texture);
		image.setPosition(note.getX() - note.getSize() / 2, note.getY() - note.getSize() / 2);
		image.setSize(note.getSize(), note.getSize());
		image.setOrigin(note.getSize() / 2, note.getSize() / 2);
		note.getStage().addActor(image);
		float scale = note.isTouched() ? 0.8f : 1.0f;
		image.addAction(sequence(scaleTo(scale, scale, 0.1f), scaleTo(scale, scale, 0.1f), fadeOut(0.2f, Interpolation.linear), run(new Runnable() {
			public void run () {
				image.remove();
			}
		})));
	}

}
