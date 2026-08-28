package io.github.dungeon_slashers.controllers;

import io.github.dungeon_slashers.Main;
import io.github.dungeon_slashers.Player;
import io.github.dungeon_slashers.item.Armor;
import io.github.dungeon_slashers.item.Item;
import io.github.dungeon_slashers.item.Weapon;

/*
 * 			CLASE TIENDA
 * 
 * Se encarga de controlar la tienda, no tiene mucho mas.
 * 
 */

public class Store {
	public static Player player;
	private static Item[] items = new Item[0];
	
	// para agregar items a la tienda
	public static void addItems(Item...newItems) {
		if(items.length == 0) {
			items = newItems;
		}else {
			Item[] temp = items.clone();
			items = new Item[temp.length + newItems.length]; //crea con la nueva cantidad de items
			for(int i = 0; i < items.length;i++) {
				if(i < temp.length) {
					items[i] = temp[i];
				}else {
					items[i] = newItems[i - temp.length];
				}
			}
		}
		sortItems();
	}
	// ordenar los items
	private static void sortItems() {
		for(int i = 0; i < items.length; i++) {
			for(int j = 0; j < items.length - i - 1; j++) {
				Item it1 = items[j];
				Item it2 = items[j + 1];
				if(it2.getClass() == Item.class) {
					if(it1.getClass() != Item.class) {
						items[j] = it2;
						items[j + 1] = it1;
					}
				}else if(it2.getClass() == Weapon.class) {
					if(it1.getClass() == Armor.class) {
						items[j] = it2;
						items[j + 1] = it1;
					}
				}
			}
		}
	}
	public static Item[] getItems() {
		return items;
	}

	//controlador de la tienda
	public static void buy(Item item) { //comprar
		/*
				if(!item.unique) {
					Menu.msg("Ingrese la cantidad que va a comprar");
					int q = InputMan.scanInt(1, 9999999);
					if(player.gold < item.getCost() * q) {
						Menu.msg("No tiene suficiente dinero.");
					}else {
						player.gold -= item.getCost() * q;
						player.addItems(item, q);
					}
				}else {
		*/
					if(player.gold < item.getCost()) {
						System.out.println("No tiene suficiente dinero.");
					}else {
						player.gold -= item.getCost();
						if(item.getClass() == Item.class) {
							player.addItems(item);
						}else if (item.getClass() == Weapon.class) {
							player.addWeapons((Weapon) item);
						}else {
							player.addArmors((Armor) item);
						}
						if(item.unique) {
							removeItem(item);
						}
					}
				//}
	}
	
	public static void buy(Item item, int q) {
		if(player.gold < item.getCost() * q) {
			Menu.msg("No tiene suficiente dinero.");
		}else {
			player.gold -= item.getCost() * q;
			player.addItems(item, q);
		}
	}
	
	public static void sell(Item item) { //vender
		/*
		if(item.q > 1) {
			Menu.msg("Ingrese la cantidad que piensa vender:");
			int q = InputMan.scanInt(1, item.q);
			player.gold += item.getCost() * q;
			player.removeItems(item, q);
		}else {
		*/
			player.gold += item.getCost();
			if(item.getClass() == Item.class) {
				player.removeItems(item);
			}else if (item.getClass() == Weapon.class) {
				player.subWeapons((Weapon) item);
			}else {
				player.subArmors((Armor) item);
			}
		//}
		if(item.unique) addItems(item);
	}
	
	public static void sell(Item item, int q) {
		player.gold += item.getCost() * q;
		player.removeItems(item, q);
	}
	
	public static int search(Item item) {
		for(int i = 0; i < items.length; i++) {
			if(item.getIDName().equals(items[i].getIDName())) {
				return i;
			}
		}
		return -1;
	}
	
	private static void removeItem(Item item) {
			int length = items.length;
			Item[] temp = items;
			boolean check = false;
			items = new Item[length-1];
			int k = 0;
			for(int i = 0; i < temp.length; i++) {
				if(temp[i] != item || !check) {
					//Siempre y cuando k no sea mayor a la length del array, 
					//y el espacio no esta ocupado por el item a eliminar,
					//	pasara los items del viejo array al nuevo
					if(k < items.length) {
						items[k] = temp[i];
						k++;
					}
				}else {
					check = true;
				}
		}
	}
	
}
