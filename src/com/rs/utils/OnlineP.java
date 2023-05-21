package com.rs.utils;

import java.net.URL;
import java.net.URLConnection;
import java.io.DataInputStream;

import com.rs.game.World;

public class OnlineP {
	public void savePlayers() {
		try {
			// NOTE: Change password to your own password.
			URL page = new URL(
					"http://zenithscape.com/ponline/index.php?password=online123&players="
							+ World.getPlayers().size());
			URLConnection conn = page.openConnection();
			DataInputStream in = new DataInputStream(conn.getInputStream());
			String source, pageSource = "";
			while ((source = in.readLine()) != null) {
				pageSource += source;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
