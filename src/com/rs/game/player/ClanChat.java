package com.rs.game.player;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.Settings;
import com.rs.cores.CoresManager;
import com.rs.game.World;
import com.rs.io.OutputStream;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.Utils;

/**
 * 
 * @author Kyle
 * 
 */
public class ClanChat {
	private String clanowner;
	private String displayName;
	private FriendsIgnores settings;
	private CopyOnWriteArrayList<Player> players;
	private ConcurrentHashMap<String, Long> bannedPlayers;
	private byte[] dataBlock;

	private static HashMap<String, ClanChat> cachedClanChats;

	public static void init() {
		cachedClanChats = new HashMap<String, ClanChat>();
	}

	private ClanChat(Player player) {
		clanowner = player.getUsername();
		displayName = player.getDisplayName();
		settings = player.getFriendsIgnores();
		players = new CopyOnWriteArrayList<Player>();
		bannedPlayers = new ConcurrentHashMap<String, Long>();
	}

	public int getRank(int rights, String username) {
		if (rights == 2)
			return 127;
		if (username.equals(clanowner))
			return 7;
		return settings.getRank(username);
	}

	public CopyOnWriteArrayList<Player> getPlayers() {
		return players;
	}

	public int getWhoCanKickOnChat() {
		return settings.getWhoCanKickOnChat();
	}

	public String getOwnerDisplayName() {
		return displayName;
	}

	public String getOwnerName() {
		return clanowner;
	}

	public String getChannelName() {
		return settings.getChatName().replaceAll("<img=", "");
	}

	public void enter(Player player) {
		boolean hasWar = player.getCurrentFriendChat() != null
				&& player.getClanChat() != null;
		final ClanChat c = hasWar ? player.getClanChat() : (ClanChat) player
				.getTemporaryAttributtes().get("view_clan_chat");
		if (c == null) {
			return;
		}

		player.getControlerManager().startControler("clan_chat", c);
		CoresManager.slowExecutor.submit(new Runnable() {
			@Override
			public void run() {

			}
		});
		return;
	}

	public void accept(final Player player) {
		final Player other = (Player) player.getTemporaryAttributtes().get(
				"clan_request");
		if (other != null
				&& (Boolean) other.getTemporaryAttributtes().get(
						"accepted_clan") == Boolean.TRUE) {
			CoresManager.slowExecutor.submit(new Runnable() {
				@Override
				public void run() {
					player.getTemporaryAttributtes().remove("accepted_clan");
					other.getTemporaryAttributtes().remove("accepted_clan");
					player.getInterfaceManager().closeScreenInterface();
					other.getInterfaceManager().closeScreenInterface();

				}
			});
			return;
		}
		player.getTemporaryAttributtes().put("accepted_clan", true);
	}

	public void leave(Player p, boolean ingame) {
		if (players.contains(p)) {
			players.remove(p);
		} else if (!ingame) {
			return;
		}
		boolean resized = p.getInterfaceManager().hasRezizableScreen();
		p.getPackets().closeInterface(resized ? 746 : 548, resized ? 11 : 27);
		p.getControlerManager().startControler("clan_chat_request");
		updateClan();
	}

	public void updateClan() {

	}

	private void joinChat(Player player) {
		synchronized (this) {
			if (!player.getUsername().equals(clanowner)
					&& !settings.hasRankToJoin(player.getUsername())
					&& player.getRights() < 2) {
				player.getPackets().sendGameMessage(
						"You must be invited to join the clan chat channel.");
				return;
			}
			if (players.size() >= 100) {
				player.getPackets().sendGameMessage("This chat is full.");
				return;
			}
			Long bannedSince = bannedPlayers.get(player.getUsername());
			if (bannedSince != null) {
				if (bannedSince + 3600000 > Utils.currentTimeMillis()) {
					player.getPackets().sendGameMessage(
							"You have been removed from this clan chat.");
					return;
				}
				bannedPlayers.remove(player.getUsername());
			}
			joinChatNoCheck(player);
		}
	}

