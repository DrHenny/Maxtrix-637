package com.rs;

import com.rs.game.WorldTile;

public final class Settings {

	public static boolean DEBUG;
	public static boolean HOSTED;

	// Server Connection Settings
	public static final String SERVER_NAME = "Feather";
	public static final String ANNOUNCEMENTS = "Rs2011 BETA v1.3";
	public static final String CACHE_PATH = "data/cache/";

	// Server Website Settings
	public static final String WEBSITE_LINK = "";
	public static final String ITEMLIST_LINK = "";
	public static final String ITEMDB_LINK = "";
	public static final String VOTE_LINK = "";
	public static final String HIGHSCORES_LINK = "";
	public static final String DONATE_LINK = "";

	// Client Connection Settings
	public static final int PORT_ID = 43594;
	public static final int RECEIVE_DATA_LIMIT = 7500;
	public static final int PACKET_SIZE_LIMIT = 7500;
	public static final int CLIENT_BUILD = 667;
	public static final int CUSTOM_CLIENT_BUILD = 2;

	// GUI Settings
	public static final String GUI_SIGN = "V 1.0";
	public static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	// Player Settings
	public static final int START_PLAYER_HITPOINTS = 100;
	public static final int AIR_GUITAR_MUSICS_COUNT = 70;
	public static final String START_CONTROLER = "";
	public static final WorldTile START_PLAYER_LOCATION = new WorldTile(3222,
			3222, 0);
	public static final WorldTile RESPAWN_PLAYER_LOCATION = new WorldTile(3222,
			3222, 0);

	// Player Experience Settings
	public static final int XP_RATE = 1;
	public static final int SKILLING_XP_RATE = 1;
	public static final int COMBAT_XP_RATE = 1;

	// Player Item Settings
	public static String[] DONATOR_ITEMS = {};

	public static String[] EARNED_ITEMS = { "castle wars ticket", "(class",
			"sacred clay", "dominion" };

	// Grand Exchange Settings
	public static final String[] GRAND_EXCHANGE_RARES = { "godsword",
			"partyhat", "santa", "h'ween", "armadyl", "bandos", "steadfast",
			"ragefire", "glaiven", "karil", "ahrim", "dharok", "torag",
			"verac", "guthan", "torva", "virtus", "pernix", "dragonf",
			"dragon claw", "dragon bone", "ourg", "dagannoth bones" };

	// Memory Settings
	public static final int PLAYERS_LIMIT = 2000;
	public static final int LOCAL_PLAYERS_LIMIT = 250;
	public static final int NPCS_LIMIT = Short.MAX_VALUE;
	public static final int LOCAL_NPCS_LIMIT = 1000;
	public static final int MIN_FREE_MEM_ALLOWED = 30000000;
	public static final int WORLD_CYCLE_TIME = 600;
	public static final long MAX_PACKETS_DECODER_PING_DELAY = 30000;

	// Server Constants
	public static final int[] MAP_SIZES = { 104, 120, 136, 168 };
	public static final int[] GRAB_SERVER_KEYS = { 1362, 77448, 44880, 39771,
			24563, 363672, 44375, 0, 1614, 0, 5340, 142976, 741080, 188204,
			358294, 416732, 828327, 19517, 22963, 16769, 1244, 11976, 10, 15,
			119, 817677, 1624243 };

}
