package pt.unl.fct.csd.Controller;

import bftsmart.tom.ServiceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.unl.fct.csd.Model.Auction;
import pt.unl.fct.csd.Model.Bid;
import pt.unl.fct.csd.Replication.ClientReplicator;

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
        return auctionController.createAuction(ownerId);
    }

    @Override
    public void terminateAuction(long auctionId) {
        auctionController.terminateAuction(auctionId);
    }

    @Override
    public Long makeBid(Bid bid) {
        return auctionController.makeBid(bid);
    }

    @Override
    public List<Auction> getOpenAuctions() {
        return auctionController.getOpenAuctions();
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
