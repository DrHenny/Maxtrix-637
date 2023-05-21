package com.rs.game.npc.fightcave;

import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.utils.Misc;

public class FightCaveNPC extends NPC {

	public FightCaveNPC(int id, WorldTile tile) {
		super(id, tile, Misc.getNameHash("FightCaves"), false, true);
	}
}