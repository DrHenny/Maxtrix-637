package com.rs.tools;

import java.io.File;

import com.rs.game.player.Player;
import com.rs.utils.SerializableFilesManager;

public class Unglitch {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		File acc = new File("cjay0091.p");
		Player player = (Player) SerializableFilesManager
				.loadSerializedFile(acc);
		player.setDisableEquip(false);
		SerializableFilesManager.storeSerializableClass(player, acc);
	}

}
