package io.github.dungeon_slashers.floors;
import java.util.Random;

import io.github.dungeon_slashers.Flags;
import io.github.dungeon_slashers.controllers.Battle;
import io.github.dungeon_slashers.item.Item;

public class Floor {
	public Room[][] layout;
	public int floorType;
	public Battle battle;
	public Item[] itemsPool;
	public Random rand = new Random();
	
	public Floor(int x, int y, Battle battle, int floorType, Item...items) {
		layout = new Room[x][y];
		this.floorType = floorType;
		this.battle = battle;
		generateLayout();
		generateBossRoom();
		showDebugLayout();
	}
	private void generateBossRoom() {
		int x = (int) layout.length / 2;
		int y = (int) layout[0].length / 2;
		int furthestX = 0;
		int furthestY = 0;
		double lastDistance = 0;
		for(int i = 0; i < layout.length; i++) {
			for(int j = 0; j < layout[0].length; j++) {
				if(layout[i][j] != null) {
					if(countAdjacentRooms(i, j) != 3) {
						continue;
					}
					int distanceX = Math.abs(i - x);
					int distanceY = Math.abs(j - y);
					double distance = Math.sqrt(Math.pow((double) distanceX, 2) + Math.pow((double) distanceY, 2));
					if((i == 0 && j == 0) || distance > lastDistance) {
						furthestX = i;
						furthestY = j;
						lastDistance = distance;
					}
				}
			}
		}
		layout[furthestX][furthestY] = new Room(Room.ROOM_BOSS, Flags.FLAG_FIRSTBOSS_DEFEATED);
	}
	public void generateLayout() {
		int x = (int) layout.length / 2;
		int y = (int) layout[0].length / 2;
		layout[x][y] = new Room(Room.ROOM_START);
		generateRooms(x, y);
	}
	private void generateRooms(int x, int y) {
		if(countAdjacentRooms(x, y) == 0) {
			return;
		}
		int rooms = rand.nextInt(countAdjacentRooms(x, y)) + 1;
		int roomsPlaced = 0;
		int count = 0;
		do {
			int i = rand.nextInt(4);
			switch (i) {
			case 0:
				if( y > 0 && checkRoom(x, y-1)) {
					if(x - 1 >= 0 && (layout[x-1][y-1] == null ||  layout[x-1][y-1].roomType != Room.ROOM_START)) {
						createRoom(x, y-1);
						roomsPlaced++;
						generateRooms(x, y-1);
					}
				}else {
					count++;
				}
				break;
			case 1:
				if(y < layout[0].length - 1 && checkRoom(x, y+1)) {
					if(x - 1 >= 0 && (layout[x-1][y+1] == null || layout[x-1][y+1].roomType != Room.ROOM_START)) {
						createRoom(x, y+1);
						roomsPlaced++;
						generateRooms(x, y+1);
					}
				}else {
					count++;
				}
				break;
			case 2:
				if(x < layout.length - 1 && checkRoom(x+1, y) && layout[x][y].roomType != Room.ROOM_START) {
					if(x - 2 >= 0 && (layout[x-2][y] == null ||  layout[x-2][y].roomType != Room.ROOM_START)) {
						createRoom(x+1, y);
						roomsPlaced++;
						generateRooms(x+1, y);
					}
				}else {
					count++;
				}
				break;
			case 3:
				if(x > 0 && checkRoom(x-1, y) && (layout[x-1][y] == null || layout[x-1][y].roomType != Room.ROOM_START)) {
					createRoom(x-1, y);
					roomsPlaced++;
					generateRooms(x-1, y);
				}else {
					count++;
				}
				break;
			}
		}while(roomsPlaced < rooms && count < 6);
	}
	private boolean checkRoom(int x, int y) {
		if(layout[x][y] != null) {
	        return false;
	    }

	    // Arriba-izquierda
	    if(x > 0 && y > 0) {
	        if(layout[x-1][y] != null &&
	           layout[x][y-1] != null &&
	           layout[x-1][y-1] != null) {
	            return false;
	        }
	    }

	    // Arriba-derecha
	    if(x < layout.length - 1 && y > 0) {
	        if(layout[x+1][y] != null &&
	           layout[x][y-1] != null &&
	           layout[x+1][y-1] != null) {
	            return false;
	        }
	    }

	    // Abajo-izquierda
	    if(x > 0 && y < layout[0].length - 1) {
	        if(layout[x-1][y] != null &&
	           layout[x][y+1] != null &&
	           layout[x-1][y+1] != null) {
	            return false;
	        }
	    }

	    // Abajo-derecha
	    if(x < layout.length - 1 && y < layout[0].length - 1) {
	        if(layout[x+1][y] != null &&
	           layout[x][y+1] != null &&
	           layout[x+1][y+1] != null) {
	            return false;
	        }
	    }

	    return true;
	}
	
	private void createRoom(int x, int y) {
		double num = rand.nextInt(1000) / 10;
		if(num > 10) {
			layout[x][y] = new Room(Room.ROOM_NORMAL);
		}else if(num > 0.5) {
			layout[x][y] = new Room(Room.ROOM_TREASURE);
		}else {
			if(!exists(Room.ROOM_SECRET)) {
				layout[x][y] = new Room(Room.ROOM_SECRET);
			}else {
				layout[x][y] = new Room(Room.ROOM_NORMAL);
			}
		}
		System.out.println("Creada habitacion en x " + x + " y " + y);
	}
	
	public void showDebugLayout() {
		for(int i = 0; i < layout.length; i++) {
			for(int j = 0; j < layout[0].length; j++) {
				if(layout[i][j] != null) {
					switch(layout[i][j].roomType) {
					case Room.ROOM_START:
						System.out.print("[S]");
						break;
					case Room.ROOM_NORMAL:
						System.out.print("[ ]");
						break;
					case Room.ROOM_BOSS:
						System.out.print("[B]");
						break;
					case Room.ROOM_SECRET:
						System.out.print("[?]");
						break;
					case Room.ROOM_TREASURE:
						System.out.print("[T]");
						break;
					}
				}else {
						System.out.print("   ");
				}
			}
			System.out.println("");
		}
	}
	
	private boolean exists(int roomType) {
		for(int i = 0; i < layout.length; i++) {
			for(int j = 0; j < layout[0].length; j++) {
				if(layout[i][j] != null && layout[i][j].roomType == roomType) {
					return true;
				}
			}
		}
		return false;
	}
	private int countAdjacentRooms(int x, int y) {
		int i = 0;
		if(x-1 < 0 || layout[x-1][y] == null) i++;
		if(x+1 >= layout.length || layout[x+1][y] == null) i++;
		if(y-1 < 0 || layout[x][y-1] == null) i++;
		if(y+1 >= layout[0].length || layout[x][y+1] == null) i++;
		return i;
	}
	
	public int[] getRoomPosition(int roomType) {
		for(int i = 0; i < layout.length; i++) {
			for(int j = 0; j < layout[0].length; j++) {
				if(layout[i][j] != null && layout[i][j].roomType == roomType) {
					return new int[] {i, j};
				}
			}
		}
		return null;
	}
}
