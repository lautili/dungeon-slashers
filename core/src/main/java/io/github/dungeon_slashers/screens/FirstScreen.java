package io.github.dungeon_slashers.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.dungeon_slashers.Main;
import io.github.dungeon_slashers.PlayerState;
import io.github.dungeon_slashers.controllers.DialMan;
import io.github.dungeon_slashers.controllers.InputMan;
import io.github.dungeon_slashers.entities.Hero;

/** Primera sala. */
public class FirstScreen implements Screen {
	Hero[] chars;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private float x;
    private float y;
    private Texture background;
    private Main game;
	public FirstScreen(Main game) {
		this.game = game;
		chars = Main.player.getCharacters();
		background = new Texture("sprites/background/background_firstScreen.jpg");
		x = 50;
		y = 50;
		DialMan.addDialogue(0, 1, chars[0].getName(), chars[0].getPortrait(), "Esta es una prueba de dialogos. asjdaajsd s d d adoalalala lalalalaallala lolololololololo", 20);
		DialMan.addDialogue(1, 2, chars[1].getName(), chars[1].getPortrait(), "Esta es una prueba de dialogos con otra foto. Hola", 60);
		DialMan.addChoice(2, 20, "A quien preferis", new String[] {chars[0].getName(), chars[1].getName(), "Los 2", "Ninguno"}, new int[] {3, 4, 5, 6});
		DialMan.addDialogue(3, -1, chars[0].getName(), chars[0].getPortrait(), "Gracias", 20);
		DialMan.addDialogue(4, -1, chars[1].getName(), chars[1].getPortrait(), "Gracias", 20);
		DialMan.addDialogue(5, -1, "Los 2", null, "Gracias", 20);
		DialMan.addDialogue(6, -1, null, null, "Te miran con cara de culo", 20);
	}
	@Override
    public void show() {
        // Prepare your screen here.
		chars = Main.player.getCharacters();
		Main.player.currScreen = "FIRST_SCREEN";
		camera = new OrthographicCamera();
		viewport = game.viewport;
		viewport.setCamera(camera);
		camera.setToOrtho(false, 320, 180);
		camera.zoom = 1f;
	}

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
    	float[] floats = new float[2];
    	if(Main.player.state == PlayerState.IDLE) {
        	floats = InputMan.movement(this, game);
    	}
    	x += floats[0] * delta;
    	y += floats[1] * delta;
    	chars[0].aniManager(floats, delta);
    	
    	ScreenUtils.clear(0, 0, 0, 1); //limpia el buffer de colores
    	camera.update();
    	game.batch.setProjectionMatrix(camera.combined);
    	game.batch.begin();
    	game.batch.draw(background, 0, 0);
    	game.batch.draw(chars[0].getCurrentFrame(), x, y);
    	DialMan.showDialogues(game, delta);
    	game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;
        
        // Resize your screen here. The parameters represent the new window size.
        viewport.update(width, height);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    }
}