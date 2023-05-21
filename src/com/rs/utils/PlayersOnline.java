package com.rs.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.rs.game.World;

public class PlayersOnline {

	public static Connection CONNECTION = null;
	public static Statement STATEMENT;
	private static String HOST = "ip";
	private static String DATABASE = "dbname";
	private static String USERNAME = "dbusername";
	private static String PASSWORD = "dbpass";

	public static void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			CONNECTION = DriverManager.getConnection("jdbc:mysql://" + HOST
					+ "/" + DATABASE, USERNAME, PASSWORD);
			STATEMENT = CONNECTION.createStatement();
			System.out.println("Updated players online.");
		} catch (Exception e) {
			System.out.println("Connection to SQL database failed");
			e.printStackTrace();
		}
	}

	public static void destroyConnection() {
		try {
			STATEMENT.close();
			CONNECTION.close();
		} catch (Exception e) {
		}
	}

	public static void updatePlayers() {
		try {
			createConnection();
			String query = "DELETE FROM playersonline";
			String query2 = "INSERT INTO playersonline (online) VALUES ('"
					+ getPlayersOnline() + "')";
			STATEMENT.executeUpdate(query);
			STATEMENT.executeUpdate(query2);
			destroyConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static int getPlayersOnline() {
		return World.getPlayers().size();
	}
}