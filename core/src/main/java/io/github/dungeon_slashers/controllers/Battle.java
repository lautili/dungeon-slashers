package io.github.dungeon_slashers.controllers;
import java.util.Random;

import io.github.dungeon_slashers.Effect;
import io.github.dungeon_slashers.Main;
import io.github.dungeon_slashers.Player;
import io.github.dungeon_slashers.Skill;
import io.github.dungeon_slashers.entities.Boss;
import io.github.dungeon_slashers.entities.BossEvent;
import io.github.dungeon_slashers.entities.Enemy;
import io.github.dungeon_slashers.entities.Entity;
import io.github.dungeon_slashers.entities.Hero;
import io.github.dungeon_slashers.item.Item;
/*
		CLASE BATTLE

será la clase encargada de controlar las batallas.

*/
public class Battle {
	
	//todo lo necesario
	private Player player;
	private Enemy[] possibleEnemy; //lista de enemigos posibles
	private Enemy[] enemies; //enemigos que realmente usar� la batalla
	private Entity[] entities; //todas las entidades
	private Hero[] heroes;
	private boolean isBoss; //(si es un jefe o no)
	private int turn;
	private Skill[] actions; //las acciones
	private Entity[][] actionsObj; //los objetivos de las acciones.
	private Item[] actionsItem; //en caso de que se use un item en la batalla
	private transient Random rand; //Random para aleatoriedades
	private int xp; //xp total
	private int gld; //oro total
	private int quant; //cantidad de enemigos posibles en el combate
		
	public Battle(boolean isBoss, int quant, Enemy...enemies) {
		this.possibleEnemy = enemies;
		this.isBoss = isBoss;
		this.quant = quant;
		rand = new Random();
	}
	
	//el controlador general de la batalla
	public void battle(Main game, float delta) {
			player = Main.player;
			heroes = player.getCharacters();
			initEnemies();
			turn = 0;
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
			String msg = ("Comienza una batalla contra ");
			for(int i = 0; i < enemies.length; i++) {
				msg += (enemies[i].getName());
				if(i == (enemies.length - 2)) {
					msg += (" y ");
				}else if(i == (enemies.length - 1)){
					msg+=("!\n");
				}else {
					msg+=(", ");
				}
				Menu.msg(msg);
			}
			boolean end = false;
			boolean tried = false;
			//la batalla
			do{
				//actualiza cosas importantes de las entidades, tanto enemigos como heroes
				BossEvent event = null; //no se usara si la pelea no es bossfight
				updateEntities();
				Menu.msg("\n");
				if(isBoss) {
					event = ((Boss) enemies[0]).checkEvents(game, turn, delta);
				}
				int cont = 0;
				for(int i = 0; i < enemies.length; i++) {
					
					if(enemies[i].hp == 0) {
						cont++; //contador para ver si todos los enemigos murieron
					}
				}
				if(cont == enemies.length) {
					win();
					end = true;
				}
				cont = 0;
				for(int i = 0; i < heroes.length; i++) {
					cont = 0;
					if(heroes[i].hp == 0) {
						cont++; //contador para ver si todos los heroes murieron
					}
					if(cont == heroes.length) {
						lose();
						end = true;
					}
				}
				if(end) {
					if(isBoss) {
						((Boss) enemies[0]).setDefeat();
					}
					enemies = null;
					break;
				} //si la batalla termino, terminar el ciclo
				Menu.battleStats(heroes, enemies);
				Menu.msg("TURNO "+ (turn+1) + ". �Que hacer?\n1. Pelear\n2. Escapar");
				int opt = InputMan.scanInt(1, 3);
				switch (opt){
				case 1: //Pelear
					tried = false;
					//acciones heroes
					for(int i = 0; i < heroes.length; i++) { 
						if(heroes[i].hp <= 0 || heroes[i].hasState("SLE")) { //si el heroe esta muerto o dormido, continua el ciclo
							actions[i] = null;
							continue;
						}
						actions[i] = charMenu(heroes[i], i);
						if(actions[i] == null && i > 0) { 
							i-=2;
							continue;
						}
						actionsObj[i][0] = heroes[i]; //asigna el perpetuador
						if(actionsItem[i] != null) {
							actions[i].setType(actionsItem[i].getType());
						}
						actionsObj[i][0] = heroes[i];
						actionsObj[i][1] = selectObj(actions[i], heroes[i], i); //asigna el/los objetivos
					}
					//acciones de enemigos
					for(int i = heroes.length; i < heroes.length+enemies.length; i++) {
						int i2 = i - heroes.length;
						if(enemies[i2].hp <= 0 || enemies[i2].hasState("SLE")) { //si esta muerto o dormido, continua el ciclo
							actions[i] = null;
							continue;
						}
						if(event == null || event.getSkill() == null) {
							actions[i] = enemyAct(enemies[i2]);
						}else {
							actions[i] = event.getSkill();
						}
						actionsObj[i][0] = enemies[i2];
						actionsObj[i][1] = selEnObj(actions[i], enemies[i2]); //objetivos
					}
					act(); //lleva a cabo todas las acciones
					break;
				case 2:
					if(!tried) { //si no se intent� ya escapar
						if(rand.nextInt(2)==0) { // 50/50 de escapar
							Menu.msg("Has escapado!"); 
							end = true;
						}else {
							Menu.msg("No pudiste escapar.");
							tried = true;
							continue;
						}
					}else {
						Menu.msg("No puedes volver a intentar a escapar en el mismo turno.");
						continue;
					}
					break;
				}
				turn++;
			}while(true);
	}
	
