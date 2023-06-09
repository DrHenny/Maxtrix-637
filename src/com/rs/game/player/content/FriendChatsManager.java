package com.rs.game.player.content;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.Settings;
import com.rs.game.World;
import com.rs.game.minigames.War;
import com.rs.game.player.FriendsIgnores;
import com.rs.game.player.Player;
import com.rs.game.player.QuickChatMessage;
import com.rs.io.OutputStream;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.Utils;

public class FriendChatsManager {

	private String owner;
	private String ownerDisplayName;
	private FriendsIgnores settings;
	private CopyOnWriteArrayList<Player> players; // even with sycro there may
													// be problems, so
													// concurrent map
	private ConcurrentHashMap<String, Long> bannedPlayers; // even with sycro
															// there may be
															// problems, so
															// concurrent map
	private byte[] dataBlock;
	private War war;
	private boolean challenger;
	private static HashMap<String, FriendChatsManager> cachedFriendChats;

	public static void init() {
		cachedFriendChats = new HashMap<String, FriendChatsManager>();
	}

	public int getRank(String username) {
		if (username.equals(owner))
			return 7;
		return settings.getRank(username);
	}

	public CopyOnWriteArrayList<Player> getPlayers() {
		return players;
	}

	static int i = 0;

	public int getWhoCanKickOnChat() {
		return settings.getWhoCanKickOnChat();
	}

	public String getOwnerDisplayName() {
		return ownerDisplayName;
	}

	public String getOwnerName() {
		return owner;
	}

	public String getChannelName() {
		return settings.getChatName();
	}

