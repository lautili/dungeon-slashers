package io.github.dungeon_slashers.screens;

import java.util.Random;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.dungeon_slashers.Effect;
import io.github.dungeon_slashers.Main;
import io.github.dungeon_slashers.MenuScrollType;
import io.github.dungeon_slashers.Skill;
import io.github.dungeon_slashers.controllers.Battle;
import io.github.dungeon_slashers.controllers.DialMan;
import io.github.dungeon_slashers.controllers.InputMan;
import io.github.dungeon_slashers.controllers.Menu;
import io.github.dungeon_slashers.entities.Boss;
import io.github.dungeon_slashers.entities.BossEvent;
import io.github.dungeon_slashers.entities.Enemy;
import io.github.dungeon_slashers.entities.Entity;
import io.github.dungeon_slashers.entities.Hero;
import io.github.dungeon_slashers.item.Item;

/** Menu sala. */
public class BattleScreen implements Screen {
	private final int DIAL_ESCAPE = 100;
	private final int DIAL_DEFEAT = 101;
	private final int DIAL_VICTORY = 102;
	
	public static final int DIAL_BACTION = 200;
	
	private final int DIAL_BOSSEVENT = 300;
	private final int DIAL_TURNEND = 301;
	
	SpriteBatch batch;
	public Battle battle;
    private FitViewport viewport;
    private OrthographicCamera camera;
    private Main game;
    private Enemy[] enemies; //enemigos que realmente usar� la batalla
	private Entity[] entities; //todas las entidades
	private Hero[] heroes;
	private Skill[] actions; //las acciones
	private Entity[][] actionsObj; //los objetivos de las acciones.
	private Item[] actionsItem; //en caso de que se use un item en la batalla
	private int turn;
	public Screen lastScreen;
	boolean win;
	boolean tried;
	private Random rand = new Random();
	
	private final int BSTATE_TURN_START = 0;
	private final int BSTATE_FIGHT_OR_FLEE = 1;
	private final int BSTATE_CHAR_CHOOSE = 2;
	private final int BSTATE_ENEMY_CHOOSE = 3;
	private final int BSTATE_ACT = 4;
	private final int BSTATE_TURN_END = 5;
	private final int BSTATE_BATTLE_END = 6;
	private final int BSTATE_BOSS_DIALOGUE = 7;
	private final int BSTATE_END_AWAITING = 8;
	private int BState;
	
	private final int ASTATE_ATTACK = 0;
	private final int ASTATE_DEFEND = 1;
	private final int ASTATE_SKILL = 2;
	private final int ASTATE_INVENTORY = 3;
	private final int ASTATE_SELECT_OBJECTIVE = 4;
	private final int ASTATE_IDLE = 5;
	private final int ASTATE_NEXT = 6;
	
	private int AState;
	private int sel[];
	
	private int curr;
	private int gld;
	private int xp;
	
	int currAct = 0;
	boolean makeAct = true;
	
	private Texture background;
	