	//si gana
	private void win() {
		Menu.msg("�Has ganado! consigues " + xp + " xp y " + gld + "G.");
		for(int i = 0; i < heroes.length; i++) {
			heroes[i].clearEffects();
			if(heroes[i].hp == 0) { //si el heroe esta muerto, lo pone a 1 de vida
				heroes[i].hp++;
				continue;
			}
			heroes[i].xp+=xp; //le suma la xp solo a los heroes que no murieron
			heroes[i].checkLvl();
		}
		player.gold+=gld;
	}
	
	private void lose() {
		Menu.msg("Perdiste.");
		for(int i = 0; i < heroes.length; i++) {
			heroes[i].hp++;
			heroes[i].clearEffects();
		}
	}

	private void updateEntities() {
		// actualiza las entidades al principio de cada turno
		for(int i = 0; i < enemies.length; i++) {
			enemies[i].prot = 1;
			if(enemies[i].hp <= 0 && !enemies[i].hasState("DWN")) {
				enemies[i].setEffect(new Effect("DWN")); //si tiene la vida en 0, le pone el efecto caido
			}
			if((turn % 2) == 0 ) { //solo en turnos pares
				enemies[i].modMP( (int) (enemies[i].getMP() * 0.2 + 10)); //recupera un poco el mana de los enemigos
				enemies[i].modSP( (int) (enemies[i].getSP() * 0.2 + 10)); //recupera un poco la stamina de los enemigos
				//esto ultimo para asegurar de que nunca se queden sin hacer habilidades.
			}
			enemies[i].updateEffects(); //actualiza los efectos de las entidades
		}
		for(int i = 0; i < heroes.length; i++) {
			heroes[i].prot = 1;
			if(heroes[i].hp <= 0 && !heroes[i].hasState("DWN")) {
				
				heroes[i].setEffect(new Effect("DWN")); //si tiene la vida en 0, le pone el efecto caido
			}
			heroes[i].updateEffects(); //actualiza los efectos de las entidades
		}
		for(int i = 0; i < actions.length; i++) { //limpia los arrays
			actions[i] = null;
			actionsObj[i][0] = null;
			actionsObj[i][1] = null;
			actionsItem[i] = null;
		}
	}

	//acciones y seleccion de acciones de los enemigos
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
							continue; //siempre y cuando no eleccione a un heroe muerto, el ciclo termina
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


	//menu de acciones para los personajes
	private Skill charMenu(Hero hero, int i){
		if(!hero.hasState("RAG")) {
			do {
				Menu.showBattleOptions("Que hara " + hero.getName(), heroes, hero);
				int opt = 0;
				if(hero != heroes[0]) {
					opt = InputMan.scanInt(1, 5); //Si no es el primer heroe, permite volver atras
				}else {
					opt = InputMan.scanInt(1, 4);
				}
				switch(opt) {
				case 1: //Ataque comun
					return hero.getSkills()[0];
				case 2: //Defenderse
					return hero.getSkills()[1];
				case 3:
					if(hero.hasState("SIL")) {
						Menu.msg("No puede usar habilidades, esta silenciado!");
						continue;
					}
					Skill skill = selectSkill(hero);
					if(skill == null) {
						continue;
					}
					return skill;
				case 4:
				    Item item = itemsInventory(hero);
				    if(item == null) {
				    	continue;
				    }
				    actionsItem[i] = item;
					return hero.getSkills()[2];
				case 5:
					break;
				default:
					continue;
				}
				break;
			}while(true);
		}else {
			return hero.getSkills()[0];
		}
		return null;
	}
	
	
	private Skill selectSkill(Hero hero) {
		Skill[] skills = hero.getSkills();
		do {
		int opt = InputMan.scanInt(skills.length - 2);
		if(opt == 0) {
			return null;
		}
		opt += 2;
		if(skills[opt].getSP() <= hero.sp && skills[opt].getMP() <= hero.mp) {
		return skills[opt];
		}else {
			Menu.msg("Mana / Stamina insuficiente.");
		}
		}while(true);
	}

