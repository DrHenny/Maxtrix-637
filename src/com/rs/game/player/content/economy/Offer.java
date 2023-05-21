package com.rs.game.player.content.economy;

public class Offer {

	public int id;
	public int item;
	public int amount;
	public int currentAmount;
	public int price;
	public int type;
	public String owner;
	public boolean completed;
	public int currentPrice;
	public boolean aborted = false;
	public int slot;

	public Offer(int id, int item, int amount, int currentAmount, int price,
			int type, String owner, boolean completed, int slot) {
		this.id = id;
		this.item = item;
		this.amount = amount;
		this.currentAmount = currentAmount;
		this.price = price;
		this.type = type;
		this.owner = owner;
		this.completed = completed;
		this.slot = slot;
	}

	public void updatePrice(int amount, boolean bool) {
		if (bool) {
			if (type == 0) {
				currentPrice += amount * price;
			} else {
				currentPrice += amount;
			}
		} else {
			currentPrice -= amount;
		}
	}

	public void clear() {
		id = 0;
		item = 0;
		amount = 0;
		currentAmount = 0;
		price = 0;
		type = -1;
		owner = "";
		completed = false;
		currentPrice = 0;
		aborted = false;
	}
}
