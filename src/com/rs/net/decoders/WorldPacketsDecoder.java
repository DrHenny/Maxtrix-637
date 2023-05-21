package com.rs.net.decoders;

import com.rs.Settings;
import com.rs.game.Animation;
import com.rs.game.Graphics;
import com.rs.game.World;
import com.rs.game.WorldTile;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.npc.familiar.Familiar.SpecialAttack;
import com.rs.game.player.ClanChat;
import com.rs.game.player.CoordsEvent;
import com.rs.game.player.Inventory;
import com.rs.game.player.LogicPacket;
import com.rs.game.player.Player;
import com.rs.game.player.PublicChatMessage;
import com.rs.game.player.QuickChatMessage;
import com.rs.game.player.Skills;
import com.rs.game.player.actions.PlayerCombat;
import com.rs.game.player.actions.PlayerFollow;
import com.rs.game.player.content.Commands;
import com.rs.game.player.content.FriendChatsManager;
import com.rs.game.player.content.Magic;
import com.rs.game.player.content.SkillCapeCustomizer;
import com.rs.game.player.content.Trade;
import com.rs.game.tasks.WorldTask;
import com.rs.game.tasks.WorldTasksManager;
import com.rs.io.InputStream;
import com.rs.net.Session;
import com.rs.net.decoders.handlers.ButtonHandler;
import com.rs.net.decoders.handlers.InventoryOptionsHandler;
import com.rs.net.decoders.handlers.NPCHandler;
import com.rs.net.decoders.handlers.ObjectHandler;
import com.rs.utils.ItemExamines;
import com.rs.utils.Logger;
import com.rs.utils.Utils;
import com.rs.utils.huffman.Huffman;

public final class WorldPacketsDecoder extends Decoder {

	private static final byte[] PACKET_SIZES = new byte[256];
	private final static int ACCEPT_TRADE_CHAT_PACKET = 46;
	private final static int PLAYER_TRADE_OPTION_PACKET = 77;
	private final static int WALKING_PACKET = 12;
	private final static int MINI_WALKING_PACKET = 83;
	// private final static int AFK_PACKET = 85;
	public final static int ACTION_BUTTON1_PACKET = 61;
	public final static int ACTION_BUTTON2_PACKET = 64;
	public final static int ACTION_BUTTON3_PACKET = 4;
	public final static int ACTION_BUTTON4_PACKET = 52;
	public final static int ACTION_BUTTON5_PACKET = 81;
	public final static int ACTION_BUTTON6_PACKET = 18;
	public final static int ACTION_BUTTON7_PACKET = 10;
	public final static int ACTION_BUTTON8_PACKET = 25;
	public final static int ACTION_BUTTON9_PACKET = 91;
	public final static int ACTION_BUTTON10_PACKET = 20;
	public final static int RECEIVE_PACKET_COUNT_PACKET = 15;
	private final static int MAGIC_ON_ITEM_PACKET = -1;
	private final static int MOVE_CAMERA_PACKET = 5;
	private final static int MAGIC_ON_GROUND_PACKET = -1;
	private final static int CLICK_PACKET = 84;
	private final static int MOUVE_MOUSE_PACKET = 29;
	private final static int KEY_TYPED_PACKET = 68;
	private final static int CLOSE_INTERFACE_PACKET = 56;
	private final static int COMMANDS_PACKET = 70;
	private final static int ITEM_ON_ITEM_PACKET = 73;
	private final static int IN_OUT_SCREEN_PACKET = 75;
	private final static int SWITCH_DETAIL = 4;
	private final static int DONE_LOADING_REGION = 33;
	private final static int PING_PACKET = 16;
	private final static int SCREEN_PACKET = 87;
	private final static int CHAT_TYPE_PACKET = 23;
	private final static int CHAT_PACKET = 36;
	private final static int PUBLIC_QUICK_CHAT_PACKET = 30;
	private final static int ADD_FRIEND_PACKET = 51;
	private final static int JOIN_FRIEND_CHAT_PACKET = 1;
	private final static int CHANGE_FRIEND_CHAT_PACKET = 41;
	private final static int KICK_FRIEND_CHAT_PACKET = 32;
	private final static int REMOVE_FRIEND_PACKET = 8;
	private final static int SEND_FRIEND_MESSAGE_PACKET = 72;
	private final static int SEND_FRIEND_QUICK_CHAT_PACKET = 79;
	private final static int OBJECT_CLICK1_PACKET = 11;
	private final static int OBJECT_CLICK2_PACKET = 2;
	private final static int OBJECT_CLICK3_PACKET = 76;
	private final static int OBJECT_EXAMINE_PACKET = 47;
	private final static int NPC_CLICK1_PACKET = 9;
	private final static int NPC_CLICK2_PACKET = 31;
	private final static int ATTACK_NPC = 66;
	private final static int PLAYER_OPTION_1_PACKET = 14;
	private final static int PLAYER_OPTION_2_PACKET = 53;
	private final static int ITEM_TAKE_PACKET = 24;
	private final static int DIALOGUE_CONTINUE_PACKET = 54;
	private final static int ENTER_INTEGER_PACKET = 3;
	private final static int ENTER_STRING_PACKET = 59;
	private final static int SWITCH_INTERFACE_ITEM_PACKET = 26;
	private final static int INTERFACE_ON_PLAYER = 40;
	private final static int INTERFACE_ON_NPC = 65;
	private final static int ITEM_ON_OBJECT_PACKET = 42;
	private final static int NPC_EXAMINE_PACKET = 92;
	private final static int ITEM_EXAMINE_PACKET = 27;
	private final static int COLOR_ID_PACKET = 22;
	// private final static int AFK_CLIENT_PACKET = 93;
	private final static int CLAN_NAME_PACKET = 7;
	private final static int CLAN_FORUM_THREAD_PACKET = 74;
	static {
		loadPacketSizes();
	}

