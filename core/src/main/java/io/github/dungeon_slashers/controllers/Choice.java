package io.github.dungeon_slashers.controllers;

public class Choice extends DialEvent {
	private String[] choices;
	int[] next;
	int currChoice;
	public Choice(int id, String msg, float time, String[] choices) {
		this.id = id;
		this.msg = msg;
		this.choices = choices;
		this.time = time;
		this.currMsg = "";
		this.currChoice = 0;
	}
	public String[] getChoices() {
		return choices;
	}
}
