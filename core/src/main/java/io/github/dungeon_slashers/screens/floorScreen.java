package io.github.dungeon_slashers.screens;

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
import io.github.dungeon_slashers.entities.Hero;
import io.github.dungeon_slashers.floors.Floor;
import io.github.dungeon_slashers.floors.Room;

/** Primera sala. */
public class floorScreen implements Screen {
	Hero[] chars;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private float[] x;
    private float[] y;
    private Texture background;
    private int level;
    private Battle battle;
    private int[] currPosition;
    private boolean showMap;
    
    private Texture[] wallTextures = new Texture[4];
    private Rectangle[] doorColliders = new Rectangle[4];
    private boolean[] wallBooleans = new boolean[4];
    
    private final int WALL_DOWN = 0;
    private final int WALL_UP = 1;
    private final int WALL_LEFT = 2;
    private final int WALL_RIGHT = 3;
    
    private Main game;
    private Floor floor;
    private boolean colboxes;
    Rectangle playerCol;
    Rectangle[] collisions;
    Rectangle[] interactions;
    Rectangle[] doors;
    private Array<Vector2> posHistory; // Breadcrumb. un array de posiciones que se irá guardando
    								   // cada vez que el jugador se mueva y hará que el resto de 
    								   // personajes sigan al principal
    			//Array es una lista mejor que ArrayList
    			//Vector 2 es una clase que guarda dos posiciones x e y
    private int followDelay = 15; //el delay con el que lo seguiran
    
	public floorScreen(Main game, Floor floor) {
		this.game = game;
		this.floor = floor;
		level = floor.floorType;
		battle = floor.battle;
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
		
		x[0] = 90;
		y[0] = 40;
		colboxes = false;
		playerCol = new Rectangle(x[0], y[0], 15, 15);
		collisions = new Rectangle[8];
		collisions[0] = new Rectangle(0, 148, 134, 32);
		collisions[1] = new Rectangle(184, 148, 135, 32);
		
		collisions[2] = new Rectangle(0, 0, 20, 47);
		collisions[3] = new Rectangle(0, 103, 20, 55);
		
		collisions[4] = new Rectangle(299, 0, 21, 47);
		collisions[5] = new Rectangle(299, 103, 21, 55);
		
		collisions[6] = new Rectangle(0, 0, 134, 15);
		collisions[7] = new Rectangle(185, 0, 135, 15);
		
		interactions = new Rectangle[1];
		
		doors = new Rectangle[4];
		doors[WALL_UP] = new Rectangle(134, 175, 51, 5);
		doors[WALL_LEFT] = new Rectangle(0, 47, 5, 55);
		doors[WALL_RIGHT] = new Rectangle(315, 47, 5, 55);
		doors[WALL_DOWN] = new Rectangle(134, 0, 51, 5);
	}
	@Override
    public void show() {
        // Prepare your screen here.
		this.resume();
		showMap = false;
		chars = Main.player.getCharacters();
		Main.player.currScreen = "FIRST_FLOOR_SCREEN";
		camera = new OrthographicCamera();
		viewport = game.viewport;
		viewport.setCamera(camera);
		camera.setToOrtho(false, 320, 180);
		camera.zoom = 1f;
		checkRoomBooleans();
		
		posHistory = new Array<>(); // inicializa el ArrayList
		for(int i = 0; i < (chars.length * followDelay) + 5; i++) {
			//mientras que i sea menor a la length de chars * la cantidad de frames...
            posHistory.add(new Vector2(x[0], y[0])); //añade un nuevo vector2 con las posiciones
            										 // del primer personaje
        }
		for(int i = 1; i < chars.length; i++) {
            x[i] = x[0];	//pone las posiciones de cada personaje en 0
            y[i] = y[0];	
            chars[i].direction = chars[0].direction; //pone a todos en la misma direccion
        }
	}
	@Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
    	float[] floats = new float[2];
    	if(Main.player.state == PlayerState.IDLE) {
        	floats = InputMan.movement(this, game);
    	}
    	if(InputMan.checkKey("F1")) {
    		if(!colboxes) {
    			colboxes = true;
    		}else {
    			colboxes = false;
    		}
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
        
        if (moved) { //si el personaje principal se movió
            posHistory.insert(0, new Vector2(x[0], y[0])); //inserta la posicion del chars[0]
            											   // en la posicion 0 del vector2
            if (posHistory.size > chars.length * followDelay + 5) {
                posHistory.pop(); //evita que la lista siga creando posiciones cuando
                				  //ya creó todos los fotogramas que necesitaba
            }
        }
        
        chars[0].aniManager(floats, delta);
        
        for (int i = 1; i < chars.length; i++) {
            int target = i * followDelay; // que el target dependa de la ubicacion a la que va 
            							// a ir cada personaje. El personaje 2
            							// tiene que ir a la posicion donde estaba el 1
            							// hace 15 frames, el 3 hace 30, etc.
            
            if (target < posHistory.size) { //si no se pasa
                Vector2 targetPos = posHistory.get(target); //busca el vector que se guardó
                											// en target.
                
                float[] followerFloats = new float[2]; 
                if (moved) {
                    followerFloats[0] = targetPos.x - x[i]; 
                    followerFloats[1] = targetPos.y - y[i]; 
                }
                //la parte de arriba es para saber a donde se mueve y llevarlo allí
                
                x[i] = targetPos.x;
                y[i] = targetPos.y;
                
                chars[i].aniManager(followerFloats, delta);
            }
        }
    	
    	if(InputMan.checkKey("Z") && Main.player.state == PlayerState.IDLE) {
    		System.out.println("chequeando colisiones en direccion " + chars[0].direction);
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
    			showMap = true;
    			Main.player.state = PlayerState.BUSY;
    		}
    	}
    	checkDoors(playerCol);
    	
