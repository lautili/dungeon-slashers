package io.github.dungeon_slashers.screens;

import java.util.Random;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.github.dungeon_slashers.Flags;
import io.github.dungeon_slashers.Main;
import io.github.dungeon_slashers.PlayerState;
import io.github.dungeon_slashers.controllers.Battle;
import io.github.dungeon_slashers.controllers.DialMan;
import io.github.dungeon_slashers.controllers.InputMan;
import io.github.dungeon_slashers.controllers.Save;
import io.github.dungeon_slashers.entities.Boss;
import io.github.dungeon_slashers.entities.Hero;
import io.github.dungeon_slashers.floors.Floor;
import io.github.dungeon_slashers.floors.Room;
import io.github.dungeon_slashers.item.Armor;
import io.github.dungeon_slashers.item.Item;
import io.github.dungeon_slashers.item.Weapon;

public class floorScreen implements Screen {
	Hero[] chars;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private int dialogueFlag;
    private float[] x;
    private float[] y;
    private Texture background;
    private int firstSteps;
    private int totSteps;
    private int steps;
    private int level;
    private float timer;
    private BattleScreen battle;
    private BattleScreen bossBattle;
    private Screen nextScreen;
    private int[] currPosition;
    private boolean showMap;
    private Random rand = new Random();
    private Texture[] wallTextures = new Texture[4];
    private Rectangle[] doorColliders = new Rectangle[4];
    private boolean[] wallBooleans = new boolean[4];
    private Rectangle bossBattleActivation;
    private Rectangle treasure;
    
    private boolean isTransitioning = false;
    private float transitionTimer = 0f;
    private BattleScreen targetBattleScreen;
    
    private final int WALL_DOWN = 0;
    private final int WALL_UP = 1;
    private final int WALL_LEFT = 2;
    private final int WALL_RIGHT = 3;
    
    private Main game;
    public Floor floor;
    private boolean colboxes;
    Rectangle playerCol;
    Rectangle[] collisions;
    Rectangle[] doors;
    private Array<Vector2> posHistory;
    private int followDelay = 15;
    
