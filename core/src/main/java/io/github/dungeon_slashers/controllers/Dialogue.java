package io.github.dungeon_slashers.controllers;

import com.badlogic.gdx.graphics.Texture;

public class Dialogue extends DialEvent {
	private String name;
	int next;
	Texture portrait;
	public Dialogue(int id, String name, Texture texture, String msg, float time) {
		this.id = id;
		this.msg = msg;
		this.portrait = texture;
		this.currMsg = "";
		this.time = time;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
