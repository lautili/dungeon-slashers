package io.github.dungeon_slashers.entities;

import com.badlogic.gdx.graphics.Texture;

import io.github.dungeon_slashers.Effect;
import io.github.dungeon_slashers.Skill;
import io.github.dungeon_slashers.controllers.Menu;

/*
		CLASE ENTITY

	sera la clase encargada de controlar parametros escenciales de las entidades.

*/

public abstract class Entity {
	// Info basica de la entidad
	protected String name;
	protected String IDname;
	protected String baseType;
	protected String className;
	protected transient Texture texture;
	
	// stats
	public int maxhp;
	public int hp;
	public int maxmp;
	public int mp;
	public int maxsp;
	public int sp;
	public int prot; //usada en calculos para ataque. define si se esta protegiendo o no
	
	//las stats iniciales, usada para formulas (heroes)
		protected int hpIn;
		protected int mpIn;
		protected int spIn;
		protected int atkIn;
		protected int defIn;
		protected int matIn;
		protected int mdfIn;
		protected int spdIn;
		
		//las stats con el aumento por nivel
		public int atk;
		public int def;
		public int mat;
		public int mdf;
		public int spd;
		
		//las stats con aumentos por armas y armadura agregado (heroes)
		public int atkF;
		public int defF;
		public int matF;
		public int mdfF;
		public int spdF;
		
		//debilidades y fortalezas (enemigos)
			protected double PHY;
			protected double RAN;
			protected double FIR;
			protected double WAT;
			protected double WIN;
			protected double EAR;
		
	//efectos de estado
	protected Effect[] effects = new Effect[6];
	//habilidades
	protected Skill[] skills;
	
	//getters
	
	public int getHP() {
		return this.maxhp;
	}
	public int getMP() {
		return this.maxmp;
	}
	public int getSP() {
		return this.maxsp;
	}
	public int getATK() {
		return this.atkF;
	}
	public int getDEF() {
		return this.defF;
	}
	public int getMAT() {
		return this.matF;
	}
	public int getMDF() {
		return this.mdfF;
	}
	public int getSPD() {
		return this.spdF;
	}
	public int getATKR() {
		return this.atk;
	}
	public int getDEFR() {
		return this.def;
	}
	public int getMATR() {
		return this.mat;
	}
	public int getMDFR() {
		return this.mdf;
	}
	public int getSPDR() {
		return this.spd;
	}
	public String getName() {
		return this.name;
	}
	public String getIDName() {
		return IDname;
	}
	public String getclassName() {
		return this.className;
	}
	public String getType() {
		return baseType;
	}
	public Effect[] getEffects() {
		return this.effects;
	}
	
	//limpiar los efectos y poner efectos nuevos
	public void clearEffects() {
		for(int i = 0; i < effects.length; i++) {
			effects[i] = null;
		}
	}
	public void setEffect(Effect effect) {
		if(effects[0] != null && effects[0].getShortName().equals("DWN")) {
			
		}else {
			System.out.println(name + effect.getMSG());
			if(effect.getShortName() == "DWN") {
				clearEffects();
				effects[0] = effect;
			}
			else {
				int cont = 0;
				for(int i = 0; i < effects.length; i++) {
					if(effects[i] != null && effects[i].getShortName().equals(effect.getShortName())) {
						break;
					}
					if(effects[i] == null) {
						effects[i] = effect;
						break;
					}else {
						cont++;
					}
				}
				if(cont == 3) {
					effects[0] = effect;
				}
			}
		}
	}
	public void clearEffect(String name) {
		for(int i = 0; i < effects.length; i ++) {
			if(effects[i].getName() == name) {
				effects[i] = null;
			}
		}
	}
	public void clearNegEffects() {
		for(int i = 0; i < effects.length; i ++) {
			if(effects[i] != null) {
				String name = effects[i].getShortName();
				if(name != "BEN" && name != "DWN") {
					Menu.atkMsg(name + effects[i].getEndMsg());
					effects[i] = null;
				}
			}
		}
	}
	
	// actualiza los efectos y chequa que este en los turnos que deben estar
	public void updateEffects() {
		for(int i = 0; i < effects.length; i ++) {
			Effect effect = effects[i];
			if(effect != null) { //si hay efecto,
				if(effect.getShortName() != "DWN") {  //si el efecto no es DWN,
					if(effect.checkTurns()) { //si el efecto termin�,
						effects[i] = null; //limpia el efecto
						Menu.atkMsg(name + effect.getEndMsg()); //y lo imprime
					}else { //si no termino
						if(effect.getTurnMsg() != null) {
							Menu.atkMsg(name + effect.getTurnMsg()); // imprime el mensaje de turno
						}
						effect.use(this); //y si es POS, BLE, BEN, ENC o TIR, hace lo que el efecto haria.
					}
				}
			}
		}
	}
	public Skill[] getSkills() {
		return skills;
	}
	public double getPHY() {
		return PHY;
	}
	public double getRAN() {
		return RAN;
	}
	public double getFIR() {
		return FIR;
	}
	public double getWAT() {
		return WAT;
	}
	public double getWIN() {
		return WIN;
	}
	public double getEAR() {
		return EAR;
	}
	
	//cambios rapidos de vida/mana/stamina para pociones / ataques
			public void modHP(int num) {
				hp += num;
				if(hp > maxhp) {
					hp = maxhp;
				}
				if(hp < 0) {
					hp = 0;
				}
			}
			public void modMP(int num) {
				mp += num;
				if(mp > maxmp) {
					mp = maxmp;
				}
				if(mp < 0) {
					mp = 0;
				}
			}
			public void modSP(int num) {
				sp += num;
				if(sp > maxsp) {
					sp = maxsp;
				}
				if(sp < 0) {
					sp = 0;
				}
			}
	//agregar una o mas skills a un personaje
			public void addSkills(Skill... skills) {
				int length = this.skills.length;
				int newLength = length + skills.length;
				Skill[] temp = this.skills;
				this.skills = new Skill[newLength];
				for(int i = 0; i < length; i++) {
					this.skills[i] = temp[i];
				}
				for(int i = length; i < newLength; i++) {
					this.skills[i] = skills[i-length];
				}
			}
			public void addSkill(Skill skill) {
				int length = skills.length;
				Skill[] temp = skills;
				skills = new Skill[length+1];
				for(int i = 0; i < length; i++) {
					skills[i] = temp[i];
				}
				skills[length] = skill;
			}
	//chequear si tiene un efecto de estado
	public boolean hasState(String name) {
		for(int i = 0; i < effects.length; i++) {
			if(effects[i] != null && name.equals(effects[i].getShortName())) {
				return true;
			}
		}
		return false;
	}
		
	public String getBaseType() {
		return baseType;
	}
}
