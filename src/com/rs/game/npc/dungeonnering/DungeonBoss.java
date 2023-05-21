package com.rs.game.npc.dungeonnering;

import com.rs.game.Entity;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.content.Dungeoneering.Dungeon;

@SuppressWarnings("serial")
public class DungeonBoss extends NPC {

	private Dungeon dungeon;

	public DungeonBoss(int id, WorldTile tile, Dungeon dungeon) {
		super(id, tile, dungeon.getDungeonBossRoomHash(), false, true);
		this.dungeon = dungeon;
		setForceMultiArea(true);
		setForceAgressive(true);
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		dungeon.openStairs();
	}

	public Dungeon getDungeon() {
		return dungeon;
	}
}
