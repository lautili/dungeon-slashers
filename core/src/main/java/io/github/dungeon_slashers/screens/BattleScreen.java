package io.github.dungeon_slashers.screens;

import java.util.Random;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
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
    private Enemy[] enemies; // enemigos que realmente usará la batalla
	private Entity[] entities; // todas las entidades
	private Hero[] heroes;
	private Skill[] actions; // las acciones
	private Entity[][] actionsObj; // los objetivos de las acciones
	private Item[] actionsItem; // en caso de que se use un item
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
	
	public Texture background;
	private int currChar;

	private BossEvent currentBossEvent;

	private Entity currentAttacker = null;
	private Entity currentTarget = null;
	private Skill currentSkill = null;
	private Item currentItem = null;
	private float actTimer = 0f;
	private float hitShakeTimer = 0f;

	private boolean inspectingEnemy = false;
	private int inspectEnemyIdx = 0;

	public BattleScreen(Main game, Battle battle, Texture background) {
		this.game = game;
		this.battle = battle;
		this.background = background;
	}

	private float getDamageBounceY() {
		if (hitShakeTimer <= 0) return 0f;
		float progress = 1f - (hitShakeTimer / 0.4f);
		if (progress < 0f) progress = 0f;
		if (progress > 1f) progress = 1f;
		return (float) (Math.abs(Math.sin(progress * Math.PI * 2.5)) * (1f - progress) * 20f);
	}

	@Override
    public void show() {
		heroes = Main.player.getCharacters();
		camera = new OrthographicCamera();
		viewport = game.viewport;
		viewport.setCamera(camera);
		camera.setToOrtho(false, 320, 180);
		camera.zoom = 1f;
		batch = game.batch;
		win = false;
		tried = false;
		makeAct = true;
		currAct = 0;
		turn = 0;
		sel = new int[4];
		xp = 0;
		gld = 0;
		enemies = battle.initEnemies();
		for(int i = 0; i < enemies.length; i++) {
			xp += enemies[i].getXP();
			gld += enemies[i].getGLD();
			enemies[i].discovered = true;
			Main.player.updateBestiary(enemies[i]);
		}
		actions = new Skill[enemies.length + heroes.length];
		actionsObj = new Entity[actions.length][3];
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
    	ScreenUtils.clear(0f, 0f, 0f, 1);
    	game.viewport.apply();
    	game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
    	
    	actTimer += delta;
    	if (hitShakeTimer > 0) hitShakeTimer -= delta;

    	batch.begin();
    	if(background != null) {
    		game.batch.draw(background, 0, 0);
    	}

    	for(int i = 0; i < enemies.length; i++) {
    		Enemy enemy = enemies[i];
    		if(!enemy.draw) {
    			continue;
    		}
    		float sectionWidth = 320f / enemies.length;
    		float x = sectionWidth * i + sectionWidth / 2f - 50f;
    		float y = 50f;

    		if (enemy == currentAttacker) {
    			y -= 8f;
    		}
    		if (enemy.attacked && hitShakeTimer > 0) {
    			x += (float) (Math.sin(actTimer * 40f) * 4f);
    		}

    		batch.draw(enemy.getTexture(), x, y, 100, 100);
    		

    		// Marcador si es el objetivo activo en BSTATE_ACT
    		try {
	    		if (BState == BSTATE_ACT && enemy.attacked) {
	    			if(currentSkill == null || !currentSkill.getID().equals("defend")) {
	    				game.mainFont.getData().setScale(0.3f);
	    				if(enemy.lastDamageTaken > 0) {
	    					if(currentTarget != null) {
	    						batch.draw(game.selection, x + 25f, y + 50f);
	    					}
		    				String temp;
		    				temp = (enemy.lastIsDamage) ? "-" : "+";
		    				String dmgText = "";
		    				if(currentSkill != null) {
		    					for(int k = 0; k < currentSkill.getAtkTimes(); k++) {
		    						dmgText += temp + enemy.lastDamageTaken + "\n";
		    					}
		    				}
		    				game.mainFont.setColor(0f, 0f, 0f, 1f);
							game.mainFont.draw(batch, dmgText, x + 10 - 0.4f, y + 100 + getDamageBounceY());
							game.mainFont.draw(batch, dmgText, x + 10 + 0.4f, y + 100 + getDamageBounceY());
							game.mainFont.draw(batch, dmgText, x + 10, y + 0.4f + 100 + getDamageBounceY());
							game.mainFont.draw(batch, dmgText, x + 10, y - 0.4f + 100 + getDamageBounceY());
							switch((int) (enemy.lastDamageWeakness * 100)) {
		    				case 100:
		    					game.mainFont.setColor(1f, 1f, 1f, 1f);
		    					break;
		    				case 150:
		    					game.mainFont.setColor(1f, 0.6f, 0.6f, 1f);
		    					break;
		    				case 200:
		    					game.mainFont.setColor(1f, 0.3f, 0.3f, 1f);
		    					break;
		    				case 400:
		    					game.mainFont.setColor(1f, 0f, 0f, 1f);
		    					break;
		    				case 75:
		    					game.mainFont.setColor(0.5f, 0.6f, 0.6f, 1f);
		    					break;
		    				case 50:
		    					game.mainFont.setColor(0.5f, 0.3f, 0.3f, 1f);
		    					break;
		    				case 25:
		    					game.mainFont.setColor(0.3f, 0.2f, 0.2f, 1f);
		    					break;
		    				default:
		    					game.mainFont.setColor(0.5f, 1f, 0.5f, 1f);
		    					break;
		    				}
		    				game.mainFont.draw(batch, dmgText, x + 10, y + 100 + getDamageBounceY());
		    				game.mainFont.setColor(1f, 1f, 1f, 1f);
	    				}
	    			}
	    		}
    		}catch(Exception e) {
    			
    		}
    	}

    	batch.draw(game.behindCharactersBattle, 0, 0);
    	for(int i = 0; i < heroes.length; i++) {
    		if(heroes[i] != null && heroes[i].getPortrait() != null) {
    			float sectionWidth = 320f / heroes.length;
    			float x = sectionWidth * i + sectionWidth / 2f - 25f;
    			float y = 0f;

    			if (heroes[i] == currentAttacker) {
    				y += 10f;
    			}
    			if (heroes[i].attacked && hitShakeTimer > 0) {
    				x += (float) (Math.sin(actTimer * 30f) * 3f);
    			}

    			batch.draw(heroes[i].getPortrait(), x, y, 50, 50);

    			// Resaltar atacante o héroe seleccionado
    			if(heroes[i] == currentAttacker || (BState == BSTATE_CHAR_CHOOSE && i == currChar)) {
    				batch.draw(game.currentChar, x, y);
    				game.invFont.getData().setScale(0.15f);
        			Effect[] effects = heroes[i].getEffects();
        			for(int j = 0; j < effects.length; j++) {
    					Effect effect = effects[j];
    					if(effect != null) {
    						game.invFont.draw(batch, effect.getShortName(), x - 5 + (15 * j), y + 55);
    					}
    				}
    			} else {
    				Effect[] effects = heroes[i].getEffects();
        			for(int j = 0; j < effects.length; j++) {
    					Effect effect = effects[j];
    					if(effect != null) {
    						game.invFont.getData().setScale(0.15f);
    						game.invFont.draw(batch, effect.getShortName(), x - 5 + (15 * j), y + 55);
    					}
    				}
    			}

    			try {
	    			if (BState == BSTATE_ACT && heroes[i].attacked) {
	    				if(currentSkill == null || !currentSkill.getID().equals("defend")) {
	    					game.mainFont.getData().setScale(0.3f);
	    					if(heroes[i].lastDamageTaken > 0) {
	    						if(currentTarget != null) {
	    							batch.draw(game.selection, x, y);
	    						}
		    					String temp;
		    					temp = (heroes[i].lastIsDamage) ? "-" : "+";
		    					String dmgText = "";
		    					if(currentSkill != null) {
		    						for(int k = 0; k < currentSkill.getAtkTimes(); k++) {
		    							dmgText += temp + heroes[i].lastDamageTaken + "\n";
		    						}
		    					}
		    					game.mainFont.setColor(0f, 0f, 0f, 1f);
		    					game.mainFont.draw(batch, dmgText, x + 10 - 0.4f, y + 50 + getDamageBounceY());
		    					game.mainFont.draw(batch, dmgText, x + 10 + 0.4f, y + 50 + getDamageBounceY());
		    					game.mainFont.draw(batch, dmgText, x + 10, y + 0.4f + 50 + getDamageBounceY());
		    					game.mainFont.draw(batch, dmgText, x + 10, y - 0.4f + 50 + getDamageBounceY());
		        				switch((int) (heroes[i].lastDamageWeakness * 100)) {
		        				case 100:
		        					game.mainFont.setColor(1f, 1f, 1f, 1f);
		        					break;
		        				case 150:
		        					game.mainFont.setColor(1f, 0.6f, 0.6f, 1f);
		        					break;
		        				case 200:
		        					game.mainFont.setColor(1f, 0.3f, 0.3f, 1f);
		        					break;
		        				case 400:
		        					game.mainFont.setColor(1f, 0f, 0f, 1f);
		        					break;
		        				case 75:
		        					game.mainFont.setColor(0.5f, 0.6f, 0.6f, 1f);
		        					break;
		        				case 50:
		        					game.mainFont.setColor(0.5f, 0.3f, 0.3f, 1f);
		        					break;
		        				case 25:
		        					game.mainFont.setColor(0.3f, 0.2f, 0.2f, 1f);
		        					break;
		        				default:
		        					game.mainFont.setColor(0.5f, 1f, 0.5f, 1f);
		        					break;
		        				}
		        				game.mainFont.draw(batch, dmgText, x + 10, y + 50 + getDamageBounceY());
		        				game.mainFont.setColor(1f, 1f, 1f, 1f);
	    					}
	        			}
	    			}
    			}catch(Exception e) {
    				
    			}
    		}
    	}

    	switch(BState) {
    	case BSTATE_BOSS_DIALOGUE:
    		break;

    	case BSTATE_TURN_START:
    		System.out.println("Turno empieza: " + turn);
    		currentBossEvent = null;
    		if(battle.isBoss) {
    			currentBossEvent = ((Boss) enemies[0]).checkEvents(game, turn, delta);
    		}
    		int count = (currentBossEvent != null) ? currentBossEvent.getMsgs().length : 0;
			updateEntities();
			if(currentBossEvent != null) {
				DialMan.addDialogue(count, DIAL_BOSSEVENT);
				DialMan.addDialogue(DIAL_BOSSEVENT, -1);
			}
			
			if(currentBossEvent == null) {
				BState = BSTATE_FIGHT_OR_FLEE;
			} else {
				BState = BSTATE_BOSS_DIALOGUE;
			}
			curr = 0;
			currChar = 0;
			currAct = 0;
			sel[0] = 0;
			sel[1] = 0;
			sel[2] = 0;
			sel[3] = 0;
    		break;

    	case BSTATE_FIGHT_OR_FLEE:
    		sel[0] = InputMan.scrollInt(MenuScrollType.VERTICAL, 2, sel[0], Menu.MenuMove);
    		game.batch.draw(game.fightOrFleeBox, 0, 170 - 16);
    		game.batch.draw(game.fightOrFleeBox, 0, 170 - 16 - 25);
    		Menu.showOptionsY(game, game.mainFont, 0.4f, 10, 170, 25, null, sel[0], "ATACAR", "HUIR");
    		if(InputMan.checkKey(Main.config.key_interact)) {
    			switch(sel[0]) {
    			case 0:
    				AState = ASTATE_IDLE;
    				BState = BSTATE_CHAR_CHOOSE;
    				break;
    			case 1:
    				if(!tried && !battle.isBoss) {
	    				if(rand.nextInt(100) < 50) {
	    					DialMan.addDialogue(0, DIAL_ESCAPE);
	    					DialMan.addDialogue(DIAL_ESCAPE, -1, null, null, "Has escapado!", 20, null);
	    					BState = BSTATE_BATTLE_END;
	    				} else {
	    					DialMan.addDialogue(0, -1, null, null, "No has podido escapar.", 20, null);
	    					tried = true;
	    				}
    				}
    				break;
    			}
    		}
    		break;

    	case BSTATE_CHAR_CHOOSE:
    		if (InputMan.checkKey(Main.config.key_alt)) {
    			if(inspectingEnemy) {
    				inspectingEnemy = false;
    			}else {
    				inspectingEnemy = true;
    				int first = getFirstUndefeatedEnemy();
    				if (first != -1) {
    					inspectingEnemy = true;
    					inspectEnemyIdx = first;
    					updateAndDrawEnemyInspect();
    				}
    			}
    		}
    		if (inspectingEnemy) {
    			updateAndDrawEnemyInspect();
    			if(InputMan.checkKey(Main.config.key_back)){
    				inspectingEnemy = false;
    			}
    		} else {
    			characterChoose();
				if(currChar >= heroes.length) {
					BState = BSTATE_ENEMY_CHOOSE;
					currAct = 0;
					makeAct = true;
				}
    		}
    		break;

    	case BSTATE_ENEMY_CHOOSE:
    		enemyChoose(currentBossEvent);
    		BState = BSTATE_ACT;
    		break;

    	case BSTATE_ACT:
    		act();
    		if(currAct >= actions.length) {
    			BState = BSTATE_TURN_END;
    		}
    		break;

    	case BSTATE_TURN_END:
    		System.out.println("Turno termina: " + turn);
    		tried = false;
    		currentAttacker = null;
    		currentTarget = null;
    		currentSkill = null;
    		currentItem = null;
    		
    		int dialCont = 0;
			for(int i = 0; i < entities.length; i++) {
				if(entities[i].hp > 0) {
					dialCont = entities[i].updateEffects(dialCont);
				}
			}
			
			int defeatedEnemies = 0;
			for(int i = 0; i < enemies.length; i++) {
				if(enemies[i].hp <= 0 && !enemies[i].hasState("DWN")) {
					enemies[i].setEffect(new Effect("DWN"), 0);
				}
				if(enemies[i].hp <= 0) {
					defeatedEnemies++;
					enemies[i].defeated = true;
				}
				Main.player.updateBestiary(enemies[i]);
			}
			
			if(defeatedEnemies == enemies.length) {
				int cont2 = 0;
				if(battle.isBoss) {
					Boss boss = (Boss) enemies[0];
					if(boss.finalEvent != null) {
						boss.finalEvent.checkBossEvent(game, boss, turn, delta);
						cont2 = boss.finalEvent.getMsgs().length;
						boss.activateFlag();
					}
				}
				DialMan.addDialogue(cont2, cont2+1, null, null, "Has ganado!", 20, null);
				cont2++;
				DialMan.addDialogue(cont2, cont2+1, null, null, "Obtienes " + gld + "G y " + xp + " XP.", 20, null);
				cont2++;
				int winCont = cont2;
				for(int i = 0; i < heroes.length; i++) {
					heroes[i].clearEffects();
					if(heroes[i].hp == 0) {
						heroes[i].hp++;
						continue;
					}
					heroes[i].xp += xp;
					winCont = heroes[i].checkLvl(winCont);
				}
				Main.player.gold += gld;
				DialMan.addDialogue(winCont, DIAL_VICTORY);
				DialMan.addDialogue(DIAL_VICTORY, -1);
				BState = BSTATE_BATTLE_END;
				win = true;
				break;
			}
			
			int defeatedHeroes = 0;
			for(int i = 0; i < heroes.length; i++) {
				if(heroes[i].hp <= 0 && !heroes[i].hasState("DWN")) {
					heroes[i].setEffect(new Effect("DWN"), 0);
				}
				if(heroes[i].hasState("DWN")) {
					heroes[i].hp = 0;
				}
				if(heroes[i].hp <= 0) {
					defeatedHeroes++;
				}
			}
			
			if(defeatedHeroes == heroes.length) {
				DialMan.addDialogue(0, DIAL_DEFEAT, null, null, "Has perdido.", 20, null);
				DialMan.addDialogue(DIAL_DEFEAT, -1);
				for(int i = 0; i < heroes.length; i++) {
					heroes[i].hp++;
					heroes[i].clearEffects();
				}
				BState = BSTATE_BATTLE_END;
				win = false;
				break;
			}
			
			DialMan.addDialogue(dialCont, DIAL_TURNEND);
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
    		for(Entity entity : entities) {
    			if(entity.hp <= 0 && entity instanceof Enemy) {
        			Enemy enemy = (Enemy) entity;
        			enemy.draw = false;
        		}
    			entity.attacked = false;
    		}
    		currAct++;
    		makeAct = true;
    		if (currAct >= actions.length) {
    			currentAttacker = null;
    			currentTarget = null;
    			currentSkill = null;
    			currentItem = null;
    		}
    		if(endTurn()) {
    			currAct = actions.length;
    		}
    		break;
    	case DIAL_DEFEAT:
    		Main.player.currScreen = "LOOSE";
    		game.setScreen(game.firstScreen);
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

    }

	private boolean isUndefeated(Enemy enemy) {
		if (enemy == null) return false;
		if (enemy.hp <= 0 || enemy.hasState("DWN")) return false;
		return true;
	}

	private int getFirstUndefeatedEnemy() {
		if (enemies == null) return -1;
		for (int i = 0; i < enemies.length; i++) {
			if (isUndefeated(enemies[i])) {
				return i;
			}
		}
		return -1;
	}

	private int getNextUndefeatedEnemy(int startIdx, int dir) {
		if (enemies == null || enemies.length == 0) return -1;
		int count = enemies.length;
		int curr = startIdx;
		for (int i = 0; i < count; i++) {
			curr = (curr + dir + count) % count;
			if (isUndefeated(enemies[curr])) {
				return curr;
			}
		}
		return -1;
	}

	private String getElemStr(double val, boolean[] weak, int idx, boolean enemyDiscovered) {
		if (weak != null && idx < weak.length && !weak[idx]) {
			return "?";
		}
		switch((int) (val * 100)) {
		case 100:
			return "NOM";
		case 150:
			return "MDE";
		case 200:
			return "DEB";
		case 400:
			return "SDE";
		case 75:
			return "MFU";
		case 50:
			return "FUE";
		case 25:
			return "SFU";
		default:
			return "NUL";
		}
	}

	private void updateAndDrawEnemyInspect() {

		int antSel = inspectEnemyIdx;
		int newSel = InputMan.scrollInt(MenuScrollType.HORIZONTAL, enemies.length, inspectEnemyIdx, Menu.MenuMove);
		if (newSel != antSel) {
			int dir = 1;
			if ((antSel == 0 && newSel == enemies.length - 1) || newSel < antSel) {
				if (!(antSel == enemies.length - 1 && newSel == 0)) {
					dir = -1;
				}
			}
			int nextValid = getNextUndefeatedEnemy(antSel, dir);
			if (nextValid != -1) {
				inspectEnemyIdx = nextValid;
			}
		}

		if (inspectEnemyIdx < 0 || inspectEnemyIdx >= enemies.length || !isUndefeated(enemies[inspectEnemyIdx])) {
			int first = getFirstUndefeatedEnemy();
			if (first != -1) {
				inspectEnemyIdx = first;
			} else {
				inspectingEnemy = false;
				return;
			}
		}

		Enemy enemy = enemies[inspectEnemyIdx];
		batch.draw(game.dialogueBBox, 0, 0, 320, 180);
		float sectionWidth = 320f / enemies.length;
		float x = sectionWidth * inspectEnemyIdx + sectionWidth / 2f - 50f;
		float y = 50f;
		batch.draw(enemy.getTexture(), x, y, 100, 100);
		batch.draw(game.selection, x + 25f, y + 50f);
		
		game.mainFont.getData().setScale(0.3f);
		game.mainFont.setColor(1f, 1f, 1f, 1f);
		game.mainFont.draw(batch, enemy.getName(), 10, 175, 100, Align.center, true);
		float x2 = 115;
		float num = (90f * enemy.hp / enemy.getHP());
		game.batch.draw(game.HPbar, x2, 165, num, 5);
		game.mainFont.getData().setScale(0.15f);
		game.mainFont.draw(batch, Integer.toString(enemy.hp), x2 + num - 5, 165);
		game.mainFont.draw(batch, Integer.toString(enemy.getHP()), x2 + 90 - 5, 175);
		game.batch.draw(game.battleBar, x2, 165);
		Effect[] effects = enemy.getEffects();
		for(int j = 0; j < effects.length; j++) {
			Effect effect = effects[j];
			if(effect != null) {
				game.invFont.getData().setScale(0.2f);
				game.invFont.draw(batch, effect.getShortName(), x2 + (20 * j), 155);
			}
		}
		
		game.invFont.getData().setScale(0.15f);
		game.invFont.setColor(1f, 1f, 1f, 1f);
		String ATK = (enemy.defeated) ? "" + enemy.getATK() : "?";
		String DEF = (enemy.defeated) ? "" + enemy.getDEF() : "?";
		String MAT = (enemy.defeated) ? "" + enemy.getMAT() : "?";
		String MDF = (enemy.defeated) ? "" + enemy.getMDF() : "?";
		String SPD = (enemy.defeated) ? "" + enemy.getSPD() : "?";
		String statsStr = "ATK:" + ATK + " DEF:" + DEF + "\nMAT:" + MAT + " MDF:" + MDF + "\nSPD:" + SPD;
		game.invFont.draw(batch, statsStr, 210, 170, 110, Align.center, false);
		game.invFont.getData().setScale(0.3f);
		boolean[] weak = enemy.getWeaknesses();
		String elemStr = "PHY:" + getElemStr(enemy.getPHY(), weak, enemy.WEAK_PHY, enemy.discovered)
				+ " RAN:" + getElemStr(enemy.getRAN(), weak, enemy.WEAK_RAN, enemy.discovered)
				+ " FIR:" + getElemStr(enemy.getFIR(), weak, enemy.WEAK_FIR, enemy.discovered)
				+ " WAT:" + getElemStr(enemy.getWAT(), weak, enemy.WEAK_WAT, enemy.discovered)
				+ " WIN:" + getElemStr(enemy.getWIN(), weak, enemy.WEAK_WIN, enemy.discovered)
				+ " EAR:" + getElemStr(enemy.getEAR(), weak, enemy.WEAK_EAR, enemy.discovered);
		game.invFont.draw(batch, elemStr, 5, 20, 320, Align.center, false);

		if (enemy.getDesc() != null && enemy.defeated) {
			int x3 = (newSel > enemies.length / 2 - 1) ? 5 : 215;
			game.invFont.getData().setScale(0.15f);
			game.invFont.draw(batch, enemy.getDesc(), x3, 140, 100, Align.center, true);
		}
	}

    private void updateEntities() {
		for(int i = 0; i < enemies.length; i++) {
			enemies[i].prot = 1;
			if(enemies[i].hasState("DWN")) {
				enemies[i].hp = 0;
			}
			if((turn % 2) == 0 ) {
				enemies[i].modMP((int) (enemies[i].getMP() * 0.2 + 10));
				enemies[i].modSP((int) (enemies[i].getSP() * 0.2 + 10));
			}
		}
		for(int i = 0; i < heroes.length; i++) {
			heroes[i].prot = 1;
			if(heroes[i].hasState("DWN")) {
				heroes[i].hp = 0;
			}
		}
		for(int i = 0; i < actions.length; i++) {
			actions[i] = null;
			actionsObj[i][0] = null;
			actionsObj[i][1] = null;
			actionsObj[i][2] = null;
			actionsItem[i] = null;
		}
	}

	private void act() {
    	if(!makeAct || currAct >= actions.length) {
    		return;
    	}
    	makeAct = false;

    	if(currAct == 0) {
	    	for(int i = 0; i < actions.length; i++) {
				for(int j = 0; j < actions.length-i-1; j++) {
					if (actions[j] == null && actions[j + 1] != null) {
					    Skill tempSkill = actions[j];
					    actions[j] = actions[j + 1];
					    actions[j + 1] = tempSkill;

					    Item tempItem = actionsItem[j];
					    actionsItem[j] = actionsItem[j + 1];
					    actionsItem[j + 1] = tempItem;

					    Entity[] tempObj = actionsObj[j];
					    actionsObj[j] = actionsObj[j + 1];
					    actionsObj[j + 1] = tempObj;

					} else if (actions[j] != null && actions[j + 1] != null) {
					    int spd1 = actionsObj[j][0].getSPD() + actions[j].SPD;
					    int spd2 = actionsObj[j + 1][0].getSPD() + actions[j + 1].SPD;

					    if (spd1 < spd2) {
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

		currentAttacker = actionsObj[currAct][0];
		currentTarget = actionsObj[currAct][1];
		currentSkill = actions[currAct];
		currentItem = actionsItem[currAct];
		hitShakeTimer = 0.4f;

		Skill skill = actions[currAct];
		if(actionsItem[currAct] == null) {
			if (skill != null && actionsObj[currAct][0].hp > 0) {
				if (skill.getSkillType() == 1) {
				    Entity target = actionsObj[currAct][1];
				    
				    if (target != null && (target.hp <= 0 || target.hasState("DWN"))) {
				        Entity newObj = null;
				        if (actionsObj[currAct][0] instanceof Enemy) {
				            do {
				            	newObj = heroes[rand.nextInt(heroes.length)];
				            	if(newObj.hp > 0) {
				            		break;
				            	}
				            }while(true);
				        } else if (actionsObj[currAct][0] instanceof Hero) {
				        	do {
				            	newObj = enemies[rand.nextInt(enemies.length)];
				            	if(newObj.hp > 0) {
				            		break;
				            	}
				            }while(true);
				        }

				        actionsObj[currAct][1] = newObj;
				        currentTarget = newObj;
				    }
				}
				if (skill.getSkillType() == 1 && actionsObj[currAct][1] == null) {
				    makeAct = true;
				    currAct++;
				    return;
				}
				switch(skill.getSkillType()) {
				case 0: // self
					skill.use(actionsObj[currAct][0]);
					break;
				case 1: // to enemy
					if(actionsObj[currAct][0].hasState("CON")) {
						if(rand.nextInt(2) == 0) {
							currentTarget = actionsObj[currAct][0];
							skill.use(actionsObj[currAct][0], actionsObj[currAct][0]);
							return;
						}
					}
					skill.use(actionsObj[currAct][0], actionsObj[currAct][1]);
					break;
				case 2: // to enemies
					if(actionsObj[currAct][0].hasState("CON")) {
						int random = rand.nextInt(2);
						if(random == 0) {
							if(actionsObj[currAct][0].getClass() == Hero.class) {
								skill.use(actionsObj[currAct][0], heroes);
							} else {
								skill.use(actionsObj[currAct][0], enemies); 
							}
						}
					}
					if(actionsObj[currAct][0].getClass() == Hero.class) {
						skill.use(actionsObj[currAct][0], enemies);
					} else {
						skill.use(actionsObj[currAct][0], heroes); 
					}
					break;
				case 3: // to ally
					if(actionsObj[currAct][0].hasState("CON")) {
						if(rand.nextInt(2) == 0) {
							skill.use(actionsObj[currAct][0], actionsObj[currAct][0]);
							return;
						}
					}
					skill.use(actionsObj[currAct][0], actionsObj[currAct][1]);
					break;
				case 4: // to allies
					if(actionsObj[currAct][0].hasState("CON")) {
						int random = rand.nextInt(2);
						if(random == 0) {
							if(actionsObj[currAct][0].getClass() == Hero.class) {
								skill.use(actionsObj[currAct][0], enemies);
							} else {
								skill.use(actionsObj[currAct][0], heroes); 
							}
						}
					}
					if(actionsObj[currAct][0].getClass() == Hero.class) {
						skill.use(actionsObj[currAct][0], heroes);
					} else {
						skill.use(actionsObj[currAct][0], enemies); 
					}
					break;
				case 5: // To enemy and ally
					if(actionsObj[currAct][0].hasState("CON")) {
						int random = rand.nextInt(2);
						if(random == 0) {
							Entity temp = actionsObj[currAct][1];
							actionsObj[currAct][1] = actionsObj[currAct][0];
							actionsObj[currAct][0] = temp;
							
						}
					}
					skill.use(actionsObj[currAct][0], actionsObj[currAct][1], actionsObj[currAct][2]);
					break;
				case 6: // to all entities
					skill.use(actionsObj[currAct][0], entities);
					break;
				}
			} else {
				makeAct = true;
				currAct++;
			}
		} else {
			if (actionsObj[currAct][0].hp > 0 && actionsObj[currAct][1] != null && actionsObj[currAct][1].hp > 0) {
		        skill.setType(actionsItem[currAct].getType());
		        skill.use(actionsObj[currAct][0], actionsObj[currAct][1], Main.player, actionsItem[currAct]);
		    } else {
		        makeAct = true;
		        currAct++;
		    }
		}
	}

	private boolean endTurn() {
		int count = 0;
		int count2 = 0;
		for(Enemy enemy : enemies) {
			if(enemy.hp <= 0) {
				count++;
				enemy.defeated = true;
			}
		}
		for(Hero hero : heroes) {
			if(hero.hp <= 0) {
				count2++;
			}
		}
		if(count >= enemies.length || count2 >= heroes.length) {
			return true;
		}
		return false;
	}

	private void enemyChoose(BossEvent event) {
		for(int i = 0; i < enemies.length; i++) {
			if(enemies[i].hp <= 0 || enemies[i].hasState("SLE")) {
				actions[curr] = null;
				curr++;
				continue;
			}
			
			// Si la entidad es el Boss principal y hay un BossEvent activo
			if(enemies[i] instanceof Boss && event != null && event.getSkill() != null) {
				actions[curr] = event.getSkill();
			} else {
				actions[curr] = enemyAct(enemies[i]);
			}
			
			actionsObj[curr][0] = enemies[i];
			actionsObj[curr][1] = selEnObj(actions[curr], enemies[i]);
			curr++;
		}
	}
    
    private Entity selEnObj(Skill skill, Enemy enemy) {
    	if(skill == null) {
			return null;
		} else {
			int sel;
			switch(skill.getSkillType()) {
			case 0:
				return enemy; 
			case 1:
				do {
					if(!enemy.hasState("RAG")) {
						sel = rand.nextInt(heroes.length);
						if(heroes[sel].hp <= 0) {
							continue;
						}
					} else {
						sel = rand.nextInt(entities.length);
						if(entities[sel].hp <= 0 || entities[sel] == enemy) {
							continue;
						} else {
							return entities[sel];
						}
					}
					break;
				} while(true);
				return heroes[sel];
			case 2:
				return null;
			case 3:
				do {
					if(!enemy.hasState("RAG")) {
						sel = rand.nextInt(enemies.length);
						if(enemies[sel].hp <= 0) {
							continue;
						}
					} else {
						sel = rand.nextInt(entities.length);
						if(entities[sel].hp <= 0) {
							continue;
						} else {
							return entities[sel];
						}
					}
					break;
				} while(true);
				return enemies[sel];
			case 4:
				return null;
			default:
				return null;
			}
		}
	}

    private Skill enemyAct(Enemy enemy) {
        Skill[] skills = enemy.getSkills();
        if (skills == null || skills.length == 0) return null;

        if (enemy.hasState("SIL")) {
            return skills[rand.nextInt(2)];
        }

        int temp = rand.nextInt(100);
        if (temp < 50) {
            return skills[0]; 
        } else if (temp < 80) {
            if (skills.length > 2) {
                int tried = 10;
                while (tried > 0) {
                    int sel = 2 + rand.nextInt(skills.length - 2);
                    if (skills[sel] != null && enemy.mp >= skills[sel].getMP() && enemy.sp >= skills[sel].getSP()) {
                        return skills[sel];
                    }
                    tried--;
                }
            }
            return skills[0];
        } else {
            return skills[1];	
        }
    }
    
	private void characterChoose() {
    	Hero hero = heroes[currChar];
    	while(hero.hp <= 0 || hero.hasState("DWN") || hero.hasState("SLE") || hero.hasState("RAG")) {
	    	if(hero.hp <= 0 || hero.hasState("DWN") || hero.hasState("SLE")) {
	    		curr++;
	    		currChar++;
	    	} else if(hero.hasState("RAG")) {
	    		actions[curr] = hero.getSkills()[0];
	    		actionsObj[curr][0] = hero;
	    		Entity entity;
	    		do {
	    			entity = entities[rand.nextInt(entities.length)];
	    			if(entity == actionsObj[curr][0] || entity.hp <= 0)
	    				continue;
	    			break;
	    		} while(true);
	    		actionsObj[curr][1] = entity;
	    		curr++;
	    		currChar++;
	    	}
	    	if(currChar >= 4) {
	    		BState = BSTATE_ENEMY_CHOOSE;
	    		return;
	    	} else {
	    		hero = heroes[currChar];
	    	}
    	}
    	Menu.showOptionsX(game, game.mainFont, 0.2f, 10, 70, 80, null, sel[1], "ATACAR", "DEFENDER", "HABILIDADES", "INVENTARIO");
    	Menu.showBattleBars(game, heroes[currChar]);
    	switch (AState) {
		case ASTATE_IDLE:
			actionsItem[curr] = null;
			sel[1] = InputMan.scrollInt(MenuScrollType.HORIZONTAL, 4, sel[1], Menu.MenuMove);
			sel[2] = 0;
    		if(InputMan.checkKey(Main.config.key_interact)) {
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
    		} else if(InputMan.checkKey(Main.config.key_back) && currChar > 0) {
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
	    				
	    				if (curr >= 0) {
		    				actions[curr] = null;
		    				actionsObj[curr][0] = null;
		    				actionsObj[curr][1] = null;
		    				actionsObj[curr][2] = null;
		    				actionsItem[curr] = null;
	    				}
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
			sel[2] = InputMan.scrollInt(MenuScrollType.VERTICAL, skills.length, sel[2], Menu.MenuMove);
			game.batch.draw(game.battleMenu, 0, 10);
			Menu.showBSkills(game, sel[2], skills, hero);
			if(InputMan.checkKey(Main.config.key_interact)) {
				if(skills[sel[2]].getMP() <= hero.mp && skills[sel[2]].getSP() <= hero.sp) {
					AState = ASTATE_SELECT_OBJECTIVE;
					actions[curr] = skills[sel[2]];
					if(skills[sel[2]].getSkillType() == 3) {
						sel[2] = currChar;
					} else {
						sel[2] = 0;
					}
				}
			} else if(InputMan.checkKey(Main.config.key_back)) {
				AState = ASTATE_IDLE;
			}
			break;

		case ASTATE_INVENTORY:
			Item[] items = Main.player.getInventory();
			sel[2] = InputMan.scrollInt(MenuScrollType.VERTICAL, items.length, sel[2], Menu.MenuMove);
			game.batch.draw(game.battleMenu, 0, 10);
			Menu.showBInventory(game, sel[2], items);
			if(InputMan.checkKey(Main.config.key_interact)) {
					AState = ASTATE_SELECT_OBJECTIVE;
					actions[curr] = hero.getSkills()[2];
					actionsItem[curr] = items[sel[2]];
					actions[curr].setType(actionsItem[curr].getType());
					sel[2] = currChar;
			} else if(InputMan.checkKey(Main.config.key_back)) {
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
					sel[2] = InputMan.scrollInt(MenuScrollType.HORIZONTAL, enemies.length, sel[2], Menu.MenuMove);
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
					if(InputMan.checkKey(Main.config.key_interact)) {
						actionsObj[curr][1] = enemies[sel[2]]; 
						AState = ASTATE_NEXT;
					} else if(InputMan.checkKey(Main.config.key_back)) {
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
				sel[2] = InputMan.scrollInt(MenuScrollType.HORIZONTAL, heroes.length, sel[2], Menu.MenuMove);
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
				if(InputMan.checkKey(Main.config.key_interact)) {
					actionsObj[curr][1] = heroes[sel[2]]; 
					AState = ASTATE_NEXT;
				} else if(InputMan.checkKey(Main.config.key_back)) {
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
					sel[2] = InputMan.scrollInt(MenuScrollType.HORIZONTAL, heroes.length, sel[2], Menu.MenuMove);
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
					if(InputMan.checkKey(Main.config.key_interact)) {
						actionsObj[curr][1] = heroes[sel[2]]; 
					} else if(InputMan.checkKey(Main.config.key_back)) {
						AState = ASTATE_IDLE;
					}
					selectAObjective(sel[2]);
				} else {
					antsel = sel[2];
					sel[2] = InputMan.scrollInt(MenuScrollType.HORIZONTAL, enemies.length, sel[2], Menu.MenuMove);
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
					if(InputMan.checkKey(Main.config.key_interact)) {
						actionsObj[curr][2] = enemies[sel[2]]; 
						AState = ASTATE_NEXT;
					} else if(InputMan.checkKey(Main.config.key_back)) {
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
        if(width <= 0 || height <= 0) return;
        viewport.update(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    public void updateGame(Main game) {
    	this.game = game;
    }

    @Override
    public void hide() {
    	Main.updateArrays();
    	game.lastScreen = this;
    }

    @Override
    public void dispose() {
    	game.lastScreen = this;
    }
}