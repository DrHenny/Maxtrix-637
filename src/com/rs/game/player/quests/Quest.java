package com.rs.game.player.quests;

import com.rs.game.WorldObject;
import com.rs.game.player.Player;

public abstract class Quest {

	public boolean processObjectClick1(Player player, WorldObject object) {
		return false;
	}

	public boolean hasRequirements(Player player) {
		return true;
	}

	public int getConfig() {
		return 0;
	}

}
