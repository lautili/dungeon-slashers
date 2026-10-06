package io.github.dungeon_slashers.controllers;

import io.github.dungeon_slashers.Main;

public class Config {
	public static transient final int RES_640480   = 0;
	public static transient final int RES_800600   = 1;
	public static transient final int RES_1024768  = 2;
	public static transient final int RES_1280720   = 3;
	public static transient final int RES_1280800  = 4;
	public static transient final int RES_1366768  = 5;
	public static transient final int RES_1440900  = 6;
	public static transient final int RES_16801050 = 7;
	public static transient final int RES_19201080 = 8;
	public static transient final int RES_19201200 = 9;
	public static transient final int RES_25601440 = 10;
	
	public static transient final int KEYS_ARROWS = 0;
	public static transient final int KEYS_WASD = 1;
	
	public float volume = 1.0f;
	public float volSFX = 1.0f;
	public float volMUSIC = 1.0f;
	public boolean fullScreen = false;
	public int currRes;
	public transient int resX = 1280;
	public transient int resY = 720;
	public int keyConfig = KEYS_ARROWS;
	public transient String key_interact = "Z";
	public transient  String key_back = "X";
	public transient  String key_alt = "C";
	public transient  String key_up = "UP";
	public transient  String key_down = "DOWN";
	public transient  String key_left = "LEFT";
	public transient  String key_right = "RIGHT";
	public transient  String key_left2 = "A";
	public transient  String key_right2 = "D";
	
	public Config(int res) {
		currRes = res;
	}
	public void switchRes(Main game) {
		switch(currRes) {
		// 16:9
		case Config.RES_1280720:
			resX = 1280;
			resY = 720;
			break;
		case Config.RES_1366768:
			resX = 1366;
			resY = 768;
			break;
		case Config.RES_19201080:
			resX = 1920;
			resY = 1080;
			break;
		case Config.RES_25601440:
			resX = 2560;
			resY = 1440;
			break;
			// 16:10
		case Config.RES_1280800:
			resX = 1280;
			resY = 800;
			break;
		case Config.RES_1440900:
			resX = 1440;
			resY = 900;
			break;
		case Config.RES_16801050:
			resX = 1680;
			resY = 1050;
			break;
		case Config.RES_19201200:
			resX = 1920;
			resY = 1200;
			break;
			// 4:3
		case Config.RES_640480:
			resX = 640;
			resY = 480;
			break;
		case Config.RES_800600:
			resX = 800;
			resY = 600;
			break;
		case Config.RES_1024768:
			resX = 1024;
			resY = 768;
			break;
		}
		game.checkFullscreen();
	}
	
	public void switchKeys() {
		switch(keyConfig) {
		default:
			key_interact = "Z";
			key_back = "X";
			key_alt = "C";
			key_up = "Up";
			key_down = "Down";
			key_left = "Left";
			key_right = "Right";
			key_left2 = "A";
			key_right2 = "D";
			break;
		case KEYS_WASD:
			key_interact = "P";
			key_back = "O";
			key_alt = "L";
			key_up = "W";
			key_down = "S";
			key_left = "A";
			key_right = "D";
			key_left2 = "Q";
			key_right2 = "E";
			break;
		}
	}
	
	public String getRes() {
		String res = "";
		switch(currRes) {
		// 16:9
		case Config.RES_1280720:
			res = "1280 x 720";
			break;
		case Config.RES_1366768:
			res = "1366 x 768";
			break;
		case Config.RES_19201080:
			res = "1920 x 1080";
			break;
		case Config.RES_25601440:
			res = "2560 x 1440";
			break;
			// 16:10
		case Config.RES_1280800:
			res = "1280 x 800";
			break;
		case Config.RES_1440900:
			res = "1440 x 900";
			break;
		case Config.RES_16801050:
			res = "1680 x 1050";
			break;
		case Config.RES_19201200:
			res = "1920 x 1200";
			break;
			// 4:3
		case Config.RES_640480:
			res = "640 x 480";
			break;
		case Config.RES_800600:
			res = "800 x 600";
			break;
		case Config.RES_1024768:
			res = "1024 x 768";
			break;
		}
		return res;
	}
}
