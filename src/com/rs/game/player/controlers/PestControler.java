package com.rs.game.player.controlers;

import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;

public class PestControler extends Controler {
	/*
	 * pest control portals 6142 - 2628 2591 0 6143 - 2680 2588 0 6144 - 2669
	 * 2570 0 6145 - 2645 2569 0
	 */
	NPC voidKnight;

	/**
	 * Timer for spawning of monsters.
	 */
	private int spawnMonster;

	/**
	 * Timer to unlock portal shield.
	 */
	private int portalShield;

	@Override
	public void start() {
		player.setNextWorldTile(new WorldTile(2658, 2612, 0));
		voidKnight = new NPC(3785, new WorldTile(2656, 2592, 0), 0, false, true);
		sendInterfaces();
	}

	@Override
	public void sendInterfaces() {
		player.getPackets().sendIComponentText(408, 1,
				"" + voidKnight.getHitpoints());
		player.getPackets().sendIComponentText(408, 11, "");
		player.getInterfaceManager().sendTab(
				player.getInterfaceManager().hasRezizableScreen() ? 10 : 19,
				408);
	}

	@Override
	public void process() {
		if (spawnMonster > 0)
			spawnMonster--;
		if (spawnMonster == 0) {
			player.getPestControl().spawnMonsters();
			spawnMonster = 15;
		}
		if (portalShield > 0)
			portalShield--;
		// uhh fuck this..
		if (portalShield == 0) {
			for (NPC n : World.getNPCs()) {
				if (n.getId() == 6146)
					n.transformIntoNPC(6144);
			}
		}
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"You can't leave the pest control area like this.");
		return false;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"You can't leave the pest control area like this.");
		return false;
	}

}