	private void joinChat(Player player) {
		synchronized (this) {
			if (!player.getUsername().equals(owner)
					&& !settings.hasRankToJoin(player.getUsername())
					&& player.getRights() < 2) {
				player.getPackets()
						.sendGameMessage(
								"You do not have a enough rank to join this friends chat channel.");
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
							"You have been banned from this channel.");
					return;
				}
				bannedPlayers.remove(player.getUsername());
			}
			joinChatNoCheck(player);
		}
	}

	public void leaveChat(Player player, boolean logout) {
		synchronized (this) {
			player.setCurrentFriendChat(null);
			players.remove(player);
			if (players.size() == 0) { // no1 at chat so uncache it
				synchronized (cachedFriendChats) {
					cachedFriendChats.remove(owner);
				}
			} else
				refreshChannel();
			if (!logout) {
				player.setCurrentFriendChatOwner(null);
				player.getPackets().sendGameMessage(
						"You have left the channel.");
				player.getPackets().sendFriendsChatChannel();
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

	public void kickPlayerFromChat(Player player, String name) {
		// System.out.println(name);
		synchronized (this) {
			int rank = getRank(player.getUsername());
			if (rank < getWhoCanKickOnChat())
				return;
			Player kicked = getPlayerByDisplayName(name);
			if (kicked == null) {
				player.getPackets().sendGameMessage(
						"This player is not this channel.");
				return;
			}
			if (rank <= getRank(kicked.getUsername()))
				return;
			kicked.setCurrentFriendChat(null);
			kicked.setCurrentFriendChatOwner(null);
			players.remove(kicked);
			bannedPlayers.put(kicked.getUsername(), Utils.currentTimeMillis());
			kicked.getPackets().sendFriendsChatChannel();
			kicked.getPackets().sendGameMessage(
					"You have been kicked from the friends chat channel.");
			player.getPackets().sendGameMessage(
					"You have kicked " + kicked.getUsername()
							+ " from friends chat channel.");
			refreshChannel();

		}
	}

	private void joinChatNoCheck(Player player) {
		synchronized (this) {
			players.add(player);
			player.setCurrentFriendChat(this);
			player.setCurrentFriendChatOwner(owner);
			player.getPackets().sendGameMessage(
					"You are now talking in the friends chat channel "
							+ settings.getChatName());
			refreshChannel();
		}
	}

	public void destroyChat() {
		synchronized (this) {
			for (Player player : players) {
				player.setCurrentFriendChat(null);
				player.setCurrentFriendChatOwner(null);
				player.getPackets().sendFriendsChatChannel();
				player.getPackets().sendGameMessage(
						"You have been removed from this channel!");
			}
		}
		synchronized (cachedFriendChats) {
			cachedFriendChats.remove(owner);
		}

	}

	public void sendQuickMessage(Player player, QuickChatMessage message) {
		synchronized (this) {
			if (!player.getUsername().equals(owner)
					&& !settings.canTalk(player) && player.getRights() < 2) {
				player.getPackets()
						.sendGameMessage(
								"You do not have a enough rank to talk on this friends chat channel.");
				return;
			}
			String formatedName = Utils.formatPlayerNameForDisplay(player
					.getUsername());
			String displayName = player.getDisplayName();
			for (Player p2 : players)
				p2.getPackets().receiveFriendChatQuickMessage(formatedName,
						displayName, player.getRights(),
						settings.getChatName(), message);
		}
	}

	public void sendMessage(Player player, String message) {
		synchronized (this) {
			if (!player.getUsername().equals(owner)
					&& !settings.canTalk(player) && player.getRights() < 2) {
				player.getPackets()
						.sendGameMessage(
								"You do not have a enough rank to talk on this friends chat channel.");
				return;
			}
			String formatedName = Utils.formatPlayerNameForDisplay(player
					.getUsername());
			String displayName = player.getDisplayName();
			for (Player p2 : players)
				p2.getPackets().receiveFriendChatMessage(formatedName,
						displayName, player.getRights(),
						settings.getChatName(), message);
		}
	}

	private void refreshChannel() {
		synchronized (this) {
			OutputStream stream = new OutputStream();
			stream.writeString(ownerDisplayName);
			String ownerName = Utils.formatPlayerNameForDisplay(owner);
			stream.writeByte(getOwnerDisplayName().equals(ownerName) ? 0 : 1);
			if (!getOwnerDisplayName().equals(ownerName))
				stream.writeString(ownerName);
			stream.writeLong(Utils.stringToLong(getChannelName()));
			stream.writeByte(getWhoCanKickOnChat());
			stream.writeByte(getPlayers().size());
			for (Player p : getPlayers()) {
				String displayName = p.getDisplayName();
				String name = Utils.formatPlayerNameForDisplay(p.getUsername());
				stream.writeString(displayName);
				stream.writeByte(displayName.equals(name) ? 0 : 1);
				if (!displayName.equals(name))
					stream.writeString(name);
				stream.writeShort(1);
				int rank = getRank(p.getUsername());
				stream.writeByte(rank);
				stream.writeString(Settings.SERVER_NAME);
			}
			dataBlock = new byte[stream.getOffset()];
			stream.setOffset(0);
			stream.getBytes(dataBlock, 0, dataBlock.length);
			for (Player player : players)
				player.getPackets().sendFriendsChatChannel();
		}
	}

	public byte[] getDataBlock() {
		return dataBlock;
	}

	private FriendChatsManager(Player player) {
		owner = player.getUsername();
		ownerDisplayName = player.getDisplayName();
		settings = player.getFriendsIgnores();
		players = new CopyOnWriteArrayList<Player>();
		bannedPlayers = new ConcurrentHashMap<String, Long>();
	}

	public static void destroyChat(Player player) {
		synchronized (cachedFriendChats) {
			FriendChatsManager chat = cachedFriendChats.get(player
					.getUsername());
			if (chat == null)
				return;
			chat.destroyChat();
			player.getPackets().sendGameMessage(
					"Your friends chat channel has now been disabled!");
		}
	}

	public static void linkSettings(Player player) {
		synchronized (cachedFriendChats) {
			FriendChatsManager chat = cachedFriendChats.get(player
					.getUsername());
			if (chat == null)
				return;
			chat.settings = player.getFriendsIgnores();
		}
	}

	public static void refreshChat(Player player) {
		synchronized (cachedFriendChats) {
			FriendChatsManager chat = cachedFriendChats.get(player
					.getUsername());
			if (chat == null)
				return;
			chat.refreshChannel();
		}
	}

	public static void joinChat(String ownerName, Player player) {
		synchronized (cachedFriendChats) {
			if (player.getCurrentFriendChat() != null)
				return;
			player.getPackets()
					.sendGameMessage("Attempting to join channel...");
			String formatedName = Utils.formatPlayerNameForProtocol(ownerName);
			FriendChatsManager chat = cachedFriendChats.get(formatedName);
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
					player.getPackets()
							.sendGameMessage(
									"You do not have a enough rank to join this friends chat channel.");
					return;
				}
				chat = new FriendChatsManager(owner);
				cachedFriendChats.put(ownerName, chat);
				chat.joinChatNoCheck(player);
			} else
				chat.joinChat(player);
		}

	}

	public War getWar() {
		return war;
	}

	public War setWar(War war) {
		return this.war = war;
	}

	public boolean isChallenger() {
		return challenger;
	}

	public void setChallenger(boolean challenger) {
		this.challenger = challenger;
	}
}
