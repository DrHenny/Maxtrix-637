package com.rs.game.player.content;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.cores.CoresManager;
import com.rs.game.RegionBuilder;
import com.rs.game.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

public final class Dungeoneering2 {

	/*
	 * generate settings, repeat rooms false for better dungs, but for that you
	 * need alot rooms handled
	 */
	public static final boolean REPEAT_ROOMS = true;

	public static final int ROTATIONS_COUNT = 4;

	/*
	 * objects handling
	 */
	public static final int[] DUNGEON_DOORS = new int[] { 50342 },
			DUNGEON_EXITS = new int[] { 51156 };

	/*
	 * floor types
	 */
	public static final int FROZEN_FLOORS = 0, ABANDONED_FLOORS = 1,
			FURNISHED_FLOORS = 2, ABANDONED2_FLOORS = 3, OCCULT_FLOORS = 4,
			WARPED_FLOORS = 5;

	/*
	 * dungeon sizes
	 */
	public static final int SMALL_DUNGEON = 0, MEDIUM_DUNGEON = 1,
			LARGE_DUNGEON = 2;
	/*
	 * door directions
	 */
	public static final int EAST_DOOR = 0, WEST_DOOR = 1, NORTH_DOOR = 2,
			SOUTH_DOOR = 3;

	public static final StartRoom[][] START_ROOMS = new StartRoom[][] {
	// FROZEN_FLOORS
	new StartRoom[] {
	// new StartRoom(new int[] {1,2,3,4,5,6}, 14, 632, EAST_DOOR, WEST_DOOR,
	// NORTH_DOOR, SOUTH_DOOR)
	new StartRoom(new int[] { 1, 2, 3, 4, 5, 6 }, 14, 624, SOUTH_DOOR) } };

	public static Room getRandomStartRoom(int floorType, int complexity) {
		ArrayList<Room> rooms = new ArrayList<Room>();
		for (StartRoom room : START_ROOMS[floorType]) {
			if (room.isComplexity(complexity))
				rooms.add(room);
		}
		return (Room) getRandomValue(rooms.toArray());
	}

	public static Object getRandomValue(Object[] objectArray) {
		return objectArray[Utils.getRandom(objectArray.length - 1)];
	}

