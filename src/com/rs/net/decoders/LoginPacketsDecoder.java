package com.rs.net.decoders;

import com.rs.Settings;
import com.rs.cache.Cache;
import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.io.AntiFlood;
import com.rs.io.InputStream;
import com.rs.net.Session;
import com.rs.utils.Logger;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.Utils;

public final class LoginPacketsDecoder extends Decoder {

	public LoginPacketsDecoder(Session session) {
		super(session);
	}

	@Override
	public void decode(InputStream stream) {
		session.setDecoder(-1);
		int packetId = stream.readUnsignedByte();
		if (packetId == 19)
			decodeLobbyLogin(stream);
		else if (packetId == 16) // 16 world login
			decodeWorldLogin(stream);
		else {
			if (Settings.DEBUG)
				Logger.log(this, "PacketId " + packetId);
			session.getChannel().close();
		}
	}

	private void decodeLobbyLogin(InputStream buffer) {

		int clientRev = buffer.readInt();
		int rsaBlockSize = buffer.readShort(); // RSA block size
		int rsaHeaderKey = buffer.readByte(); // RSA header key
		System.out.println(" " + rsaBlockSize + " " + rsaHeaderKey + " "
				+ clientRev);

		int[] loginKeys = new int[4];
		for (int data = 0; data < 4; data++) {
			loginKeys[data] = buffer.readInt();
		}
		buffer.readLong();
		String pass = buffer.readString();
		@SuppressWarnings("unused")
		long serverSeed = buffer.readLong();
		@SuppressWarnings("unused")
		long clientSeed = buffer.readLong();

		buffer.decodeXTEA(loginKeys, buffer.getOffset(), buffer.getLength());
		String name = buffer.readString();
		boolean isHD = buffer.readByte() == 1;
		boolean isResizable = buffer.readByte() == 1;
		System.out.println("  Is resizable? " + isResizable);
		for (int i = 0; i < 24; i++)
			buffer.readByte();
		String settings = buffer.readString();

		@SuppressWarnings("unused")
		int unknown = buffer.readInt();
		int[] crcValues = new int[35];
		for (int i = 0; i < crcValues.length; i++)
			crcValues[i] = buffer.readInt();
		System.out.println(name + ", " + pass);
		Player player;

		if (!SerializableFilesManager.containsPlayer(name)) {
			player = new Player(name);
			player.setPassword(pass);

			// session.getLoginPackets().sendClientPacket(2);
			// return;
		} else {
			player = SerializableFilesManager.loadPlayer(name);
			if (player == null) {
				session.getLoginPackets().sendClientPacket(20);
				return;
			}
			if (!player.getPassword().equals(pass)) {
				session.getLoginPackets().sendClientPacket(3);
				return;
			}
		}
		if (player.isPermBanned()
				|| (player.getBanned() > System.currentTimeMillis()))
			session.getLoginPackets().sendClientPacket(4);
		else {
			player.init(session, name);
			session.getLoginPackets().sendLobbyDetails(player);// sendLoginDetails(player);
			session.setDecoder(3, player);
			session.setEncoder(2, player);
			SerializableFilesManager.savePlayer(player);

			// player.start();
		}
	}

