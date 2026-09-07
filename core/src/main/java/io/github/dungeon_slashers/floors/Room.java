package io.github.dungeon_slashers.floors;

public class Room {
	
	public static final int ROOM_START = 0;
	public static final int ROOM_NORMAL = 1;
	public static final int ROOM_TREASURE = 2;
	public static final int ROOM_BOSS = 3;
	public static final int ROOM_SECRET = 4;
	
	public static final int DISC_TOTAL = 2;
	public static final int DISC_PART = 1;
	public static final int DISC_NOT = 0;
	
	public int roomType;
	
	private boolean isTreasureOpen;
	private int bossFlag;
	
	public int discovered;
	
	public Room(int roomType) {
		this.roomType = roomType;
		if(roomType == ROOM_TREASURE) {
			isTreasureOpen = false;
		}
		if(roomType == ROOM_START) {
			discovered = DISC_TOTAL;
		}else {
			discovered = DISC_NOT;
		}
	}
	public Room(int roomType, int bossFlag) {
		this.roomType = roomType;
		this.bossFlag = bossFlag;
	}
	
	public boolean isTreasureOpen() {
		return isTreasureOpen;
	}
}
