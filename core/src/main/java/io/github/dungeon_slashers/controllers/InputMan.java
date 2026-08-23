package io.github.dungeon_slashers.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;

import io.github.dungeon_slashers.Main;
import io.github.dungeon_slashers.MenuScrollType;
import io.github.dungeon_slashers.PlayerState;
import io.github.dungeon_slashers.screens.MenuScreen;

public class InputMan {
	public static Main game;
	private static MenuScreen menuScreen = new MenuScreen(game);
	public static float[] movement(Screen lastScreen, Main game) {
		float speed = 150f;
		float[] floats = new float[2];
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
			Main.player.state = PlayerState.MENU;
			menuScreen.lastScreen = lastScreen;
			menuScreen.updateGame(game);
			lastScreen.pause();
			game.setScreen(menuScreen);
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
	public static int scrollInt(MenuScrollType type, int max, int curr) {
		max--;
		switch(type) {
		case VERTICAL:
			if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
				curr--;
			}else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
				curr++;
			}
			break;
		case VERTICAL_INVERTED:
			if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
				curr++;
			}else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
				curr--;
			}
			break;
		case HORIZONTAL:
			if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
				curr--;
			}else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
				curr++;
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
	public static int[] scrollInt(int maxi, int maxj, int[] pos) {
		maxi--;
		maxj--;
		if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			pos[0]--;
			pos[1] = 0;
		}else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			pos[0]++;
			pos[1] = 0;
		}else if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			pos[1]--;
		}else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			pos[1]++;
		}
		
		if(pos[0] > maxi) {
			pos[0] = maxi;
		}else if (pos[0] < 0) {
			pos[0] = 0;
		}
		if(pos[1] > maxj) {
			pos[1] = maxj;
		}else if (pos[1] < 0) {
			pos[1] = 0;
		}
		
		return pos;
	}
	public static int[] scrollMenu(int maxi, int maxj, int maxk, int[] pos, boolean restart) {
		maxi--;
		maxj--;
		maxk--;
		if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			pos[0]--;
			pos[1] = 0;
		}else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			pos[0]++;
			pos[1] = 0;
		}else if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			pos[1]--;
		}else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			pos[1]++;
		}else if(Gdx.input.isKeyJustPressed(Input.Keys.A)) {
			pos[2]--;
			if(restart) pos[1] = 0;
		}else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
			pos[2]++;
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
			return true;
		}
		return false;
	}
}
