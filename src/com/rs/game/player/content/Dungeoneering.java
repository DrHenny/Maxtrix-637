package com.rs.game.player.content;

import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.RegionBuilder;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.npc.dungeonnering.DungeonBoss;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.content.dungeoneering.*;
import com.rs.game.player.content.dungeoneering.rooms.HandledRoom;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.utils.*;

public final class Dungeoneering {

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
	 * door directions
	 */
	public static final int EAST_DOOR = 0, WEST_DOOR = 1, NORTH_DOOR = 2,
			SOUTH_DOOR = 3;

	public static final StartRoom[] START_ROOMS = new StartRoom[] {
	// FROZEN_FLOOR
	new StartRoom(822, 14, 624, SOUTH_DOOR) };

	public static Object getRandomValue(Object[] objectArray) {
		return objectArray[Utils.getRandom(objectArray.length - 1)];
	}

	public static final BossRoom[][] BOSS_ROOMS = new BossRoom[][] {
	// FROZEN_FLOORS
	new BossRoom[] {
			// Gluttonous behemoth
			new BossRoom(new RoomEvent() {
				@Override
				public void openRoom(Dungeon dungeon, int[] pos) {
					WorldTile base = dungeon.getBaseCoords(pos);
					World.spawnObject(new WorldObject(49283, 10, 3,
							base.getX() - 7, base.getY() + 4, 0), true);
					if (dungeon.getPartySize() >= 2)
						World.spawnObject(
								new WorldObject(49283, 10, 2, base.getX() + 3,
										base.getY() + 4, 0), true);
					int npcId = getBossId(1,
							dungeon.getStartPartyTotalCombatLevel(),
							"Gluttonous behemoth");
					DungeonBoss boss = new DungeonBoss(npcId, new WorldTile(
							base.getX() - 3, base.getY() + 2, 0), dungeon);
					boss.setCantFollowUnderCombat(true);
				}
			}, 817, 1, 28, 624, SOUTH_DOOR),
			// Astea Frostweb
			new BossRoom(new RoomEvent() {
				@Override
				public void openRoom(Dungeon dungeon, int[] pos) {
					int npcId = getBossId(1,
							dungeon.getStartPartyTotalCombatLevel(),
							"Astea Frostweb");
					WorldTile base = dungeon.getBaseCoords(pos);
					// new AsteaFrostweb(npcId, new WorldTile(base.getX() - 1,
					// base.getY() - 4, 0), dungeon);
				}
			}, 816, 1, 30, 624, SOUTH_DOOR) } };

	public static int getBossId(int numberNPCSonRoom,
			int startPartyTotalCombatLevel, String npcName) {
		int recommendedLevel = startPartyTotalCombatLevel / numberNPCSonRoom;
		int lastCombatLevelDifference = -1;
		int npcId = -1;
		for (int id = 0; id < Utils.getNPCDefinitionsSize(); id++) {
			NPCDefinitions npcDef = NPCDefinitions.getNPCDefinitions(id);
			if (npcDef.name != null && npcDef.name.equals(npcName)) {
				int difference = npcDef.combatLevel - recommendedLevel;
				if (difference < 0)
					difference = -difference;
				if (lastCombatLevelDifference == -1
						|| difference < lastCombatLevelDifference
						|| (difference == lastCombatLevelDifference && NPCDefinitions
								.getNPCDefinitions(npcId).combatLevel < npcDef.combatLevel)) {
					lastCombatLevelDifference = difference;
					npcId = id;
				}
			}
		}
		return npcId;
	}

	/*
	 * TODO dungLevel for boss
	 */
	public static Room getRandomRoom(int floorType) {
		return (Room) getRandomValue(DungeonConstants.NORMAL_ROOMS[floorType]);
	}

	public static HandledRoom getRandomBossRoom1(int floorType, int dungLevel) {
		return (HandledRoom) getRandomValue(BOSS_ROOMS[floorType]);
	}

	public static Room getRandomBossRoom(int floorType, int dungLevel) {
		return (Room) getRandomValue(BOSS_ROOMS[floorType]);
	}

	public static void main(String[] args) { // Test
		long start = Utils.currentTimeMillis();
		DungeonStructure smallDungeon = generateDungeonStructure(FROZEN_FLOORS,
				1, 120);
		System.out.println("Took: " + (Utils.currentTimeMillis() - start)
				+ " ms.");
		System.out.println("Small dungeon has: " + smallDungeon.getRoomsCount()
				+ " rooms.");
	}

