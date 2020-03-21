package pt.unl.fct.csd.Controller;

import bftsmart.tom.ServiceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RestController;
import pt.unl.fct.csd.Model.Bid;

@PropertySource("classpath:application.properties")
@RestController
public class AuctionControllerReplicatorImp extends AuctionControllerImp {
    private final Logger logger =
            LoggerFactory.getLogger(AuctionControllerReplicatorImp.class);

    @Value("${replica.id}")
    private int ID;

    @Autowired
    ServiceProxy serviceProxy;

    @Override
    public Long createAuction(String ownerId) {
        //TODO
        return super.createAuction(ownerId);
    }

    @Override
    public void terminateAuction(long auctionId) {
        //TODO
        super.terminateAuction(auctionId);
    }

    @Override
    public Long makeBid(Bid newBid) {
        //TODO
        return super.makeBid(newBid);
    }
}
