package com.rs.game.npc.combat.impl;

import com.rs.game.Animation;
import com.rs.game.Entity;
import com.rs.game.Graphics;
import com.rs.game.Hit;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.CombatScript;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.utils.Utils;

public class JadinkoMaleCombat extends CombatScript {

	public Object[] getKeys() {
		return (new Object[] { Integer.valueOf(13822) });
	}

	public int attack(NPC jM, Entity target) {
		int attack = Utils.getRandom(3);
		int hit = Utils.getRandom(600);
		int distanceX = target.getX() - jM.getX();
		int distanceY = target.getY() - jM.getY();
		NPCCombatDefinitions defs = jM.getCombatDefinitions();
		int size = jM.getSize();
		if (distanceX < -1 || distanceY < -1) {
			jM.setNextAnimation(new Animation(3215));
			jM.setNextGraphics(new Graphics(2716));
			target.setNextGraphics(new Graphics(2726));
			delayHit(jM, 2, target, new Hit[] { getMagicHit(jM, hit) });
		} else {
			switch (attack) {
			case 2:
			case 3:
			case 0:
				jM.setNextAnimation(new Animation(3214));
				delayHit(jM, 2, target, new Hit[] { getMeleeHit(jM, hit) });
				break;
			case 1:
				jM.setNextAnimation(new Animation(3215));
				jM.setNextGraphics(new Graphics(2716));
				target.setNextGraphics(new Graphics(2726));
				delayHit(jM, 2, target, new Hit[] { getMagicHit(jM, hit) });
				break;
			}
		}
		return defs.getAttackDelay();
	}
}