package pt.unl.fct.csd.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import pt.unl.fct.csd.Exceptions.*;
import pt.unl.fct.csd.Model.Auction;
import pt.unl.fct.csd.Model.Bid;
import pt.unl.fct.csd.Model.Transaction;
import pt.unl.fct.csd.Model.UserAccount;
import pt.unl.fct.csd.Repository.AuctionRepository;
import pt.unl.fct.csd.Repository.BidRepository;
import pt.unl.fct.csd.Repository.UserAccountRepository;

import java.util.List;
import java.util.Optional;


public class AuctionControllerImp implements AuctionController {

    private final Logger logger =
            LoggerFactory.getLogger(AuctionControllerImp.class);

    @Autowired
    private AuctionRepository auctionRepository;
    
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private WalletController walletControllerReplicatorImp;

	@Override
	public void createAuction(String ownerId) {
		if (!userAccountRepository.existsById(ownerId)) {
			throw new ResourceDoesNotExistException();
		}

		Auction auction = new Auction();
		auction.setOwner(ownerId);
		auctionRepository.save(auction);
	}

	@Override
	public void terminateAuction(long auctionId) {
		Auction auction = auctionRepository.findById(auctionId).
			orElseThrow(ResourceDoesNotExistException::new);
		auction.closeAuction();
		auctionRepository.save(auction);
	}

	@Override
	public List<Auction> getOpenAuctions() {
		return auctionRepository.getAllByisClosedFalse();
	}

	@Override
	public List<Auction> getClosedAuction() {
		return auctionRepository.getAllByisClosedTrue();
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
				orElseThrow(ResourceDoesNotExistException::new);
		Long bidId = auction.getLastBid();

		if(bidId == null)
			throw new InvalidOperationException();

		return bidRepository.findById(bidId).
				orElseThrow(ResourceDoesNotExistException::new);
	}

	//Todo improve check if user have money to bid
	@Override
	public void makeBid(Bid newBid) {
		UserAccount bidder = getUserAccount(newBid.getBidderId());
		Auction auction = getAuction(newBid.getAuctionId());
		bidConsistencyChecks(bidder,newBid,auction);
		replaceLastBid(auction, newBid, bidder);
		auctionRepository.save(auction);
	}

	private UserAccount getUserAccount(String userId) {
		Optional<UserAccount> user = userAccountRepository.findById(userId);
		return user.orElseThrow(UserDoesNotExistException::new);
	}

	private Auction getAuction(Long auctionId) {
		Optional<Auction> auction = auctionRepository.findById(auctionId);
		return auction.orElseThrow(AuctionDoesNotExistException::new);
	}

	private void bidConsistencyChecks(UserAccount bidder, Bid newBid, Auction auction) {
		if (!userHasEnoughMoney(bidder, newBid))
			throw new UserDoesNotHaveTheMoneyToMakeBidException();
		if (auction.isAuctionClosed())
			throw new AuctionIsAlreadyClosedException();
	}

	private boolean userHasEnoughMoney(UserAccount bidder, Bid newBid) {
		return bidder.getMoney() >= newBid.getValue();
	}

	private void replaceLastBid(Auction auction, Bid newBid, UserAccount bidder) {
		try {
			tryToReplaceLastBid(newBid, auction);
		} catch (BidDoesNotExistException e) {}
		bidRepository.save(newBid);			//Bid must be saved here in order to have an ID
		auction.setlastBid(newBid.getId());
		removeMoneyFromBid(bidder, newBid.getValue());
	}

	private void tryToReplaceLastBid(Bid newBid, Auction auction) throws BidDoesNotExistException{
		Bid last = getBid(auction.getLastBid());
		if (newBid.getValue() <= last.getValue())
			throw new BidAmountIsTooSmallToSurpassPreviousException();
		reimbursePreviousBidder(last.getBidderId(), last.getValue());
	}

	private Bid getBid(Long bidId) throws BidDoesNotExistException{
		if (bidId == null)
			throw new BidDoesNotExistException();
		Optional<Bid> bid = bidRepository.findById(bidId);
		return bid.orElseThrow(BidDoesNotExistException::new);
	}

	private void reimbursePreviousBidder(String bidder, long amount) {
		Transaction t = new Transaction();
		t.setTo(bidder);
		t.setAmount(amount);
		walletControllerReplicatorImp.createMoney(t);
	}

	private void removeMoneyFromBid(UserAccount bidder, long bidValue) {
		walletControllerReplicatorImp.removeMoney(bidder, bidValue);
	}

}
