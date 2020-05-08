package pt.unl.fct.csd.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.unl.fct.csd.Models.Auction;

@Service
public class AuctionRepositoryWrapperImp {

    @Autowired
    private AuctionRepository auctionRepository;

    public Auction findById(){
        return null;
    }


}
