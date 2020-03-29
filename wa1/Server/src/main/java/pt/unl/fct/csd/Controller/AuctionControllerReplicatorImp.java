package pt.unl.fct.csd.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.unl.fct.csd.Model.Auction;
import pt.unl.fct.csd.Model.Bid;
import pt.unl.fct.csd.Model.VoidWrapper;
import pt.unl.fct.csd.Replication.ClientReplicator;
import pt.unl.fct.csd.Replication.InvokerWrapper;
import pt.unl.fct.csd.Replication.Path;

import java.util.Arrays;
import java.util.List;


@RestController("ImpAuctionReplicator")
@RequestMapping(value = AuctionController.BASE_URL)
public class AuctionControllerReplicatorImp implements AuctionController {
    private final Logger logger =
            LoggerFactory.getLogger(AuctionControllerReplicatorImp.class);

    @Autowired
    ClientReplicator clientReplicator;

    @Qualifier("ImpAuction")
    @Autowired
    AuctionController auctionController;

    @Override
    public Long createAuction(String ownerId) {
        logger.info("Proxy received Create Auction");
        InvokerWrapper<Long> auctionId = clientReplicator.
                invokeOrderedReplication(ownerId, Path.CREATE_AUCTION);
        return auctionId.getResultOrThrow();
    }

    @Override
    public void terminateAuction(long auctionId) {
        logger.info("Proxy received Terminate Auction");
        InvokerWrapper<VoidWrapper> result = clientReplicator.
                invokeOrderedReplication(auctionId, Path.TERMINATE_AUCTION);
        result.getResultOrThrow();
    }

    @Override
    public Long makeBid(Bid bid) {
        logger.info("Proxy received Make Bid");
        InvokerWrapper<Long> bidId = clientReplicator.
                invokeOrderedReplication(bid, Path.CREATE_BID_AUCTION);
        return bidId.getResultOrThrow();
    }

    @Override
    public List<Auction> getOpenAuctions() {
        logger.info("Proxy received Get Open Auctions");
        return new GenericListResults<Auction, Void>(clientReplicator).
                getListWithPath(Path.GET_OPEN_AUCTIONS);
    }

    @Override
    public List<Auction> getClosedAuction() {
        logger.info("Proxy received Get Closed Auctions");
        return new GenericListResults<Auction, Void>(clientReplicator).
                getListWithPath(Path.GET_CLOSED_AUCTIONS);
    }

    @Override
    public List<Bid> getAuctionBids(long auctionId) {
        logger.info("Proxy received Get Auction Bids");
        return new GenericListResults<Bid, Long>(clientReplicator).
                getListWithPath(auctionId, Path.GET_AUCTION_BIDS);
    }

    @Override
    public List<Bid> getClientBids(String clientId) {
        logger.info("Proxy received Get Client Bids");
        return new GenericListResults<Bid, String>(clientReplicator).
                getListWithPath(clientId, Path.GET_CLIENT_BIDS);
    }

    @Override
    public Bid getCloseBid(long auctionId) {
        logger.info("Proxy received Get Close Bid");
        InvokerWrapper<Bid> bid = clientReplicator.
                invokeUnorderedReplication(auctionId, Path.GET_CLOSE_BID);
        return bid.getResultOrThrow();
    }
}
