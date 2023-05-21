package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.WorldTile;
import com.rs.game.player.content.Magic;

// Referenced classes of package com.rs.game.player.dialogues:
//            Dialogue

public class XPRate extends Dialogue {

	public XPRate() {
	}

	public void start() {
		npcId = ((Integer) parameters[0]).intValue();
		sendEntityDialogue(SEND_2_TEXT_CHAT,
				new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Hey there " + player.getDisplayName()
								+ ", I can set your combat XP rate.",
						"Interested?" }, IS_NPC, npcId, 9827);
	}

	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue((short) 241,
					new String[] { player.getDisplayName(),
							"Sure, let's see what I can set it to!" },
					(byte) 0, player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendDialogue((short) 238, new String[] { "Select an option",
					"<col=ef7216>Hard</col>: 2x RuneScape",
					"<col=676ad9>Easy</col>: 35x RuneScape",
					"<col=0006ff>No Challenge</col>: 50x RuneScape",
					"<col=de4ac0>Normal</col>: 15x RuneScape", "Nevermind" });
			stage = 2;
		} else if (stage == 2) {
			if (componentId == 1) {
				player.combatExperience = 2;
				sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Excellent! Your combat rate is now 2x RuneScape.",
						"Have fun!" }, (byte) 1, npcId, 9850);
			} else if (componentId == 2) {
				player.combatExperience = 35;
				sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Excellent! Your combat rate is now 35x RuneScape.",
						"Have fun!" }, (byte) 1, npcId, 9850);
			} else if (componentId == 3) {
				player.combatExperience = 50;
				sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Excellent! Your combat rate is now 50x RuneScape.",
						"Have fun!" }, (byte) 1, npcId, 9850);
			} else if (componentId == 4) {
				player.combatExperience = 15;
				sendEntityDialogue(SEND_2_TEXT_CHAT, new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Excellent! Your combat rate is now 15x RuneScape.",
						"Have fun!" }, (byte) 1, npcId, 9850);
			} else {
				end();
			}
		}
	}

	private void teleportPlayer(int x, int y, int z) {
		Magic.sendNormalTeleportSpell(player, 0, 0.0D, new WorldTile(x, y, z),
				new int[0]);
	}

	public void finish() {
	}

	private int npcId;
}
