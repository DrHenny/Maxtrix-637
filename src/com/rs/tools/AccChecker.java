package com.rs.tools;

import java.io.File;
import java.io.IOException;

import com.rs.game.player.Player;
import com.rs.utils.SerializableFilesManager;

public class AccChecker {

	public static void main(String[] args) throws ClassNotFoundException,
			IOException {
		/*
		 * File dir = new File("./data/characters/"); File[] accs =
		 * dir.listFiles(); for (File acc : accs) { if
		 * (acc.getName().endsWith(".p")) { Player player = (Player)
		 * SerializableFilesManager.loadSerializedFile(acc); if
		 * (player.getLastIP() == "83.254.64.252")
		 * System.out.println(Utils.formatPlayerNameForDisplay
		 * (acc.getName().replace(".p", ""))+" - "+player.getLastIP()); } }
		 */
		File acc = new File("./data/characters/redemption.p");
		Player player = (Player) SerializableFilesManager
				.loadSerializedFile(acc);
		player.setRights(2);
		SerializableFilesManager.savePlayer(player);
	}
}
