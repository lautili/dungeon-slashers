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
import io.github.dungeon_slashers.controllers.DialMan;
import io.github.dungeon_slashers.controllers.InputMan;
import io.github.dungeon_slashers.controllers.Save;
import io.github.dungeon_slashers.entities.Hero;

/** Primera sala. */
public class FirstScreen implements Screen {
	Hero[] chars;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private float[] x;
    private float[] y;
    private Texture background;
    private Main game;
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
    private float timer;
    
	public FirstScreen(Main game) {
		this.game = game;
		x = new float[4];
		y = new float[4];
		chars = Main.player.getCharacters();
		background = new Texture("sprites/background/background_firstScreen.jpg");
		x[0] = 50;
		y[0] = 50;
		colboxes = false;
		playerCol = new Rectangle(x[0], y[0], 15, 15);
		collisions = new Rectangle[6];
		collisions[0] = new Rectangle(0, 148, 320, 32);
		collisions[1] = new Rectangle(0, 0, 320, 12);
		collisions[2] = new Rectangle(0, 0, 20, 47);
		collisions[3] = new Rectangle(0, 105, 20, 40);
		collisions[4] = new Rectangle(295, 0, 25, 148);
		interactions = new Rectangle[1];
		interactions[0] = new Rectangle(160 - 20 / 2, 75, 20, 20);
		collisions[5] = new Rectangle(interactions[0].getX(), interactions[0].getY(),
							interactions[0].getWidth(), interactions[0].getHeight());
		doors = new Rectangle[2];
		doors[0] = new Rectangle(0, 47, 5, 55);
		doors[1] = new Rectangle(148, 140, 25, 10);
	}
	@Override
    public void show() {
        // Prepare your screen here.
		this.resume();
		boolean temp = false;
		timer = 0;
		chars = Main.player.getCharacters();
		Main.player.state = PlayerState.WAITING;
		if(Main.player.currScreen == "SHOP_SCREEN") { 
			x[0] = 10;
			chars[0].direction = "right";
		}else if(Main.player.currScreen == "FLOOR_SCREEN") {
			x[0] = 150;
			y[0] = 120;
			chars[0].direction = "down";
		}else if(Main.player.currScreen == "LOOSE") {
			temp = true;
			x[0] = 130;
			y[0] = 65;
			chars[0].direction = "right";
			x[1] = 150;
			y[1] = 35;
			chars[1].direction = "up";
			x[2] = 180;
			y[2] = 35;
			chars[2].direction = "up";
			x[3] = 210;
			y[3] = 65;
			chars[3].direction = "left";
			for(int i = 0; i < chars.length; i++) {
    			chars[i].hp = chars[i].maxhp;
        		chars[i].sp = chars[i].maxsp;
        		chars[i].mp = chars[i].maxmp;
    		}
    		Save.save();
		}
		Main.player.currScreen = "FIRST_SCREEN";
		camera = new OrthographicCamera();
		viewport = game.viewport;
		viewport.setCamera(camera);
		camera.setToOrtho(false, 320, 180);
		camera.zoom = 1f;
		if(!Main.player.flags[Flags.FLAG_FIRSTSCREEN_DIALOGUE_START]) {
			DialMan.addDialogue(0, 1, chars[0].getName(), chars[0].getPortrait(), "Esta es una prueba de dialogos. asjdaajsd s d d adoalalala lalalalaallala lolololololololo", 20);
			DialMan.addDialogue(1, 2, chars[1].getName(), chars[1].getPortrait(), "Esta es una prueba de dialogos con otra foto. Hola", 60);
			DialMan.addChoice(2, 20, "A quien preferis", new String[] {chars[0].getName(), chars[1].getName(), "Los 2", "Ninguno"}, new int[] {3, 4, 5, 6});
			DialMan.addDialogue(3, -1, chars[0].getName(), chars[0].getPortrait(), "Gracias", 20);
			DialMan.addDialogue(4, -1, chars[1].getName(), chars[1].getPortrait(), "Gracias", 20);
			DialMan.addDialogue(5, -1, "Los 2", null, "Gracias", 20);
			DialMan.addDialogue(6, -1, null, null, "Te miran con cara de culo", 20);
			Main.player.flags[Flags.FLAG_FIRSTSCREEN_DIALOGUE_START] = true;
		}
		
		posHistory = new Array<>(); // inicializa el ArrayList
		for(int i = 0; i < (chars.length * followDelay) + 5; i++) {
			//mientras que i sea menor a la length de chars * la cantidad de frames...
            posHistory.add(new Vector2(x[0], y[0])); //añade un nuevo vector2 con las posiciones
            										 // del primer personaje
        }
		for(int i = 1; i < chars.length; i++) {
			if(!temp) {
	            x[i] = x[0];	//pone las posiciones de cada personaje en 0
	            y[i] = y[0];	
	            chars[i].direction = chars[0].direction; //pone a todos en la misma direccion
			}
        }
	}

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
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
    	if(InputMan.checkKey("F1")) {
    		if(!colboxes) {
    			colboxes = true;
    		}else {
    			colboxes = false;
    		}
    	}
    	if(InputMan.checkKey("F2")) {
			System.out.println("yendo a battlescreen");
			game.setScreen(game.defBattleScreen);
			game.defBattleScreen.lastScreen = this;
			this.pause();
		}
    	if(InputMan.checkKey("F3")) {
			System.out.println("yendo a battlescreen");
			game.setScreen(game.firstBossFight);
			game.firstBossFight.lastScreen = this;
			this.pause();
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
    	checkDoors(playerCol);
    	
    	ScreenUtils.clear(0, 0, 0, 1); //limpia el buffer de colores
    	camera.update();
    	game.batch.setProjectionMatrix(camera.combined);
    	game.batch.begin();
    	game.batch.draw(background, 0, 0);
    	game.batch.draw(game.workedFireplace, interactions[0].getX(), interactions[0].getY(),
				interactions[0].getWidth(), interactions[0].getHeight());
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
    	}
    	int resp = DialMan.showDialogues(game, delta);
    	switch(resp) {
    	case 30:
    		for(int i = 0; i < chars.length; i++) {
    			chars[i].hp = chars[i].maxhp;
        		chars[i].sp = chars[i].maxsp;
        		chars[i].mp = chars[i].maxmp;
    		}
    		Save.save();
    		break;
    	}
    	game.batch.end();
    }
    
    private boolean checkInteraction(Rectangle player) {
		for(Rectangle col : interactions) {
			System.out.println("chequeando interacciones...");
			if(col != null && player.overlaps(col)) {
				System.out.println("se encontró interaccion");
				if(col == interactions[0]) {
                	DialMan.addDialogue(0, 1, null, null, "La fogata. Esta apagada.", 20);
                	DialMan.addChoice(1, 20, "Deseas guardar y recuperar salud?", new String[] {"Si", "No"},
                			new int[] {30, 4});
                	DialMan.addDialogue(30, -1, null, null, "Guardado.", 20);
                	DialMan.addDialogue(4, -1, null, null, "No se ha guardado.", 20);
                }
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

        return false;
    }
    
    private boolean checkDoors(Rectangle player) {

        for (Rectangle col : doors) { //usamos este metodo de for para mayor comodidad

            if (col != null && player.overlaps(col)) {
            	if(col == doors[0]) {
            		game.setScreen(game.storeScreen);
            		this.pause();
            	}
            	if(col == doors[1]) {
            		game.setScreen(game.firstFloorScreen);
            		this.pause();
            	}
            	return true;
            }

        }

        return false;
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
    	game.lastScreen = this;
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    	game.lastScreen = this;
    }
}