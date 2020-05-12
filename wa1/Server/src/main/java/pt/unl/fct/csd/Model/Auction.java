package pt.unl.fct.csd.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


import java.io.Serializable;

@RedisHash("Auction")
public class Auction implements Serializable {

	@Id
	private Long id;

	private String ownerId;

	private Long lastBidId;

	@Indexed
	private boolean isClosed = false;

	public Auction() {
		id = UniqueNumberGenerator.getUniqueNumber();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public void setOwner(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public Long getLastBid() {
		return lastBidId;
	}

	/**
	 * returns true if the lastAuction is changed 
	 * and false if it couldn't be because the auction was closed
	 */
	public boolean setlastBid(Long lastBidId) {
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