	public static void loadPacketSizes() {
		for (int id = 0; id < 256; id++)
			PACKET_SIZES[id] = -4;
		PACKET_SIZES[64] = 8;
		PACKET_SIZES[18] = 8;
		PACKET_SIZES[25] = 8;
		PACKET_SIZES[41] = -1;
		PACKET_SIZES[14] = 3;
		PACKET_SIZES[46] = 3;
		PACKET_SIZES[87] = 6;
		PACKET_SIZES[47] = 9;
		PACKET_SIZES[57] = 3;
		PACKET_SIZES[67] = 3;
		PACKET_SIZES[91] = 8;
		PACKET_SIZES[24] = 7;
		PACKET_SIZES[73] = 16;
		PACKET_SIZES[40] = 11;
		PACKET_SIZES[36] = -1;
		PACKET_SIZES[74] = -1;
		PACKET_SIZES[31] = 3;
		PACKET_SIZES[54] = 6;
		PACKET_SIZES[12] = -1;
		PACKET_SIZES[23] = 1;
		PACKET_SIZES[9] = 3;
		PACKET_SIZES[17] = -1;
		PACKET_SIZES[44] = -1;
		PACKET_SIZES[88] = -1;
		PACKET_SIZES[42] = 17;
		PACKET_SIZES[49] = 3;
		PACKET_SIZES[21] = 15;
		PACKET_SIZES[59] = -1;
		PACKET_SIZES[37] = -1;
		PACKET_SIZES[6] = 8;
		PACKET_SIZES[55] = 7;
		PACKET_SIZES[69] = 9;
		PACKET_SIZES[26] = 16;
		PACKET_SIZES[39] = 12;
		PACKET_SIZES[71] = 4;
		PACKET_SIZES[22] = 2;
		PACKET_SIZES[32] = -1;
		PACKET_SIZES[79] = -1;
		PACKET_SIZES[89] = 4;
		PACKET_SIZES[90] = -1;
		PACKET_SIZES[15] = 4;
		PACKET_SIZES[72] = -2;
		PACKET_SIZES[20] = 8;
		PACKET_SIZES[92] = 3;
		PACKET_SIZES[82] = 3;
		PACKET_SIZES[28] = 3;
		PACKET_SIZES[81] = 8;
		PACKET_SIZES[7] = -1;
		PACKET_SIZES[4] = 8;
		PACKET_SIZES[60] = -1;
		PACKET_SIZES[13] = 2;
		PACKET_SIZES[52] = 8;
		PACKET_SIZES[65] = 11;
		PACKET_SIZES[85] = 2;
		PACKET_SIZES[86] = 7;
		PACKET_SIZES[78] = -1;
		PACKET_SIZES[83] = -1;
		PACKET_SIZES[27] = 7;
		PACKET_SIZES[2] = 9;
		PACKET_SIZES[93] = 1;
		PACKET_SIZES[70] = -1;
		PACKET_SIZES[1] = -1;
		PACKET_SIZES[8] = -1;
		PACKET_SIZES[11] = 9;
		PACKET_SIZES[0] = 9;
		PACKET_SIZES[51] = -1;
		PACKET_SIZES[5] = 4;
		PACKET_SIZES[45] = 7;
		PACKET_SIZES[75] = 4;
		PACKET_SIZES[53] = 3;
		PACKET_SIZES[33] = 0;
		PACKET_SIZES[50] = 3;
		PACKET_SIZES[76] = 9;
		PACKET_SIZES[80] = -1;
		PACKET_SIZES[77] = 3;
		PACKET_SIZES[68] = -1;
		PACKET_SIZES[43] = 3;
		PACKET_SIZES[30] = -1;
		PACKET_SIZES[19] = 3;
		PACKET_SIZES[16] = 0;
		PACKET_SIZES[34] = 4;
		PACKET_SIZES[48] = 0;
		PACKET_SIZES[56] = 0;
		PACKET_SIZES[58] = 2;
		PACKET_SIZES[10] = 8;
		PACKET_SIZES[35] = 7;
		PACKET_SIZES[84] = 6;
		PACKET_SIZES[66] = 3;
		PACKET_SIZES[61] = 8;
		PACKET_SIZES[29] = -1;
		PACKET_SIZES[62] = 3;
		PACKET_SIZES[3] = 4;
		PACKET_SIZES[63] = 4;
		PACKET_SIZES[73] = 16;
		PACKET_SIZES[38] = -1;
	}

	private Player player;
	private int chatType;

	public WorldPacketsDecoder(Session session, Player player) {
		super(session);
		this.player = player;
	}

	@Override
	public void decode(InputStream stream) {
		while (stream.getRemaining() > 0 && session.getChannel().isConnected() && !player.hasFinished()) {
			int packetId = stream.readUnsignedByte();
			if (packetId >= PACKET_SIZES.length) {
				System.out.println("PacketId " + packetId + " has fake packet id.");
				break;
			}
			int length = PACKET_SIZES[packetId];
			if (length == -1)
				length = stream.readUnsignedByte();
			else if (length == -2)
				length = stream.readUnsignedShort();
			else if (length == -3)
				length = stream.readInt();
			else if (length == -4) {
				length = stream.getRemaining();
				System.out.println("Invalid size for PacketId " + packetId + ". Size guessed to be " + length);
			}
			if (length > stream.getRemaining()) {
				length = stream.getRemaining();
				System.out.println("PacketId " + packetId + " has fake size. - expected size " + length);
				// break;

			}

			System.out.println("PacketId " + packetId + " has . - expected size " + length);

			int startOffset = stream.getOffset();
			processPackets(packetId, stream, length);
			stream.setOffset(startOffset + length);
		}
	}

	public static void decodeLogicPacket(final Player player, LogicPacket packet) {
		int packetId = packet.getId();
		// System.out.println("Packet: "+packetId);
		InputStream stream = new InputStream(packet.getData());
		if (packetId == WALKING_PACKET || packetId == MINI_WALKING_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			long currentTime = Utils.currentTimeMillis();
			if (player.getStopDelay() > currentTime)
				return;
			if (player.getFreezeDelay() >= currentTime) {
				player.getPackets().sendGameMessage("A magical force prevents you from moving.");
				return;
			}
			int length = stream.getLength();

			if (packetId == MINI_WALKING_PACKET)
				length -= 13;
			int baseX = stream.readUnsignedShortLE128();
			int baseY = stream.readUnsignedShortLE128();
			stream.readByte();
			int steps = (length - 5) / 2;
			if (steps > 25)
				steps = 25;
			player.stopAll();
			for (int step = 0; step < steps; step++)
				// dynamic part temporary fix
				if (!player.addWalkSteps(baseX + stream.readUnsignedByte(), baseY + stream.readUnsignedByte(), -1, !player.isAtDynamicRegion()))
					break;
		} else if (packetId == PLAYER_OPTION_2_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			@SuppressWarnings("unused")
			boolean unknown = stream.readByte() == 1;
			int playerIndex = stream.readUnsignedShort();
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished() || !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.getStopDelay() > Utils.currentTimeMillis())
				return;
			player.stopAll(false);
			player.getActionManager().setSkill(new PlayerFollow(p2));
		} else if (packetId == PLAYER_OPTION_1_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			@SuppressWarnings("unused")
			boolean unknown = stream.readByte() == 1;
			int playerIndex = stream.readUnsignedShort();
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished() || !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.getStopDelay() > Utils.currentTimeMillis() || !player.getControlerManager().canPlayerOption1(p2))
				return;
			if (!player.isCanPvp())
				return;
			if (!player.getControlerManager().canAttack(p2))
				return;

