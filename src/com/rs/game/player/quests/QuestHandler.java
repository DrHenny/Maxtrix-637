package com.rs.game.player.quests;

import java.util.HashMap;
import java.util.Map;

import com.rs.game.WorldObject;
import com.rs.game.player.Player;
import com.rs.utils.Logger;
import com.rs.utils.Utils;

public class QuestHandler {

	private static Map<Integer, Quest> quests = new HashMap<Integer, Quest>();

	@SuppressWarnings("rawtypes")
	public static final void init() {
		try {
			Class[] classes = Utils
					.getClasses("com.rs.game.player.quests.impl");
			for (Class c : classes) {
				if (c.isAnonymousClass())
					continue;
				Object o = c.newInstance();
				if (!(o instanceof Quest))
					continue;
				Quest script = (Quest) o;
				int key = script.getConfig();
				quests.put(key, script);
			}
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static boolean handleObject(Player player, WorldObject object) {
		for (Quest q : getQuests().values()) {
			return q.processObjectClick1(player, object);
		}
		return false;
	}

	public static boolean hasRequirements(Player player) {
		for (Quest q : getQuests().values()) {
			return q.hasRequirements(player);
		}
		return false;
	}

	public static Map<Integer, Quest> getQuests() {
		return quests;
	}

}
