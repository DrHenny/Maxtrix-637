package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.World;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.utils.Utils;

/**
 * 
 * @author Humid
 * 
 */
public class JadCombat extends CombatScript {

	public Object[] getKeys() {
		return (new Object[] { Integer.valueOf(14221), Integer.valueOf(2745),
				Integer.valueOf(15208) });
	}

	// Range Emote-9276
	// Melee Emote-9277
	// Mage Emote-9300
	// Range Gfx-451
	public int attack(NPC npc, Entity target) {
		int jadAttack = Utils.getRandom(2);
		int jadHit = Utils.getRandom(500);
		NPCCombatDefinitions defs = npc.getCombatDefinitions();

		if (target.withinDistance(npc, 1)) {
			npc.setNextAnimation(new Animation(16204));
			delayHit(npc, 1, target, new Hit[] { getMeleeHit(npc, jadHit) });
		} else {
			switch (jadAttack) {
			case 1:
				npc.setNextAnimation(new Animation(16195));
				npc.setNextGraphics(new Graphics(2995));
				delayHit(npc, 1, target, new Hit[] { getMagicHit(npc, jadHit) });
				break;
			case 2:
				npc.setNextAnimation(new Animation(16202));
				npc.setNextGraphics(new Graphics(2994));
				World.sendProjectile(npc, target, 1627, 41, 16, 41, 35, 16, 0);
				delayHit(npc, 1, target, new Hit[] { getRangeHit(npc, jadHit) });
				break;
			}
		}
		return defs.getAttackDelay();
	}

}