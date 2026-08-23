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
	private static Player player;
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

	//controlador de la tienda
	public static void shop(Main game) {
		boolean leave = false;
		
		do {
			player = Main.player;
			//Menu.showOptions("Que desea hacer?", true, "Comprar", "Vender");
			switch(InputMan.scanInt(2)) {
			case 0:
				leave = true;
				break;
			case 1:
				buy(game);
				break;
			case 2:
				sell();
				break;
			}
		}while(!leave);
	}
	private static void buy(Main game) { //comprar
		do {
			Menu.msg("GLD: " + player.gold);
			Menu.showItems(items);
			Menu.showItemOptions("Que va a comprar?", items);
			int opt = InputMan.scanInt(items.length);
			if(opt == 0) {
				break;
			}
			opt--;
			Item item = items[opt];
				Menu.showItemStats(game, item);
			//Menu.showOptions("Seguro que desea comprar este item?", false, "Si", "No");
			if(InputMan.scanInt(1, 2) == 1) {
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
					if(player.gold < item.getCost()) {
						Menu.msg("No tiene suficiente dinero.");
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
				}
			}
		}while(true);
	}
	private static void sell() { //vender
		do {
			Item[] temp = new Item[player.getInventory().length + player.getWeapons().length + player.getArmors().length];
			for(int i = 0; i < temp.length; i++) {
				if(i < player.getInventory().length) {
					temp[i] = player.getInventory()[i];
				}else if(i < player.getWeapons().length) {
					temp[i] = player.getWeapons()[i - player.getInventory().length];
				}else {
					temp[i] = player.getArmors()[i - player.getInventory().length - player.getWeapons().length];
				}
			}
			Menu.msg("GLD: " + player.gold);
			Menu.showPlayItems(temp);
			Menu.showItemOptions("Que va a vender?", temp);
			int opt = InputMan.scanInt(temp.length);
			if(opt == 0) {
				break;
			}
			opt--;
			Item item = temp[opt];
			//Menu.showOptions("Seguro que desea vender este item?", false, "Si", "No");
			if(InputMan.scanInt(1, 2) == 1) {
				if(item.q > 1) {
					Menu.msg("Ingrese la cantidad que piensa vender:");
					int q = InputMan.scanInt(1, item.q);
					player.gold += item.getCost() * q;
					player.removeItems(item, q);
				}else {
					player.gold += item.getCost();
					if(item.getClass() == Item.class) {
						player.removeItems(item);
					}else if (item.getClass() == Weapon.class) {
						player.subWeapons((Weapon) item);
					}else {
						player.subArmors((Armor) item);
					}
				}
				addItems(item);
			}
		}while(true);
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
