package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Hit;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.utils.Utils;

/**
 * 
 * @author Tyler
 * 
 */
public class NomadCombat extends CombatScript {

	public Object[] getKeys() {
		return (new Object[] { Integer.valueOf(8528) });
	}

	// Melee Emote-12696
	// Mage Emote-9300
	// Range Gfx-451
	public int attack(NPC npc, Entity target) {
		int nomadAttack = Utils.getRandom(2);
		int nomadHit = Utils.getRandom(500);
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		if (target.withinDistance(npc, 1)) {
			npc.setNextAnimation(new Animation(12696));
			delayHit(npc, 1, target, new Hit[] { getMeleeHit(npc, nomadHit) });
		} else {
			switch (nomadAttack) {
			case 1:
				npc.setNextAnimation(new Animation(12697));
				delayHit(npc, 1, target,
						new Hit[] { getMagicHit(npc, nomadHit) });
				break;
			}
		}
		return defs.getAttackDelay();
	}

}