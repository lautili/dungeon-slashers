package io.github.dungeon_slashers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.dungeon_slashers.Main;
import io.github.dungeon_slashers.MenuScrollType;
import io.github.dungeon_slashers.Player;
import io.github.dungeon_slashers.PlayerState;
import io.github.dungeon_slashers.Skill;
import io.github.dungeon_slashers.controllers.InputMan;
import io.github.dungeon_slashers.controllers.Menu;
import io.github.dungeon_slashers.entities.Hero;
import io.github.dungeon_slashers.item.Armor;
import io.github.dungeon_slashers.item.Item;
import io.github.dungeon_slashers.item.Weapon;

/** Menu sala. */
public class MenuScreen implements Screen {
	SpriteBatch batch;
	Hero[] chars;
    private FitViewport viewport;
    private OrthographicCamera camera;
    private Main game;
    private int lastChar;
    private Skill selSkill;
    public Screen lastScreen;
    private int[] pos = new int[3];
	public MenuScreen(Main game) {
		this.game = game;
		
	}
	@Override
    public void show() {
        // Prepare your screen here.
		chars = Main.player.getCharacters();
		camera = new OrthographicCamera();
		viewport = game.viewport;
		viewport.setCamera(camera);
		camera.setToOrtho(false, 320, 180);
		camera.zoom = 2f;
		batch = game.batch;
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
    	ScreenUtils.clear(0, 0, 0, 1); //limpia el buffer de colores
    	
    	Item[] itemArray = null;
    	Skill[] skillArray = null;
    	
    	switch(pos[0]) {
    	case 0:
    		itemArray = Main.player.getInventory();
    		break;
    	case 1:
    		itemArray = Main.player.getWeapons();
    		break;
    	case 2:
    		itemArray = Main.player.getArmors();
    		break;
    	case 3:
    		System.out.println("Consiguiendo las skills de " + chars[pos[2]].getName());
    		skillArray = chars[pos[2]].getRealSkills();
    		for (int i = 0; i < skillArray.length; i++) {
        	    System.out.println(
        	        i + ": " + skillArray[i] + 
        	        " | " + (skillArray[i] != null ? skillArray[i].getName() : "NULL"));
        	}
    		break;
    	}
    	
    	game.viewport.apply();
    	game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
    	
    	batch.begin();
    	
    	Menu.showOptionsX(game, game.invFont, 0.4f, -100, 250, 140, null, pos[0], "INVENTARIO", "ARMAS", "ARMADURAS", "HABILIDADES");
    	if(pos[0] != 3) {
	    	Menu.showItems(game, -100, 220, 10, pos[1], itemArray);
	    	if(itemArray.length > 0) {
	    	Menu.showItemStats(game, itemArray[pos[1]]);
	    	}
    	}else {
    		System.out.println(skillArray);
    		Menu.showSkills(game, -100, 220, 10, pos[1], skillArray);
	    	if(skillArray.length > 0) {
	    	Menu.showSkillStats(game, skillArray[pos[1]]);
	    	}
    	}
    	Menu.showHeroStats(game, chars[pos[2]]);
    	if(Main.player.state == PlayerState.CHOICE) {
    		game.invFont.getData().setScale(0.6f);
    		game.invFont.draw(batch, "Seleccione \nal personaje \ncon el que usara \nel hechizo.", -150, 40);
    	}
    	batch.end();
    	if(Main.player.state == PlayerState.MENU) {
	    	if(pos[0] != 3) {
	    		pos = InputMan.scrollMenu(4, itemArray.length, chars.length, pos, false);
	    	}else {
	    		pos = InputMan.scrollMenu(4, skillArray.length, chars.length, pos, true);
	    	}
    	}else {
    		pos[2] = InputMan.scrollInt(MenuScrollType.HORIZONTAL, chars.length, pos[2]);
    	}
    	
    	if(InputMan.checkKey("X")) {
    		if(Main.player.state == PlayerState.MENU) {
	    		game.setScreen(lastScreen);
	    		Main.player.state = PlayerState.IDLE;
    		}else {
    			
    		}
    		dispose();
    	}else if(InputMan.checkKey("Z")) {
    		if(Main.player.state == PlayerState.MENU) {
	    		if(itemArray != null && itemArray.length > 0) {
		    		if(itemArray[pos[1]].getClass() == Weapon.class) {
		    			Main.player.changeWeapons(chars[pos[2]], (Weapon) itemArray[pos[1]]);
		    		}else if(itemArray[pos[1]].getClass() == Armor.class) {
		    			Main.player.changeArmors(chars[pos[2]], (Armor) itemArray[pos[1]]);
		    		}else {
		    			itemArray[pos[1]].Use(chars[pos[2]], Main.player);
		    		}
	    		}else if (skillArray != null && skillArray.length > 0){
	    			if(skillArray[pos[1]].getMenu()) {
	    				if(skillArray[pos[1]].getSkillType() == 3) {
	    				selSkill = skillArray[pos[1]];
	    				lastChar = pos[2];
	    				Main.player.state = PlayerState.CHOICE;
	    				}else {
	    					skillArray[pos[1]].use(chars[pos[2]], chars);
	    				}
	    			}
	    		}
    		}else {
    			if(skillArray != null) {
    				selSkill.use(chars[lastChar], chars[pos[2]]);
    				pos[2] = lastChar;
    				Main.player.state = PlayerState.MENU;
    			}
    		}
    	}else if(InputMan.checkKey("C")) {
    		chars[pos[2]].modHP(-20);
    	}
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
    public void updateGame(Main game) {
    	this.game = game;
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