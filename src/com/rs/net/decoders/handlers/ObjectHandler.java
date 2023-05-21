package com.rs.net.decoders.handlers;

import com.rs.Settings;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.Animation;
import com.rs.game.ForceMovement;
import com.rs.game.ForceTalk;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.item.Item;
import com.rs.game.minigames.CastleWars;
import com.rs.game.minigames.PestControl;
import com.rs.game.minigames.War;
import com.rs.game.minigames.War.Stage;
import com.rs.game.npc.NPC;
import com.rs.game.player.CoordsEvent;
import com.rs.game.player.FarmingManager;
import com.rs.game.player.Inventory;
import com.rs.game.player.OwnedObjectManager;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.Agility;
import com.rs.game.player.actions.Cooking;
import com.rs.game.player.actions.EssenceMining;
import com.rs.game.player.actions.Farming;
import com.rs.game.player.actions.Mining;
import com.rs.game.player.actions.PlayerCombat;
import com.rs.game.player.actions.Summoning;
import com.rs.game.player.actions.Woodcutting;
import com.rs.game.player.actions.Cooking.Cookables;
import com.rs.game.player.actions.EssenceMining.EssenceDefinitions;
import com.rs.game.player.actions.Hunter.HunterEquipment;
import com.rs.game.player.actions.Hunter.HunterNPC;
import com.rs.game.player.actions.Mining.RockDefinitions;
import com.rs.game.player.actions.Smithing.ForgingBar;
import com.rs.game.player.actions.Smithing.ForgingInterface;
import com.rs.game.player.actions.Woodcutting.TreeDefinitions;
import com.rs.game.player.content.*;
import com.rs.game.player.content.Burying.Bone;
import com.rs.game.player.content.dungeoneering.*;
import com.rs.game.player.controlers.*;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.io.InputStream;
import com.rs.utils.*;

public class ObjectHandler {

