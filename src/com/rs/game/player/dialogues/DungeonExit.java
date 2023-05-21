package com.rs.game.player.dialogues;

public class DungeonExit extends Dialogue {

	// private DungControler dungeon;

	@Override
	public void start() {
		sendDialogue(SEND_2_TEXT_CHAT, "",
				"This ladder leads back to the surface. You will not be able",
				"to come back to this dungeon if you leave.");

	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendDialogue(SEND_2_LARGE_OPTIONS,
					"Leave the dungeon and return to the surface?", "Yes.",
					"No.");
			stage = 0;
		} else if (stage == 0) {
			if (componentId == 2) {
				player.dungeon = null;
			}
			end();
		}
	}

	@Override
	public void finish() {

	}

}
