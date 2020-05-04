package pt.unl.fct.csd.Controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pt.unl.fct.csd.Model.Auction;
import pt.unl.fct.csd.Model.Bid;

import java.util.List;

public interface AuctionController {

    Long createAuction(String ownerId);

    void terminateAuction(long auctionId);

    List<Auction> getOpenAuctions();

    List<Auction> getClosedAuction();

    List<Bid> getAuctionBids(long auctionId);

    List<Bid> getClientBids(String clientId);

    Bid getCloseBid(long auctionId);

    Long makeBid(Bid bid);
}
