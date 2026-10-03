package io.github.dungeon_slashers.entities;

import com.badlogic.gdx.graphics.Texture;

import io.github.dungeon_slashers.Main;
import io.github.dungeon_slashers.Skill;

/*
 * 		CLASE BOSS
 * Clase hija de Enemy, simplemente para contar los bosses
 */

public class Boss extends Enemy{
	private transient BossEvent[] events;
	private transient int bossFlag;
	public transient Texture bossIdle;
	public transient BossEvent finalEvent;
	public Boss(String name, String IDname, String baseType, int hp, int mp, int sp, int atk, int def, int mat, int mdf, int spd,
			int xp, int gld, int priority, double PHY, double RAN, double FIR, double WAT, double WIN, double EAR,
			Skill attack, Skill defend, int bossFlag) {
		super(name, IDname, baseType, hp, mp, sp, atk, def, mat, mdf, spd, xp, gld, priority, PHY, RAN, FIR, WAT, WIN, EAR, attack,
				defend);
		this.bossFlag = bossFlag;
		try {
			bossIdle = new Texture("sprites/enemies/" + IDname + "-down.png");
		}catch(Exception e) {
			bossIdle = new Texture("sprites/enemies/ogre-down.png");
		}
	}
	
	//settear los Eventos de Boss
	public void setEvents(BossEvent... events) {
		this.events = events;
		for(int i = 0; i < events.length;i++) {
			if(events[i].getHP() && events[i].getNum() == 0) {
				finalEvent = events[i];
			}
		}
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
		
	}

	public void activateFlag() {
		Main.player.flags[bossFlag] = true;
	}
	public int getFlag() {
		return bossFlag;
	}
}
