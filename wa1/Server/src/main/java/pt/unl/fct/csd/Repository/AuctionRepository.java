package pt.unl.fct.csd.Repository;

import org.springframework.data.repository.CrudRepository;

import pt.unl.fct.csd.Model.Auction;

import java.util.List;

public interface AuctionRepository extends CrudRepository<Auction, Long> {

	List<Auction> getAllByisClosedFalse();
	
	List<Auction> getAllByisClosedTrue();


}
