package com.rs.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class DisplayName {

	private static LinkedList<String> list;
	private static File file = new File("./data/displays.txt");

	public static void loadFile() {
		try {
			if (!file.exists()) {
				file.createNewFile();
				return;
			}
			list = new LinkedList<String>();
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				list.add(line);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeDisplayName(String display) {
		if (!list.contains(display))
			return;
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			list.add(display);
			writer.write(display);
			writer.flush();
			writer.newLine();
			writer.close();
			System.out.println(display);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void replaceDisplayName(String oldDisplay, String newDisplay) {
		if (!list.contains(oldDisplay))
			return;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.contains(oldDisplay)) {
					line.replace(oldDisplay, newDisplay);
					list.remove(oldDisplay);
					list.add(newDisplay);
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean containsDisplay(String display) {
		if (list.contains(display))
			return true;
		return false;

	}
}
