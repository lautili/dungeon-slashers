package io.github.dungeon_slashers.controllers;

import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import io.github.dungeon_slashers.Main;
import io.github.dungeon_slashers.Player;
import io.github.dungeon_slashers.item.Item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Save {
	private static Gson gson = new Gson();
	private static FileHandle saveFile = Gdx.files.local("save.json");
	private static FileHandle shopFile = Gdx.files.local("store.json");
	public static FileHandle configFile = Gdx.files.local("config.json");
	public static Main game;
	
	public static void save() {
		String json = gson.toJson(Main.player);
		saveFile.writeString(json, false); // false = sobrescribir
		json = gson.toJson(Store.getItems());
		shopFile.writeString(json, false);
	}
	public static Boolean load() {
		if (saveFile.exists()) {
		    String json = saveFile.readString();
		    Player temp = gson.fromJson(json, Player.class);
		    try {
			    Item[] temp2 = gson.fromJson(shopFile.readString(), Item[].class);
			    Store.setItems(temp2);
			}catch(Exception e) {
				
			}
		    boolean[] oldFlags = temp.flags;
		    boolean[] newFlags = new boolean[Main.player.flags.length];
		    if(Main.player.flags.length > temp.flags.length) {
		    	for(int i = 0; i < newFlags.length; i++) {
		    		if(i >= oldFlags.length) {
		    			continue;
		    		}else {
		    			newFlags[i] = oldFlags[i];
		    		}
		    	}
		    }else if(Main.player.flags.length < temp.flags.length) {
		    	for(int i = 0; i < newFlags.length; i++) {
		    			newFlags[i] = oldFlags[i];
		    	}
		    }else {
		    	newFlags = oldFlags;
		    }
		    Main.player = temp;
		    Main.player.flags = newFlags;
		    Main.player.loadCharacters();
		    Main.updateArrays();
		    game.setScreen(game.firstScreen);
		    
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
	public static boolean loadConfig() {
		try {
			String json = configFile.readString();
		    Config temp = gson.fromJson(json, Config.class);
		    Main.config = temp;
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	public static void saveConfig(Config config) {
		String json = gson.toJson(config);
		configFile.writeString(json, false);
	}
}
