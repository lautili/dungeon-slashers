package io.github.dungeon_slashers;

import com.badlogic.gdx.Screen;

import io.github.dungeon_slashers.controllers.InputMan;
import io.github.dungeon_slashers.controllers.Menu;
import io.github.dungeon_slashers.entities.Hero;
import io.github.dungeon_slashers.item.Armor;
import io.github.dungeon_slashers.item.Item;
import io.github.dungeon_slashers.item.Weapon;

/*
		CLASE PLAYER

	sera la clase encargada de controlar los inventarios, el dinero y los personajes como conjunto.

*/

public class Player {
	private Item[] inventory; //inventario de items
	private Weapon[] weapons; //inventario de armas
	private Armor[] armors; //inventario de armaduras
	private Hero[] characters; //los personajes
	public String currScreen;
	public PlayerState state;
	public boolean[] flags = new boolean[1]; // las flags
	public int gold;
	
	//constructor
	public Player() {
		this.inventory = new Item[0];
		this.weapons = new Weapon[0];
		this.armors = new Armor[0];
		this.characters = new Hero[4];
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
			}else {
				System.out.println("Este arma no es compatible con la clase " + hero.getclassName());
			}
		}
	
	//actualizar inventarios
	public void addWeapons(Weapon weapon) {
		//Crea un nuevo array con un espacio extra y agrega la nueva arma en ese lugar
		int length = weapons.length;
		Weapon[] temp = weapons;
		weapons = new Weapon[length+1];
		for(int i = 0; i < length; i++) {
			weapons[i] = temp[i];
		}
		weapons[length] = weapon;
		System.out.println("Se agrego el arma");
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
			}else {
				System.out.println("No se encontro ese arma en el inventario.");
			}
		}
	}
	
	public void addArmors(Armor armor) {
		//Crea un nuevo array con un espacio extra y agrega la nueva armadura en ese lugar
		int length = armors.length;
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
			}else {
				System.out.println("No se encontró esa armadura en el inventario.");
			}
		}
	}
	
	//Si no se especifica la cantidad, se agrega solo 1
	public void addItems(Item item) {
		boolean check = false;
		//chequea si el item ya se encuentra en el inventario. si lo está, guarda su ubicacion
		for(int i = 0; i < inventory.length; i++) {
			if(inventory[i].getName().equals(item.getName())) {
				inventory[i] = item;
				check = true;
				break;
			}
		}
		if(check) {
			//Si el item esta en el inventario, simplemente agrega uno mas a su cantidad
			item.q++;
		}else {
			item.q = 1;
			//Crea un nuevo array con un espacio extra y agrega el nuevo item en ese lugar
			int length = inventory.length;
			Item[] temp = inventory;
			inventory = new Item[length+1];
			for(int i = 0; i < length; i++) {
				inventory[i] = temp[i];
			}
			inventory[length] = item;
		}
	}
	//Si se agrega el parametro cantidad, agrega esa cantidad al inventario
	public void addItems(Item item, int q) {
		boolean check = false;
		//chequea si el item ya se encuentra en el inventario. si lo está, guarda su ubicacion
		for(int i = 0; i < inventory.length; i++) {
			if(inventory[i].getName().equals(item.getName())) {
				inventory[i] = item;
				check = true;
				break;
			}
		}
		if(check) {
			//Si el item a� en el inventario, simplemente agrega q a su cantidad
			item.q += q;
		}else {
			item.q = q;
			//Crea un nuevo array con el espacio extra y agrega el nuevo item en ese lugar
			int length = inventory.length;
			Item[] temp = inventory;
			inventory = new Item[length+1];
			for(int i = 0; i < length; i++) {
				inventory[i] = temp[i];
			}
			inventory[length] = item;
		}
	}
	//Si no se especifica la cantidad, se quita solo 1
	public void removeItems(Item item) {
		boolean check = false;
		//chequea si el item ya se encuentra en el inventario. si lo esta, guarda su ubicacion
		for(int i = 0; i < inventory.length; i++) {
			if(inventory[i] == item) {
				check = true;
				break;
			}
		}
		if(check) {
			//Si el item esta en el inventario y tiene mas de 1 unidad, simplemente quita 1 de su cantidad
			if(item.q > 1) {
				item.q--;
			}else {
				item.q = 0;
				//Crea un nuevo array con un espacio menos 
				int length = inventory.length;
				Item[] temp = inventory;
				inventory = new Item[length-1];
				int k = 0;
				for(int i = 0; i < temp.length; i++) {
					if(temp[i] != item) {
						//Siempre y cuando k no sea mayor a la length del array, 
						//y el espacio no esta ocupado por el item a eliminar,
						//	pasara los items del viejo array al nuevo
						if(k < inventory.length) {
							inventory[k] = temp[i];
							k++;
						}
					}
				}
			}
		}else {
			System.out.println("No se encontró el item en el inventario.");
		}		
	}
	//Si se especifica la cantidad, se saca q
		public void removeItems(Item item, int q) {
			boolean check = false;
			//chequea si el item ya se encuentra en el inventario.
			for(int i = 0; i < inventory.length; i++) {
				if(inventory[i] == item) {
					item = inventory[i];
					check = true;
					break;
				}
			}
			if(check) {
				//Si el item esta en el inventario y tiene mas de 1 unidad, simplemente quita q de su cantidad
				if(item.q > q) {
					item.q -= q;
				}else if(item.q == q){
					item.q = 0;
					//Crea un nuevo array con un espacio menos 
					int length = inventory.length;
					Item[] temp = inventory;
					inventory = new Item[length-1];
					int k = 0;
					for(int i = 0; i < temp.length; i++) {
						if(temp[i] != item) {
							//Siempre y cuando k no sea mayor a la length del array, 
							//y el espacio no este ocupado por el item a eliminar,
							//	pasara los items del viejo array al nuevo
							if(k < inventory.length) {
								inventory[k] = temp[i];
								k++;
							}
						}
					}
				}else {
					System.out.println("No se pueden quitar mas items de los que se posee.");
				}
			}else {
				System.out.println("No se encontro el item en el inventario.");
			}		
		}
	
	
	public void loadCharacters() {
		for(int i = 0; i < characters.length; i++) {
			characters[i].loadTextures();
			System.out.println(characters[i].getWeapon().getName());
		}
	}

	
}
