package com.rs.game.npc.dungeonnering;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.content.dungeoneering.DungeonManager;
import com.rs.game.player.content.dungeoneering.RoomReference;

@SuppressWarnings("serial")
public final class Tokash extends NPC {

	public DungeonManager dungeon;
	public RoomReference reference;

	public Tokash(int id, WorldTile tile, DungeonManager dungeonManager,
			RoomReference reference) {
		super(id, tile, -1, true, true);
		this.reference = reference;
		this.dungeon = dungeonManager;
		this.setHitpoints((this.getCombatLevel() * 10) + 200);
		this.setName("Zenith's Slave");
		this.getCombatDefinitions().setMaxHit((this.getCombatLevel() * 2) + 50);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (isDead())
			return;
	}

	@Override
	public void sendDeath(Entity source) {
		this.setNextAnimation(new Animation(14369));
		super.sendDeath(source);
		dungeon.stairs(this.reference, 7, 0);
		dungeon.stairs(this.reference, 7, 15);
	}

}
