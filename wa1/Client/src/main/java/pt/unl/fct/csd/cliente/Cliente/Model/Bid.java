package pt.unl.fct.csd.cliente.Cliente.Model;

import java.io.Serializable;

public class Bid implements Serializable {

	private Long id;

	private String bidderId;
	
	private Long auctionId;

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
