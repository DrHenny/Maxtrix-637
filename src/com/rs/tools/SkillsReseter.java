package com.rs.tools;

import java.io.File;
import java.io.IOException;

import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.utils.SerializableFilesManager;

public class SkillsReseter {

	public static int[] KEEP_SKILLS = { Skills.ATTACK, Skills.DEFENCE,
			Skills.STRENGTH, Skills.HITPOINTS, Skills.MAGIC, Skills.RANGE,
			Skills.PRAYER, Skills.SUMMONING };

	public static boolean resetSkill(int id) {
		for (int s : KEEP_SKILLS)
			if (s == id)
				return false;
		return true;
	}

	public static void main(String[] args) throws ClassNotFoundException,
			IOException {
		// Cache.init();
		File[] chars = new File("data/characters").listFiles();
		for (File acc : chars) {
			try {
				Player player = (Player) SerializableFilesManager
						.loadSerializedFile(acc);
				/*
				 * for(int i = 0; i < 25; i++) { if(!resetSkill(i)) continue;
				 * player.getSkills().resetSkillNoRefresh(i); }
				 */
				/*
				 * player.getSkills().addSkillXpRefresh(Skills.HERBLORE, 250);
				 * //ability to do herblore skill
				 * 
				 * SerializableFilesManager.storeSerializableClass(player, acc);
				 */

				/*
				 * Player newPlayer = new Player(player.getPassword());
				 * newPlayer.setKillCount(player.getKillCount());
				 * newPlayer.setDeathCount(player.getDeathCount());
				 * newPlayer.getSkills().passLevels(player);
				 * newPlayer.getMusicsManager().passMusics(player);
				 * SerializableFilesManager.storeSerializableClass(newPlayer,
				 * acc);
				 */
				if (player.getRights() > 0) {
					player.setRights(0);
					SerializableFilesManager
							.storeSerializableClass(player, acc);

				}
			} catch (Throwable e) {
				e.printStackTrace();
				System.out.println("failed: " + acc.getName());
			}
		}
		System.out.println("Done.");
	}
}
