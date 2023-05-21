package com.rs.tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.content.ItemConstants;

/**
 * @author Tyluur <ItsTyluur@gmail.com>
 */
public class TipItPriceDumper {

	private static final String OUT = "grand_exchange_tipit.txt";

	public static void main(String[] args) throws IOException {
		System.out.println("Starting..");
		Cache.init();
		while (true) {
			for (int i = 0; i < 25000; i++) {
				if (ItemConstants.isTradeable(new Item(i))
						&& !ItemDefinitions.getItemDefinitions(i).isNoted()) {
					dumpItem(i);
					System.err.println("Finished dumping " + i);
				}
			}
		}
	}

	private static int retryCounts = 0;

	private static final void dumpItem(int itemId) {
		final String URL = "tip.it/runescape/index.php?gec&itemid=" + itemId
				+ "";
		try {
			WebPage page = new WebPage(URL);
			page.load();
			for (String lines : page.getLines()) {
				if (lines
						.contains("<tr><td colspan=\"4\"><b>Current Market Price: </b>")) {
					String text = lines.replaceAll("<tr>", "")
							.replaceAll("<td colspan=\"4\">", "")
							.replaceAll("<b>", "").replaceAll("</b>", "")
							.replaceAll("</td>", "").replaceAll("</tr>", "")
							.replaceAll(",", "").replaceAll("gp", "")
							.replaceAll("Current Market Price: ", "");
					BufferedWriter writer = new BufferedWriter(new FileWriter(
							OUT, true));
					writer.write(itemId + " - " + text);
					writer.newLine();
					writer.flush();
					writer.close();
				}
			}
		} catch (IOException e) {
			dumpItem(itemId);
			System.err
					.println("Error, retrying time number : " + retryCounts++);
		}
	}

}