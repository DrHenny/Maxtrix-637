package com.rs.game.player.content.economy;

public class GrandExchangeLoader {

	/**
	 * Byte buffer for storing bytes to be loaded or saved.
	 */
	// private Stream stream = new Stream(5000, 5000);
	/*
	 * public GrandExchangeLoader() {
	 * 
	 * }
	 * 
	 * public void saveOffers() throws Exception { int i = 0; for(Offer Offer :
	 * GrandExchange.offerList) { if(Offer == null) { continue; } i++;
	 * stream.outOffset = 0; stream.writeString("id:" + Offer.id);
	 * stream.writeString("item:" + Offer.item); stream.writeString("amount:" +
	 * Offer.amount); stream.writeString("currentAmount:" +
	 * Offer.currentAmount); stream.writeString("price:" + Offer.price);
	 * stream.writeString("type:" + Offer.type); stream.writeString("owner:" +
	 * Offer.owner); if(Offer.completed) { stream.writeString("completed:" + 1);
	 * } else { stream.writeString("completed:" + 0); }
	 * stream.writeString("currentPrice:" + Offer.currentPrice);
	 * if(Offer.aborted) { stream.writeString("aborted:" + 1); } else {
	 * stream.writeString("aborted:" + 0); } stream.writeString("slot:" +
	 * Offer.slot); stream.writeString("null"); FileOutputStream out = new
	 * FileOutputStream("./data/Offers/" + i + ".txt");
	 * out.write(stream.getBuffer(), 0, stream.getOffset()); out.flush();
	 * out.close(); out = null; } }
	 * 
	 * public void loadOffers() { for(int i = 0; i <= 2000; i++) { //
	 * stream.setOffset(0); try { FileInputStream in = new
	 * FileInputStream("./data/Offers/" + i + ".txt"); //
	 * in.read(stream.getBuffer()); in.close(); in = null; } catch (Exception e)
	 * { continue; } String line; try { int id = 0; int item = 0; int amount =
	 * 0; int currentAmount = 0; int price = 0; int type = 0; String owner = "";
	 * boolean completed = false; int currentPrice = 0; int slot = 0; boolean
	 * aborted = false; // while ((line = stream.toString()) != null &&
	 * line.length() > 0) { if (line.startsWith("id:")) { id =
	 * Integer.parseInt(line.substring(3)); } else if(line.startsWith("item:"))
	 * { item = Integer.parseInt(line.substring(5)); } else
	 * if(line.startsWith("amount:")) { amount =
	 * Integer.parseInt(line.substring(7)); } else
	 * if(line.startsWith("currentAmount:")) { currentAmount =
	 * Integer.parseInt(line.substring(14)); } else
	 * if(line.startsWith("price:")) { price =
	 * Integer.parseInt(line.substring(6)); } else if(line.startsWith("type:"))
	 * { type = Integer.parseInt(line.substring(5)); } else
	 * if(line.startsWith("owner:")) { owner = line.substring(6); } else
	 * if(line.startsWith("completed:")) { int iii =
	 * Integer.parseInt(line.substring(10)); if(iii == 1) { completed = true; }
	 * else { completed = false; } } else if(line.startsWith("currentPrice:")) {
	 * currentPrice = Integer.parseInt(line.substring(13)); } else
	 * if(line.startsWith("aborted:")) { int iii =
	 * Integer.parseInt(line.substring(10)); if(iii == 1) { aborted = true; }
	 * else { aborted = false; } } else if(line.startsWith("slot:")) { slot =
	 * Integer.parseInt(line.substring(5)); } // } if(id != 0) {
	 * GrandExchange.offerList.add(new
	 * Offer(id,item,amount,currentAmount,price,type,owner,completed,slot)); }
	 * for(Offer l : GrandExchange.offerList) { if(l == null) { continue; }
	 * if(l.id == id && l.item == item && l.amount == amount && l.currentAmount
	 * == currentAmount && l.price == price && l.type == type &&
	 * l.owner.equals(owner) && l.slot == slot) { l.currentPrice = currentPrice;
	 * l.aborted = aborted; break; } } } catch(Exception e) {} }
	 * System.out.println("[GrandExchange] - Loaded Offers"); }
	 */
}