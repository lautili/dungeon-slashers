package io.github.dungeon_slashers.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.dungeon_slashers.Main;
import io.github.dungeon_slashers.Skill;
import io.github.dungeon_slashers.entities.Enemy;
import io.github.dungeon_slashers.entities.Entity;
import io.github.dungeon_slashers.entities.Hero;
import io.github.dungeon_slashers.item.Armor;
import io.github.dungeon_slashers.item.Item;
import io.github.dungeon_slashers.item.Weapon;

/*
 * 		CLASE MENU
 * 	Esta clase va a ser algo peculiar. primero que nada, no va a tener constructores publicos.
 * es decir, la unica forma de construirla va a ser en la propia clase. todos los metodos seran estaticos. es decir,
 * la forma de acceder a ellos no es por un objeto en si, si no a la clase. es decir, Menu.getX(), por ejemplo.
 * esto permite que los metodos de la clase se utilicen en cualquier clase, en cualquier momento, sin tener que pasar un objeto de clase Menu
 * como parametro.
 */

public class Menu {
	public static boolean menu;
	public static FitViewport viewport;
private static void charSelection(Hero warrior, Hero mage, Hero sage, Hero explorer, Hero thief) {
	//Array de Strings con los nombres de las clases
			String[] charSel = {"Warrior", "Mage", "Explorer", "Thief", "Sage"};
			int heroesSeleccionados = 0;
			
	// Ciclo para elegir a los personajes
	while (heroesSeleccionados < 4) {

		// Ingresa la seleccion con el largo de la cantidad de personajes -1
		int seleccion = InputMan.scanInt(1, charSel.length);
		seleccion--;
		
		//Si la seleccion esta vacia (Ya fue elegido), 
		//	lo avisa y repite el ciclo sin hacer lo que hay por delante
		if (charSel[seleccion].equals("")) {
			Menu.msg("Ese personaje ya fue elegido o no esta disponible. Escoja uno de la lista.");
			continue;
		}
		
		// crea la funcion para asignar a un heroe
		Hero heroeAsignar = null;
		
		//hace un switch a la selecci�n para saber a quien eligi�, y guarda ese personaje en heroeAsignar
		switch (charSel[seleccion]) {
			case "Warrior": 
				heroeAsignar = warrior; 
				break;
			case "Mage":     
				heroeAsignar = mage; 
				break;
			case "Explorer": 
				heroeAsignar = explorer; 
				break;
			case "Thief":    
				heroeAsignar = thief; 
				break;
			case "Sage":     
				heroeAsignar = sage; 
				break;
		}
		
		//Muestra todos los datos del heroe
		Menu.msg("\nEsta seguro que desea elegir a este personaje?\n	0-No\n	1-Si");
		int opt = InputMan.scanInt(0, 1);
		if (opt == 1) {
			//Si elige al personaje, lo guarda en la proxima ubicacion de la lista, 
			//	avanza a la proxima posicion de la lista (heroesSeleccionados) y borra al
			//	personaje del array de Strings de las clases
			Main.player.getCharacters()[heroesSeleccionados] = heroeAsignar;
			Menu.msg(heroeAsignar.getName() + " a�adido al equipo!");
			heroesSeleccionados++;
			charSel[seleccion] = "";
		}else {
			//Si cambia de opinion, repite el ciclo
			continue;
		}
	}
	Menu.msg("\n�Equipo completado con exito!");
	
}

//muestra las estadisticas de las entidades en una batalla
public static void battleStats(Hero[] heroes, Enemy[] enemies) {
	showEntityBStats(heroes);
	System.out.println("\n--------------------------------------------------------------------------------------------------------------------------------------------------\n");
	showEntityBStats(enemies);
}

// metodo generalizado para mostrar una estadistica como una barra, como una barra de vida
private static String statBar(int maxStat, int stat) {
	String msg = "[";
	for(int i=0; i < 10; i++) {
		if(stat >= maxStat * (0.1 * i) && stat != 0) {
			msg += "#";
		}else {
			msg += "-";
		}
	}
	msg += "] (" + stat + "/" + maxStat + ")";
	return msg;
}

//mostrar las estadisticas de un heroe
public static void showHeroStats(Main game, Hero entity) {
	
	String showHP = statBar(entity.maxhp, entity.hp);
	String showMP = statBar(entity.maxmp, entity.mp);
	String showSP = statBar(entity.maxsp, entity.sp);
	String showATK = (entity.atk < entity.atkF) ? (" (+"+ (entity.atkF-entity.atk) +")")
			:(entity.atk > entity.atkF) ? (" (-"+ (entity.atk-entity.atkF) +")")
			: " (0)";
	String showMAT = (entity.mat < entity.matF) ? (" (+"+ (entity.matF-entity.mat) +")")
			:(entity.mat > entity.matF) ? (" (-"+ (entity.mat-entity.matF) +")")
			: " (0)";
	String showDEF = (entity.def < entity.defF) ? (" (+"+ (entity.defF-entity.def) +")")
			:(entity.def > entity.defF) ? (" (-"+ (entity.def-entity.defF) +")")
			: " (0)";
	String showMDF = (entity.mdf < entity.mdfF) ? (" (+"+ (entity.mdfF-entity.mdf) +")")
			:(entity.mdf > entity.mdfF) ? (" (-"+ (entity.mdf-entity.mdfF) +")")
			: " (0)";
	String showSPD = (entity.spd < entity.spdF) ? (" (+"+ (entity.spdF-entity.spd) +")")
			:(entity.spd > entity.spdF) ? (" (-"+ (entity.spd-entity.spdF) +")")
			: " (0)";
	
	game.batch.draw(entity.getPortrait(), 80, -50, 100, 100);
	game.invFont.getData().setScale(0.8f);
	game.invFont.draw(game.batch, entity.getName(), 190, 58);
	game.invFont.getData().setScale(0.4f);
	game.invFont.draw(game.batch,"Clase " + entity.getclassName(), 345, 52);
	game.invFont.getData().setScale(0.3f);
	game.mainFont.getData().setScale(0.3f);
	game.mainFont.draw(game.batch, "HP " + showHP, 50, -52);
	game.mainFont.draw(game.batch, "MP " + showMP, 50, -63);
	game.mainFont.draw(game.batch, "SP " + showSP, 50, -74);
	game.mainFont.getData().setScale(0.3f);
	game.mainFont.draw(game.batch, "ATK " + entity.atkF + showATK, 330, 30);
	game.mainFont.draw(game.batch, "MAT " + entity.matF + showMAT, 405, 30);
	game.mainFont.draw(game.batch, "DEF " + entity.atkF + showDEF, 330, 0);
	game.mainFont.draw(game.batch, "MDF " + entity.matF + showMDF, 405, 0);
	game.mainFont.draw(game.batch, "SPD " + entity.matF + showSPD, 363, -30);
	game.invFont.draw(game.batch, entity.getXPleft() + " XP para el proximo nivel", 280, -62);
	game.invFont.getData().setScale(0.35f);
	game.invFont.draw(game.batch, "Arma: \n" + entity.getWeapon().getName(), 190, 30, 120, Align.center, true);
	game.invFont.draw(game.batch, "Armadura: \n" + entity.getArmor().getName(), 190, -20, 120, Align.center, true);
}
//mostrar stats de un item
public static void showItemStats(Main game, Item i) {
	GlyphLayout layout = new GlyphLayout();
	game.invFont.getData().setScale(0.5f);
	game.invFont.draw(game.batch, i.getName(), 140, 222);
	if(i.getClass() == Weapon.class) {
		game.invFont.getData().setScale(0.4f);
		Weapon w = (Weapon) i;
		String msg = "";
		game.invFont.draw(game.batch, "ATK " + w.getATK(), 350, 200);
		game.invFont.draw(game.batch, "MAT " + w.getMAT(), 420, 200);
		game.invFont.draw(game.batch, "SPD " + w.getSPD(), 390, 180);
		game.invFont.getData().setScale(0.3f);
		for(int j = 0; j < w.getclassName().length; j++) {
			
			msg += (w.getclassName()[j]);
			if(j== (w.getclassName().length - 2)) {
				msg += (" y ");
			}else if(j == (w.getclassName().length - 1)){
				msg+=("");
			}else {
				msg+=(", ");
			}
		}
		game.invFont.draw(game.batch, msg, 350, 220, 60, Align.center, true);
	}else if(i.getClass() == Armor.class){
		game.invFont.getData().setScale(0.4f);
		Armor a = (Armor) i;
		game.invFont.draw(game.batch, "ATK " + a.getATK(), 350, 200);
		game.invFont.draw(game.batch, "MAT " + a.getMAT(), 420, 200);
		game.invFont.draw(game.batch, "DEF " + a.getDEF(), 350, 180);
		game.invFont.draw(game.batch, "MDF " + a.getMDF(), 420, 180);
		game.invFont.draw(game.batch, "SPD " + a.getSPD(), 390, 160);
	}
	game.invFont.getData().setScale(0.5f);
	game.invFont.draw(game.batch, i.getCost() + "G", 440, 224);
	game.invFont.getData().setScale(0.35f);
	layout.setText(
		    game.invFont,
		    i.getDesc(),
		    Color.WHITE,
		    200,      // ancho máximo de la caja
		    Align.left,
		    true      // wrap
		);
	game.invFont.draw(game.batch, layout, 140, 200);
}

//mostrar las estadisticas de los personajes en batalla
	private static void showEntityBStats(Entity[] entities) {
		// Nombres
		for (int i = 0; i < entities.length; i++) {
			String temp = (entities[i].getClass() == Hero.class) ? " (" + entities[i].getclassName() + ")" : "";
		    System.out.printf("| %-30s ", entities[i].getName() + temp);
		}
		System.out.println("|");

		// HP
		for (int i = 0; i < entities.length; i++) {
		    System.out.printf("| %-30s ", "HP: " + statBar(entities[i].maxhp, entities[i].hp));
		}
		System.out.println("|");

		if(entities[0].getClass() == Hero.class) {
			// MP
			for (int i = 0; i < entities.length; i++) {
			    System.out.printf("| %-30s ", "MP: " + statBar(entities[i].maxmp, entities[i].mp));
			}
			System.out.println("|");
	
			// SP
			for (int i = 0; i < entities.length; i++) {
			    System.out.printf("| %-30s ", "SP: " + statBar(entities[i].maxsp, entities[i].sp));
			}
			System.out.println("|");
		}
		//efectos de estado
		for (int i = 0; i < entities.length; i++) {
			String msg = "";
			for(int j = 0; j < entities[i].getEffects().length; j++) {
				msg += (entities[i].getEffects()[j] != null) ? entities[i].getEffects()[j].getShortName() : "";
				msg += " ";
			}
			System.out.printf("| %-30s ", msg);
		}
		System.out.println("|");
	}
	
