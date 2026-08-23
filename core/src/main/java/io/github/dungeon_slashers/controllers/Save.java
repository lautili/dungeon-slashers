package io.github.dungeon_slashers.controllers;

import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import io.github.dungeon_slashers.Main;
import io.github.dungeon_slashers.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Save {
	private static Gson gson = new Gson();
	private static FileHandle saveFile = Gdx.files.local("save.json");
	public static Main game;
	
	public static void save() {
		String json = gson.toJson(Main.player);
		saveFile.writeString(json, false); // false = sobrescribir
	}
	public static Boolean load() {
		if (saveFile.exists()) {
		    String json = saveFile.readString();
		    Main.player = gson.fromJson(json, Player.class);
		    Main.player.loadCharacters();
		    Main.updateArrays();
		    game.setScreenFromSave(Main.player.currScreen);
		    return true;
		}
		return false;
	}
	public static boolean exists() {
            if (saveFile.exists()) {
                return true;
            }
		return false;
	}
}
