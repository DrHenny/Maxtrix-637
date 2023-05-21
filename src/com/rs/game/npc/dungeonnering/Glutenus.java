package com.rs.game.npc.dungeonnering;

import com.rs.game.Entity;
import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.content.dungeoneering.DungeonManager;
import com.rs.game.player.content.dungeoneering.RoomReference;

@SuppressWarnings("serial")
public final class Glutenus extends NPC {

	private int meleeNPCId;
	private int switchPrayersDelay;
	private int spawnedSpiders;
	public DungeonManager dungeon;
	public RoomReference reference;

	public Glutenus(int id, WorldTile tile, DungeonManager dungeonManager,
			RoomReference reference) {
		super(id, tile, -1, true, true);
		this.reference = reference;
		this.dungeon = dungeonManager;
		meleeNPCId = id;
		World.spawnObject(new WorldObject(49283, 10, 3, reference.getX() - 7,
				reference.getY() + 4, 0), true);
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
		super.sendDeath(source);
		dungeon.stairs(this.reference, 7, 0);
		dungeon.stairs(this.reference, 7, 15);
	}

}
