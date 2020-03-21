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
    private BidRepository bidRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private WalletController walletControllerReplicatorImp;

	@Override
	public Long createAuction(String ownerId) {
		if (!existsUser(ownerId))
			throw new UserDoesNotExistException(ownerId);

		Auction auction = new Auction();
		auction.setOwner(ownerId);
		auctionRepository.save(auction);
		return auction.getId();
	}

	private boolean existsUser(String userId) {
		//return new UserCommonsImpl().existsUserAccount(userId);
		return existsUserAccount(userId);
	}

	@Override
	public void terminateAuction(long auctionId) {
		Auction auction = getAuction(auctionId);
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
		getAuction(auctionId);
		return bidRepository.getAllByAuctionId(auctionId);
	}

	@Override
	public List<Bid> getClientBids(String clientId) {
		if (!existsUserAccount(clientId))
			throw new UserDoesNotExistException(clientId);
		return bidRepository.getAllByBidderId(clientId);
	}

	@Override
	public Bid getCloseBid(long auctionId) {
		Auction auction = getAuction(auctionId);
		if (!auction.isAuctionClosed())
			throw new AuctionIsNotClosedException(auctionId);
		Long bidId = auction.getLastBid();
		return getBid(bidId);
	}

	@Override
	public Long makeBid(Bid newBid) {
		UserAccount bidder = getUser(newBid.getBidderId());
		Auction auction = getAuction(newBid.getAuctionId());
		bidConsistencyChecks(bidder,newBid,auction);
		replaceLastBid(auction, newBid, bidder);
		auctionRepository.save(auction);
		return newBid.getId();
	}

	private UserAccount getUser(String userId) {
		//return new UserCommonsImpl().getUserAccount(userId);
		return getUserAccount(userId);
	}

	private Auction getAuction(Long auctionId) {
		Optional<Auction> auction = auctionRepository.findById(auctionId);
		return auction.orElseThrow(() -> new AuctionDoesNotExistException(auctionId));
	}

	private void bidConsistencyChecks(UserAccount bidder, Bid newBid, Auction auction) {
		if (newBid.getValue() <= 0)
			throw new BidAmountIsInvalidException();
		if (!doesUserHaveEnoughMoney(bidder, newBid))
			throw new UserDoesNotHaveTheMoneyToMakeBidException(bidder.getId(), newBid.getValue());
		if (auction.isAuctionClosed())
			throw new AuctionIsAlreadyClosedException(auction.getId());
	}

	private boolean doesUserHaveEnoughMoney(UserAccount bidder, Bid newBid) {
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
			throw new BidAmountIsTooSmallToSurpassPreviousException(last.getValue());
		reimbursePreviousBidder(last.getBidderId(), last.getValue());
	}

	private Bid getBid(Long bidId) throws BidDoesNotExistException{
		if (bidId == null)
			throw new BidDoesNotExistException();
		return bidRepository.findById(bidId).
				orElseThrow(BidDoesNotExistException::new);
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

	/**************************************************************/

	public boolean existsUserAccount(String userId) {
		try {
			getUserAccount(userId);
			return true;
		} catch (UserDoesNotExistException e) {
			return false;
		}
	}

	public UserAccount getUserAccount(String userId) throws UserDoesNotExistException{
		Optional<UserAccount> user = userAccountRepository.findById(userId);
		return user.orElseThrow(() -> new UserDoesNotExistException(userId));
	}

	public void saveUserInDB(UserAccount user) {
		userAccountRepository.save(user);
	}

}
