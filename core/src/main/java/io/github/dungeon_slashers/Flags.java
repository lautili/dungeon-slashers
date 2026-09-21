package io.github.dungeon_slashers;

import io.github.dungeon_slashers.controllers.DialMan;
import io.github.dungeon_slashers.entities.Entity;

public class Flags {
	public static final int FLAG_FIRSTSCREEN_DIALOGUE_START = 0;
	public static final int FLAG_FIRSTBOSS_DEFEATED = 1;
	public static final int FLAG_FIRSTBOSS_DIALOGUE = 2;
	
	public static void DialogueFlag(int flag, Entity entity) {
		switch(flag) {
		case FLAG_FIRSTBOSS_DIALOGUE:
			DialMan.addDialogue(0, 1, "???", null, "Hola...", 20);
        	DialMan.addDialogue(1, 2, entity.getName(), null, "Soy... Ogro...", 20);
        	DialMan.addDialogue(2, 100, entity.getName(), null, "Preparense... porque lo que se viene...", 20);
        	DialMan.addDialogue(100, -1, entity.getName(), null, "No es para nada bonito.", 100);
			break;
		}
	}
}
