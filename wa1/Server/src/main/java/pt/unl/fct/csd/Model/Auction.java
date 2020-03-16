package pt.unl.fct.csd.Model;

import java.util.List;

import javax.persistence.*;

@Entity
@Table
public class Auction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "ownerId", nullable = false)
	private String ownerId;

	@Column(name = "bids", nullable = false)
	private List<String> bids;

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

	public List<String> getBidIds() {
		return bids;
	}

	public void setBids(List<String> bids) {
		this.bids = bids;
	}
}