	// seleccion de objetivos
	private Entity selectObj(Skill skill, Entity e, int i2) {
		if(e.hasState("RAG")) {
			do {
			Entity choice = entities[rand.nextInt(entities.length)];
			if(choice == e) {
				continue;
			}
			return choice;
			}while(true);
		}
		int opt;
		switch(skill.getSkillType()) {
		case 0: // Self
			return e;
		case 1: // to Enemy
			if(enemies.length > 1) {
				Menu.showObjBatOptions("Seleccione el objetivo: ", false, enemies);
				do {
					opt = InputMan.scanInt(enemies.length);
					opt--;
					if(enemies[opt].hp > 0) {
						return enemies[opt];
					}else {
						Menu.msg("Elija un objetivo no muerto.");
					}
				}while(true);
			}else {
				return enemies[0];
			}
		case 2: // to Enemies
			return null; //lo maneja act()
		case 3: // to Ally
			Menu.showObjBatOptions("Seleccione el objetivo: ", false, heroes);
			do {
				opt = InputMan.scanInt(heroes.length);
				opt--;
				if(heroes[opt].hp > 0) {
					return heroes[opt];
				}else {
					Menu.msg("Este heroe esta caido. elija una opcion valida.");
				}
			}while(true);
		case 4: // to Allies
			return null; //lo maneja act()
		case 5: // To Enemy and Ally
			Menu.showObjBatOptions("Seleccione el heroe: ", false, heroes);
			do {
				opt = InputMan.scanInt(heroes.length - 1);
				if(heroes[opt].hp > 0) {
					actionsObj[i2][2] = heroes[opt];
					break;
				}else {
					Menu.msg("Este heroe esta caido. elija una opcion valida.");
				}
			}while(true);
			if(enemies.length > 1) {
				Menu.showObjBatOptions("Seleccione el objetivo: ", false, enemies);
				do {
					opt = InputMan.scanInt(enemies.length - 1);
					if(enemies[opt].hp > 0) {
						return enemies[opt];
					}else {
						Menu.msg("Elija un objetivo no muerto.");
					}
				}while(true);
			}else {
				return enemies[0];
			}
		case 6:
			return null;
		default:
			return null;
		}
	}

