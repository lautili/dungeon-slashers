package io.github.dungeon_slashers;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import io.github.dungeon_slashers.controllers.DialMan;
import io.github.dungeon_slashers.controllers.Menu;
import io.github.dungeon_slashers.entities.Enemy;
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
	private static Sound PHY = Gdx.audio.newSound(Gdx.files.internal("sounds/sword.mp3"));
	private static Sound RAN = Gdx.audio.newSound(Gdx.files.internal("sounds/arrow.mp3"));
	private static Sound FIR = Gdx.audio.newSound(Gdx.files.internal("sounds/fire.mp3"));
	private static Sound WAT = Gdx.audio.newSound(Gdx.files.internal("sounds/water.mp3"));
	private static Sound WIN = Gdx.audio.newSound(Gdx.files.internal("sounds/wind.mp3"));
	private static Sound EAR = Gdx.audio.newSound(Gdx.files.internal("sounds/earth.mp3"));
	private static Sound SWORD_BIG = Gdx.audio.newSound(Gdx.files.internal("sounds/sword_big.mp3"));
	private static Sound RAN_BIG = Gdx.audio.newSound(Gdx.files.internal("sounds/arrow_big.mp3"));
	private static Sound MAGIC_BIG = Gdx.audio.newSound(Gdx.files.internal("sounds/magic_big.mp3"));
	private static Sound BLADE_BIG = Gdx.audio.newSound(Gdx.files.internal("sounds/blade_big.mp3"));
	private static Sound SAGE_BIG = Gdx.audio.newSound(Gdx.files.internal("sounds/sage_big.mp3"));
	private static Sound SHIELD = Gdx.audio.newSound(Gdx.files.internal("sounds/shield.mp3"));
	private static Sound HEAL = Gdx.audio.newSound(Gdx.files.internal("sounds/healing.mp3"));
	private static Sound TRAP = Gdx.audio.newSound(Gdx.files.internal("sounds/trap.mp3"));
	private static Sound SMOKE_BOMB = Gdx.audio.newSound(Gdx.files.internal("sounds/smokebomb.mp3"));
	private static Sound BARD = Gdx.audio.newSound(Gdx.files.internal("sounds/bard.mp3"));
	private static Sound GROWL = Gdx.audio.newSound(Gdx.files.internal("sounds/growl.mp3"));
	
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
	public String getID() {
		return shortName;
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
	public int getAtkTimes() {
		return atkTimes;
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
		Sound snd = null;
		a.modSP(-SPcost);
		a.modMP(-MPcost);
		switch(shortName) {
		case "defend":
			//Protegerse
			a.prot = 2;
			snd = SHIELD;
			break;
		}
		snd.play(Main.config.volume * Main.config.volSFX);
		DialMan.addDialogue(0, BattleScreen.DIAL_BACTION);
		DialMan.addBDialogue(BattleScreen.DIAL_BACTION, -1, msg);
	}
	public void use(Entity a, Entity b) { //A otra entidad
		checkRand();
		if(atkMsg != null) {
			DialMan.addBDialogue(0, 1, a.getName() + atkMsg + b.getName()); //muestra el mensaje de la skill
		}else {
			DialMan.addDialogue(0, 1);
		}
		b.attacked = true;
		int count = 1;
		int dmg = 0;
		String dmgType;
		a.modSP(-SPcost);
		a.modMP(-MPcost);
		//Si la skill no tiene un tipo definido, usa la del que la castea.
		// por ejemplo, los ataques comunes no tienen tipo, asi que a pesar de que el explorador y 
		// el ladron usan el mismo ataque, el tipo del primero es RAN y del segundo es PHY
		if(type == null ) {
			dmgType = a.getType();
		}else {
			dmgType = type;
		}
		Sound snd;
		switch(dmgType) {
		case "PHY":
			snd = PHY;
			break;
		case "RAN":
			snd = RAN;
			break;
		case "FIR":
			snd = FIR;
			break;
		case "WAT":
			snd = WAT;
			break;
		case "WIN":
			snd = WIN;
			break;
		case "EAR":
			snd = EAR;
			break;
		default:
			snd = PHY;
			break;
		}
		if(b instanceof Enemy) {
			((Enemy) b).updateDiscoveries(dmgType);
		}
		b.lastDamageTaken = 0; // Se reinicia para acumular el daño total de la acción
		for(int i = 0; i < atkTimes; i++) {
			double mul = detMul(b, dmgType); //multiplicador de daño para chequear resistencias y debilidades
			b.lastDamageWeakness = mul;	    //esto solo aplica con enemigos, los heroes no reciben mas o menos daño.
			switch(shortName) {
			//ataque normal fisico
			case "defAtt":
				dmg = ((a.getATK()*4) / ( (b.getDEF() / 10) + 1 )) / b.prot ; //la formula del ataque
				dmg *= mul; 
				b.modHP(-dmg);
				break;
			//ataque normal magico
			case "defMat":
				dmg = ((a.getMAT()*4) / ( (b.getMDF() / 10) + 1 )) / b.prot ;
				dmg *= mul;
				b.modHP(-dmg);
				break;
				
			//warrior
			case "charAtk":
				dmg = ((a.getMAT()*6) / ( (b.getMDF() / 10) + 1 )) / b.prot ;
				dmg *= mul;
				b.modHP(-dmg);
				break;
			case "deepCut":
				dmg = ((a.getMAT()*5) / ( (b.getMDF() / 10) + 1 )) / b.prot ;
				dmg *= mul;
				count = b.setEffect(new Effect("BLE"), count);
				
				b.modHP(-dmg);
				break;
			case "knockout":
				dmg = ((a.getATK()*5) / ( (b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul;
				count = b.setEffect(new Effect("CON"), count);
				
				b.modHP(-dmg);
				break;
			case "skullCracker":
				dmg = ((a.getATK()*6) / ( (b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul;
				count = b.setEffect(new Effect("CON"), count);
				
				b.modHP(-dmg);
				break;
			case "brutalBlow":
				dmg = ((a.getATK()*8) / ( (b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul; 
				b.modHP(-dmg);
				break;
			case "lunge":
				dmg = ((a.getATK()*6) / ( (b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul; 
				count = b.setEffect(new Effect("CON"), count);
				b.modHP(-dmg);
				break;
			case "hustle":
				dmg = ((a.getATK()*8) / ( (b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul;
				count = b.setEffect(new Effect("RAG"), count);
				
				b.modHP(-dmg);
				break;
			case "backhand":
				dmg = ((a.getATK()*6) / ( (b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul;
				count = b.setEffect(new Effect("SLE"), count);
				b.modHP(-dmg);
				break;
			
				
				
			//mago
			case "fireBall":
				dmg = ((a.getMAT()*6) / ( (b.getMDF() / 10) + 1 )) / b.prot ;
				dmg *= mul;
				b.modHP(-dmg);
				break;
			case "windBurst":
				dmg = ((a.getMAT()*6) / ( (b.getMDF() / 10) + 1 )) / b.prot ;
				dmg *= mul;
				b.modHP(-dmg);
				break;
			case "terrAttack":
				dmg = ((a.getMAT()*6) / ( (b.getMDF() / 10) + 1 )) / b.prot ;
				dmg *= mul;
				b.modHP(-dmg);
				break;
			case "splatter":
				dmg = ((a.getMAT()*6) / ( (b.getMDF() / 10) + 1 )) / b.prot ;
				dmg *= mul;
				b.modHP(-dmg);
				break;
			case "poisoning":
				count = b.setEffect(new Effect("POI"), count);
				snd = HEAL;
				break;
			case "incantation":
				count = b.setEffect(new Effect("ENC"), count);
				snd = HEAL;
				break;
			case "decibels":
				count = b.setEffect(new Effect("SIL"), count);
				snd = HEAL;
				break;
				
				//thief
			case "decCut":
				dmg = ((a.getATK()*6) / ( (b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul; 
				count = b.setEffect(new Effect("BLE"), count);
				
				b.modHP(-dmg);
				break;
			case "fastAtk":
				dmg = ((a.getATK()*5) / ( (b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul;
				b.modHP(-dmg);
				break;
			case "smokeBomb":
				count = b.setEffect(new Effect("SIL"), count);
				count = b.setEffect(new Effect("CON"), count);
				snd = SMOKE_BOMB;
				break;
			case "sneakAtk":
				dmg = ((a.getATK()*6) / ( ((int) (b.getDEF() * 0.6 ) / 10) + 1 )) / b.prot ;
				dmg *= mul;
				b.modHP(-dmg);
				break;
			case "venomEdge":
				dmg = ((a.getATK()*6) / ((b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul;
				count = b.setEffect(new Effect("POI"), count);
				b.modHP(-dmg);
				break;
			case "magicTheft":
				dmg = ((int) (b.getHP() * 0.1));
				int mdmg = ((int) (b.getMP() * 0.1));
				b.modHP(-dmg);
				b.modMP(-mdmg);
				a.modHP(dmg);
				a.modMP(mdmg);
				snd = HEAL;
				break;
			case "vitalTheft":
				dmg = ((int) (b.getHP() * 0.1));
				int sdmg = ((int) (b.getSP() * 0.1));
				b.modHP(-dmg);
				b.modSP(-sdmg);
				a.modHP(dmg);
				a.modSP(sdmg);
				snd = HEAL;
				break;
			case "sleepPll":
				count = b.setEffect(new Effect("SLE"), count);
				snd = SMOKE_BOMB;
				break;
			case "finisher":
				dmg = ((a.getATK()*8) / ((b.getDEF() / 10) + 1 )) / b.prot ;
				if(b.hasState("BLE")) {
					dmg *= 1.5;
				}
				dmg *= mul;
				b.modHP(-dmg);
				break;
			case "hitman":
				dmg = (a.getATK()*8) / b.prot ;
				dmg *= mul;
				b.modHP(-dmg);
				break;
				
				// explorer
			case "deadShot":
				dmg = ((a.getATK()*6) / ( (b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul; 
				b.modHP(-dmg);
				break;
			case "fireArrow":
				dmg = ((a.getATK()*5) / ( (b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul; 
				b.modHP(-dmg);
				break;
			case "iceArrow":
				dmg = ((a.getATK()*5) / ( (b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul; 
				b.modHP(-dmg);
				break;
			case "calTrap":
				count = b.setEffect(new Effect("POI"), count);
				snd = TRAP;
				break;
			case "decoy":
				count = b.setEffect(new Effect("CON"), count);
				snd = TRAP;
				break;
			case "nailIt":
				dmg = ((a.getATK()*8) / ( (b.getDEF() / 10) + 1 )) / b.prot ;
				dmg *= mul; 
				b.modHP(-dmg);
				break;
			case "allyTotem":
				count = b.setEffect(new Effect("BEN"), count);
				snd = HEAL;
				break;
				
				//Sage
			case "healing":
				dmg = ( (int) (b.getHP() * 0.4)	 +  10);
				b.modHP(dmg);
				snd = HEAL;
				break;
			case "deaftones":
				count = b.setEffect(new Effect("CON"), count);
				count = b.setEffect(new Effect("POI"), count);
				snd = HEAL;
				break;
			case "bardSong":
				dmg = ( (int) (b.getHP() * 0.6)	 +  20);
				b.modHP(dmg);
				snd = BARD;
				break;
			case "purification":
				b.clearNegEffects();
				snd = HEAL;
				break;
			case "vitalLust":
				dmg = (int) (b.getHP() * 0.15);
				b.modHP(-dmg);
				a.modHP(dmg);
				snd = HEAL;
				break;
			case "revive":
				if(b.hasState("DWN")) {
					dmg = (int) (b.getHP() * 0.5);
					b.clearEffect("DWN");
					b.modHP(dmg);
				}
				snd = HEAL;
				break;
			}
			b.lastDamageTaken += dmg;
		}
		snd.play(Main.config.volume * Main.config.volSFX);
		DialMan.addDialogue(count, BattleScreen.DIAL_BACTION);
		DialMan.addDialogue(BattleScreen.DIAL_BACTION, -1);
	}

	public void use(Entity a, Entity[] b) { //A varias entidades
		checkRand();
		if(atkMsg != null) {
			DialMan.addBDialogue(0, 1, a.getName() + atkMsg); //muestra el mensaje de la skill
		}else {
			DialMan.addDialogue(0, 1);
		}
		int count = 1;
		a.modSP(-SPcost);
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
		Sound snd;
		switch(dmgType) {
		case "PHY":
			snd = PHY;
			break;
		case "RAN":
			snd = RAN;
			break;
		case "FIR":
			snd = FIR;
			break;
		case "WAT":
			snd = WAT;
			break;
		case "WIN":
			snd = WIN;
			break;
		case "EAR":
			snd = EAR;
			break;
		default:
			snd = PHY;
			break;
		}
		a.modMP(-MPcost);
		for(int i = 0; i<b.length; i++) {
			if(b[i] instanceof Enemy) {
				((Enemy) b[i]).updateDiscoveries(dmgType);
			}
			b[i].attacked = true;
			b[i].lastDamageTaken = 0; // Se reinicia para acumular el daño en cada objetivo
			for(int j = 0; j < atkTimes; j++) {
				double mul = detMul(b[i], dmgType); //multiplicador de daño para chequear resistencias y debilidades
				b[i].lastDamageWeakness = mul;						 //esto solo aplica con enemigos, los heroes no reciben mas o menos daño.
				switch(shortName) {
				
				//warrior 
				case "crossCut":
					dmg = ((a.getATK()*5) / ( (b[i].getDEF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
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
					break;
				case "heavyTackle":
					dmg = ((a.getATK()*6) / ( (b[i].getDEF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					count = b[i].setEffect(new Effect("TIR"), count);
					break;
				case "heavyLand":
					dmg = ((a.getATK()*8) / ( (b[i].getDEF() / 10))) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					count = b[i].setEffect(new Effect("CON"), count);
					break;
				case "warCry":
					int temp = rand.nextInt(3);
					switch(temp) {
					case 0:
						count = b[i].setEffect(new Effect("CON"), count);
						break;
					case 1:
						count = b[i].setEffect(new Effect("RAG"), count);
						break;
					case 2:
						count = b[i].setEffect(new Effect("SIL"), count);
						break;
					}
					snd = GROWL;
					break;
				case "crushAtk":
					if(a.getATK() > a.getMAT()) {
						dmg = ((a.getATK()*12) / ( (b[i].getDEF() / 10) + 1)) / b[i].prot ;
					}else {
						dmg = ((a.getMAT()*12) / ( (b[i].getMDF() / 10) + 1)) / b[i].prot ;
					}
					count = b[i].setEffect(new Effect("TIR"), count);
					count = b[i].setEffect(new Effect("CON"), count);
					snd = SWORD_BIG;
					break;
					
					//mage
				case "fireplace":
					dmg = ((a.getMAT()*6) / ( (b[i].getMDF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					break;
				case "blizzard":
					dmg = ((a.getMAT()*6) / ( (b[i].getMDF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					break;
				case "pressure":
					dmg = ((a.getMAT()*6) / ( (b[i].getMDF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					break;
				case "tides":
					dmg = ((a.getMAT()*6) / ( (b[i].getMDF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					break;
				case "tiresome":
					count = b[i].setEffect(new Effect("TIR"), count);
					break;
				case "fireHur":
					dmg = ((a.getMAT()*8) / ( (b[i].getMDF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					count = b[i].setEffect(new Effect("RAG"), count);
					break;
				case "collapse":
					dmg = ((a.getMAT()*8) / ( (b[i].getMDF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					count = b[i].setEffect(new Effect("SIL"), count);
					break;
				case "seaquake":
					dmg = ((a.getMAT()*8) / ( (b[i].getMDF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					count = b[i].setEffect(new Effect("SLE"), count);
					break;
				case "tornado":
					dmg = ((a.getMAT()*8) / ( (b[i].getMDF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					count = b[i].setEffect(new Effect("CON"), count);
					break;
				case "curse":
					count = b[i].setEffect(new Effect("POI"), count);
					count = b[i].setEffect(new Effect("TIR"), count);
					count = b[i].setEffect(new Effect("ENC"), count);
					snd = HEAL;
					break;
				case "blessing":
					count = b[i].setEffect(new Effect("BEN"), count);
					snd = HEAL;
					break;
				case "lastPrism":
					if(a.getATK() > a.getMAT()) {
						dmg = ((a.getATK()*12) / ( (b[i].getDEF() / 10) + 1)) / b[i].prot ;
					}else {
						dmg = ((a.getMAT()*12) / ( (b[i].getMDF() / 10) + 1)) / b[i].prot ;
					}
					dmg *= mul;
					b[i].modHP(-dmg);
					snd = MAGIC_BIG;
					break;
					
					//thief
				case "bladeRain":
					dmg = ((a.getATK()*6) / ( (b[i].getDEF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					count = b[i].setEffect(new Effect("BLE"), count);
					break;
				case "bladeSweep":
					dmg = ((a.getATK()*6) / ( (b[i].getDEF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					count = b[i].setEffect(new Effect("BLE"), count);
					break;
				case "bladeTornado":
					dmg = ((a.getATK()*7) / ( (b[i].getDEF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					count = b[i].setEffect(new Effect("BLE"), count);
					break;
				case "chaos":
					dmg = (int) (b[i].getHP() * 0.05);
					count = b[i].setEffect(new Effect("TIR"), count);
					count = b[i].setEffect(new Effect("BLE"), count);
					b[i].modHP(-dmg);
					a.modHP(dmg);
					break;
				case "throatSlice":
					if(a.getATK() > a.getMAT()) {
						dmg = ((a.getATK()*10) / ( (b[i].getDEF() / 10) + 1)) / b[i].prot ;
					}else {
						dmg = ((a.getMAT()*10) / ( (b[i].getMDF() / 10) + 1)) / b[i].prot ;
					}
					count = b[i].setEffect(new Effect("SIL"), count);
					count = b[i].setEffect(new Effect("BLE"), count);
					snd = BLADE_BIG;
					break;
					
					//explorador
				case "arrowRain":
					dmg = ((a.getATK()*5) / ( (b[i].getDEF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					count = b[i].setEffect(new Effect("BLE"), count);
					break;
				case "slimeDust":
					count = b[i].setEffect(new Effect("POI"), count);
					snd = TRAP;
					break;
				case "expTorment":
					count = b[i].setEffect(new Effect("CON"), count);
					count = b[i].setEffect(new Effect("TIR"), count);
					snd = TRAP;
					break;
				case "tarPit":
					count = b[i].setEffect(new Effect("POI"), count);
					count = b[i].setEffect(new Effect("TIR"), count);
					snd = TRAP;
					break;
				case "debrisShower":
					dmg = ((a.getATK()*7) / ( (b[i].getDEF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					break;
				case "worldRevolving":
					count = b[i].setEffect(new Effect("RAG"), count);
					snd = HEAL;
					break;
				case "sleepGas":
					count = b[i].setEffect(new Effect("SLE"), count);
					snd = SMOKE_BOMB;
					break;
				case "finalTrial":
					if(a.getATK() > a.getMAT()) {
						dmg = ((a.getATK()*10) / ( (b[i].getDEF() / 10) + 1)) / b[i].prot ;
					}else {
						dmg = ((a.getMAT()*10) / ( (b[i].getMDF() / 10) + 1)) / b[i].prot ;
					}
					count = b[i].setEffect(new Effect("BLE"), count);
					snd = RAN_BIG;
					break;
					
					//sage
				case "mulHeal":
					dmg = ( (int) (b[i].getHP() * 0.2)	 +  10);
					if(b[i].hp > 0) {
						b[i].modHP(dmg);
					}else {
						b[i].modHP(0);
					}
					snd = HEAL;
					break;
				case "incant":
					count = b[i].setEffect(new Effect("ENC"), count);
					snd = HEAL;
					break;
				case "silence":
					count = b[i].setEffect(new Effect("SIL"), count);
					snd = HEAL;
					break;
				case "toxicDust":
					dmg = ((a.getMAT()*6) / ( (b[i].getMDF() / 10) + 1 )) / b[i].prot ;
					dmg *= mul;
					b[i].modHP(-dmg);
					count = b[i].setEffect(new Effect("POI"), count);
					break;
				case "shadowSpell":
					count = b[i].setEffect(new Effect("ENC"), count);
					count = b[i].setEffect(new Effect("TIR"), count);
					snd = HEAL;
					break;
				case "godOffering":
					count = b[i].setEffect(new Effect("BEN"), count);
					snd = HEAL;
					break;
				case "healingRitual":
					dmg = ( (int) (b[i].getHP() * 0.4)	 +  20);
					if(b[i].hp > 0) {
						b[i].modHP(dmg);
					}else {
						b[i].modHP(0);
					}
					snd = HEAL;
					break;
				case "divineEx":
					b[i].clearNegEffects();
					snd = HEAL;
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
							count = b[i].setEffect(new Effect("BEN"), count);
						}
					}else {
						count = b[i].setEffect(new Effect("POI"), count);
						count = b[i].setEffect(new Effect("ENC"), count);
						count = b[i].setEffect(new Effect("TIR"), count);
					}
					snd = SAGE_BIG;
					break;
				}
				b[i].lastDamageTaken += dmg;
			}
		}
		snd.play(Main.config.volume * Main.config.volSFX);
		DialMan.addDialogue(count, BattleScreen.DIAL_BACTION);
		DialMan.addDialogue(BattleScreen.DIAL_BACTION, -1);
	}
	public void use(Entity a, Entity ally, Entity enemy) { //caso particular, skillType 5
		checkRand();
		ally.attacked = true;
		enemy.attacked = true;
		ally.lastDamageTaken = 0;
		enemy.lastDamageTaken = 0;
		if(atkMsg != null) {
			DialMan.addBDialogue(0, BattleScreen.DIAL_BACTION, a.getName() + atkMsg + enemy.getName()); //muestra el mensaje de la skill
		}else {
			DialMan.addDialogue(0, BattleScreen.DIAL_BACTION);
		}
		int dmg = 0;
		a.modSP(-SPcost);
		a.modMP(-MPcost);
		switch(shortName) {
		case "staThief":
			dmg = (int) (enemy.getSP() * 0.2) + 10;
			ally.lastDamageTaken = dmg;
			ally.modSP(dmg);
			enemy.modSP(-dmg);
			enemy.lastDamageTaken = dmg;
			break;
		}
		HEAL.play(Main.config.volume * Main.config.volSFX);
		DialMan.addDialogue(BattleScreen.DIAL_BACTION, -1);
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
		DialMan.addBDialogue(1, BattleScreen.DIAL_BACTION, msg);
		DialMan.addBDialogue(BattleScreen.DIAL_BACTION, -1, msg2);
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