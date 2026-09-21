package io.github.dungeon_slashers.controllers;
import java.util.Random;

import io.github.dungeon_slashers.Effect;
import io.github.dungeon_slashers.Main;
import io.github.dungeon_slashers.Player;
import io.github.dungeon_slashers.Skill;
import io.github.dungeon_slashers.entities.Boss;
import io.github.dungeon_slashers.entities.BossEvent;
import io.github.dungeon_slashers.entities.Enemy;
import io.github.dungeon_slashers.entities.Entity;
import io.github.dungeon_slashers.entities.Hero;
import io.github.dungeon_slashers.item.Item;
/*
		CLASE BATTLE

será la clase encargada de controlar las batallas.

*/
public class Battle {
	
	//todo lo necesario
	private Player player;
	public Enemy[] possibleEnemy; //lista de enemigos posibles
	public boolean isBoss; //(si es un jefe o no)
	private transient Random rand; //Random para aleatoriedades
	private int xp; //xp total
	private int gld; //oro total
	private int quant; //cantidad de enemigos posibles en el combate
		
	public Battle(boolean isBoss, int quant, Enemy...enemies) {
		this.possibleEnemy = enemies;
		this.isBoss = isBoss;
		this.quant = quant;
		rand = new Random();
	}
	
	//Inicializa a los enemigos
	public Enemy[] initEnemies() {
		Enemy[] enemies;
		if(!isBoss) {
		xp = 0;
		gld = 0;
			enemies = new Enemy[rand.nextInt(quant) + 1]; //genera la cantidad de enemigos. varia del 1 al 3
			for(int i = 0; i < enemies.length; i++) {
				if(possibleEnemy.length > 1) {
					enemies[i] = getRandomEnemy();
				}else {
					enemies[i] = new Enemy(possibleEnemy[0]);
				}
			}
		}else {
			enemies = possibleEnemy;
		}
		return enemies;
	}
	
	//funcion para calcular a los enemigos randoms por su prioridad
	private Enemy getRandomEnemy() {
		 int total = 0;

		 // Suma todas las probabilidades (aunque siempre son 100, por las dudas)
		 for (int i = 0; i < possibleEnemy.length; i++) {
		     total += possibleEnemy[i].getPriority();
		 }

		 int r = rand.nextInt(total);

		 int acum = 0;

		 // Busca en que rango cayo el numero
		 for (int i = 0; i < possibleEnemy.length; i++) {
		     acum += possibleEnemy[i].getPriority(); //va sumando las posibilidades de cada enemigo para ver 
		     										//	si r cayo en alguno

		     if (r < acum) {
		         return new Enemy(possibleEnemy[i]); //si r finalmente es menor que acum, devuelve a ese enemigo
		     }
		 }
		 return null;
	}

}
