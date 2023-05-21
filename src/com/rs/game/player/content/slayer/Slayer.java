package com.rs.game.player.content.slayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.rs.game.player.Player;
import com.rs.utils.Misc;

/**
 * The slayer master dialogue
 * 
 * @author Emperial
 * 
 */
public class Slayer {

	/**
	 * 0 = Hello 1 = Option
	 * 
	 * OTHER 2 = For your first task I'm assigning you to 3 = You still have a
	 * task 4 = Great your doing great, Your new task is
	 */

	public static String assignTask(Player player, SlayerMaster master) {
		SlayerTask tasks = player.slayerTask;
		List<SlayerTasks> possibleTasks = new ArrayList<SlayerTasks>();
		for (SlayerTasks task : SlayerTasks.values()) {
			if (task.type == master.type) {
				possibleTasks.add(task);
			}
		}
		if (possibleTasks.isEmpty()) {
			return null;
		}
		Collections.shuffle(possibleTasks);
		SlayerTasks task = possibleTasks.get(0);
		tasks.setTask(task);
		tasks.setMonstersLeft(Misc.random(tasks.getTask().min,
				tasks.getTask().max));
		return tasks.getTaskMonstersLeft() + " " + tasks.getTask().simpleName;
	}

}
