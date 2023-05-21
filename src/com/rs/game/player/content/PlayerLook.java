package com.rs.game.player.content;

import com.rs.game.player.Player;

public final class PlayerLook {

	public static void openMageMakeOver(Player player) {
		player.getInterfaceManager().sendInterface(900);
		player.getPackets().sendIComponentText(900, 33, "Confirm");
		player.getPackets().sendConfigByFile(6098,
				player.getAppearence().isMale() ? 0 : 1);
		player.getPackets().sendConfigByFile(6099,
				player.getAppearence().getSkinColor());
		player.getTemporaryAttributtes().put("MageMakeOverGender",
				player.getAppearence().isMale());
		player.getTemporaryAttributtes().put("MageMakeOverSkin",
				player.getAppearence().getSkinColor());
	}

	public static void handleMageMakeOverButtons(Player player, int buttonId) {
		if (buttonId == 16 || buttonId == 17)
			player.getTemporaryAttributtes().put("MageMakeOverGender",
					buttonId == 16);
		else if (buttonId >= 20 && buttonId <= 31) {

			int skin;
			if (buttonId == 31)
				skin = 11;
			else if (buttonId == 30)
				skin = 10;
			else if (buttonId == 20)
				skin = 9;
			else if (buttonId == 21)
				skin = 8;
			else if (buttonId == 22)
				skin = 7;
			else if (buttonId == 29)
				skin = 6;
			else if (buttonId == 28)
				skin = 5;
			else if (buttonId == 27)
				skin = 4;
			else if (buttonId == 26)
				skin = 3;
			else if (buttonId == 25)
				skin = 2;
			else if (buttonId == 24)
				skin = 1;
			else
				skin = 0;
			player.getTemporaryAttributtes().put("MageMakeOverSkin", skin);
		} else if (buttonId == 33) {
			Boolean male = (Boolean) player.getTemporaryAttributtes().remove(
					"MageMakeOverGender");
			Integer skin = (Integer) player.getTemporaryAttributtes().remove(
					"MageMakeOverSkin");
			player.closeInterfaces();
			if (male == null || skin == null)
				return;
			if (male == player.getAppearence().isMale()
					&& skin == player.getAppearence().getSkinColor())
				player.getDialogueManager().startDialogue("MakeOverMage", 2676,
						1);
			else {
				player.getDialogueManager().startDialogue("MakeOverMage", 2676,
						2);
				if (player.getAppearence().isMale() != male) {
					if (male)
						player.getAppearence().resetAppearence();
					else
						player.getAppearence().female();
				}
				player.getAppearence().setSkinColor(skin);
				player.getAppearence().generateAppearenceData();
			}
		}
	}

