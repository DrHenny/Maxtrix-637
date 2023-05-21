package com.rs.cache.loaders;

import com.alex.utils.Constants;
import com.rs.cache.Cache;
import com.rs.game.player.Equipment;
import com.rs.game.player.Skills;
import com.rs.io.InputStream;
import com.rs.utils.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

@SuppressWarnings("unused")
public final class ItemDefinitions {

	public static final ItemDefinitions[] itemsDefinitions;

	static { // that's why this is here
		itemsDefinitions = new ItemDefinitions[Utils.getItemDefinitionsSize()];
	}

	public int id;
	private boolean loaded;

	public int modelId;
	public String name;

	// model size information
	private int modelZoom;
	private int modelRotation1;
	private int modelRotation2;
	private int modelOffset1;
	private int modelOffset2;

	// extra information
	private int stackable;
	private static int value;
	private static int tempValue;
	private boolean membersOnly;

	// wearing model information
	private int maleEquip1;
	private int femaleEquip1;
	private int maleEquip2;
	private int femaleEquip2;

	// options
	private String[] groundOptions;
	public String[] inventoryOptions;

	// model information
	public int[] originalModelColors;
	public int[] modifiedModelColors;
	public short[] originalTextureColors;
	private short[] modifiedTextureColors;
	private byte[] unknownArray1;
	private int[] unknownArray2;
	// extra information, not used for newer items
	private boolean unnoted;

	private int maleEquipModelId3;
	private int femaleEquipModelId3;
	private int unknownInt1;
	private int unknownInt2;
	private int unknownInt3;
	private int unknownInt4;
	private int unknownInt5;
	private int unknownInt6;
	private int certId;
	private int certTemplateId;
	private int[] stackIds;
	private int[] stackAmounts;
	private int unknownInt7;
	private int unknownInt8;
	private int unknownInt9;
	private int unknownInt10;
	private int unknownInt11;
	private int teamId;
	private int lendId;
	private int lendTemplateId;
	private int unknownInt12;
	private int unknownInt13;
	private int unknownInt14;
	private int unknownInt15;
	private int unknownInt16;
	private int unknownInt17;
	private int unknownInt18;
	private int unknownInt19;
	private int unknownInt20;
	private int unknownInt21;
	private int unknownInt22;
	private int unknownInt23;

	// extra added
	private boolean noted;
	private boolean lended;

	private HashMap<Integer, Object> clientScriptData;
	private HashMap<Integer, Integer> itemRequiriments;
	private int grandExchangePrice;

	public static final ItemDefinitions getItemDefinitions(int itemId) {
		if (itemId < 0 || itemId >= itemsDefinitions.length)
			itemId = 0;
		ItemDefinitions def = itemsDefinitions[itemId];
		if (def == null)
			itemsDefinitions[itemId] = def = new ItemDefinitions(itemId);
		return def;

	}

	public static final void clearItemsDefinitions() {
		for (int i = 0; i < itemsDefinitions.length; i++)
			itemsDefinitions[i] = null;
	}

	public ItemDefinitions(int id) {
		this.id = id;
		setDefaultsVariableValues();
		setDefaultOptions();
		loadItemDefinitions();
	}

