package io.github.dungeon_slashers.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;

import io.github.dungeon_slashers.Main;
import io.github.dungeon_slashers.MenuScrollType;

public class InputMan {
	public static Main game;
	private static float timer = 0;
	public static float[] movement(Screen lastScreen, Main game) {
		float speed = 150f;
		float[] floats = new float[2];
		if(Gdx.input.isKeyPressed(Input.Keys.C)) {
			speed = 200f;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			floats[1] = speed;
		}else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			floats[1] = -speed;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			floats[0] = -speed;
		}else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			floats[0] = speed;
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.B)) {
			Save.save();
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.X)) {
			game.menuScreen.lastScreen = lastScreen;
			game.menuScreen.updateGame(game);
			lastScreen.pause();
			game.setScreen(game.menuScreen);
		}
		return floats;
	}
	public static int scanInt() {
		return 0;
	}
	public static int scanInt(int max) {
		return 0;
	}
	public static int scanInt(int min, int max) {
		return 0;
	}
	public static int scrollInt(MenuScrollType type, int max, int curr, Sound sound) {
		max--;
		switch(type) {
		case VERTICAL:
			if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
				curr--;
				sound.play(Main.config.volume * 0.5f);
			}else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
				curr++;
				sound.play(Main.config.volume * 0.5f);
			}
			break;
		case VERTICAL_INVERTED:
			if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
				curr++;
				sound.play(Main.config.volume * 0.5f);
			}else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
				curr--;
				sound.play(Main.config.volume * 0.5f);
			}
			break;
		case HORIZONTAL:
			if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
				curr--;
				sound.play(Main.config.volume * 0.5f);
			}else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
				curr++;
				sound.play(Main.config.volume * 0.5f);
			}
			break;
		}
		if(curr < 0) {
			curr = max;
		}
		if(curr > max) {
			curr = 0;
		}
		return curr;
	}
	public static int[] scrollInt(int maxi, int maxj, int[] pos, Sound sound) {
		maxi--;
		maxj--;
		if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			pos[0]--;
			pos[1] = 0;
			sound.play(Main.config.volume * 0.5f);
		}else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			pos[0]++;
			pos[1] = 0;
			sound.play(Main.config.volume * 0.5f);
		}else if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			pos[1]--;
			sound.play(Main.config.volume * 0.5f);
		}else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			pos[1]++;
			sound.play(Main.config.volume * 0.5f);
		}
		
		if(pos[0] > maxi) {
			pos[0] = 0;
		}else if (pos[0] < 0) {
			pos[0] = maxi;
		}
		if(pos[1] > maxj) {
			pos[1] = 0;
		}else if (pos[1] < 0) {
			pos[1] = maxj;
		}
		
		return pos;
	}
	public static int[] scrollMenu(int maxi, int maxj, int maxk, int[] pos, boolean restart, Sound sound) {
		maxi--;
		maxj--;
		maxk--;
		if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			pos[0]--;
			pos[1] = 0;
			sound.play(Main.config.volume * 0.5f);
		}else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			pos[0]++;
			pos[1] = 0;
			sound.play(Main.config.volume * 0.5f);
		}else if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			pos[1]--;
			sound.play(Main.config.volume * 0.5f);
		}else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			pos[1]++;
			sound.play(Main.config.volume * 0.5f);
		}else if(Gdx.input.isKeyJustPressed(Input.Keys.A)) {
			if(pos[0] < 4) {
				pos[2]--;
			}
			sound.play(Main.config.volume * 0.5f);
			if(restart) pos[1] = 0;
		}else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
			if(pos[0] < 4) {
				pos[2]++;
			}
			sound.play(Main.config.volume * 0.5f);
			if(restart) pos[1] = 0;
		}
		
		if(pos[0] > maxi) {
			pos[0] = 0;
		}else if (pos[0] < 0) {
			pos[0] = maxi;
		}
		if(pos[1] > maxj) {
			pos[1] = 0;
		}else if (pos[1] < 0) {
			pos[1] = maxj;
		}
		if(pos[2] > maxk) {
			pos[2] = 0;
		}else if (pos[2] < 0) {
			pos[2] = maxk;
		}
		
		return pos;
	}
	public static boolean checkKey(String string) {
		if(Gdx.input.isKeyJustPressed(Input.Keys.valueOf(string))) {
			Menu.MenuOk.play(Main.config.volume * 0.5f);
			return true;
		}
		return false;
	}
	
	public static boolean checkKey(String string, Sound sound) {
		if(Gdx.input.isKeyJustPressed(Input.Keys.valueOf(string))) {
			if(sound != null) {
				sound.play(Main.config.volume * 0.5f);
			}
			return true;
		}
		return false;
	}
	public static boolean checkHold(String string, float time) {
		if(Gdx.input.isKeyPressed(Input.Keys.valueOf(string))) {
			if(timer > time*60) {
				Menu.MenuMove.play(Main.config.volume * 0.5f);
				return true;
			}else {
				timer++;
			}
		}else if(!Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)){
			timer = 0;
		}
		return false;
	}
	public static boolean checkHold(String string, Sound sound, float time) {
		if(Gdx.input.isKeyPressed(Input.Keys.valueOf(string))) {
			if(timer > time*60) {
				if(sound != null) {
					sound.play(Main.config.volume * 0.5f);
				}
				return true;
			}else {
				timer++;
			}
		}else if(!Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)){
			timer = 0;
		}
		return false;
	}
}