	public static void handleBeardButtons(Player player, int buttonId,
			int slotId) {// Hair and color match button count so just loop and
							// do ++, but cant find button ids
		if (buttonId == 6)
			player.getTemporaryAttributtes().put("hairSaloon", true);
		else if (buttonId == 7)
			player.getTemporaryAttributtes().put("hairSaloon", false);
		else if (buttonId == 18) {
			player.getInterfaceManager().closeScreenInterface();
			player.getAppearence().generateAppearenceData();
		} else if (buttonId == 10) {
			int hairCoordinate = -1;
			if ((boolean) player.getTemporaryAttributtes().get("hairSaloon")) {
				if (slotId == 1)// bald
					hairCoordinate = 256;
				else if (slotId == 3)// dreadlocks
					hairCoordinate = 257;
				else if (slotId == 5)// long hair
					hairCoordinate = 258;
				else if (slotId == 7)
					hairCoordinate = 259;
				else if (slotId == 9)
					hairCoordinate = 260;
				else if (slotId == 11)
					hairCoordinate = 261;
				else if (slotId == 12)
					hairCoordinate = 262;
				else if (slotId == 13)
					hairCoordinate = 5;
				else if (slotId == 15)
					hairCoordinate = 7;
				else if (slotId == 17)
					hairCoordinate = 264;
				else if (slotId == 19)
					hairCoordinate = 91;
				else if (slotId == 23)
					hairCoordinate = 93;
				else if (slotId == 21)
					hairCoordinate = 92;
				else if (slotId == 29)// curtains
					hairCoordinate = 93;
				else if (slotId == 25)// samurai
					hairCoordinate = 94;
				else if (slotId == 27)
					hairCoordinate = 95;
				else if (slotId == 29)
					hairCoordinate = 96;
				else if (slotId == 31)
					hairCoordinate = 97;
				else
					hairCoordinate = 0;
				player.getAppearence().setHairStyle(hairCoordinate);
			} else {
				int beardCoordinate = -1;
				if (slotId == 1)// red
					beardCoordinate = 20;
				else if (slotId == 3)// moustache
					beardCoordinate = 13;
				else if (slotId == 5)// handle bar
					beardCoordinate = 98;
				else if (slotId == 7)// sensi
					beardCoordinate = 18;
				else if (slotId == 9)// half goatee
					beardCoordinate = 4;
				else if (slotId == 11)// imperial
					beardCoordinate = 5;
				else if (slotId == 13)// goatee
					beardCoordinate = 10;
				else if (slotId == 15)// short beard
					beardCoordinate = 15;
				else if (slotId == 17)// short full
					beardCoordinate = 16;
				else if (slotId == 19)// full mutton
					beardCoordinate = 100;
				else if (slotId == 21)// medium beard
					beardCoordinate = 12;
				else if (slotId == 23)// long full beard
					beardCoordinate = 11;
				else if (slotId == 25)// waxed
					beardCoordinate = 102;
				else if (slotId == 27)// sideburns
					beardCoordinate = 17;
				else if (slotId == 29)// mutton
					beardCoordinate = 99;
				else if (slotId == 31)// full mustache
					beardCoordinate = 101;
				else if (slotId == 33)// visor
					beardCoordinate = 104;
				else if (slotId == 35)// split beard
					beardCoordinate = 17;
				else if (slotId == 36)// Dali
					beardCoordinate = 103;
				else
					beardCoordinate = 14;
				player.getAppearence().setBeardStyle(beardCoordinate);
			}
		} else if (buttonId == 16) {// 350
			int hairColourCoordinate = 0;
			if (slotId == 1)
				hairColourCoordinate = 20;
			else if (slotId == 3)
				hairColourCoordinate = 19;
			else if (slotId == 5)
				hairColourCoordinate = 10;
			else if (slotId == 7)
				hairColourCoordinate = 18;
			else if (slotId == 9)
				hairColourCoordinate = 4;
			else if (slotId == 11)
				hairColourCoordinate = 5;
			else if (slotId == 13)
				hairColourCoordinate = 15;
			else if (slotId == 15)
				hairColourCoordinate = 7;
			else if (slotId == 17)
				hairColourCoordinate = 26;
			else if (slotId == 19)
				hairColourCoordinate = 6;
			else if (slotId == 21)
				hairColourCoordinate = 21;
			else if (slotId == 23)
				hairColourCoordinate = 9;
			else if (slotId == 25)
				hairColourCoordinate = 22;
			else if (slotId == 27)
				hairColourCoordinate = 17;
			else if (slotId == 29)
				hairColourCoordinate = 8;
			else if (slotId == 31)
				hairColourCoordinate = 16;
			else if (slotId == 33)
				hairColourCoordinate = 11;
			else if (slotId == 35)
				hairColourCoordinate = 24;
			else if (slotId == 37)
				hairColourCoordinate = 23;
			else if (slotId == 39)
				hairColourCoordinate = 3;
			else if (slotId == 41)
				hairColourCoordinate = 2;
			else if (slotId == 43)
				hairColourCoordinate = 1;
			else if (slotId == 45)
				hairColourCoordinate = 14;
			else if (slotId == 47)
				hairColourCoordinate = 13;
			else if (slotId == 49)
				hairColourCoordinate = 12;
			player.getAppearence().setHairColor(hairColourCoordinate);

		}
	}

	public static void openBeardInterface(Player player) {
		player.getInterfaceManager().sendInterface(309);
		player.getPackets().sendIComponentText(309, 20, "Confirm");
		player.getPackets().sendUnlockIComponentOptionSlots(309, 10, 0, 63, 0); // hairs
																				// and
																				// mustaches
		player.getPackets().sendUnlockIComponentOptionSlots(309, 16, 1, 49, 0); // colors
		player.getTemporaryAttributtes().put("hairSaloon", true);
	}

	private PlayerLook() {

	}

}
