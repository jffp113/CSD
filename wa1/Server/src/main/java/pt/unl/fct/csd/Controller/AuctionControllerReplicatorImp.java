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
        InvokerWrapper<Long> auctionId = clientReplicator.
                invokeReplication(ownerId, Path.CREATE_AUCTION);
        return auctionId.getResultOrThrow();
    }

    @Override
    public void terminateAuction(long auctionId) {
        InvokerWrapper<VoidWrapper> result = clientReplicator.
                invokeReplication(auctionId, Path.TERMINATE_AUCTION);
        result.getResultOrThrow();
    }

    @Override
    public Long makeBid(Bid bid) {
        InvokerWrapper<Long> bidId = clientReplicator.
                invokeReplication(bid, Path.CREATE_BID_AUCTION);
        return bidId.getResultOrThrow();
    }

    @Override
    public List<Auction> getOpenAuctions() {
        InvokerWrapper<Auction[]> auctions = clientReplicator.
                invokeReplication();
    }

    @Override
    public List<Auction> getClosedAuction() {
        return auctionController.getClosedAuction();
    }

    @Override
    public List<Bid> getAuctionBids(long auctionId) {
        return auctionController.getAuctionBids(auctionId);
    }

    @Override
    public List<Bid> getClientBids(String clientId) {
        return auctionController.getClientBids(clientId);
    }

    @Override
    public Bid getCloseBid(long auctionId) {
        return auctionController.getCloseBid(auctionId);
    }
}
