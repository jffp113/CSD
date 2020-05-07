package pt.unl.fct.csd.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.unl.fct.csd.Model.AsyncReply;
import pt.unl.fct.csd.Model.Auction;
import pt.unl.fct.csd.Model.Bid;
import pt.unl.fct.csd.Model.VoidWrapper;
import pt.unl.fct.csd.Replication.*;

import java.util.List;

@RestController("ImpAuctionReplicator")
@RequestMapping(value = CollectiveAuctionController.BASE_URL)
public class AuctionControllerReplicatorImp implements CollectiveAuctionController {
    private final Logger logger =
            LoggerFactory.getLogger(AuctionControllerReplicatorImp.class);

    @Autowired
    ClientAsyncReplicator clientAsyncReplicator;
    @Autowired
    ClientReplicator clientReplicator;


    @Override
    public List<AsyncReply> createAuction(String ownerId) throws InterruptedException {
        logger.info("Proxy received Create Auction");
        return clientAsyncReplicator.invokeOrderedReplication(ownerId, Path.CREATE_AUCTION);
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
    public List<AsyncReply> getOpenAuctions() {
        logger.info("Proxy received Get Open Auctions");
        return clientAsyncReplicator.invokeUnorderedReplication(Path.GET_OPEN_AUCTIONS);
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