	private int currChar;
	public BattleScreen(Main game, Battle battle, Texture background) {
		this.game = game;
		this.battle = battle;
		this.background = background;
	}
	@Override
    public void show() {
        // Prepare your screen here.
		heroes = Main.player.getCharacters();
		camera = new OrthographicCamera();
		viewport = game.viewport;
		viewport.setCamera(camera);
		camera.setToOrtho(false, 320, 180);
		camera.zoom = 1f;
		batch = game.batch;
		win = false;
		tried = false;
		turn = 0;
		sel = new int[4];
		xp = 0;
		gld = 0;
		enemies = battle.initEnemies();
		for(int i = 0; i < enemies.length; i++) {
			xp+=enemies[i].getXP();
			gld+=enemies[i].getGLD();
		}
		actions = new Skill[enemies.length + heroes.length];
		// espacio 0 para perpetuador, espacio 1 para victima
		actionsObj = new Entity[actions.length][3]; // 0. caster 1. objetivo 2. objetivo aliado para skillType 5
		actionsItem = new Item[actions.length];
		entities = new Entity[actions.length];
		for(int i = 0; i < heroes.length; i++) {
			entities[i] = heroes[i];
		}
		for(int i = 0; i < enemies.length; i++) {
			int i2 = i + heroes.length;
			entities[i2] = enemies[i];
		}
		System.out.println("Comienza batalla contra: ");
		
		for(int i = 0; i < enemies.length; i++) {
			System.out.println(enemies[i].getName());
		}

		BState = BSTATE_TURN_START;
    }
    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
    	ScreenUtils.clear(0f, 0f, 0f, 1); //limpia el buffer de colores
    	game.viewport.apply();
    	game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
    	batch.begin();
    	if(background != null) {
    		game.batch.draw(background, 0, 0);
    	}
    	for(int i = 0; i < enemies.length; i++) {
    		Enemy enemy = enemies[i];
    		if((enemy.hp <= 0 || enemy.hasState("DWN")) && !battle.isBoss) {
    			continue;
    		}
    		float sectionWidth = 320f / enemies.length;
    		float x = sectionWidth * i + sectionWidth / 2f - 50f;
    		batch.draw(enemy.getTexture(), x, 50, 100, 100);
    	}
    	batch.draw(game.behindCharactersBattle, 0, 0);
    	for(int i = 0; i < heroes.length; i++) {
    		if(heroes[i] != null && heroes[i].getPortrait() != null) {
    			float sectionWidth = 320f / heroes.length;
    			float x = sectionWidth * i + sectionWidth / 2f - 25f;
    			batch.draw(heroes[i].getPortrait(), x, 0, 50, 50);
    			if(BState == BSTATE_CHAR_CHOOSE && i == currChar) {
    				batch.draw(game.currentChar, x, 0);
    				game.invFont.getData().setScale(0.15f);
        			Effect[] effects = heroes[currChar].getEffects();
        			for(int j = 0; j < effects.length; j++) {
    					Effect effect = effects[j];
    					if(effect != null) {
    						game.invFont.draw(batch, effect.getShortName(), x - 5 + (15 * j), 55);
    					}
    				}
    			}
    		}
    	}
    	BossEvent event = null;
    	switch(BState) {
    	case BSTATE_BOSS_DIALOGUE:
    		
    		break;
    	case BSTATE_TURN_START:
    		//actualiza cosas importantes de las entidades, tanto enemigos como heroes
			 //no se usara si la pelea no es bossfight
    		event = null;
    		if(battle.isBoss) {
    			event = ((Boss) enemies[0]).checkEvents(game, turn, delta);
    		}
    		int count = (event != null) ? event.getMsgs().length : 0;
			updateEntities(count);
			DialMan.addDialogue(count, DIAL_BOSSEVENT);
			DialMan.addDialogue(DIAL_BOSSEVENT, -1);
			
			if(event == null) {
				BState = BSTATE_FIGHT_OR_FLEE;
			}else {
				BState = BSTATE_BOSS_DIALOGUE;
			}
			curr = 0;
			currChar = 0;
			sel[0] = 0;
			sel[1] = 0;
			sel[2] = 0;
			sel[3] = 0;
    		break;
    	case BSTATE_FIGHT_OR_FLEE:
    		sel[0] = InputMan.scrollInt(MenuScrollType.VERTICAL, 2, sel[0]);
    		game.batch.draw(game.fightOrFleeBox, 0, 170 - 16);
    		game.batch.draw(game.fightOrFleeBox, 0, 170 - 16 - 25);
    		Menu.showOptionsY(game, game.mainFont, 0.4f, 10, 170, 25, null, sel[0], "ATACAR", "HUIR");
    		if(InputMan.checkKey("Z")) {
    			switch(sel[0]) {
    			case 0:
    				AState = ASTATE_IDLE;
    				BState = BSTATE_CHAR_CHOOSE;
    				break;
    			case 1:
    				if(!tried && !battle.isBoss) {
	    				if(rand.nextInt(100) < 50) {
	    					DialMan.addDialogue(0, DIAL_ESCAPE);
	    					DialMan.addDialogue(DIAL_ESCAPE, -1, null, null, "Has escapado!", 20);
	    					BState = BSTATE_BATTLE_END;
	    				}else {
	    					DialMan.addDialogue(0, -1, null, null, "No has podido escapar.", 20);
	    					tried = true;
	    				}
    				}
    				break;
    			}
    		}
    		break;
    	case BSTATE_CHAR_CHOOSE:
    		//90 de largo cada uno, 16.6f de distancia entre si
    		characterChoose();
    		if(currChar >= heroes.length) {
    			currChar = 0;
    			BState = BSTATE_ENEMY_CHOOSE;
    		}
    		break;

    	case BSTATE_ENEMY_CHOOSE:
    		enemyChoose(event);
    		BState = BSTATE_ACT;
    		break;

    	case BSTATE_ACT:
    		act();
    		if(currAct >= actions.length) {
    		BState = BSTATE_TURN_END;
    		}
    		break;

    	case BSTATE_TURN_END:
    		tried = false;
    		int cont = 0;
			for(int i = 0; i < enemies.length; i++) {
				if(enemies[i].hp <= 0 && !enemies[i].hasState("DWN")) {
					enemies[i].setEffect(new Effect("DWN")); //si tiene la vida en 0, le pone el efecto caido
				}
				if(enemies[i].hp <= 0) {
					cont++; //contador para ver si todos los enemigos murieron
				}
			}
			if(cont == enemies.length) {
				int cont2 = 0;
				if(battle.isBoss) {
					Boss boss = (Boss) enemies[0];
					if(boss.finalEvent != null) {
						boss.finalEvent.checkBossEvent(game, boss, turn, delta);
						cont2 = boss.finalEvent.getMsgs().length;
						boss.activateFlag();
					}
				}
				DialMan.addDialogue(cont2, cont2+1, null, null, "Has ganado!", 20);
				cont2++;
				DialMan.addDialogue(cont2, cont2+1, null, null, "Obtienes " + gld + "G y " + xp + " XP.", 20);
				cont2++;
				cont = cont2;
				for(int i = 0; i < heroes.length; i++) {
					heroes[i].clearEffects();
					if(heroes[i].hp == 0) { //si el heroe esta muerto, lo pone a 1 de vida
						heroes[i].hp++;
						continue;
					}
					
					heroes[i].xp+=xp; //le suma la xp solo a los heroes que no murieron
					cont = heroes[i].checkLvl(cont);
				}
				Main.player.gold+=gld;
				DialMan.addDialogue(cont, DIAL_VICTORY);
				DialMan.addDialogue(DIAL_VICTORY, -1);
				BState = BSTATE_BATTLE_END;
				win = true;
				break;
			}
			cont = 0;
			for(int i = 0; i < heroes.length; i++) {
				if(heroes[i].hp <= 0 && !heroes[i].hasState("DWN")) {
					
					heroes[i].setEffect(new Effect("DWN")); //si tiene la vida en 0, le pone el efecto caido
				}
				if(heroes[i].hp <= 0) {
					cont++; //contador para ver si todos los heroes murieron
				}
			}
			if(cont == heroes.length) {
				DialMan.addDialogue(0, DIAL_DEFEAT, null, null, "Has perdido.", delta);
				DialMan.addDialogue(DIAL_DEFEAT, -1);
				for(int i = 0; i < heroes.length; i++) {
					heroes[i].hp++;
					heroes[i].clearEffects();
				}
				BState = BSTATE_BATTLE_END;
				win = false;
				break;
			}
			cont = 0;
			for(int i = 0; i < entities.length; i++) {
				cont = entities[i].updateEffects(cont);
			}
			DialMan.addDialogue(cont, DIAL_TURNEND);
			DialMan.addDialogue(DIAL_TURNEND, -1);
			currAct = 0;
			makeAct = true;
			turn++;
			BState = BSTATE_END_AWAITING;
    		break;
    	case BSTATE_BATTLE_END:
    		
    		break;
    	}
    	int dialogues = DialMan.showBDialogues(game, delta);
    	switch(dialogues) {
    	case DIAL_BACTION:
    		currAct++;
    		makeAct = true;
    		break;
    	case DIAL_DEFEAT:
    		game.setScreen(game.firstScreen);
    		Main.player.currScreen = "LOOSE";
    		enemies = null;
    		break;
    	case DIAL_VICTORY:
    		if(battle.isBoss) {
				((Boss) enemies[0]).setDefeat();
			}
			enemies = null;
    		game.setScreen(lastScreen);
    		break;
    	case DIAL_ESCAPE:
    		game.setScreen(lastScreen);
    		enemies = null;
    		break;
    	case DIAL_TURNEND:
    		BState = BSTATE_TURN_START;
    		break;
    	case DIAL_BOSSEVENT:
    		BState = BSTATE_FIGHT_OR_FLEE;
    		break;
    	}
    	batch.end();
    	if(InputMan.checkKey("F1")) {
    		heroes[currChar].setEffect(new Effect("POI"));
    		heroes[currChar].setEffect(new Effect("CAN"));
    		heroes[currChar].setEffect(new Effect("ENC"));
    		heroes[currChar].setEffect(new Effect("BEN"));
    	}
    	if(InputMan.checkKey("F2")) {
    		heroes[currChar].setEffect(new Effect("RAG"));
    	}
    	if(InputMan.checkKey("F3")) {
    		heroes[currChar].setEffect(new Effect("SIL"));
    	}
    	if(InputMan.checkKey("F4")) {
    		heroes[currChar].setEffect(new Effect("SLE"));
    	}
    	if(InputMan.checkKey("F5")) {
    		heroes[currChar].setEffect(new Effect("DWN"));
    		heroes[currChar].hp = 0;
    	}
    }
    private void updateEntities(int count) {
    	// actualiza las entidades al principio de cada turno
    			for(int i = 0; i < enemies.length; i++) {
    				enemies[i].prot = 1;
    				
    				if((turn % 2) == 0 ) { //solo en turnos pares
    					enemies[i].modMP( (int) (enemies[i].getMP() * 0.2 + 10)); //recupera un poco el mana de los enemigos
    					enemies[i].modSP( (int) (enemies[i].getSP() * 0.2 + 10)); //recupera un poco la stamina de los enemigos
    					//esto ultimo para asegurar de que nunca se queden sin hacer habilidades.
    				}
    			}
    			for(int i = 0; i < heroes.length; i++) {
    				heroes[i].prot = 1;
    			}
    			for(int i = 0; i < actions.length; i++) { //limpia los arrays
    				actions[i] = null;
    				actionsObj[i][0] = null;
    				actionsObj[i][1] = null;
    				actionsObj[i][2] = null;
    				actionsItem[i] = null;
    			}
	}
	private void act() {
    	//simple bubblesort para ordenar las acciones por velocidad
    	if(!makeAct || currAct >= actions.length) {
    		return;
    	}
    	makeAct = false;
    	if(currAct == 0) {
	    	for(int i = 0; i < actions.length; i++) {
				for(int j = 0; j < actions.length-i-1; j++) {
					if(actions[j] == null || actions[j + 1] == null) {
						continue;
					}else {
						 int spd1 = actionsObj[j][0].getSPD() + actions[j].SPD; // SPD de la entidad + SPD de la Skill
					     int spd2 = actionsObj[j + 1][0].getSPD() + actions[j + 1].SPD; // SPD entidad + SPD Skill
					     if (spd1 < spd2) { // mayor velocidad primero
	
					         Skill tempSkill = actions[j];
					         actions[j] = actions[j + 1];
					         actions[j + 1] = tempSkill;
					         
					         Item tempItem = actionsItem[j];
					         actionsItem[j] = actionsItem[j + 1];
					         actionsItem[j + 1] = tempItem;
					         
					         Entity[] tempObj = actionsObj[j];
					         actionsObj[j] = actionsObj[j + 1];
					         actionsObj[j + 1] = tempObj;
					     }
					}
				}
			}
    	}
		//de esta forma las acciones se ordenan por quien actuara primero segun su velocidad
		//	y la velocidad de la accion.
		
				Skill skill = actions[currAct]; //guarda la skill en una variable
				if(actionsItem[currAct] == null) {
					if (skill != null
					        && actionsObj[currAct][0].hp > 0
					        && !actionsObj[currAct][0].hasState("DWN")
					        && (actionsObj[currAct][1] == null ||
					        actionsObj[currAct][1].hp > 0
					        	&& !actionsObj[currAct][1].hasState("DWN")
					        	)
					) {
						switch(skill.getSkillType()) {
						case 0: //self
							skill.use(actionsObj[currAct][0]);
							break;
						case 1: //to enemy
							if(actionsObj[currAct][0].hasState("CON")) {
								if(rand.nextInt(2) == 0) {
								skill.use(actionsObj[currAct][0],actionsObj[currAct][0]);
								return;
								}
							}
								skill.use(actionsObj[currAct][0],actionsObj[currAct][1]);
							break;
						case 2: //to enemies
							if(actionsObj[currAct][0].hasState("CON")) {
								int random = rand.nextInt(2);
								if(random == 0) {
									if(actionsObj[currAct][0].getClass() == Hero.class) { //si lo usa un heroe, lo usa en heroes
										skill.use(actionsObj[currAct][0], heroes);
									}else { //si no (es un enemigo), la usa en heroes
										skill.use(actionsObj[currAct][0], enemies); 
									}
								}
							}
								if(actionsObj[currAct][0].getClass() == Hero.class) { //si lo usa un heroe, lo usa en enemigos
									skill.use(actionsObj[currAct][0], enemies);
								}else { //si no (es un enemigo), la usa en heroes
									skill.use(actionsObj[currAct][0], heroes); 
								}
							break;
						case 3: //to ally
							if(actionsObj[currAct][0].hasState("CON")) {
								if(rand.nextInt(2) == 0) {
									skill.use(actionsObj[currAct][0], actionsObj[currAct][0]);
									return;
								}
							}
							skill.use(actionsObj[currAct][0], actionsObj[currAct][1]);
							break;
						case 4: //to allies
							if(actionsObj[currAct][0].hasState("CON")) {
								int random = rand.nextInt(2);
								if(random == 0) {
									if(actionsObj[currAct][0].getClass() == Hero.class) { //si lo usa un heroe, lo usa en heroes
										skill.use(actionsObj[currAct][0], enemies);
									}else { //si no (es un enemigo), la usa en heroes
										skill.use(actionsObj[currAct][0], heroes); 
									}
								}
							}
								if(actionsObj[currAct][0].getClass() == Hero.class) { //si lo usa un heroe, lo usa en enemigos
									skill.use(actionsObj[currAct][0], heroes);
								}else { //si no (es un enemigo), la usa en heroes
									skill.use(actionsObj[currAct][0], enemies); 
								}
							break;
						case 5: //To enemy and ally
							if(actionsObj[currAct][0].hasState("CON")) {
								int random = rand.nextInt(2);
								if(random == 0) {
									Entity temp = actionsObj[currAct][1];
									actionsObj[currAct][1] = actionsObj[currAct][0];
									actionsObj[currAct][0] = temp;
								}
							}
									skill.use(actionsObj[currAct][0], actionsObj[currAct][2], actionsObj[currAct][1]);
							break;
						case 6: //to all entities
							skill.use(actionsObj[currAct][0], entities);
							break;
						}
					}else {
						makeAct = true;
						currAct++;
					}
				}else {
					//solo hace esto si la accion usa un item
					skill.setType(actionsItem[currAct].getType());
					skill.use(actionsObj[currAct][0], actionsObj[currAct][1], Main.player, actionsItem[currAct]);
				}
	}
	private void enemyChoose(BossEvent event) {
		for(int i = 0; i < enemies.length; i++) {
			if(enemies[i].hp <= 0 || enemies[i].hasState("SLE")) { //si esta muerto o dormido, continua el ciclo
				actions[curr] = null;
				curr++;
				continue;
			}
			if(event == null || event.getSkill() == null) {
				actions[curr] = enemyAct(enemies[i]);
			}else {
				actions[curr] = event.getSkill();
			}
			actionsObj[curr][0] = enemies[i];
			actionsObj[curr][1] = selEnObj(actions[curr], enemies[i]); //objetivos
			curr++;
		}
	}
    
    private Entity selEnObj(Skill skill, Enemy enemy) {
    	if(skill == null) {
			return null;
		}else {
			int sel;
			switch(skill.getSkillType()) {
			case 0: //self
				return enemy; 
			case 1: //to enemy
				do {
					if(!enemy.hasState("RAG")) {
						sel = rand.nextInt(heroes.length);
						if(heroes[sel].hp <= 0) {
							continue; //siempre y cuando no eleccione a un heroe muerto, el ciclo termina
						}
					}else {
						sel = rand.nextInt(entities.length);
						if(entities[sel].hp <= 0 || entities[sel] == enemy) {
							continue;
						}else {
							return entities[sel];
						}
					}
					break;
				}while(true);
				return heroes[sel];
			case 2: //to enemies
				return null; //la seleccion a varios objetivos las maneja act()
			case 3: //to ally
				do {
					if(!enemy.hasState("RAG")) {
						sel = rand.nextInt(enemies.length);
						if(enemies[sel].hp <= 0) {
							continue; //siempre y cuando no eleccione a un enemigo muerto, el ciclo termina
						}
					}else {
						sel = rand.nextInt(entities.length);
						if(entities[sel].hp <= 0) {
							continue;
						}else {
							return entities[sel];
						}
					}
					break;
				}while(true);
				return enemies[sel];
			case 4: //to allies
				return null; //la seleccion a varios objetivos las maneja act()
			default:
				return null;
			}
		}
	}
	private Skill enemyAct(Enemy enemy) {
		Skill[] skills = enemy.getSkills(); //guarda las skills del enemigo en una lista
		int sel = 0;
			if(enemy.hasState("SIL")) {
				sel = rand.nextInt(2);
				return skills[sel];
			}else {
				do {
					int temp = rand.nextInt(100);
					if(temp < 50) { //que tenga prioridad por sobre otras cosas un ataque normal
						return skills[0];
					}else if(temp < 80) { //que la 2da mayor prioridad sea usar skills
						if(skills.length <= 2) { //si no tiene skills
							return skills[0];
						}
						sel = rand.nextInt(skills.length); //selecciona aleatoriamente una skill
						if(skills[sel] != null) {
							if(enemy.mp < skills[sel].getMP() || enemy.sp < skills[sel].getSP()) {
								continue; //si el enemigo tiene < MP o SP que la que usa la skill,vuelve a hacer el random
							}
							return skills[sel];
						}
					}else { //que la prioridad mas pequeña la tenga defenderse
						return skills[1];	
					}
				}while(true);
			}
	}
    
	private void characterChoose() {
		System.out.println("curr: " + curr + "\ncurrChar: " + currChar);
    	Hero hero = heroes[currChar];
    	while(hero.hp <= 0 || hero.hasState("DWN") || hero.hasState("SLE") || hero.hasState("RAG")) {
	    	if(hero.hp <= 0 || hero.hasState("DWN") || hero.hasState("SLE")) {
	    		curr++;
	    		currChar++;
	    	}else if(hero.hasState("RAG")) {
	    		actions[curr] = hero.getSkills()[0];
	    		actionsObj[curr][0] = hero;
	    		Entity entity;
	    		do{
	    			entity = entities[rand.nextInt(entities.length)];
	    			if(entity == actionsObj[curr][0] || entity.hp <= 0)
	    				continue;
	    			break;
	    		}while(true);
	    		actionsObj[curr][1] = entity;
	    		curr++;
	    		currChar++;
	    	}
	    	if(currChar >= 4) {
	    		BState = BSTATE_ENEMY_CHOOSE;
	    		return;
	    	}else {
	    		hero = heroes[currChar];
	    	}
    	}
    	Menu.showOptionsX(game, game.mainFont, 0.2f, 10, 70, 80, null, sel[1], "ATACAR", "DEFENDER", "HABILIDADES", "INVENTARIO");
    	switch (AState) {
		case ASTATE_IDLE:
			actionsItem[curr] = null;
    		Menu.showBattleBars(game, heroes[currChar]);
			sel[1] = InputMan.scrollInt(MenuScrollType.HORIZONTAL, 4, sel[1]);
			sel[2] = 0;
    		if(InputMan.checkKey("Z")) {
    			switch(sel[1]) {
    			case ASTATE_ATTACK:
    				AState = ASTATE_SELECT_OBJECTIVE;
    				actions[curr] = hero.getSkills()[0];
    				
    				break;
    			case ASTATE_DEFEND:
    				AState = ASTATE_SELECT_OBJECTIVE;
    				actions[curr] = hero.getSkills()[1];
    				
    				break;
    			case ASTATE_SKILL:
    				if(!hero.hasState("SIL"))
    				AState = ASTATE_SKILL;
    				
    				break;
    			case ASTATE_INVENTORY:
    				AState = ASTATE_INVENTORY;
    				
    				break;
    			}
    		}else if(InputMan.checkKey("X") && currChar > 0) {
    			int ogCurrChar = currChar;
    			actions[curr] = null;
				actionsObj[curr][0] = null;
				actionsObj[curr][1] = null;
				actionsObj[curr][2] = null;
				actionsItem[curr] = null;
    			currChar--;
    			curr--;
    			if(currChar > 0 ) {
    				while(heroes[currChar].hp <= 0 || heroes[currChar].hasState("DWN")) {
	    				currChar--;
	    				curr--;
	    				actions[curr] = null;
	    				actionsObj[curr][0] = null;
	    				actionsObj[curr][1] = null;
	    				actionsObj[curr][2] = null;
	    				actionsItem[curr] = null;
	    				if(currChar < 0) {
	    					currChar = ogCurrChar;
	    					break;
	    				}
    				}
    			}
    			AState = ASTATE_IDLE;
    		}
    		
    		break;
		case ASTATE_SKILL:
			Skill[] skills = hero.getRealSkills();
			sel[2] = InputMan.scrollInt(MenuScrollType.VERTICAL, skills.length, sel[2]);
			game.batch.draw(game.battleMenu, 0, 0);
			Menu.showBSkills(game, sel[2], skills, hero);
			if(InputMan.checkKey("Z")) {
				if(skills[sel[2]].getMP() <= hero.mp && skills[sel[2]].getSP() <= hero.sp) {
					AState = ASTATE_SELECT_OBJECTIVE;
					actions[curr] = skills[sel[2]];
					if(skills[sel[2]].getSkillType() == 3) {
						sel[2] = currChar;
					}else {
						sel[2] = 0;
					}
				}
			}else if(InputMan.checkKey("X")) {
				AState = ASTATE_IDLE;
			}
			
			break;
		case ASTATE_INVENTORY:
			Item[] items = Main.player.getInventory();
			sel[2] = InputMan.scrollInt(MenuScrollType.VERTICAL, items.length, sel[2]);
			game.batch.draw(game.battleMenu, 0, 0);
			Menu.showBInventory(game, sel[2], items);
			if(InputMan.checkKey("Z")) {
					AState = ASTATE_SELECT_OBJECTIVE;
					actions[curr] = hero.getSkills()[2];
					actionsItem[curr] = items[sel[2]];
					actions[curr].setType(actionsItem[curr].getType());
					sel[2] = currChar;
			}else if(InputMan.checkKey("X")) {
				AState = ASTATE_IDLE;
			}
			break;
			
		case ASTATE_SELECT_OBJECTIVE:
			actionsObj[curr][0] = hero;
			int antsel;
			boolean haciaAdelante;
			switch(actions[curr].getSkillType()) {
			case 0: // Self
				actionsObj[curr][1] = hero; 
				AState = ASTATE_NEXT;
				break;
			case 1: // to Enemy
					antsel = sel[2];
					sel[2] = InputMan.scrollInt(MenuScrollType.HORIZONTAL, enemies.length, sel[2]);
					if (antsel == 0 && sel[2] == enemies.length - 1) {
					    haciaAdelante = false;
					} else if (antsel == enemies.length - 1 && sel[2] == 0) {
					    haciaAdelante = true;
					} else {
					    haciaAdelante = sel[2] > antsel;
					}
					while(enemies[sel[2]].hp <= 0 || enemies[sel[2]].hasState("DWN")) {
						if (haciaAdelante) {
					        sel[2]++;
					        if (sel[2] >= enemies.length) {
					            sel[2] = 0;
					        }
					    } else {
					        sel[2]--;
					        if (sel[2] < 0) {
					            sel[2] = enemies.length - 1;
					        }
					    }
						if(sel[2] < 0 || sel[2] >= enemies.length) {
							sel[2] = antsel;
						}
					}
					if(InputMan.checkKey("Z")) {
						actionsObj[curr][1] = enemies[sel[2]]; 
						AState = ASTATE_NEXT;
					}else if(InputMan.checkKey("X")) {
						AState = ASTATE_IDLE;
					}
					
					selectObjective(sel[2]);
				break;
			case 2: // to Enemies
				actionsObj[curr][1] = null; 
				AState = ASTATE_NEXT;
				break;
			case 3: // to Ally
				
				antsel = sel[2];
				sel[2] = InputMan.scrollInt(MenuScrollType.HORIZONTAL, heroes.length, sel[2]);
				if (antsel == 0 && sel[2] == heroes.length - 1) {
				    haciaAdelante = false;
				} else if (antsel == heroes.length - 1 && sel[2] == 0) {
				    haciaAdelante = true;
				} else {
				    haciaAdelante = sel[2] > antsel;
				}
				while(heroes[sel[2]].hp <= 0 || heroes[sel[2]].hasState("DWN")) {
					if (haciaAdelante) {
				        sel[2]++;
				        if (sel[2] >= heroes.length) {
				            sel[2] = 0;
				        }
				    } else {
				        sel[2]--;
				        if (sel[2] < 0) {
				            sel[2] = heroes.length - 1;
				        }
				    }
					if(sel[2] < 0 || sel[2] >= heroes.length) {
						sel[2] = antsel;
					}
				}
				if(InputMan.checkKey("Z")) {
					actionsObj[curr][1] = heroes[sel[2]]; 
					AState = ASTATE_NEXT;
				}else if(InputMan.checkKey("X")) {
					AState = ASTATE_IDLE;
				}
				selectAObjective(sel[2]);
				break;
			case 4: // to Allies
				actionsObj[curr][1] = null; 
				AState = ASTATE_NEXT;
				break;
			case 5: // To Enemy and Ally
				if(actionsObj[curr][1] == null) {
					antsel = sel[2];
					sel[2] = InputMan.scrollInt(MenuScrollType.HORIZONTAL, heroes.length, sel[2]);
					if (antsel == 0 && sel[2] == heroes.length - 1) {
					    haciaAdelante = false;
					} else if (antsel == heroes.length - 1 && sel[2] == 0) {
					    haciaAdelante = true;
					} else {
					    haciaAdelante = sel[2] > antsel;
					}
					while(heroes[sel[2]].hp <= 0 || heroes[sel[2]].hasState("DWN")) {
						if (haciaAdelante) {
					        sel[2]++;
					        if (sel[2] >= heroes.length) {
					            sel[2] = 0;
					        }
					    } else {
					        sel[2]--;
					        if (sel[2] < 0) {
					            sel[2] = heroes.length - 1;
					        }
					    }
						if(sel[2] < 0 || sel[2] >= heroes.length) {
							sel[2] = antsel;
						}
					}
					if(InputMan.checkKey("Z")) {
						actionsObj[curr][1] = heroes[sel[2]]; 
					}else if(InputMan.checkKey("X")) {
						AState = ASTATE_IDLE;
					}
				}else {
					antsel = sel[2];
					sel[2] = InputMan.scrollInt(MenuScrollType.HORIZONTAL, enemies.length, sel[2]);
					if (antsel == 0 && sel[2] == enemies.length - 1) {
					    haciaAdelante = false;
					} else if (antsel == enemies.length - 1 && sel[2] == 0) {
					    haciaAdelante = true;
					} else {
					    haciaAdelante = sel[2] > antsel;
					}
					while(enemies[sel[2]].hp <= 0 || enemies[sel[2]].hasState("DWN")) {
						if (haciaAdelante) {
					        sel[2]++;
					        if (sel[2] >= enemies.length) {
					            sel[2] = 0;
					        }
					    } else {
					        sel[2]--;
					        if (sel[2] < 0) {
					            sel[2] = enemies.length - 1;
					        }
					    }
						if(sel[2] < 0 || sel[2] >= enemies.length) {
							sel[2] = antsel;
						}
					}
						if(InputMan.checkKey("Z")) {
							actionsObj[curr][2] = enemies[sel[2]]; 
							AState = ASTATE_NEXT;
						}else if(InputMan.checkKey("X")) {
							actionsObj[curr][1] = null;
						}
						selectObjective(sel[2]);
				}
				break;
			case 6:
				actionsObj[curr][1] = null; 
				AState = ASTATE_NEXT;
				break;
			default:
				actionsObj[curr][1] = null; 
				AState = ASTATE_NEXT;
				break;
			}
			break;
		case ASTATE_NEXT:
			curr++;
			currChar++;
			sel[1] = 0;
			sel[2] = 0;
			sel[3] = 0;
			AState = ASTATE_IDLE;
			break;
		}
	}
	private void selectAObjective(int sel) {
		for(int i = 0; i < heroes.length; i++) {
			float sectionWidth = 320f / heroes.length;
			float x = sectionWidth * i + sectionWidth / 2f - 25f;
			if(i == sel) {
				Hero hero = heroes[sel];
				batch.draw(game.selection, x, 0);
				Menu.showBattleBars(game, hero);
			}
			
		}
	}
	private void selectObjective(int sel) {
		for(int i = 0; i < enemies.length; i++) {
			float sectionWidth = 320f / enemies.length;
			float x = sectionWidth * i + sectionWidth / 2f - 25f;
			float x2 = sectionWidth * i + sectionWidth / 2f - 45f;
			if(i == sel) {
				batch.draw(game.selection, x, 50 + 50);
				float num = (90f * enemies[i].hp / enemies[i].getHP());
				game.batch.draw(game.HPbar, x2, 155, num, 5);
				game.mainFont.getData().setScale(0.15f);
				game.mainFont.draw(batch, Integer.toString(enemies[i].hp), x2 + num - 5, 155);
				game.mainFont.draw(batch, Integer.toString(enemies[i].getHP()), x2 + 90 - 5, 165);
				game.batch.draw(game.battleBar, x2, 155);
				Effect[] effects = enemies[i].getEffects();
				for(int j = 0; j < effects.length; j++) {
					Effect effect = effects[j];
					if(effect != null) {
						game.invFont.getData().setScale(0.2f);
						game.invFont.draw(batch, effect.getShortName(), x2 + (20 * j), 165);
					}
				}
			}
			
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