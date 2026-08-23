package io.github.dungeon_slashers.entities;

import io.github.dungeon_slashers.Main;
import io.github.dungeon_slashers.Skill;
import io.github.dungeon_slashers.controllers.DialMan;

/*
 * 		CLASE BOSSEVENT
 * 
 * clases para los eventos de los bosses
 * 
 */

public class BossEvent {
	private boolean isHP; //si va a chequear la vida del enemigo o los turnos de la batalla
	private int num; //el porcentaje de vida o el turno en el que hara el evento
	private Skill skill; //la skill que usara
	private String[] msgs; //los dialogos que dira
	private boolean check; //un check para asegurarse de que no se repita el mismo turno

	public BossEvent(boolean isHP, int num, Skill skill) {
		this.isHP = isHP;
		this.num = num;
		this.skill = skill;
	}
	public BossEvent(boolean isHP, int num, String... msgs) {
		this.isHP = isHP;
		this.num = num;
		this.msgs = msgs;
	}
	public BossEvent(boolean isHP, int num, Skill skill, String... msgs) {
		this.isHP = isHP;
		this.num = num;
		this.skill = skill;
		this.msgs = msgs;
	}
	
	//getters
	public Skill getSkill() {
		return skill;
	}
	public String[] getMsgs() {
		return msgs;
	}
	
	public boolean checkBossEvent(Main game, Boss boss, int turn, float delta) {
		if(!check) {
			if(isHP) {
				if( boss.hp <= (int) (boss.getHP() * ((double) (num) / 100))) { //checks para ver que tenga menos de num% de vida
					check = true;
					for(int i = 0; i < msgs.length; i++) {
						int i2;
						if(i+1 == msgs.length) {
							i2 = 0;
						}else {
							i2 = i+1;
						}
						DialMan.addDialogue(i, i2, boss.name, null, msgs[i], 2); //codigo sencillo para hacer los dialogos
					}
					if(msgs.length > 0) {
						DialMan.showDialogues(game, delta);
					}
					return true;
				}
			}else {
				if(turn == num) { //chequea que sea ese turno
					check = true;
					for(int i = 0; i < msgs.length; i++) {
						int i2;
						if(i+1 == msgs.length) {
							i2 = 0;
						}else {
							i2 = i+1;
						}
						DialMan.addDialogue(i, i2, boss.name, null, msgs[i], 2);
					}
					if(msgs.length > 0) {
						DialMan.showDialogues(game, delta);
					}
					return true;
				}
			}
		}
		return false;
	}
}
