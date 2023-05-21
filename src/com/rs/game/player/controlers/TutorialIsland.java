package com.rs.game.player.controlers;

import com.rs.Settings;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.Fishing;
import com.rs.game.player.actions.Fishing.FishingSpots;
import com.rs.game.player.actions.Woodcutting.TreeDefinitions;

public final class TutorialIsland extends Controler {

	private static final int RUNESCAPE_GUIDE_NPC = 945,
			SURVIVAL_EXPERT_NPC = 943;

	@Override
	public void start() {
		if (getStage() < 6)
			player.getPackets().sendGlobalConfig(168, -1); // sets no selected
															// tab
		refreshStage();
		sendInterfaces();
	}

	@Override
	public boolean canEquip(int slotId, int itemId) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"You'll be told how to equip items later.");
		return false;
	}

	public int getStage() {
		if (getArguments() == null)
			setArguments(new Object[] { 0 }); // index 0 = stage
		return (Integer) getArguments()[0];
	}

	public void setStage(int stage) {
		getArguments()[0] = stage;
	}

	public void refreshStage() {
		int stage = getStage();
		if (stage == 0 || stage == 1 || stage == 2) {
			NPC guide = findNPC(RUNESCAPE_GUIDE_NPC);
			if (guide != null) // not saving icon as theres no need for more
								// than 1icon
				player.getHintIconsManager().addHintIcon(guide, 0, -1, false);
		} else if (stage == 3)
			player.getHintIconsManager().addHintIcon(3097, 3107, 0, 125, 4, 0,
					-1, false);
		else if (stage == 4 || stage == 11) {
			NPC survival = findNPC(SURVIVAL_EXPERT_NPC);
			if (survival != null) // not saving icon as theres no need for more
									// than 1icon
				player.getHintIconsManager()
						.addHintIcon(survival, 0, -1, false);
		} else if (stage == 6)
			player.getHintIconsManager().addHintIcon(3099, 3095, 0, 150, 4, 0,
					-1, false);
		else if (stage == 12) {
			// 233 - 3100 3091 0
			NPC spot = null;
			for (NPC npc : World.getNPCs()) {
				if (npc == null || npc.getId() != 233 || npc.getX() != 3100
						|| npc.getY() != 3091 || npc.getPlane() != 0)
					continue;
				spot = npc;
				break;
			}
			if (spot != null)
				player.getHintIconsManager().addHintIcon(spot, 0, -1, false);
		}
		sendInterfaces();
	}

	public NPC findNPC(int id) {
		// as it may be far away
		for (NPC npc : World.getNPCs()) {
			if (npc == null || npc.getId() != id)
				continue;
			return npc;
		}
		return null;
	}

	public void updateProgress() {
		setStage(getStage() + 1);
		if (getStage() == 1)
			player.getInterfaceManager().sendSettings();
		else if (getStage() == 2)
			player.getPackets().sendConfig(1021, 0); // unflash
		else if (getStage() == 5) {
			player.getInterfaceManager().sendInventory();
			player.getHintIconsManager().removeUnsavedHintIcon();
		} else if (getStage() == 6)
			player.getPackets().sendConfig(1021, 0); // unflash
		else if (getStage() == 7)
			player.getHintIconsManager().removeUnsavedHintIcon();
		else if (getStage() == 10)
			player.getInterfaceManager().sendSkills();
		else if (getStage() == 11)
			player.getPackets().sendConfig(1021, 0); // unflash
		refreshStage();
	}

	public void sendProgress() {
		int stage = getStage();
		player.getInterfaceManager()
				.sendTab(
						player.getInterfaceManager().hasRezizableScreen() ? 5
								: 17, 371);
		player.getInterfaceManager().replaceRealChatBoxInterface(372);
		if (stage == 0) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			player.getPackets().sendIComponentText(372, 0, "Getting Started");
			player.getPackets()
					.sendIComponentText(372, 1,
							"To start the tutorial use your left mouse button to click on the");
			player.getPackets()
					.sendIComponentText(
							372,
							2,
							Settings.SERVER_NAME
									+ " Guide in this room .He is indicated by a flashing");
			player.getPackets()
					.sendIComponentText(372, 3,
							"yellow arrow above his head. If you can't see him use your");
			player.getPackets().sendIComponentText(372, 4,
					"keyboard arrow keys to rotate the view.");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 1) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			player.getPackets().sendIComponentText(372, 0, "");
			player.getPackets().sendIComponentText(372, 1, "");
			player.getPackets().sendIComponentText(372, 2, "Player controls");
			player.getPackets()
					.sendIComponentText(372, 3,
							"Please click on the flashing spanner icon found at the buttom");
			player.getPackets()
					.sendIComponentText(372, 4,
							"right of your screen. This will display your player controls.");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
			player.getPackets().sendConfig(1021, 13); // flashing setting tab
		} else if (stage == 2) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			player.getPackets().sendIComponentText(372, 0, "Player controls");
			player.getPackets()
					.sendIComponentText(372, 1,
							"On the side panel you can now see a variety of options from");
			player.getPackets()
					.sendIComponentText(372, 2,
							"changing the brightness of the screen and of the volume of");
			player.getPackets().sendIComponentText(372, 3,
					"music, to selecting whether your player should help");
			player.getPackets()
					.sendIComponentText(372, 4,
							"from other players. Don't worry about these too much for now.");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 3) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			player.getPackets().sendIComponentText(372, 0,
					"Interacting with the scenery");
			player.getPackets()
					.sendIComponentText(372, 1,
							"You can interact with many items of the scenery by simply clicking");
			player.getPackets()
					.sendIComponentText(372, 2,
							"on them. Right clicking will also give more options. Feel free to");
			player.getPackets()
					.sendIComponentText(372, 3,
							"try it with the things in this room, then click on the door");
			player.getPackets()
					.sendIComponentText(372, 4,
							"indicated with the yellow arrow to go througth to the next");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 4) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			sendProgress(5);
			player.getPackets().sendIComponentText(372, 0, "Moving around");
			player.getPackets()
					.sendIComponentText(372, 1,
							"You can interact with many items of the scenery by simply clicking");
			player.getPackets()
					.sendIComponentText(372, 2,
							"ground will walk you to that point. Talk to the Survival Expert by");
			player.getPackets()
					.sendIComponentText(372, 3,
							"the pond to continue the tutorial. Remember you can rotate");
			player.getPackets().sendIComponentText(372, 4,
					"the view by pressing the arrow keys.");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 5) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			sendProgress(5);
			player.getPackets().sendIComponentText(372, 0,
					"Viewing the items that you were given.");
			player.getPackets()
					.sendIComponentText(372, 1,
							"Click on the flashing backpack icon to the right hand size of");
			player.getPackets()
					.sendIComponentText(372, 2,
							"the main window to view your inventory. Your inventory is a list");
			player.getPackets().sendIComponentText(372, 3,
					"of everything you have in your backpack.");
			player.getPackets().sendIComponentText(372, 4, "");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
			player.getPackets().sendConfig(1021, 5); // flashing inv tab
		} else if (stage == 6) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			sendProgress(5);
			player.getPackets().sendIComponentText(372, 0, "Cut down a tree");
			player.getPackets()
					.sendIComponentText(372, 1,
							"You can click on the backpack icon at any time to view the");
			player.getPackets()
					.sendIComponentText(372, 2,
							"items that you currently have in your inventory. You will see");
			player.getPackets()
					.sendIComponentText(372, 3,
							"that you now have an axe in your inventory. Use this to get");
			player.getPackets().sendIComponentText(372, 4,
					"some logs by clicking on one of the trees in the area.");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 7) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			sendProgress(5);
			player.getPackets().sendIComponentText(372, 0, "Please wait");
			player.getPackets().sendIComponentText(372, 1, "");
			player.getPackets()
					.sendIComponentText(372, 2,
							"Your character is now attemping to cut down the tree. Sit back");
			player.getPackets().sendIComponentText(372, 3,
					"for a moment while he does all the hard work.");
			player.getPackets().sendIComponentText(372, 4, "");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 8) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			sendProgress(5);
			player.getPackets().sendIComponentText(372, 0, "Making a fire");
			player.getPackets()
					.sendIComponentText(372, 1,
							"Well done! You managed to cut some logs from the tree! Next,");
			player.getPackets().sendIComponentText(372, 2,
					"use the tinderbox in your inventory to light the logs.");
			player.getPackets().sendIComponentText(372, 3,
					"First click on the tinderbox to 'use' it.");
			player.getPackets().sendIComponentText(372, 4,
					"Then click on the logs in your inventory to light them.");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 9) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			sendProgress(5);
			player.getPackets().sendIComponentText(372, 0, "Please wait");
			player.getPackets().sendIComponentText(372, 1, "");
			player.getPackets().sendIComponentText(372, 2,
					"Your character is now attemping to light the fire.");
			player.getPackets().sendIComponentText(372, 3,
					"This should only take a few seconds.");
			player.getPackets().sendIComponentText(372, 4, "");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 10) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			sendProgress(5);
			player.getPackets().sendIComponentText(372, 0, "");
			player.getPackets().sendIComponentText(372, 1, "");
			player.getPackets().sendIComponentText(372, 2,
					"You gained some experience.");
			player.getPackets()
					.sendIComponentText(372, 3,
							"Click on the flashing bar graph icon near the inventory button");
			player.getPackets().sendIComponentText(372, 4,
					"to see your skill stats.");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
			player.getPackets().sendConfig(1021, 2); // flashing skills tab
		} else if (stage == 11) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			sendProgress(5);
			player.getPackets().sendIComponentText(372, 0, "Your skill stats");
			player.getPackets()
					.sendIComponentText(372, 1,
							"Here you will see how good your skills are. As you move your");
			player.getPackets()
					.sendIComponentText(372, 2,
							"mouse over any of the icons in this panel, the small yellow");
			player.getPackets()
					.sendIComponentText(372, 3,
							"popup box will show you the start amount of experience you");
			player.getPackets()
					.sendIComponentText(372, 4,
							"have and how much is needed to get to the next level. Speak to");
			player.getPackets().sendIComponentText(372, 5,
					"the Survival Expert to continue.");
			player.getPackets().sendIComponentText(372, 6, "");
		} else if (stage == 12) {
			player.getPackets().sendHideIComponent(371, 4, true); // hides the
																	// please
																	// follow
																	// intrucions
			sendProgress(5);
			player.getPackets()
					.sendIComponentText(372, 0, "Catch some Shrimp.");
			player.getPackets()
					.sendIComponentText(372, 1,
							"Click on the sparkling fishing spot indicated by the fishing");
			player.getPackets()
					.sendIComponentText(372, 2,
							"arrow. Remember, you can check your inventory by clicking the");
			player.getPackets().sendIComponentText(372, 3, "backpack icon.");
			player.getPackets().sendIComponentText(372, 4, "");
			player.getPackets().sendIComponentText(372, 5, "");
			player.getPackets().sendIComponentText(372, 6, "");
		}
	}

	public void sendProgress(int percentage) {
		for (int i = 0; i < percentage / 5; i++)
			player.getPackets().sendHideIComponent(371, 5 + i, true);
	}

	/*
	 * return can use
	 */
	public boolean canUseItemOnItem(Item itemUsed, Item usedWith) {
		if (getStage() == 8) {
			if ((itemUsed.getId() == 590 || usedWith.getId() == 590)
					&& (itemUsed.getId() == 1511 || usedWith.getId() == 1511)) {
				updateProgress();
			}
		}
		return true;
	}

	@Override
	public void trackXP(int skillId, int addedXp) {
		if (getStage() == 9 && skillId == Skills.FIREMAKING)
			updateProgress();
		else if (getStage() == 12 && skillId == Skills.FISHING)
			updateProgress();
	}

	/*
	 * return can add
	 */
	@Override
	public boolean canAddInventoryItem(int itemId, int amount) {
		if (getStage() == 7 && itemId == TreeDefinitions.NORMAL.getLogsId()) {
			updateProgress();
			player.getDialogueManager().startDialogue("ItemMessage",
					"You get some logs.", itemId);
		}
		return true;
	}

	/*
	 * return process normaly
	 */
	@Override
	public boolean processButtonClick(int interfaceId, int componentId,
			int slotId, int packetId) {
		if (((interfaceId == 548 && componentId == 55) || (interfaceId == 746 && componentId == 132))
				&& getStage() == 1)
			updateProgress();
		else if (((interfaceId == 548 && componentId == 85) || (interfaceId == 746 && componentId == 124))
				&& getStage() == 5)
			updateProgress();
		else if (((interfaceId == 548 && componentId == 82) || (interfaceId == 746 && componentId == 121))
				&& getStage() == 10)
			updateProgress();

		return true;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 3014 && object.getX() == 3098
				&& object.getY() == 3107) {
			int stage = getStage();
			if (stage < 3 || player.getY() != object.getY())
				return false;
			if (stage == 3) {
				updateProgress();
			}
			WorldObject openedDoor = new WorldObject(object.getId(),
					object.getType(), object.getRotation() + 1,
					object.getX() - 1, object.getY(), object.getPlane());
			if (World.removeTemporaryObject(object, 1200, false)) {
				World.spawnTemporaryObject(openedDoor, 1200, false);
				player.addStopDelay(2);
				player.stopAll();
				player.addWalkSteps(
						player.getX() >= object.getX() ? object.getX() - 1
								: object.getX(), player.getY(), -1, false);
			}
			return false;
		} else if (object.getId() == 3033) {
			int stage = getStage();
			if (stage == 6)
				updateProgress();
		}
		return true;
	}

	@Override
	public boolean processNPCClick1(NPC npc) {
		FishingSpots spot = FishingSpots.forId(npc.getId());
		if (npc.getId() == RUNESCAPE_GUIDE_NPC) {
			player.getDialogueManager().startDialogue("RuneScapeGuide",
					RUNESCAPE_GUIDE_NPC, this);
			return false;
		} else if (npc.getId() == SURVIVAL_EXPERT_NPC) {
			player.getDialogueManager().startDialogue("SurvivalExpert",
					SURVIVAL_EXPERT_NPC, this);
			return false;
		} else if (spot != null) {
			player.getActionManager().setSkill(new Fishing(spot, npc));
			return true;
		}
		return true;
	}

	@Override
	public void sendInterfaces() {
		int stage = getStage();
		boolean rezi = player.getInterfaceManager().hasRezizableScreen();
		player.getPackets().closeInterface(rezi ? 29 : 150);// Attack tab
		if (stage < 10)
			player.getPackets().closeInterface(rezi ? 30 : 151);// Skill tab
		player.getPackets().closeInterface(rezi ? 31 : 152);// Quest tab
		player.getPackets().closeInterface(rezi ? 32 : 153);// Achievement tab
		if (stage < 5)
			player.getPackets().closeInterface(rezi ? 33 : 154);// Inventory tab
		player.getPackets().closeInterface(rezi ? 34 : 155);// Equipment tab
		player.getPackets().closeInterface(rezi ? 35 : 156);// pray tab
		player.getPackets().closeInterface(rezi ? 36 : 157);// magic tab
		player.getPackets().closeInterface(rezi ? 38 : 159);// Friend tab
		player.getPackets().closeInterface(rezi ? 39 : 160);// Ignore tab
		player.getPackets().closeInterface(rezi ? 40 : 161);// Clan tab
		if (stage == 0)
			player.getPackets().closeInterface(rezi ? 41 : 162);// Settings tab
		player.getPackets().closeInterface(rezi ? 42 : 163);// Emote tab
		player.getPackets().closeInterface(rezi ? 43 : 164);// Music tab
		player.getPackets().closeInterface(rezi ? 44 : 165);// Notes tab
		sendProgress();
	}

	/*
	 * return remove controler
	 */
	@Override
	public boolean login() {
		start();
		return false;
	}

	/*
	 * return remove controler
	 */
	@Override
	public boolean logout() {
		return false;
	}

}
