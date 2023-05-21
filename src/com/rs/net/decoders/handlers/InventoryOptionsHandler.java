package com.rs.net.decoders.handlers;

import java.util.List;

import com.rs.Settings;
import com.rs.cores.WorldThread;
import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.npc.familiar.Familiar.SpecialAttack;
import com.rs.game.player.Equipment;
import com.rs.game.player.Inventory;
import com.rs.game.player.Pets;
import com.rs.game.player.Player;
import com.rs.game.player.actions.Firemaking;
import com.rs.game.player.actions.Fletching;
import com.rs.game.player.actions.GemCutting;
import com.rs.game.player.actions.HerbCleaning;
import com.rs.game.player.actions.Herblore;
import com.rs.game.player.actions.Hunter;
import com.rs.game.player.actions.LeatherCrafting;
import com.rs.game.player.actions.Summoning;
import com.rs.game.player.actions.Fletching.Fletch;
import com.rs.game.player.actions.GemCutting.Gem;
import com.rs.game.player.actions.Hunter.HunterEquipment;
import com.rs.game.player.actions.Summoning.Pouches;
import com.rs.game.player.content.AncientEffigies;
import com.rs.game.player.content.Burying;
import com.rs.game.player.content.DiceGame;
import com.rs.game.player.content.Foods;
import com.rs.game.player.content.ItemOnItemHandler;
import com.rs.game.player.content.Magic;
import com.rs.game.player.content.Pots;
import com.rs.game.player.content.Runecrafting;
import com.rs.game.player.content.SkillCapeCustomizer;
import com.rs.game.player.content.Burying.Bone;
import com.rs.game.player.content.ItemOnItemHandler.ItemOnItem;
import com.rs.game.player.controlers.*;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.io.InputStream;
import com.rs.utils.ItemExamines;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

public class InventoryOptionsHandler {

