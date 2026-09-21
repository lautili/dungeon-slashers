package io.github.dungeon_slashers;

import java.util.Random;

import io.github.dungeon_slashers.controllers.DialMan;
import io.github.dungeon_slashers.controllers.Menu;
import io.github.dungeon_slashers.entities.Entity;
import io.github.dungeon_slashers.entities.Hero;
import io.github.dungeon_slashers.item.Item;
import io.github.dungeon_slashers.screens.BattleScreen;

/*
		CLASE SKILL

	será la clase encargada de controlar las habilidades y ataques en general.

*/

public class Skill {
	//valores
	private String shortName; //nombre corto, para hacer mas facil el switch del Use
	private String name;
	private String desc;
	private int MPcost;
	private int SPcost;
	private boolean menu; // Si se puede usar en el menu
	private String type; // El elemento del ataque (PHY, RAN, FIR, WIN, WAT, EAR, NONE, UNI). dejar vacio para elemento del caster
	private int atkTimes; //Las veces que ataca
	public int SPD;
	private String atkMsg;
	private transient Random rand = new Random();
	private int lvl;
	// skillType sera el tipo de la skill. cada numero determina una cosa distinta:
	// 0: de Entidad a ella misma
	// 1: de Entidad a Entidad enemiga
	// 2: de Entidad a Entidades enemigas
	// 3: de Entidad a Entidad aliada
	// 4: de Entidad a Entidades aliadas
	// 5: de Entidad a Aliado y Enemigo
	// 6: de Entidad a todas las entidades
	private int skillType;
	//esta se usarñ en la forma de decidir como manejar la selecciñn de objetivos en el combate
	//constructores
	public Skill(String shortName, String name, String desc, String type, String atkMsg, int skillType, int SPD, boolean menu, int atkTimes) {
		this.name = name;
		this.desc = desc;
		this.skillType = skillType;
		this.shortName = shortName;
		this.type = type;
		this.atkMsg = atkMsg;
		this.SPD = SPD;
		this.menu = menu;
		this.atkTimes = atkTimes;
		MPcost = 0;
		SPcost = 0;
	}
	
	//para uso de items
	public Skill(int SPD) {
		this.SPD = SPD;
	}
	//esto se hace para usar items en el combate, ya que las acciones son manejadas como skills.
	
	
	public Skill(String shortName, String name, String desc, String type, String atkMsg, int skillType, int SPD, boolean menu, int atkTimes, int MPcost, int SPcost) {
		this.name = name;
		this.desc = desc;
		this.shortName = shortName;
		this.type = type;
		this.atkMsg = atkMsg;
		this.skillType = skillType;
		this.SPD = SPD;
		this.menu = menu;
		this.atkTimes = atkTimes;
		this.MPcost = MPcost;
		this.SPcost = SPcost;
	}
	public Skill(String shortName, String name, String desc, String type, String atkMsg, int skillType, int SPD, boolean menu, int atkTimes, 
			int MPcost, int SPcost, int lvl) { //para agregar las habilidades por nivel
		this.name = name;
		this.desc = desc;
		this.shortName = shortName;
		this.type = type;
		this.atkMsg = atkMsg;
		this.skillType = skillType;
		this.SPD = SPD;
		this.menu = menu;
		this.lvl = lvl;
		this.atkTimes = atkTimes;
		this.MPcost = MPcost;
		this.SPcost = SPcost;
	}
	//getters
	public String getName() {
		return name;
	}
	public String getDesc() {
		return desc;
	}
	public int getLvl() {
		return lvl;
	}
	public int getMP() {
		return MPcost;
	}
	public int getSkillType() {
		return skillType;
	}
	public int getSP() {
		return SPcost;
	}
	public boolean getMenu() {
		return menu;
	}
	public String getType() {
		return type;
	}
	public void setType(int n) { //UNICAMENTE usar con items en skills
		skillType = n;
	}
	