    	ScreenUtils.clear(0, 0, 0, 1); //limpia el buffer de colores
    	camera.update();
    	game.batch.setProjectionMatrix(camera.combined);
    	game.batch.begin();
    	game.batch.draw(background, 0, 0);
    	for(int i = 0; i < wallTextures.length; i++) {
    		if(wallBooleans[i]) {
    			game.batch.draw(wallTextures[i], 0, 0);
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
    		for(int i = 0; i < interactions.length; i++) {
    			Rectangle col = interactions[i];
    			if(col != null) {
    				game.batch.draw(game.intBox, col.x, col.y, col.width, col.height);
    			}
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
    	
    	}
    	
    	if(resp != -1) {
    		System.out.println(resp);
    	}
    	
    	
    	if(showMap) {
    		
    		showMap();
    	}
    	
    	game.batch.end();
    }
    
    private void showMap() {
    	game.batch.draw(game.mapBackground, 0, 0);
    	int roomSize = 8;
        int spacing = 2;

        int mapWidth = floor.layout.length * (roomSize + spacing);
        int mapHeight = floor.layout[0].length * (roomSize + spacing);

        int startX = 320 - mapWidth - 5;
        int startY = 180 - mapHeight - 5;

        for(int i = 0; i < floor.layout.length; i++) {

            for(int j = 0; j < floor.layout[0].length; j++) {

                if(floor.layout[i][j] != null) {

                    int roomX = startX + j * (roomSize + spacing);
                    int roomY = startY + (floor.layout[0].length - 1 - i) * (roomSize + spacing);

                    if(i == currPosition[0] && j == currPosition[1]) {

                        // habitación actual
                        game.batch.draw(game.roomCurr, roomX, roomY, roomSize, roomSize);

                    } else if(floor.layout[i][j].discovered == Room.DISC_TOTAL) {
                        // habitación descubierta
                        game.batch.draw(game.roomDisc, roomX, roomY, roomSize, roomSize);

                    } else if (floor.layout[i][j].discovered == Room.DISC_PART){
                    	// habitación descubierta a la que no se entro
                    	game.batch.draw(game.roomUnd, roomX, roomY, roomSize, roomSize);
                    }
                }
            }
        }
	}
	private boolean checkInteraction(Rectangle player) {
		for(Rectangle col : interactions) {
			if(col != null && player.overlaps(col)) {
				return true;
				
			}
		}
		return false;
	}
	//para evitar colisiones
    private boolean checkCollision(Rectangle player) {

        for (Rectangle col : collisions) { //usamos este metodo de for para mayor comodidad

            if (col != null && player.overlaps(col)) {
            	return true;
            }

        }
        
        for (int i = 0; i < doorColliders.length; i++) {
        	Rectangle col = doorColliders[i];
            if (col != null && player.overlaps(col) && wallBooleans[i]) {
            	return true;
            }

        }

        return false;
    }
    
    private boolean checkDoors(Rectangle player) {

        for (Rectangle col : doors) { //usamos este metodo de for para mayor comodidad

            if (col != null && player.overlaps(col)) {
            	if(col == doors[WALL_LEFT]) {
            		currPosition[1]--;
            		x[0] = doors[WALL_RIGHT].x - 25;
            		y[0] = doors[WALL_RIGHT].y + 5;
            	}
            	if(col == doors[WALL_UP]) {
            		currPosition[0]--;
            		x[0] = doors[WALL_DOWN].x + 5;
            		y[0] = doors[WALL_DOWN].y + 25;
            	}
            	if(col == doors[WALL_DOWN]) {
            		currPosition[0]++;
            		x[0] = doors[WALL_UP].x + 5;
            		y[0] = doors[WALL_UP].y - 25;
            	}
            	if(col == doors[WALL_RIGHT]) {
            		currPosition[1]++;
            		x[0] = doors[WALL_LEFT].x + 25;
            		y[0] = doors[WALL_LEFT].y + 5;
            	}
            	floor.layout[currPosition[0]][currPosition[1]].discovered = Room.DISC_TOTAL;
            	posHistory = new Array<>(); // inicializa el ArrayList
        		for(int i = 0; i < (chars.length * followDelay) + 5; i++) {
        			//mientras que i sea menor a la length de chars * la cantidad de frames...
                    posHistory.add(new Vector2(x[0], y[0])); //añade un nuevo vector2 con las posiciones
                    										 // del primer personaje
                }
        		for(int i = 1; i < chars.length; i++) {
                    x[i] = x[0];	//pone las posiciones de cada personaje en 0
                    y[i] = y[0];	
                    chars[i].direction = chars[0].direction; //pone a todos en la misma direccion
                }
        		checkRoomBooleans();
            	return true;
            }

        }

        return false;
    }
    
    private void checkRoomBooleans() {
    	//arriba
		if(currPosition[0] - 1 < 0 || floor.layout[currPosition[0]-1][currPosition[1]] == null) {
			wallBooleans[WALL_UP] = true;
		}else {
			if(floor.layout[currPosition[0]-1][currPosition[1]].discovered == Room.DISC_NOT)
				floor.layout[currPosition[0]-1][currPosition[1]].discovered = Room.DISC_PART;
			wallBooleans[WALL_UP] = false;
		}
		//abajo
		if(currPosition[0] + 1 >= floor.layout.length || floor.layout[currPosition[0]+1][currPosition[1]] == null) {
			wallBooleans[WALL_DOWN] = true;
		}else {
			if(floor.layout[currPosition[0]+1][currPosition[1]].discovered == Room.DISC_NOT)
				floor.layout[currPosition[0]+1][currPosition[1]].discovered = Room.DISC_PART;
			wallBooleans[WALL_DOWN] = false;
		}
		//izquierda
		if(currPosition[1] - 1 < 0 || floor.layout[currPosition[0]][currPosition[1]-1] == null) {
			wallBooleans[WALL_LEFT] = true;
		}else {
			if(floor.layout[currPosition[0]][currPosition[1]-1].discovered == Room.DISC_NOT)
				floor.layout[currPosition[0]][currPosition[1]-1].discovered = Room.DISC_PART;
			wallBooleans[WALL_LEFT] = false;
		}
		//derecha
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