package io.github.dungeon_slashers.controllers;

import com.badlogic.gdx.audio.Sound;

public class Choice extends DialEvent {
	private String[] choices;
	int[] next;
	int currChoice;
	public Choice(int id, String msg, float time, String[] choices, Sound snd) {
		this.id = id;
		this.msg = msg;
		this.choices = choices;
		this.time = time;
		this.currMsg = "";
		this.currChoice = 0;
		this.snd = snd;
	}
	public String[] getChoices() {
		return choices;
	}
}