	//Mostrar stats de skill
	public static void showSkillStats(Main game, Skill s) {
		int x1 = 430;
		GlyphLayout layout = new GlyphLayout();
		game.invFont.getData().setScale(0.5f);
		game.invFont.draw(game.batch, s.getName(), 140, 222);
		if(s.getType() != null) {
			String msg = null;
			switch (s.getType()) {
			case "FIR":
				msg = "FUEGO";
				break;
			case "WAT":
				msg = "AGUA";
				break;
			case "WIN":
				msg = "VIENTO";
				break;
			case "EAR":
				msg = "TIERRA";
				break;
			case "UNI":
				msg = "UNIVERSAL";
				break;
			case "PHY":
				msg = "FISICO";
				break;
			case "RAN":
				msg = "RANGO";
				break;
			case "NONE":
				msg = "NONE";
				break;
			}
			game.invFont.getData().setScale(0.6f);
			game.invFont.draw(game.batch, msg, 380, 200);
		}
		game.invFont.getData().setScale(0.4f);
		if(s.getMP() > 0) {
			game.invFont.draw(game.batch, s.getMP() + " MP", x1, 222);
			x1 -= 60;
		}
		if(s.getSP() > 0) {			
			game.invFont.draw(game.batch, s.getSP() + " SP", x1, 222);
		}
		game.invFont.getData().setScale(0.35f);
		layout.setText(
			    game.invFont,
			    s.getDesc(),
			    Color.WHITE,
			    200,      // ancho máximo de la caja
			    Align.left,
			    true      // wrap
			);
		game.invFont.draw(game.batch, layout, 140, 200);
	}
	