	public static final NormalRoom[][] NORMAL_ROOMS = new NormalRoom[][] {
	// FROZEN_FLOORS
	new NormalRoom[] {
			new NormalRoom(1, 8, 240, SOUTH_DOOR),
			new NormalRoom(1, 10, 240, SOUTH_DOOR),
			new NormalRoom(1, 12, 240, SOUTH_DOOR)

			,
			new NormalRoom(1, 8, 242, SOUTH_DOOR, WEST_DOOR),
			new NormalRoom(1, 10, 242, SOUTH_DOOR, WEST_DOOR),
			new NormalRoom(1, 12, 242, SOUTH_DOOR, WEST_DOOR)

			,
			new NormalRoom(1, 8, 244, NORTH_DOOR, SOUTH_DOOR),
			new NormalRoom(1, 10, 244, NORTH_DOOR, SOUTH_DOOR),
			new NormalRoom(1, 12, 244, NORTH_DOOR, SOUTH_DOOR)

			,
			new NormalRoom(1, 8, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
			new NormalRoom(1, 10, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
			new NormalRoom(1, 12, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR)

			,
			new NormalRoom(1, 8, 248, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR,
					EAST_DOOR),
			new NormalRoom(1, 10, 248, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR,
					EAST_DOOR),
			new NormalRoom(1, 12, 248, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR,
					EAST_DOOR)

			,
			new NormalRoom(1, 14, 240, SOUTH_DOOR),
			new NormalRoom(1, 16, 240, SOUTH_DOOR),
			new NormalRoom(1, 18, 240, SOUTH_DOOR),
			new NormalRoom(1, 20, 240, SOUTH_DOOR),
			new NormalRoom(1, 22, 240, SOUTH_DOOR)

			,
			new NormalRoom(1, 24, 242, SOUTH_DOOR, WEST_DOOR),
			new NormalRoom(1, 26, 242, SOUTH_DOOR, WEST_DOOR)

			,
			new NormalRoom(1, 28, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR)

			,
			new NormalRoom(1, 30, 244, NORTH_DOOR, SOUTH_DOOR),
			new NormalRoom(1, 32, 244, NORTH_DOOR, SOUTH_DOOR)

			,
			new NormalRoom(1, 34, 242, SOUTH_DOOR, WEST_DOOR)

			,
			new NormalRoom(1, 40, 244, NORTH_DOOR, SOUTH_DOOR),
			new NormalRoom(1, 42, 244, NORTH_DOOR, SOUTH_DOOR),
			new NormalRoom(1, 44, 244, NORTH_DOOR, SOUTH_DOOR),
			new NormalRoom(1, 46, 244, NORTH_DOOR, SOUTH_DOOR),
			new NormalRoom(1, 48, 244, NORTH_DOOR, SOUTH_DOOR),
			new NormalRoom(1, 50, 244, NORTH_DOOR, SOUTH_DOOR)

			,
			new NormalRoom(1, 38, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR)

			,
			new NormalRoom(1, 36, 248, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR,
					EAST_DOOR)

			,
			new NormalRoom(1, 52, 240, SOUTH_DOOR),
			new NormalRoom(1, 56, 240, SOUTH_DOOR),
			new NormalRoom(1, 56, 240, SOUTH_DOOR),
			new NormalRoom(1, 58, 240, SOUTH_DOOR),
			new NormalRoom(1, 60, 240, SOUTH_DOOR)

			,
			new NormalRoom(1, 62, 240, SOUTH_DOOR),
			new NormalRoom(1, 64, 240, SOUTH_DOOR),
			new NormalRoom(1, 66, 240, SOUTH_DOOR),
			new NormalRoom(1, 68, 240, SOUTH_DOOR),
			new NormalRoom(1, 70, 240, SOUTH_DOOR),
			new NormalRoom(1, 72, 240, SOUTH_DOOR),
			new NormalRoom(1, 74, 240, SOUTH_DOOR),
			new NormalRoom(1, 76, 240, SOUTH_DOOR),
			new NormalRoom(1, 78, 240, SOUTH_DOOR)

			,
			new NormalRoom(1, 62, 242, SOUTH_DOOR, WEST_DOOR),
			new NormalRoom(1, 64, 242, SOUTH_DOOR, WEST_DOOR),
			new NormalRoom(1, 66, 242, SOUTH_DOOR, WEST_DOOR),
			new NormalRoom(1, 68, 242, SOUTH_DOOR, WEST_DOOR),
			new NormalRoom(1, 70, 242, SOUTH_DOOR, WEST_DOOR),
			new NormalRoom(1, 72, 242, SOUTH_DOOR, WEST_DOOR),
			new NormalRoom(1, 74, 242, SOUTH_DOOR, WEST_DOOR),
			new NormalRoom(1, 76, 242, SOUTH_DOOR, WEST_DOOR),
			new NormalRoom(1, 78, 242, SOUTH_DOOR, WEST_DOOR)

			,
			new NormalRoom(1, 62, 244, NORTH_DOOR, SOUTH_DOOR),
			new NormalRoom(1, 64, 244, NORTH_DOOR, SOUTH_DOOR),
			new NormalRoom(1, 66, 244, NORTH_DOOR, SOUTH_DOOR),
			new NormalRoom(1, 68, 244, NORTH_DOOR, SOUTH_DOOR),
			new NormalRoom(1, 70, 244, NORTH_DOOR, SOUTH_DOOR),
			new NormalRoom(1, 72, 244, NORTH_DOOR, SOUTH_DOOR),
			new NormalRoom(1, 74, 244, NORTH_DOOR, SOUTH_DOOR),
			new NormalRoom(1, 76, 244, NORTH_DOOR, SOUTH_DOOR),
			new NormalRoom(1, 78, 244, NORTH_DOOR, SOUTH_DOOR)

			,
			new NormalRoom(1, 62, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
			new NormalRoom(1, 64, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
			new NormalRoom(1, 66, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
			new NormalRoom(1, 68, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
			new NormalRoom(1, 70, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
			new NormalRoom(1, 72, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
			new NormalRoom(1, 74, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
			new NormalRoom(1, 76, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
			new NormalRoom(1, 78, 246, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR)

			,
			new NormalRoom(1, 62, 248, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR,
					EAST_DOOR),
			new NormalRoom(1, 64, 248, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR,
					EAST_DOOR),
			new NormalRoom(1, 66, 248, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR,
					EAST_DOOR),
			new NormalRoom(1, 68, 248, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR,
					EAST_DOOR),
			new NormalRoom(1, 70, 248, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR,
					EAST_DOOR),
			new NormalRoom(1, 72, 248, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR,
					EAST_DOOR),
			new NormalRoom(1, 74, 248, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR,
					EAST_DOOR),
			new NormalRoom(1, 76, 248, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR,
					EAST_DOOR),
			new NormalRoom(1, 78, 248, NORTH_DOOR, SOUTH_DOOR, WEST_DOOR,
					EAST_DOOR) } };

	public static final BossRoom[][] BOSS_ROOMS = new BossRoom[][] {
	// FROZEN_FLOORS
	new BossRoom[] { new BossRoom("Icy Bones", 1, 24, 626, SOUTH_DOOR) } };

	/*
	 * TODO dungLevel for boss
	 */
	public static Room getRandomBossRoom(int floorType, int dungLevel) {
		return (Room) getRandomValue(BOSS_ROOMS[floorType]);
	}

	// TODO
	public static final Room[][] PUZZLE_ROOMS = new Room[][] {};

	public static void main(String[] args) { // Test
		long start = Utils.currentTimeMillis();
		long start2 = Utils.currentTimeMillis();
		DungeonStructure smallDungeon = generateDungeonStructure(FROZEN_FLOORS,
				1, SMALL_DUNGEON, 120);
		System.out.println("Took: " + (Utils.currentTimeMillis() - start)
				+ " ms.");
		System.out.println("Small dungeon has: " + smallDungeon.getRoomsCount()
				+ " rooms.");
		start = Utils.currentTimeMillis();
		DungeonStructure mediumDungeon = generateDungeonStructure(
				FROZEN_FLOORS, 1, MEDIUM_DUNGEON, 120);
		System.out.println("Took: " + (Utils.currentTimeMillis() - start)
				+ " ms.");
		System.out.println("Medium dungeon has: "
				+ mediumDungeon.getRoomsCount() + " rooms.");
		start = Utils.currentTimeMillis();
		DungeonStructure largeDungeon = generateDungeonStructure(FROZEN_FLOORS,
				1, LARGE_DUNGEON, 120);
		System.out.println("Took: " + (Utils.currentTimeMillis() - start)
				+ " ms.");
		System.out.println("Large dungeon has: " + largeDungeon.getRoomsCount()
				+ " rooms.");
		System.out.println("TotalTime: " + (Utils.currentTimeMillis() - start2)
				+ " ms.");
	}

	/*
	 * makes up to 10 trys to generate random dungeon structures, if all them
	 * too small returns biggest one
	 */
	public static DungeonStructure generateDungeonStructure(int floorType,
			int complexity, int size, int lowestDungLevel) {
		int tryId = 0;
		DungeonStructure lastStructure = null;
		int[] ratio = getRatio(size);
		int minimumRooms = ratio[0] * ratio[1] / 2; // half of max dungeon rooms
													// at least
		while (tryId++ < 10) { // up to 10trys
			DungeonStructure structure = generateRandomDungeonStructure(
					floorType, complexity, size, lowestDungLevel);
			if (structure == null)
				continue;
			if (structure.getRoomsCount() >= minimumRooms)
				return structure;
			if (lastStructure == null
					|| lastStructure.getRoomsCount() < structure
							.getRoomsCount())
				lastStructure = structure;
		}
		return lastStructure;
	}

	public static int[] getRatio(int size) {
		int ratioX;
		int ratioY;
		if (size == SMALL_DUNGEON) {
			ratioX = 4;
			ratioY = 4;
		} else if (size == MEDIUM_DUNGEON) {
			ratioX = 4;
			ratioY = 8;
		} else {
			ratioX = 8;
			ratioY = 8;
		}
		return new int[] { ratioX, ratioY };
	}

	public static DungeonStructure generateRandomDungeonStructure(
			int floorType, int complexity, int size, int lowestDungLevel) {
		int[] ratio = getRatio(size);
		DungeonStructure structure = new DungeonStructure(floorType,
				complexity, ratio[0], ratio[1]);
		Room startRoom = getRandomStartRoom(floorType, complexity);
		if (startRoom == null)
			return null;
		Room bossRoom = getRandomBossRoom(floorType, lowestDungLevel);
		if (bossRoom == null)
			return null;
		int[] place = (int[]) getRandomValue(getPossiblePlaces(startRoom,
				ratio[0], ratio[1]));
		if (place == null)
			return null;
		int[] bossPlace = (int[]) getRandomValue(getPossiblePlaces(place,
				bossRoom, ratio[0], ratio[1]));
		if (bossPlace == null)
			return null;
		structure.addRoom(place[0], place[1], startRoom, place[2]);
		structure.addRoom(bossPlace[0], bossPlace[1], bossRoom, place[2]);
		/*
		 * if(!structure.validate()) return null;
		 */
		return structure;
	}

	private static Object[] getPossiblePlaces(int[] usedPlace, Room room,
			int ratioX, int ratioY) {
		// x,y,rotation
		ArrayList<int[]> possiblePlaces = new ArrayList<int[]>();
		for (int x = 0; x < ratioX; x++)
			for (int y = 0; y < ratioY; y++) {
				if (usedPlace != null && usedPlace[0] == x && usedPlace[1] == y)
					continue;
				for (int rotation = 0; rotation < ROTATIONS_COUNT; rotation++) {
					if (checkPlaceRoomBounds(x, y, room, ratioX, ratioY,
							rotation))
						possiblePlaces.add(new int[] { x, y, rotation });
				}
			}
		return possiblePlaces.toArray();
	}

	private static Object[] getPossiblePlaces(Room room, int ratioX, int ratioY) {
		return getPossiblePlaces(null, room, ratioX, ratioY);
	}

	// validate dungeon
	public static final int IMPOSSIBLE_DUNGEON = 0, RECHECK_DUNGEON = 1,
			VALID_DUNGEON = 2;

	public static ArrayList<Object[]> getPossibleNormalRooms(int x, int y,
			int floorType, int complexity, DungeonStructure structure) {
		ArrayList<Object[]> possibleRooms = new ArrayList<Object[]>();
		NormalRoom[] rooms = NORMAL_ROOMS[floorType];
		for (NormalRoom room : rooms) {
			if (!room.isComplexity(complexity))
				continue;
			for (int rotation = 0; rotation < ROTATIONS_COUNT; rotation++) {
				if (!structure.isPossible(x, y, room, rotation))
					continue;
				possibleRooms.add(new Object[] { room, rotation });
			}
		}
		return possibleRooms;
	}

	public static final class DungeonStructure {

		private Room[][] rooms;
		private int[][] rotations;
		private int floorType;
		private int complexity;
		private int roomsCount;

		public DungeonStructure(int floorType, int complexity, int ratioX,
				int ratioY) {
			this.floorType = floorType;
			this.complexity = complexity;
			rooms = new Room[ratioX][ratioY];
			rotations = new int[ratioX][ratioY];
		}

		public Room[][] getRooms() {
			return rooms;
		}

		public int[][] getRotations() {
			return rotations;
		}

		public void addRoom(int x, int y, Room room, int rotation) {
			rooms[x][y] = room;
			rotations[x][y] = rotation;
			roomsCount++;
		}

		public int[] getStartRoomPos() {
			for (int x = 0; x < rooms.length; x++) {
				for (int y = 0; y < rooms[x].length; y++) {
					if (rooms[x][y] != null && rooms[x][y] instanceof StartRoom) {
						return new int[] { x, y };
					}
				}
			}
			return new int[] { 0, 0 };
		}

		public boolean validate() {
			while (true) {
				int opcode = tryValidate();
				if (opcode == IMPOSSIBLE_DUNGEON)
					return false;
				else if (opcode == VALID_DUNGEON)
					return true;
			}
		}

		private int tryValidate() {
			for (int x = 0; x < rooms.length; x++) {
				for (int y = 0; y < rooms[x].length; y++) {
					if (rooms[x][y] != null) {
						int opcode = validateRoom(x, y);
						if (opcode != VALID_DUNGEON)
							return opcode;
					}
				}
			}
			return VALID_DUNGEON;
		}

		private boolean isPossible(int x, int y) {
			return isPossible(x, y, rooms[x][y], rotations[x][y]);
		}

		public boolean isPossible(int x, int y, Room room, int rotation) {
			return checkPlaceRoomBounds(x, y, room, rooms.length,
					rooms[x].length, rotation);
		}

		private boolean containsRoom(Room room) {
			for (int x = 0; x < rooms.length; x++)
				for (int y = 0; y < rooms[x].length; y++)
					if (rooms[x][y] != null && rooms[x][y].equals(room))
						return true;
			return false;
		}

		/*
		 * return false if no room found
		 */

		private boolean addRoom(int x, int y) {
			ArrayList<Object[]> possibleNormalRooms = getPossibleNormalRooms(x,
					y, floorType, complexity, this);
			if (!REPEAT_ROOMS) {
				ArrayList<Object[]> cleanedPossibleNormalRooms = new ArrayList<Object[]>();
				for (Object[] room : possibleNormalRooms) {
					if (!containsRoom((Room) room[0]))
						cleanedPossibleNormalRooms.add(room);
				}
				possibleNormalRooms = cleanedPossibleNormalRooms;
			}
			if (possibleNormalRooms.isEmpty())
				return false;
			Object[] room = (Object[]) getRandomValue(possibleNormalRooms
					.toArray());
			addRoom(x, y, (Room) room[0], (Integer) room[1]);
			return true;
		}

		private int validateRoom(int x, int y) {
			if (!isPossible(x, y))
				return IMPOSSIBLE_DUNGEON;
			Room room = rooms[x][y];
			int rotation = rotations[x][y];
			if (room.hasWestDoor(rotation))
				if (rooms[x - 1][y] == null)
					return addRoom(x - 1, y) ? RECHECK_DUNGEON
							: IMPOSSIBLE_DUNGEON;
			if (room.hasSouthDoor(rotation))
				if (rooms[x][y - 1] == null)
					return addRoom(x, y - 1) ? RECHECK_DUNGEON
							: IMPOSSIBLE_DUNGEON;
			if (room.hasEastDoor(rotation))
				if (rooms[x + 1][y] == null)
					return addRoom(x + 1, y) ? RECHECK_DUNGEON
							: IMPOSSIBLE_DUNGEON;
			if (room.hasNorthDoor(rotation))
				if (rooms[x][y + 1] == null)
					return addRoom(x, y + 1) ? RECHECK_DUNGEON
							: IMPOSSIBLE_DUNGEON;
			return VALID_DUNGEON;
		}

		public int getRoomsCount() {
			return roomsCount;
		}

	}

	public static boolean checkPlaceRoomBounds(int x, int y, Room room,
			int ratioX, int ratioY, int rotation) {
		if (x == 0 && room.hasWestDoor(rotation))
			return false;
		if (y == 0 && room.hasSouthDoor(rotation))
			return false;
		if (x == ratioX - 1 && room.hasEastDoor(rotation))
			return false;
		if (y == ratioY - 1 && room.hasNorthDoor(rotation))
			return false;
		return true;
	}

	private static final class BossRoom extends Room {

		private String bossName;
		private int requiredLevel;

		private BossRoom(String bossName, int requiredLevel, int regionX,
				int regionY, int... doorsDirections) {
			super(regionX, regionY, doorsDirections);
			this.bossName = bossName;
			this.requiredLevel = requiredLevel;
		}

		@SuppressWarnings("unused")
		public int getRequiredLevel() {
			return requiredLevel;
		}

		@SuppressWarnings("unused")
		public String getBossName() {
			return bossName;
		}

	}

	private static final class NormalRoom extends Room {

		private int complexity;

		private NormalRoom(int complexity, int regionX, int regionY,
				int... doorsDirections) {
			super(regionX, regionY, doorsDirections);
			this.complexity = complexity;
		}

		public boolean isComplexity(int complexity) {
			return this.complexity <= complexity;
		}

	}

	private static class StartRoom extends Room {

		private int[] complexitys;

		private StartRoom(int[] complexitys, int regionX, int regionY,
				int... doorsDirections) {
			super(regionX, regionY, doorsDirections);
			this.complexitys = complexitys;
		}

		public boolean isComplexity(int complexity) {
			for (int c : complexitys)
				if (c == complexity)
					return true;
			return false;
		}
	}

	private static class Room {

		private int regionX;
		private int regionY;
		private int[] doorsDirections;

		private Room(int regionX, int regionY, int... doorsDirections) {
			this.regionX = regionX;
			this.regionY = regionY;
			this.doorsDirections = doorsDirections;
		}

		public int getRegionX() {
			return regionX;
		}

		public int getRegionY() {
			return regionY;
		}

		public boolean hasSouthDoor(int rotation) {
			return hasDoor(rotation == 0 ? SOUTH_DOOR
					: rotation == 1 ? WEST_DOOR : rotation == 2 ? NORTH_DOOR
							: EAST_DOOR);
		}

		public boolean hasNorthDoor(int rotation) {
			return hasDoor(rotation == 0 ? NORTH_DOOR
					: rotation == 1 ? EAST_DOOR : rotation == 2 ? SOUTH_DOOR
							: WEST_DOOR);
		}

		public boolean hasWestDoor(int rotation) {
			return hasDoor(rotation == 0 ? WEST_DOOR
					: rotation == 1 ? NORTH_DOOR : rotation == 2 ? EAST_DOOR
							: SOUTH_DOOR);
		}

		public boolean hasEastDoor(int rotation) {
			return hasDoor(rotation == 0 ? EAST_DOOR
					: rotation == 1 ? SOUTH_DOOR : rotation == 2 ? WEST_DOOR
							: NORTH_DOOR);
		}

		public boolean hasDoor(int direction) {
			for (int dir : doorsDirections)
				if (dir == direction)
					return true;
			return false;
		}
	}

	public static void startDungeon(final int floor, final int complexity,
			final int size, Player... teamArray) {
		new Dungeon(floor, complexity, size, teamArray);
	}

	public static class Dungeon {

		private int floor;
		private int complexity;
		private CopyOnWriteArrayList<Player> team;
		private boolean[][] openedRooms;
		private int openedRoomsCount;
		private DungeonStructure structure;
		private int[] mapBaseCoords;
		private boolean destroyed;
		private boolean started;
		private int size;

		private Dungeon(final int floor, final int complexity, final int size,
				Player[] teamArray) {
			this.floor = floor;
			this.complexity = complexity;
			this.size = size;
			final int[] ratio = getRatio(size);
			openedRooms = new boolean[ratio[0]][ratio[1]];
			team = new CopyOnWriteArrayList<Player>();
			for (Player player : teamArray) {
				team.add(player);
				player.getControlerManager().startControler("DungControler",
						this);
			}
			// slow executor loads dungeon as it may take up to few secs
			CoresManager.slowExecutor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						// generates dungeon structure
						structure = generateDungeonStructure(
								getFloorType(floor), complexity, size,
								getLowestPartDungeonneringLevel());
						// finds an empty map area bounds
						mapBaseCoords = RegionBuilder.findEmptyMap(
								ratio[0] * 2, ratio[1] * 2);
						System.out.println(mapBaseCoords[0] << 3);
						System.out.println(mapBaseCoords[1] << 3);
						// reserves all map area
						RegionBuilder
								.cutMap(mapBaseCoords[0], mapBaseCoords[1],
										ratio[0] * 2, ratio[1] * 2, 0);
						if (!checkRoom(structure.getStartRoomPos())) {
							destroyDungeon();
							return;
						}
						startDungeon();
					} catch (Throwable e) {
						Logger.handle(e);
					}
				}
			});
		}

		public void remove(Player player) {
			team.remove(player);
			if (started) {
				if (team.isEmpty())
					destroyDungeon();
			}
		}

		public void startDungeon() {
			if (team.isEmpty()) {
				destroyDungeon();
				return;
			}
			for (Player player : team) {
				player.stopAll();
				player.setMapSize(0); // biggest map size so less reloading when
										// walking from a part of dungeon to
										// another
				teleHome(player);
				player.getCombatDefinitions().setSpellBook(3); // sets dungeon
																// spellbook
				player.getPackets().sendGameMessage("");
				player.getPackets().sendGameMessage("-Welcome to Daemonheim-");
				player.getPackets().sendGameMessage(
						"Floor " + floor + " Complexity " + complexity
								+ " (Full)");
				player.getPackets().sendGameMessage(
						"Dungeon Size: "
								+ (size == SMALL_DUNGEON ? "Small"
										: size == MEDIUM_DUNGEON ? "Medium"
												: "Large"));
				player.getPackets().sendGameMessage(
						"Party Size:Dificulty " + team.size() + ":"
								+ team.size());
				player.getPackets().sendGameMessage("");
			}
			started = true;
		}

		public void teleHome(Player player) {
			int[] startRoomPos = structure.getStartRoomPos();
			player.setNextWorldTile(new WorldTile(
					((mapBaseCoords[0] << 3) + startRoomPos[0] * 16) + 8,
					((mapBaseCoords[1] << 3) + startRoomPos[1] * 16) + 8, 0));

			System.out.println(player.getNextWorldTile().getX());
			System.out.println(player.getNextWorldTile().getY());
		}

		/*
		 * true doesnt move, false moves
		 */
		public boolean checkRoom(int... pos) {
			if (pos.length != 2)
				return true;
			if (openedRooms[pos[0]][pos[1]])
				return false;
			if (openedRoomsCount >= structure.getRoomsCount()) // cant happen
				return true;
			Room room = structure.getRooms()[pos[0]][pos[1]];
			if (room == null)
				return true;
			openedRooms[pos[0]][pos[1]] = true;
			openedRoomsCount++;
			final int roomRegionX = mapBaseCoords[0] + (pos[0] * 2);
			final int roomRegionY = mapBaseCoords[1] + (pos[1] * 2);
			RegionBuilder.copy2RatioSquare(room.getRegionX(),
					room.getRegionY(), roomRegionX, roomRegionY,
					structure.getRotations()[pos[0]][pos[1]]);
			int regionId = (((roomRegionX / 8) << 8) + (roomRegionY / 8));
			for (Player player : team) {
				if (!player.getMapRegionsIds().contains(regionId)) // if player
																	// is to
																	// far... no
																	// need to
																	// reload,
																	// he will
																	// reload
																	// when walk
																	// to that
																	// room
					continue;
				player.setForceNextMapLoadRefresh(true);
				player.loadMapRegions();
			}
			return true;
		}

		public int[] getCurrentRoomCoords(WorldTile tile) {
			return new int[] { tile.getChunkX() << 3, tile.getChunkY() << 3 };
		}

		public int[] getCurrentRoomPos(WorldTile tile) {
			return new int[] { (tile.getChunkX() - mapBaseCoords[0]) / 2,
					(tile.getChunkY() - mapBaseCoords[1]) / 2 };
		}

		public void destroyDungeon() {
			if (destroyed)
				return;
			destroyed = true;
			// gives 1 game ticket so people can leave
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					int[] ratio = getRatio(size);
					RegionBuilder.destroyMap(mapBaseCoords[0],
							mapBaseCoords[1], ratio[0] * 2, ratio[1] * 2);
				}
			}, 1);
		}

		public int getLowestPartDungeonneringLevel() {
			int lowestDungeonneringLevel = 120;
			for (Player player : team) {
				int dungLevel = player.getSkills().getLevelForXp(
						Skills.DUNGEONEERING);
				if (dungLevel < lowestDungeonneringLevel)
					lowestDungeonneringLevel = dungLevel;
			}
			return lowestDungeonneringLevel;
		}

		public boolean hasStarted() {
			return started;
		}

		public boolean isDestroyed() {
			return destroyed;
		}

	}

	public static int getFloorType(int floor) {
		return FROZEN_FLOORS; // TODO
	}

	private Dungeoneering2() {

	}
}
