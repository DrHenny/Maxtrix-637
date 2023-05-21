package com.rs.game.player.content;

import com.rs.game.player.ClanChat;
import com.rs.game.player.Player;
import com.rs.io.InputStream;

public class ClanChatManager {

	public static void handleButtons(Player player, int buttonId, int packetId,
			InputStream stream) {
		String username = player.getDisplayName();
		String clanName = "Feather";

		if (buttonId == 85 && packetId == 61) {
			player.getPackets().sendJoinClanChat(username, clanName);
		}

		if (buttonId == 80 && packetId == 61) {
			if (player.inClanChat == false) {
				player.getPackets().sendGameMessage(
						"You need to be in a clan chat to view the settings");
			} else if (player.inClanChat == true) {
				player.getInterfaceManager().sendInterface(1096);
			}
		}

	}

}
