package pt.unl.fct.csd.Model;

import javax.persistence.*;

@Entity
@Table
public class Auction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "ownerId", nullable = false)
	private final String ownerId;

	@Column(name = "closingBidId", nullable = true)
	private String lastBidId;
	
	@Column(name = "isClosed", nullable = false)
	private boolean isClosed = false;

	public Auction(String ownerId) {
		this.ownerId = ownerId;
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