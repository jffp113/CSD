package pt.unl.fct.csd.cliente.Cliente.Model;

import com.google.gson.annotations.SerializedName;

public class Auction {

	@SerializedName(value = "id", alternate = {"Id"})
	private Long id;

	@SerializedName(value = "ownerId", alternate = {"OwnerId"})
	private String ownerId;

	@SerializedName(value = "lastBidId", alternate = {"LastBidId"})
	private String lastBidId;

	@SerializedName(value = "isClosed", alternate = {"IsClosed"})
	private boolean isClosed;
	
	public Auction () {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getLastBid() {
		return lastBidId;
	}

	/**
	 * returns true if the lastAuction is changed 
	 * and false if it couldn't be because the auction was closed
	 */
	public boolean setlastBid(String lastBidId) {
		if (!isClosed) {
			this.lastBidId = lastBidId;
			return true;
		}
		return false;
	}
	
	public boolean isAuctionClosed() {
		return isClosed;
	}
	
	public void closeAuction() {
		this.isClosed = true;
	}
}