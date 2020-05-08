package Models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
public class Auction implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "ownerId", nullable = false)
	private String ownerId;

	@Column(name = "closingBidId", nullable = true)
	private Long lastBidId;
	
	@Column(name = "isClosed", nullable = false)
	private boolean isClosed = false;

	public Auction() {}
	
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