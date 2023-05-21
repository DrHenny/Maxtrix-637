package com.rs.game.player.dialogues;

import java.util.HashMap;

import com.rs.game.player.dialogues.draynor.WiseOldMan;
import com.rs.game.player.dialogues.lumbridge.BobDialogue;
import com.rs.game.player.dialogues.lumbridge.BorderGuard;
import com.rs.game.player.dialogues.lumbridge.DoomSayer;
import com.rs.game.player.dialogues.lumbridge.FatherAereck;
import com.rs.game.player.dialogues.lumbridge.Frog;
import com.rs.game.player.dialogues.lumbridge.Hans;
import com.rs.game.player.dialogues.lumbridge.LumbridgeCook;
import com.rs.game.player.dialogues.lumbridge.LumbridgeMan;
import com.rs.game.player.dialogues.lumbridge.LumbridgeSage;
import com.rs.game.player.dialogues.lumbridge.Musician;
import com.rs.game.player.dialogues.lumbridge.RangedInstructor;
import com.rs.game.player.dialogues.lumbridge.SirVant;
import com.rs.game.player.dialogues.varrock.GrandExchangeTutor;

public final class DialogueHandler {

	private static final HashMap<Object, Class<Dialogue>> handledDialogues = new HashMap<Object, Class<Dialogue>>();