	public floorScreen(Main game, Floor floor, Screen nextScreen, Screen prevScreen, int dialogueFlag) {
		this.game = game;
		this.floor = floor;
		this.nextScreen = nextScreen;
		level = floor.floorType;
		battle = floor.battle;
		bossBattle = floor.bossBattle;
		this.dialogueFlag = dialogueFlag;
		x = new float[4];
		y = new float[4];
		chars = Main.player.getCharacters();
		background = new Texture("sprites/background/background_floor_" + level + ".jpg");
		currPosition = floor.getRoomPosition(Room.ROOM_START);
		wallTextures[WALL_DOWN] = new Texture("sprites/background/background_floor_" + level + "_wall_down.png");
		wallTextures[WALL_UP] = new Texture("sprites/background/background_floor_" + level + "_wall_up.png");
		wallTextures[WALL_LEFT] = new Texture("sprites/background/background_floor_" + level + "_wall_left.png");
		wallTextures[WALL_RIGHT] = new Texture("sprites/background/background_floor_" + level + "_wall_right.png");
		
		doorColliders[WALL_UP] = new Rectangle(134, 148, 51, 32);
		doorColliders[WALL_LEFT] = new Rectangle(0, 47, 20, 46);
		doorColliders[WALL_RIGHT] = new Rectangle(299, 47, 20, 46);
		doorColliders[WALL_DOWN] = new Rectangle(134, 0, 51, 15);
		
		x[0] = 150;
		y[0] = 15;
		colboxes = false;
		playerCol = new Rectangle(x[0], y[0], 15, 15);
		collisions = new Rectangle[8];
		collisions[0] = new Rectangle(0, 148, 134, 32);
		collisions[1] = new Rectangle(184, 148, 135, 32);
		bossBattleActivation = new Rectangle(0, 80, 320, 100);
		collisions[2] = new Rectangle(0, 0, 20, 47);
		collisions[3] = new Rectangle(0, 103, 20, 55);
		
		collisions[4] = new Rectangle(299, 0, 21, 47);
		collisions[5] = new Rectangle(299, 103, 21, 55);
		
		collisions[6] = new Rectangle(0, 0, 134, 15);
		collisions[7] = new Rectangle(185, 0, 135, 15);
		
		doors = new Rectangle[4];
		doors[WALL_UP] = new Rectangle(134, 175, 51, 5);
		doors[WALL_LEFT] = new Rectangle(0, 47, 5, 55);
		doors[WALL_RIGHT] = new Rectangle(315, 47, 5, 55);
		doors[WALL_DOWN] = new Rectangle(134, 0, 51, 5);
		
		treasure = new Rectangle(160 - 12, 90 - 8, 24, 16);
	}
	@Override
    public void show() {
		this.resume();
		game.playMusic(game.dungeonMusic, false);
		if(Main.player.currScreen != "BATTLE_SCREEN" 
				&& Main.player.currScreen != "MENU_SCREEN") {
			currPosition = floor.getRoomPosition(Room.ROOM_START);
			x[0] = 150;
			y[0] = 20;
		}
		timer = 0;
		steps = 0;
		firstSteps = 0;
		totSteps = 0;
		isTransitioning = false;
		transitionTimer = 0f;
		targetBattleScreen = null;
		if(Main.player.currScreen != "MENU_SCREEN") {
			Main.player.state = PlayerState.WAITING;
		}
		showMap = false;
		chars = Main.player.getCharacters();
		Main.player.currScreen = "FLOOR_SCREEN";
		camera = new OrthographicCamera();
		viewport = game.viewport;
		viewport.setCamera(camera);
		camera.setToOrtho(false, 320, 180);
		camera.zoom = 1f;
		checkRoomBooleans();
		if(Main.player.state == PlayerState.WAITING) {
			posHistory = new Array<>();
			for(int i = 0; i < (chars.length * followDelay) + 5; i++) {
	            posHistory.add(new Vector2(x[0], y[0]));
	        }
			for(int i = 1; i < chars.length; i++) {
	            x[i] = x[0];
	            y[i] = y[0];	
	            chars[i].direction = chars[0].direction;
	        }
		}
		if(Main.player.flags[dialogueFlag]) {
			floor.discoverAll();
		}
	}
	@Override
    public void render(float delta) {
		Room room = floor.layout[currPosition[0]][currPosition[1]];
		if(Main.player.state == PlayerState.WAITING) {
    		timer += delta;
    		if(timer >= game.roomDelay) {
    			Main.player.state = PlayerState.IDLE;
    			timer = 0;
    		}
    	}
    	float[] floats = new float[2];
    	if(Main.player.state == PlayerState.IDLE) {
        	floats = InputMan.movement(this, game);
    	}
    	if(InputMan.checkKey("F1", null)) {
    		if(!colboxes) {
    			colboxes = true;
    		}else {
    			colboxes = false;
    		}
    	}
    	if(InputMan.checkKey("F6", null)) {
    		floor.discoverAll();
    	}
    	float moveX = floats[0] * delta;
    	float moveY = floats[1] * delta;
    	
    	boolean moved = false; 
        
        playerCol.x = x[0] + 2 + moveX;
        if (!checkCollision(playerCol)) {
            x[0] += moveX;
            if (moveX != 0) moved = true;
        }
        
        playerCol.x = x[0] + 2;
        playerCol.y = y[0] + moveY;
        if (!checkCollision(playerCol)) {
            y[0] += moveY;
            if (moveY != 0) moved = true;
        }
        playerCol.y = y[0];
        
        if (moved) {
            posHistory.insert(0, new Vector2(x[0], y[0]));
            if (posHistory.size > chars.length * followDelay + 5) {
                posHistory.pop();
            }
            if(room.roomType != Room.ROOM_START && room.roomType != Room.ROOM_BOSS) {
	            if(!isTransitioning) {
	            	if(firstSteps <= 100) {
	            		firstSteps++;
	            	}else {
	            		if(steps < 60) {
	            			steps++;
	            		}else {
	            			totSteps++;
	            			if(rand.nextInt(100) < 8 || totSteps > 100) {
	            				isTransitioning = true;
	            				transitionTimer = 0f;
	            				targetBattleScreen = battle;
	            				targetBattleScreen.battle.initEnemies();
	            				Main.player.state = PlayerState.BUSY;
	            			}else {
	            				steps = 0;
	            			}
	            		}
	            	}
	            }
            }
        }
        
        chars[0].aniManager(floats, delta);
        
        for (int i = 1; i < chars.length; i++) {
            int target = i * followDelay;
            
            if (target < posHistory.size) {
                Vector2 targetPos = posHistory.get(target);
                
                float[] followerFloats = new float[2]; 
                if (moved) {
                    followerFloats[0] = targetPos.x - x[i]; 
                    followerFloats[1] = targetPos.y - y[i]; 
                }
                
                x[i] = targetPos.x;
                y[i] = targetPos.y;
                
                chars[i].aniManager(followerFloats, delta);
            }
        }
    	
    	if(InputMan.checkKey(Main.config.key_interact, null) && Main.player.state == PlayerState.IDLE) {
	    	switch(chars[0].direction) {
	    	case "up":
	    		playerCol.y = y[0] + 10;
	    		break;
	    	case "down":
	    		playerCol.y = y[0] - 10;
	    		break;
	    	case "left":
	    		playerCol.x = x[0] + 2 - 10;
	    		break;
	    	case "right":
	    		playerCol.x = x[0] + 2 + 10;
	    		break;
	    	}
	    	checkInteraction(playerCol);
	    	playerCol.x = x[0]+2;
	    	playerCol.y = y[0];
    	}
    	if(InputMan.checkKey("M")) {
    		if(showMap) {
    			showMap = false;
    			Main.player.state = PlayerState.IDLE;
    		}else {
    			if(Main.player.state == PlayerState.IDLE) {
    				showMap = true;
	    			Main.player.state = PlayerState.BUSY;
    			}
    		}
    	}
    	checkDoors(playerCol);
    	
    	ScreenUtils.clear(0, 0, 0, 1);
    	game.viewport.apply();
    	camera.update();
    	game.batch.setProjectionMatrix(camera.combined);
    	game.batch.begin();
    	
    	game.batch.draw(background, 0, 0);
    	for(int i = 0; i < wallTextures.length; i++) {
    		if(wallBooleans[i] && 
    				!(i == WALL_DOWN  && floor.layout[currPosition[0]][currPosition[1]].roomType == Room.ROOM_START)
    				&& 
    				!(i == WALL_UP  
    					&& floor.layout[currPosition[0]][currPosition[1]].roomType == Room.ROOM_BOSS 
    					&& getBossFlag())
    			) {
    			game.batch.draw(wallTextures[i], 0, 0);
    		}
    	}
    	if(!getBossFlag() && room.roomType == Room.ROOM_BOSS) {
    		Boss boss = (Boss) (bossBattle.battle.possibleEnemy[0]);
        	game.batch.draw(boss.bossIdle, 320 / 2 - boss.bossIdle.getWidth() / 2, 180 / 2 - boss.bossIdle.getHeight() / 2 + 25);
        }
    	if(room.roomType == Room.ROOM_TREASURE) {
    		if(room.isTreasureOpen()) {
    			game.batch.draw(game.chestOpen, treasure.x, treasure.y);
    		}else {
    			game.batch.draw(game.chestClosed, treasure.x, treasure.y);
    		}
    	}
    	game.batch.draw(chars[3].getCurrentFrame(), x[3], y[3]);
    	game.batch.draw(chars[2].getCurrentFrame(), x[2], y[2]);
    	game.batch.draw(chars[1].getCurrentFrame(), x[1], y[1]);
    	game.batch.draw(chars[0].getCurrentFrame(), x[0], y[0]);
    	
    	if(colboxes) {
    		game.batch.draw(game.colBox, playerCol.x, playerCol.y, playerCol.width, playerCol.height);
    		for(int i = 0; i < collisions.length; i++) {
    			Rectangle col = collisions[i];
    			if(col != null) {
    				game.batch.draw(game.colBox, col.x, col.y, col.width, col.height);
    			}
    		}
    		if(treasure != null && room.roomType == Room.ROOM_TREASURE) {
    			game.batch.draw(game.intBox, treasure.x, treasure.y, treasure.width, treasure.height);
    		}
    		for(int i = 0; i < doors.length; i++) {
    			Rectangle col = doors[i];
    			if(col != null) {
    				game.batch.draw(game.doorBox, col.x, col.y, col.width, col.height);
    			}
    		}
    		for(int i = 0; i < doorColliders.length; i++) {
    			Rectangle col = doorColliders[i];
    			if(col != null && wallBooleans[i]) {
    				game.batch.draw(game.colBox, col.x, col.y, col.width, col.height);
    			}
    		}
    	}
    	int resp = DialMan.showDialogues(game, delta);
    	switch(resp) {
    	case 100:
    		isTransitioning = true;
    		transitionTimer = 0f;
    		targetBattleScreen = bossBattle;
    		targetBattleScreen.battle.initEnemies();
    		Main.player.state = PlayerState.BUSY;
    		break;
    	}
    	
    	if(isTransitioning) {
    		transitionTimer += delta;
    		
    		if(transitionTimer < 0.5f) {
    			float alpha = transitionTimer / 0.5f;
    			game.batch.setColor(1, 1, 1, alpha);
    			game.batch.draw(game.black, 0, 0, 320, 180);
    		} else if(transitionTimer < 1.1f) {
    			game.batch.setColor(1, 1, 1, 1);
    			game.batch.draw(game.black, 0, 0, 320, 180);
    			
    			float bgAlpha = (transitionTimer - 0.5f) / 0.6f;
    			Texture bgTex = targetBattleScreen.background;
    			if(bgTex != null) {
    				game.batch.setColor(1, 1, 1, bgAlpha);
    				game.batch.draw(bgTex, 0, 0, 320, 180);
    			}
    		} else {
    			game.batch.setColor(1, 1, 1, 1);
    			targetBattleScreen.lastScreen = this;
    			game.setScreen(targetBattleScreen);
    		}
    		game.batch.setColor(1, 1, 1, 1);
    	}
    	
    	if(showMap) {
    		showMap();
    	}
    	
    	game.batch.end();
    }
    
