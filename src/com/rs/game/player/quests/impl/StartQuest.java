package com.rs.game.player.quests.impl;

import com.rs.game.WorldObject;
import com.rs.game.player.Player;
import com.rs.game.player.quests.Quest;

public class StartQuest extends Quest {

	@Override
	public boolean processObjectClick1(Player player, WorldObject object) {
		player.getPackets().sendGameMessage("Hello!");
		return true;
	}

}