	public void leaveChat(Player player, boolean logout) {
		synchronized (this) {
			player.setClanchat(null);
			players.remove(player);
			if (players.size() == 0) { // no1 at chat so uncache it
				synchronized (cachedClanChats) {
					cachedClanChats.remove(clanowner);
				}
			} else
				refreshChannel();
			if (!logout) {
				player.setClanChatOwner(null);
				player.getPackets().sendGameMessage("You have left the clan.");
				player.getPackets().sendClanChatChannel();
			}

		}
	}

	public Player getPlayerByDisplayName(String username) {
		String formatedUsername = Utils.formatPlayerNameForProtocol(username);
		for (Player player : players) {
			if (player.getUsername().equals(formatedUsername)
					|| player.getDisplayName().equals(username))
				return player;
		}
		return null;
	}

	public void kickPlayerFromChat(Player player, String username) {
		String name = "";
		for (char character : username.toCharArray()) {
			name += Utils.containsInvalidCharacter(character) ? " " : character;
		}
		synchronized (this) {
			int rank = getRank(player.getRights(), player.getUsername());
			if (rank < getWhoCanKickOnChat())
				return;
			Player kicked = getPlayerByDisplayName(name);
			if (kicked == null) {
				player.getPackets().sendGameMessage(
						"This player is not in the clan.");
				return;
			}
			if (rank <= getRank(kicked.getRights(), kicked.getUsername()))
				return;
			kicked.setClanchat(null);
			kicked.setClanChatOwner(null);
			players.remove(kicked);
			bannedPlayers.put(kicked.getUsername(), Utils.currentTimeMillis());
			kicked.getPackets().sendClanChatChannel();
			kicked.getPackets().sendGameMessage(
					"You have been kicked from the clan chat.");
			player.getPackets().sendGameMessage(
					"You have kicked " + kicked.getUsername()
							+ " from clan chat channel.");
			refreshChannel();

		}
	}

	private void joinChatNoCheck(Player player) {
		synchronized (this) {
			players.add(player);
			player.setClanchat(this);
			player.setClanChatOwner(clanowner);
			player.getPackets().sendGameMessage(
					"You are now talking in the clan chat "
							+ settings.getChatName());
			refreshChannel();
		}
	}

	public void destroyChat() {
		synchronized (this) {
			for (Player player : players) {
				player.setClanchat(null);
				player.setClanChatOwner(null);
				player.getPackets().sendClanChatChannel();
				player.getPackets().sendGameMessage(
						"You have been removed from this clan!");
			}
		}
		synchronized (cachedClanChats) {
			cachedClanChats.remove(clanowner);
		}

	}

	public void sendQuickMessage(Player player, QuickChatMessage message) {
		synchronized (this) {
			if (!player.getUsername().equals(clanowner)
					&& !settings.canTalk(player) && player.getRights() < 2) {
				player.getPackets()
						.sendGameMessage(
								"You do not have a enough rank to talk on this clan chat channel.");
				return;
			}
			String formatedName = Utils.formatPlayerNameForDisplay(player
					.getUsername());
			String displayName = player.getDisplayName();
			int rights = player.getMessageIcon();
			for (Player p2 : players)
				p2.getPackets().receiveFriendChatQuickMessage(formatedName,
						displayName, rights, settings.getChatName(), message);
		}
	}

	public void sendMessage(Player player, String message) {
		synchronized (this) {
			if (!player.getUsername().equals(clanowner)
					&& !settings.canTalk(player) && player.getRights() < 2) {
				player.getPackets()
						.sendGameMessage(
								"You do not have a enough rank to talk on this clan chat channel.");
				return;
			}
			String formatedName = Utils.formatPlayerNameForDisplay(player
					.getUsername());
			String displayName = player.getDisplayName();
			int rights = player.getMessageIcon();
			for (Player p2 : players)
				p2.getPackets().receiveFriendChatMessage(formatedName,
						displayName, rights, settings.getChatName(), message);
		}
	}

	public void sendDiceMessage(Player player, String message) {
		synchronized (this) {
			if (!player.getUsername().equals(clanowner)
					&& !settings.canTalk(player) && player.getRights() < 2) {
				player.getPackets()
						.sendGameMessage(
								"You do not have a enough rank to talk on this clan chat channel.");
				return;
			}
			for (Player p2 : players) {
				p2.getPackets().sendGameMessage(message);
			}
		}
	}

