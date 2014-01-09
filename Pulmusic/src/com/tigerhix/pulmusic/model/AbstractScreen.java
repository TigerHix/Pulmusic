package com.tigerhix.pulmusic.model;

import com.badlogic.gdx.Screen;
import com.tigerhix.pulmusic.Pulmusic;

public abstract class AbstractScreen implements Screen {

    protected Pulmusic game;

    public AbstractScreen(Pulmusic game) {
        this.game = game;
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
    
}