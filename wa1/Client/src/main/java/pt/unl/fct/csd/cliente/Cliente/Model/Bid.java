package pt.unl.fct.csd.cliente.Cliente.Model;

import com.google.gson.annotations.SerializedName;

public class Bid {

	@SerializedName(value = "id", alternate = {"Id"})
	private Long id;

	@SerializedName(value = "bidderId", alternate = {"BidderId"})
	private String bidderId;

	@SerializedName(value = "auctionId", alternate = {"AuctionId"})
	private Long auctionId;

	@SerializedName(value = "value", alternate = {"Value"})
	private int value;

	public Bid(String bidderId, Long auctionId, int value){
		this.bidderId = bidderId;
		this.auctionId = auctionId;
		this.value = value;
	}

	public Long getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(Long auctionId) {
		this.auctionId = auctionId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBidderId() {
		return bidderId;
	}

	public void setBidderId(String bidderId) {
		this.bidderId = bidderId;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
