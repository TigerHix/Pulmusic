package com.tigerhix.pulmusic.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
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
import com.tigerhix.pulmusic.model.Pattern;
import com.tigerhix.pulmusic.model.ScoreCounter;

public class MatchScreen extends AbstractScreen {
	
	public static boolean debug = false;
	
	private Pulmusic game;
	private AssetManager manager;
	
	private int progress; // 0 = resources, 1 = notes, 2 = loaded
	private long startTimeMillis;
	
	private static Stage stage;
	private Beatmap beatmap;
	private Pattern pattern;
	private Music music;
	private Image scanner;
	private Label info;
	
	private double totalTime;
	private int totalRow;
	private boolean paused;
	
	private ScoreCounter counter;
	
	int i;
	
	public MatchScreen(Pulmusic game, Beatmap beatmap) {
		super(game);
		this.game = game;
		this.beatmap = beatmap;
		this.music = Gdx.audio.newMusic(beatmap.getMusicFileHandle());
		// Initialize
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		Gdx.input.setInputProcessor(stage);
		manager = Pulmusic.getManager();
		if (Gdx.app.getType() == ApplicationType.Desktop) MatchScreen.debug = true;
		// Add listener
		stage.addListener(new TouchListener());
	}
	
	public class TouchListener extends InputListener {
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			List<Note> inRange = new ArrayList<Note>();
            for (Note note : pattern.getNotes()) {
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
            for (Note note : pattern.getNotes()) {
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
        	progress ++;
        } else if (progress == 1){
        	// Initialize pattern
    		pattern = new Pattern(beatmap.getPatternLines());
    		pattern.setMatchScreen(this);
    		// Setup counter
    		counter = new ScoreCounter(pattern);
    		progress ++;
        } else if (progress == 2) {
    		setupStage();
    		List<Note> notes = new ArrayList<Note>(pattern.getNotes());
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
            totalRow = (int) ((totalTime + pattern.getOffset()) / pattern.getSpeed());
            // Update info label
            info.setText("FPS: " + Gdx.graphics.getFramesPerSecond()
            		+ "\nSCORE: " + Settings.SCORE_FORMATTER.format(counter.getScore())
            		+ "\nACCURACY: " + Settings.ACCURACY_FORMATTER.format(counter.getAccuracy())
            		+ "\nCOMBO: " + counter.getCombo()
            		+ "\nMAX COMBO: " + counter.getMaxCombo());
            info.setX(stage.getWidth() - info.getTextBounds().width);
            info.setY(stage.getHeight() - info.getTextBounds().height);
            // Update scanner
            if (Settings.SHOW_SCANNER) { 
                scanner.setZIndex(Integer.MAX_VALUE); // Make sure the scanner overlays notes   
                float y;
                if (totalRow % 2 == 0) { // Up to down
        			y = (float) (Settings.NOTEAREA_HEIGHT - Settings.NOTEAREA_HEIGHT * ((totalTime + pattern.getOffset()) % pattern.getSpeed() / pattern.getSpeed()));
                } else { // Down to up
                	y = (float) (Settings.NOTEAREA_HEIGHT * ((totalTime + pattern.getOffset()) % pattern.getSpeed() / pattern.getSpeed()));
                } 
                y -= scanner.getHeight() / 2;
                y += Settings.NOTEAREA_VERTICAL_MARGIN;
                scanner.setY(y);
            }
            // Update notes
            for (Note note : pattern.getNotes()) {
        		float timeDifference = (float) (note.getTime() - totalTime);
        		if (timeDifference >= 0 && timeDifference <= pattern.getSpeed()) {
        			if (!note.isShown()) {
        				note.showNote();
        			}
        		}
        	}
            // Update lines of chains
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            Gdx.gl.glLineWidth(5);
            for (Chain chain : pattern.getChains()) {
            	shapeRenderer.begin(ShapeType.Line);
            	for (int i = 0; i < chain.getNotes().size() - 1; i++) {
            		if (!chain.getNotes().get(i).isShown() || chain.getNotes().get(i).getRow() - totalRow < 0 || totalTime - chain.getNotes().get(i).getTime() >= pattern.getSpeed() / 5) continue;
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
            totalRow = (int) ((totalTime + pattern.getOffset()) / pattern.getSpeed());
            // Update info label
            info.setText("FPS: " + Gdx.graphics.getFramesPerSecond()
            		+ "\nSCORE: " + Settings.SCORE_FORMATTER.format(counter.getScore())
            		+ "\nACCURACY: " + Settings.ACCURACY_FORMATTER.format(counter.getAccuracy())
            		+ "\nCOMBO: " + counter.getCombo()
            		+ "\nMAX COMBO: " + counter.getMaxCombo());
            info.setX(stage.getWidth() - info.getTextBounds().width);
            info.setY(stage.getHeight() - info.getTextBounds().height);
            // Update lines of chains
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            Gdx.gl.glLineWidth(5);
            for (Chain chain : pattern.getChains()) {
            	shapeRenderer.begin(ShapeType.Line);
            	for (int i = 0; i < chain.getNotes().size() - 1; i++) {
            		if (!chain.getNotes().get(i).isShown() || chain.getNotes().get(i).getRow() - totalRow < 0 || totalTime - chain.getNotes().get(i).getTime() >= pattern.getSpeed() / 5) continue;
            		shapeRenderer.setColor(Color.DARK_GRAY);
            		shapeRenderer.line(chain.getNotes().get(i).getX(), chain.getNotes().get(i).getY(), 
                        		chain.getNotes().get(i + 1).getX(), chain.getNotes().get(i + 1).getY());
            	}
            	shapeRenderer.end();
            }
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
		music.play();
	}
	
	public void start() {
		startTimeMillis = System.currentTimeMillis();
	}
	
	public void end() {
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
		music.stop();
		music.dispose();
		new Timer().schedule(new TimerTask() {          
    	    @Override
    	    public void run() {
    	    	progress ++;
    	    	game.resultMatch(counter);
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
