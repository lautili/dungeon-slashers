package io.github.dungeon_slashers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.dungeon_slashers.Main;
import io.github.dungeon_slashers.MenuScrollType;
import io.github.dungeon_slashers.controllers.DialMan;
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
    private final int STATE_FIRSTDIALOGUE = 0;
    private final int STATE_CHOOSECHARACTERS = 1;
    private final int STATE_LASTDIALOGUE = 2;
    private int screenState;
    private boolean check;
    private final int DIAL_FIRST = 100;
    private final int DIAL_LAST = 200;
    private OrthographicCamera camera;
    private FitViewport viewport;
    
    public CharSelectScreen(Main game) {
		this.game = game;
		screenState = STATE_FIRSTDIALOGUE;
		check = false;
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
		camera = new OrthographicCamera();
		viewport = game.viewport;
		viewport.setCamera(camera);
		camera.setToOrtho(false, 320, 180);
		camera.zoom = 1f;
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
    	ScreenUtils.clear(0, 0, 0, 1); //limpia el buffer de colores
    	
    	camera.update();
    	game.batch.setProjectionMatrix(camera.combined);
    	game.batch.begin();
    	switch(screenState) {
    	case STATE_FIRSTDIALOGUE:
    		if(!check) {
    			Hero robert = searchCharacter("warrior");
    			DialMan.addDialogue(0, 1, "???", null, "...", 20, null);
    			DialMan.addDialogue(1, 2, "???", null, "Hola..?", 20, null);
    			DialMan.addDialogue(2, 3, "???", null, "Dante...", 20, null);
    			DialMan.addDialogue(3, 4, "???", null, "Dante!", 20, null);
    			DialMan.addDialogue(4, 5, "???", null, "Dante!!", 20, null);
    			DialMan.addDialogue(5, 6, "Dante", null, "Ugh...", 20, null);
    			DialMan.addDialogue(6, 7, "???", null, "Dante, señor.", 20, null);
    			DialMan.addDialogue(7, 8, "???", null, "Soy yo, Robert.", 20, null);
    			DialMan.addDialogue(8, 9, "Dante", null, "Que... que paso?", 20, null);
    			DialMan.addDialogue(9, 10, "Robert", robert.getPortrait(), 
    					"Estabamos en camino a la mazmorra del contrato...", 20, null);
    			DialMan.addDialogue(10, 11, "Robert", robert.getPortrait(), 
    					"Cuando una piedra cayo sobre usted y lo noqueo.", 20, null);
    			DialMan.addDialogue(11, 12, "Dante", null, "El resto... estan bien?", 20, null);
    			DialMan.addDialogue(12, 13, "Robert", robert.getPortrait(), 
    					"Si, pero lamentablemente encontramos un problema al acceder" + 
    			"a la mazmorra...", 20, null);
    			DialMan.addDialogue(13, 14, "Robert", robert.getPortrait(), 
    					"Solo 4 de nosotros, sin incluirlo a usted, podran ingresar.", 20, null);
    			DialMan.addDialogue(14, 15, "Robert", robert.getPortrait(), 
    					"La puerta se cierra cuando una quinta persona entra.", 20, null);
    			DialMan.addDialogue(15, 16, "Dante", null, "Asi que... uno quedara afuera, verdad?", 20, null);
    			DialMan.addDialogue(16, DIAL_FIRST, "Robert", robert.getPortrait(), 
    					"Lamentablemente es el caso.", 20, null);
    			DialMan.addDialogue(DIAL_FIRST, -1, "Dante", null, "Bueno, entonces... veamos...", 20, null);
    			check = true;
    		}
    		break;
    	case STATE_CHOOSECHARACTERS:
	    	sel = InputMan.scrollInt(MenuScrollType.HORIZONTAL, 5, sel, Menu.MenuMove);
	    	game.mainFont.getData().setScale(0.3f);
	    	game.mainFont.draw(game.batch, "SELECCIONE LOS PERSONAJES:", 15, 165);
	    	if(countChars() == 4) {
	    		game.mainFont.getData().setScale(0.2f);
	    		game.mainFont.draw(game.batch, "4 Personajes seleccionados, presione X para continuar.", 5, 155);
	    	}
	    	
	    	Menu.showOptionsSelScreen(game, game.mainFont, 0.2f, 10, 20, 60, null, sel, selCharacters, characters);
	    	showCharInfo(characters[sel], sel);
	    	if(InputMan.checkKey(Main.config.key_interact)) {
	    		if(selChars[sel]) {
	    			selChars[sel] = false;
	    			removeCharacter(characters[sel]);
	    			
	    		}else {
	    			if(countChars() < 4) {
	    				selChars[sel] = true;
	    				addCharacter(characters[sel]);
	    			}
	    		}
	    	}
	    	if(InputMan.checkKey(Main.config.key_back)) {
	    		if(countChars() == 4) {
	    			check = false;
	    			screenState = STATE_LASTDIALOGUE;
	    			Main.player.setCharacters(selCharacters);
	    		}
	    	}
    	case STATE_LASTDIALOGUE:
    		if(!check) {
    			Hero notChosen = searchUnchosenCharacter();
    			DialMan.addDialogue(0, 1, notChosen.getName(), notChosen.getPortrait(), 
    					"Bueno...", 20, null);
    			DialMan.addDialogue(1, 2, notChosen.getName(), notChosen.getPortrait(), 
    					"Esta bien, puedo vivir con ello.", 20, null);
    			DialMan.addDialogue(2, 3, selCharacters[0].getName(), selCharacters[0].getPortrait(), 
    					"Soy lider! No se preocupen, sera facil.", 20, null);
    			DialMan.addDialogue(3, 4, selCharacters[1].getName(), selCharacters[1].getPortrait(), 
    					"Con la habilidad para rebobinar de Dante, saldremos ilesos de esto.", 20, null);
    			DialMan.addDialogue(4, 5, selCharacters[2].getName(), selCharacters[2].getPortrait(), 
    					"Espero que sepas lo que haces, " + selCharacters[1].getName(), 20, null);
    			DialMan.addDialogue(5, DIAL_LAST, selCharacters[3].getName(), selCharacters[3].getPortrait(), 
    					"Entramos, Dante?", 20, null);
    			DialMan.addDialogue(DIAL_LAST, -1, "Dante", null, "Vamos.", 20, null);

    			check = true;
    		}
    		break;
    	}
    	int resp = DialMan.showDialogues(game, delta);
       	switch(resp) {
       	case DIAL_FIRST:
       		screenState = STATE_CHOOSECHARACTERS;
       		break;
        case DIAL_LAST:
				game.setScreen(game.firstScreen);
				dispose();
        		break;
    	}
       	game.batch.end();
    }

    private void showCharInfo(Hero hero, int sel) {
		int x;
		int x2;
		if(sel > characters.length / 2 - 1) {
			x = 10;
			x2 = 170;
		}else {
			x = 170;
			x2 = 10;
		}
		game.mainFont.getData().setScale(0.3f);
		game.mainFont.draw(game.batch, hero.getName(), x, 140);
		String desc = "";
		switch(hero.getIDName()) {
		case "warrior":
			desc = "El guerrero del grupo. Tiene una gran cantidad de vida y los ataques con su espada "
					+ "pueden resultar letales contra casi cualquier enemigo, pero falla en el ambito "
					+ "magico o si un enemigo es fuerte contra ataques fisicos. A pesar de moverse lento, "
					+ "si su ataque te alcanza, date por muerto. \n"
					+ "Por suerte, nunca tuve problemas con el por mi cuenta, y es bastante amable de hecho! "
					+ "Pero, si he tenido que rebobinar varias veces mi reloj para revivir a Noelle cuando "
					+ "estos dos tuvieron alguna pelea en el trayecto a aqui...";
			break;
		case "mage":
			desc = "Que grupo de mercenarios existe sin un buen mago a su lado! maga*, no le gusta que "
					+ "le digan mago. Su habilidad magica es envidiable... no conozco a nadie que sea capaz "
					+ "de manejar las 4 escuelas de magia con la habilidad que lo hace ella. "
					+ "A pesar de no aparentarlo bajo esa bata y esa cara de pocos amigos, es una chica "
					+ "muy amigable! Siempre y cuando no te dediques a la necromancia... Odia a esos tipos. "
					+ "Muchas veces intente indagar en su pasado, pero cada vez que parecia que iba a encontrar "
					+ "algo, mi memoria se vuelve borrosa... raro.";
			break;
		case "sage":
			desc = "Y como olvidarme del curandero... Antes de esta nueva habilidad que adquiri, cualquier "
					+ "problema de salud caia en manos de el. No habla mucho... creo que solo esta con nosotros "
					+ "por el dinero, pero no soy quien para juzgarlo. Las veces que he intimado con el en privado "
					+ "me demostro ser una persona bastante fuerte, sabia y empatica. Se preocupa por nosotros... "
					+ "aunque su objetivo sea el dinero.";
			break;
		case "thief":
			desc = "Es escurridiza... la encontramos cuando me robo un artefacto investigando un asesinato "
					+ "en las afueras de la Capital y si no fuese por un flechazo gelido de Ocelot "
					+ "se habria escapado con el. Pensamos en llevarla a las autoridades, pero parecia bastante "
					+ "arrepentida de lo que hacia... y solo lo hacia para comer. Decidi, en contra de la "
					+ "voluntad de los muchachos, contratarla. Hice bien, fue una gran adicion al equipo y "
					+ "demostro que su agilidad y perspicacia podian ser un buen recurso... y ahora, aunque sea, "
					+ "puede comer todos los dias.";
			break;
		case "explorer":
			desc = "El unico limite de este tipo para abatir a enemigos a distancia es el tiempo que tarda "
					+ "la flecha en caer y la cantidad maxima de distancia que recorre. Si existiesen armas "
					+ "que lanzasen proyectiles con una velocidad mas alta... este tipo podria acertarle a alguien "
					+ "en la otra punta del mundo.\n"
					+ "Nos lo encontramos porque... mato a un objetivo que estabamos protegiendo. Sera buen tirador,"
					+ " pero no destaca mucho en el sigilo...\n"
					+ "Luego de pagar una suma para dejarlo libre, se unio a nosotros. No se de donde proviene su "
					+ "nombre... pero me genera bastante intriga.";
			break;
		}
		game.mainFont.getData().setScale(0.15f);
		game.mainFont.draw(game.batch, desc, x, 130, 150, Align.center, true);
	}

	private Hero searchUnchosenCharacter() {
    	for(int i = 0; i < 5; i++) {
			if(!selChars[i]) return characters[i];
		}
		return null;
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
	private Hero searchCharacter(String IDname) {
		for(Hero hero : characters) {
			if(hero.getIDName().equals(IDname)) {
				return hero;
			}
		}
		return null;
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
    	game.lastScreen = this;
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    	game.lastScreen = this;
    }
}