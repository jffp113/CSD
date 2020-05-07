package pt.unl.fct.csd.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.unl.fct.csd.Model.Bid;
import pt.unl.fct.csd.Replication.*;
import java.util.List;

@RestController("ImpAuctionReplicator")
@RequestMapping(value = CollectiveAuctionController.BASE_URL)
public class AuctionControllerReplicatorImp implements CollectiveAuctionController {
    private final Logger logger =
            LoggerFactory.getLogger(AuctionControllerReplicatorImp.class);

    @Autowired
    ClientAsyncReplicator clientAsyncReplicator;

    @Override
    public List<AsyncReply> createAuction(String ownerId) {
        logger.info("Proxy received Create Auction");
        return clientAsyncReplicator.invokeOrderedReplication(ownerId, Path.CREATE_AUCTION);
    }

    @Override
    public List<AsyncReply> terminateAuction(long auctionId) { // changed from void
        logger.info("Proxy received Terminate Auction");
        return clientAsyncReplicator.invokeOrderedReplication(auctionId, Path.TERMINATE_AUCTION);
    }

    @Override
    public List<AsyncReply> makeBid(Bid bid) {
        logger.info("Proxy received Make Bid");
        return clientAsyncReplicator.invokeOrderedReplication(bid, Path.CREATE_BID_AUCTION);
    }

    @Override
    public List<AsyncReply> getOpenAuctions() {
        logger.info("Proxy received Get Open Auctions");
        return clientAsyncReplicator.invokeUnorderedReplication(Path.GET_OPEN_AUCTIONS);
    }

    @Override
    public List<AsyncReply> getClosedAuction() {
        logger.info("Proxy received Get Closed Auctions");
        return clientAsyncReplicator.invokeUnorderedReplication(Path.GET_CLOSED_AUCTIONS);
    }

    @Override
    public List<AsyncReply> getAuctionBids(long auctionId) {
        logger.info("Proxy received Get Auction Bids");
        return clientAsyncReplicator.invokeUnorderedReplication(auctionId, Path.GET_AUCTION_BIDS);
    }

    @Override
    public List<AsyncReply> getClientBids(String clientId) {
        logger.info("Proxy received Get Client Bids");
        return clientAsyncReplicator.invokeUnorderedReplication(clientId, Path.GET_CLIENT_BIDS);
    }

    @Override
    public List<AsyncReply> getCloseBid(long auctionId) {
        logger.info("Proxy received Get Close Bid");
        return clientAsyncReplicator.invokeUnorderedReplication(auctionId, Path.GET_CLOSE_BID);
    }
}
