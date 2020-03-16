package pt.unl.fct.csd.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import pt.unl.fct.csd.Exceptions.AccountDoesNotExistException;
import pt.unl.fct.csd.Exceptions.InvalidTransactionException;
import pt.unl.fct.csd.Model.Auction;
import pt.unl.fct.csd.Model.Bid;
import pt.unl.fct.csd.Model.Transaction;
import pt.unl.fct.csd.Model.UserAccount;
import pt.unl.fct.csd.Repository.AuctionRepository;
import pt.unl.fct.csd.Repository.BidRepository;
import pt.unl.fct.csd.Repository.TransactionRepository;
import pt.unl.fct.csd.Repository.UserAccountRepository;

import java.util.List;
import java.util.Optional;


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
	public void createAuction(Auction auction) {
		if (userAccountRepository.existsById(auction.getOwnerId())) {
			throw new AccountDoesNotExistException();
		}

		auction.setOwnerId(null); //TODO LETS CHANGE THIS LATER
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
		return auctionRepository.getAllByIsOpenTrue();
	}

	@Override
	public List<Auction> getClosedAuction() {
		return auctionRepository.getAllByIsOpenFalse();
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