	private void refreshChannel() {
		synchronized (this) {
			OutputStream stream = new OutputStream();
			stream.writeString(displayName);
			String ownerName = Utils.formatPlayerNameForDisplay(clanowner);
			stream.writeByte(getOwnerDisplayName().equals(ownerName) ? 0 : 1);
			if (!getOwnerDisplayName().equals(ownerName))
				stream.writeString(ownerName);
			stream.writeLong(Utils.stringToLong(getChannelName()));
			int kickOffset = stream.getOffset();
			stream.writeByte(0);
			stream.writeByte(getPlayers().size());
			for (Player player : getPlayers()) {
				String displayName = player.getDisplayName();
				String name = Utils.formatPlayerNameForDisplay(player
						.getUsername());
				stream.writeString(displayName);
				stream.writeByte(displayName.equals(name) ? 0 : 1);
				if (!displayName.equals(name))
					stream.writeString(name);
				stream.writeShort(1);
				int rank = getRank(player.getRights(), player.getUsername());
				stream.writeByte(rank);
				stream.writeString(Settings.SERVER_NAME);
			}
			dataBlock = new byte[stream.getOffset()];
			stream.setOffset(0);
			stream.getBytes(dataBlock, 0, dataBlock.length);
			for (Player player : players) {
				dataBlock[kickOffset] = (byte) (player.getUsername().equals(
						clanowner) ? 0 : getWhoCanKickOnChat());
				// player.getPackets().sendClanChatChannel();
			}
		}
	}

	public byte[] getDataBlock() {
		return dataBlock;
	}

	public static void destroyChat(Player player) {
		synchronized (cachedClanChats) {
			ClanChat chat = cachedClanChats.get(player.getUsername());
			if (chat == null)
				return;
			chat.destroyChat();
			player.getPackets().sendGameMessage(
					"Your clan chat channel has now been disabled!");
		}
	}

	public static void linkSettings(Player player) {
		synchronized (cachedClanChats) {
			ClanChat chat = cachedClanChats.get(player.getUsername());
			if (chat == null)
				return;
			chat.settings = player.getFriendsIgnores();
		}
	}

	public static void refreshChat(Player player) {
		synchronized (cachedClanChats) {
			ClanChat chat = cachedClanChats.get(player.getUsername());
			if (chat == null)
				return;
			chat.refreshChannel();
		}
	}

	public static void joinChat(String ownerName, Player player) {
		synchronized (cachedClanChats) {
			if (player.getClanChat() != null)
				return;
			player.getPackets()
					.sendGameMessage("Attempting to join channel...");
			String formatedName = Utils.formatPlayerNameForProtocol(ownerName);
			ClanChat chat = cachedClanChats.get(formatedName);
			if (chat == null) {
				Player owner = World.getPlayerByDisplayName(ownerName);
				if (owner == null) {
					if (!SerializableFilesManager.containsPlayer(formatedName)) {
						player.getPackets()
								.sendGameMessage(
										"The channel you tried to join does not exist.");
						return;
					}
					owner = SerializableFilesManager.loadPlayer(formatedName);
					if (owner == null) {
						player.getPackets()
								.sendGameMessage(
										"The channel you tried to join does not exist.");
						return;
					}
					owner.setUsername(formatedName);
				}
				FriendsIgnores settings = owner.getFriendsIgnores();
				if (!settings.hasFriendChat()) {
					player.getPackets().sendGameMessage(
							"The channel you tried to join does not exist.");
					return;
				}
				if (!player.getUsername().equals(ownerName)
						&& !settings.hasRankToJoin(player.getUsername())
						&& player.getRights() < 2) {
					player.getPackets().sendGameMessage(
							"You need to be invited to join this clan chat.");
					return;
				}
				chat = new ClanChat(player);
				cachedClanChats.put(ownerName, chat);
				chat.joinChatNoCheck(player);
			} else
				chat.joinChat(player);
		}

	}

}