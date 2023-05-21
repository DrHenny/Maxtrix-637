package com.rs.game.npc.others;

import java.util.List;

import com.rs.game.World;
import com.rs.game.WorldObject;
import com.rs.game.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.OwnedObjectManager;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.game.player.OwnedObjectManager.ConvertEvent;
import com.rs.game.player.actions.Hunter.HunterNPC;

@SuppressWarnings("serial")
public class HuntNPC extends NPC {

	public HuntNPC(int id, WorldTile tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
	}

	@Override
	public void processNPC() {
		super.processNPC();
		List<WorldObject> objects = World.getRegion(getRegionId())
				.getSpawnedObjects();
		if (objects != null) {
			final HunterNPC info = HunterNPC.forId(getId());
			int objectId = info.getEquipment().getObjectId();
			for (WorldObject object : objects) {
				if (object.getId() == objectId) {
					if (OwnedObjectManager.convertIntoObject(object,
							new WorldObject(info.getTransformObjectId(), 10, 0,
									this.getX(), this.getY(), this.getPlane()),
							new ConvertEvent() {
								@Override
								public boolean canConvert(Player player) {
									if (player == null)
										return false;
									return player.getSkills().getLevel(
											Skills.HUNTER) >= info.getLevel();
								}
							})) {
						setRespawnTask(); // auto finishes
					}
				}
			}
		}
	}
}