	public static void handleItemOption2(final Player player, final int slotId,
			final int itemId, Item item) {
		if (Firemaking.isFiremaking(player, itemId))
			return;
		if (itemId >= 5509 && itemId <= 5514) {
			int pouch = -1;
			if (itemId == 5509)
				pouch = 0;
			if (itemId == 5510)
				pouch = 1;
			if (itemId == 5512)
				pouch = 2;
			if (itemId == 5514)
				pouch = 3;
			Runecrafting.emptyPouch(player, pouch);
			player.stopAll(false);
		} else if (itemId == 15098) {
			// DiceGame.handleItem(player, Rolls.FRIENDS_ROLL);
			return;
		} else {
			if (player.isEquipDisabled())
				return;
			long passedTime = Utils.currentTimeMillis()
					- WorldThread.LAST_CYCLE_CTM;
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					List<Integer> slots = player.getSwitchItemCache();
					int[] slot = new int[slots.size()];
					for (int i = 0; i < slot.length; i++)
						slot[i] = slots.get(i);
					player.getSwitchItemCache().clear();
					ButtonHandler.sendWear(player, slot);
					player.stopAll(false);
				}

			}, passedTime >= 600 ? 0 : passedTime > 400 ? 1 : 0);// switching
			// one item,
			// if delay
			// too close
			// to next
			// ticket,
			// delay so
			// wont
			// instant

			if (player.getSwitchItemCache().contains(slotId))
				return;
			player.getSwitchItemCache().add(slotId);
		}
	}

	public static void handleItemOption1(Player player, final int slotId,
			final int itemId, Item item) {
		long time = Utils.currentTimeMillis();
		if (player.getStopDelay() >= time
				|| player.getEmotesManager().getNextEmoteEnd() >= time)
			return;
		player.stopAll(false);
		if (Foods.eat(player, item, slotId))
			return;

		if (Burying.bury(player, slotId))
			return;
		if (itemId == 15098) {
			DiceGame.rollDice8(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2075));
			return;
		}
		if (itemId == 15086) {
			DiceGame.rollDice2(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2072));
			return;
		}
		if (itemId == 15088) {
			DiceGame.rollDice3(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2074));
			return;
		}
		if (itemId == 15090) {
			DiceGame.rollDice4(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2071));
			return;
		}
		if (itemId == 15092) {
			DiceGame.rollDice5(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2070));
			return;
		}
		if (itemId == 15094) {
			DiceGame.rollDice5(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2073));
			return;
		}
		if (itemId == 15096) {
			DiceGame.rollDice7(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2068));
			return;
		}
		if (itemId == 15098) {
			DiceGame.rollDice8(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2075));
			return;
		}
		if (itemId == 15100) {
			DiceGame.rollDice1(player);
			player.setNextAnimation(new Animation(11900));
			player.setNextGraphics(new Graphics(2069));
			return;
		}
		if (!player.getControlerManager().handleItemOption1(player, slotId,
				itemId, item))
			return;
		if (Pots.pot(player, item, slotId))
			return;
		if (itemId >= 5509 && itemId <= 5514) {
			int pouch = -1;
			if (itemId == 5509)
				pouch = 0;
			if (itemId == 5510)
				pouch = 1;
			if (itemId == 5512)
				pouch = 2;
			if (itemId == 5514)
				pouch = 3;
			Runecrafting.fillPouch(player, pouch);
			return;
		}

		if (itemId == 6199) {
			int[] RandomItems = { 11732, 4151, 11283, 385, 2347, 1712, 1712,
					6585, 1712, 6585, 11732, 11732, 3105, 6918, 6920, 6922,
					6924, 6570, 10828, 1079, 1127, 20072, 20072, 8850, 10551,
					10548, 4087, 15332, 15332, 4712, 4714, 4716, 4718, 4720,
					4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745,
					4747, 4749, 4751, 4753, 4755, 4757, 4759, 6585, 5698, 1704,
					7378, 7370, 7390, 6737, 6731, 6733, 11716, 6199, 6199,
					7386, 7394, 11846, 11850, 11852, 2673, 2669, 2671, 6889,
					6914, 2653, 2655, 2657, 1837, 10330, 11848, 11854, 11856,
					10332, 10334, 10336, 542, 4087, 4585, 6568, 6568, 10338,
					10340, 10342, 10344, 10346, 10348, 10350, 10352, 2581,
					13736, 6916, 6918, 6920, 6922, 6924, 3481, 3483, 3486,
					2577, 2665, 10452, 10454, 10456, 9470, 2661, 10450, 10446,
					10448, 1037, 14595, 14603, 1050, 23679, 23680, 23681,
					23682, 23683, 23684, 23685, 23686, 23687, 23688, 23689,
					23690, 23691, 23692, 23693, 23694, 23695, 23696, 23697,
					23698, 23699, 23700, 1040, 1042, 1044, 1046, 1048, 1050,
					1053, 1055, 1057, 11732, 3105, 1712, 1704, 1706, 1079,
					1127, 6585, 6570, }; // Other ids go in there as well
			player.getInventory().deleteItem(6199, 1);
			for (int i = 0; i < RandomItems.length; i++)
				player.getInventory().addItem(RandomItems[i], 1);
			player.getPackets().sendGameMessage(
					"You've recieved an item from the Mystery Box!");
			return;
		}
		if (itemId == 952) {// spade
			player.resetWalkSteps();
			if (Barrows.digToBrother(player)) {
				player.getControlerManager().startControler("Barrows");
				return;
			}
			player.setNextAnimation(new Animation(830));
			player.getPackets().sendGameMessage("You find nothing.");
			return;
		}
		if (HerbCleaning.clean(player, item, slotId))
			return;
		Bone bone = Bone.forId(itemId);
		if (bone != null) {
			Bone.bury(player, slotId);
			return;
		}
		if (Magic.useTabTeleport(player, itemId))
			return;
		if (itemId == AncientEffigies.SATED_ANCIENT_EFFIGY
				|| itemId == AncientEffigies.GORGED_ANCIENT_EFFIGY
				|| itemId == AncientEffigies.NOURISHED_ANCIENT_EFFIGY
				|| itemId == AncientEffigies.STARVED_ANCIENT_EFFIGY)
			player.getDialogueManager().startDialogue("AncientEffigiesD",
					itemId);
		else if (itemId == 4155)
			player.getDialogueManager().startDialogue("EnchantedGemDialouge");
		else if (itemId == 1856) {// Information Book
			player.getInterfaceManager().sendInterface(275);
			player.getPackets()
					.sendIComponentText(275, 2, Settings.SERVER_NAME);
			player.getPackets().sendIComponentText(275, 16,
					"Welcome to " + Settings.SERVER_NAME + ".");
			player.getPackets().sendIComponentText(275, 17,
					"If want some an item use command ::item id.");
			player.getPackets().sendIComponentText(275, 18,
					"If you don't have an item list you can find ids");
			player.getPackets().sendIComponentText(275, 19,
					"at http://itemdb.biz");
			player.getPackets().sendIComponentText(275, 20,
					"You can change your prayers and spells at home.");
			player.getPackets().sendIComponentText(275, 21,
					"If you need any help, do ::ticket. (Don't abuse it)");
			player.getPackets().sendIComponentText(275, 22,
					"at start of your message on public chat.");
			player.getPackets().sendIComponentText(275, 22,
					"By the way you can compare your ::score with your mates.");
			player.getPackets().sendIComponentText(275, 23,
					"Oh and ye, don't forget to ::vote and respect rules.");
			player.getPackets().sendIComponentText(275, 24, "");
			player.getPackets().sendIComponentText(275, 25,
					"Forums: " + Settings.WEBSITE_LINK);
			player.getPackets().sendIComponentText(275, 26, "");
			player.getPackets().sendIComponentText(275, 27,
					"Enjoy your time on " + Settings.SERVER_NAME + ".");
			player.getPackets().sendIComponentText(275, 28,
					"<img=1> Staff Team");
			player.getPackets().sendIComponentText(275, 29, "");
			player.getPackets().sendIComponentText(275, 30, "");
			player.getPackets().sendIComponentText(275, 14,
					"<u>Visit Website</u>");
			for (int i = 31; i < 300; i++)
				player.getPackets().sendIComponentText(275, i, "");
		} else if (itemId == HunterEquipment.BOX.getId()) // almost done
			player.getActionManager().setSkill(new Hunter(HunterEquipment.BOX));
		else if (itemId == HunterEquipment.BRID_SNARE.getId())
			player.getActionManager().setSkill(
					new Hunter(HunterEquipment.BRID_SNARE));
		if (Settings.DEBUG)
			Logger.log("ItemHandler", "Item Select:" + itemId + ", Slot Id:"
					+ slotId);
	}

	/*
	 * returns the other
	 */
	public static Item contains(int id1, Item item1, Item item2) {
		if (item1.getId() == id1)
			return item2;
		if (item2.getId() == id1)
			return item1;
		return null;
	}

	public static boolean contains(int id1, int id2, Item... items) {
		boolean containsId1 = false;
		boolean containsId2 = false;
		for (Item item : items) {
			if (item.getId() == id1)
				containsId1 = true;
			else if (item.getId() == id2)
				containsId2 = true;
		}
		return containsId1 && containsId2;
	}

	public static void handleItemOnItem(final Player player, InputStream stream) {
		int interfaceId = stream.readIntV1() >> 16;
		int itemUsedId = stream.readUnsignedShort128();
		int fromSlot = stream.readUnsignedShortLE128();
		int interfaceId2 = stream.readIntV2() >> 16;
		int itemUsedWithId = stream.readUnsignedShort128();
		int toSlot = stream.readUnsignedShortLE();
		if ((interfaceId2 == 747 || interfaceId2 == 662)
				&& interfaceId == Inventory.INVENTORY_INTERFACE) {
			if (player.getFamiliar() != null) {
				player.getFamiliar().setSpecial(true);
				if (player.getFamiliar().getSpecialAttack() == SpecialAttack.ITEM) {
					if (player.getFamiliar().hasSpecialOn())
						player.getFamiliar().submitSpecial(toSlot);
				}
			}
			return;
		}

		if (interfaceId == Inventory.INVENTORY_INTERFACE
				&& interfaceId == interfaceId2
				&& !player.getInterfaceManager().containsInventoryInter()) {
			if (toSlot >= 28 || fromSlot >= 28)
				return;
			Item usedWith = player.getInventory().getItem(toSlot);
			Item itemUsed = player.getInventory().getItem(fromSlot);
			if (itemUsed == null || usedWith == null
					|| itemUsed.getId() != itemUsedId
					|| usedWith.getId() != itemUsedWithId)
				return;
			player.stopAll();
			if (!player.getControlerManager().canUseItemOnItem(itemUsed,
					usedWith))
				return;
			Fletch fletch = Fletching.isFletching(usedWith, itemUsed);
			if (fletch != null) {
				player.getDialogueManager().startDialogue("FletchingD", fletch);
				return;
			}
			int herblore = Herblore.isHerbloreSkill(itemUsed, usedWith);
			if (herblore > -1) {
				player.getDialogueManager().startDialogue("HerbloreD",
						herblore, itemUsed, usedWith);
				return;
			}
			if (itemUsedId == 15086 && itemUsedWithId == 15086) {
				DiceGame.rollDice2(player);
				player.setNextAnimation(new Animation(11900));
				player.setNextGraphics(new Graphics(2072));
				return;
			}
			if (itemUsedId == 15088 && itemUsedWithId == 15088) {
				DiceGame.rollDice3(player);
				player.setNextAnimation(new Animation(11900));
				player.setNextGraphics(new Graphics(2074));
				return;
			}
			if (itemUsedId == 15090 && itemUsedWithId == 15090) {
				DiceGame.rollDice4(player);
				player.setNextAnimation(new Animation(11900));
				player.setNextGraphics(new Graphics(2071));
				return;
			}
			if (itemUsedId == 15092 && itemUsedWithId == 15092) {
				DiceGame.rollDice5(player);
				player.setNextAnimation(new Animation(11900));
				player.setNextGraphics(new Graphics(2070));
				return;
			}
			if (itemUsedId == 15094 && itemUsedWithId == 15094) {
				DiceGame.rollDice5(player);
				player.setNextAnimation(new Animation(11900));
				player.setNextGraphics(new Graphics(2073));
				return;
			}
			if (itemUsedId == 15096 && itemUsedWithId == 15096) {
				DiceGame.rollDice7(player);
				player.setNextAnimation(new Animation(11900));
				player.setNextGraphics(new Graphics(2068));
				return;
			}
			if (itemUsedId == 15098 && itemUsedWithId == 15098) {
				DiceGame.rollDice8(player);
				player.setNextAnimation(new Animation(11900));
				player.setNextGraphics(new Graphics(2075));
				return;
			}
			if (itemUsedId == 15100 && itemUsedWithId == 15100) {
				DiceGame.rollDice1(player);
				player.setNextAnimation(new Animation(11900));
				player.setNextGraphics(new Graphics(2069));
				return;
			}
			if (itemUsed.getId() == LeatherCrafting.NEEDLE.getId()
					|| usedWith.getId() == LeatherCrafting.NEEDLE.getId()) {
				if (LeatherCrafting
						.handleItemOnItem(player, itemUsed, usedWith)) {
					return;
				}
			}
			/*
			 * Sets set = ArmourSets.getArmourSet(itemUsedId, itemUsedWithId);
			 * if (set != null) { ArmourSets.exchangeSets(player, set); return;
			 * }
			 */
			ItemOnItem itemOnItem = ItemOnItem.forId(itemUsedId);
			if (itemOnItem != null) {
				if (itemUsedWithId == itemOnItem.getItem2())
					ItemOnItemHandler.handleItemOnItem(player, itemOnItem,
							usedWith.getId(), itemUsed.getId());
				return;
			}
			if (Firemaking.isFiremaking(player, itemUsed, usedWith))
				return;
			else if (contains(1755, Gem.OPAL.getUncut(), itemUsed, usedWith))
				GemCutting.cut(player, Gem.OPAL);
			else if (contains(1755, Gem.JADE.getUncut(), itemUsed, usedWith))
				GemCutting.cut(player, Gem.JADE);
			else if (contains(1755, Gem.RED_TOPAZ.getUncut(), itemUsed,
					usedWith))
				GemCutting.cut(player, Gem.RED_TOPAZ);
			else if (contains(1755, Gem.SAPPHIRE.getUncut(), itemUsed, usedWith))
				GemCutting.cut(player, Gem.SAPPHIRE);
			else if (contains(1755, Gem.EMERALD.getUncut(), itemUsed, usedWith))
				GemCutting.cut(player, Gem.EMERALD);
			else if (contains(1755, Gem.RUBY.getUncut(), itemUsed, usedWith))
				GemCutting.cut(player, Gem.RUBY);
			else if (contains(1755, Gem.DIAMOND.getUncut(), itemUsed, usedWith))
				GemCutting.cut(player, Gem.DIAMOND);
			else if (contains(1755, Gem.DRAGONSTONE.getUncut(), itemUsed,
					usedWith))
				GemCutting.cut(player, Gem.DRAGONSTONE);
			else if (itemUsed.getId() == 21369 && usedWith.getId() == 4151) {
				player.getInventory().deleteItem(21369, 1);
				player.getInventory().deleteItem(4151, 1);
				player.getInventory().addItem(21371, 1);
				player.sm("Good job, you have succesfully combined a whip and vine into a vine whip.");
			} else if (contains(1755, Gem.ONYX.getUncut(), itemUsed, usedWith))
				GemCutting.cut(player, Gem.ONYX);
			else
				player.getPackets().sendGameMessage(
						"Nothing interesting happens.");
			if (Settings.DEBUG)
				Logger.log("ItemHandler", "Used:" + itemUsed.getId()
						+ ", With:" + usedWith.getId());
		}
	}

	public static void handleItemOption3(Player player, int slotId, int itemId,
			Item item) {
		long time = Utils.currentTimeMillis();
		if (player.getStopDelay() >= time
				|| player.getEmotesManager().getNextEmoteEnd() >= time)
			return;
		player.stopAll(false);
		if (itemId == 20767 || itemId == 20769 || itemId == 20771)
			SkillCapeCustomizer.startCustomizing(player, itemId);
		else if (Equipment.getItemSlot(itemId) == Equipment.SLOT_AURA)
			player.getAuraManager().sendTimeRemaining(itemId);
	}

	public static void handleItemOption4(Player player, int slotId, int itemId,
			Item item) {
		System.out.println("Option 4");
	}

	public static void handleItemOption5(Player player, int slotId, int itemId,
			Item item) {
		System.out.println("Option 5");
	}

	public static void handleItemOption6(Player player, int slotId, int itemId,
			Item item) {
		long time = Utils.currentTimeMillis();
		if (player.getStopDelay() >= time
				|| player.getEmotesManager().getNextEmoteEnd() >= time)
			return;
		player.stopAll(false);
		Pouches pouches = Pouches.forId(itemId);
		if (pouches != null)
			Summoning.spawnFamiliar(player, pouches);
		else if (itemId == 1438)
			Runecrafting.locate(player, 3127, 3405);
		else if (itemId == 1440)
			Runecrafting.locate(player, 3306, 3474);
		else if (itemId == 1442)
			Runecrafting.locate(player, 3313, 3255);
		else if (itemId == 1444)
			Runecrafting.locate(player, 3185, 3165);
		else if (itemId == 1446)
			Runecrafting.locate(player, 3053, 3445);
		else if (itemId == 1448)
			Runecrafting.locate(player, 2982, 3514);
		else if (itemId <= 1712 && itemId >= 1706 || itemId >= 10354
				&& itemId <= 10362)
			player.getDialogueManager().startDialogue("Transportation",
					"Edgeville", new WorldTile(3087, 3496, 0), "Karamja",
					new WorldTile(2918, 3176, 0), "Draynor Village",
					new WorldTile(3105, 3251, 0), "Al Kharid",
					new WorldTile(3293, 3163, 0), itemId);
		else if (itemId == 1704 || itemId == 10352)
			player.getPackets()
					.sendGameMessage(
							"The amulet has ran out of charges. You need to recharge it if you wish it use it once more.");
		else if (itemId >= 3853 && itemId <= 3867)
			player.getDialogueManager().startDialogue("Transportation",
					"Burthrope Games Room", new WorldTile(2880, 3559, 0),
					"Barbarian Outpost", new WorldTile(2519, 3571, 0),
					"Gamers' Grotto", new WorldTile(2970, 9679, 0),
					"Corporeal Beast", new WorldTile(2886, 4377, 0), itemId);
	}

	public static void handleItemOption7(Player player, int slotId, int itemId,
			Item item) {
		long time = System.currentTimeMillis();
		if (player.getStopDelay() >= time
				|| player.getEmotesManager().getNextEmoteEnd() >= time)
			return;
		player.stopAll(false);
		if (item.getDefinitions().isDestroyItem()) {
			player.getDialogueManager().startDialogue("DestroyItemOption",
					new Object[] { Integer.valueOf(slotId), item });
			return;
		}
		if (player.getCharges().degradeCompletly(item)) {
			return;
		}
		/**
		 * Pets
		 */
		if (IsPet(itemId)) {
			for (int i = 0; i < itempets.length; i++) {
				if (itemId == itempets[i]) {
					if (player.getPet() != null) {
						player.sendMessage("You already have a pet spawned, please dissmis it to spawn another.");
						return;
					}
					player.setPetId(itempets[i]);
					new Pets(npcpets[i], player, new WorldTile(
							player.getX() + 1, player.getY() + 1,
							player.getPlane()), 0, false);
					player.getInventory().deleteItem(slotId, item);
				}
			}
		} else {
			player.getInventory().deleteItem(slotId, item);
			World.addGroundItem(item, new WorldTile(player), player, false,
					180, true);
			player.getPackets().sendSound(2739, 0, 1);
		}
	}

	public static void handleItemOption8(Player player, int slotId, int itemId,
			Item item) {
		player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
	}

	public static int[] itempets = { 22973, 12196, 21512, 22992, 22993, 22994,
			22995, 12469, 12470, 12471, 12472, 12473, 12474, 12475, 12476,
			12481, 12482, 12484, 12485, 12487, 12488, 12489, 12490, 12492,
			12493, 12496, 12497, 12498, 12499, 12500, 12501, 12502, 12503,
			12505, 12506, 12507, 12508, 12509, 12510, 12511, 12512, 12513,
			12514, 12515, 12516, 12517, 12518, 12519, 12520, 12521, 12523,
			14627, 14626, 7581, 7582, 7583, 7584, 7585 };
	public static int[] npcpets = { 2267, 6969, 3604, 14832, 14768, 14769,
			14770, 6900, 6901, 6902, 6903, 6904, 6905, 6906, 6907, 6908, 6909,
			6911, 6912, 6914, 6915, 6916, 6919, 6920, 6923, 6942, 6943, 6945,
			6946, 6947, 6948, 6949, 6950, 6951, 6952, 6953, 6954, 6955, 6956,
			6957, 6958, 6959, 6960, 6961, 6962, 6963, 6964, 6965, 6966, 6967,
			6968, 8550, 8551, 3503, 3504, 3505, 3506, 3507 };

	public static boolean IsPet(int j) {
		for (int i : itempets) {
			if (i != j) {
				continue;
			}
			return true;

		}
		return false;
	}
}