    private void showMap() {
    	game.batch.draw(game.mapBackground, 0, 0);

	int gridRows = floor.layout.length;
	int gridColumns = floor.layout[0].length;
	int spacing = 1;
	int roomSize = Math.max(1, Math.min(
			(320 - 20 - (gridColumns - 1) * spacing) / gridColumns,
			(180 - 20 - (gridRows - 1) * spacing) / gridRows));
	int mapWidth = (gridColumns * roomSize) + ((gridColumns - 1) * spacing);
	int mapHeight = (gridRows * roomSize) + ((gridRows - 1) * spacing);

	int startX = (320 - mapWidth) / 2;
	int startY = (180 - mapHeight) / 2;

	for(int i = 0; i < gridRows; i++) {
            for(int j = 0; j < gridColumns; j++) {
                if(floor.layout[i][j] != null) {
                    int roomX = startX + j * (roomSize + spacing);
                    int roomY = startY + (gridRows - 1 - i) * (roomSize + spacing);

                    if(i == currPosition[0] && j == currPosition[1]) {
                        game.batch.draw(game.roomCurr, roomX, roomY, roomSize, roomSize);
                        switch(floor.layout[i][j].roomType) {
                        case Room.ROOM_START:
                        	game.batch.draw(game.roomStart, roomX, roomY, roomSize, roomSize);
                        	break;
                        case Room.ROOM_TREASURE:
                        	game.batch.draw(game.roomTreasure, roomX, roomY, roomSize, roomSize);
                        	break;
                        case Room.ROOM_SECRET:
                        	game.batch.draw(game.roomSecret, roomX, roomY, roomSize, roomSize);
                        	break;
                        case Room.ROOM_BOSS:
                        	game.batch.draw(game.roomBoss, roomX, roomY, roomSize, roomSize);
                        	break;
                        }
                    } else if(floor.layout[i][j].discovered == Room.DISC_TOTAL) {
                        game.batch.draw(game.roomDisc, roomX, roomY, roomSize, roomSize);
                        switch(floor.layout[i][j].roomType) {
                        case Room.ROOM_START:
                        	game.batch.draw(game.roomStart, roomX, roomY, roomSize, roomSize);
                        	break;
                        case Room.ROOM_TREASURE:
                        	game.batch.draw(game.roomTreasure, roomX, roomY, roomSize, roomSize);
                        	break;
                        case Room.ROOM_BOSS:
                        	game.batch.draw(game.roomBoss, roomX, roomY, roomSize, roomSize);
                        	break;
                        }
                    } else if (floor.layout[i][j].discovered == Room.DISC_PART){
                    	game.batch.draw(game.roomUnd, roomX, roomY, roomSize, roomSize);
                    }
                }
            }
        }
	}
	private boolean checkInteraction(Rectangle player) {
		Room room = floor.layout[currPosition[0]][currPosition[1]];
		if(room.roomType == Room.ROOM_TREASURE && player.overlaps(treasure)) {
			if(room.isTreasureOpen()) {
				DialMan.addDialogue(0, -1, null, null, "Ya has abierto este cofre.", 20, null);
			}else {
				String msg = "";
				int random = rand.nextInt(3);
				if(random == 0 || random == 2) {
					int gold = rand.nextInt(floor.maxGld - (floor.maxGld - (floor.maxGld / 3))) + (floor.maxGld - floor.maxGld / 3);
					msg += gold + "G";
					Main.player.gold += gold;
				}
				if(random == 1 || random == 2) {
					int n = rand.nextInt(100);
					int n2;
					if(n > 45) {
						n2 = 1;
					}else if (n > 20) {
						n2 = 2;
					}else if(n > 5) {
						n2 = 3;
					}else {
						n2 = 4;
					}
					Item lastItem = null;
					for(int i = 1; i <= n2; i++) {
					    if(!msg.equals("")) {
					        if(i < n2) {
					            msg += ", ";
					        }else {
					            msg += " y ";
					        }
					    }
					    Item item = floor.getRandomItem();
					    if (!item.unique) {
					        if(lastItem == null || !item.getIDName().equals(lastItem.getIDName())) {
					            int q = rand.nextInt(item.maxQ) + 1;
					            if(q > 1) {
					                msg += q + " ";
					            }
					            Main.player.addItems(item, q);
					        }else {
					            i--;
					            continue;
					        }
					    }else {
					        floor.removeFromPool(item);
					        if (item instanceof Armor) {
					            Main.player.addArmors((Armor) item);
					        }else if(item instanceof Weapon){
					            Main.player.addWeapons((Weapon) item);
					        }else {
					            Main.player.addItems(item);
					        }
					    }
					    lastItem = item;
					    msg += item.getName();
					}
				}
				msg += ".";
				DialMan.addDialogue(0, 1, null, null, "Has abierto el cofre!", 20, null);
				DialMan.addDialogue(1, -1, null, null, "Has encontrado " + msg, 20, null);
				room.setTreasureOpen();
			}
			return true;
		}
		return false;
	}
    private boolean checkCollision(Rectangle player) {

        for (Rectangle col : collisions) {

            if (col != null && player.overlaps(col)) {
            	return true;
            }

        }
        
        for (int i = 0; i < doorColliders.length; i++) {
        	Rectangle col = doorColliders[i];
            if (col != null && player.overlaps(col) && 
            		(wallBooleans[i]  && !(i == WALL_DOWN  
            			&& floor.layout[currPosition[0]][currPosition[1]].roomType == Room.ROOM_START)) && 
            			!(i == WALL_UP  
            				&& floor.layout[currPosition[0]][currPosition[1]].roomType == Room.ROOM_BOSS && getBossFlag())
            			
            		) {
            	return true;
            }

        }
        Room room = floor.layout[currPosition[0]][currPosition[1]];
        if(room.roomType == Room.ROOM_TREASURE && player.overlaps(treasure)) {
			return true;
		}
		return false;
    }
    
