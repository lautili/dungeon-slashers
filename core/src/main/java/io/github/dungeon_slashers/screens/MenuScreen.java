package io.github.dungeon_slashers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.dungeon_slashers.Main;
import io.github.dungeon_slashers.MenuScrollType;
import io.github.dungeon_slashers.PlayerState;
import io.github.dungeon_slashers.Skill;
import io.github.dungeon_slashers.controllers.Config;
import io.github.dungeon_slashers.controllers.InputMan;
import io.github.dungeon_slashers.controllers.Menu;
import io.github.dungeon_slashers.controllers.Save;
import io.github.dungeon_slashers.entities.Enemy;
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
    private Config initialConfigCopy;
    private int[] pos = new int[3];
	public MenuScreen(Main game) {
		this.game = game;
		
	}
	@Override
    public void show() {
        // Prepare your screen here.
		chars = Main.player.getCharacters();
		camera = new OrthographicCamera();
		Main.player.currScreen = "MENU_SCREEN";
		viewport = game.viewport;
		viewport.setCamera(camera);
		camera.setToOrtho(false, 320, 180);
		camera.zoom = 2f;
		batch = game.batch;
		Main.player.state = PlayerState.MENU;

		initialConfigCopy = new Config(Main.config.currRes);
		initialConfigCopy.volume = Main.config.volume;
		initialConfigCopy.fullScreen = Main.config.fullScreen;
    }

	@Override
    public void render(float delta) {
    	ScreenUtils.clear(0, 0, 0, 1);
    	
    	Item[] itemArray = null;
    	Skill[] skillArray = null;
    	Enemy[] bestiaryArray = null;
    	
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
    		skillArray = chars[pos[2]].getRealSkills();
    		break;
    	case 4:
    		bestiaryArray = Main.player.getBestiary();
    		break;
    	case 5:
    		
            break;
    	}
    	
    	game.viewport.apply();
    	game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
    	
    	batch.begin();
    	
    	
    	Menu.showOptionsX(game, game.mainFont, 0.3f, -100, 250, 100, null, pos[0], "INVENTARIO", "ARMAS", "ARMADURAS", "HABILIDADES", "BESTIARIO", "AJUSTES");
    	
    	if(pos[0] == 4) {
    		if (bestiaryArray != null) {
    			Menu.showEnemies(game, -100, 220, 10, pos[1], bestiaryArray);
    			if(bestiaryArray.length > 0) {
    				Menu.showEnemyStats(game, bestiaryArray[pos[1]]);
    			}
    		}
    	}else if(pos[0] == 5) {
            // Mostrar panel de configuración utilizando un sub-índice en pos[1]
            Menu.showConfigOptions(game, -100, 220, 20, pos[1], Main.config);
        } else if(pos[0] != 3) {
	    	Menu.showItems(game, -100, 220, 10, pos[1], itemArray);
	    	if(itemArray.length > 0) {
	    		Menu.showItemStats(game, itemArray[pos[1]]);
	    	}
    	} else {
    		Menu.showSkills(game, -100, 220, 10, pos[1], skillArray);
	    	if(skillArray.length > 0) {
	    		Menu.showSkillStats(game, skillArray[pos[1]]);
	    	}
    	}
    	if(pos[0] != 4 && pos[0] != 5) {
    		Menu.showHeroStats(game, chars[pos[2]]);
    	}
    	if(Main.player.state == PlayerState.CHOICE) {
    		game.invFont.getData().setScale(0.6f);
    		game.invFont.draw(batch, "Seleccione \nal personaje \ncon el que usara \nel hechizo.", -150, 40);
    	}
    	batch.end();
    	
    	if(Main.player.state == PlayerState.MENU) {
    		int listLength = 0;
    		if(pos[0] == 4) listLength = (bestiaryArray != null) ? bestiaryArray.length : 0;
    		else if(pos[0] == 5) listLength = 7;
    		else if(pos[0] != 3) listLength = (itemArray != null) ? itemArray.length : 0;
    		else listLength = (skillArray != null) ? skillArray.length : 0;
    		
	    	pos = InputMan.scrollMenu(6, listLength, chars.length, pos, (pos[0] == 3), Menu.MenuMove);
    	} else {
    		pos[2] = InputMan.scrollInt(MenuScrollType.HORIZONTAL, chars.length, pos[2], Menu.MenuMove);
    	}
    	float dif = 0;
    	Config config = Main.config;
    	if(pos[0] == 5) {
    		dif = InputMan.checkKey(config.key_left2, null) ? -1f : InputMan.checkKey(config.key_right2, null) ? 1f : 0;
    		if(pos[1] <= 2) {
    			dif = InputMan.checkHold(config.key_left2, 0.5f) ? -1f : InputMan.checkHold(config.key_right2, 0.5f) ? 1f : dif;
    		}
    		if(dif != 0) {
	    		if (pos[1] == 0) { // Volumen
	    			dif /= 100f;
	    			Main.config.volume += dif;
	    			Main.config.volume = Math.max(Main.config.volume, 0f);
	    			Main.config.volume = Math.min(1.0f, Main.config.volume);
	    		}else if(pos[1] == 1) { 
	    			dif /= 100f;
	    			Main.config.volSFX += dif;
	    			Main.config.volSFX = Math.max(Main.config.volSFX, 0f);
	    			Main.config.volSFX = Math.min(1.0f, Main.config.volSFX);
	    		}else if(pos[1] == 2){
	    			dif /= 100f;
	    			Main.config.volMUSIC += dif;
	    			Main.config.volMUSIC = Math.max(Main.config.volMUSIC, 0f);
	    			Main.config.volMUSIC = Math.min(1.0f, Main.config.volMUSIC);
	    		}else if (pos[1] == 4) { // Resolución
	    			Main.config.currRes += dif;        		
	    			if(Main.config.currRes >= 0 && Main.config.currRes <= 10) {
	    				Main.config.switchRes(game);
	    			}else {
	    				Main.config.currRes -= dif;
	    			}
	    		}
    		}
    	}
    	if(InputMan.checkKey(config.key_back)) {
    		if(Main.player.state == PlayerState.MENU) {
    			
                if (Main.config.volume != initialConfigCopy.volume || 
                    Main.config.fullScreen != initialConfigCopy.fullScreen || 
                    Main.config.currRes != initialConfigCopy.currRes) {
                    Save.saveConfig(Main.config);
                }
                game.setScreen(lastScreen);
                Main.player.state = PlayerState.IDLE;
            }
            dispose();
    	} else if(InputMan.checkKey(config.key_interact)) {
    		if(Main.player.state == PlayerState.MENU && pos[0] == 5) {
                    if (pos[1] == 1) { // Pantalla completa
                        Main.config.fullScreen = !Main.config.fullScreen;
                        game.checkFullscreen();
                    }else if(pos[1] == 3) {
                    	Save.saveConfig(Main.config);
                    	Gdx.app.exit();
                    }else if(pos[1] == 5) {
                    	if(config.keyConfig == Config.KEYS_ARROWS) {
                    		config.keyConfig = Config.KEYS_WASD;
                    	}else {
                    		config.keyConfig = Config.KEYS_ARROWS;
                    	}
                    	config.switchKeys();
                    }
            }
    		if(Main.player.state == PlayerState.MENU) {
	    		if(pos[0] != 4 && itemArray != null && itemArray.length > 0) { // Si no es bestiario
		    		if(itemArray[pos[1]].getClass() == Weapon.class) {
		    			Main.player.changeWeapons(chars[pos[2]], (Weapon) itemArray[pos[1]]);
		    		}else if(itemArray[pos[1]].getClass() == Armor.class) {
		    			Main.player.changeArmors(chars[pos[2]], (Armor) itemArray[pos[1]]);
		    		}else {
		    			itemArray[pos[1]].Use(chars[pos[2]], Main.player);
		    		}
	    		}else if (pos[0] == 3 && skillArray != null && skillArray.length > 0){
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
    		} else {
    			if(skillArray != null) {
    				selSkill.use(chars[lastChar], chars[pos[2]]);
    				pos[2] = lastChar;
    				Main.player.state = PlayerState.MENU;
    			}
    		}
    	} else if(InputMan.checkKey(config.key_alt)) {
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
    	game.lastScreen = this;
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    	game.lastScreen = this;
    }
}