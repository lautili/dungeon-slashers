package io.github.dungeon_slashers.item;

/*
		CLASE WEAPON

	será la clase encargada de controlar las armas y sus estadisticas.

*/

public class Weapon extends Item {
	private int atk;
	private int mat;
	private int spd;
	private String[] className; //las clases que pueden usarlo
	
	//constructor
	public Weapon(String name, String IDname, String desc, int atk, int mat, int spd, int cost, String... className) {
		super(name, IDname, desc, cost, true);
		this.atk = atk;
		this.mat = mat;
		this.spd = spd;
		this.className = className;
	}
	//getters
	public String getName() {
		return this.name;
	}
	public String getDesc() {
		return desc;
	}
	public String[] getclassName() {
		return this.className;
	}
	public int getATK() {
		return this.atk;
	}
	public int getMAT() {
		return this.mat;
	}
	public int getSPD() {
		return this.spd;
	}
	public int getCost() {
		return this.cost;
	}
}