    private boolean checkDoors(Rectangle player) {
    	if(Main.player.state != PlayerState.IDLE) {
    		return false;
    	}
        for (Rectangle col : doors) {

            if (col != null && player.overlaps(col)) {
            	if(col == doors[WALL_LEFT]) {
            		currPosition[1]--;
            		x[0] = doors[WALL_RIGHT].x - 20;
            	}
            	if(col == doors[WALL_UP]) {
            		if(floor.layout[currPosition[0]][currPosition[1]].roomType == Room.ROOM_BOSS) {
            			game.setScreen(nextScreen);
            		}else {
            			currPosition[0]--;
	            		y[0] = doors[WALL_DOWN].y + 20;
            		}
            	}
            	if(col == doors[WALL_DOWN]) {
            		if(floor.layout[currPosition[0]][currPosition[1]].roomType == Room.ROOM_START) {
            			game.setScreen(game.firstScreen);
            		}else{
	            		currPosition[0]++;
	            		y[0] = doors[WALL_UP].y - 20;
            		}
            	}
            	if(col == doors[WALL_RIGHT]) {
            		currPosition[1]++;
            		x[0] = doors[WALL_LEFT].x + 20;
            	}
            	floor.layout[currPosition[0]][currPosition[1]].discovered = Room.DISC_TOTAL;
            	posHistory = new Array<>();
        		for(int i = 0; i < (chars.length * followDelay) + 5; i++) {
                    posHistory.add(new Vector2(x[0], y[0]));
                }
        		for(int i = 1; i < chars.length; i++) {
                    x[i] = x[0];
                    y[i] = y[0];	
                    chars[i].direction = chars[0].direction;
                }
        		checkRoomBooleans();
            	return true;
            }

        }
        if(player.overlaps(bossBattleActivation) 
        		&& !getBossFlag() 
        		&& floor.layout[currPosition[0]][currPosition[1]].roomType == Room.ROOM_BOSS 
        		) {
        	if(!Main.player.flags[dialogueFlag]) {
	        	Boss boss = (Boss) bossBattle.battle.possibleEnemy[0];
	        	Flags.DialogueFlag(dialogueFlag, boss);
	        	Main.player.flags[dialogueFlag] = true;	        	
	        	Main.player.state = PlayerState.BUSY;
        	}else {
        		DialMan.addDialogue(0, 100);
        		DialMan.addDialogue(100, -1);
        	}
        }
        
        return false;
    }
    
