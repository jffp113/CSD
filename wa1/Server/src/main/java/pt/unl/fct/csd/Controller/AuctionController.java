package pt.unl.fct.csd.Controller;

import org.springframework.web.bind.annotation.*;

import pt.unl.fct.csd.Model.Auction;
import pt.unl.fct.csd.Model.Bid;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = AuctionController.BASE_URL)
public interface AuctionController {

    String BASE_URL = "/auctions";
    String CREATE_AUCTION = "/create/{ownerId}";
    String TERMINATE_AUCTION = "/terminate/{auctionId}";
    String GET_CLOSE_BID = "/{auctionId}/closebid";	
    String GET_OPEN_AUCTIONS = "/open";
    String GET_CLOSED_AUCTIONS = "/closed";
    String GET_AUCTION_BIDS = "/{auctionId}/bids";
    String GET_CLIENT_BIDS = "/client/{clientId}";
    
    @PostMapping(value = CREATE_AUCTION)
    void createAuction(@PathVariable("ownerId") String ownerId);
 
    @PutMapping(value = TERMINATE_AUCTION)
    void terminateAuction(@PathVariable("auctionId") long auctionId);
   
    @GetMapping(
            value = GET_OPEN_AUCTIONS,
            produces = APPLICATION_JSON_VALUE)
    List<Auction> getOpenAuctions();

    @GetMapping(
            value = GET_CLOSED_AUCTIONS,
            produces = APPLICATION_JSON_VALUE)
    List<Auction> getClosedAuction();

    @GetMapping(
            value = GET_AUCTION_BIDS,
            produces = APPLICATION_JSON_VALUE)
    List<Bid> getAuctionBids(@PathVariable("auctionId") long auctionId);
    
    @GetMapping(
            value = GET_CLIENT_BIDS,
            produces = APPLICATION_JSON_VALUE)
    List<Bid> getClientBids(@PathVariable("clientId") String clientId);
    
    @GetMapping(
            value = GET_CLOSE_BID,
            produces = APPLICATION_JSON_VALUE)
    Bid getCloseBid(@PathVariable("auctionId") long auctionId);
    
}

