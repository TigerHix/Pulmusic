package com.tigerhix.pulmusic.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.tigerhix.pulmusic.Pulmusic;
import com.tigerhix.pulmusic.Settings;
import com.tigerhix.pulmusic.Utils;
import com.tigerhix.pulmusic.enums.Type;
import com.tigerhix.pulmusic.model.AbstractScreen;
import com.tigerhix.pulmusic.model.Beatmap;
import com.tigerhix.pulmusic.model.Chain;
import com.tigerhix.pulmusic.model.Note;
import com.tigerhix.pulmusic.model.ScoreCounter;

public class MatchScreen extends AbstractScreen {
	
	private Pulmusic game;
	private AssetManager manager;
	
	private String path;
	
	private int progress; // 0 = resources, 1 = notes, 2 = loaded
	private long startTimeMillis;
	
	private static Stage stage;
	private Beatmap beatmap;
	private Image scanner;
	private Label info;
	
	private double totalTime;
	private int totalRow;
	private boolean paused;
	
	private ScoreCounter counter;
	
	public MatchScreen(Pulmusic game, String path) {
		super(game);
		this.game = game;
		this.path = path;
		// Initialize
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		Gdx.input.setInputProcessor(stage);
		// Load UI resources
		manager = Pulmusic.getManager();
		manager.load("ui/scanner.png", Texture.class);
		manager.load("ui/noteUp.png", Texture.class);
		manager.load("ui/noteDown.png", Texture.class);
		manager.load("ui/chainUp.png", Texture.class);
		manager.load("ui/chainDown.png", Texture.class);
		manager.load("ui/shadow.png", Texture.class);
		manager.load("ui/perfect.png", Texture.class);
		manager.load("ui/excellent.png", Texture.class);
		manager.load("ui/good.png", Texture.class);
		manager.load("ui/bad.png", Texture.class);
		manager.load("ui/miss.png", Texture.class);
		// Add listener
		stage.addListener(new TouchListener());
	}
	
