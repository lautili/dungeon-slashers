package io.github.dungeon_slashers.item;

import io.github.dungeon_slashers.Player;
import io.github.dungeon_slashers.controllers.Menu;
import io.github.dungeon_slashers.entities.Entity;

/*
		CLASE ITEM

	sera la clase que se encargue de manejar items consumibles, como pociones.

*/

public class Item {
	protected String name;
	public static transient final int TYPE_ITEM = 0;
	public static transient final int TYPE_WEAPON = 1;
	public static transient final int TYPE_ARMOR = 2;
	public int typeIt;
	protected String IDname;
	protected String desc;
	protected int cost;
	public boolean unique; //si al comprarse desaparece de la tienda
	
	public static transient final int USE_MENU = 0;
	public static transient final int USE_BATTLE = 1;
	public static transient final int USE_BOTH = 2;
	public static transient final int USE_NONE = 4;
	
	private int use; 
	private int skillType; //el tipo que deber� tener la skill cuando se use con este item
	//la cantidad que se encuentra en el inventario, solo usada al operar en el inventario
	public int q;
	public boolean canDead;
	public int maxQ;
	public boolean grabbed;
	
	//constructores
	public Item(String name, String IDname, String desc, int cost, boolean unique) { //para armas y armaduras
		this.name = name;
		this.IDname = IDname;
		this.desc = desc;
		this.cost = cost;
		this.unique = unique;
		this.typeIt = TYPE_ITEM;
	}
	public Item(String name, String IDname, String desc, int cost, int maxQ, int type, boolean unique, int use, boolean canDead) { //para items de verdad
		this.name = name;
		this.IDname = IDname;
		this.desc = desc;
		this.skillType = type;
		this.cost = cost;
		this.use = use;
		this.canDead = canDead;
		this.maxQ = maxQ;
	}
	
	//getters
	public String getName() {
		return name;
	}
	public String getDesc() {
		return desc;
	}
	public int getType() {
		return skillType;
	}
	public int getCost() {
		return cost;
	}
	public int getUse() {
		return use;
	}
	public String getIDName() {
		return IDname;
	}
	
	public String Use(Entity a, Player player, boolean isBattle) {
		boolean use = false;
		String msg = "Placeholder message"; //esto es en caso de que hayan errores
		String errMsg = "Placeholder error message"; //esto es en caso de que hayan errores
		
		if(isBattle) {
			a.attacked = true;
			a.lastDamageTaken = 0;
			if(this.use == USE_BOTH || this.use == USE_BATTLE) {
				use = true;
			}
		}else {
			if(this.use == USE_BOTH || this.use == USE_MENU) {
				use = true;
			}
		}
		
		if(use == false) {
			return "No se puede usar el item";
		}
		int dmg = 0;
		switch(IDname) {
		//Al no saber usar Interfaces aun, tendra que ser suficiente un enorme switch por item	
		case "minHPot":
			if(a.hp<a.getHP()) {
				dmg = (int) (a.getHP() * 0.2 + 5);
				a.modHP(dmg);
				msg = ( a.getName() + " recupera " + (int) (a.getHP() * 0.2 + 5) + " HP! (" + a.hp + "/" + a.getHP() + ")");
			}else {
				 use = false;
				 errMsg = "La vida esta al maximo!";
				}
			break;
			
		case "minMPot":
			if(a.mp<a.getMP()) {
				dmg = (int) (a.getMP() * 0.2 + 10);
				a.modMP(dmg);
				msg = ( a.getName() + " recupera " + (int) (a.getMP() * 0.2 + 10) + " MP! (" + a.mp + "/" + a.getMP() + ")");
			}else {
				 use = false;
				 errMsg = "La Mana esta al maximo!";
				}
			break;
			
		case "minSPot":
			if(a.sp<a.getSP()) {
				dmg = (int) (a.getSP() * 0.2 + 10);
				a.modSP(dmg);
				msg = ( a.getName() + " recupera " + (int) (a.getSP() * 0.2 + 10) + " MS! (" + a.sp + "/" + a.getSP() + ")");				
			}else {
			 use = false;
			 errMsg = "La Stamina esta al maximo!";
			}
			break;
		case "minHPven":
			if(a.hp>0) {
				dmg =  -(int) (a.getHP() * 0.2 + 10);
				a.modHP(dmg);
				msg = ( a.getName() + " pierde " + (int) (a.getHP() * 0.2 + 10) + " HP! (" + a.hp + "/" + a.getHP() + ")");				
			}else {
			 use = false;
			 errMsg = "El enemigo esta muerto!";
			}
			break;
		case "minMPven":
			if(a.mp>0) {
				dmg =  -(int) (a.getMP() * 0.2 + 10);
				a.modMP(dmg);
				msg = ( a.getName() + " pierde " + (int) (a.getMP() * 0.2 + 10) + " MP! (" + a.mp + "/" + a.getMP() + ")");				
			}else {
			 use = false;
			 errMsg = "El enemigo no tiene maná!";
			}
			break;
		case "minSPven":
			if(a.sp>0) {
				dmg =  -(int) (a.getSP() * 0.2 + 10);
				a.modSP(dmg);
				msg = ( a.getName() + " pierde " + (int) (a.getSP() * 0.2 + 10) + " HP! (" + a.sp + "/" + a.getSP() + ")");				
			}else {
			 use = false;
			 errMsg = "El enemigo no tiene stamina!";
			}
			break;
		}
		if(use) {
			if(isBattle) {
				a.lastDamageTaken += dmg;
			}
			player.removeItems(this); //si se puede usar "saca" el item
			return msg;
		}else {
			return errMsg;
		}
	}
}
