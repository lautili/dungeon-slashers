package io.github.dungeon_slashers;

import com.badlogic.gdx.Screen;

import io.github.dungeon_slashers.controllers.InputMan;
import io.github.dungeon_slashers.controllers.Menu;
import io.github.dungeon_slashers.entities.Enemy;
import io.github.dungeon_slashers.entities.Hero;
import io.github.dungeon_slashers.item.Armor;
import io.github.dungeon_slashers.item.Item;
import io.github.dungeon_slashers.item.Weapon;

public class Player {
	private Item[] inventory;
	private Weapon[] weapons;
	private Armor[] armors;
	private Hero[] characters;
	private Enemy[] bestiary;
	public String currScreen;
	public PlayerState state;
	public boolean[] flags = new boolean[Flags.LENGTH];
	public int gold;
	
	//constructor
	public Player() {
		this.inventory = new Item[0];
		this.weapons = new Weapon[0];
		this.armors = new Armor[0];
		this.setCharacters(new Hero[4]);
		this.gold = 100;
		state = PlayerState.IDLE;
	}
	
	//getters
	public Item[] getInventory() {
		return this.inventory;
	}
	public Weapon[] getWeapons() {
		return this.weapons;
	}
	public Armor[] getArmors() {
		return this.armors;
	}
	public Hero[] getCharacters() {
		return this.characters;
	}
	public Enemy[] getBestiary() {
		return bestiary;
	}
	public void setBestiary(Enemy[] enemies) {
		bestiary = enemies;
	}
	public void updateBestiary(Enemy enemy) {
		for(Enemy enemy2 : bestiary) {
			if(enemy2.getIDName().equals(enemy.getIDName())) {
				enemy2.defeated = enemy.defeated;
				enemy2.discovered = enemy.discovered;
				enemy2.setWeaknesses(enemy.getWeaknesses());
			}
		}
	}
	
	//cambiar armadura de un heroe
	public void changeArmors(Hero hero, Armor armor) {
		Armor newAr = hero.changeArmor(armor);
		for(int i = 0; i < armors.length; i++) {
			if (armors[i] == armor) {
				armors[i] = newAr;
				break;
			}
		}
	}
	//cambiar arma de un heroe
		public void changeWeapons(Hero hero, Weapon weapon) {
			Weapon newWp = hero.changeWeapon(weapon);
			if(newWp != null) {
				for(int i = 0; i < this.weapons.length; i++) {
					if (this.weapons[i] == weapon) {
						this.weapons[i] = newWp;
						break;
					}
				}
			}
		}
	
	//actualizar inventarios
	public void addWeapons(Weapon weapon) {
		weapon.grabbed = true;
		//Crea un nuevo array con un espacio extra y agrega la nueva arma en ese lugar
		int length = weapons.length;
		Weapon[] temp = weapons;
		weapons = new Weapon[length+1];
		for(int i = 0; i < length; i++) {
			weapons[i] = temp[i];
		}
		weapons[length] = weapon;
	}
	public void subWeapons(Weapon weapon) {
		if(weapons.length > 0) {
			boolean proceed = false;
			//chequea que el arma se encuentre en el inventario
			for(int i = 0; i < weapons.length; i++) {
				if(weapons[i] == weapon) {
					proceed = true;
					break;
				}
			}
			if(proceed) {
				//Crea un nuevo array con un espacio menos 
				int length = this.weapons.length;
				Weapon[] temp = this.weapons;
				boolean check = false;
				this.weapons = new Weapon[length-1];
				int k = 0;
				for(int i = 0; i < temp.length; i++) {
					if(temp[i] != weapon || !check) {
						//Siempre y cuando k no sea mayor a la length del array, 
						//y el espacio no este ocupado por el arma a eliminar, 
						//	pasara las armas del viejo array al nuevo
						if(k < this.weapons.length) {
							this.weapons[k] = temp[i];
							k++;
						}
					}else {
						check = true;
					}
				}
			}
		}
	}
	