	//metodos varios del menu
	public static void showOptionsY(Main game, BitmapFont font, float fontSize, float x, float y, float dif, String msg, int sel, String... options) {
		font.getData().setScale(fontSize);
		if(msg != null) {
			font.draw(game.batch, msg, x, y);
			y -= dif;
		}
		
		for(int i = 0; i < options.length; i++) {
			if(!options[i].equals("") && options[i] != null) {
				String msg1;
				if(i == sel) {
					font.draw(game.batch, ">", x-10, y);
				}
				msg1 = options[i];
				font.draw(game.batch, msg1, x, y);
				y -= dif;
			}
		}
	}
	public static void showOptionsX(Main game, BitmapFont font, float fontSize, float x, float y, float dif, String msg, int sel, String... options) {
		font.getData().setScale(fontSize);
		if(msg != null) {
			font.draw(game.batch, msg, x, y);
			y -= dif;
		}
		
		for(int i = 0; i < options.length; i++) {
			if(!options[i].equals("") && options[i] != null) {
				String msg1;
				if(i == sel) {
					font.draw(game.batch, ">", x-10, y);
				}
				msg1 = options[i];
				font.draw(game.batch, msg1, x, y);
				x += dif;
			}
		}
	}
	
	public static void showItems(Main game, float x, float y, float dif, int pos, Item[] items) {
		for(int i = 0; i < items.length; i++) {
			String msg;
			game.invFont.getData().setScale(0.3f);
			if(i == pos) {
				game.mainFont.getData().setScale(0.3f);
				game.mainFont.draw(game.batch, ">", x-10, y);
			}
			msg = items[i].getName();
			game.invFont.draw(game.batch, msg, x, y);
			msg = (items[i].q > 1) ? "x" + items[i].q : "";
			game.invFont.draw(game.batch, msg, x + 220, y);
			y -= dif;
		}
	}
	
