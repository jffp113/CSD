package pt.unl.fct.csd.cliente.Cliente.Services;

import java.util.List;

import pt.unl.fct.csd.cliente.Cliente.Model.Auction;
import pt.unl.fct.csd.cliente.Cliente.Model.Bid;

public interface AuctionClient {

	void createAuction(String clientId);
	
	void terminateAuction(long auctionId);
	
	List<Auction> getOpenAuctions();
	
	List<Auction> getClosedAuctions();
	
	List<Bid> getAuctionBids(long auctionId);
	
	List<Bid> getClientBids(String clientId);
	
	Bid getClosedBid(long Auction);
}
