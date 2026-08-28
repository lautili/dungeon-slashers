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
import io.github.dungeon_slashers.controllers.Store;
import io.github.dungeon_slashers.entities.Hero;
import io.github.dungeon_slashers.item.Armor;
import io.github.dungeon_slashers.item.Item;
import io.github.dungeon_slashers.item.Weapon;

/** Menu sala. */
public class StoreScreen implements Screen {
	SpriteBatch batch;
    private FitViewport viewport;
    private OrthographicCamera camera;
    private Main game;
    private Item currItem;
    private int maxQ;
    private int currQ;
    private int[] pos = new int[2];
    private Item[] playerItems;
	public StoreScreen(Main game) {
		this.game = game;
		
	}
	@Override
    public void show() {
        // Prepare your screen here.
		camera = new OrthographicCamera();
		viewport = game.viewport;
		viewport.setCamera(camera);
		camera.setToOrtho(false, 320, 180);
		camera.zoom = 2f;
		batch = game.batch;
		updatePlayerItems();
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
    	ScreenUtils.clear(0, 0, 0, 1); //limpia el buffer de colores
    	Store.player = Main.player;
    	int actualQ = currQ+1;
    	Item[] itemArray = null;
    	
    	switch(pos[0]) {
    	case 0:
    		itemArray = Store.getItems();
    		break;
    	case 1:
    		itemArray = playerItems;
    		break;
    	}
    	
    	if(pos[1] >= itemArray.length) {
    		pos[1] = (pos[1] <= 0) ? 0 : pos[1] - 1;
    	}
    	
    	game.viewport.apply();
    	game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
    	
    	batch.begin();
    	
    	Menu.showOptionsX(game, game.mainFont, 0.4f, -100, 250, 200, null, pos[0], "COMPRAR", "VENDER", Main.player.gold + "G");
	    Menu.showItemsStore(game, -100, 220, 10, pos, itemArray);
	    if(itemArray.length > 0) {
	    	try {
	    		Menu.showItemStats(game, itemArray[pos[1]]);
	    	}catch(Exception e) {
	    		pos[1] = (pos[1] <= 0) ? 0 : pos[1] - 1;
	    	}
	    }
	    
	    if(Main.player.state == PlayerState.CHOICE) {
	    	
	    	float baseWidth = 100f;
	        float x = 360f;
	        float y = 180f;
	        float arrowOffset = 30f;
	        
	    	game.mainFont.setColor(1, 1, 1, 1);
	    	game.mainFont.getData().setScale(0.5f);
	    	if (actualQ > 1) {
	            game.mainFont.draw(batch, "<", x - arrowOffset, y - 10, baseWidth, Align.center, false);
	        }
	    	if (actualQ < maxQ) {
	            game.mainFont.draw(batch, ">", x + arrowOffset, y - 10, baseWidth, Align.center, false);
	        }
	    	game.mainFont.getData().setScale(1f);
	    	game.mainFont.draw(batch, String.valueOf(actualQ), x, y, baseWidth, Align.center, false);
	    }
    	batch.end();
    	if(Main.player.state == PlayerState.MENU) {
    		pos = InputMan.scrollInt(2, itemArray.length, pos);
    	}else {
    		currQ = InputMan.scrollInt(MenuScrollType.HORIZONTAL, maxQ, currQ);
    		actualQ = currQ + 1;
    	}
    	
    	if(InputMan.checkKey("X")) {
    		if(Main.player.state == PlayerState.MENU) {
	    		game.setScreen(game.firstScreen);
	    		Main.player.state = PlayerState.IDLE;
    		}else {
    			Main.player.state = PlayerState.MENU;
    			currQ = 0;
    		}
    		dispose();
    	}else if(InputMan.checkKey("Z")) {
    		if(Main.player.state == PlayerState.MENU) {
	    		if(itemArray.length > 0 && itemArray != null) {
		    		switch(pos[0]) {
		    		case 0:
		    			if(itemArray[pos[1]].unique) {
		    				Store.buy(itemArray[pos[1]]);
		    			}else {
		    				currItem = itemArray[pos[1]];
		    				maxQ = (int) Main.player.gold / currItem.getCost();
		    				Main.player.state = PlayerState.CHOICE;
		    			}
		    			break;
		    		case 1:
		    			if(itemArray[pos[1]].unique) {
		    				Store.sell(itemArray[pos[1]]);
		    			}else {
		    				currItem = itemArray[pos[1]];
		    				maxQ = currItem.q;
		    				Main.player.state = PlayerState.CHOICE;
		    			}
		    			break;
		    		}
		    		updatePlayerItems();
		    		
	    		}
    		}else {
    			switch(pos[0]) {
    			case 0:
    				Store.buy(currItem, actualQ);
    				break;
    			case 1:
    				Store.sell(currItem, actualQ);
    				break;
    			}
    			Main.player.state = PlayerState.MENU;
    		}
    	}
    }
    private void updatePlayerItems() {
    	playerItems = new Item[Main.player.getInventory().length + Main.player.getWeapons().length + Main.player.getArmors().length];
    	Item[] inv = Main.player.getInventory();
		Weapon[] wea = Main.player.getWeapons();
		Armor[] arm = Main.player.getArmors();
		for(int i = 0; i < inv.length; i++) {
			playerItems[i] = inv[i];
		}
		for(int i = 0;i < wea.length; i++) {
			playerItems[i + inv.length] = wea[i];
		}
		for(int i = 0;i < arm.length; i++) {
			playerItems[i + inv.length + wea.length] = arm[i];
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