	public static void handleOption1(final Player player, InputStream stream) {
		if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
				|| player.isDead())
			return;
		long currentTime = Utils.currentTimeMillis();
		if (player.getStopDelay() >= currentTime
		// || player.getFreezeDelay() >= currentTime
				|| player.getEmotesManager().getNextEmoteEnd() >= currentTime)
			return;
		@SuppressWarnings("unused")
		boolean junk = stream.readUnsignedByte128() == 1;
		int x = stream.readUnsignedShort128();
		final int id = stream.readInt();
		int y = stream.readUnsignedShortLE();
		final WorldTile tile = new WorldTile(x, y, player.getPlane());
		final int regionId = tile.getRegionId();
		if (!player.getMapRegionsIds().contains(regionId))
			return;
		WorldObject mapObject = World.getRegion(regionId).getObject(id, tile);
		if (mapObject == null || mapObject.getId() != id) { // temporary fixes
			// fix
			if (player.isAtDynamicRegion()
					&& World.getRotation(player.getPlane(), x, y) != 0) {
				ObjectDefinitions defs = ObjectDefinitions
						.getObjectDefinitions(id);
				if (defs.getSizeX() > 1 || defs.getSizeY() > 1) {
					for (int xs = 0; xs < defs.getSizeX() + 1
							&& (mapObject == null || mapObject.getId() != id); xs++) {
						for (int ys = 0; ys < defs.getSizeY() + 1
								&& (mapObject == null || mapObject.getId() != id); ys++) {
							tile.setLocation(x + xs, y + ys, tile.getPlane());
							mapObject = World.getRegion(regionId).getObject(id,
									tile);
						}
					}
				}
			}
			if (mapObject == null || mapObject.getId() != id)
				return;
		}
		final WorldObject object = !player.isAtDynamicRegion() ? mapObject
				: new WorldObject(id, mapObject.getType(),
						mapObject.getRotation(), x, y, player.getPlane());
		if (player.isAtDynamicRegion()) {
			int rotation = object.getRotation();
			rotation += World.getRotation(player.getPlane(), x, y);
			if (rotation > 3)
				rotation -= 4;
			object.setRotation(rotation);
		}
		player.stopAll(false);
		final ObjectDefinitions objectDef = object.getDefinitions();
		player.setCoordsEvent(new CoordsEvent(tile, new Runnable() {
			@Override
			public void run() {
				player.stopAll();
				player.setNextFaceWorldTile(new WorldTile(object.getCoordFaceX(
						objectDef.getSizeX(), objectDef.getSizeY(),
						object.getRotation()), object.getCoordFaceY(
						objectDef.getSizeX(), objectDef.getSizeY(),
						object.getRotation()), object.getPlane()));
				if (!player.getControlerManager().processObjectClick1(object))
					return;
				if (CastleWars.handleObjects(player, id))
					return;
				/*
				 * if (!QuestHandler.handleObject(player, object)) return;
				 */
				HunterNPC hunterNpc = HunterNPC.forObjectId(id);
				if (hunterNpc != null) {
					if (OwnedObjectManager.removeObject(player, object)) {
						player.setNextAnimation(hunterNpc.getEquipment()
								.getPickUpAnimation());
						player.getInventory().addItem(hunterNpc.getItem(), 1);
						player.getInventory().addItem(
								hunterNpc.getEquipment().getId(), 1);
						player.getSkills().addXp(Skills.HUNTER,
								hunterNpc.getXp());
						player.setTrapAmount(player.getTrapAmount() - 1);
					} else {
						player.getPackets().sendGameMessage(
								"This isn't your trap.");
					}
				} else if (id == 28213) {

					War war = player.getCurrentFriendChat().getWar();
					if (war != null && war.getStage() == Stage.STARTED)
						war.startControler(player);
					else
						player.getPackets().sendGameMessage(
								"You can't go in atm.");

				} else if (id == HunterEquipment.BOX.getObjectId()) {
					if (OwnedObjectManager.removeObject(player, object)) {
						player.setNextAnimation(new Animation(19192));
						player.getInventory().addItem(
								HunterEquipment.BOX.getId(), 1);
						player.setTrapAmount(player.getTrapAmount() - 1);
					} else
						player.getPackets().sendGameMessage(
								"This isn't your trap.");
				} else if (id == 59463) { // works now
					player.getDialogueManager().startDialogue("Crate");
					// } else if (id == 66017){
					// Barrows.processObjectClick1(object);
				} else if (id == 4277) {
					// player.sendMessage("You successfully thieve from the stall");
					player.addStopDelay(4);
					player.getInventory().addItem(995, 1270);
					player.setNextAnimation(new Animation(881));
					player.getSkills().addXp(17, 100);
				} else if (id == 2878) { // works now
					player.getDialogueManager().startDialogue("Pool");
				} else if (id == HunterEquipment.BRID_SNARE.getObjectId()) {
					if (OwnedObjectManager.removeObject(player, object)) {
						player.setNextAnimation(new Animation(19192));
						World.getRegion(regionId).removeObject(object);
						player.getInventory().addItem(
								HunterEquipment.BRID_SNARE.getId(), 1);
						player.setTrapAmount(player.getTrapAmount() - 1);
					} else
						player.getPackets().sendGameMessage(
								"This isn't your trap.");

				} else if (id == 48496) {
					new DungeonPartyManager(player);
					player.dungtime = 800;
				} else if (id == 46935 && object.getX() == 3090
						&& object.getY() == 3498) {
					TowersPkControler.enter(player);
				} else if (object.getDefinitions().name
						.equalsIgnoreCase("Obelisk") && object.getY() > 3527) {
					player.getControlerManager().startControler(
							"ObeliskControler", object);
				} else if (id == 2350
						&& (object.getX() == 3352 && object.getY() == 3417 && object
								.getPlane() == 0))
					player.useStairs(832, new WorldTile(3177, 5731, 0), 1, 2);
				else if (id == 2353
						&& (object.getX() == 3177 && object.getY() == 5730 && object
								.getPlane() == 0))
					player.useStairs(828, new WorldTile(3353, 3416, 0), 1, 2);
				else if (id == 10949 || id == 18994 || id == 18995
						|| id == 18996 || id == 3038 || id == 3245
						|| id == 11933 || id == 11934 || id == 11935
						|| id == 11957 || id == 11958 || id == 11959)
					player.getActionManager().setSkill(
							new Mining(object, RockDefinitions.Tin_Ore));
				else if (id == 37312 || id == 11952 || id == 37310) // gold ore
					player.getActionManager().setSkill(
							new Mining(object, RockDefinitions.Gold_Ore));
				else if (id == 19000 || id == 19001 || id == 19002
						|| id == 37309 || id == 37307 || id == 11954
						|| id == 11955 || id == 11956) // iron ore
					player.getActionManager().setSkill(
							new Mining(object, RockDefinitions.Iron_Ore));
				else if (id == 37306 || id == 2311 || id == 37304
						|| id == 37305) // silver ore
					player.getActionManager().setSkill(
							new Mining(object, RockDefinitions.Silver_Ore));
				else if (id == 10948 || id == 18997 || id == 18998
						|| id == 18999 || id == 14850 || id == 14851
						|| id == 3233 || id == 3032 || id == 11930
						|| id == 11931 || id == 11932) // coal ore
					player.getActionManager().setSkill(
							new Mining(object, RockDefinitions.Coal_Ore));
				else if (id == 18991 || id == 18992 || id == 18993
						|| id == 3027 || id == 3229 || id == 11936
						|| id == 11937 || id == 11938 || id == 11960
						|| id == 11961 || id == 11962) // copper
					player.getActionManager().setSkill(
							new Mining(object, RockDefinitions.Copper_Ore));
				else if (id == 3041 || id == 3280 || id == 11942 || id == 11944) // mithril
					// ore
					player.getActionManager().setSkill(
							new Mining(object, RockDefinitions.Mithril_Ore));
				else if (id == 3273 || id == 3040 || id == 11939 || id == 11941) // adamant
					// ore
					player.getActionManager().setSkill(
							new Mining(object, RockDefinitions.Adamant_Ore));
				else if (id == 14860 || id == 14861)
					player.getActionManager().setSkill(
							new Mining(object, RockDefinitions.Runite_Ore));
				else if (id == 10947)
					player.getActionManager().setSkill(
							new Mining(object, RockDefinitions.Granite_Ore));
				else if (id == 10946)
					player.getActionManager().setSkill(
							new Mining(object, RockDefinitions.Sandstone_Ore));
				else if (id == 11554 || id == 11552)
					player.getPackets().sendGameMessage(
							"That rock is currently unavailable.");
				else if (id == 2491)
					player.getActionManager()
							.setSkill(
									new EssenceMining(
											object,
											player.getSkills().getLevel(
													Skills.MINING) < 30 ? EssenceDefinitions.Rune_Essence
													: EssenceDefinitions.Pure_Essence));
				else if (id == 2478)
					Runecrafting.craftEssence(player, 556, 1, 5, false, 11, 2,
							22, 3, 34, 4, 44, 5, 55, 6, 66, 7, 77, 88, 9, 99,
							10);
				else if (id == 2479)
					Runecrafting.craftEssence(player, 558, 2, 5.5, false, 14,
							2, 28, 3, 42, 4, 56, 5, 70, 6, 84, 7, 98, 8);
				else if (id == 2480)
					Runecrafting.craftEssence(player, 555, 5, 6, false, 19, 2,
							38, 3, 57, 4, 76, 5, 95, 6);
				else if (id == 2481)
					Runecrafting.craftEssence(player, 557, 9, 6.5, false, 26,
							2, 52, 3, 78, 4);
				else if (id == 2482)
					Runecrafting.craftEssence(player, 554, 14, 7, false, 35, 2,
							70, 3);
				else if (id == 2483)
					Runecrafting.craftEssence(player, 559, 20, 7.5, false, 46,
							2, 92, 3);
				else if (id == 2484)
					Runecrafting.craftEssence(player, 564, 27, 8, true, 59, 2);
				else if (id == 2487)
					Runecrafting
							.craftEssence(player, 562, 35, 8.5, true, 74, 2);
				else if (id == 17010)
					Runecrafting.craftEssence(player, 9075, 40, 8.7, true, 82,
							2);
				else if (id == 2486)
					Runecrafting.craftEssence(player, 561, 45, 9, true, 91, 2);
				else if (id == 2485)
					Runecrafting.craftEssence(player, 563, 50, 9.5, true);
				else if (id == 2488)
					Runecrafting.craftEssence(player, 560, 65, 10, true);
				else if (id == 30624)
					Runecrafting.craftEssence(player, 565, 77, 10.5, true);
				else if (id == 2452) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.AIR_TIARA
							|| hatId == Runecrafting.OMNI_TIARA)
						Runecrafting.enterAirAltar(player);
				} else if (id == 2455) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.EARTH_TIARA
							|| hatId == Runecrafting.OMNI_TIARA)
						Runecrafting.enterEarthAltar(player);
				} else if (id == 2456) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.FIRE_TIARA
							|| hatId == Runecrafting.OMNI_TIARA)
						Runecrafting.enterFireAltar(player);
				} else if (id == 2454) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.WATER_TIARA
							|| hatId == Runecrafting.OMNI_TIARA)
						Runecrafting.enterWaterAltar(player);
				} else if (id == 2457) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.BODY_TIARA
							|| hatId == Runecrafting.OMNI_TIARA)
						Runecrafting.enterBodyAltar(player);
				} else if (id == 2453) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == Runecrafting.MIND_TIARA
							|| hatId == Runecrafting.OMNI_TIARA)
						Runecrafting.enterMindAltar(player);
				} else if (id == 36972) {
					player.setNextAnimation(new Animation(712));
					player.setNextGraphics(new com.rs.game.Graphics(624));
					player.getPackets().sendGameMessage("You pray to the gods.");
					player.getInventory().deleteItem(536, 1);
					// player.getSkills().addXp(Skills.PRAYER, 300);
				} else if (id == 36972) {
					player.setNextAnimation(new Animation(712));
					player.setNextGraphics(new com.rs.game.Graphics(624));
					player.getPackets().sendGameMessage("You pray to the gods.");
					player.getInventory().deleteItem(18830, 1);
					// player.getSkills().addXp(Skills.PRAYER, 600);
				} else if (id == 47120) { // zaros altar
					// recharge if needed
					if (player.getPrayer().getPrayerpoints() < player
							.getSkills().getLevelForXp(Skills.PRAYER) * 10) {
						player.addStopDelay(12);
						player.setNextAnimation(new Animation(12563));
						player.getPrayer().setPrayerpoints(
								(int) ((player.getSkills().getLevelForXp(
										Skills.PRAYER) * 10) * 1.15));
						player.getPrayer().refreshPrayerPoints();
					}
					player.getDialogueManager().startDialogue("ZarosAltar");
				}
				/*
				 * else if (id == 9369) { if (player.getX() == 2399 &&
				 * player.getY() == 5177) {
				 * FightPitsControler.enterWaitRoom(player);
				 * player.getControlerManager
				 * ().startControler("FightPitsControler"); } else if
				 * (player.getX() == 2399 && player.getY() == 5175)
				 * player.addWalkSteps(2399, 5175, -1, false); }
				 */
				else if (id == 36786)
					player.getDialogueManager().startDialogue("Banker", 4907);
				else if (id == 42377 || id == 42378)
					player.getDialogueManager().startDialogue("Banker", 2759);
				else if (id == 42217 || id == 782 || id == 34752 || id == 4369)
					player.getDialogueManager().startDialogue("Banker", 553);
				/*
				 * else if (id == 57437) player.getBank().openBank(); else if
				 * (id == 6084) player.getBank().openBank(); else if (id ==
				 * 22819) player.getBank().openBank(); else if (id == 25808)
				 * player.getBank().openBank();
				 */
				else if (id == 42425 && object.getX() == 3220
						&& object.getY() == 3222) { // zaros portal
					player.useStairs(10256, new WorldTile(3353, 3416, 0), 4, 5,
							"And you find yourself into a digsite.");
					player.addWalkSteps(3222, 3223, -1, false);
					player.getPackets().sendGameMessage(
							"You examine portal and it aborves you...");
				}/*
				 * else if (id ==
				 * HunterNPC.CRIMSON_SWIFT.getTransformObjectId()) {
				 * player.getInventory
				 * ().addItem(HunterNPC.CRIMSON_SWIFT.getItem(), 1);
				 * player.getInventory
				 * ().addItem(HunterEquipment.BRID_SNARE.getId(), 1);
				 * player.setNextAnimation
				 * (HunterEquipment.BRID_SNARE.getPickUpAnimation());
				 * player.getSkills().addXp(Skills.HUNTER,
				 * HunterNPC.CRIMSON_SWIFT.getXp());
				 * player.setTrampAmount(player.getTrampAmount() - 1);
				 * World.removeObject(object, true); } else if (id ==
				 * HunterNPC.CERULEAN_TWITCH.getTransformObjectId()) {
				 * player.getInventory
				 * ().addItem(HunterNPC.CERULEAN_TWITCH.getItem(), 1);
				 * player.getInventory
				 * ().addItem(HunterEquipment.BRID_SNARE.getId(), 1);
				 * player.setNextAnimation
				 * (HunterEquipment.BRID_SNARE.getPickUpAnimation());
				 * player.setTrampAmount(player.getTrampAmount() - 1);
				 * World.removeObject(object, true); } else if (id ==
				 * HunterNPC.COPPER_LONGTAIL.getTransformObjectId()) {
				 * player.getInventory
				 * ().addItem(HunterNPC.COPPER_LONGTAIL.getItem(), 1);
				 * player.getInventory
				 * ().addItem(HunterEquipment.BRID_SNARE.getId(), 1);
				 * player.setNextAnimation
				 * (HunterEquipment.BRID_SNARE.getPickUpAnimation());
				 * player.setTrampAmount(player.getTrampAmount() - 1);
				 * World.removeObject(object, true); } else if (id ==
				 * HunterNPC.FERRT.getTransformObjectId()) {
				 * player.getInventory().addItem(HunterNPC.FERRT.getItem(), 1);
				 * player.getInventory().addItem(HunterEquipment.BOX.getId(),
				 * 1);
				 * player.setNextAnimation(HunterEquipment.BOX.getPickUpAnimation
				 * ()); player.setTrampAmount(player.getTrampAmount() - 1);
				 * World.removeObject(object, true); } else if (id ==
				 * HunterNPC.GECKO.getTransformObjectId()) {
				 * player.getInventory().addItem(HunterNPC.GECKO.getItem(), 1);
				 * player.getInventory().addItem(HunterEquipment.BOX.getId(),
				 * 1);
				 * player.setNextAnimation(HunterEquipment.BOX.getPickUpAnimation
				 * ()); player.setTrampAmount(player.getTrampAmount() - 1);
				 * World.removeObject(object, true); } else if (id ==
				 * HunterNPC.GOLDEN_WARBLER.getTransformObjectId()) {
				 * player.getInventory().addItem(HunterNPC.FERRT.getItem(), 1);
				 * player
				 * .getInventory().addItem(HunterEquipment.BRID_SNARE.getId(),
				 * 1); player.setNextAnimation(HunterEquipment.BRID_SNARE.
				 * getPickUpAnimation());
				 * player.setTrampAmount(player.getTrampAmount() - 1);
				 * World.removeObject(object, true); } else if (id ==
				 * HunterNPC.MONKEY.getTransformObjectId()) {
				 * player.getInventory().addItem(HunterNPC.MONKEY.getItem(), 1);
				 * player.getInventory().addItem(HunterEquipment.BOX.getId(),
				 * 1);
				 * player.setNextAnimation(HunterEquipment.BOX.getPickUpAnimation
				 * ()); player.setTrampAmount(player.getTrampAmount() - 1);
				 * World.removeObject(object, true); } else if (id ==
				 * HunterNPC.RACCOON.getTransformObjectId()) {
				 * player.getInventory().addItem(HunterNPC.RACCOON.getItem(),
				 * 1);
				 * player.getInventory().addItem(HunterEquipment.BOX.getId(),
				 * 1);
				 * player.setNextAnimation(HunterEquipment.BOX.getPickUpAnimation
				 * ()); player.setTrampAmount(player.getTrampAmount() - 1);
				 * World.removeObject(object, true); } else if (id ==
				 * HunterNPC.TROPICAL_WAGTAIL.getTransformObjectId()) {
				 * player.getInventory
				 * ().addItem(HunterNPC.TROPICAL_WAGTAIL.getItem(), 1);
				 * player.getInventory
				 * ().addItem(HunterEquipment.BRID_SNARE.getId(), 1);
				 * player.setNextAnimation
				 * (HunterEquipment.BRID_SNARE.getPickUpAnimation());
				 * player.setTrampAmount(player.getTrampAmount() - 1);
				 * World.removeObject(object, true); } else if (id ==
				 * HunterNPC.WIMPY_BIRD.getTransformObjectId()) {
				 * player.getInventory().addItem(HunterNPC.WIMPY_BIRD.getItem(),
				 * 1);
				 * player.getInventory().addItem(HunterEquipment.BRID_SNARE.getId
				 * (), 1); player.setNextAnimation(HunterEquipment.BRID_SNARE.
				 * getPickUpAnimation());
				 * player.setTrampAmount(player.getTrampAmount() - 1);
				 * World.removeObject(object, true); }
				 */else if (id == 46500 && object.getX() == 3351
						&& object.getY() == 3415) { // zaros portal
					player.useStairs(-1, new WorldTile(
							Settings.RESPAWN_PLAYER_LOCATION.getX(),
							Settings.RESPAWN_PLAYER_LOCATION.getY(),
							Settings.RESPAWN_PLAYER_LOCATION.getPlane()), 2, 3,
							"You found your way back to home.");
					player.addWalkSteps(3351, 3415, -1, false);
				} else if (id == 9293) {
					if (player.getSkills().getLevel(Skills.AGILITY) < 70) {
						player.getPackets()
								.sendGameMessage(
										"You need an agility level of 70 to use this obstacle.",
										true);
						return;
					}
					int x = player.getX() == 2886 ? 2892 : 2886;
					WorldTasksManager.schedule(new WorldTask() {
						int count = 0;

						@Override
						public void run() {
							player.setNextAnimation(new Animation(844));
							if (count++ == 1)
								stop();
						}

					}, 0, 0);
					player.setNextForceMovement(new ForceMovement(
							new WorldTile(x, 9799, 0), 3,
							player.getX() == 2886 ? 1 : 3));
					player.useStairs(-1, new WorldTile(x, 9799, 0), 3, 4);
				} else if (id == 2295)
					Agility.walkGnomeLog(player);
				else if (id == 2285)
					Agility.climbGnomeObstacleNet(player);
				else if (id == 35970)
					Agility.climbUpGnomeTreeBranch(player);
				else if (id == 2312)
					Agility.walkGnomeRope(player);
				else if (id == 4059)
					Agility.walkBackGnomeRope(player);
				else if (id == 2314)
					Agility.climbDownGnomeTreeBranch(player);
				else if (id == 2286)
					Agility.climbGnomeObstacleNet2(player);
				else if (id == 43543 || id == 43544)
					Agility.enterGnomePipe(player, object.getX(), object.getY());
				else if (Wilderness.isDitch(id)) {// wild ditch
					player.getDialogueManager().startDialogue(
							"WildernessDitch", object);
				} else if (id == 42611) {// Magic Portal
					player.getDialogueManager().startDialogue("MagicPortal");
				} else if (id == 27254) {// Edgeville portal
					player.getPackets().sendGameMessage(
							"You enter the portal...");
					player.useStairs(10584, new WorldTile(3087, 3488, 0), 2, 3,
							"..and are transported to Edgeville.");
					player.addWalkSteps(1598, 4506, -1, false);
				} else if (id == 15522) {// portal sign
					if (player.withinDistance(new WorldTile(1598, 4504, 0), 1)) {// PORTAL
						// 1
						player.getInterfaceManager().sendInterface(327);
						player.getPackets().sendIComponentText(327, 13,
								"Edgeville");
						player.getPackets()
								.sendIComponentText(
										327,
										14,
										"This portal will take you to edgeville. There "
												+ "you can multi pk once past the wilderness ditch.");
					}
					if (player.withinDistance(new WorldTile(1598, 4508, 0), 1)) {// PORTAL
						// 2
						player.getInterfaceManager().sendInterface(327);
						player.getPackets().sendIComponentText(327, 13,
								"Mage Bank");
						player.getPackets()
								.sendIComponentText(
										327,
										14,
										"This portal will take you to the mage bank. "
												+ "The mage bank is a 1v1 deep wilderness area.");
					}
					if (player.withinDistance(new WorldTile(1598, 4513, 0), 1)) {// PORTAL
						// 3
						player.getInterfaceManager().sendInterface(327);
						player.getPackets().sendIComponentText(327, 13,
								"Magic's Portal");
						player.getPackets()
								.sendIComponentText(
										327,
										14,
										"This portal will allow you to teleport to areas that "
												+ "will allow you to change your magic spell book.");
					}
				} else if (id == 37929) {// corp beast
					if (object.getX() == 2971 && object.getY() == 4382
							&& object.getPlane() == 0)
						player.getInterfaceManager().sendInterface(650);
					else if (object.getX() == 2918 && object.getY() == 4382
							&& object.getPlane() == 0) {
						player.stopAll();
						player.setNextWorldTile(new WorldTile(
								player.getX() == 2921 ? 2917 : 2921, player
										.getY(), player.getPlane()));
					}
				} else if (id == 37928 && object.getX() == 2883
						&& object.getY() == 4370 && object.getPlane() == 0) {
					player.stopAll();
					player.setNextWorldTile(new WorldTile(3214, 3782, 0));
					player.getControlerManager().startControler("Wilderness");
				} else if (id == 38815 && object.getX() == 3209
						&& object.getY() == 3780 && object.getPlane() == 0) {
					if (player.getSkills().getLevelForXp(Skills.WOODCUTTING) < 37
							|| player.getSkills().getLevelForXp(Skills.MINING) < 45
							|| player.getSkills().getLevelForXp(
									Skills.SUMMONING) < 23
							|| player.getSkills().getLevelForXp(
									Skills.FIREMAKING) < 47
							|| player.getSkills().getLevelForXp(Skills.PRAYER) < 55) {
						player.getPackets()
								.sendGameMessage(
										"You need 23 Summoning, 37 Woodcutting, 45 Mining, 47 Firemaking and 55 Prayer to enter this dungeon.");
						return;
					}
					player.stopAll();
					player.setNextWorldTile(new WorldTile(2885, 4372, 0));
					player.getControlerManager().forceStop();
					// TODO all reqs, skills not added
				} else if (id == 9369) {
					player.getControlerManager().startControler("FightPits");
				} else if (id == 50205) {
					Summoning.infusePouches(player);
				} else if (id == 54019 || id == 54020 || id == 55301)
					PkRank.showRanks(player);
				else if (id == 1817 && object.getX() == 2273
						&& object.getY() == 4680) { // kbd lever
					Magic.pushLeverTeleport(player, new WorldTile(3067, 10254,
							0));
				} else if (id == 1816 && object.getX() == 3067
						&& object.getY() == 10252) { // kbd out lever
					Magic.pushLeverTeleport(player,
							new WorldTile(2273, 4681, 0));
				} else if (id == 9356) {
					player.getDialogueManager().startDialogue("JadEnter");
				} else if (id == 28779) {
					player.getDialogueManager().startDialogue("BorkEnter");
				} else if (id == 28698) {
					player.getDialogueManager().startDialogue("LunarAltar");
				} else if (id == 32015 && object.getX() == 3069
						&& object.getY() == 10256) { // kbd stairs
					player.useStairs(828, new WorldTile(3017, 3848, 0), 1, 2);
					player.getControlerManager().startControler("Wilderness");
				} else if (id == 1765 && object.getX() == 3017
						&& object.getY() == 3849) { // kbd out stairs
					player.stopAll();
					player.setNextWorldTile(new WorldTile(3069, 10255, 0));
					player.getControlerManager().forceStop();
				} else if ((id == 14315) || (id == 14314)) {
					player.setPestControl(new PestControl(player));
					player.getPestControl().handleObjectClick1(player, object);
				} else if (id == 5959) {
					Magic.pushLeverTeleport(player,
							new WorldTile(2539, 4712, 0));
				} else if (id == 5960) {
					Magic.pushLeverTeleport(player,
							new WorldTile(3089, 3957, 0));
				} else if (id == 62675)
					player.getCutscenesManager().play("DTPreview");
				else if (id == 62681)
					player.getDominionTower().viewScoreBoard();
				else if (id == 2273) {
					player.setNextWorldTile(new WorldTile(2851, 5933, 0));
					player.sm("Use your fire cape on the floating orb to bring out Har'Arken.");
					player.sm("WARNING     WARNING     WARNING     WARNING     WARNING     WARNING     WARNING");
					player.sm("You will lose your fire cape and not be able to get it back, but gain the kiln cape if you win!");
				} else if (id == 62678 || id == 62679)
					player.getDominionTower().openModes();
				else if (id == 62688)
					player.getDialogueManager().startDialogue("DTClaimRewards");
				else if (id == 62677)
					player.getDominionTower().talkToFace();
				else if (id == 62680)
					player.getDominionTower().openBankChest();
				else if (id == 62676) { // dominion exit
					player.useStairs(-1, new WorldTile(3374, 3093, 0), 0, 1);
				} else if (id == 62674) { // dominion entrance
					player.useStairs(-1, new WorldTile(3744, 6405, 0), 0, 1);
				} else if (id == 26384) {
					if (player.BandosKC < 40) {
						player.getPackets()
								.sendGameMessage(
										"You need a Bandos killcount of at least 40 to enter this room.");
						return;
					} else if (player.BandosKC >= 40) {
						player.move(new WorldTile(2864, 5354, 2));
					}

					/**
					 * 
					 * Godwars stuff.
					 * 
					 */
					switch (object.getId()) {
					case 26384:
						if (player.BandosKC < 40) {
							player.getPackets()
									.sendGameMessage(
											"You need a Bandos killcount of at least 40 to enter this room.");
							return;
						} else if (player.BandosKC >= 40) {
							player.move(new WorldTile(2864, 5354, 2));
						}
						break;
					case 26428:
						if (player.ZamorakKC < 40) {
							player.getPackets()
									.sendGameMessage(
											"You need a Zamorak killcount of at least 40 to enter this room.");
							return;
						} else if (player.ZamorakKC >= 40) {
							player.move(new WorldTile(2925, 5331, 2));
						}
						break;
					case 26427:
						if (player.SaradominKC < 40) {
							player.getPackets()
									.sendGameMessage(
											"You need a Saradomin killcount of at least 40 to enter this room.");
							return;
						} else if (player.SaradominKC >= 40) {
							player.move(new WorldTile(2907, 5265, 0));
						}
						break;
					case 26426:
						if (player.ArmadylKC < 40) {
							player.getPackets()
									.sendGameMessage(
											"You need an Armadyl killcount of at least 40 to enter this room.");
							return;
						} else if (player.ArmadylKC >= 40) {
							player.move(new WorldTile(2839, 5296, 2));
						}
						break;
					case 26444:
						player.move(new WorldTile(2916, 5300, 1));
						break;
					case 26445:
						player.move(new WorldTile(2198, 5273, 0));
						break;
					case 57225:
						player.getDialogueManager()
								.startDialogue("NexEntrance");
						break;
					}

				} else {
					switch (objectDef.name.toLowerCase()) {
					case "web":
						if (objectDef.containsOption(0, "Slash")) {
							player.setNextAnimation(new Animation(PlayerCombat
									.getWeaponAttackEmote(player.getEquipment()
											.getWeaponId(), player
											.getCombatDefinitions()
											.getAttackStyle())));
							slashWeb(player, object);
						}
						break;
					case "bank booth":
						if (objectDef.containsOption(0, "Bank"))
							player.getBank().openBank();
						break;
					case "bank chest":
						if (objectDef.containsOption(0, "Use"))
							player.getBank().openBank();
					case "bank deposit box":
						if (objectDef.containsOption(0, "Deposit"))
							player.getBank().openDepositBox();
						break;
					case "bank":
						player.getBank().openBank();
						break;
					// Woodcutting start
					case "tree":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setSkill(
									new Woodcutting(object,
											TreeDefinitions.NORMAL));
						break;
					case "dead tree":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setSkill(
									new Woodcutting(object,
											TreeDefinitions.DEAD));
						break;
					case "oak":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager()
									.setSkill(
											new Woodcutting(object,
													TreeDefinitions.OAK));
						break;
					case "willow":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setSkill(
									new Woodcutting(object,
											TreeDefinitions.WILLOW));
						break;
					case "maple tree":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setSkill(
									new Woodcutting(object,
											TreeDefinitions.MAPLE));
						break;
					case "ivy":
						if (objectDef.containsOption(0, "Chop"))
							player.getActionManager()
									.setSkill(
											new Woodcutting(object,
													TreeDefinitions.IVY));
						break;
					case "yew":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager()
									.setSkill(
											new Woodcutting(object,
													TreeDefinitions.YEW));
						break;
					case "magic tree":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setSkill(
									new Woodcutting(object,
											TreeDefinitions.MAGIC));
						break;
					case "cursed magic tree":
						if (objectDef.containsOption(0, "Chop down"))
							player.getActionManager().setSkill(
									new Woodcutting(object,
											TreeDefinitions.CURSED_MAGIC));
						break;
					// Woodcutting end
					case "gate":
					case "large door":
					case "metal door":
						if (id == 21600) {
							World.removeObject(object, true);
							return;
						}

						if (object.getType() == 0
								&& objectDef.containsOption(0, "Open"))
							handleGate(player, object);
						break;
					case "door":
						if (id == 21507 || id == 21505) {
							World.removeObject(object, true);
							return;
						}

						if (object.getType() == 0
								&& (objectDef.containsOption(0, "Open") || objectDef
										.containsOption(0, "Unlock")))
							handleDoor(player, object);
						break;
					case "ladder":
						if (id == 21512 || id == 21514)
							return;

						handleLadder(player, object, 1);
						break;
					case "staircase":
						handleStaircases(player, object, 1);
						break;
					case "altar":
						if (objectDef.containsOption(0, "Pray-at")) {
							final int maxPrayer = player.getSkills()
									.getLevelForXp(Skills.PRAYER) * 10;
							if (player.getPrayer().getPrayerpoints() < maxPrayer) {
								player.addStopDelay(5);
								player.getPackets().sendGameMessage(
										"You pray to the gods...", true);
								player.setNextAnimation(new Animation(645));
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										player.getPrayer().restorePrayer(
												maxPrayer);
										player.getPackets()
												.sendGameMessage(
														"...and recharged your prayer.",
														true);
									}
								}, 2);
							} else {
								player.getPackets().sendGameMessage(
										"You already have full prayer.", true);
							}
							if (id == 6552)
								player.getDialogueManager().startDialogue(
										"AncientAltar");
						}
						break;
					}
				}
				Logger.log("ObjectHandler", "cliked 1 at object id : " + id
						+ ", " + object.getX() + ", " + object.getY() + ", "
						+ object.getPlane() + ", " + object.getType() + ", "
						+ object.getRotation() + ", "
						+ object.getDefinitions().name);
			}

		}, objectDef.getSizeX(), Wilderness.isDitch(id) ? 4 : objectDef
				.getSizeY(), object.getRotation()));
	}

	public static void slashWeb(Player player, WorldObject object) {

		if (Utils.getRandom(1) == 0) {
			World.spawnTemporaryObject(new WorldObject(object.getId() + 1,
					object.getType(), object.getRotation(), object.getX(),
					object.getY(), object.getPlane()), 60000, true);
			player.getPackets().sendGameMessage("You slash through the web!");
		} else
			player.getPackets().sendGameMessage(
					"You fail to cut through the web.");
	}

	public static void handleOption2(final Player player, InputStream stream) {
		if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
				|| player.isDead())
			return;
		long currentTime = Utils.currentTimeMillis();
		if (player.getStopDelay() >= currentTime
				|| player.getFreezeDelay() >= currentTime
				|| player.getEmotesManager().getNextEmoteEnd() >= currentTime)
			return;
		@SuppressWarnings("unused")
		boolean junk = stream.readUnsignedByte128() == 1;
		int x = stream.readUnsignedShort128();
		final int id = stream.readInt();
		int y = stream.readUnsignedShortLE();
		final WorldTile tile = new WorldTile(x, y, player.getPlane());
		int regionId = tile.getRegionId();
		if (!player.getMapRegionsIds().contains(regionId))
			return;
		WorldObject mapObject = World.getRegion(regionId).getObject(id, tile);
		if (mapObject == null || mapObject.getId() != id) { // temporary fixes
			// fix
			if (player.isAtDynamicRegion()
					&& World.getRotation(player.getPlane(), x, y) != 0) {
				ObjectDefinitions defs = ObjectDefinitions
						.getObjectDefinitions(id);
				if (defs.getSizeX() > 1 || defs.getSizeY() > 1) {
					for (int xs = 0; xs < defs.getSizeX() + 1
							&& (mapObject == null || mapObject.getId() != id); xs++) {
						for (int ys = 0; ys < defs.getSizeY() + 1
								&& (mapObject == null || mapObject.getId() != id); ys++) {
							tile.setLocation(x + xs, y + ys, tile.getPlane());
							mapObject = World.getRegion(regionId).getObject(id,
									tile);
						}
					}
				}
			}
			if (mapObject == null || mapObject.getId() != id)
				return;
		}

		final WorldObject object = !player.isAtDynamicRegion() ? mapObject
				: new WorldObject(id, mapObject.getType(),
						mapObject.getRotation(), x, y, player.getPlane());
		if (player.isAtDynamicRegion()) {
			int rotation = object.getRotation();
			rotation += World.getRotation(player.getPlane(), x, y);
			if (rotation > 3)
				rotation -= 4;
			object.setRotation(rotation);
		}
		player.stopAll(false);
		final ObjectDefinitions objectDef = object.getDefinitions();
		player.setCoordsEvent(new CoordsEvent(tile, new Runnable() {
			@Override
			public void run() {
				player.stopAll();
				player.setNextFaceWorldTile(new WorldTile(object.getCoordFaceX(
						objectDef.getSizeX(), objectDef.getSizeY(),
						object.getRotation()), object.getCoordFaceY(
						objectDef.getSizeX(), objectDef.getSizeY(),
						object.getRotation()), object.getPlane()));
				if (!player.getControlerManager().processObjectClick2(object))
					return;
				if (id == 36786 || id == 42378 || id == 42377 || id == 42217
						|| id == 27663)
					player.getBank().openBank();
				else if (object.getDefinitions().name
						.equalsIgnoreCase("furnace"))
					player.getDialogueManager().startDialogue("SmeltingD",
							object);
				else if (id == 61)
					player.getDialogueManager().startDialogue("LunarAltar");
				else if (id == 62677)
					player.getDominionTower().openRewards();
				/*------------ Theiving stalls ----------------------- 
				else if (id == 4277)
				                        player.setNextAnimation(new Animation(881));
				else if (id == 4277)
				            		player.getPackets().sendGameMessage(
				                   			"You successfully thieve from the stall");
				else if (id == 4277)
					player.getInventory().addItem(995, 5000); //The amount of GP it gives.
				else if (id == 4277)
					player.addStopDelay(4); //delay so it don't auto thieve rapidly & people autoclick
				else if (id == 4277)
					player.getSkills().addXp(17, 1000);
				--------------- END ---------------------------------*/
				else if (id == 62688)
					player.getDialogueManager().startDialogue(
							"SimpleMessage",
							"You have a Dominion Factor of "
									+ player.getDominionTower()
											.getDominionFactor() + ".");
				else if (id == 34384 || id == 34383 || id == 14011
						|| id == 7053 || id == 34387 || id == 34386
						|| id == 34385)
					Thieving.handleStalls(player, object);
				else {
					switch (objectDef.name.toLowerCase()) {
					case "gate":
					case "metal door":
						if (object.getType() == 0
								&& objectDef.containsOption(1, "Open"))
							handleGate(player, object);
						break;
					case "door":
						if (id == 21507) {
							World.removeObject(object, true);
							return;
						}

						if (object.getType() == 0
								&& objectDef.containsOption(1, "Open"))
							handleDoor(player, object);
						break;
					case "ladder":
						if (id == 21512)
							return;

						handleLadder(player, object, 2);
						break;
					case "staircase":
						handleStaircases(player, object, 2);
						break;
					}
				}
				if (Settings.DEBUG)
					Logger.log("ObjectHandler", "cliked 2 at object id : " + id
							+ ", " + object.getX() + ", " + object.getY()
							+ ", " + object.getPlane());
			}
		}, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
	}

	public static void handleExamine(final Player player, InputStream stream) {
		if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
				|| player.isDead())
			return;
		@SuppressWarnings("unused")
		boolean junk = stream.readUnsignedByte128() == 1;
		int x = stream.readUnsignedShort128();
		final int id = stream.readInt();
		int y = stream.readUnsignedShortLE();
		final WorldTile tile = new WorldTile(x, y, player.getPlane());
		int regionId = tile.getRegionId();
		if (!player.getMapRegionsIds().contains(regionId))
			return;
		WorldObject mapObject = World.getRegion(regionId).getObject(id, tile);
		if (mapObject == null || mapObject.getId() != id)
			return;
		final WorldObject object = !player.isAtDynamicRegion() ? mapObject
				: new WorldObject(id, mapObject.getType(),
						mapObject.getRotation(), x, y, player.getPlane());
		player.getPackets().sendGameMessage(
				"It's an " + object.getDefinitions().name + ".");
		Logger.log("ObjectHandler", "examined object: " + id + ", x: " + x
				+ ", y: " + y);
	}

	public static void handleOption3(final Player player, InputStream stream) {
		if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
				|| player.isDead())
			return;
		long currentTime = Utils.currentTimeMillis();
		if (player.getStopDelay() >= currentTime
		// || player.getFreezeDelay() >= currentTime
				|| player.getEmotesManager().getNextEmoteEnd() >= currentTime)
			return;
		@SuppressWarnings("unused")
		boolean junk = stream.readUnsignedByte128() == 1;
		int x = stream.readUnsignedShort128();
		final int id = stream.readInt();
		int y = stream.readUnsignedShortLE();
		final WorldTile tile = new WorldTile(x, y, player.getPlane());
		int regionId = tile.getRegionId();
		if (!player.getMapRegionsIds().contains(regionId))
			return;
		WorldObject mapObject = World.getRegion(regionId).getObject(id, tile);
		if (mapObject == null || mapObject.getId() != id) { // temporary fixes
			// fix
			if (player.isAtDynamicRegion()
					&& World.getRotation(player.getPlane(), x, y) != 0) {
				ObjectDefinitions defs = ObjectDefinitions
						.getObjectDefinitions(id);
				if (defs.getSizeX() > 1 || defs.getSizeY() > 1) {
					for (int xs = 0; xs < defs.getSizeX() + 1
							&& (mapObject == null || mapObject.getId() != id); xs++) {
						for (int ys = 0; ys < defs.getSizeY() + 1
								&& (mapObject == null || mapObject.getId() != id); ys++) {
							tile.setLocation(x + xs, y + ys, tile.getPlane());
							mapObject = World.getRegion(regionId).getObject(id,
									tile);
						}
					}
				}
			}
			if (mapObject == null || mapObject.getId() != id)
				return;
		}
		final WorldObject object = !player.isAtDynamicRegion() ? mapObject
				: new WorldObject(id, mapObject.getType(),
						mapObject.getRotation(), x, y, player.getPlane());
		if (player.isAtDynamicRegion()) {
			int rotation = object.getRotation();
			rotation += World.getRotation(player.getPlane(), x, y);
			if (rotation > 3)
				rotation -= 4;
			object.setRotation(rotation);
		}
		player.stopAll(false);
		final ObjectDefinitions objectDef = object.getDefinitions();
		player.setCoordsEvent(new CoordsEvent(tile, new Runnable() {
			@Override
			public void run() {
				player.stopAll();
				player.setNextFaceWorldTile(new WorldTile(object.getCoordFaceX(
						objectDef.getSizeX(), objectDef.getSizeY(),
						object.getRotation()), object.getCoordFaceY(
						objectDef.getSizeX(), objectDef.getSizeY(),
						object.getRotation()), object.getPlane()));
				if (!player.getControlerManager().processObjectClick3(object))
					return;
				player.setNextFaceWorldTile(tile);
				switch (objectDef.name.toLowerCase()) {
				case "gate":
				case "metal door":
					if (object.getType() == 0
							&& objectDef.containsOption(2, "Open"))
						handleGate(player, object);
					break;
				case "door":
					if (object.getType() == 0
							&& objectDef.containsOption(2, "Open"))
						handleDoor(player, object);
					break;
				case "ladder":
					if (id == 21512)
						return;

					handleLadder(player, object, 3);
					break;
				case "staircase":
					handleStaircases(player, object, 3);
					break;
				}
				if (Settings.DEBUG)
					Logger.log("ObjectHandler", "cliked 3 at object id : " + id
							+ ", " + object.getX() + ", " + object.getY()
							+ ", " + object.getPlane() + ", ");
			}
		}, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
	}

	public static boolean handleGate(Player player, WorldObject object) {
		if (World.isSpawnedObject(object))
			return false;
		if (object.getRotation() == 0) {

			boolean south = true;
			WorldObject otherDoor = World.getObject(new WorldTile(
					object.getX(), object.getY() + 1, object.getPlane()),
					object.getType());
			if (otherDoor == null
					|| otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object
							.getDefinitions().name)) {
				otherDoor = World.getObject(
						new WorldTile(object.getX(), object.getY() - 1, object
								.getPlane()), object.getType());
				if (otherDoor == null
						|| otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name
								.equalsIgnoreCase(object.getDefinitions().name))
					return false;
				south = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(),
					object.getType(), object.getRotation() + 1, object.getX(),
					object.getY(), object.getPlane());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(),
					otherDoor.getType(), otherDoor.getRotation() + 1,
					otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
			if (south) {
				openedDoor1.moveLocation(-1, 0, 0);
				openedDoor1.setRotation(3);
				openedDoor2.moveLocation(-1, 0, 0);
			} else {
				openedDoor1.moveLocation(-1, 0, 0);
				openedDoor2.moveLocation(-1, 0, 0);
				openedDoor2.setRotation(3);
			}

			if (World.removeTemporaryObject(object, 60000, true)
					&& World.removeTemporaryObject(otherDoor, 60000, true)) {
				player.faceObject(openedDoor1);
				World.spawnTemporaryObject(openedDoor1, 60000, true);
				World.spawnTemporaryObject(openedDoor2, 60000, true);
				return true;
			}
		} else if (object.getRotation() == 2) {

			boolean south = true;
			WorldObject otherDoor = World.getObject(new WorldTile(
					object.getX(), object.getY() + 1, object.getPlane()),
					object.getType());
			if (otherDoor == null
					|| otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object
							.getDefinitions().name)) {
				otherDoor = World.getObject(
						new WorldTile(object.getX(), object.getY() - 1, object
								.getPlane()), object.getType());
				if (otherDoor == null
						|| otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name
								.equalsIgnoreCase(object.getDefinitions().name))
					return false;
				south = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(),
					object.getType(), object.getRotation() + 1, object.getX(),
					object.getY(), object.getPlane());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(),
					otherDoor.getType(), otherDoor.getRotation() + 1,
					otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
			if (south) {
				openedDoor1.moveLocation(1, 0, 0);
				openedDoor2.setRotation(1);
				openedDoor2.moveLocation(1, 0, 0);
			} else {
				openedDoor1.moveLocation(1, 0, 0);
				openedDoor1.setRotation(1);
				openedDoor2.moveLocation(1, 0, 0);
			}
			if (World.removeTemporaryObject(object, 60000, true)
					&& World.removeTemporaryObject(otherDoor, 60000, true)) {
				player.faceObject(openedDoor1);
				World.spawnTemporaryObject(openedDoor1, 60000, true);
				World.spawnTemporaryObject(openedDoor2, 60000, true);
				return true;
			}
		} else if (object.getRotation() == 3) {

			boolean right = true;
			WorldObject otherDoor = World.getObject(new WorldTile(
					object.getX() - 1, object.getY(), object.getPlane()),
					object.getType());
			if (otherDoor == null
					|| otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object
							.getDefinitions().name)) {
				otherDoor = World.getObject(new WorldTile(object.getX() + 1,
						object.getY(), object.getPlane()), object.getType());
				if (otherDoor == null
						|| otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name
								.equalsIgnoreCase(object.getDefinitions().name))
					return false;
				right = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(),
					object.getType(), object.getRotation() + 1, object.getX(),
					object.getY(), object.getPlane());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(),
					otherDoor.getType(), otherDoor.getRotation() + 1,
					otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
			if (right) {
				openedDoor1.moveLocation(0, -1, 0);
				openedDoor2.setRotation(0);
				openedDoor1.setRotation(2);
				openedDoor2.moveLocation(0, -1, 0);
			} else {
				openedDoor1.moveLocation(0, -1, 0);
				openedDoor1.setRotation(0);
				openedDoor2.setRotation(2);
				openedDoor2.moveLocation(0, -1, 0);
			}
			if (World.removeTemporaryObject(object, 60000, true)
					&& World.removeTemporaryObject(otherDoor, 60000, true)) {
				player.faceObject(openedDoor1);
				World.spawnTemporaryObject(openedDoor1, 60000, true);
				World.spawnTemporaryObject(openedDoor2, 60000, true);
				return true;
			}
		} else if (object.getRotation() == 1) {

			boolean right = true;
			WorldObject otherDoor = World.getObject(new WorldTile(
					object.getX() - 1, object.getY(), object.getPlane()),
					object.getType());
			if (otherDoor == null
					|| otherDoor.getRotation() != object.getRotation()
					|| otherDoor.getType() != object.getType()
					|| !otherDoor.getDefinitions().name.equalsIgnoreCase(object
							.getDefinitions().name)) {
				otherDoor = World.getObject(new WorldTile(object.getX() + 1,
						object.getY(), object.getPlane()), object.getType());
				if (otherDoor == null
						|| otherDoor.getRotation() != object.getRotation()
						|| otherDoor.getType() != object.getType()
						|| !otherDoor.getDefinitions().name
								.equalsIgnoreCase(object.getDefinitions().name))
					return false;
				right = false;
			}
			WorldObject openedDoor1 = new WorldObject(object.getId(),
					object.getType(), object.getRotation() + 1, object.getX(),
					object.getY(), object.getPlane());
			WorldObject openedDoor2 = new WorldObject(otherDoor.getId(),
					otherDoor.getType(), otherDoor.getRotation() + 1,
					otherDoor.getX(), otherDoor.getY(), otherDoor.getPlane());
			if (right) {
				openedDoor1.moveLocation(0, 1, 0);
				openedDoor1.setRotation(0);
				openedDoor2.moveLocation(0, 1, 0);
			} else {
				openedDoor1.moveLocation(0, 1, 0);
				openedDoor2.setRotation(0);
				openedDoor2.moveLocation(0, 1, 0);
			}
			if (World.removeTemporaryObject(object, 60000, true)
					&& World.removeTemporaryObject(otherDoor, 60000, true)) {
				player.faceObject(openedDoor1);
				World.spawnTemporaryObject(openedDoor1, 60000, true);
				World.spawnTemporaryObject(openedDoor2, 60000, true);
				return true;
			}
		}
		return false;
	}

	public static boolean handleDoor(Player player, WorldObject object) {
		if (World.isSpawnedObject(object))
			return false;
		WorldObject openedDoor = new WorldObject(object.getId(),
				object.getType(), object.getRotation() + 1, object.getX(),
				object.getY(), object.getPlane());
		if (object.getRotation() == 0)
			openedDoor.moveLocation(-1, 0, 0);
		else if (object.getRotation() == 1)
			openedDoor.moveLocation(0, 1, 0);
		else if (object.getRotation() == 2)
			openedDoor.moveLocation(1, 0, 0);
		else if (object.getRotation() == 3)
			openedDoor.moveLocation(0, -1, 0);
		if (World.removeTemporaryObject(object, 60000, true)) {
			player.faceObject(openedDoor);
			World.spawnTemporaryObject(openedDoor, 60000, true);
			return true;
		}
		return false;
	}

	public static boolean handleStaircases(Player player, WorldObject object,
			int optionId) {
		String option = object.getDefinitions().getOption(optionId);
		if (option.equalsIgnoreCase("Climb-up")) {
			if (player.getPlane() == 3)
				return false;
			player.useStairs(-1, new WorldTile(player.getX(), player.getY(),
					player.getPlane() + 1), 0, 1);
		} else if (option.equalsIgnoreCase("Climb-down")) {
			if (player.getPlane() == 0)
				return false;
			player.useStairs(-1, new WorldTile(player.getX(), player.getY(),
					player.getPlane() - 1), 0, 1);
		} else if (option.equalsIgnoreCase("Climb")) {
			if (player.getPlane() == 3 || player.getPlane() == 0)
				return false;
			player.getDialogueManager().startDialogue(
					"ClimbNoEmoteStairs",
					new WorldTile(player.getX(), player.getY(), player
							.getPlane() + 1),
					new WorldTile(player.getX(), player.getY(), player
							.getPlane() - 1), "Go up the stairs.",
					"Go down the stairs.");
		} else
			return false;
		return false;
	}

	public static boolean handleLadder(Player player, WorldObject object,
			int optionId) {
		String option = object.getDefinitions().getOption(optionId);
		if (option.equalsIgnoreCase("Climb-up")) {
			if (player.getPlane() == 3)
				return false;
			player.useStairs(828, new WorldTile(player.getX(), player.getY(),
					player.getPlane() + 1), 1, 2);
		} else if (option.equalsIgnoreCase("Climb-down")) {
			if (player.getPlane() == 0)
				return false;
			player.useStairs(828, new WorldTile(player.getX(), player.getY(),
					player.getPlane() - 1), 1, 2);
		} else if (option.equalsIgnoreCase("Climb")) {
			if (player.getPlane() == 3 || player.getPlane() == 0)
				return false;
			player.getDialogueManager().startDialogue(
					"ClimbEmoteStairs",
					new WorldTile(player.getX(), player.getY(), player
							.getPlane() + 1),
					new WorldTile(player.getX(), player.getY(), player
							.getPlane() - 1), "Climb up the ladder.",
					"Climb down the ladder.", 828);
		} else
			return false;
		return true;
	}

	public static void handleItemOnObject(final Player player,
			InputStream stream) {
		if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
				|| player.isDead())
			return;
		long currentTime = Utils.currentTimeMillis();
		if (player.getStopDelay() >= currentTime
		// || player.getFreezeDelay() >= currentTime
				|| player.getEmotesManager().getNextEmoteEnd() >= currentTime)
			return;

		@SuppressWarnings("unused")
		final int unknown = stream.readUnsignedByteC();
		final int y = stream.readUnsignedShortLE();
		final int itemSlot = stream.readUnsignedShortLE();
		final int interfaceHash = stream.readIntLE();
		final int interfaceId = interfaceHash >> 16;
		final int itemId = stream.readUnsignedShortLE128();
		final int x = stream.readUnsignedShortLE();
		final int id = stream.readInt();
		final WorldTile tile = new WorldTile(x, y, player.getPlane());
		int regionId = tile.getRegionId();
		if (!player.getMapRegionsIds().contains(regionId))
			return;
		WorldObject mapObject = World.getRegion(regionId).getObject(id, tile);
		if (mapObject == null || mapObject.getId() != id)
			return;
		final WorldObject object = !player.isAtDynamicRegion() ? mapObject
				: new WorldObject(id, mapObject.getType(),
						mapObject.getRotation(), x, y, player.getPlane());
		final Item item = player.getInventory().getItem(itemSlot);
		if (player.isDead()
				|| Utils.getInterfaceDefinitionsSize() <= interfaceId)
			return;
		if (player.getStopDelay() > Utils.currentTimeMillis())
			return;
		if (!player.getInterfaceManager().containsInterface(interfaceId))
			return;
		if (item == null || item.getId() != itemId)
			return;
		player.stopAll(false); // false
		final ObjectDefinitions objectDef = object.getDefinitions();
		/*
		 * // now walk done serversided for this
		 * player.addWalkStepsInteract(object
		 * .getCoordFaceX(objectDef.getSizeX()),
		 * object.getCoordFaceY(objectDef.getSizeY()), -1, objectDef.getSizeX(),
		 * objectDef.getSizeY(), true);
		 */
		player.setCoordsEvent(new CoordsEvent(tile, new Runnable() {
			@Override
			public void run() {
				player.setNextFaceWorldTile(new WorldTile(object.getCoordFaceX(
						objectDef.getSizeX(), objectDef.getSizeY(),
						object.getRotation()), object.getCoordFaceY(
						objectDef.getSizeX(), objectDef.getSizeY(),
						object.getRotation()), object.getPlane()));
				if (interfaceId == Inventory.INVENTORY_INTERFACE) { // inventory
					if (object.getDefinitions().name.equals("Anvil")) {
						player.getTemporaryAttributtes()
								.put("itemUsed", itemId);
						ForgingBar bar = ForgingBar.forId(itemId);
						if (bar != null)
							ForgingInterface.sendSmithingInterface(player);
					} else if (itemId == 1438 && object.getId() == 2452) {
						Runecrafting.enterAirAltar(player);
					} else if (itemId == 1440 && object.getId() == 2455) {
						Runecrafting.enterEarthAltar(player);
					} else if (itemId == 1442 && object.getId() == 2456) {
						Runecrafting.enterFireAltar(player);
					} else if (itemId == 1444 && object.getId() == 2454) {
						Runecrafting.enterWaterAltar(player);
					} else if (itemId == 1446 && object.getId() == 2457) {
						Runecrafting.enterBodyAltar(player);
					} else if (itemId == 1448 && object.getId() == 2453) {
						Runecrafting.enterMindAltar(player);
					} else if (itemId == 536
							&& object.getDefinitions().name.equals("Altar")) {
						player.getPackets().sendGameMessage(
								"You pray at the gods");
						player.getInventory().deleteItem(new Item(536, 1));
						player.getSkills().addXp(Skills.PRAYER, 650);
						player.getPackets().sendSound(2738, 0, 1);
						player.setNextAnimation(new Animation(896));
						player.setNextGraphics(new Graphics(624));
						player.getInventory().refresh();
					} else if (object.getId() == 733 || object.getId() == 64729) {
						player.setNextAnimation(new Animation(PlayerCombat
								.getWeaponAttackEmote(-1, 0)));
						slashWeb(player, object);
					} else if (objectDef.name.toLowerCase().contains("range")
							|| objectDef.name.toLowerCase().contains("stove")
							|| id == 2732) {
						Cookables cook = Cooking.isCookingSkill(item);
						if (cook != null) {
							player.getDialogueManager().startDialogue(
									"CookingD", cook, object);
						}
					} else {
						if (Settings.DEBUG)
							System.out.println("item on object: " + id);
					}
				}
			}
		}, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
	}

}