	public static boolean isInteger(String i) {
		try {
			Integer.parseInt(i);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	public static int loadExchangePrices(int itemId) {
		try (BufferedReader buf = new BufferedReader(new FileReader(
				                                                           "./data/displays.txt"))) {
			String line;
			while ((line = buf.readLine()) != null) {

				String[] regex = line.split(",");
				if (line.startsWith(String.valueOf("ItemID: " + itemId))) {
					int itemValue = Integer.valueOf(regex[1].replace(
							" Value: ", ""));
					System.out.println("ITEMID: " + itemId + " PRICE: "
							                   + itemValue);
					return itemValue;
				}
			}
			buf.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public boolean isLoaded() {
		return loaded;
	}

	private final void loadItemDefinitions() {
		byte[] data = Cache.STORE.getIndexes()[Constants.ITEM_DEFINITIONS_INDEX]
				              .getFile(getArchiveId(), getFileId());
		if (data == null) {
			// System.out.println("Failed loading Item " + id+".");
			return;
		}
		readOpcodeValues(new InputStream(data));
		if (certTemplateId != -1)
			toNote();
		if (lendTemplateId != -1)
			toLend();
		if (unknownValue1 != -1)
			toLendBind();
		loaded = true;
	}

	private void toNote() {
		// ItemDefinitions noteItem; //certTemplateId
		ItemDefinitions realItem = getItemDefinitions(certId);
		membersOnly = realItem.membersOnly;
		value = realItem.value;
		name = realItem.name;
		stackable = 1;
		noted = true;
	}

	public static ItemDefinitions forName(String name) {
		for (ItemDefinitions definition : itemsDefinitions) {
			if (definition.name.equalsIgnoreCase(name)) {
				return definition;
			}
		}
		return null;
	}

	private void toLendBind() {
		// ItemDefinitions lendItem; //lendTemplateId
		ItemDefinitions realItem = getItemDefinitions(unknownValue2);
		originalModelColors = realItem.originalModelColors;
		maleEquipModelId3 = realItem.maleEquipModelId3;
		femaleEquipModelId3 = realItem.femaleEquipModelId3;
		teamId = realItem.teamId;
		value = 0;
		membersOnly = realItem.membersOnly;
		name = realItem.name;
		inventoryOptions = new String[5];
		groundOptions = realItem.groundOptions;
		if (realItem.inventoryOptions != null)
			for (int optionIndex = 0; optionIndex < 4; optionIndex++)
				inventoryOptions[optionIndex] = realItem.inventoryOptions[optionIndex];
		inventoryOptions[4] = "Discard";
		maleEquip1 = realItem.maleEquip1;
		maleEquip2 = realItem.maleEquip2;
		femaleEquip1 = realItem.femaleEquip1;
		femaleEquip2 = realItem.femaleEquip2;
		clientScriptData = realItem.clientScriptData;
		lended = true;
	}

	public void setValue(int value) {
		this.value = value;
	}

	private void toLend() {
		ItemDefinitions lendItem; // lendTemplateId
		ItemDefinitions realItem = getItemDefinitions(lendId);
		originalModelColors = realItem.originalModelColors;
		maleEquipModelId3 = realItem.maleEquipModelId3;
		femaleEquipModelId3 = realItem.femaleEquipModelId3;
		teamId = realItem.teamId;
		value = 0;
		membersOnly = realItem.membersOnly;
		name = realItem.name;
		inventoryOptions = new String[5];
		groundOptions = realItem.groundOptions;
		if (realItem.inventoryOptions != null)
			for (int optionIndex = 0; optionIndex < 4; optionIndex++)
				inventoryOptions[optionIndex] = realItem.inventoryOptions[optionIndex];
		inventoryOptions[4] = "Discard";
		maleEquip1 = realItem.maleEquip1;
		maleEquip2 = realItem.maleEquip2;
		femaleEquip1 = realItem.femaleEquip1;
		femaleEquip2 = realItem.femaleEquip2;
		clientScriptData = realItem.clientScriptData;
		lended = true;
	}

	public int getArchiveId() {
		return id >>> 8;
	}

	public int getFileId() {
		return 0xff & id;
	}

	public boolean isDestroyItem() {
		if (inventoryOptions == null)
			return false;
		for (String option : inventoryOptions) {
			if (option == null)
				continue;
			if (option.equalsIgnoreCase("destroy"))
				return true;
		}
		return false;
	}

	public boolean isWearItem() {
		if (inventoryOptions == null)
			return false;
		for (String option : inventoryOptions) {
			if (option == null)
				continue;
			if (option.equalsIgnoreCase("wield")
					    || option.equalsIgnoreCase("wear")
					    || option.equalsIgnoreCase("equip"))
				return true;
		}
		return false;
	}

	public boolean isWearItem(boolean male) {
		if (inventoryOptions == null)
			return false;
		if (Equipment.getItemSlot(id) != Equipment.SLOT_RING
				    && Equipment.getItemSlot(id) != Equipment.SLOT_ARROWS
				    && Equipment.getItemSlot(id) != Equipment.SLOT_AURA
				    && (male ? getMaleWornModelId1() == -1
				             : getFemaleWornModelId1() == -1))
			return false;
		for (String option : inventoryOptions) {
			if (option == null)
				continue;
			if (option.equalsIgnoreCase("wield")
					    || option.equalsIgnoreCase("wear")
					    || option.equalsIgnoreCase("equip"))
				return true;
		}
		return false;
	}

	public boolean hasSpecialBar() {
		if (clientScriptData == null)
			return false;
		Object specialBar = clientScriptData.get(686);
		if (specialBar != null && specialBar instanceof Integer)
			return (Integer) specialBar == 1;
		return false;
	}

	public int getRenderAnimId() {
		if (clientScriptData == null)
			return 1426;
		Object animId = clientScriptData.get(644);
		if (animId != null && animId instanceof Integer)
			return (Integer) animId;
		return 1426;
	}

	public int getQuestId() {
		if (clientScriptData == null)
			return -1;
		Object questId = clientScriptData.get(861);
		if (questId != null && questId instanceof Integer)
			return (Integer) questId;
		return -1;
	}

	public HashMap<Integer, Integer> getCreateItemRequirements() {
		if (clientScriptData == null)
			return null;
		HashMap<Integer, Integer> items = new HashMap<Integer, Integer>();
		int requiredId = -1;
		int requiredAmount = -1;
		for (int key : clientScriptData.keySet()) {
			Object value = clientScriptData.get(key);
			if (value instanceof String)
				continue;
			if (key >= 538 && key <= 770) {
				if (key % 2 == 0)
					requiredId = (Integer) value;
				else
					requiredAmount = (Integer) value;
				if (requiredId != -1 && requiredAmount != -1) {
					items.put(requiredAmount, requiredId);
					requiredId = -1;
					requiredAmount = -1;
				}
			}
		}
		return items;
	}

	public HashMap<Integer, Object> getClientScriptData() {
		return clientScriptData;
	}

	public HashMap<Integer, Integer> getWearingSkillRequirements() {
		return getWearingSkillRequiriments();
	}

	public HashMap<Integer, Integer> getWearingSkillRequiriments() {
		if (clientScriptData == null)
			return null;
		if (itemRequiriments == null) {
			HashMap<Integer, Integer> skills = new HashMap<Integer, Integer>();
			for (int i = 0; i < 10; i++) {
				Integer skill = (Integer) clientScriptData.get(749 + (i * 2));
				if (skill != null) {
					Integer level = (Integer) clientScriptData
							                          .get(750 + (i * 2));
					if (level != null)
						skills.put(skill, level);
				}
			}
			Integer maxedSkill = (Integer) clientScriptData.get(277);
			if (maxedSkill != null)
				skills.put(maxedSkill, id == 19709 ? 120 : 99);
			itemRequiriments = skills;
			if (id == 7462)
				itemRequiriments.put(Skills.DEFENCE, 40);
			else if (name.equals("Dragon defender")) {
				itemRequiriments.put(Skills.ATTACK, 60);
				itemRequiriments.put(Skills.DEFENCE, 60);
			}
			if (name.equals("Completionist cape")) {
				itemRequiriments.put(Skills.DEFENCE, 99);
				itemRequiriments.put(Skills.ATTACK, 99);
				itemRequiriments.put(Skills.STRENGTH, 99);
				itemRequiriments.put(Skills.CRAFTING, 99);
				itemRequiriments.put(Skills.HERBLORE, 99);
				itemRequiriments.put(Skills.THIEVING, 99);
				itemRequiriments.put(Skills.HITPOINTS, 99);
				itemRequiriments.put(Skills.RANGE, 99);
				itemRequiriments.put(Skills.PRAYER, 99);
				itemRequiriments.put(Skills.MAGIC, 99);
				itemRequiriments.put(Skills.COOKING, 99);
				itemRequiriments.put(Skills.WOODCUTTING, 99);
				itemRequiriments.put(Skills.FLETCHING, 99);
				itemRequiriments.put(Skills.FIREMAKING, 99);
				itemRequiriments.put(Skills.SMITHING, 99);
				itemRequiriments.put(Skills.MINING, 99);
				itemRequiriments.put(Skills.AGILITY, 99);
			}
		}

		return itemRequiriments;
	}

	/*
	 * public HashMap<Integer, Integer> getWearingSkillRequiriments() { if
	 * (clientScriptData == null) return null; HashMap<Integer, Integer> skills
	 * = new HashMap<Integer, Integer>(); int nextLevel = -1; int nextSkill =
	 * -1; for (int key : clientScriptData.keySet()) { Object value =
	 * clientScriptData.get(key); if (value instanceof String) continue; if(key
	 * == 277) { skills.put((Integer) value, id == 19709 ? 120 : 99); }else if
	 * (key == 23 && id == 15241) { skills.put(4, (Integer) value);
	 * skills.put(11, 61); } else if (key >= 749 && key < 797) { if (key % 2 ==
	 * 0) nextLevel = (Integer) value; else nextSkill = (Integer) value; if
	 * (nextLevel != -1 && nextSkill != -1) { skills.put(nextSkill, nextLevel);
	 * nextLevel = -1; nextSkill = -1; } }
	 *
	 * } return skills; }
	 */

	private void setDefaultOptions() {
		groundOptions = new String[] { null, null, "take", null, null };
		inventoryOptions = new String[] { null, null, null, null, "drop" };
	}

	private void setDefaultsVariableValues() {
		name = "null";
		maleEquip1 = -1;
		maleEquip2 = -1;
		femaleEquip1 = -1;
		femaleEquip2 = -1;
		modelZoom = 2000;
		lendId = -1;
		lendTemplateId = -1;
		certId = -1;
		certTemplateId = -1;
		unknownInt9 = 128;
		value = 1;
		maleEquipModelId3 = -1;
		femaleEquipModelId3 = -1;
		unknownValue1 = -1;
		unknownValue2 = -1;

	}

	private final void readValues(InputStream stream, int opcode) {
		if (opcode == 1)
			modelId = stream.readUnsignedShort();//.readBigSmart();
		else if (opcode == 2)
			name = stream.readString();
		else if (opcode == 4)
			modelZoom = stream.readUnsignedShort();
		else if (opcode == 5)
			modelRotation1 = stream.readUnsignedShort();
		else if (opcode == 6)
			modelRotation2 = stream.readUnsignedShort();
		else if (opcode == 7) {
			modelOffset1 = stream.readUnsignedShort();
			if (modelOffset1 > 32767)
				modelOffset1 -= 65536;
			modelOffset1 <<= 0;
		} else if (opcode == 8) {
			modelOffset2 = stream.readUnsignedShort();
			if (modelOffset2 > 32767)
				modelOffset2 -= 65536;
			modelOffset2 <<= 0;
		} else if (opcode == 11)
			stackable = 1;
		else if (opcode == 12)
			value = stream.readInt();
		else if (opcode == 117)
			opcode117 = stream.readUnsignedByte();
		else if (opcode == 82)
			opcode82 = stream.readUnsignedByte();
		else if (opcode == 13)
			opcode13 = stream.readUnsignedByte();
		else if (opcode == 14)
			opcode14 = stream.readUnsignedByte();
		else if (opcode == 9)
			opcode9 = stream.readUnsignedByte();
		else if (opcode == 27)
			opcode27 = stream.readUnsignedByte();
		else if (opcode == 66)
			opcode66 = stream.readUnsignedByte();
		else if (opcode == 116)
			opcode116 = stream.readUnsignedByte();
		else if (opcode == 157)
			opcode157 = stream.readUnsignedByte();
		else if (opcode == 244)
			opcode244 = stream.readUnsignedByte();
		else if (opcode == 170)
			opcode170 = stream.readUnsignedByte();
		else if (opcode == 151)
			opcode151 = stream.readUnsignedByte();// 14 66 116 157 244 170 151 9
			// 27
		else if (opcode == 16)
			membersOnly = true;
		else if (opcode == 18) // added
			stream.readUnsignedShort();
		else if (opcode == 23)
			maleEquip1 = stream.readUnsignedShort();
		else if (opcode == 24)
			maleEquip2 = stream.readUnsignedShort();
		else if (opcode == 25)
			femaleEquip1 = stream.readUnsignedShort();
		else if (opcode == 26)
			femaleEquip2 = stream.readUnsignedShort();
		else if (opcode >= 30 && opcode < 35)
			groundOptions[opcode - 30] = stream.readString();
		else if (opcode >= 35 && opcode < 40)
			inventoryOptions[opcode - 35] = stream.readString();
		else if (opcode == 40) {
			int length = stream.readUnsignedByte();
			originalModelColors = new int[length];
			modifiedModelColors = new int[length];
			for (int index = 0; index < length; index++) {
				originalModelColors[index] = stream.readUnsignedShort();
				modifiedModelColors[index] = stream.readUnsignedShort();
			}
		} else if (opcode == 41) {
			int length = stream.readUnsignedByte();
			originalTextureColors = new short[length];
			modifiedTextureColors = new short[length];
			for (int index = 0; index < length; index++) {
				originalTextureColors[index] = (short) stream
						                                       .readUnsignedShort();
				modifiedTextureColors[index] = (short) stream
						                                       .readUnsignedShort();
			}
		} else if (opcode == 42) {
			int length = stream.readUnsignedByte();
			unknownArray1 = new byte[length];
			for (int index = 0; index < length; index++)
				unknownArray1[index] = (byte) stream.readByte();
		} else if (opcode == 65)
			unnoted = true;
		else if (opcode == 78)
			maleEquipModelId3 = stream.readUnsignedShort();
		else if (opcode == 79)
			femaleEquipModelId3 = stream.readUnsignedShort();
		else if (opcode == 90)
			unknownInt1 = stream.readUnsignedShort();
		else if (opcode == 91)
			unknownInt2 = stream.readUnsignedShort();
		else if (opcode == 92)
			unknownInt3 = stream.readUnsignedShort();
		else if (opcode == 93)
			unknownInt4 = stream.readUnsignedShort();
		else if (opcode == 95)
			unknownInt5 = stream.readUnsignedShort();
		else if (opcode == 96)
			unknownInt6 = stream.readUnsignedByte();
		else if (opcode == 97)
			certId = stream.readUnsignedShort();
		else if (opcode == 98)
			certTemplateId = stream.readUnsignedShort();
		else if (opcode >= 100 && opcode < 110) {
			if (stackIds == null) {
				stackIds = new int[10];
				stackAmounts = new int[10];
			}
			stackIds[opcode - 100] = stream.readUnsignedShort();
			stackAmounts[opcode - 100] = stream.readUnsignedShort();
		} else if (opcode == 110)
			unknownInt7 = stream.readUnsignedShort();
		else if (opcode == 111)
			unknownInt8 = stream.readUnsignedShort();
		else if (opcode == 112)
			unknownInt9 = stream.readUnsignedShort();
		else if (opcode == 113)
			unknownInt10 = stream.readByte();
		else if (opcode == 114)
			unknownInt11 = stream.readByte() * 5;
		else if (opcode == 115)
			teamId = stream.readUnsignedByte();
		else if (opcode == 121)
			lendId = stream.readUnsignedShort();
		else if (opcode == 122)
			lendTemplateId = stream.readUnsignedShort();
		else if (opcode == 125) {
			unknownInt12 = stream.readByte() << 0;
			unknownInt13 = stream.readByte() << 0;
			unknownInt14 = stream.readByte() << 0;
		} else if (opcode == 126) {
			unknownInt15 = stream.readByte() << 0;
			unknownInt16 = stream.readByte() << 0;
			unknownInt17 = stream.readByte() << 0;
		} else if (opcode == 127) {
			unknownInt18 = stream.readUnsignedByte();
			unknownInt19 = stream.readUnsignedShort();
		} else if (opcode == 128) {
			unknownInt20 = stream.readUnsignedByte();
			unknownInt21 = stream.readUnsignedShort();
		} else if (opcode == 129) {
			unknownInt20 = stream.readUnsignedByte();
			unknownInt21 = stream.readUnsignedShort();
		} else if (opcode == 130) {
			unknownInt22 = stream.readUnsignedByte();
			unknownInt23 = stream.readUnsignedShort();
		} else if (opcode == 132) {
			int length = stream.readUnsignedByte();
			unknownArray2 = new int[length];
			for (int index = 0; index < length; index++)
				unknownArray2[index] = stream.readUnsignedShort();
		} else if (opcode == 134) {
			int unknownValue = stream.readUnsignedByte();
		} else if (opcode == 139) {
			unknownValue2 = stream.readUnsignedShort();
		} else if (opcode == 140) {
			unknownValue1 = stream.readUnsignedShort();
		} else if (opcode == 249) {
			int length = stream.readUnsignedByte();
			if (clientScriptData == null)
				clientScriptData = new HashMap<Integer, Object>(length);
			for (int index = 0; index < length; index++) {
				boolean stringInstance = stream.readUnsignedByte() == 1;
				int key = stream.read24BitInt();
				Object value = stringInstance ? stream.readString() : stream
						                                                      .readInt();
				clientScriptData.put(key, value);
			}
		} else if (opcode == 17) {
			stream.readUnsignedByte();
		} else { System.out.println("MISSING OPCODE " + opcode + " FOR ITEM " + id); }
	}

	private int opcode13;
	private int opcode82;
	private int opcode117;
	private int opcode66;
	private int opcode116;
	private int opcode157;
	private int opcode244;
	private int opcode170;
	private int opcode151;
	private int opcode14;
	private int opcode27;
	private int opcode9;
	private int unknownValue1;
	private int unknownValue2;
	private static BufferedReader reader;

	private final void readOpcodeValues(InputStream stream) {
		while (true) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			readValues(stream, opcode);
		}
	}

	public String getName() {
		return name;
	}

	public int getFemaleWornModelId1() {
		return femaleEquip1;
	}

	public int getFemaleWornModelId2() {
		return femaleEquip2;
	}

	public int getMaleWornModelId1() {
		return maleEquip1;
	}

	public int getMaleWornModelId2() {
		return maleEquip2;
	}

	public boolean isLended() {
		return lended;
	}

	public boolean isStackable() {
		return stackable == 1;
	}

	public boolean isNoted() {
		return noted;
	}

	public int getLendId() {
		return lendId;
	}

	public int getCertId() {
		return certId;
	}

	public int getValue(int itemId) {
		return loadExchangePrices(itemId);
	}

}