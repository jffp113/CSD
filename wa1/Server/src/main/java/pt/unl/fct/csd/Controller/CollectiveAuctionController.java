package pt.unl.fct.csd.Controller;

import org.springframework.web.bind.annotation.*;

import pt.unl.fct.csd.Model.Bid;
import pt.unl.fct.csd.Model.AsyncReply;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


public interface CollectiveAuctionController {

    String BASE_URL = "/auctions";
    String CREATE_AUCTION = "/create/{ownerId}";
    String CREATE_BID_AUCTION = "/create/bid";
    String TERMINATE_AUCTION = "/terminate/{auctionId}";
    String GET_CLOSE_BID = "/{auctionId}/closebid";	
    String GET_OPEN_AUCTIONS = "/open";
    String GET_CLOSED_AUCTIONS = "/closed";
    String GET_AUCTION_BIDS = "/{auctionId}/bids";
    String GET_CLIENT_BIDS = "/client/{clientId}";
    
    @PostMapping(value = CREATE_AUCTION)
    List<AsyncReply> createAuction(@PathVariable("ownerId") String ownerId) throws InterruptedException;

    @PutMapping(value = TERMINATE_AUCTION)
    List<AsyncReply> terminateAuction(@PathVariable("auctionId") long auctionId);
   
    @GetMapping(
            value = GET_OPEN_AUCTIONS,
            produces = APPLICATION_JSON_VALUE)
    List<AsyncReply> getOpenAuctions();

    @GetMapping(
            value = GET_CLOSED_AUCTIONS,
            produces = APPLICATION_JSON_VALUE)
    List<AsyncReply> getClosedAuction();

    @GetMapping(
            value = GET_AUCTION_BIDS,
            produces = APPLICATION_JSON_VALUE)
    List<AsyncReply> getAuctionBids(@PathVariable("auctionId") long auctionId);
    
    @GetMapping(
            value = GET_CLIENT_BIDS,
            produces = APPLICATION_JSON_VALUE)
    List<AsyncReply> getClientBids(@PathVariable("clientId") String clientId);
    
    @GetMapping(
            value = GET_CLOSE_BID,
            produces = APPLICATION_JSON_VALUE)
    List<AsyncReply> getCloseBid(@PathVariable("auctionId") long auctionId);

    @PostMapping(
            value = CREATE_BID_AUCTION,
            consumes = APPLICATION_JSON_VALUE)
    List<AsyncReply> makeBid(@RequestBody Bid bid);
}

