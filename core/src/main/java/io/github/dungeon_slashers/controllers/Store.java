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
	public static Item[] items = new Item[0];
	
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
	public static void buy(Item item) {
					if(player.gold < item.getCost()) {
						
					}else {
						player.gold -= item.getCost();
						if (item instanceof Weapon) {
							player.addWeapons((Weapon) item);
						}else if (item instanceof Armor){
							player.addArmors((Armor) item);
						}else {
							player.addItems(item);
						}
						if(item.unique) {
							removeItem(item);
						}
					}
	}
	
	public static void buy(Item item, int q) {
		if(player.gold < item.getCost() * q) {
			
		}else {
			player.gold -= item.getCost() * q;
			player.addItems(item, q);
		}
	}
	
	public static void sell(Item item) {
		
			player.gold += item.getCost();
			if (item instanceof Weapon) {
				player.subWeapons((Weapon) item);
			}else if(item instanceof Armor){
				player.subArmors((Armor) item);
			}else {
				player.removeItems(item);
			}
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
			int index = getItemIndex(item);
			int k = 0;
			if(index == -1) {
				return;
			}
			items = new Item[length-1];
			for(int i = 0; i < temp.length; i++) {
				if(i != index) {
					if(k < items.length) {
						items[k] = temp[i];
						k++;
					}
				}
		}
	}
	private static int getItemIndex(Item item) {
		for(int i = 0; i < items.length; i++) {
			Item item1 = items[i];
			if(item1 == null) continue;
			if(item1.getIDName().equals(item.getIDName())) {
				return i;
			}
		}
		return -1;
	}
	public static void setItems(Item[] items2) {
		items = items2;
	}
	
}
