package com.rs.game.player;

import com.rs.game.WorldTile;

public final class CoordsEvent {

	private WorldTile tile;
	private Runnable event;
	private int sizeX;
	private int sizeY;

	public CoordsEvent(WorldTile tile, Runnable event, int sizeX, int sizeY,
			int rotation) {
		this.tile = tile;
		this.event = event;
		if (rotation == 1 || rotation == 3) {
			this.sizeX = sizeX <= 0 ? 1 : sizeX;
			this.sizeY = sizeY <= 0 ? 1 : sizeY;
		} else {
			this.sizeX = sizeX <= 0 ? 1 : sizeX;
			this.sizeY = sizeY <= 0 ? 1 : sizeY;
		}
	}

	public CoordsEvent(WorldTile tile, Runnable event, int sizeX, int sizeY) {
		this(tile, event, sizeX, sizeY, -1);
	}

	public CoordsEvent(WorldTile tile, Runnable event, int size) {
		this(tile, event, size, size);
	}

	/*
	 * returns if done
	 */
	public boolean processEvent(Player player) {
		if (player.getPlane() != tile.getPlane())
			return true;
		int distanceX = Math.abs(player.getX() - tile.getX());
		int distanceY = Math.abs(player.getY() - tile.getY());
		if (distanceX > sizeX || distanceY > sizeY)
			return cantReach(player);
		// if(sizeX == 1 && sizeY == 1 && player.getX() != tile.getX() &&
		// player.getY() != tile.getY())
		if (player.hasWalkSteps())
			player.resetWalkSteps();
		if (player.getNextWalkDirection() != -1)
			return false;
		/*
		 * if(!player.clipedProjectile(tile, false)) return cantReach(player);
		 */
		// player.resetWalkSteps();
		event.run();
		return true;

	}

	public boolean cantReach(Player player) {
		if (!player.hasWalkSteps() && player.getNextWalkDirection() == -1) {
			player.getPackets().sendGameMessage("You can't reach that.");
			return true;
		}
		return false;
	}
}