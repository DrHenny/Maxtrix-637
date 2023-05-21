package com.rs.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.rs.cache.Cache;
import com.rs.cache.loaders.ClientScriptMap;
import com.rs.cache.loaders.GraphicDefinitions;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.utils.Utils;

public class Test {

	// private static ArrayList<Integer> value = new ArrayList<Integer>();

	public static void main(String[] args) throws IOException, Exception {
		Cache.init();

		/*
		 * for(int modelId = 61330; modelId < 61440; modelId++) { int[]
		 * modelType = isModelType(modelId); if(modelType == null ||
		 * modelType[0] == 2)
		 * System.out.println(modelId+", "+Arrays.toString(modelType)); }
		 */
		/*
		 * int modelId = 61332; int[] modelType = isModelType(61356);
		 * System.out.println(modelId+", "+Arrays.toString(modelType));
		 */
		// System.out.println(ItemDefinitions.getItemDefinitions(23114).modelId);
		// System.out.println(Arrays.toString(NPCDefinitions.getNPCDefinitions(14242).modelIds));
		/*
		 * int xp_rate = 7; for (int index = 0; index < 10; index++) { for (int
		 * i = 0; i < 5000; i++) { int max_xp = 13034431; int currentXp = (int)
		 * ((.4 * Utils.getRandom(20) + 80) * xp_rate); value.add(max_xp /
		 * currentXp); } xp_rate += 5 + Utils.getRandom(5); int lastValues =
		 * 1300000; int highestValues = 1; for (int i = 0; i < value.size();
		 * i++) { int values = value.get(i); if (values < lastValues) lastValues
		 * = (int) values; if (values > highestValues) highestValues = (int)
		 * values; } System.out.println("Highest Value : " + highestValues +
		 * " Lowest values " + lastValues+ " Xp rate "+xp_rate); }
		 */
		System.out.println(Cache.STORE.getIndexes()[17].getTable().isNamed());
		File file = new File("csmaps.txt");
		if (file.exists())
			file.delete();
		else
			file.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		Cache.init();

		for (int i = 0; i < 10000; i++) {
			ClientScriptMap map = ClientScriptMap.getMap(i);
			if (map.getValues() == null)
				continue;
			writer.append(i + " - " + map.getValues().toString());
			writer.newLine();
			writer.flush();
			System.out.println("i: " + i + ", " + map.getValues().toString());
		}

		/*
		 * IComponentDefinitions[] inter =
		 * IComponentDefinitions.getInterface(1028); IComponentDefinitions i =
		 * inter[83]; //for(IComponentDefinitions i : inter) {
		 * System.out.println(i.type); //}
		 */
		/*
		 * long start = System.currentTimeMillis(); EntityList test = new
		 * EntityList(2048); for (int i = 0; i < 2047; i++) { test.add(new
		 * EntityTest()); } System.out.println(System.currentTimeMillis() -
		 * start + ", " + test.size()); for (int i2 = 0; i2 < 100; i2++) { for
		 * (int i = 0; i < 20000; i++) { for (Object t : test) {
		 * 
		 * } } } System.out.println(System.currentTimeMillis() - start); for
		 * (int i2 = 1; i2 < 2000; i2++) { for (int i = 1; i < 20000; i++) {
		 * test.get(i); } } System.out.println(System.currentTimeMillis() -
		 * start); for (Object t : test.toArray(new Object[test.size()])) {
		 * test.remove(t); }
		 */

		// System.out.println(System.currentTimeMillis() - start + ", "
		// + test.size());

		/*
		 * for(int i = 0; i < Utils.getInterfaceDefinitionsSize(); i++) {
		 * IComponentDefinitions[] inter =
		 * IComponentDefinitions.getInterface(i); for(int c = 0; c <
		 * inter.length; c++) {
		 * if(inter[c].aString4790.toLowerCase().contains("wave"))
		 * System.out.println(i+", "+c+", "+inter[c].aString4790); } }
		 */
	}

	public static int[] isModelType(int modelId) {
		if (!Cache.STORE.getIndexes()[7].archiveExists(modelId))
			return new int[] { -1 };
		for (int i = 0; i < Utils.getGraphicDefinitionsSize(); i++) {
			GraphicDefinitions def = GraphicDefinitions
					.getAnimationDefinitions(i);
			if (def.defaultModel == modelId || def.anInt1450 == modelId)
				return new int[] { 0, i };
		}
		for (int i = 0; i < Utils.getItemDefinitionsSize(); i++) {
			ItemDefinitions def = ItemDefinitions.getItemDefinitions(i);
			if (def.modelId == modelId
					|| def.getFemaleWornModelId1() == modelId
					|| def.getFemaleWornModelId2() == modelId
					|| def.getMaleWornModelId1() == modelId
					|| def.getMaleWornModelId1() == modelId)
				return new int[] { 1, i };
		}
		for (int i = 0; i < Utils.getNPCDefinitionsSize(); i++) {
			NPCDefinitions def = NPCDefinitions.getNPCDefinitions(i);
			for (int id : def.modelIds)
				if (id == modelId)
					return new int[] { 2, i };
		}
		for (int i = 0; i < Utils.getObjectDefinitionsSize(); i++) {
			ObjectDefinitions def = ObjectDefinitions.getObjectDefinitions(i);
			if (def.modelIds == null)
				continue;
			for (int ids[] : def.modelIds) {
				for (int id : ids) {
					if (id == modelId)
						return new int[] { 3, i };
				}
			}
		}
		return null;
	}

}