	public static void showBattleOptions(String msg, Hero[] heroes, Hero hero) {
		System.out.println(msg);
		System.out.println("1. Atacar");
		System.out.println("2. Defender");
		System.out.println("3. Skills");
		System.out.println("4. Inventario");
		if(hero != heroes[0]) {
			System.out.println("5. Atras"); //Si no es el primer heroe, permite volver atras
		}
		System.out.println("\n");
	}
	
	public static void showObjOptions(String msg, boolean hasExit, Entity[] entities) {
		System.out.println(msg);
		if(hasExit) {
			System.out.println("0. Salir");
		}
		for(int i = 0; i < entities.length; i++) {
			if(entities[i] != null) {
				System.out.println((i+1) + ". " + entities[i].getName());
			}
		}
	}
	public static void showObjBatOptions(String msg, boolean hasExit, Entity[] e) {
		System.out.println(msg);
		if(hasExit) {
			System.out.println("0. Salir");
		}
		for(int i = 0; i < e.length; i++) {
			if(e[i].hp > 0) {
				System.out.println(i+1 + ". " + e[i].getName()); //Solo muestra los que no estan caidos
			}
		}
	}
	
	public static void showItemBatOptions(String msg, boolean hasExit, Item[] inv) {
		System.out.println(msg);
		if(hasExit) {
			System.out.println("0. Salir");
		}
		for(int i = 0; i < inv.length; i++) {
			System.out.println(i+1 + ". " + inv[i].getName());
		}
	}
	public static void showItemOptions(String msg, Item[] inventory) {
		System.out.println(msg);
		System.out.println("0. Salir");
		for(int i = 0; i < inventory.length; i++) {
			System.out.println(i+1 + ". " + inventory[i].getName());
		}
	}
	
	public static void showWeaponOptions(String msg, Weapon[] weapons) {
		System.out.println(msg);
		System.out.println("0. Salir");
		for(int i = 0; i < weapons.length; i++) {
			System.out.println(i+1 + ". " + weapons[i].getName());
		}
	}
	public static void showArmorOptions(String msg, Armor[] armors) {
		System.out.println(msg);
		System.out.println("0. Salir");
		for(int i = 0; i < armors.length; i++) {
			System.out.println(i+1 + ". " + armors[i].getName());
		}
	}
	
	public static void msg(String msg) {
		System.out.println(msg);
	}

	public static void atkMsg(String msg) {
		System.out.println(msg);
	}

