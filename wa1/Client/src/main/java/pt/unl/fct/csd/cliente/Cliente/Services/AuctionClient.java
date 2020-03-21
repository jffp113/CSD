package pt.unl.fct.csd.cliente.Cliente.Services;

import java.util.List;

import pt.unl.fct.csd.cliente.Cliente.Model.Auction;
import pt.unl.fct.csd.cliente.Cliente.Model.Bid;
import pt.unl.fct.csd.cliente.Cliente.exceptions.ServerAnswerException;

public interface AuctionClient {

	Long createAuction(String ownerId) throws ServerAnswerException;
	
	void terminateAuction(long auctionId) throws ServerAnswerException;
	
	List<Auction> getOpenAuctions() throws ServerAnswerException;
	
	List<Auction> getClosedAuctions() throws ServerAnswerException;
	
	List<Bid> getAuctionBids(long auctionId) throws ServerAnswerException;
	
	List<Bid> getClientBids(String clientId) throws ServerAnswerException;
	
	Bid getClosedBid(long Auction) throws ServerAnswerException;

	Long createBid(String bidderId, Long auctionId, int value) throws ServerAnswerException;
}