	public void decodeWorldLogin(InputStream stream) {
		if (World.exiting_start != 0) {
			session.getLoginPackets().sendClientPacket(14);
			return;
		}
		int packetSize = stream.readUnsignedShort();
		if (packetSize != stream.getRemaining()) {
			session.getChannel().close();
			return;
		}
		if (stream.readInt() != Settings.CLIENT_BUILD
				|| stream.readInt() != Settings.CUSTOM_CLIENT_BUILD) {
			session.getLoginPackets().sendClientPacket(6);
			return;
		}
		@SuppressWarnings("unused")
		boolean unknownEquals14 = stream.readUnsignedByte() == 1;
		if (stream.readUnsignedByte() != 10) { // rsa block check
			session.getLoginPackets().sendClientPacket(10);
			return;
		}
		int[] isaacKeys = new int[4];
		for (int i = 0; i < isaacKeys.length; i++)
			isaacKeys[i] = stream.readInt();
		if (stream.readLong() != 0L) { // rsa block check, pass part
			session.getLoginPackets().sendClientPacket(10);
			return;
		}
		String password = stream.readString();
		@SuppressWarnings("unused")
		String unknown = Utils.longToString(stream.readLong());
		stream.readLong(); // random value
		stream.decodeXTEA(isaacKeys, stream.getOffset(), stream.getLength());
		String username = Utils
				.formatPlayerNameForProtocol(stream.readString());
		stream.readUnsignedByte(); // unknown
		int displayMode = stream.readUnsignedByte();
		int screenWidth = stream.readUnsignedShort();
		int screenHeight = stream.readUnsignedShort();
		@SuppressWarnings("unused")
		int unknown2 = stream.readUnsignedByte();
		stream.skip(24); // 24bytes directly from a file, no idea whats there
		@SuppressWarnings("unused")
		String settings = stream.readString();
		@SuppressWarnings("unused")
		int affid = stream.readInt();
		stream.skip(stream.readUnsignedByte()); // useless settings
		if (stream.readUnsignedByte() != 5) {
			session.getLoginPackets().sendClientPacket(10);
			return;
		}
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readUnsignedShort();
		stream.readUnsignedByte();
		stream.read24BitInt();
		stream.readUnsignedShort();
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readJagString();
		stream.readJagString();
		stream.readJagString();
		stream.readJagString();
		stream.readUnsignedByte();
		stream.readUnsignedShort();
		@SuppressWarnings("unused")
		int unknown3 = stream.readInt();
		@SuppressWarnings("unused")
		long userFlow = stream.readLong();
		boolean hasAditionalInformation = stream.readUnsignedByte() == 1;
		if (hasAditionalInformation)
			stream.readString(); // aditionalInformation
		@SuppressWarnings("unused")
		boolean hasJagtheora = stream.readUnsignedByte() == 1;
		@SuppressWarnings("unused")
		boolean js = stream.readUnsignedByte() == 1;
		@SuppressWarnings("unused")
		boolean hc = stream.readUnsignedByte() == 1;
		for (int index = 0; index < Cache.STORE.getIndexes().length; index++) {
			int crc = Cache.STORE.getIndexes()[index] == null ? 0 : Cache.STORE
					.getIndexes()[index].getCRC();
			int receivedCRC = stream.readInt();
			if (crc != receivedCRC && index < 32) {
				// Logger.log(this,
				// "Invalid CRC at index: "+index+", "+receivedCRC+", "+crc);
				session.getLoginPackets().sendClientPacket(6);
				return;
			}
		}
		// invalid chars
		if (username.length() <= 1 || username.length() >= 15
				|| username.contains("?") || username.contains(":")
				|| username.startsWith(" ") || username.endsWith(" ")
				|| username.contains("  ") || username.endsWith("_")
				|| username.endsWith("  ") || username.endsWith("<")
				|| username.contains("/") || username.contains("\\")
				|| username.contains("*") || username.contains("\"")) {
			session.getLoginPackets().sendClientPacket(3);
			return;
		}
		if (World.getPlayers().size() >= Settings.PLAYERS_LIMIT - 10) {
			session.getLoginPackets().sendClientPacket(7);
			return;
		}
		if (World.containsPlayer(username)) {
			session.getLoginPackets().sendClientPacket(5);
			return;
		}
		if (AntiFlood.getSessionsIP(session.getIP()) > 3) {
			session.getLoginPackets().sendClientPacket(9);
			return;
		}
		Player player;
		if (!SerializableFilesManager.containsPlayer(username))
			player = new Player(password);
		else {
			player = SerializableFilesManager.loadPlayer(username);
			if (player == null) {
				session.getLoginPackets().sendClientPacket(20);
				return;
			}
			if (!SerializableFilesManager.createBackup(username)) {
				session.getLoginPackets().sendClientPacket(20);
				return;
			}
			if (!player.getPassword().equals(password)) {
				session.getLoginPackets().sendClientPacket(3);
				return;
			}
		}
		if (player.isPermBanned()
				|| player.getBanned() > Utils.currentTimeMillis()) {
			session.getLoginPackets().sendClientPacket(4);
			return;
		}
		player.init(session, username, displayMode, screenWidth, screenHeight);
		session.getLoginPackets().sendLoginDetails(player);
		session.setDecoder(3, player);
		session.setEncoder(2, player);
		player.start();

	}

}