	//para dialogos
	public static void showDialogue(Main game, Dialogue dialogue) {
		if(dialogue.getName() != null) {
			game.dialFont.getData().setScale(0.5f);
			game.batch.draw(new Texture("ui/box-name.png"), 00, 59);
			game.dialFont.draw(game.batch, dialogue.getName(), 0, 79);
			
		}
		game.dialFont.getData().setScale(0.4f);
	    GlyphLayout layout = new GlyphLayout();
	    layout.setText(
			    game.dialFont,
			    dialogue.currMsg,
			    Color.WHITE,
			    300,      // ancho máximo de la caja
			    Align.left,
			    true      // wrap
			);
	    game.batch.draw(new Texture("ui/box-dialogue.png"), 0, 00);
	    game.dialFont.draw(game.batch, layout, 5, 58);
	    
	    if(dialogue.portrait != null) {
	    	game.batch.draw(new Texture("ui/box-portrait.png"), 250, 59);
	    	game.batch.draw(dialogue.portrait, 251, 60, 68, 68);
	    }
	}
	public static void showBattleDialogue(Main game, Dialogue dialogue) {
		if(dialogue.getName() != null) {
			game.dialFont.draw(game.batch, dialogue.getName(), 10, 80);
			
		}
	    GlyphLayout layout = new GlyphLayout();
	    layout.setText(
			    game.invFont,
			    dialogue.currMsg,
			    Color.WHITE,
			    300,      // ancho máximo de la caja
			    Align.left,
			    true      // wrap
			);
	    game.batch.draw(new Texture("ui/box-dialogue.png"), 0, 00);
	    game.dialFont.draw(game.batch, layout, 10, 54);
	}
	public static void showChoice(Main game, Choice choice) {
		game.dialFont.getData().setScale(0.4f);
	    GlyphLayout layout = new GlyphLayout();
	    layout.setText(
			    game.dialFont,
			    choice.currMsg,
			    Color.WHITE,
			    300,      // ancho máximo de la caja
			    Align.left,
			    true      // wrap
			);
	    game.batch.draw(new Texture("ui/box-dialogue.png"), 0, 00);
	    game.dialFont.draw(game.batch, layout, 5, 58);
	}
	public static void showChoices(Main game, Choice choice) {
		int j = 0;
		for(int i = (choice.getChoices().length - 1); i >= 0; i--) {
			game.batch.draw(new Texture("ui/box-choice.png"), 220, 59+(j*15));
			game.dialFont.draw(game.batch, choice.getChoices()[i], 232, 74+(j*15));
			if(i == choice.currChoice) {
				game.dialFont.draw(game.batch, ">", 222, 74+(j*15));
			}
			j++;
		}
	}

	public static void showItems(Item[] items) {
		for(int i = 0; i < items.length; i++) {
			String msg = (items[i].getClass() == Item.class) ? "Item" : (items[i].getClass() == Weapon.class) ? "Arma" : "Armadura";
			System.out.printf("| %-30s [" + items[i].getCost() + "G]|\n", items[i].getName() + " (" + msg + ")");
		}
	}

	public static void showPlayItems(Item[] items) {
		for(int i = 0; i < items.length; i++) {
			String temp = "";
			if(!items[i].unique) {
				temp = "x" + items[i].q;
			}
			String msg = (items[i].getClass() == Item.class) ? "Item" : (items[i].getClass() == Weapon.class) ? "Arma" : "Armadura";
			System.out.printf("| %-30s [" + items[i].getCost() + "G]|\n", items[i].getName() + " (" + msg + ") " + temp);
		}
	}

	public static void showItemStatsShop(Item i) {
		System.out.println("----|NOMBRE: "+ i.getName() +"|----\n"
				+ "DESCRIPCION: "+ i.getDesc() +"\n"
				+ "COSTO: "+ i.getCost() +"G\n");
	}

	public static void showSkills(Main game, float x, float y, float dif, int pos, Skill[] skills) {
		game.invFont.getData().setScale(0.3f);
		for(int i = 0; i < skills.length; i++) {
			int x1 = 220;
			if(i == pos) {
				game.mainFont.getData().setScale(0.3f);
				game.mainFont.draw(game.batch, ">", x-10, y);
			}
			System.out.println(skills[i]);
			if(!skills[i].getMenu()) {
				game.invFont.setColor(0.5f, 0.5f, 0.5f, 1);
			}
			game.invFont.draw(game.batch, skills[i].getName(), x, y);
			if(skills[i].getMP() > 0) {
				game.invFont.setColor(0.3f, 0.3f, 1f, 1);
				game.invFont.draw(game.batch, Integer.toString(skills[i].getMP()), x + x1, y);
				x1 -= 20;
			}
			if(skills[i].getSP() > 0) {
				game.invFont.setColor(0.3f, 1f, 0.3f, 1);
				game.invFont.draw(game.batch, Integer.toString(skills[i].getSP()), x + x1, y);
			}
			game.invFont.setColor(1, 1, 1, 1);
			y -= dif;
		}
	}

}
