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
	game.mainFont.draw(game.batch, "HP ", 50, -52);
	game.mainFont.draw(game.batch, "MP ", 50, -63);
	game.mainFont.draw(game.batch, "SP ", 50, -74);
	float mult = 2f;
	game.batch.draw(game.HPbar, 75, -62, (90f * entity.hp / entity.getHP()) * mult, 5 * mult);
	game.batch.draw(game.battleBar, 75, -62, 90  * mult, 5  * mult);
	game.batch.draw(game.MPbar, 75, -73, (90f * entity.mp / entity.getMP()) * mult, 5 * mult);
	game.batch.draw(game.battleBar, 75, -73, 90  * mult, 5  * mult);
	game.batch.draw(game.SPbar, 75, -84, (90f * entity.sp / entity.getSP()) * mult, 5 * mult);
	game.batch.draw(game.battleBar, 75, -84, 90  * mult, 5  * mult);
	game.mainFont.getData().setScale(0.3f);
	game.mainFont.draw(game.batch, "ATK " + entity.atkF + showATK, 320, 30);
	game.mainFont.draw(game.batch, "MAT " + entity.matF + showMAT, 405, 30);
	game.mainFont.draw(game.batch, "DEF " + entity.atkF + showDEF, 320, 0);
	game.mainFont.draw(game.batch, "MDF " + entity.matF + showMDF, 405, 0);
	game.mainFont.draw(game.batch, "SPD " + entity.matF + showSPD, 353, -30);
	game.invFont.draw(game.batch, entity.getXPleft() + " XP para el proximo nivel", 280, -62);
	game.invFont.getData().setScale(0.35f);
	game.invFont.draw(game.batch, "Arma: \n" + entity.getWeapon().getName(), 190, 30, 120, Align.center, true);
	game.invFont.draw(game.batch, "Armadura: \n" + entity.getArmor().getName(), 190, -20, 120, Align.center, true);
}
//mostrar stats de un item
public static void showItemStats(Main game, Item i) {
	game.invFont.setColor(1, 1, 1, 1);
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
			if(items[i] != null) {
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
	}
	public static void showItems(Main game, int x, int y, float scale, int length, int dif, int sel, Item[] items) {
		if (items == null || items.length == 0 || length <= 0) return;
		
		sel = Math.max(0, Math.min(sel, items.length - 1));
		
		int startIndex = 0;
	    if (items.length > length) {
	        startIndex = Math.max(0, Math.min(sel - length / 2, items.length - length));
	    }
	    
		int currentY = y;
	    int visibleCount = Math.min(length, items.length);
		for (int i = 0; i < visibleCount; i++) {
	        int itemIndex = startIndex + i;
	        Item item = items[itemIndex];

	        if (item != null) {
	        	
	            if (itemIndex == sel) {
	                game.mainFont.draw(game.batch, ">", x - 5, currentY);
	            }

	            game.invFont.draw(game.batch, item.getName(), x, currentY);

	            if (item.q > 1) {
	                String qtyMsg = "x" + item.q;
	                
	                game.invFont.draw(game.batch, qtyMsg, x + 150, currentY); 
	            }

	            currentY -= dif;
	        }
	    }
	}
	//para dialogos
	public static void showDialogue(Main game, Dialogue dialogue) {
		if(dialogue.getName() != null) {
			game.dialFont.getData().setScale(0.5f);
			game.batch.draw(game.nameBox, 00, 59);
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
	    game.batch.draw(game.dialogueBox, 0, 00);
	    game.dialFont.draw(game.batch, layout, 5, 58);
	    
	    if(dialogue.portrait != null) {
	    	game.batch.draw(game.portraitBox, 250, 59);
	    	game.batch.draw(dialogue.portrait, 251, 60, 68, 68);
	    }
	}
	public static void showBDialogue(Main game, Dialogue dialogue) {
		if(dialogue.getName() != null) {
			game.dialFont.getData().setScale(0.5f);
			game.batch.draw(game.nameBBox, 0, 102);
			game.dialFont.draw(game.batch, dialogue.getName(), 0, 123);
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
	    game.batch.draw(game.dialogueBBox, 0, 120);
	    game.dialFont.draw(game.batch, layout, 5, 174);
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
	    game.batch.draw(game.dialogueBox, 0, 00);
	    game.dialFont.draw(game.batch, layout, 5, 58);
	}
	public static void showChoices(Main game, Choice choice) {
		int j = 0;
		for(int i = (choice.getChoices().length - 1); i >= 0; i--) {
			game.batch.draw(game.choiceBox, 220, 59+(j*15));
			game.dialFont.draw(game.batch, choice.getChoices()[i], 232, 74+(j*15));
			if(i == choice.currChoice) {
				game.dialFont.draw(game.batch, ">", 222, 74+(j*15));
			}
			j++;
		}
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

	public static void showItemsStore(Main game, float x, float y, float dif, int[] pos, Item[] items) {
		for(int i = 0; i < items.length; i++) {
			if(items[i] != null) {
				String msg;
				game.invFont.setColor(1, 1, 1, 1);
				game.invFont.getData().setScale(0.3f);
				if(i == pos[1]) {
					game.mainFont.getData().setScale(0.3f);
					game.mainFont.draw(game.batch, ">", x-10, y);
				}
				if(pos[0] == 0) {
					if(items[i].getCost() > Main.player.gold) {
						game.invFont.setColor(0.6f, 0.6f, 0.6f, 1);
					}
				}
				msg = items[i].getName();
				game.invFont.draw(game.batch, msg, x, y);
				msg = (pos[0] != 1) ? items[i].getCost() + "G" : (items[i].q > 1) ? "x" + items[i].q : "";
				game.invFont.draw(game.batch, msg, x + 210, y);
				y -= dif;
			}
		}
	}

	public static void showBattleBars(Main game, Hero hero) {
		game.batch.draw(game.HPbar, 10, 55, (90f * hero.hp / hero.getHP()), 5);
		game.batch.draw(game.battleBar, 10, 55);
		game.batch.draw(game.MPbar, 115, 55, (90f * hero.mp / hero.getMP()), 5);
		game.batch.draw(game.battleBar, 115, 55);
		game.batch.draw(game.SPbar, 220, 55, (90f * hero.sp / hero.getSP()), 5);
		game.batch.draw(game.battleBar, 220, 55);
	}

	public static void showBInventory(Main game, int pos, Item[] items) {
		showItems(game, 10, 170, 0.25f, 5, 20, pos, items);
		Item item = items[pos];
		showItemBStats(game, item);
	}
	public static void showItemBStats(Main game, Item i) {
		int y = 170;
		int x = 180;
		game.invFont.setColor(1, 1, 1, 1);
		GlyphLayout layout = new GlyphLayout();
		game.invFont.getData().setScale(0.25f);
		game.invFont.draw(game.batch, i.getName(), x, y);
		game.invFont.getData().setScale(0.22f);
		layout.setText(
			    game.invFont,
			    i.getDesc(),
			    Color.WHITE,
			    130,      // ancho máximo de la caja
			    Align.left,
			    true      // wrap
			);
		game.invFont.draw(game.batch, layout, x, y - 20);
	}

	public static void showBSkills(Main game, int pos, Skill[] skills, Hero hero) {
		showSkills(game, 10, 170, 0.25f, 8, 10, pos, skills, hero);
		Skill skill = skills[pos];
		showSkillBStats(game, skill);
	}

	public static void showSkills(Main game, int x, int y, float scale, int length, int dif, int sel, Skill[] skills, Hero hero) {
	    if (skills == null || skills.length == 0 || length <= 0) return;

	    sel = Math.max(0, Math.min(sel, skills.length - 1));

	    int startIndex = 0;
	    if (skills.length > length) {
	        startIndex = Math.max(0, Math.min(sel - length / 2, skills.length - length));
	    }

	    int currentY = y;
	    int visibleCount = Math.min(length, skills.length);

	    for (int i = 0; i < visibleCount; i++) {
	        int skillIndex = startIndex + i;
	        Skill skill = skills[skillIndex];

	        if (skill != null) {
	            int x1 = 150;

	            if (skillIndex == sel) {
	                game.mainFont.getData().setScale(scale);
	                game.mainFont.draw(game.batch, ">", x - 5, currentY);
	            }

	            game.invFont.getData().setScale(scale);
	            
	            if (hero.mp < skill.getMP() || hero.sp < skill.getSP()) {
	                game.invFont.setColor(0.3f, 0.3f, 0.3f, 1f);
	            } else {
	            	game.invFont.setColor(1f, 1f, 1f, 1f);
	            }
	            
	            game.invFont.draw(game.batch, skill.getName(), x, currentY);

	            if (skill.getMP() > 0) {
	                game.invFont.setColor(0.3f, 0.3f, 1f, 1f);
	                game.invFont.draw(game.batch, Integer.toString(skill.getMP()), x + x1, currentY);
	                x1 -= 20;
	            }
	            
	            if (skill.getSP() > 0) {
	                game.invFont.setColor(0.3f, 1f, 0.3f, 1f);
	                game.invFont.draw(game.batch, Integer.toString(skill.getSP()), x + x1, currentY);
	            }

	            game.invFont.setColor(1f, 1f, 1f, 1f);

	            currentY -= dif;
	        }
	    }
	}
	public static void showSkillBStats(Main game, Skill s) {
	    if (s == null) return;

	    int y = 170;
	    int x = 180;
	    
	    game.invFont.setColor(1, 1, 1, 1);
	    GlyphLayout layout = new GlyphLayout();

	    // Nombre de la habilidad
	    game.invFont.getData().setScale(0.25f);
	    game.invFont.draw(game.batch, s.getName(), x, y);

	    // Tipo / Elemento
	    if (s.getType() != null) {
	        String typeMsg = "";
	        switch (s.getType()) {
	            case "FIR": typeMsg = "FUEGO"; break;
	            case "WAT": typeMsg = "AGUA"; break;
	            case "WIN": typeMsg = "VIENTO"; break;
	            case "EAR": typeMsg = "TIERRA"; break;
	            case "UNI": typeMsg = "UNIVERSAL"; break;
	            case "PHY": typeMsg = "FISICO"; break;
	            case "RAN": typeMsg = "RANGO"; break;
	            default:    typeMsg = ""; break;
	        }
	        game.invFont.getData().setScale(0.2f);
	        game.invFont.draw(game.batch, typeMsg, x + 100, y);
	    }

	    // Descripción con ajuste automático de texto (Wrap)
	    game.invFont.getData().setScale(0.22f);
	    layout.setText(
	        game.invFont,
	        s.getDesc(),
	        Color.WHITE,
	        130,             // Ancho máximo de la caja
	        Align.left,
	        true             // Wrap
	    );
	    game.invFont.draw(game.batch, layout, x, y - 15);
	}

	public static void showOptionsSelScreen(Main game, BitmapFont font, float fontSize, float x, float y, float dif, String msg,
			int sel, Hero[] selCharacters, Hero[] characters) {
		font.getData().setScale(fontSize);
		if(msg != null) {
			font.draw(game.batch, msg, x, y);
			y -= dif;
		}
		
		for(int i = 0; i < characters.length; i++) {
			String name = characters[i].getclassName();
			if(!name.equals("") && name != null) {
				String msg1;
				if(i == sel) {
					font.draw(game.batch, ">", x-10, y);
				}
				if(isSelected(selCharacters, characters[i])) {
					font.setColor(1f, 1f, 1f, 1f);
				}else {
					font.setColor(0.5f, 0.5f, 0.5f, 1f);
				}
				msg1 = name.toUpperCase();
				font.draw(game.batch, msg1, x, y);
				font.setColor(1f, 1f, 1f, 1f);
				x += dif;
			}
		}
	}

	private static boolean isSelected(Hero[] selCharacters, Hero hero) {
		for(Hero selHero : selCharacters) {
			if(selHero == hero) {
				return true;
			}
		}
		return false;
	}
}