			if (!player.isCanPvp() || !p2.isCanPvp()) {
				player.getPackets().sendGameMessage("You can only attack players in a player-vs-player area.");
				return;
			}
			if (!p2.isAtMultiArea() || !player.isAtMultiArea()) {
				if (player.getAttackedBy() != p2 && player.getAttackedByDelay() > Utils.currentTimeMillis()) {
					player.getPackets().sendGameMessage("You are already in combat.");
					return;
				}
				if (p2.getAttackedBy() != player && p2.getAttackedByDelay() > Utils.currentTimeMillis()) {
					if (p2.getAttackedBy() instanceof NPC) {
						p2.setAttackedBy(player); // changes enemy to player,
						// player has priority over
						// npc on single areas
					} else {
						player.getPackets().sendGameMessage("That player is already in combat.");
						return;
					}
				}
			}
			player.stopAll(false);
			player.getActionManager().setSkill(new PlayerCombat(p2));

		} else if (packetId == ATTACK_NPC) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			if (player.getStopDelay() > Utils.currentTimeMillis())
				return;
			@SuppressWarnings("unused")
			boolean unknown = stream.readByte128() == 1;
			int npcIndex = stream.readUnsignedShort128();
			NPC npc = World.getNPCs().get(npcIndex);
			System.out.println("" + npc.getId());
			if (npc == null || npc.isDead() || npc.hasFinished() || !player.getMapRegionsIds().contains(npc.getRegionId()) || !npc.getDefinitions().hasAttackOption())
				return;
			if (!player.getControlerManager().canAttack(npc))
				return;
			if (npc instanceof Familiar) {
				Familiar familiar = (Familiar) npc;
				if (familiar == player.getFamiliar()) {
					player.getPackets().sendGameMessage("You can't attack your own familiar.");
					return;
				}
				if (!familiar.canAttack(player)) {
					player.getPackets().sendGameMessage("You can't attack this npc.");
					return;
				}
			} else if (!npc.isForceMultiAttacked()) {
				if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
					if (player.getAttackedBy() != npc && player.getAttackedByDelay() > Utils.currentTimeMillis()) {
						player.getPackets().sendGameMessage("You are already in combat.");
						return;
					}
					if (npc.getAttackedBy() != player && npc.getAttackedByDelay() > Utils.currentTimeMillis()) {
						player.getPackets().sendGameMessage("This npc is already in combat.");
						return;
					}
				}
			}
			player.stopAll(false);
			player.getActionManager().setSkill(new PlayerCombat(npc));
		} else if (packetId == INTERFACE_ON_PLAYER) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			if (player.getStopDelay() > Utils.currentTimeMillis())
				return;
			int playerIndex = stream.readUnsignedShortLE();
			int interfaceHash = stream.readIntLE();
			@SuppressWarnings("unused")
			int junk1 = stream.readUnsignedShort();
			@SuppressWarnings("unused")
			boolean unknown = stream.read128Byte() == 1;
			@SuppressWarnings("unused")
			int junk2 = stream.readUnsignedShortLE128();
			int interfaceId = interfaceHash >> 16;
			int componentId = interfaceHash - (interfaceId << 16);
			if (Utils.getInterfaceDefinitionsSize() <= interfaceId)
				return;
			if (!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			if (componentId == 65535)
				componentId = -1;
			if (componentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId)
				return;
			Player p2 = World.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished() || !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			player.stopAll(false);
			switch (interfaceId) {
			case 662:
			case 747:
				if (player.getFamiliar() == null)
					return;
				player.resetWalkSteps();
				if ((interfaceId == 747 && componentId == 14) || (interfaceId == 662 && componentId == 65) || (interfaceId == 662 && componentId == 74) || interfaceId == 747 && componentId == 17) {
					if ((interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 23 || interfaceId == 747 && componentId == 17)) {
						if (player.getFamiliar().getSpecialAttack() != SpecialAttack.ENTITY)
							return;
					}
					if (!player.isCanPvp() || !p2.isCanPvp()) {
						player.getPackets().sendGameMessage("You can only attack players in a player-vs-player area.");
						return;
					}
					if (!player.getFamiliar().canAttack(p2)) {
						player.getPackets().sendGameMessage("You can only use your familiar in a multi-zone area.");
						return;
					} else {
						player.getFamiliar().setSpecial(interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 17);
						player.getFamiliar().setTarget(p2);
					}
				}
				break;
			case 193:
				switch (componentId) {
				case 28:
				case 32:
				case 24:
				case 20:
				case 30:
				case 34:
				case 26:
				case 22:
				case 29:
				case 33:
				case 25:
				case 21:
				case 31:
				case 35:
				case 27:
				case 23:
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFaceWorldTile(new WorldTile(p2.getCoordFaceX(p2.getSize()), p2.getCoordFaceY(p2.getSize()), p2.getPlane()));
						if (!player.getControlerManager().canAttack(p2))
							return;
						if (!player.isCanPvp() || !p2.isCanPvp()) {
							player.getPackets().sendGameMessage("You can only attack players in a player-vs-player area.");
							return;
						}
						if (!p2.isAtMultiArea() || !player.isAtMultiArea()) {
							if (player.getAttackedBy() != p2 && player.getAttackedByDelay() > Utils.currentTimeMillis()) {
								player.getPackets().sendGameMessage("That " + (player.getAttackedBy() instanceof Player ? "player" : "npc") + " is already in combat.");
								return;
							}
							if (p2.getAttackedBy() != player && p2.getAttackedByDelay() > Utils.currentTimeMillis()) {
								if (p2.getAttackedBy() instanceof NPC) {
									p2.setAttackedBy(player); // changes enemy
									// to player,
									// player has
									// priority over
									// npc on single
									// areas
								} else {
									player.getPackets().sendGameMessage("That player is already in combat.");
									return;
								}
							}
						}
						player.getActionManager().setSkill(new PlayerCombat(p2));
					}
					break;
				}
			case 192:
				switch (componentId) {
				case 25: // air strike
				case 28: // water strike
				case 30: // earth strike
				case 32: // fire strike
				case 34: // air bolt
				case 39: // water bolt
				case 42: // earth bolt
				case 45: // fire bolt
				case 49: // air blast
				case 52: // water blast
				case 58: // earth blast
				case 63: // fire blast
				case 70: // air wave
				case 73: // water wave
				case 77: // earth wave
				case 80: // fire wave
				case 86: // teleblock
				case 84: // air surge
				case 87: // water surge
				case 89: // earth surge
				case 91: // fire surge
				case 99: // storm of armadyl
				case 36: // bind
				case 66: // Sara Strike
				case 67: // Guthix Claws
				case 68: // Flame of Zammy
				case 55: // snare
				case 81: // entangle
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFaceWorldTile(new WorldTile(p2.getCoordFaceX(p2.getSize()), p2.getCoordFaceY(p2.getSize()), p2.getPlane()));
						if (!player.getControlerManager().canAttack(p2))
							return;
						if (!player.isCanPvp() || !p2.isCanPvp()) {
							player.getPackets().sendGameMessage("You can only attack players in a player-vs-player area.");
							return;
						}
						if (!p2.isAtMultiArea() || !player.isAtMultiArea()) {
							if (player.getAttackedBy() != p2 && player.getAttackedByDelay() > Utils.currentTimeMillis()) {
								player.getPackets().sendGameMessage("That " + (player.getAttackedBy() instanceof Player ? "player" : "npc") + " is already in combat.");
								return;
							}
							if (p2.getAttackedBy() != player && p2.getAttackedByDelay() > Utils.currentTimeMillis()) {
								if (p2.getAttackedBy() instanceof NPC) {
									p2.setAttackedBy(player); // changes enemy
									// to player,
									// player has
									// priority over
									// npc on single
									// areas
								} else {
									player.getPackets().sendGameMessage("That player is already in combat.");
									return;
								}
							}
						}
						player.getActionManager().setSkill(new PlayerCombat(p2));
					}
					break;
				}
				break;
			}
			System.out.println("Spell:" + componentId);
		} else if (packetId == INTERFACE_ON_NPC) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			if (player.getStopDelay() > Utils.currentTimeMillis())
				return;
			int slot = stream.readUnsignedShortLE128();
			if (slot == 65535)
				return;
			@SuppressWarnings("unused")
			int junk2 = stream.readUnsignedShortLE();
			int npcIndex = stream.readUnsignedShortLE();
			int interfaceHash = stream.readIntV2();
			@SuppressWarnings("unused")
			boolean unknown = stream.readByte() == 1;
			int interfaceId = interfaceHash >> 16;
			int componentId = interfaceHash - (interfaceId << 16);

			if (Utils.getInterfaceDefinitionsSize() <= interfaceId)
				return;
			if (!player.getInterfaceManager().containsInterface(interfaceId))
				return;
			if (componentId == 65535)
				componentId = -1;
			if (componentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId)
				return;
			NPC npc = World.getNPCs().get(npcIndex);
			if (npc == null || npc.isDead() || npc.hasFinished() || !player.getMapRegionsIds().contains(npc.getRegionId()))
				return;
			if (!npc.getDefinitions().hasAttackOption()) {
				player.getPackets().sendGameMessage("You can't attack this npc.");
				return;
			}
			player.stopAll(false);
			switch (interfaceId) {
			case Inventory.INVENTORY_INTERFACE:
				Item item = player.getInventory().getItem(slot);// construct
																// only if
																// needed
				if (item == null)
					return;
				if (!player.getInventory().containsItem(item.getId(), item.getAmount()))
					return;
				if (!player.getControlerManager().processItemOnNPC(npc, item))
					return;
				// InventoryOptionsHandler.handleItemOnNPC(npc, item);
				break;
			case 662:
			case 747:
				if (player.getFamiliar() == null)
					return;
				player.resetWalkSteps();
				if ((interfaceId == 747 && componentId == 14) || (interfaceId == 662 && componentId == 65) || (interfaceId == 662 && componentId == 74) || interfaceId == 747 && componentId == 17 || interfaceId == 747 && componentId == 23) {
					if ((interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 17)) {
						if (player.getFamiliar().getSpecialAttack() != SpecialAttack.ENTITY)
							return;
					}
					if (npc == player.getFamiliar()) {
						player.getPackets().sendGameMessage("You can't attack your own familiar.");
						return;
					}
					if (!player.getFamiliar().canAttack(npc)) {
						player.getPackets().sendGameMessage("You can only use your familiar in a multi-zone area.");
						return;
					} else {
						player.getFamiliar().setSpecial(interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 17);
						player.getFamiliar().setTarget(npc);
					}
				}
				break;
			case 193:
				switch (componentId) {
				case 28:
				case 32:
				case 24:
				case 20:
				case 30:
				case 34:
				case 26:
				case 22:
				case 29:
				case 33:
				case 25:
				case 21:
				case 31:
				case 35:
				case 27:
				case 23:
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFaceWorldTile(new WorldTile(npc.getCoordFaceX(npc.getSize()), npc.getCoordFaceY(npc.getSize()), npc.getPlane()));
						if (!player.getControlerManager().canAttack(npc))
							return;
						if (npc instanceof Familiar) {
							Familiar familiar = (Familiar) npc;
							if (familiar == player.getFamiliar()) {
								player.getPackets().sendGameMessage("You can't attack your own familiar.");
								return;
							}
							if (!familiar.canAttack(player)) {
								player.getPackets().sendGameMessage("You can't attack this npc.");
								return;
							}
						} else if (!npc.isForceMultiAttacked()) {
							if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
								if (player.getAttackedBy() != npc && player.getAttackedByDelay() > Utils.currentTimeMillis()) {
									player.getPackets().sendGameMessage("You are already in combat.");
									return;
								}
								if (npc.getAttackedBy() != player && npc.getAttackedByDelay() > Utils.currentTimeMillis()) {
									player.getPackets().sendGameMessage("This npc is already in combat.");
									return;
								}
							}
						}
						player.getActionManager().setSkill(new PlayerCombat(npc));
					}
					break;
				}
			case 192:
				switch (componentId) {
				case 25: // air strike
				case 28: // water strike
				case 30: // earth strike
				case 32: // fire strike
				case 34: // air bolt
				case 39: // water bolt
				case 42: // earth bolt
				case 45: // fire bolt
				case 49: // air blast
				case 52: // water blast
				case 58: // earth blast
				case 63: // fire blast
				case 70: // air wave
				case 73: // water wave
				case 77: // earth wave
				case 80: // fire wave
				case 84: // air surge
				case 87: // water surge
				case 89: // earth surge
				case 66: // Sara Strike
				case 67: // Guthix Claws
				case 68: // Flame of Zammy
				case 93:
				case 91: // fire surge
				case 99: // storm of Armadyl
				case 36: // bind
				case 55: // snare
				case 81: // entangle
					if (Magic.checkCombatSpell(player, componentId, 1, false)) {
						player.setNextFaceWorldTile(new WorldTile(npc.getCoordFaceX(npc.getSize()), npc.getCoordFaceY(npc.getSize()), npc.getPlane()));
						if (!player.getControlerManager().canAttack(npc))
							return;
						if (npc instanceof Familiar) {
							Familiar familiar = (Familiar) npc;
							if (familiar == player.getFamiliar()) {
								player.getPackets().sendGameMessage("You can't attack your own familiar.");
								return;
							}
							if (!familiar.canAttack(player)) {
								player.getPackets().sendGameMessage("You can't attack this npc.");
								return;
							}
						} else if (!npc.isForceMultiAttacked()) {
							if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
								if (player.getAttackedBy() != npc && player.getAttackedByDelay() > Utils.currentTimeMillis()) {
									player.getPackets().sendGameMessage("You are already in combat.");
									return;
								}
								if (npc.getAttackedBy() != player && npc.getAttackedByDelay() > Utils.currentTimeMillis()) {
									player.getPackets().sendGameMessage("This npc is already in combat.");
									return;
								}
							}
						}
						player.getActionManager().setSkill(new PlayerCombat(npc));
					}
					break;
				}
				break;
			}
			System.out.println("Spell:" + componentId);
		} else if (packetId == NPC_CLICK1_PACKET) {
			NPCHandler.handleOption1(player, stream);
		} else if (packetId == NPC_CLICK2_PACKET) {
			NPCHandler.handleOption2(player, stream);
		} else if (packetId == OBJECT_CLICK1_PACKET) {
			ObjectHandler.handleOption1(player, stream);
		} else if (packetId == OBJECT_CLICK2_PACKET) {
			ObjectHandler.handleOption2(player, stream);
		} else if (packetId == OBJECT_CLICK3_PACKET) {
			ObjectHandler.handleOption3(player, stream);
		} else if (packetId == ITEM_TAKE_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead())
				return;
			long currentTime = Utils.currentTimeMillis();
			if (player.getStopDelay() > currentTime)
				// || player.getFreezeDelay() >= currentTime)
				return;
			final int id = stream.readUnsignedShort128();
			@SuppressWarnings("unused")
			boolean unknown = stream.readByte() == 1;
			int y = stream.readUnsignedShort();
			int x = stream.readUnsignedShortLE();
			final WorldTile tile = new WorldTile(x, y, player.getPlane());
			final int regionId = tile.getRegionId();
			if (!player.getMapRegionsIds().contains(regionId))
				return;
			final FloorItem item = World.getRegion(regionId).getGroundItem(id, tile, player);
			if (item == null)
				return;
			player.stopAll(false);
			player.setCoordsEvent(new CoordsEvent(tile, new Runnable() {
				@Override
				public void run() {
					final FloorItem item = World.getRegion(regionId).getGroundItem(id, tile, player);
					if (item == null)
						return;
					player.setNextFaceWorldTile(tile);
					World.removeGroundItem(player, item);
				}
			}, 0));
		} else if (packetId == ITEM_ON_OBJECT_PACKET)
			ObjectHandler.handleItemOnObject(player, stream);
	}

	public void processPackets(final int packetId, InputStream stream, int length) {

		player.setPacketsDecoderPing(Utils.currentTimeMillis());
		if (packetId == PING_PACKET) {
			// kk we ping :)
		} else if (packetId == MOUVE_MOUSE_PACKET) {
			// USELESS PACKET
		} else if (packetId == KEY_TYPED_PACKET) {
			// USELESS PACKET
		} else if (packetId == RECEIVE_PACKET_COUNT_PACKET) {
			// interface packets
			stream.readInt();
		} else if (packetId == ITEM_ON_ITEM_PACKET) {
			InventoryOptionsHandler.handleItemOnItem(player, stream);

		} else if (packetId == PLAYER_TRADE_OPTION_PACKET) {
			long currentTime = System.currentTimeMillis();
			boolean unknown2 = stream.readByte() == 1;
			int playerIndex = stream.readUnsignedShort();
			Player other = (Player) World.getPlayers().get(playerIndex);
			if (player.getTrade() != null || other.getTrade() != null) {
				player.getPackets().sendGameMessage("Your already in a trade!");
				return;
			}
			if (player.getLocation().getX() == other.getLocation().getX() && player.getLocation().getY() == other.getLocation().getY()) {
				player.sendMessage("Cant trade in this position");
				other.sendMessage("Cant trade in this position");
				return;
			}
			if (other.getTemporaryAttributtes().get("didRequestTrade") == Boolean.TRUE && (Integer) other.getTemporaryAttributtes().get("tradeWithIndex") == player.getIndex()) {
				Trade session = new Trade(player, other, player);
				player.setTrade(session);
				other.setTrade(session);
				session.start();
				if (player.getLocation().getX() == other.getLocation().getX() && player.getLocation().getY() == other.getLocation().getY()) {
					player.sendMessage("Cant trade in this position");
					player.getTrade().endSession();

					return;
				}
			} else {
				if (player.getLocation().getX() == other.getLocation().getX() && player.getLocation().getY() == other.getLocation().getY()) {
					player.sendMessage("Cant trade in this position");
					return;
				}
				player.getPackets().sendGameMessage("Sending trade request...");
				other.getPackets().sendTradeRequestMessage(player);
				player.getTemporaryAttributtes().put("didRequestTrade", Boolean.TRUE);
				player.getTemporaryAttributtes().put("tradeWithIndex", other.getIndex());
				player.stopAll(false);

			}

		} else if (packetId == ACCEPT_TRADE_CHAT_PACKET) {
			long currentTime = System.currentTimeMillis();
			boolean unknown2 = stream.readByte() == 1;
			int playerIndex = stream.readUnsignedShort();
			Player other = (Player) World.getPlayers().get(playerIndex);
			if (player.getTrade() != null || other.getTrade() != null) {
				player.getPackets().sendGameMessage("You're already in a trade!");
			}
			if (player.getLocation().getX() == other.getLocation().getX() && player.getLocation().getY() == other.getLocation().getY()) {
				player.sendMessage("Cant trade in this position");
				other.sendMessage("Cant trade in this position");
				return;
			}
			if (other.getTemporaryAttributtes().get("didRequestTrade") == Boolean.TRUE && (Integer) other.getTemporaryAttributtes().get("tradeWithIndex") == player.getIndex()) {
				Trade session = new Trade(player, other, player);
				player.setTrade(session);
				other.setTrade(session);
				session.start();
				if (player.getLocation().getX() == other.getLocation().getX() && player.getLocation().getY() == other.getLocation().getY()) {
					player.sendMessage("Cant trade in this position");
					other.sendMessage("Cant trade in this position");
					player.getTrade().endSession();
					other.getTrade().endSession();
					return;
				}
			} else {

				if (player.getLocation().getX() == other.getLocation().getX() && player.getLocation().getY() == other.getLocation().getY()) {
					player.sendMessage("Cant trade in this position");
					other.sendMessage("Cant trade in this position");
					return;
				}
				player.getPackets().sendGameMessage("Sending trade request...");
				other.getPackets().sendTradeRequestMessage(player);
				player.getTemporaryAttributtes().put("didRequestTrade", Boolean.TRUE);
				player.getTemporaryAttributtes().put("tradeWithIndex", other.getIndex());
			}
		} else if (packetId == MAGIC_ON_GROUND_PACKET) {
			int inventoryInter = stream.readInt() >> 16;
			int itemId = stream.readShort();
			int junk = stream.readShort();
			int itemSlot = stream.readShortLE();
			int interfaceSet = stream.readIntV1();
			int spellId = interfaceSet & 0xFFF;
			int magicInter = interfaceSet >> 16;
			System.out.println("Item:" + itemId + "slot:" + itemSlot + "spell:" + spellId + "i:" + interfaceSet + "l:" + magicInter + "x:" + junk + "k:" + inventoryInter);
		} else if (packetId == MAGIC_ON_ITEM_PACKET) {
			int inventoryInter = stream.readInt() >> 16;
			int itemId = stream.readShort128();
			@SuppressWarnings("unused")
			int junk = stream.readShort();
			@SuppressWarnings("unused")
			int itemSlot = stream.readShortLE();
			int interfaceSet = stream.readIntV1();
			int spellId = interfaceSet & 0xFFF;
			int magicInter = interfaceSet >> 16;
			if (inventoryInter == 149 && magicInter == 192) {
				switch (spellId) {
				case 59:// High Alch
					if (player.getSkills().getLevel(Skills.MAGIC) < 55) {
						player.getPackets().sendGameMessage("You do not have the required level to cast this spell.");
						return;
					}
					if (itemId == 995) {
						player.getPackets().sendGameMessage("You can't alch this!");
						return;
					}
					if (player.getEquipment().getWeaponId() == 1401 || player.getEquipment().getWeaponId() == 3054 || player.getEquipment().getWeaponId() == 19323) {
						if (!player.getInventory().containsItem(561, 1)) {
							player.getPackets().sendGameMessage("You do not have the required runes to cast this spell.");
							return;
						}
						player.setNextAnimation(new Animation(9633));
						player.setNextGraphics(new Graphics(112));
						player.getInventory().deleteItem(561, 1);
						player.getInventory().deleteItem(itemId, 1);
						player.getInventory().addItem(995, new Item(itemId, 1).getDefinitions().getValue(itemId) >> 6);
					} else {
						if (!player.getInventory().containsItem(561, 1) || !player.getInventory().containsItem(554, 5)) {
							player.getPackets().sendGameMessage("You do not have the required runes to cast this spell.");
							return;
						}
						player.setNextAnimation(new Animation(713));
						player.setNextGraphics(new Graphics(113));
						player.getInventory().deleteItem(561, 1);
						player.getInventory().deleteItem(554, 5);
						player.getInventory().deleteItem(itemId, 1);
						player.getInventory().addItem(995, new Item(itemId, 1).getDefinitions().getValue(itemId) >> 6);
					}
					break;
				default:
					System.out.println("Spell:" + spellId + ", Item:" + itemId);
				}
				System.out.println("Spell:" + spellId + ", Item:" + itemId);
			}
			/*
			 * } else if (packetId == AFK_CLIENT_PACKET) {
			 * WorldTasksManager.schedule(new WorldTask() {
			 * 
			 * @Override public void run() {
			 * player.getSession().getChannel().close(); } }, 5, 5);
			 */
			/*
			 * } else if (packetId == AFK_PACKET) { if (packetId == 93)//TODO
			 * WorldTasksManager.schedule(new WorldTask() {
			 * 
			 * @Override public void run() {
			 * player.getSession().getChannel().close(); } }, 5, 5);
			 */

		} else if (packetId == CLOSE_INTERFACE_PACKET) {
			if (!player.isRunning()) {
				player.run();
				return;
			}
			player.stopAll();
		} else if (packetId == MOVE_CAMERA_PACKET) {
			// not using it atm
			stream.readUnsignedShort();
			stream.readUnsignedShort();
		} else if (packetId == IN_OUT_SCREEN_PACKET) {
			// not using this check because not 100% efficient
			@SuppressWarnings("unused")
			boolean inScreen = stream.readByte() == 1;
		} else if (packetId == SCREEN_PACKET) {
			int displayMode = stream.readUnsignedByte();
			player.setScreenWidth(stream.readUnsignedShort());
			player.setScreenHeight(stream.readUnsignedShort());
			@SuppressWarnings("unused")
			boolean switchScreenMode = stream.readUnsignedByte() == 1;
			if (!player.hasStarted() || player.hasFinished() || displayMode == player.getDisplayMode() || !player.getInterfaceManager().containsInterface(742))
				return;
			player.setDisplayMode(displayMode);
			player.getInterfaceManager().removeAll();
			player.getInterfaceManager().sendInterfaces();
			player.getInterfaceManager().sendInterface(742);
		} else if (packetId == CLICK_PACKET) {
			int mouseHash = stream.readShortLE128();
			int mouseButton = mouseHash >> 15;
			int time = mouseHash - (mouseButton << 15); // time
			int positionHash = stream.readIntV1();
			int y = positionHash >> 16; // y;
			int x = positionHash - (y << 16); // x
			@SuppressWarnings("unused")
			boolean clicked;
			// mass click or stupid autoclicker, lets stop lagg
			if (time <= 1 || x < 0 || x > player.getScreenWidth() || y < 0 || y > player.getScreenHeight()) {
				// player.getSession().getChannel().close();
				clicked = false;
				return;
			}
			clicked = true;
		} else if (packetId == DIALOGUE_CONTINUE_PACKET) {
			int interfaceHash = stream.readIntV2();
			@SuppressWarnings("unused")
			int junk = stream.readShortLE128();
			int interfaceId = interfaceHash >> 16;
			@SuppressWarnings("unused")
			int buttonId = (interfaceHash & 0xFF);
			if (Utils.getInterfaceDefinitionsSize() <= interfaceId) {
				// hack, or server error or client error
				// player.getSession().getChannel().close();
				return;
			}
			if (!player.isRunning() || !player.getInterfaceManager().containsInterface(interfaceId))
				return;
			int componentId = interfaceHash - (interfaceId << 16);
			player.getDialogueManager().continueDialogue(interfaceId, componentId);
		} else if (packetId == ACTION_BUTTON1_PACKET || packetId == ACTION_BUTTON2_PACKET || packetId == ACTION_BUTTON4_PACKET || packetId == ACTION_BUTTON5_PACKET || packetId == ACTION_BUTTON6_PACKET || packetId == ACTION_BUTTON7_PACKET || packetId == ACTION_BUTTON8_PACKET || packetId == ACTION_BUTTON3_PACKET || packetId == ACTION_BUTTON9_PACKET || packetId == ACTION_BUTTON10_PACKET) {
			ButtonHandler.handleButtons(player, stream, packetId);
		} else if (packetId == ENTER_STRING_PACKET) {
			if (!player.isRunning() || player.isDead())
				return;
			String value = stream.readString();
			if (player.getInterfaceManager().containsInterface(1108))
				player.getFriendsIgnores().setChatPrefix(value);
		} else if (packetId == ENTER_INTEGER_PACKET) {
			if (!player.isRunning() || player.isDead())
				return;
			int value = stream.readInt();
			if ((player.getInterfaceManager().containsInterface(762) && player.getInterfaceManager().containsInterface(763)) || player.getInterfaceManager().containsInterface(11)) {
				if (value < 0)
					return;
				Integer bank_item_X_Slot = (Integer) player.getTemporaryAttributtes().remove("bank_item_X_Slot");
				if (bank_item_X_Slot == null)
					return;
				if (player.getTemporaryAttributtes().remove("bank_isWithdraw") != null)
					player.getBank().withdrawItem(bank_item_X_Slot, value);
				else
					player.getBank().depositItem(bank_item_X_Slot, value, player.getInterfaceManager().containsInterface(11) ? false : true);
			} else if (player.getInterfaceManager().containsInterface(206) && player.getInterfaceManager().containsInterface(207)) {
				if (value < 0)
					return;
				Integer pc_item_X_Slot = (Integer) player.getTemporaryAttributtes().remove("pc_item_X_Slot");
				if (pc_item_X_Slot == null)
					return;
				if (player.getTemporaryAttributtes().remove("pc_isRemove") != null)
					player.getPriceCheckManager().removeItem(pc_item_X_Slot, value);
				else
					player.getPriceCheckManager().addItem(pc_item_X_Slot, value);
			} else if (player.getInterfaceManager().containsInterface(671) && player.getInterfaceManager().containsInterface(665)) {
				if (player.getFamiliar() == null || player.getFamiliar().getBob() == null)
					return;
				if (value < 0)
					return;
				Integer bob_item_X_Slot = (Integer) player.getTemporaryAttributtes().remove("bob_item_X_Slot");
				if (bob_item_X_Slot == null)
					return;
				if (player.getTemporaryAttributtes().remove("bob_isRemove") != null)
					player.getFamiliar().getBob().removeItem(bob_item_X_Slot, value);
				else
					player.getFamiliar().getBob().addItem(bob_item_X_Slot, value);
			} else if (player.getTemporaryAttributtes().get("skillId") != null) {
				int skillId = (Integer) player.getTemporaryAttributtes().remove("skillId");
				if (skillId == Skills.HITPOINTS && value == 1)
					value = 10;
				else if (value < 1)
					value = 1;
				else if (value > 99)
					value = 99;
				player.getSkills().set(skillId, value);
				player.getSkills().setXp(skillId, Skills.getXPForLevel(value));
				player.getAppearence().generateAppearenceData();
				player.getDialogueManager().finishDialogue();
			} else if (player.getTemporaryAttributtes().get("offerX") != null && player.getInterfaceManager().containsInterface(335) && player.getTrade().getState() == player.getTrade().getState().STATE_ONE) {
				player.getTrade().addItem(player, (Integer) player.getTemporaryAttributtes().get("offerX"), value);
				player.getTemporaryAttributtes().remove("offerX");
			} else if (player.getTemporaryAttributtes().get("removeX") != null && player.getInterfaceManager().containsInterface(335) && player.getTrade().getState() == player.getTrade().getState().STATE_ONE) {
				player.getTrade().removeItem(player, (Integer) player.getTemporaryAttributtes().get("removeX"), value);
				player.getTemporaryAttributtes().remove("removeX");

			}
		} else if (packetId == SWITCH_INTERFACE_ITEM_PACKET) {
			stream.readUnsignedShort();
			int fromSlot = stream.readUnsignedShortLE();
			stream.readUnsignedShort128();
			int interface1Hash = stream.readIntV1();
			int toSlot = stream.readUnsignedShortLE();
			int interface2Hash = stream.readIntV2();

			int fromInterfaceId = interface1Hash >> 16;
			int fromComponentId = interface1Hash - (fromInterfaceId << 16);

			int toInterfaceId = interface2Hash >> 16;
			int toComponentId = interface2Hash - (toInterfaceId << 16);

			if (Utils.getInterfaceDefinitionsSize() <= fromInterfaceId || Utils.getInterfaceDefinitionsSize() <= toInterfaceId)
				return;
			if (!player.getInterfaceManager().containsInterface(fromInterfaceId) || !player.getInterfaceManager().containsInterface(toInterfaceId))
				return;
			if (fromComponentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(fromInterfaceId) <= fromComponentId)
				return;
			if (toComponentId != -1 && Utils.getInterfaceDefinitionsComponentsSize(toInterfaceId) <= toComponentId)
				return;
			if (fromInterfaceId == Inventory.INVENTORY_INTERFACE && fromComponentId == 0 && toInterfaceId == Inventory.INVENTORY_INTERFACE && toComponentId == 0) {
				toSlot -= 28;
				if (toSlot < 0 || toSlot >= player.getInventory().getItemsContainerSize() || fromSlot >= player.getInventory().getItemsContainerSize())
					return;
				player.getInventory().switchItem(fromSlot, toSlot);
			} else if (fromInterfaceId == 763 && fromComponentId == 0 && toInterfaceId == 763 && toComponentId == 0) {
				if (toSlot >= player.getInventory().getItemsContainerSize() || fromSlot >= player.getInventory().getItemsContainerSize())
					return;
				player.getInventory().switchItem(fromSlot, toSlot);
			} else if (fromInterfaceId == 762 && toInterfaceId == 762) {
				player.getBank().switchItem(fromSlot, toSlot, fromComponentId, toComponentId);
			}
			System.out.println("Switch item " + fromInterfaceId + ", " + fromSlot + ", " + toSlot);
		} else if (packetId == SWITCH_DETAIL) {
			int hash = stream.readInt();
			if (hash != 1057001181) {
				// hack, or server error or client error
				player.getSession().getChannel().close();
				return;
			}
			// done loading region when switch detail or mapregion
		} else if (packetId == DONE_LOADING_REGION) {
			/*
			 * if(!player.clientHasLoadedMapRegion()) { //load objects and items
			 * here player.setClientHasLoadedMapRegion(); }
			 * //player.refreshSpawnedObjects(); //player.refreshSpawnedItems();
			 */
		} else if (packetId == WALKING_PACKET || packetId == MINI_WALKING_PACKET || packetId == ITEM_TAKE_PACKET || packetId == PLAYER_OPTION_2_PACKET || packetId == PLAYER_OPTION_1_PACKET || packetId == ATTACK_NPC || packetId == INTERFACE_ON_PLAYER || packetId == INTERFACE_ON_NPC || packetId == NPC_CLICK1_PACKET || packetId == NPC_CLICK2_PACKET || packetId == OBJECT_CLICK1_PACKET || packetId == SWITCH_INTERFACE_ITEM_PACKET || packetId == OBJECT_CLICK2_PACKET || packetId == OBJECT_CLICK3_PACKET || packetId == ITEM_ON_OBJECT_PACKET) {
			player.addLogicPacketToQueue(new LogicPacket(packetId, length, stream));
			// TODO queue
		} else if (packetId == OBJECT_EXAMINE_PACKET) {
			ObjectHandler.handleExamine(player, stream);
		} else if (packetId == NPC_EXAMINE_PACKET) {
			NPCHandler.handleExamine(player, stream);
		} else if (packetId == CLAN_NAME_PACKET) {
		} else if (packetId == CLAN_FORUM_THREAD_PACKET) {
		} else if (packetId == ITEM_EXAMINE_PACKET) {
			final int id = stream.readUnsignedShort128();
			@SuppressWarnings("unused")
			boolean unknown = stream.readByte() == 1;// Dont delete this.
			int y = stream.readUnsignedShort();
			int x = stream.readUnsignedShortLE();
			final WorldTile tile = new WorldTile(x, y, player.getPlane());
			final int regionId = tile.getRegionId();
			final FloorItem item = World.getRegion(regionId).getGroundItem(id, tile, player);
			player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
		} else if (packetId == JOIN_FRIEND_CHAT_PACKET) {
			if (!player.hasStarted())
				return;
			FriendChatsManager.joinChat(stream.readString(), player);
		} else if (packetId == KICK_FRIEND_CHAT_PACKET) {
			if (!player.hasStarted())
				return;
			player.setLastPublicMessage(Utils.currentTimeMillis() + 1000); // avoids
																			// message
																			// appearing
			player.kickPlayerFromFriendsChannel(stream.readString());
		} else if (packetId == CHANGE_FRIEND_CHAT_PACKET) {
			if (!player.hasStarted() || !player.getInterfaceManager().containsInterface(1108))
				return;
			player.getFriendsIgnores().changeRank(stream.readString(), stream.readUnsignedByteC());
		} else if (packetId == ADD_FRIEND_PACKET) {
			if (!player.hasStarted())
				return;
			player.getFriendsIgnores().addFriend(stream.readString());
		} else if (packetId == REMOVE_FRIEND_PACKET) {
			if (!player.hasStarted())
				return;
			player.getFriendsIgnores().removeFriend(stream.readString());
		} else if (packetId == SEND_FRIEND_MESSAGE_PACKET) {
			if (!player.hasStarted())
				return;
			if (player.getMuted() > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage("You temporary muted. Recheck in 48 hours.");
				return;
			}
			String username = stream.readString();
			Player p2 = World.getPlayerByDisplayName(username);
			if (p2 == null)
				return;

			player.getFriendsIgnores().sendMessage(p2, Utils.fixChatMessage(Huffman.readEncryptedMessage(150, stream)));
		} else if (packetId == SEND_FRIEND_QUICK_CHAT_PACKET) {
			if (!player.hasStarted())
				return;
			String username = stream.readString();
			int fileId = stream.readUnsignedShort();
			byte[] data = null;
			if (length > 3 + username.length()) {
				data = new byte[length - (3 + username.length())];
				stream.readBytes(data);
			}
			data = Utils.completeQuickMessage(player, fileId, data);
			Player p2 = World.getPlayerByDisplayName(username);
			if (p2 == null)
				return;
			player.getFriendsIgnores().sendQuickChatMessage(p2, new QuickChatMessage(fileId, data));
		} else if (packetId == PUBLIC_QUICK_CHAT_PACKET) {
			if (!player.hasStarted())
				return;
			if (player.getLastPublicMessage() > Utils.currentTimeMillis())
				return;
			player.setLastPublicMessage(Utils.currentTimeMillis() + 300);
			// just tells you which client script created packet
			@SuppressWarnings("unused")
			boolean secondClientScript = stream.readByte() == 1;// script 5059
			// or 5061
			int fileId = stream.readUnsignedShort();
			byte[] data = null;
			if (length > 3) {
				data = new byte[length - 3];
				stream.readBytes(data);
			}
			data = Utils.completeQuickMessage(player, fileId, data);
			if (chatType == 0)
				player.sendPublicChatMessage(new QuickChatMessage(fileId, data));
			else if (chatType == 1)
				player.sendFriendsChannelQuickMessage(new QuickChatMessage(fileId, data));
			Logger.log(this, "Unknown chat type: " + chatType);
		} else if (packetId == CHAT_TYPE_PACKET) {
			chatType = stream.readUnsignedByte();
		} else if (packetId == CHAT_PACKET) {
			if (!player.hasStarted())
				return;
			if (player.getLastPublicMessage() > Utils.currentTimeMillis())
				return;
			if (player.getMessageAmount() > 13) {
				player.setMuted(Utils.currentTimeMillis() + (3 * 60 * 60 * 1000));
				player.getPackets().sendGameMessage("You have been muted for spamming for 3 hours.");
			}
			player.incrementMessageAmount();
			player.setLastPublicMessage(Utils.currentTimeMillis() + 300);
			int colorEffect = stream.readUnsignedByte();
			int moveEffect = stream.readUnsignedByte();
			String message = Huffman.readEncryptedMessage(250, stream);
			if (message == null || message.replaceAll(" ", "").equals(""))
				return;
			if (message.toLowerCase().contains("lethium")) {
				return;
			}
			if (message.startsWith("::") || message.startsWith(";")) {
				// if command exists and processed wont send message as public
				// message
				if (Commands.processCommand(player, message.replace("::", "").replace(";", ""), false, false))
					return;
				return;
			}
			if (player.getMuted() > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage("You temporary muted. Recheck in 48 hours.");
				return;
			}
			int effects = (colorEffect << 8) | (moveEffect & 0xff);
			if (chatType == 1)
				player.sendFriendsChannelMessage(Utils.fixChatMessage(message));
			else
				player.sendPublicChatMessage(new PublicChatMessage(Utils.fixChatMessage(message), effects));
			Logger.log(this, "Chat type: " + chatType);
		} else if (packetId == COMMANDS_PACKET) {
			if (!player.isRunning())
				return;
			boolean clientCommand = stream.readUnsignedByte() == 1;
			@SuppressWarnings("unused")
			boolean unknown = stream.readUnsignedByte() == 1;
			String command = stream.readString();
			if (!Commands.processCommand(player, command, true, clientCommand) && Settings.DEBUG)
				Logger.log(this, "Command: " + command);
		} else if (packetId == COLOR_ID_PACKET) {
			if (!player.hasStarted())
				return;
			int colorId = stream.readUnsignedShort();
			if (player.getTemporaryAttributtes().get("SkillcapeCustomize") != null)
				SkillCapeCustomizer.handleSkillCapeCustomizerColor(player, colorId);
		} else {
			Logger.log(this, "Missing packet " + packetId + ", expected size: " + length + ", actual size: " + PACKET_SIZES[packetId]);
		}
	}

	public Player getPlayer() {
		return player;
	}

}
