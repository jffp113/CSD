package pt.unl.fct.csd.Repository;

import org.springframework.data.repository.CrudRepository;

import pt.unl.fct.csd.Model.Bid;

import java.util.List;

public interface BidRepository extends CrudRepository<Bid, Long> {
	
	List<Bid> getAllByAuctionId(long auctionId);
	
	List<Bid> getAllByBidderId(String bidderId);
}
