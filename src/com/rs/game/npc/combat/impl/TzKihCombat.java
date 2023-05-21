package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.Player;

public class TzKihCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "tz-kih" };
	}

	@Override
	public int attack(NPC npc, Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		Familiar familiar = (Familiar) npc;
		boolean usingSpecial = familiar.hasSpecialOn();
		int damage = 0;
		if (npc instanceof Familiar) {// TODO get anim and gfx
			if (usingSpecial) {
				for (Entity entity : npc.getPossibleTargets()) {
					damage = getRandomMaxHit(npc, 70,
							NPCCombatDefinitions.MAGE, target);
					Player player = (Player) target;
					if (player.getTemporaryAttributtes().get("drainingPrayer") != null)
						player.getPrayer().drainPrayer(damage);
					else
						delayHit(npc, 1, entity, getMagicHit(npc, damage));
				}
			}
			return defs.getAttackDelay();
		}
		npc.setNextAnimation(new Animation(8257));
		damage = getRandomMaxHit(npc, 50, NPCCombatDefinitions.MAGE, target);
		Player player = (Player) target;
		if (player.getTemporaryAttributtes().get("drainingPrayer") != null)
			player.getPrayer().drainPrayer(damage);
		else
			delayHit(npc, 1, target, getMagicHit(npc, damage));
		return defs.getAttackDelay();
	}
}
