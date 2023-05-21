package com.rs.game.player.dialogues;

import com.rs.Settings;
import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.player.controlers.LumbiTutorial;

/**
 * Ozan. Starter Tutorial NPC.
 * 
 * @author Raghav
 * 
 */
public class OzanD extends Dialogue {

	private int npcId, questStage;

	@Override
	public void start() {
		npcId = (int) parameters[0];
		questStage = (int) parameters[1];
		switch (questStage) {
		case 0:
			sendEntityDialogue(Dialogue.SEND_3_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Hello, " + player.getDisplayName() + "!",
					"Welcome to " + Settings.SERVER_NAME + ". I'm Ozan.",
					"I'm here to guide you." }, IS_NPC, npcId, 9847);
			break;
		case 1:
			sendEntityDialogue(
					SEND_NO_CONTINUE_1_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"This is Duel Arena. You can challenge other players here." },
					IS_NPC, npcId, 9848);
			break;
		case 2:
			sendEntityDialogue(
					SEND_NO_CONTINUE_1_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"This is Castle Wars. This is a player-versus-player minigame." },
					IS_NPC, npcId, 9849);
			break;
		case 3:
			sendEntityDialogue(
					SEND_NO_CONTINUE_1_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"This is Dominon Tower. You can fight various bosses here." },
					IS_NPC, npcId, 9850);
			break;
		case 4:
			sendEntityDialogue(SEND_NO_CONTINUE_2_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"This is Barrows. You can fight Barrows brothers here",
					"to get reward from their chest." }, IS_NPC, npcId, 9827);
			break;
		case 5:
			sendEntityDialogue(SEND_NO_CONTINUE_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"and we're back..." }, IS_NPC, npcId, 9827);
			break;
		case 6:
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"We're done, " + player.getDisplayName()
									+ ". Have fun playing." }, IS_NPC, npcId,
					9847);
			stage = 3;
			break;
		}
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
		case -1:
			stage = 0;
			sendEntityDialogue(Dialogue.SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"Hello, Ozan. I'm new here. Please guide me?" },
					IS_PLAYER, player.getIndex(), 9827);
			break;
		case 0:
			stage = 1;
			sendEntityDialogue(Dialogue.SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Alright, let's start. Take this teletab and break it." },
					IS_NPC, npcId, 9810);
			break;
		case 1:
			sendEntityDialogue(Dialogue.SEND_NO_CONTINUE_1_TEXT_CHAT,
					new String[] { "", "Ozan gave you a teletab." }, IS_ITEM,
					LumbiTutorial.TELETAB, 1);
			player.getPackets().sendIComponentText(372, 3,
					"Break the tele tab.");
			player.getInventory().addItem(LumbiTutorial.TELETAB, 1);
			break;
		case 3:
			stage = 4;
			sendEntityDialogue(Dialogue.SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"Thanks a lot, ozan." }, IS_PLAYER,
					player.getIndex(), 9846);
			break;
		case 4:
			stage = 5;
			sendEntityDialogue(Dialogue.SEND_1_TEXT_CHAT, new String[] {
					NPCDefinitions.getNPCDefinitions(npcId).name,
					"Your welcome. Take your starter stuff." }, IS_NPC, npcId,
					9848);
			break;
		case 5:
			player.getControlerManager().getControler().forceClose();
			end();
			break;
		default:
			end();
			break;
		}
	}

	@Override
	public void finish() {

	}

}
