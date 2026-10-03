package io.github.dungeon_slashers.controllers;

import com.badlogic.gdx.audio.Sound;

public abstract class DialEvent {
	int id;
	float time;
	Sound snd;
	String msg;
	String currMsg;
	int nextChar;
}
