package com.rs.game.npc.dungeonnering;

import com.rs.game.Entity;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.content.dungeoneering.Dungeon;
import com.rs.game.player.content.dungeoneering.DungeonManager;
import com.rs.game.player.content.dungeoneering.RoomReference;

@SuppressWarnings("serial")
public class CopyOfDungeonBoss extends NPC {

	private DungeonManager dungeon;
	private RoomReference room;

	public CopyOfDungeonBoss(int id, WorldTile tile, DungeonManager dungeon) {
		super(id, tile, -1, false, true);
		this.dungeon = dungeon;
		this.room = room;
		setForceMultiArea(true);
		setForceAgressive(true);
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		// dungeon.openStairs(player);
	}

	public Dungeon getDungeon() {
		return dungeon.getDungeon();
	}
}
