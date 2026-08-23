package io.github.dungeon_slashers.entities;

import io.github.dungeon_slashers.Effect;
import io.github.dungeon_slashers.Skill;

/*
		CLASE ENEMY

	sera la clase encargada de controlar a los enemigos.

*/

public class Enemy extends Entity{		
	protected int priority; //la prioridad que tiene al randomizar las batallas
	public int xp; //la xp que dan
	public int gld; //el oro que dan
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
		this.baseType =enemy.baseType;
		this.maxhp = enemy.hp;
		this.hp = enemy.hp;
		this.maxsp = enemy.sp;
		this.sp = enemy.sp;
		this.maxmp = enemy.mp;
		this.mp = enemy.mp;
		this.atk = enemy.atk;
		this.def = enemy.def;
		this.mat = enemy.mat;
		this.mdf = enemy.mdf;
		this.spd = enemy.spd;
		this.xp = enemy.xp;
		this.gld = enemy.gld;
		this.priority = enemy.priority;
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
}
