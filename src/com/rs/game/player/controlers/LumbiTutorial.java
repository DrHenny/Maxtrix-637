package com.rs.game.player.controlers;

import com.rs.Settings;
import com.rs.game.Entity;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.content.Magic;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;

/**
 * Lumbridge starter tutorial.
 * 
 * @author Raghav
 * 
 */
public class LumbiTutorial extends Controler {

	private static final int OZAN_NPC_ID = 7888;
	public static final int TELETAB = 8013;

	@Override
	public void start() {
		sendInterfaces();
	}

	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().replaceRealChatBoxInterface(372);
		for (int i = 0; i < 6; i++)
			player.getPackets().sendIComponentText(372, i, "");
		player.getPackets().sendIComponentText(372, 0,
				"Welcome to " + Settings.SERVER_NAME);
		player.getPackets().sendIComponentText(372, 3, "Talk to Ozan!");
	}

	@Override
	public boolean processNPCClick1(NPC npc) {
		if (npc.getId() == OZAN_NPC_ID)
			player.getDialogueManager().startDialogue("OzanD", OZAN_NPC_ID, 0);
		return false;
	}

	@Override
	public boolean login() {
		start();
		return false;
	}

	@Override
	public boolean logout() {
		return false;
	}

	@Override
	public boolean canPlayerOption1(Player target) {
		return false;
	}

	@Override
	public boolean canHit(Entity entity) {
		return false;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		return false;
	}

	@Override
	public boolean processNPCClick2(NPC npc) {
		return false;
	}

	@Override
	public boolean processObjectClick2(WorldObject object) {
		return false;
	}

	@Override
	public boolean processObjectClick3(WorldObject object) {
		return false;
	}

	@Override
	public boolean sendDeath() {
		return false;
	}

	@Override
	public boolean canAttack(Entity target) {
		return false;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		player.getPackets()
				.sendGameMessage("You can't do this at this moment.");
		return false;
	}

	@Override
	public boolean checkWalkStep(int lastX, int lastY, int nextX, int nextY) {
		if ((Boolean) player.getTemporaryAttributtes().get("cantmove") != null) {
			return false;
		}
		return true;
	}

	@Override
	public void forceClose() {
		player.getInventory().addItem(1856, 1);
		player.getInventory().addItem(10362, 1);
		player.getInventory().addItem(3867, 1);
		player.getTemporaryAttributtes().remove("cantmove");
		player.getInterfaceManager().closeReplacedRealChatBoxInterface();
	}

	@Override
	public boolean handleItemOption1(Player player, int slotId, int itemId,
			Item item) {
		if (itemId == TELETAB) {
			final Player p = player;
			p.getTemporaryAttributtes().put("cantmove", true);
			p.getPackets().sendIComponentText(372, 3, "");
			p.getInventory().deleteItem(itemId, 1);
			WorldTasksManager.schedule(new WorldTask() {
				int loop = 0;

				@Override
				public void run() {
					switch (loop) {
					case 0:
						Magic.useTeleTab(p, new WorldTile(3368, 3268, 0));
						break;
					case 1:
						p.getDialogueManager().startDialogue("OzanD",
								OZAN_NPC_ID, 1);
						break;
					case 2:
						Magic.useTeleTab(p, new WorldTile(2442, 3090, 0));
						break;
					case 3:
						p.getDialogueManager().startDialogue("OzanD",
								OZAN_NPC_ID, 2);
						break;
					case 4:
						Magic.useTeleTab(p, new WorldTile(3365, 3083, 0));
						break;
					case 5:
						p.getDialogueManager().startDialogue("OzanD",
								OZAN_NPC_ID, 3);
						break;
					case 6:
						Magic.useTeleTab(p, new WorldTile(3566, 3289, 0));
						break;
					case 7:
						p.getDialogueManager().startDialogue("OzanD",
								OZAN_NPC_ID, 4);
						break;
					case 8:
						Magic.useTeleTab(p, new WorldTile(3223, 3219, 0));
						break;
					case 9:
						p.getDialogueManager().startDialogue("OzanD",
								OZAN_NPC_ID, 5);
						break;
					case 10:
						p.getDialogueManager().startDialogue("OzanD",
								OZAN_NPC_ID, 6);
						break;
					}
					loop++;
				}
			}, 0, 5);
		}
		return true;
	}

}