	@SuppressWarnings("unchecked")
	public static final void init() {
		try {
			Class<Dialogue> value79 = (Class<Dialogue>) Class
					.forName(Varnis.class.getCanonicalName());
			handledDialogues.put("Varnis", value79);
			Class<Dialogue> value1 = (Class<Dialogue>) Class
					.forName(LevelUp.class.getCanonicalName());
			handledDialogues.put("LevelUp", value1);

			Class<Dialogue> value57 = (Class<Dialogue>) Class
					.forName(Crate.class.getCanonicalName());
			handledDialogues.put("DungLad", value57);

			Class<Dialogue> value61 = (Class<Dialogue>) Class
					.forName(Crate.class.getCanonicalName());
			handledDialogues.put("Crate", value61);

			Class<Dialogue> value62 = (Class<Dialogue>) Class
					.forName(Pool.class.getCanonicalName());
			handledDialogues.put("Pool", value62);

			Class<Dialogue> value2 = (Class<Dialogue>) Class
					.forName(ZarosAltar.class.getCanonicalName());
			handledDialogues.put("ZarosAltar", value2);
			Class<Dialogue> value3 = (Class<Dialogue>) Class
					.forName(ClimbNoEmoteStairs.class.getCanonicalName());
			handledDialogues.put("ClimbNoEmoteStairs", value3);
			Class<Dialogue> value4 = (Class<Dialogue>) Class
					.forName(Banker.class.getCanonicalName());
			handledDialogues.put("Banker", value4);
			Class<Dialogue> value5 = (Class<Dialogue>) Class
					.forName(DestroyItemOption.class.getCanonicalName());
			handledDialogues.put("DestroyItemOption", value5);
			Class<Dialogue> value6 = (Class<Dialogue>) Class
					.forName(FremennikShipmaster.class.getCanonicalName());
			handledDialogues.put("FremennikShipmaster", value6);
			Class<Dialogue> value7 = (Class<Dialogue>) Class
					.forName(DungeonExit.class.getCanonicalName());
			handledDialogues.put("DungeonExit", value7);
			Class<Dialogue> value8 = (Class<Dialogue>) Class
					.forName(NexEntrance.class.getCanonicalName());
			handledDialogues.put("NexEntrance", value8);
			Class<Dialogue> value9 = (Class<Dialogue>) Class
					.forName(MagicPortal.class.getCanonicalName());
			handledDialogues.put("MagicPortal", value9);
			Class<Dialogue> value10 = (Class<Dialogue>) Class
					.forName(LunarAltar.class.getCanonicalName());
			handledDialogues.put("LunarAltar", value10);
			Class<Dialogue> value11 = (Class<Dialogue>) Class
					.forName(AncientAltar.class.getCanonicalName());
			handledDialogues.put("AncientAltar", value11);
			// TODO 12 and 13
			Class<Dialogue> value12 = (Class<Dialogue>) Class
					.forName(FletchingD.class.getCanonicalName());
			handledDialogues.put("FletchingD", value12);
			Class<Dialogue> value14 = (Class<Dialogue>) Class
					.forName(RuneScapeGuide.class.getCanonicalName());
			handledDialogues.put("RuneScapeGuide", value14);
			Class<Dialogue> value15 = (Class<Dialogue>) Class
					.forName(SurvivalExpert.class.getCanonicalName());
			handledDialogues.put("SurvivalExpert", value15);
			Class<Dialogue> value16 = (Class<Dialogue>) Class
					.forName(SimpleMessage.class.getCanonicalName());
			handledDialogues.put("SimpleMessage", value16);
			Class<Dialogue> value17 = (Class<Dialogue>) Class
					.forName(ItemMessage.class.getCanonicalName());
			handledDialogues.put("ItemMessage", value17);
			Class<Dialogue> value18 = (Class<Dialogue>) Class
					.forName(ClimbEmoteStairs.class.getCanonicalName());
			handledDialogues.put("ClimbEmoteStairs", value18);
			Class<Dialogue> value19 = (Class<Dialogue>) Class
					.forName(QuestGuide.class.getCanonicalName());
			handledDialogues.put("QuestGuide", value19);
			Class<Dialogue> value20 = (Class<Dialogue>) Class
					.forName(GemCuttingD.class.getCanonicalName());
			handledDialogues.put("GemCuttingD", value20);
			Class<Dialogue> value21 = (Class<Dialogue>) Class
					.forName(CookingD.class.getCanonicalName());
			handledDialogues.put("CookingD", value21);
			Class<Dialogue> value22 = (Class<Dialogue>) Class
					.forName(HerbloreD.class.getCanonicalName());
			handledDialogues.put("HerbloreD", value22);
			Class<Dialogue> value23 = (Class<Dialogue>) Class
					.forName(BarrowsD.class.getCanonicalName());
			handledDialogues.put("BarrowsD", value23);
			Class<Dialogue> value24 = (Class<Dialogue>) Class
					.forName(SmeltingD.class.getCanonicalName());
			handledDialogues.put("SmeltingD", value24);
			Class<Dialogue> value25 = (Class<Dialogue>) Class
					.forName(LeatherCraftingD.class.getCanonicalName());
			handledDialogues.put("LeatherCraftingD", value25);
			Class<Dialogue> value26 = (Class<Dialogue>) Class
					.forName(EnchantedGemDialouge.class.getCanonicalName());
			handledDialogues.put("EnchantedGemDialouge", value26);
			Class<Dialogue> value27 = (Class<Dialogue>) Class
					.forName(ForfeitDialouge.class.getCanonicalName());
			handledDialogues.put("ForfeitDialouge", value27);
			Class<Dialogue> value28 = (Class<Dialogue>) Class
					.forName(Transportation.class.getCanonicalName());
			handledDialogues.put("Transportation", value28);
			Class<Dialogue> value29 = (Class<Dialogue>) Class
					.forName(WildernessDitch.class.getCanonicalName());
			handledDialogues.put("WildernessDitch", value29);
			Class<Dialogue> value30 = (Class<Dialogue>) Class
					.forName(SimpleNPCMessage.class.getCanonicalName());
			handledDialogues.put("SimpleNPCMessage", value30);
			Class<Dialogue> value31 = (Class<Dialogue>) Class
					.forName(Transportation.class.getCanonicalName());
			handledDialogues.put("Transportation", value31);
			Class<Dialogue> value32 = (Class<Dialogue>) Class
					.forName(DTSpectateReq.class.getCanonicalName());
			handledDialogues.put("DTSpectateReq", value32);
			Class<Dialogue> value33 = (Class<Dialogue>) Class
					.forName(StrangeFace.class.getCanonicalName());
			handledDialogues.put("StrangeFace", value33);
			Class<Dialogue> value34 = (Class<Dialogue>) Class
					.forName(AncientEffigiesD.class.getCanonicalName());
			handledDialogues.put("AncientEffigiesD", value34);
			Class<Dialogue> value35 = (Class<Dialogue>) Class
					.forName(DTClaimRewards.class.getCanonicalName());
			handledDialogues.put("DTClaimRewards", value35);
			Class<Dialogue> value36 = (Class<Dialogue>) Class
					.forName(SetSkills.class.getCanonicalName());
			handledDialogues.put("SetSkills", value36);
			Class<Dialogue> value37 = (Class<Dialogue>) Class
					.forName(DismissD.class.getCanonicalName());
			handledDialogues.put("DismissD", value37);
			Class<Dialogue> value38 = (Class<Dialogue>) Class
					.forName(MrEx.class.getCanonicalName());
			handledDialogues.put("MrEx", value38);
			Class<Dialogue> value39 = (Class<Dialogue>) Class
					.forName(MakeOverMage.class.getCanonicalName());
			handledDialogues.put("MakeOverMage", value39);
			Class<Dialogue> value40 = (Class<Dialogue>) Class
					.forName(KaramjaTrip.class.getCanonicalName());
			handledDialogues.put("KaramjaTrip", value40);
			Class<Dialogue> value41 = (Class<Dialogue>) Class
					.forName(OzanD.class.getCanonicalName());
			handledDialogues.put("OzanD", value41);
			Class<Dialogue> value43 = (Class<Dialogue>) Class
					.forName(Lucien.class.getCanonicalName());
			handledDialogues.put("Lucien", value43);
			Class<Dialogue> value44 = (Class<Dialogue>) Class
					.forName(TeleportMinigame.class.getCanonicalName());
			handledDialogues.put("TeleportMinigame", value44);
			Class<Dialogue> value45 = (Class<Dialogue>) Class
					.forName(TeleportBosses.class.getCanonicalName());
			handledDialogues.put("TeleportBosses", value45);
			Class<Dialogue> value46 = (Class<Dialogue>) Class
					.forName(TeleportTraining.class.getCanonicalName());
			handledDialogues.put("TeleportTraining", value46);
			Class<Dialogue> value47 = (Class<Dialogue>) Class
					.forName(Turael.class.getCanonicalName());
			handledDialogues.put("Turael", value47);
			Class<Dialogue> value51 = (Class<Dialogue>) Class
					.forName(JadEnter.class.getCanonicalName());
			handledDialogues.put("JadEnter", value51);
			Class<Dialogue> value52 = (Class<Dialogue>) Class
					.forName(BorkEnter.class.getCanonicalName());
			handledDialogues.put("BorkEnter", value52);
			Class<Dialogue> value48 = (Class<Dialogue>) Class
					.forName(Veliaf.class.getCanonicalName());
			handledDialogues.put("Veliaf", value48);
			Class<Dialogue> value49 = (Class<Dialogue>) Class.forName(Max.class
					.getCanonicalName());
			handledDialogues.put("Max", value49);
			Class<Dialogue> value54 = (Class<Dialogue>) Class
					.forName(RoyalGuard.class.getCanonicalName());
			handledDialogues.put("RoyalGuard", value54);

			Class<Dialogue> value55 = (Class<Dialogue>) Class
					.forName(XPRate.class.getCanonicalName());
			handledDialogues.put("XPRate", value55);

			Class<Dialogue> value56 = (Class<Dialogue>) Class
					.forName(BobDialogue.class.getCanonicalName());
			handledDialogues.put("BobDialogue", value56);

			Class<Dialogue> value58 = (Class<Dialogue>) Class
					.forName(LumbridgeSage.class.getCanonicalName());
			handledDialogues.put("LumbridgeSage", value58);

			Class<Dialogue> value59 = (Class<Dialogue>) Class
					.forName(FatherAereck.class.getCanonicalName());
			handledDialogues.put("FatherAereck", value59);

			Class<Dialogue> value60 = (Class<Dialogue>) Class
					.forName(LumbridgeMan.class.getCanonicalName());
			handledDialogues.put("LumbridgeMan", value60);

			Class<Dialogue> value64 = (Class<Dialogue>) Class
					.forName(DoomSayer.class.getCanonicalName());
			handledDialogues.put("DoomSayer", value64);
			
			Class<Dialogue> value65 = (Class<Dialogue>) Class
					.forName(WiseOldMan.class.getCanonicalName());
			handledDialogues.put("WiseOldMan", value65);

			Class<Dialogue> value63 = (Class<Dialogue>) Class
					.forName(RangedInstructor.class.getCanonicalName());
			handledDialogues.put("RangedInstructor", value63);

			Class<Dialogue> value66 = (Class<Dialogue>) Class
					.forName(Musician.class.getCanonicalName());
			handledDialogues.put("Musician", value66);


			Class<Dialogue> value68 = (Class<Dialogue>) Class
					.forName(GrandExchangeTutor.class.getCanonicalName());
			handledDialogues.put("GrandExchangeTutor", value68);

			Class<Dialogue> value67 = (Class<Dialogue>) Class
					.forName(LumbridgeCook.class.getCanonicalName());
			handledDialogues.put("LumbridgeCook", value67);

			Class<Dialogue> value69 = (Class<Dialogue>) Class
					.forName(Hans.class.getCanonicalName());
			handledDialogues.put("Hans", value69);

			Class<Dialogue> value70 = (Class<Dialogue>) Class
					.forName(Frog.class.getCanonicalName());
			handledDialogues.put("Frog", value70);

			Class<Dialogue> value72 = (Class<Dialogue>) Class
					.forName(SirVant.class.getCanonicalName());
			handledDialogues.put("SirVant", value72);

			Class<Dialogue> value71 = (Class<Dialogue>) Class
					.forName(BorderGuard.class.getCanonicalName());
			handledDialogues.put("BorderGuard", value71);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static final void reload() {
		handledDialogues.clear();
		init();
	}

	public static final Dialogue getDialogue(Object key) {
		if (key instanceof Dialogue)
			return (Dialogue) key;
		Class<Dialogue> classD = handledDialogues.get(key);
		if (classD == null)
			return null;
		try {
			return classD.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	private DialogueHandler() {

	}
}
