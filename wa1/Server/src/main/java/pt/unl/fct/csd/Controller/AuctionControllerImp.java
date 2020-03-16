package pt.unl.fct.csd.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import pt.unl.fct.csd.Exceptions.AccountDoesNotExistException;
import pt.unl.fct.csd.Model.Auction;
import pt.unl.fct.csd.Model.Bid;
import pt.unl.fct.csd.Repository.AuctionRepository;
import pt.unl.fct.csd.Repository.BidRepository;
import pt.unl.fct.csd.Repository.UserAccountRepository;

import java.util.List;

@RestController
public class AuctionControllerImp implements AuctionController {

    private final Logger logger =
            LoggerFactory.getLogger(AuctionControllerImp.class);

    @Autowired
    private AuctionRepository auctionRepository;
    
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private BidRepository bidRepository;

	@Override
	public void createAuction(String ownerId) {
		if (userAccountRepository.existsById(ownerId)) {
			throw new AccountDoesNotExistException();
		}

		Auction auction = new Auction();
		auction.setOwner(ownerId);
		auctionRepository.save(auction);
	}

	@Override
	public void terminateAuction(long auctionId) {
		Auction auction = auctionRepository.findById(auctionId).
			orElseThrow(() -> new AccountDoesNotExistException());
		auction.closeAuction();
	}

	@Override
	public List<Auction> getOpenAuctions() {
		return auctionRepository.getAllByIsClosedFalse();
	}

	@Override
	public List<Auction> getClosedAuction() {
		return auctionRepository.getAllByIsClosedTrue();
	}

	@Override
	public List<Bid> getAuctionBids(long auctionId) {
		return bidRepository.getAllByAuctionId(auctionId);
	}

	@Override
	public List<Bid> getClientBids(String clientId) {
		return bidRepository.getAllByBidderId(clientId);
	}

	@Override
	public Bid getCloseBid(long auctionId) {
		Auction auction = auctionRepository.findById(auctionId).
				orElseThrow(() -> new AccountDoesNotExistException());
		String bidId = auction.getLastBid();
		return bidRepository.findById(bidId).
				orElseThrow(() -> new AccountDoesNotExistException());
	}

}