    private void checkRoomBooleans() {
		if(currPosition[0] - 1 < 0 || floor.layout[currPosition[0]-1][currPosition[1]] == null) {
			wallBooleans[WALL_UP] = true;
		}else {
			if(floor.layout[currPosition[0]-1][currPosition[1]].discovered == Room.DISC_NOT)
				floor.layout[currPosition[0]-1][currPosition[1]].discovered = Room.DISC_PART;
			wallBooleans[WALL_UP] = false;
		}
		if(currPosition[0] + 1 >= floor.layout.length || floor.layout[currPosition[0]+1][currPosition[1]] == null) {
			wallBooleans[WALL_DOWN] = true;
		}else {
			if(floor.layout[currPosition[0]+1][currPosition[1]].discovered == Room.DISC_NOT)
				floor.layout[currPosition[0]+1][currPosition[1]].discovered = Room.DISC_PART;
			wallBooleans[WALL_DOWN] = false;
		}
		if(currPosition[1] - 1 < 0 || floor.layout[currPosition[0]][currPosition[1]-1] == null) {
			wallBooleans[WALL_LEFT] = true;
		}else {
			if(floor.layout[currPosition[0]][currPosition[1]-1].discovered == Room.DISC_NOT)
				floor.layout[currPosition[0]][currPosition[1]-1].discovered = Room.DISC_PART;
			wallBooleans[WALL_LEFT] = false;
		}
		if(currPosition[1] + 1 >= floor.layout[0].length || floor.layout[currPosition[0]][currPosition[1]+1] == null) {
			wallBooleans[WALL_RIGHT] = true;
		}else {
			if(floor.layout[currPosition[0]][currPosition[1]+1].discovered == Room.DISC_NOT)
				floor.layout[currPosition[0]][currPosition[1]+1].discovered = Room.DISC_PART;
			wallBooleans[WALL_RIGHT] = false;
		}
	}
    
    @Override
    public void resize(int width, int height) {
        if(width <= 0 || height <= 0) return;
        viewport.update(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
    	game.lastScreen = this;
    }

    @Override
    public void dispose() {
    	game.lastScreen = this;
    }
    
    private boolean getBossFlag() {
    	Boss boss = (Boss) (bossBattle.battle.possibleEnemy[0]);
    	int flag = boss.getFlag();
    	return Main.player.flags[flag];
    }
	public void updatePool(Item[] items) {
		for(Item item : items) {
			for(Item item1 : floor.itemsPool) {
				if(item == null || item1 == null) {
					continue;
				}
				if(item.getIDName().equals(item1.getIDName()) && item.grabbed && item.unique) {
					floor.removeFromPool(item1);
				}
			}
		}
	}

	public void resetPosition() {
		currPosition = floor.getRoomPosition(Room.ROOM_START);
		x[0] = 150;
		y[0] = 20;
	}
}