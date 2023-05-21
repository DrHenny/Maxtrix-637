package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.Animation;
import com.rs.game.WorldTile;
import com.rs.game.minigames.CastleWars;
import com.rs.game.player.content.Magic;
import com.rs.utils.Misc;

public class RoyalGuard extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		if (Misc.random(3) == 0)
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"'Ello " + player.getDisplayName() + "!",
							"Lovely day in Echo!" }, IS_NPC, npcId, 9850);
		else if (Misc.random(3) == 1)
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"'Ello " + player.getDisplayName() + "!",
							"Could you tell the King to import some tea, please?" },
					IS_NPC, npcId, 9850);
		else if (Misc.random(3) == 2)
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"'Ello " + player.getDisplayName() + "!",
							"Not a single intruder today! They must be scared of us." },
					IS_NPC, npcId, 9850);
		else if (Misc.random(3) == 3)
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"'Ello " + player.getDisplayName() + "!",
							"Glad to see you out and about after your.. accident." },
					IS_NPC, npcId, 9850);
		else
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"'Ello " + player.getDisplayName() + "!",
							"Glad to see you out and about after your.. accident." },
					IS_NPC, npcId, 9850);

	}

	@Override
	public void run(int interfaceId, int componentId) {
		end();
	}

	@Override
	public void finish() {
	}
}
