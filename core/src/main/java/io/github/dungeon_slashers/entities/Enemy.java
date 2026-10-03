package io.github.dungeon_slashers.entities;

import com.badlogic.gdx.graphics.Texture;

import io.github.dungeon_slashers.Effect;
import io.github.dungeon_slashers.Skill;

/*
		CLASE ENEMY

	sera la clase encargada de controlar a los enemigos.

*/

public class Enemy extends Entity{		
	protected transient int priority; //la prioridad que tiene al randomizar las batallas
	public transient int xp; //la xp que dan
	public transient int gld; //el oro que dan
	private transient String description;
	public boolean defeated = false;
	public boolean discovered = false;
	private boolean[] discWeakness = new boolean[6];
	public transient final int WEAK_PHY = 0;
	public transient final int WEAK_RAN = 1;
	public transient final int WEAK_FIR = 2;
	public transient final int WEAK_WAT = 3;
	public transient final int WEAK_WIN = 4;
	public transient final int WEAK_EAR = 5;
	
	//constructor
	public Enemy(String name, String IDname, String baseType, int hp, int mp, int sp, int atk, int def, int mat, int mdf, int spd, 
			int xp, int gld, int priority,
			double PHY, double RAN, double FIR, double WAT, double WIN, double EAR,
			Skill attack, Skill defend) {
		this.name = name;
		this.IDname = IDname;
		this.baseType = baseType;
		this.maxhp = hp;
		this.hp = hp;
		this.maxsp = sp;
		this.sp = sp;
		this.maxmp = mp;
		this.mp = mp;
		this.atk = atk;
		this.def = def;
		this.mat = mat;
		this.mdf = mdf;
		this.spd = spd;
		this.xp = xp;
		this.gld = gld;
		this.priority = priority;
		atkF = atk;
		defF = def;
		matF = mat;
		mdfF = mdf;
		spdF = spd;
		this.PHY = PHY;
		this.RAN = RAN;
		this.FIR = FIR;
		this.WAT = WAT;
		this.WIN = WIN;
		this.EAR = EAR;
		try {
			texture = new Texture("sprites/enemies/" + IDname + ".png");
		}catch(Exception e) {
			texture = new Texture("sprites/enemies/something.png");
		}
		skills = new Skill[2];
		skills[0] = attack;
		skills[1] = defend;
		prot = 1;
	}
	
	//este constructor es para copiar enemigos. si, por ejemplo, una pelea tiene 2 slimes, 
	//  con la primer funcion los 2 compartiran vida.
	//	gracias a esta funcion, se pueden crear varias instancias de un slime sin preocuparse por el problema 
	//	anterior ni tener que copiarlo manualmente con el anterior constructor.
	//	Aunque podria llegar a llenar el buffer, en un juego de este tama�o el buffer no es realmente un problema, 
	//  asi que se puede utilizar este metodo sin miedo.
	public Enemy(Enemy enemy) { 
		this.name = enemy.name;
		this.IDname = enemy.IDname;
		this.baseType =enemy.baseType;
		this.maxhp = enemy.hp;
		this.hp = enemy.hp;
		this.maxsp = enemy.sp;
		this.sp = enemy.sp;
		this.maxmp = enemy.mp;
		this.mp = enemy.mp;
		this.atk = enemy.atk;
		this.texture = enemy.texture;
		this.def = enemy.def;
		this.mat = enemy.mat;
		this.mdf = enemy.mdf;
		this.spd = enemy.spd;
		this.xp = enemy.xp;
		this.gld = enemy.gld;
		this.priority = enemy.priority;
		this.description = enemy.description;
		this.discWeakness = enemy.discWeakness;
		defeated = enemy.defeated;
		discovered = enemy.discovered;
		atkF = atk;
		defF = def;
		matF = mat;
		mdfF = mdf;
		spdF = spd;
		this.PHY = enemy.PHY;
		this.RAN = enemy.RAN;
		this.FIR = enemy.FIR;
		this.WAT = enemy.WAT;
		this.WIN = enemy.WIN;
		this.EAR = enemy.EAR;
		effects = new Effect[3];
		skills = enemy.skills;
		prot = 1;
	}

	//getters
	public int getPriority() {
		return priority;
	}
	public int getXP() {
		return xp;
	}
	public int getGLD() {
		return gld;
	}
	public String getDesc() {
		return description;
	}
	public double[] getWeakness() {
		double[] temp = {
				PHY,
				RAN,
				FIR,
				WAT,
				WIN,
				EAR
		};
		return temp;
	}
	public void updateDiscoveries(String type) {
		if(type == null) {
			return;
		}
		switch(type) {
		case "PHY":
			discWeakness[WEAK_PHY] = true;
			break;
		case "RAN":
			discWeakness[WEAK_RAN] = true;
			break;
		case "FIR":
			discWeakness[WEAK_FIR] = true;
			break;
		case "WAT":
			discWeakness[WEAK_WAT] = true;
			break;
		case "WIN":
			discWeakness[WEAK_WIN] = true;
			break;
		case "EAR":
			discWeakness[WEAK_EAR] = true;
			break;
		}
	}
	public boolean[] getWeaknesses() {
		return discWeakness;
	}
	public void setWeaknesses(boolean[] weak) {
		discWeakness = weak;	
	}
	
	public void setDesc(String description) {
		this.description = description;
	}
}
