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
		// TODO Auto-generated method stub
	}

	@Override
	public void terminateAuction(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Auction> getOpenAuctions(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Auction> getClosedAuction(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Bid> getAuctionBids(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Bid> getClientBids(String clientId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bid getCloseBid(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