	/*
	 * makes up to 10 trys to generate random dungeon structures, if all them
	 * too small returns biggest one
	 */
	public static DungeonStructure generateDungeonStructure(int floorType,
			int complexity, int lowestDungLevel) {
		int room11 = Misc.random(10);
		int room22 = Misc.random(10);
		int[] ratio = getRatio(0);
		DungeonStructure structure = new DungeonStructure(floorType, ratio[0],
				ratio[1]);
		Room startRoom = START_ROOMS[floorType];
		// Room room1 = getRandomRoom(room11);
		// Room room2 = getRandomRoom(room22);
		Room bossRoom = getRandomBossRoom(floorType, lowestDungLevel);
		if (bossRoom == null)
			return null;
		structure.addRoom(0, 0, startRoom, 2);
		// structure.addRoom(0, 1, room1, 0);
		// structure.addRoom(0, 2, room2, 0);
		structure.addRoom(0, 1, bossRoom, 0);
		return structure;
	}

	public static final class DungeonStructure {

		private Room[][] rooms;
		private int[][] rotations;
		private int roomsCount;
		private int floorType;

		public DungeonStructure(int floorType, int ratioX, int ratioY) {
			rooms = new Room[ratioX][ratioY];
			rotations = new int[ratioX][ratioY];
			this.floorType = floorType;
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

		public int[] getBossRoomPos() {
			for (int x = 0; x < rooms.length; x++) {
				for (int y = 0; y < rooms[x].length; y++) {
					if (rooms[x][y] != null && rooms[x][y] instanceof BossRoom) {
						return new int[] { x, y };
					}
				}
			}
			return new int[] { 0, 0 };
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

		public int getRoomsCount() {
			return roomsCount;
		}

		public int getFloorType() {
			return floorType;
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

	public static interface RoomEvent {

		public void openRoom(Dungeon dungeon, int[] pos);
	}

	private static final class BossRoom extends Room {

		private int requiredLevel;

		private BossRoom(RoomEvent event, int musicId, int requiredLevel,
				int regionX, int regionY, int... doorsDirections) {
			super(event, musicId, regionX, regionY, doorsDirections);
			this.requiredLevel = requiredLevel;
		}

		@SuppressWarnings("unused")
		public int getRequiredLevel() {
			return requiredLevel;
		}

	}

	private static class StartRoom extends Room {

		private StartRoom(int musicId, int regionX, int regionY,
				int... doorsDirections) {
			super(musicId, regionX, regionY, doorsDirections);
		}
	}

	private static class Room {

		private int regionX;
		private int regionY;
		private int[] doorsDirections;
		private int musicId;
		private RoomEvent event;

		private Room(int musicId, int regionX, int regionY,
				int... doorsDirections) {
			this(null, musicId, regionX, regionY, doorsDirections);
		}

		private Room(RoomEvent event, int musicId, int regionX, int regionY,
				int... doorsDirections) {
			this.event = event;
			this.regionX = regionX;
			this.regionY = regionY;
			this.doorsDirections = doorsDirections;
			this.musicId = musicId;
		}

		public int getRegionX() {
			return regionX;
		}

		public int getRegionY() {
			return regionY;
		}

		public void openRoom(Dungeon dungeon, int[] pos) {
			if (event == null)
				return;
			event.openRoom(dungeon, pos);
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

		public int getMusicId() {
			return musicId;
		}

	}

	public static void startDungeon(final int floor, final int complexity,
			final int size, Player... teamArray) {
		new Dungeon(floor, complexity, size, teamArray);
	}

	public static int[] getRatio(int size) {
		int ratioX = 1, ratioY = 2;
		return new int[] { ratioX, ratioY };
	}

	public static class Dungeon {

		private int floor;
		private CopyOnWriteArrayList<Player> team;
		private boolean[][] openedRooms;
		private int openedRoomsCount;
		private DungeonStructure structure;
		private int[] mapBaseCoords;
		private boolean destroyed;
		private boolean started;
		private int startPartyTotalCombatLevel;
		private int dungeonBossRoomHash;

		private Dungeon(final int floor, final int complexity, final int size,
				Player[] teamArray) {
			this.floor = floor;
			dungeonBossRoomHash = -1;
			final int[] ratio = getRatio(0);
			openedRooms = new boolean[ratio[0]][ratio[1]];
			team = new CopyOnWriteArrayList<Player>();
			for (Player player : teamArray) {
				team.add(player);
				player.getControlerManager().startControler("DungControler",
						this);
			}
			setPartyTotalCombatLevel();
			// slow executor loads dungeon as it may take up to few secs
			CoresManager.slowExecutor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						// generates dungeon structure
						structure = generateDungeonStructure(
								getFloorType(floor), complexity, size);
						// finds an empty map area bounds
						mapBaseCoords = RegionBuilder.findEmptyMap(
								ratio[0] * 2, ratio[1] * 2);
						// reserves all map area
						RegionBuilder
								.cutMap(mapBaseCoords[0], mapBaseCoords[1],
										ratio[0] * 2, ratio[1] * 2, 0);
						dungeonBossRoomHash = MapAreas.getRandomAreaHash();
						WorldTile base = getBaseCoords(structure
								.getBossRoomPos());
						MapAreas.addArea(
								dungeonBossRoomHash,
								new int[] { base.getPlane(), base.getX() - 8,
										base.getX() + 8, base.getY() - 8,
										base.getY() + 8 });
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
			WorldTile homeTile = getHomeTeleTile();
			int[] homeRoom = structure.getStartRoomPos();
			World.spawnNPC(11226, new WorldTile(homeTile, 2), -1, true);
			for (Player player : team) {
				player.stopAll();
				player.reset();
				player.setMapSize(2); // biggest map size so less reloading when
										// walking from a part of dungeon to
										// another
				player.setForceMultiArea(true);
				player.setNextWorldTile(homeTile);
				playMusic(player, homeRoom);
				player.getCombatDefinitions().setSpellBook(3); // sets dungeon
																// spellbook
				player.getPackets().sendGameMessage("");
				player.getPackets().sendGameMessage("-Welcome to Daemonheim-");
				player.getPackets().sendGameMessage(
						"Floor " + floor + " Complexity " + 6 + " (Full)");
				player.getPackets().sendGameMessage("Dungeon Size: " + "Small");
				player.getPackets().sendGameMessage(
						"Party Size:Dificulty " + team.size() + ":"
								+ team.size());
				player.getPackets().sendGameMessage("");
			}
			started = true;
		}

		public WorldTile getHomeTeleTile() {
			return getBaseCoords(structure.getStartRoomPos());
		}

		public WorldTile getBaseCoords(int[] pos) {
			return new WorldTile(((mapBaseCoords[0] << 3) + pos[0] * 16) + 8,
					((mapBaseCoords[1] << 3) + pos[1] * 16) + 8, 0);

		}

		public void playMusic(Player player, int... pos) {
			Room room = structure.getRooms()[pos[0]][pos[1]];
			player.getMusicsManager().playMusic(room.getMusicId());
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
			room.openRoom(this, pos);
			return true;
		}

		public int[] getCurrentRoomCoords(WorldTile tile) {
			return new int[] { tile.getChunkX() << 3, tile.getChunkY() << 3 };
		}

		public int[] getCurrentRoomPos(WorldTile tile) {
			return new int[] { (tile.getChunkX() - mapBaseCoords[0]) / 2,
					(tile.getChunkY() - mapBaseCoords[1]) / 2 };
		}

		public int getPartySize() {
			return team.size();
		}

		public int getDungeonBossRoomHash() {
			return dungeonBossRoomHash;
		}

		public void destroyDungeon() {
			if (destroyed)
				return;
			destroyed = true;
			MapAreas.removeArea(dungeonBossRoomHash);
			// gives 1 game ticket so people can leave
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					int[] ratio = getRatio(0);
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

		public int getStartPartyTotalCombatLevel() {
			return (startPartyTotalCombatLevel * 2);
		}

		public void setPartyTotalCombatLevel() {
			int combatLevel = 0;
			for (Player player : team)
				combatLevel += player.getSkills().getCombatLevelWithSummoning();
			startPartyTotalCombatLevel = combatLevel;
		}

		public boolean hasStarted() {
			return started;
		}

		public boolean isDestroyed() {
			return destroyed;
		}

		public void openStairs() {
			WorldObject object;
			int[] pos = structure.getBossRoomPos();
			switch (structure.getFloorType()) {
			case FROZEN_FLOORS:
			default:
				object = new WorldObject(3784, 10, 3,
						((mapBaseCoords[0] << 3) + pos[0] * 16) + 7,
						((mapBaseCoords[1] << 3) + pos[1] * 16) + 15, 0);
				break;
			}
			World.spawnObject(object, false);
			for (Player player : team)
				player.getPackets().sendMusicEffect(415);
		}

	}

	public static int getFloorType(int floor) {
		return FROZEN_FLOORS; // TODO
	}

	private Dungeoneering() {

	}
}