	//lleva a cabo todas las acciones 
		private void act() {
			
			//simple bubblesort para ordenar las acciones por velocidad
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
			
			//de esta forma las acciones se ordenan por quien actuara primero segun su velocidad
			//	y la velocidad de la accion.
			
			Menu.msg("\n_____________________________\n");
			for(int i = 0; i < actions.length; i++) {
				if(actions[i] != null && actionsObj[i][0].hp > 0) { //si la entidad no esta muerta:
					
					Skill skill = actions[i]; //guarda la skill en una variable
					
					if(actionsItem[i] == null) {
						if(skill != null) {
						switch(skill.getSkillType()) {
						case 0: //self
							skill.use(actionsObj[i][1]);
							break;
						case 1: //to enemy
							if(actionsObj[i][0].hasState("CON")) {
								if(rand.nextInt(2) == 0) {
								skill.use(actionsObj[i][0],actionsObj[i][0]);
								continue;
								}
							}
								skill.use(actionsObj[i][0],actionsObj[i][1]);
							break;
						case 2: //to enemies
							if(actionsObj[i][0].hasState("CON")) {
								int random = rand.nextInt(2);
								if(random == 0) {
									if(actionsObj[i][0].getClass() == Hero.class) { //si lo usa un heroe, lo usa en heroes
										skill.use(actionsObj[i][0], heroes);
									}else { //si no (es un enemigo), la usa en heroes
										skill.use(actionsObj[i][0], enemies); 
									}
									continue;
								}
							}
								if(actionsObj[i][0].getClass() == Hero.class) { //si lo usa un heroe, lo usa en enemigos
									skill.use(actionsObj[i][0], enemies);
								}else { //si no (es un enemigo), la usa en heroes
									skill.use(actionsObj[i][0], heroes); 
								}
							break;
						case 3: //to ally
							if(actionsObj[i][0].hasState("CON")) {
								if(rand.nextInt(2) == 0) {
									skill.use(actionsObj[i][0], actionsObj[i][0]);
								}
							}
							skill.use(actionsObj[i][0], actionsObj[i][1]);
							break;
						case 4: //to allies
							if(actionsObj[i][0].hasState("CON")) {
								int random = rand.nextInt(2);
								if(random == 0) {
									if(actionsObj[i][0].getClass() == Hero.class) { //si lo usa un heroe, lo usa en heroes
										skill.use(actionsObj[i][0], enemies);
									}else { //si no (es un enemigo), la usa en heroes
										skill.use(actionsObj[i][0], heroes); 
									}
									continue;
								}
							}
								if(actionsObj[i][0].getClass() == Hero.class) { //si lo usa un heroe, lo usa en enemigos
									skill.use(actionsObj[i][0], heroes);
								}else { //si no (es un enemigo), la usa en heroes
									skill.use(actionsObj[i][0], enemies); 
								}
							break;
						case 5: //To enemy and ally
							if(actionsObj[i][0].hasState("CON")) {
								int random = rand.nextInt(2);
								if(random == 0) {
									Entity temp = actionsObj[i][1];
									actionsObj[i][1] = actionsObj[i][0];
									actionsObj[i][0] = temp;
								}
							}
									skill.use(actionsObj[i][0], actionsObj[i][2], actionsObj[i][1]);
							break;
						case 6: //to all entities
							skill.use(actionsObj[i][0], entities);
							break;
						}
						}
					}else {
						//solo hace esto si la accion usa un item
						skill.setType(actionsItem[i].getType());
						skill.use(actionsObj[i][0], actionsObj[i][1], player, actionsItem[i]);
					}
				}
			}
			Menu.msg("_____________________________\n");
		}
	
		
		
	//Inicializa a los enemigos
	private void initEnemies() {
		if(!isBoss) {
		xp = 0;
		gld = 0;
			enemies = new Enemy[rand.nextInt(quant) + 1]; //genera la cantidad de enemigos. varia del 1 al 3
			for(int i = 0; i < enemies.length; i++) {
				if(possibleEnemy.length > 1) {
					enemies[i] = getRandomEnemy();
				}else {
					enemies[i] = new Enemy(possibleEnemy[0]);
				}
				xp+=enemies[i].getXP();
				gld+=enemies[i].getGLD();
			}
		}else {
			enemies = possibleEnemy;
			xp = possibleEnemy[0].xp;
			gld = possibleEnemy[0].gld;
		}
	}
	
	//funcion para calcular a los enemigos randoms por su prioridad
	private Enemy getRandomEnemy() {
		 int total = 0;

		 // Suma todas las probabilidades (aunque siempre son 100, por las dudas)
		 for (int i = 0; i < possibleEnemy.length; i++) {
		     total += possibleEnemy[i].getPriority();
		 }

		 int r = rand.nextInt(total);

		 int acum = 0;

		 // Busca en que rango cayo el numero
		 for (int i = 0; i < possibleEnemy.length; i++) {
		     acum += possibleEnemy[i].getPriority(); //va sumando las posibilidades de cada enemigo para ver 
		     										//	si r cayo en alguno

		     if (r < acum) {
		         return new Enemy(possibleEnemy[i]); //si r finalmente es menor que acum, devuelve a ese enemigo
		     }
		 }
		 return null;
	}
	
	//controlador para el inventario (Work in Progress)
	public Item itemsInventory(Hero hero) {
		Item[] inventory = player.getInventory();
		if(inventory.length > 0) {
				//Muestra los items y ejecuta un simple codigo para mostrar todas las opciones
				Menu.showItemBatOptions("Elija el item que desea usar: ", true, inventory);
				do {
					//si la opcion es 0, sale del inventario. Si no, ejecuta las otras opciones
					int opt = InputMan.scanInt(0, inventory.length);
					if(opt == 0) {
						Menu.msg("Saliendo del inventario...");
						return null;
					}else {
						opt--;
						return inventory[opt];
					}
				}
				while(true);
		}else {
			Menu.msg("No tiene items en su inventario.");
			
			return null;
		}
	}
}
