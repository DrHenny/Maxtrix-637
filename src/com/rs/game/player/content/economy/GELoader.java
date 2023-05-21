package com.rs.game.player.content.economy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GELoader {
	public int maxListedItems = 21435;
	public GEList[] geList = new GEList[maxListedItems];
	public String sell = "";
	public String buy = "";

	public GELoader() {
		System.out.println("[Ge Prices loaded]");
		try {
			loadprices();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadprices() throws IOException {
		String line = "";
		FileReader fr = new FileReader("./data/items/grandExchange.cfg");
		BufferedReader br = new BufferedReader(fr);
		String s = "";
		while ((s = br.readLine()) != null) {
			String Read[] = s.split("\t");
			if (Read[0].contains("[")) {
				break;
			}
			String itemId = Read[0].substring(7);
			if (Read[1].contains("m")) {
				sell = Read[1].replace("m", "0000");
			} else if (Read[1].contains("k")) {
				sell = Read[1].replace("k", "00");
			} else {
				sell = Read[1].replace(",", "");
			}
			if (Read[2].contains("m")) {
				buy = Read[2].replace("m", "0000");
			} else if (Read[2].contains("k")) {
				buy = Read[2].replace("k", "00");
			} else {
				buy = Read[2].replace(",", "");
			}
			sell = sell.replace(".", "").replace(" ", "");
			buy = buy.replace(".", "").replace(" ", "");

			if (sell.equals("null")) {
				sell = "0";
			}
			if (buy.equals("null")) {
				buy = "0";
			}
			newItemList(Integer.parseInt(itemId), Integer.parseInt(sell),
					Integer.parseInt(buy));
		}

	}

	private void newItemList(int ItemId, int buy, int sell) {
		if (ItemId > maxListedItems) {
			System.out.println("maxListedItems to low.");
			return;
		}
		geList[ItemId] = new GEList(ItemId, buy, sell);
	}
}