	public void addArmors(Armor armor) {
		//Crea un nuevo array con un espacio extra y agrega la nueva armadura en ese lugar
		int length = armors.length;
		armor.grabbed = true;
		Armor[] temp = armors;
		armors = new Armor[length+1];
		for(int i = 0; i < length; i++) {
			armors[i] = temp[i];
		}
		armors[length] = armor;
	}
	public void subArmors(Armor armor) {
		if(armors.length > 0) {
			boolean proceed = false;
			//chequea que la armadura se encuentre en el inventario
			for(int i = 0; i < armors.length; i++) {
				if(armors[i] == armor) {
					proceed = true;
					break;
				}
			}
			if(proceed) {
				//Crea un nuevo array con un espacio menos 
				int length = armors.length;
				Armor[] temp = armors;
				boolean check = false;
				armors = new Armor[length-1];
				int k = 0;
				for(int i = 0; i < temp.length; i++) {
					if(temp[i] != armor || !check) {
						//Siempre y cuando k no sea mayor a la length del array, 
						//y el espacio no este ocupado por la armadura a eliminar, 
						//	pasara las armaduras del viejo array al nuevo
						if(k < armors.length) {
							armors[k] = temp[i];
							k++;
						}
					}else {
						check = true;
					}
				}
			}
		}
	}
	
	private int findInventoryIndex(Item item) {
		if(item == null) {
			return -1;
		}
		for(int i = 0; i < inventory.length; i++) {
			if(inventory[i] == item || (inventory[i] != null && inventory[i].getIDName().equals(item.getIDName()))) {
				return i;
			}
		}
		return -1;
	}
	
	//Si no se especifica la cantidad, se agrega solo 1
	public void addItems(Item item) {
		if(item == null) {
			return;
		}
		item.grabbed = true;
		int index = findInventoryIndex(item);
		if(index >= 0) {
			inventory[index] = item;
			item.q++;
			return;
		}
		item.q = 1;
		int length = inventory.length;
		Item[] temp = inventory;
		inventory = new Item[length + 1];
		for(int i = 0; i < length; i++) {
			inventory[i] = temp[i];
		}
		inventory[length] = item;
	}
	//Si se agrega el parametro cantidad, agrega esa cantidad al inventario
	public void addItems(Item item, int q) {
		if(item == null || q <= 0) {
			return;
		}
		item.grabbed = true;
		int index = findInventoryIndex(item);
		if(index >= 0) {
			inventory[index] = item;
			item.q += q;
			return;
		}
		item.q = q;
		int length = inventory.length;
		Item[] temp = inventory;
		inventory = new Item[length + 1];
		for(int i = 0; i < length; i++) {
			inventory[i] = temp[i];
		}
		inventory[length] = item;
	}
	//Si no se especifica la cantidad, se quita solo 1
	public void removeItems(Item item) {
		if(item == null) {
			return;
		}
		int index = findInventoryIndex(item);
		if(index < 0) {
			return;
		}
		Item target = inventory[index];
		if(target.q > 1) {
			target.q--;
			return;
		}
		target.q = 0;
		int length = inventory.length;
		Item[] temp = inventory;
		inventory = new Item[length - 1];
		int k = 0;
		for(int i = 0; i < temp.length; i++) {
			if(i != index) {
				if(k < inventory.length) {
					inventory[k] = temp[i];
					k++;
				}
			}
		}
	}
	//Si se especifica la cantidad, se saca q
	public void removeItems(Item item, int q) {
		if(item == null || q <= 0) {
			return;
		}
		int index = findInventoryIndex(item);
		if(index < 0) {
			return;
		}
		Item target = inventory[index];
		if(target.q > q) {
			target.q -= q;
			return;
		}
		target.q = 0;
		int length = inventory.length;
		Item[] temp = inventory;
		inventory = new Item[length - 1];
		int k = 0;
		for(int i = 0; i < temp.length; i++) {
			if(i != index) {
				if(k < inventory.length) {
					inventory[k] = temp[i];
					k++;
				}
			}
		}
	}
	
	
	public void loadCharacters() {
		for(int i = 0; i < characters.length; i++) {
			characters[i].loadTextures();
		}
	}

	public void setCharacters(Hero[] characters) {
		this.characters = characters;
	}
	
}
