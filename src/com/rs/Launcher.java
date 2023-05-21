package com.rs;

import com.alex.store.Index;
import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.ItemEquipIds;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.Region;
import com.rs.game.RegionBuilder;
import com.rs.game.World;
import com.rs.game.npc.combat.CombatScriptsHandler;
import com.rs.game.player.Player;
import com.rs.game.player.content.FishingSpotsHandler;
import com.rs.game.player.content.FriendChatsManager;
import com.rs.game.player.controlers.ControlerHandler;
import com.rs.game.player.cutscenes.CutscenesHandler;
import com.rs.game.player.dialogues.DialogueHandler;
import com.rs.net.ServerChannelHandler;
import com.rs.utils.*;
import com.rs.utils.huffman.Huffman;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.concurrent.TimeUnit;

public final class Launcher {

	public static void main(String[] args) throws Exception {

		Settings.HOSTED = false;
		Settings.DEBUG = true;
		long currentTime = Utils.currentTimeMillis();
		Date date = new Date(currentTime);
		String displayDate = date.getMonth() + "/" + date.getDate() + "/" + String.valueOf(date.getYear());
		System.err.println("[" + displayDate + " - Launcher] - Initiating RS2Cache Files");
		Cache.init();
		ItemEquipIds.init();
		System.err.println("[" + displayDate + " - Launcher] - Initiating Item Equipment IDs");
		Huffman.init();
		System.err.println("[" + displayDate + " - Launcher] - Initiating Huffman");
		IPBanL.init();
		System.err.println("[" + displayDate + " - Launcher] - Initiating IPBans");
		MapContainersXteas.init();
		System.err.println("[" + displayDate + " - Launcher] - Initiating Map xTeas");
		MapAreas.init();
		System.err.println("[" + displayDate + " - Launcher] - Initiating Map areas");
		ObjectSpawns.init();
		System.err.println("[" + displayDate + " - Launcher] - Initiating Object Spawns");
		NPCSpawns.init();
		System.err.println("[" + displayDate + " - Launcher] - Initiating NPC Spawns");
		NPCCombatDefinitionsL.init();
		System.err.println("[" + displayDate + " - Launcher] - Initiating NPC Combat Definitions");
		NPCBonuses.init();
		System.err.println("[" + displayDate + " - Launcher] - Initiating NPC Bonuses");
		NPCDrops.init();
		System.err.println("[" + displayDate + " - Launcher] - Initiating NPC Drops");
		ItemExamines.init();
		ItemBonuses.init();
		NPCExamines.loadExamines();
		System.err.println("[" + displayDate + " - Launcher] - Initiating Item Handlers");
		ShopsHandler.init();
		System.err.println("[" + displayDate + " - Launcher] - Initiating ShopsHandler");
		DisplayName.loadFile();
		System.err.println("[" + displayDate + " - Launcher] - Initiating Display Names");
		FishingSpotsHandler.init();
		CombatScriptsHandler.init();
		System.err.println("[" + displayDate + " - Launcher] - Initiating Dialogue Handlers");
		DialogueHandler.init();
		System.err.println("[" + displayDate + " - Launcher] - Initiating Controllers");
		ControlerHandler.init();
		System.err.println("[" + displayDate + " - Launcher] - Initiating Cutscenes");
		CutscenesHandler.init();
		System.err.println("[" + displayDate + " - Launcher] - Initiating Friend Chats");
		FriendChatsManager.init();
		System.err.println("[" + displayDate + " - Launcher] - Initiating Cores");
		CoresManager.init();
		System.err.println("[" + displayDate + " - Launcher] - Initiating World");
		World.init();
		System.err.println("[" + displayDate + " - Launcher] - Initiating Region Builder");
		RegionBuilder.init();
		System.err.println("[" + displayDate + " - Launcher] - Initiating Server Channel Handler");
		try {
			ServerChannelHandler.init();
			NPCSpawning.spawnNPCS();
		} catch (Throwable e) {
			Logger.handle(e);
			System.err.println("[" + displayDate + " - Launcher] - ERROR: COULD NOT LOAD SERVER CHANNEL HANDLER!");
			System.exit(1);
			return;
		}
		System.err.println("[" + displayDate + " - Launcher] - Server successfully bootstrapped to port: " + Settings.PORT_ID + " in " + ((Utils.currentTimeMillis() - currentTime) / 1000) + " seconds.");
		addAccountsSavingTask();
		if (Settings.HOSTED) { addUpdatePlayersOnlineTask(); }
		addCleanMemoryTask();
		// Donations.init();
	}

	private static void setWebsitePlayersOnline(int amount) throws IOException {
		URL url = new URL("http://127.0.0.1/matrix/updateplayeramount.php?players=" + amount + "&auth=JFHDJF3847234");
		url.openStream().close();
	}

	private static void addUpdatePlayersOnlineTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					setWebsitePlayersOnline(World.getPlayers().size());
				} catch (Throwable e) {
					// Logger.handle(e);
				}
			}
		}, 2, 2, TimeUnit.MINUTES);
	}

	private static void addCleanMemoryTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					cleanMemory(Runtime.getRuntime().freeMemory() < Settings.MIN_FREE_MEM_ALLOWED);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}, 0, 10, TimeUnit.MINUTES);
	}

	private static void addAccountsSavingTask() {
		CoresManager.slowExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				try {
					saveFiles();
				} catch (Throwable e) {
					Logger.handle(e);
				}

			}
		}, 1, 1, TimeUnit.SECONDS);
	}

	public static void saveFiles() {
		for (Player player : World.getPlayers()) {
			if (player == null || !player.hasStarted() || player.hasFinished()) { continue; }
			SerializableFilesManager.savePlayer(player);
		}
		IPBanL.save();
		PkRank.save();
		DTRank.save();
	}

	public static void cleanMemory(boolean force) {
		if (force) {
			ItemDefinitions.clearItemsDefinitions();
			NPCDefinitions.clearNPCDefinitions();
			ObjectDefinitions.clearObjectDefinitions();
			for (Region region : World.getRegions().values()) { region.removeMapFromMemory(); }
		}
		for (Index index : Cache.STORE.getIndexes()) {
			if (index == null) { continue; }
			index.resetCachedFiles();
		}
		CoresManager.fastExecutor.purge();
		System.gc();
	}

	public static void shutdown() {
		try {
			closeServices();
		} finally {
			System.exit(0);
		}
	}

	public static void closeServices() {
		ServerChannelHandler.shutdown();
		CoresManager.shutdown();
		if (Settings.HOSTED) {
			try {
				setWebsitePlayersOnline(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void restart() {
		closeServices();
		System.gc();
		try {
			System.exit(2);
		} catch (Throwable e) {
			Logger.handle(e);
		}

	}

	private Launcher() {

	}

}