	private void checkRand() {
		if(rand == null) {
			rand = new Random();
		}
	}
	//usos
	public void use(Entity a) {	//Self
		checkRand();
		String msg = (a.getName() + atkMsg); //muestra el mensaje de la skill
		a.modSP(-SPcost);
		a.modMP(-MPcost);
		switch(shortName) {
		case "defend":
			//Protegerse
			a.prot = 2;
			break;
		}
		DialMan.addDialogue(0, BattleScreen.DIAL_BACTION);
		DialMan.addDialogue(BattleScreen.DIAL_BACTION, -1, null, null, msg, 20);
	}
	public void use(Entity a, Entity b) { //A otra entidad
		checkRand();
		int cont = 1;
		if(atkMsg != null) {
			DialMan.addDialogue(0, 1, null, null, a.getName() + atkMsg + b.getName(), 20); //muestra el mensaje de la skill
		}else {
			DialMan.addDialogue(0, 1);
		}
		int dmg = 0;
		String dmgType;
		a.modSP(-SPcost);
		a.modMP(-MPcost);
		//Si la skill no tiene un tipo definido, usa la del que la castea.
		// por ejemplo, los ataques comunes no tienen tipo, asi que a pesar de que el explorador y 
		// el ladron usan el mismo ataque, el tipo del primero es RAN y del segundo es PHY
		String msg = null;
		String msg2 = null;
		String msg3 = null;
		if(type == null ) {
			dmgType = a.getType();
		}else {
			dmgType = type;
		}
		
		for(int i = 0; i < atkTimes; i++) {
			Double mul = detMul(b, dmgType); //multiplicador de daño para chequear resistencias y debilidades
											 //esto solo aplica con enemigos, los heroes no reciben mas o menos daño.
			switch(shortName) {
			//ataque normal fisico
			case "defAtt":
				dmg = ((a.getATK()*4) / ( (b.getDEF() / 10) + 1 )) / b.prot ; //la formula del ataque
				dmg *= mul; 
				b.modHP(-dmg);
				msg = ("El ataque hizo " + dmg + " de daño!");
				break;
			//ataque normal magico
			case "defMat":
				dmg = ((a.getMAT()*4) / ( (b.getMDF() / 10) + 1 )) / b.prot ;
				dmg *= mul;
				b.modHP(-dmg);
				msg = ("El ataque hizo " + dmg + " de daño!");
				break;
				
			//warrior
			case "charAtk":
				dmg = ((a.getMAT()*6) / ( (b.getMDF() / 10) + 1 )) / b.prot ;
				dmg *= mul;
				b.modHP(-dmg);
				msg = ("El ataque hizo " + dmg + " de daño!");
				break;
			case "deepCut":
				dmg = ((a.getMAT()*5) / ( (b.getMDF() / 10) + 1 )) / b.prot ;
				dmg *= mul;
				if(rand.nextInt(100) < 20) {
					msg2 = b.setEffect(new Effect("BLE"));
				}
				b.modHP(-dmg);
				msg = ("El ataque hizo " + dmg + " de daño!");
				break;
			case "knockout":
				dmg = ((a.getATK()*5) / ( (b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul;
				if(rand.nextInt(100) < 25) {
					msg2 = b.setEffect(new Effect("CON"));
				}
				b.modHP(-dmg);
				msg = ("El ataque hizo " + dmg + " de daño!");
				break;
			case "skullCracker":
				dmg = ((a.getATK()*6) / ( (b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul;
				if(rand.nextInt(100) < 30) {
					msg2 = b.setEffect(new Effect("CON"));
				}
				b.modHP(-dmg);
				msg = ("El ataque hizo " + dmg + " de daño!");
				break;
			case "brutalBlow":
				dmg = ((a.getATK()*8) / ( (b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul; 
				b.modHP(-dmg);
				msg = ("El ataque hizo " + dmg + " de daño!");
				break;
			case "lunge":
				dmg = ((a.getATK()*6) / ( (b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul; 
				msg2 = b.setEffect(new Effect("CON"));
				b.modHP(-dmg);
				msg = ("El ataque hizo " + dmg + " de daño!");
				break;
			case "hustle":
				dmg = ((a.getATK()*8) / ( (b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul; 
				if(rand.nextInt(100)<40) {
					msg2 = b.setEffect(new Effect("RAG"));
				}
				b.modHP(-dmg);
				msg = ("El ataque hizo " + dmg + " de daño!");
				break;
			case "backhand":
				dmg = ((a.getATK()*6) / ( (b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul;
				msg2 = b.setEffect(new Effect("SLE"));
				b.modHP(-dmg);
				msg = ("El ataque hizo " + dmg + " de daño!");
				break;
			
				
				
			//mago
			case "fireBall":
				dmg = ((a.getMAT()*6) / ( (b.getMDF() / 10) + 1 )) / b.prot ;
				dmg *= mul;
				b.modHP(-dmg);
				msg = ("El ataque hizo " + dmg + " de daño!");
				break;
			case "windBurst":
				dmg = ((a.getMAT()*6) / ( (b.getMDF() / 10) + 1 )) / b.prot ;
				dmg *= mul;
				b.modHP(-dmg);
				msg = ("El ataque hizo " + dmg + " de daño!");
				break;
			case "terrAttack":
				dmg = ((a.getMAT()*6) / ( (b.getMDF() / 10) + 1 )) / b.prot ;
				dmg *= mul;
				b.modHP(-dmg);
				msg = ("El ataque hizo " + dmg + " de daño!");
				break;
			case "splatter":
				dmg = ((a.getMAT()*6) / ( (b.getMDF() / 10) + 1 )) / b.prot ;
				dmg *= mul;
				b.modHP(-dmg);
				msg = ("El ataque hizo " + dmg + " de daño!");
				break;
			case "poisoning":
				msg2 = b.setEffect(new Effect("POI"));
				break;
			case "incantation":
				msg2 = b.setEffect(new Effect("ENC"));
				break;
			case "decibels":
				msg2 = b.setEffect(new Effect("SIL"));
				break;
				
				//thief
			case "decCut":
				dmg = ((a.getATK()*6) / ( (b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul; 
				if(rand.nextInt(100)<30) {
					msg2 = b.setEffect(new Effect("BLE"));
				}
				b.modHP(-dmg);
				msg = ("El ataque hizo " + dmg + " de daño!");
				break;
			case "fastAtk":
				dmg = ((a.getATK()*5) / ( (b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul;
				b.modHP(-dmg);
				msg = ("El ataque hizo " + dmg + " de daño!");
				break;
			case "smokeBomb":
				msg2 = b.setEffect(new Effect("SIL"));
				if(rand.nextInt(100)>20) {
					msg3 = b.setEffect(new Effect("CON"));
				}
				break;
			case "sneakAtk":
				dmg = ((a.getATK()*6) / ( ((int) (b.getDEF() * 0.6 ) / 10) + 1 )) / b.prot ;
				dmg *= mul;
				b.modHP(-dmg);
				msg = ("El ataque hizo " + dmg + " de daño!");
				break;
			case "venomEdge":
				dmg = ((a.getATK()*6) / ((b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul;
				msg2 = b.setEffect(new Effect("POI"));
				b.modHP(-dmg);
				msg = ("El ataque hizo " + dmg + " de daño!");
				break;
			case "magicTheft":
				dmg = ((int) (b.getHP() * 0.1));
				int mdmg = ((int) (b.getMP() * 0.1));
				b.modHP(-dmg);
				b.modMP(-mdmg);
				a.modHP(dmg);
				a.modMP(mdmg);
				msg = (a.getName() + " ha robado " + dmg + " HP y " + mdmg + " MP de " + b.getName() + "!");
				break;
			case "vitalTheft":
				dmg = ((int) (b.getHP() * 0.1));
				int sdmg = ((int) (b.getSP() * 0.1));
				b.modHP(-dmg);
				b.modSP(-sdmg);
				a.modHP(dmg);
				a.modSP(sdmg);
				msg = (a.getName() + " ha robado " + dmg + " HP y " + sdmg + " SP de " + b.getName() + "!");
				break;
			case "sleepPll":
				msg2 = b.setEffect(new Effect("SLE"));
				break;
			case "finisher":
				dmg = ((a.getATK()*8) / ((b.getDEF() / 10) + 1 )) / b.prot ;
				if(b.hasState("BLE")) {
					dmg *= 1.5;
				}
				dmg *= mul;
				b.modHP(-dmg);
				msg = ("El ataque hizo " + dmg + " de daño!");
				break;
			case "hitman":
				dmg = (a.getATK()*8) / b.prot ;
				dmg *= mul;
				b.modHP(-dmg);
				msg = ("El ataque hizo " + dmg + " de daño!");
				break;
				
				// explorer
			case "deadShot":
				dmg = ((a.getATK()*6) / ( (b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul; 
				b.modHP(-dmg);
				msg = ("El ataque hizo " + dmg + " de daño!");
				break;
			case "fireArrow":
				dmg = ((a.getATK()*5) / ( (b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul; 
				b.modHP(-dmg);
				msg = ("El ataque hizo " + dmg + " de daño!");
				break;
			case "iceArrow":
				dmg = ((a.getATK()*5) / ( (b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul; 
				b.modHP(-dmg);
				msg = ("El ataque hizo " + dmg + " de daño!");
				break;
			case "calTrap":
				msg2 = b.setEffect(new Effect("POI"));
				break;
			case "decoy":
				msg2 = b.setEffect(new Effect("CON"));
				break;
			case "nailIt":
				dmg = ((a.getATK()*8) / ( (b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul; 
				b.modHP(-dmg);
				msg = ("El ataque hizo " + dmg + " de daño!");
				break;
			case "allyTotem":
				msg2 = b.setEffect(new Effect("BEN"));
				break;
				
				//Sage
			case "healing":
				dmg = ( (int) (b.getHP() * 0.4)	 +  10);
				b.modHP(dmg);
				msg = (b.getName() + " recupero " + dmg + " HP!");
				break;
			case "deaftones":
				msg2 = b.setEffect(new Effect("CON"));
				if(rand.nextInt() < 50) {
					msg3 = b.setEffect(new Effect("POI"));
				}
				break;
			case "bardSong":
				dmg = ( (int) (b.getHP() * 0.6)	 +  20);
				b.modHP(dmg);
				msg = (b.getName() + " recupero " + dmg + " HP!");
				break;
			case "purification":
				b.clearNegEffects();
				break;
			case "vitalLust":
				dmg = (int) (b.getHP() * 0.15);
				b.modHP(-dmg);
				a.modHP(dmg);
				msg = (a.getName() + " robo " + dmg + " HP de " + b.getName() + "!");
				break;
			case "revive":
				if(b.hasState("DWN")) {
					dmg = (int) (b.getHP() * 0.5);
					b.clearEffect("DWN");
					b.modHP(dmg);
					msg = (b.getName() + " revivio!");
				}
				break;
			}
			DialMan.addDialogue(cont, cont+1, null, null, msg, 20);
			cont++;
			if(msg2 != null) {
				DialMan.addDialogue(cont, cont+1, null, null, b.getName() + msg2, 20);
				cont++;
			}
			if(msg3 != null) {
				DialMan.addDialogue(cont, cont+1, null, null, b.getName() + msg3, 20);
				cont++;
			}
			msg = null;
			switch((int) (mul * 100)) {
			case 25:
				msg = ("No fue para nada efectivo...");
				break;
			case 50:
				msg = ("No fue tan efectivo...");
				break;
			case 75:
				msg = ("No fue efectivo...");
				break;
			case 150:
				msg = ("Fue efectivo!");
				break;
			case 200:
				msg = ("Fue muy efectivo!");
				break;
			case 400:
				msg = ("Fue demasiado efectivo!");
				break;
			}
			if(msg != null) {
				DialMan.addDialogue(cont, cont+1, null, null, msg, 20);
				cont++;
			}
		}
		DialMan.addDialogue(cont, BattleScreen.DIAL_BACTION);
		DialMan.addDialogue(BattleScreen.DIAL_BACTION, -1);
	}

	public void use(Entity a, Entity[] b) { //A varias entidades
		checkRand();
		if(atkMsg != null) {
			DialMan.addDialogue(0, 1, null, null, a.getName() + atkMsg, 20); //muestra el mensaje de la skill
		}else {
			DialMan.addDialogue(0, 1);
		}
		String msg = null;
		String msg2 = null;
		String msg3 = null;
		String msg4 = null;
		int count = 1;
		a.modSP(-SPcost);
		a.modMP(-MPcost);
		for(int i = 0; i<b.length; i++) {
			int dmg = 0;
			String dmgType;
			
			//Si la skill no tiene un tipo definido, usa la del que la castea.
			// por ejemplo, los ataques comunes no tienen tipo, asi que a pesar de que el explorador y 
			// el ladron usan el mismo ataque, el tipo del primero es RAN y del segundo es PHY
			if(type == null ) {
				dmgType = a.getType();
			}else {
				dmgType = type;
			}
			
			for(int j = 0; j < atkTimes; j++) {
				Double mul = detMul(b[i], dmgType); //multiplicador de daño para chequear resistencias y debilidades
												 //esto solo aplica con enemigos, los heroes no reciben mas o menos daño.
				switch(shortName) {
				
				//warrior 
				case "crossCut":
					dmg = ((a.getATK()*5) / ( (b[i].getDEF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					msg = ("El ataque le hizo " + dmg + " de daño a " + b[i].getName() + "!");
					break;
				case "moralDest":
					dmg = ((a.getATK()*6) / ( (b[i].getDEF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					if(rand.nextInt(100) <35) {
						if(atkTimes == 1) {
							atkTimes = 2;
						}else {
							atkTimes = 1;
						}
					}else {
						atkTimes = 1;
					}
					msg = ("El ataque le hizo " + dmg + " de daño a " + b[i].getName() + "!");
					break;
				case "heavyTackle":
					dmg = ((a.getATK()*6) / ( (b[i].getDEF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					if(rand.nextInt(100) <50) {
						msg2 = b[i].setEffect(new Effect("TIR"));
					}
					msg = ("El ataque le hizo " + dmg + " de daño a " + b[i].getName() + "!");
					break;
				case "heavyLand":
					dmg = ((a.getATK()*8) / ( (b[i].getDEF() / 10))) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					if(rand.nextInt(100) <30) {
						msg2 = b[i].setEffect(new Effect("CON"));
					}
					msg = ("El ataque le hizo " + dmg + " de daño a " + b[i].getName() + "!");
					break;
				case "warCry":
					int temp = rand.nextInt(3);
					switch(temp) {
					case 0:
						msg2 = b[i].setEffect(new Effect("CON"));
						break;
					case 1:
						msg2 = b[i].setEffect(new Effect("RAG"));
						break;
					case 2:
						msg2 = b[i].setEffect(new Effect("SIL"));
						break;
					}
					break;
				case "crushAtk":
					if(a.getATK() > a.getMAT()) {
						dmg = ((a.getATK()*12) / ( (b[i].getDEF() / 10) + 1)) / b[i].prot ;
					}else {
						dmg = ((a.getMAT()*12) / ( (b[i].getMDF() / 10) + 1)) / b[i].prot ;
					}
					msg2 = b[i].setEffect(new Effect("TIR"));
					msg3 = b[i].setEffect(new Effect("CON"));
					msg = ("El ataque le hizo " + dmg + " de daño a " + b[i].getName() + "!");
					break;
					
					//mage
				case "fireplace":
					dmg = ((a.getMAT()*6) / ( (b[i].getMDF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					msg = ("El ataque le hizo " + dmg + " de daño a " + b[i].getName() + "!");
					break;
				case "blizzard":
					dmg = ((a.getMAT()*6) / ( (b[i].getMDF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					msg = ("El ataque le hizo " + dmg + " de daño a " + b[i].getName() + "!");
					break;
				case "pressure":
					dmg = ((a.getMAT()*6) / ( (b[i].getMDF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					msg = ("El ataque le hizo " + dmg + " de daño a " + b[i].getName() + "!");
					break;
				case "tides":
					dmg = ((a.getMAT()*6) / ( (b[i].getMDF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					msg = ("El ataque le hizo " + dmg + " de daño a " + b[i].getName() + "!");
					break;
				case "tiresome":
					msg2 = b[i].setEffect(new Effect("TIR"));
					break;
				case "fireHur":
					dmg = ((a.getMAT()*8) / ( (b[i].getMDF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					if(rand.nextInt(100) <40) {
						msg2 = b[i].setEffect(new Effect("RAG"));
					}
					msg = ("El ataque le hizo " + dmg + " de daño a " + b[i].getName() + "!");
					break;
				case "collapse":
					dmg = ((a.getMAT()*8) / ( (b[i].getMDF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					if(rand.nextInt(100) <30) {
						msg2 = b[i].setEffect(new Effect("SIL"));
					}
					msg = ("El ataque le hizo " + dmg + " de daño a " + b[i].getName() + "!");
					break;
				case "seaquake":
					dmg = ((a.getMAT()*8) / ( (b[i].getMDF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					if(rand.nextInt(100) <25) {
						msg2 = b[i].setEffect(new Effect("SLE"));
					}
					msg = ("El ataque le hizo " + dmg + " de daño a " + b[i].getName() + "!");
					break;
				case "tornado":
					dmg = ((a.getMAT()*8) / ( (b[i].getMDF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					if(rand.nextInt(100) <50) {
						msg2 = b[i].setEffect(new Effect("CON"));
					}
					msg = ("El ataque le hizo " + dmg + " de daño a " + b[i].getName() + "!");
					break;
				case "curse":
					msg2 = b[i].setEffect(new Effect("POI"));
					msg3 = b[i].setEffect(new Effect("TIR"));
					msg4 = b[i].setEffect(new Effect("ENC"));
					break;
				case "blessing":
					msg2 = b[i].setEffect(new Effect("BEN"));
					break;
				case "lastPrism":
					if(a.getATK() > a.getMAT()) {
						dmg = ((a.getATK()*12) / ( (b[i].getDEF() / 10) + 1)) / b[i].prot ;
					}else {
						dmg = ((a.getMAT()*12) / ( (b[i].getMDF() / 10) + 1)) / b[i].prot ;
					}
					dmg *= mul;
					b[i].modHP(-dmg);
					msg = ("El ataque le hizo " + dmg + " de daño a " + b[i].getName() + "!");
					break;
					
					//thief
				case "bladeRain":
					dmg = ((a.getATK()*6) / ( (b[i].getDEF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					if(rand.nextInt(100) <25) {
						msg2 = b[i].setEffect(new Effect("BLE"));
					}
					msg = ("El ataque le hizo " + dmg + " de daño a " + b[i].getName() + "!");
					break;
				case "bladeSweep":
					dmg = ((a.getATK()*6) / ( (b[i].getDEF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					if(rand.nextInt(100) < 5) {
						msg2 = b[i].setEffect(new Effect("BLE"));
					}
					msg = ("El ataque le hizo " + dmg + " de daño a " + b[i].getName() + "!");
					break;
				case "bladeTornado":
					dmg = ((a.getATK()*7) / ( (b[i].getDEF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					if(rand.nextInt(100) < 50) {
						msg2 = b[i].setEffect(new Effect("BLE"));
					}
					msg = ("El ataque le hizo " + dmg + " de daño a " + b[i].getName() + "!");
					break;
				case "chaos":
					dmg = (int) (b[i].getHP() * 0.05);
					msg2 = b[i].setEffect(new Effect("TIR"));
					msg3 = b[i].setEffect(new Effect("BLE"));
					b[i].modHP(-dmg);
					a.modHP(dmg);
					msg = (a.getName() + " le robo " + dmg + " HP a " + b[i].getName() + "!");
					break;
				case "throatSlice":
					if(a.getATK() > a.getMAT()) {
						dmg = ((a.getATK()*10) / ( (b[i].getDEF() / 10) + 1)) / b[i].prot ;
					}else {
						dmg = ((a.getMAT()*10) / ( (b[i].getMDF() / 10) + 1)) / b[i].prot ;
					}
					msg2 = b[i].setEffect(new Effect("SIL"));
					msg3 = b[i].setEffect(new Effect("BLE"));
					msg = ("El ataque le hizo " + dmg + " de daño a " + b[i].getName() + "!");
					break;
					
					//explorador
				case "arrowRain":
					dmg = ((a.getATK()*5) / ( (b[i].getDEF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					if(rand.nextInt(100) < 10) {
						msg2 = b[i].setEffect(new Effect("BLE"));
					}
					msg = ("El ataque le hizo " + dmg + " de daño a " + b[i].getName() + "!");
					break;
				case "slimeDust":
					msg2 = b[i].setEffect(new Effect("POI"));
					break;
				case "expTorment":
					msg2 = b[i].setEffect(new Effect("CON"));
					msg3 = b[i].setEffect(new Effect("TIR"));
					break;
				case "tarPit":
					msg2 = b[i].setEffect(new Effect("POI"));
					msg3 = b[i].setEffect(new Effect("TIR"));
					break;
				case "debrisShower":
					dmg = ((a.getATK()*7) / ( (b[i].getDEF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					msg = ("El ataque le hizo " + dmg + " de daño a " + b[i].getName() + "!");
					break;
				case "worldRevolving":
					msg2 = b[i].setEffect(new Effect("RAG"));
					break;
				case "sleepGas":
					msg2 = b[i].setEffect(new Effect("SLE"));
					break;
				case "finalTrial":
					if(a.getATK() > a.getMAT()) {
						dmg = ((a.getATK()*10) / ( (b[i].getDEF() / 10) + 1)) / b[i].prot ;
					}else {
						dmg = ((a.getMAT()*10) / ( (b[i].getMDF() / 10) + 1)) / b[i].prot ;
					}
					msg2 = b[i].setEffect(new Effect("BLE"));
					msg = ("El ataque le hizo " + dmg + " de daño a " + b[i].getName() + "!");
					break;
					
					//sage
				case "mulHeal":
					dmg = ( (int) (b[i].getHP() * 0.2)	 +  10);
					b[i].modHP(dmg);
					msg = (b[i].getName() + " recupero " + dmg + " HP!");
					break;
				case "incant":
					msg2 = b[i].setEffect(new Effect("ENC"));
					break;
				case "silence":
					msg2 = b[i].setEffect(new Effect("SIL"));
					break;
				case "toxicDust":
					dmg = ((a.getMAT()*6) / ( (b[i].getMDF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					msg2 = b[i].setEffect(new Effect("POI"));
					msg = ("El ataque le hizo " + dmg + " de daño a " + b[i].getName() + "!");
					break;
				case "shadowSpell":
					msg2 = b[i].setEffect(new Effect("ENC"));
					msg3 = b[i].setEffect(new Effect("TIR"));
					break;
				case "godOffering":
					msg2 = b[i].setEffect(new Effect("BEN"));
					break;
				case "healingRitual":
					dmg = ( (int) (b[i].getHP() * 0.4)	 +  20);
					break;
				case "divineEx":
					b[i].clearNegEffects();
					break;
				case "sacrifice":
					if(b[i].getClass() == a.getClass()) {
						if(b[i] == a) {
							b[i].hp = 0;
						}else {
							if(b[i].hasState("DWN")) {
								b[i].clearEffects();
							}
							b[i].hp = b[i].getHP();
							b[i].clearNegEffects();
							msg2 = b[i].setEffect(new Effect("BEN"));
						}
					}else {
						msg2 = b[i].setEffect(new Effect("POI"));
						msg3 = b[i].setEffect(new Effect("ENC"));
						msg4 = b[i].setEffect(new Effect("TIR"));
					}
					break;
				}
				
				DialMan.addDialogue(count, count + 1, null, null, msg, 20);
				count++;
				if(msg2 != null) {
					DialMan.addDialogue(count, count + 1, null, null, b[i].getName() + msg2, 20);
					count++;
				}
				if(msg3 != null) {
					DialMan.addDialogue(count, count + 1, null, null, b[i].getName() +  msg3, 20);
					count++;
				}
				if(msg4 != null) {
					DialMan.addDialogue(count, count + 1, null, null, b[i].getName() +  msg4, 20);
					count++;
				}
				
				String finMsg = null;
				switch((int) (mul * 100)) {
				case 25:
					finMsg = ("No fue para nada efectivo...");
					break;
				case 50:
					finMsg = ("No fue tan efectivo...");
					break;
				case 75:
					finMsg = ("No fue efectivo...");
					break;
				case 150:
					finMsg = ("Fue efectivo!");
					break;
				case 200:
					finMsg = ("Fue muy efectivo!");
					break;
				case 400:
					finMsg = ("Fue demasiado efectivo!");
					break;
				}
				if(finMsg != null) {
					DialMan.addDialogue(count, count + 1, null, null, finMsg, 20);
					count++;
				}
			}
		}
		DialMan.addDialogue(count, BattleScreen.DIAL_BACTION);
		DialMan.addDialogue(BattleScreen.DIAL_BACTION, -1);
	}
	public void use(Entity a, Entity ally, Entity enemy) { //caso particular, skillType 5
		checkRand();
		if(atkMsg != null) {
			DialMan.addDialogue(0, BattleScreen.DIAL_BACTION, null, null, a.getName() + atkMsg + enemy.getName(), 20); //muestra el mensaje de la skill
		}else {
			DialMan.addDialogue(0, BattleScreen.DIAL_BACTION);
		}
		String msg = null;
		int dmg = 0;
		a.modSP(-SPcost);
		a.modMP(-MPcost);
		
		switch(shortName) {
		case "staThief":
			dmg = (int) (enemy.getSP() * 0.2) + 10;
			ally.modSP(dmg);
			enemy.modSP(-dmg);
			msg = (a.getName() + " robo " + dmg + " SP de " + enemy.getName() + " y se lo dio a " + ally.getName() + "!");
			break;
		}
		DialMan.addDialogue(BattleScreen.DIAL_BACTION, -1, null, null, msg, 20);
	}
	public void use(Entity a, Entity b, Player player, Item item) { //caso especial para combates donde se usa un item
		checkRand();
		String msg = null;
		String msg2 = null;
		DialMan.addDialogue(0, 1);
		if(item.q >= 0) {
			if(a == b) {
				msg = (a.getName() + " usa " + item.getName() +"!");
				msg2 = item.Use(b, player);
			}else {
				msg = (a.getName() + " usa " + item.getName() + " sobre " + b.getName() + "!");
				msg2 = item.Use(b, player);
			}
		}else {
			msg = (a.getName() + " no pudo usar" + item.getName() + " porque se termino.\n");
		}
		DialMan.addDialogue(1, BattleScreen.DIAL_BACTION, null, null, msg, 20);
		DialMan.addDialogue(BattleScreen.DIAL_BACTION, -1, null, null, msg2, 20);
	}
	
	//devuelve la resistencia del enemigo a ese tipo
	private double detMul(Entity b, String type) {
		if(b.getClass() != Hero.class) {
			switch(type) {
			case "PHY":
				return b.getPHY();
			case "RAN":
				return b.getRAN();
			case "FIR":
				return b.getFIR();
			case "WAT":
				return b.getWAT();
			case "WIN":
				return b.getWIN();
			case "EAR":
				return b.getEAR();
			default:
				return 1;
			}
		}else {
			return 1;
		}
	}
}
