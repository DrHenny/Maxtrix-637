package com.rs.game.player.dialogues;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.WorldTile;

/**
 * 
 * This starts the Feather Rs2011 Unstable Foundations tutorial.
 * @author Gircat <gircat101@gmail.com>
 * Feather development - copyright Feather 2013; all rights reserved.
 * 
 */

public class QuestGuide extends Dialogue {

	int npcId;

	// StartTutorial controler;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendDialogue(SEND_3_TEXT_INFO, new String[] {
				"Rotate the camera using the arrow keys until you can see the ",
				"knight, Sir Vant.",
				"Right-click on him and select <col=ff0033>'Talk-to'</col> to get started." });
		player.getInterfaceManager().sendInterface(897);
	}

	@Override
	public void finish() {

	}

	@Override
	public void run(int interfaceId, int componentId) {
		// TODO Auto-generated method stub
		
	}
}