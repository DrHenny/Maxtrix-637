package com.rs.game.player.dialogues;

import com.rs.game.WorldTile;
import com.rs.utils.Misc;

public class Vote extends Dialogue {

	@Override
	public void start() {
		sendDialogue(SEND_4_OPTIONS, "Oh, Would you like to go to Glacors?",
				"10m ZSGP", "1 Vote Point", "Mystery Box", ""); // Change
																// options
																// maybe?
	}

	public void run(int interfaceId, int componentId) {
		if (interfaceId == SEND_4_OPTIONS) {
			if (componentId == 1) {
				player.getInventory().addItem(995, 10000000);
				player.GotVote = false;
				player.sm("You have recieved 10m.");
				player.sm("Thank you for voting, please vote every 24hours!");
				player.getInterfaceManager().closeChatBoxInterface();
				return;
			} else if (componentId == 2) {
				player.VotePoints += 1;
				player.GotVote = false;
				player.sm("You have recieved a vote point and now have "
						+ player.VotePoints + " vote points.");
				player.sm("Thank you for voting, please vote every 24hours!");
				player.getInterfaceManager().closeChatBoxInterface();
				return;

			} else if (componentId == 3) {
				int[] crapvoteitem = { 1511, 1511, 1511, 1511, 1518, 1518,
						1518, 1521, 1521, 1521, 1540 };
				int[] goodvoteitem = { 2615, 2617, 2619, 2621, 2591, 2593,
						2595, 2597, 10400, 10402, 10404, 10406, 10408, 10410,
						10412, 10414, 10416, 10418, 10420, 10422, 10424, 10426,
						10428, 10430, 10432, 10434, 10436, 10438 };
				int[] rarevoteitem = { 23679, 23680, 23681, 23682, 23683,
						23684, 23685, 23686, 23687, 23688, 23689, 23690, 23691,
						23692, 23693, 23694, 23695, 23696, 23697, 23698, 23699,
						23700 };
				int crapitem = crapvoteitem[Misc.random(crapvoteitem.length)];
				int gooditem = goodvoteitem[Misc.random(goodvoteitem.length)];
				int rareitem = rarevoteitem[Misc.random(rarevoteitem.length)];
				int j = Misc.random(1000);
				if (j > 945) {
					player.getInventory().addItem(rareitem, 1);
					player.GotVote = false;
					player.sm("You have recieved a lucky item, your pretty damn lucky.");
					player.sm("Thank you for voting, please vote every 24hours!");
					player.getInterfaceManager().closeChatBoxInterface();
					return;
				} else if (j > 365) {
					player.getInventory().addItem(gooditem, 1);
					player.GotVote = false;
					player.sm("You have recieved a good item, maybe you will be luckier next time.");
					player.sm("Thank you for voting, please vote every 24hours!");
					player.getInterfaceManager().closeChatBoxInterface();
					return;
				} else if (j >= 0) {
					player.getInventory().addItem(crapitem, 1);
					player.GotVote = false;
					player.sm("You have recieved a crap item, maybe you will be lucky next time.");
					player.sm("Thank you for voting, please vote every 24hours!");
					player.getInterfaceManager().closeChatBoxInterface();
					return;
				}

			}
			player.sm("Thank you for voting, please vote every 24hours!");
		}
	}

	@Override
	public void finish() {

	}

}