	public class TouchListener extends InputListener {
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			List<Note> inRange = new ArrayList<Note>();
            for (Note note : beatmap.getNotes()) {
            	if (note.isShown() && !note.isTouched() && !note.isRemoved()) {
            		if (Utils.difference(x, note.getX()) < Settings.NOTE_SIZE / 1.5 && Utils.difference(y, note.getY()) < Settings.NOTE_SIZE / 1.5) {
            			inRange.add(note);
            		}
            	}
            }
            if (inRange.size() == 0) return true;
            Note selectedNote = inRange.get(0);
            for (Note note : inRange) {
            	if (note.getId() < selectedNote.getId()) {
            		selectedNote = note;
            	}
            }
            selectedNote.touchNote();
            return true;
		}
		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            
		}
		@Override
		public void touchDragged (InputEvent event, float x, float y, int pointer) {
			List<Note> inRange = new ArrayList<Note>();
            for (Note note : beatmap.getNotes()) {
            	if (note.getType() == Type.CHAIN && note.isShown() && !note.isTouched() && !note.isRemoved()) {
            		if (Utils.difference(x, note.getX()) < Settings.NOTE_SIZE / 2.5 && Utils.difference(y, note.getY()) < Settings.NOTE_SIZE / 2.5) {
            			inRange.add(note);
            		}
            	}
            }
            if (inRange.size() == 0) return;
            Note selectedNote = inRange.get(0);
            for (Note note : inRange) {
            	if (note.getId() < selectedNote.getId()) {
            		selectedNote = note;
            	}
            }
            selectedNote.touchNote();
		}
	}

	@Override
	public void render(float delta) {
		// Clear the screen
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        if (progress == 0) {
        	if (manager.update()) {
    			progress ++;
    			manager.get("ui/scanner.png", Texture.class).setFilter(TextureFilter.Linear, TextureFilter.Linear);
    			manager.get("ui/noteUp.png", Texture.class).setFilter(TextureFilter.Linear, TextureFilter.Linear);
    			manager.get("ui/noteDown.png", Texture.class).setFilter(TextureFilter.Linear, TextureFilter.Linear);
    			manager.get("ui/chainUp.png", Texture.class).setFilter(TextureFilter.Linear, TextureFilter.Linear);
    			manager.get("ui/chainDown.png", Texture.class).setFilter(TextureFilter.Linear, TextureFilter.Linear);
    			manager.get("ui/shadow.png", Texture.class).setFilter(TextureFilter.Linear, TextureFilter.Linear); 
    			manager.get("ui/perfect.png", Texture.class).setFilter(TextureFilter.Linear, TextureFilter.Linear);
    			manager.get("ui/excellent.png", Texture.class).setFilter(TextureFilter.Linear, TextureFilter.Linear);
    			manager.get("ui/good.png", Texture.class).setFilter(TextureFilter.Linear, TextureFilter.Linear);
    			manager.get("ui/bad.png", Texture.class).setFilter(TextureFilter.Linear, TextureFilter.Linear); 
    			manager.get("ui/miss.png", Texture.class).setFilter(TextureFilter.Linear, TextureFilter.Linear); // Set filters
    	    } else {
    	    	float progress = manager.getProgress();
    			Gdx.app.log("", "Loading resources: " + progress);
    	    }
        } else if (progress == 1){
        	// Load beatmap
    		beatmap = new Beatmap(this, Gdx.files.internal("data" + File.separator + path));	 
    		// Setup counter
    		counter = new ScoreCounter(beatmap);
    		progress ++;
        } else if (progress == 2) {
    		setupStage();
    		List<Note> notes = new ArrayList<Note>(beatmap.getNotes());
    		Collections.reverse(notes);
    		for (Note note : notes) {			
    			note.setPosition(note.getX(), note.getY());
    			note.setSize(Settings.NOTE_SIZE, Settings.NOTE_SIZE);
    			note.setOrigin(Settings.NOTE_SIZE / 2, Settings.NOTE_SIZE / 2);
    			stage.addActor(note);
    		}
    		progress ++;
        } else if (progress == 3) {
        	progress ++;
        } else if (progress == 4) {
        	play();
        	new Timer().schedule(new TimerTask() {          
        	    @Override
        	    public void run() {
        	    	start();
        	    }
        	}, (long) (Settings.BEATMAP_DELAY * 1000));
        	progress ++;
        } else if (progress == 5) {
        	// Act & draw
            stage.act();
            stage.draw();
            // Update variables
            totalTime = (double) (System.currentTimeMillis() - startTimeMillis) / 1000;
            totalRow = (int) ((totalTime + beatmap.getOffset()) / beatmap.getSpeed());
            // Update info label
            info.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
            info.setX(stage.getWidth() - info.getTextBounds().width);
            // Update scanner
            if (Settings.SHOW_SCANNER) { 
                scanner.setZIndex(Integer.MAX_VALUE); // Make sure the scanner overlays notes   
                float y;
                if (totalRow % 2 == 0) { // Up to down
        			y = (float) (Settings.NOTEAREA_HEIGHT - Settings.NOTEAREA_HEIGHT * ((totalTime + beatmap.getOffset()) % beatmap.getSpeed() / beatmap.getSpeed()));
                } else { // Down to up
                	y = (float) (Settings.NOTEAREA_HEIGHT * ((totalTime + beatmap.getOffset()) % beatmap.getSpeed() / beatmap.getSpeed()));
                } 
                y -= scanner.getHeight() / 2;
                y += Settings.NOTEAREA_VERTICAL_MARGIN;
                scanner.setY(y);
            }
            // Update notes
            for (Note note : beatmap.getNotes()) {
        		float timeDifference = (float) (note.getTime() - totalTime);
        		if (timeDifference >= 0 && timeDifference <= beatmap.getSpeed()) {
        			if (!note.isShown()) {
        				note.showNote();
        			}
        		}
        	}
            // Update lines of chains
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            Gdx.gl.glLineWidth(5);
            for (Chain chain : beatmap.getChains()) {
            	shapeRenderer.begin(ShapeType.Line);
            	for (int i = 0; i < chain.getNotes().size() - 1; i++) {
            		if (!chain.getNotes().get(i).isShown() || chain.getNotes().get(i).getRow() - totalRow < 0 || totalTime - chain.getNotes().get(i).getTime() >= beatmap.getSpeed() / 5) continue;
            		shapeRenderer.setColor(Color.DARK_GRAY);
            		shapeRenderer.line(chain.getNotes().get(i).getX(), chain.getNotes().get(i).getY(), 
                        		chain.getNotes().get(i + 1).getX(), chain.getNotes().get(i + 1).getY());
            	}
            	shapeRenderer.end();
            }
        } else if (progress == 6) {
        	// Act & draw
            stage.act();
            stage.draw();
            // Update variables
            totalTime = (double) (System.currentTimeMillis() - startTimeMillis) / 1000;
            totalRow = (int) ((totalTime + beatmap.getOffset()) / beatmap.getSpeed());
            // Update info label
            info.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
            info.setX(stage.getWidth() - info.getTextBounds().width);
        }
	}
	

	@Override
	public void resize(int width, int height) {
		
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
	
	public void setupStage() {	 
        // Background
        Image background = beatmap.getBackground();
        background.setName("background");
        background.setSize(stage.getWidth(), stage.getHeight());
        background.setTouchable(Touchable.disabled);
        background.setColor(background.getColor().r, background.getColor().g, background.getColor().b, 0.1f);
        background.setZIndex(0);
        stage.addActor(background);
        // Scanner
        if (Settings.SHOW_SCANNER) { 
        	scanner = new Image(manager.get("ui/scanner.png", Texture.class));
            scanner.setName("scanner");
            scanner.setOrigin(scanner.getWidth() / 2, scanner.getHeight() / 2);
            scanner.setSize(stage.getWidth(), stage.getHeight() * 0.05f);
            scanner.setTouchable(Touchable.disabled);
            stage.addActor(scanner); 
        }
        // Info label
        info = new Label("Initializing", new LabelStyle(new BitmapFont(), Color.GRAY));
        info.setName("info");
        info.setY(0); 
        info.setX(stage.getWidth() - info.getTextBounds().width); 
        stage.addActor(info);  
	}
	
	public void play() {
		Music music = beatmap.getMusic();
		music.play();
	}
	
	public void start() {
		startTimeMillis = System.currentTimeMillis();
	}
	
	public void end() {
		Music music = beatmap.getMusic();
		progress ++;
		scanner.addAction(sequence(fadeOut(0.5f, Interpolation.linear), run(new Runnable() {
			public void run () {
				scanner.remove();
			}
		})));
		if (!music.isPlaying()) {
			result();
		} else {
			music.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(Music music) {
					result();
				}
			});
		}
	}
	
	public void result() {
		Music music = beatmap.getMusic();
		music.dispose();
		new Timer().schedule(new TimerTask() {          
    	    @Override
    	    public void run() {
    	    	game.endMatch(counter);
    	    }
    	}, 3000);
	}
	
	public static Stage getStage() {
		return stage;
	}

	public double getTotalTime() {
		return totalTime;
	}

	public int getTotalRow() {
		return totalRow;
	}

	public boolean isPaused() {
		return paused;
	}
	
	public ScoreCounter getScoreCounter() {
		return counter;
	}

}
