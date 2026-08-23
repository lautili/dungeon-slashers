package io.github.dungeon_slashers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.dungeon_slashers.Main;
import io.github.dungeon_slashers.MenuScrollType;
import io.github.dungeon_slashers.controllers.InputMan;
import io.github.dungeon_slashers.controllers.Menu;
import io.github.dungeon_slashers.controllers.Save;

/** First screen of the application. Displayed after the application is created. */
public class MainMenuScreen implements Screen {
	private int sel;
    private Main game;
    public MainMenuScreen(Main game) {
		this.game = game;
	}
    
	@Override
    public void show() {
        // Prepare your screen here.
		sel = 0;
		Main.player.currScreen = "MAIN_MENU_SCREEN";
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
    	ScreenUtils.clear(0, 0, 0, 1); //limpia el buffer de colores
    	
    	game.viewport.apply();
    	game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
    	
    	sel = InputMan.scrollInt(MenuScrollType.VERTICAL, 3, sel);
    	game.batch.begin();
    	
    	game.titleFont.draw(game.batch, "DUNGEON SLASHERS", -75, 80f);
    	
    	Menu.showOptionsY(game, game.mainFont, 0.3f, -140, -20, 20, null, sel, "NUEVA PARTIDA", "CARGAR PARTIDA", "SALIR");
    	
    	game.batch.end();
    	if(InputMan.checkKey("Z")) {
    		switch(sel) {
    		case 0:
    			game.setScreen(new FirstScreen(game));
    			dispose();
    			break;
    		case 1:
    			Save.load();
    			dispose();
    			break;
    		case 2:
    			Gdx.app.exit();
    			break;
    		}
    	}
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;
        
        // Resize your screen here. The parameters represent the new window size.
        game.viewport.update(width, height);
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