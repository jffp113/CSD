package pt.unl.fct.csd.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.unl.fct.csd.Model.Bid;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
	
	List<Bid> getAllByAuctionId(long auctionId);
	
	List<Bid> getAllByBidderId(String bidderId);
}
