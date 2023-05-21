package com.rs.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class StorePriceCommit {

	public static void main(String[] args) throws Exception {
		initialize();
		System.out.println(get(995));
	}

	/**
	 * The map price map <Integer, The item id ,Integer> The item price
	 */
	private static Map<Integer, Integer> priceMap = new HashMap<Integer, Integer>();

	/**
	 * Initializes the price loader, loads the prices
	 */
	public static void initialize() throws Exception {
		File prices = new File("data/shopPrices.ini");
		if (!prices.exists()) {
			System.err.println("Could not find shop price file!");
			System.exit(0);
		}
		BufferedReader reader = new BufferedReader(new FileReader(prices));
		String line;
		while ((line = reader.readLine()) != null) {
			String[] split = line.split(":");
			split[1] = split[1].replaceAll("k", "000");
			split[1] = split[1].replaceAll("m", "000000");
			priceMap.put(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
		}
		System.out.println("Loaded " + priceMap.size() + " shop item prices.");
	}

	/**
	 * Grabs the item price for itemId
	 * 
	 * @param itemId
	 *            The item id to get the price for
	 * @return The item shop price
	 */
	public static int get(int itemId) {
		return priceMap.get(itemId);
	}

}