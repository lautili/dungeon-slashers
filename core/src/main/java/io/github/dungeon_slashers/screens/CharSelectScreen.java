package io.github.dungeon_slashers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.dungeon_slashers.Main;
import io.github.dungeon_slashers.MenuScrollType;
import io.github.dungeon_slashers.controllers.InputMan;
import io.github.dungeon_slashers.controllers.Menu;
import io.github.dungeon_slashers.controllers.Save;
import io.github.dungeon_slashers.entities.Hero;

/** First screen of the application. Displayed after the application is created. */
public class CharSelectScreen implements Screen {
	private int sel;
    private Main game;
    private Hero[] characters;
    private Hero[] selCharacters;
    private boolean[] selChars;
    public CharSelectScreen(Main game) {
		this.game = game;
		characters = new Hero[5];
		selCharacters = new Hero[4];
		selChars = new boolean[5];
		for(int i = 0; i < 5; i++) {
			selChars[i] = false;
			characters[i] = Main.characters[i];
		}
	}
    
	@Override
    public void show() {
        // Prepare your screen here.
		sel = 0;
		Main.player.currScreen = "CHAR_SELECT_SCREEN";
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
    	ScreenUtils.clear(0, 0, 0, 1); //limpia el buffer de colores
    	
    	game.viewport.apply();
    	game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
    	
    	sel = InputMan.scrollInt(MenuScrollType.HORIZONTAL, 5, sel);
    	game.batch.begin();
    	
    	game.mainFont.getData().setScale(0.3f);
    	game.mainFont.draw(game.batch, "SELECCIONE LOS PERSONAJES:", -75, 80f);
    	
    	Menu.showOptionsX(game, game.mainFont, 0.2f, -140, -70, 60, null, sel, "GUERRERO", "MAGA",
    														"LADRONA", "EXPLORADOR", "CURANDERO");
    	
    	game.batch.end();
    	if(InputMan.checkKey("Z")) {
    		if(selChars[sel]) {
    			selChars[sel] = false;
    			removeCharacter(characters[sel]);
    			
    			System.out.println("Personajes seleccionados: " + countChars());
    	    	System.out.println("Personajes en orden: ");
    	    	for(int i = 0; i < 4; i++) {
    	    		if(selCharacters[i] != null) {
    	    			System.out.println(selCharacters[i].getName());
    	    		}
    	    	}
    		}else {
    			if(countChars() < 4) {
    				selChars[sel] = true;
    				System.out.println("Agregando al personaje: " + characters[sel].getName());
    				addCharacter(characters[sel]);
    				
    				System.out.println("Personajes seleccionados: " + countChars());
    		    	System.out.println("Personajes en orden: ");
    		    	for(int i = 0; i < 4; i++) {
    		    		if(selCharacters[i] != null) {
    		    			System.out.println(selCharacters[i].getName());
    		    		}
    		    	}
    			}
    		}
    	}
    	if(InputMan.checkKey("X")) {
    		if(countChars() == 4) {
    			Main.player.setCharacters(selCharacters);
    			game.setScreen(game.firstScreen);
    			dispose();
    		}
    	}
    }

    private int countChars() {
    	int count = 0;
		for(int i = 0; i < 5; i++) {
			if(selChars[i]) count++;
		}
		return count;
	}

	private void removeCharacter(Hero hero) {
    	for(int i = 0; i < 4; i++) {
			if(selCharacters[i] == hero) {
				selCharacters[i] = null;
				return;
			}
		}
	}

	private void addCharacter(Hero hero) {
		for(int i = 0; i < 4; i++) {
			if(selCharacters[i] == null) {
				selCharacters[i] = hero;
				return;
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