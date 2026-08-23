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
	protected String IDname;
	protected String desc;
	protected int cost;
	public boolean unique; //si al comprarse desaparece de la tienda
	private boolean useMenu; //si se puede usar en el menu
	private int skillType; //el tipo que deber� tener la skill cuando se use con este item
	//la cantidad que se encuentra en el inventario, solo usada al operar en el inventario
	public int q;
	
	//constructores
	public Item(String name, String IDname, String desc, int cost, boolean unique) { //para armas y armaduras
		this.name = name;
		this.IDname = IDname;
		this.desc = desc;
		this.cost = cost;
		this.unique = unique;
	}
	public Item(String name, String IDname, String desc, int cost, int type, boolean unique, boolean useMenu) { //para items de verdad
		this.name = name;
		this.IDname = IDname;
		this.desc = desc;
		this.skillType = type;
		this.cost = cost;
		this.useMenu = useMenu;
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
	public boolean getMenu() {
		return useMenu;
	}
	public String getIDName() {
		return IDname;
	}
	
	public void Use(Entity a, Player player) {
		boolean use = true;
		String msg = "Placeholder message"; //esto es en caso de que hayan errores
		String errMsg = "Placeholder error message"; //esto es en caso de que hayan errores
		switch(IDname) {
		//Al no saber usar Interfaces aun, tendra que ser suficiente un enorme switch por item	
		case "minHPot":
			if(a.hp<a.getHP()) {
				a.modHP((int) (a.getHP() * 0.2 + 5));
				msg = ( a.getName() + " recupera " + (int) (a.getHP() * 0.2 + 5) + " HP! (" + a.hp + "/" + a.getHP() + ")");
			}else {
				 use = false;
				 errMsg = "La vida esta al maximo! No se uso la pocion.";
				}
			break;
			
		case "minMPot":
			if(a.mp<a.getMP()) {
				a.modMP((int) (a.getMP() * 0.2 + 10));
				msg = ( a.getName() + " recupera " + (int) (a.getMP() * 0.2 + 10) + " MP! (" + a.mp + "/" + a.getMP() + ")");
			}else {
				 use = false;
				 errMsg = "La Mana esta al maximo! No se uso la pocion.";
				}
			break;
			
		case "minSPot":
			if(a.sp<a.getSP()) {
				a.modSP((int) (a.getSP() * 0.2 + 10));
				msg = ( a.getName() + " recupera " + (int) (a.getSP() * 0.2 + 10) + " MS! (" + a.sp + "/" + a.getSP() + ")");				
			}else {
			 use = false;
			 errMsg = "La Stamina esta al maximo! No se uso la pocion.";
			}
			break;
		case "minHPven":
			if(a.hp>0) {
				a.modHP( -(int) (a.getHP() * 0.2 + 10));
				msg = ( a.getName() + " pierde " + (int) (a.getHP() * 0.2 + 10) + " MS! (" + a.sp + "/" + a.getHP() + ")");				
			}else {
			 use = false;
			 errMsg = "El enemigo est� muerto! No se uso la pocion.";
			}
			break;
		}
		if(use) {
			player.removeItems(this); //si se puede usar "saca" el item
			Menu.msg(msg); // e imprime el mensaje de uso
		}else {
			Menu.msg(errMsg); //si no se puede usar imprime el mensaje de error
		}
	}
}
