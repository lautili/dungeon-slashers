package io.github.dungeon_slashers.entities;

import io.github.dungeon_slashers.Main;
import io.github.dungeon_slashers.Skill;

/*
 * 		CLASE BOSS
 * Clase hija de Enemy, simplemente para contar los bosses
 */

public class Boss extends Enemy{
	private BossEvent[] events;
	private int bossID;
	public Boss(String name, String IDname, String baseType, int hp, int mp, int sp, int atk, int def, int mat, int mdf, int spd,
			int xp, int gld, int priority, double PHY, double RAN, double FIR, double WAT, double WIN, double EAR,
			Skill attack, Skill defend, int bossID) {
		super(name, IDname, baseType, hp, mp, sp, atk, def, mat, mdf, spd, xp, gld, priority, PHY, RAN, FIR, WAT, WIN, EAR, attack,
				defend);
		this.bossID = bossID;
	}
	
	//settear los Eventos de Boss
	public void setEvents(BossEvent... events) {
		this.events = events;
	}
	public BossEvent checkEvents(Main game, int turn, float delta) {
		for(int i = 0; i < events.length; i++) {
			if(events[i].checkBossEvent(game, this, turn, delta)) {
				return events[i];
			}
		}
		return null;
	}

	public void setDefeat() {
		Main.player.getBosses()[bossID] = true;
	}
}
