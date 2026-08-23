package io.github.dungeon_slashers.controllers;

import com.badlogic.gdx.graphics.Texture;

import io.github.dungeon_slashers.Main;
import io.github.dungeon_slashers.MenuScrollType;
import io.github.dungeon_slashers.PlayerState;

/*
 * 		CLASE DIALMAN
 * 
 * Esta clase se encarga de controlar los dialogos. su funcionamiento es simple:
 * Vos ingresas los dialogos con su id y el id del dialogo al que saltaran al terminar
 * Al ingresar opciones, ingresas un array de Strings para los mensajes y un array de IDs para los dialogos
 * a los que saltara cada opcion.
 * 
 * Una vez terminados los dialogos, usaria showDialogue que muestra todas las elecciones en sucesion y devuelve el ultimo dialogo
 * mostrado. esto para saber que fue lo que se eligio en el final.
 */

public class DialMan {
	private static DialEvent[] events = new DialEvent[0];
	private static int cont = 0;
	private static int next;
	private static float time;
	
	public static void addDialogue(int id, int idnext, String name, Texture texture, String msg, float time) {
		Dialogue dialogue = new Dialogue(id, name, texture, msg, time);
		DialEvent[] temp = events.clone();
		events = new DialEvent[temp.length + 1];
		for(int i = 0; i < temp.length; i++) {
			events[i] = temp[i];
		}
		events[temp.length] = dialogue; //inserta el dialogo
		dialogue.next = idnext; //le pone el ID del dialogo al que saltara
		temp = null;
		dialogue = null;
	}
	
	public static void addChoice(int id, float time, String msg, String[] choices, int[] ids) {
		Choice choice = new Choice(id, msg, time, choices);
		DialEvent[] temp = events.clone();
		events = new DialEvent[temp.length + 1];
		for(int i = 0; i < temp.length; i++) {
			events[i] = temp[i];
		}
		events[temp.length] = choice;
		choice.next = ids;
		temp = null;
		choice = null;
	}
	public static int showDialogues(Main game, float delta) {
		if(events.length > 0) {
			Main.player.state = PlayerState.BUSY;
			next = showDialogue(game, cont, delta);
			if(next == -1) {
				Main.player.state = PlayerState.IDLE;
				events = new DialEvent[0];
				cont = 0;
				next = 0;
				return 0;
			}else {
				cont = next;
				return cont;
			}
		} 
		return -1;
	}
	private static int showDialogue(Main game, int next, float delta) {
			int i;
			time += delta;
			DialEvent event = events[next];
			if(event instanceof Dialogue) {
				Dialogue dial = (Dialogue) event;
				Menu.showDialogue(game, dial);
				if(!InputMan.checkKey("Z")){
					if(!dial.currMsg.equals(dial.msg) && time > (dial.time / 1000)) {
						dial.currMsg += dial.msg.charAt(dial.nextChar);
						dial.nextChar++;
						time = 0;
					}
					return next;	
				}
				if(!dial.currMsg.equals(dial.msg)) {
					dial.currMsg = dial.msg;
					return next;
				}else {
					if(events.length > 1) {
						next = dial.next;
					}else {
						next = -1;
					}
				}
			}else {
				Choice choice = (Choice) event;
				Menu.showChoice(game, choice);
				if(!InputMan.checkKey("Z")){
					if(!choice.currMsg.equals(choice.msg) && time > (choice.time / 1000)) {
						choice.currMsg += choice.msg.charAt(choice.nextChar);
						choice.nextChar++;
						time = 0;
					}
					if(choice.currMsg.equals(choice.msg)) {
						Menu.showChoices(game, choice);
						choice.currChoice = InputMan.scrollInt(MenuScrollType.VERTICAL, choice.getChoices().length, choice.currChoice);
					}
					return next;	
				}
				if(!choice.currMsg.equals(choice.msg)) {
					choice.currMsg = choice.msg;
					return next;
				}else {
					if(events.length > 1) {
						next = choice.next[choice.currChoice];
					}else {
						next = -1;
					}
				}
			}
			if(next <= 0) {
				return -1;
			}
			i = getEvent(next);
			return i;
	}
	//para conseguir el evento al que se va a saltar en la lista
	private static int getEvent(int id) {
	    for(int i = 0; i < events.length; i++) {
	        if(events[i].id == id)
	            return i;
	    }
	    return 0;
	}
}
