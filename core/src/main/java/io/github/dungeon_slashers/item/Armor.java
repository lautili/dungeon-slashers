package io.github.dungeon_slashers.item;

/*
		CLASE ARMOR	

	sera la clase encargada de controlar a las armaduras.

*/

public class Armor extends Item {
	private int atk;
	private int mat;
	private int mdf;
	private int def;
	private int spd;
	
	//constructor
	public Armor(String name, String IDname, String desc, int atk, int mat, int def, int mdf, int spd, int cost) {
		super(name, IDname, desc, cost, true);
		this.atk = atk;
		this.def = def;
		this.mdf = mdf;
		this.spd = spd;
		this.mat = mat;
	}
	
	//getters
	public int getATK() {
		return this.atk;
	}
	public int getDEF() {
		return this.def;
	}
	public int getMAT() {
		return this.mat;
	}
	public int getMDF() {
		return this.mdf;
	}
	public int getSPD() {
		return this.spd;
	}
	public int getCost() {
		return this.cost;
	}
}
