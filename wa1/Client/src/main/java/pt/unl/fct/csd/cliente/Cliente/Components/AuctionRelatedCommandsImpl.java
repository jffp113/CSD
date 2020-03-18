package pt.unl.fct.csd.cliente.Cliente.Components;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import pt.unl.fct.csd.cliente.Cliente.Model.Auction;
import pt.unl.fct.csd.cliente.Cliente.Model.Bid;
import pt.unl.fct.csd.cliente.Cliente.Services.AuctionClient;

@ShellComponent
public class AuctionRelatedCommandsImpl {
	//TODO improvement include a Terminal class with colors

    private AuctionClient client;

    @Autowired
    public AuctionRelatedCommandsImpl(AuctionClient client) {
        this.client = client;
    }

    @ShellMethod("Creates an auction owned by the specified user")
    public String createAuction( @ShellOption() String toUser) {
        client.createAuction(toUser);
        //TODO model exceptions
        return "Auction was created with success";
    }

    @ShellMethod("Lists the bids from an auction")
    public String getAuctionBids(@ShellOption() long auctionId)
    {
        //TODO model exceptions
    	List<Bid> bids = client.getAuctionBids(auctionId);
    	return listBids(bids);
    }

    @ShellMethod("Lists the bids from a client")
    public String getClientBids(@ShellOption() String clientId) {
        //TODO model exceptions
    	List<Bid> bids = client.getClientBids(clientId);
    	return listBids(bids);
    }
    
    private String listBids(List<Bid> bids) {
    	StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Auction Bids:\n");
        for (Bid bid : bids) {
        	String toPrint = String.format("Bid from %s with amount %d\n", 
        			bid.getBidderId(), bid.getValue());
        	stringBuffer.append(toPrint);
        }
        return stringBuffer.toString();
    }


    @ShellMethod("Lists the open auctions in the system")
    public String getOpenAuctions() {
    	List<Auction> auctions = client.getOpenAuctions();
    	return listAuctions(auctions);
    }

    @ShellMethod("Lists the closed auctions in the system")
    public String getClosedAuctions() {
    	List<Auction> auctions = client.getClosedAuctions();
    	return listAuctions(auctions);
    }
    
    private String listAuctions(List<Auction> auctions) {
    	StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Auctions:\n");
        for (Auction auction : auctions) {
        	String toPrint = String.format("An auction with id %d from %s\n", 
        			auction.getId(), auction.getOwnerId());
        	stringBuffer.append(toPrint);
        }
        return stringBuffer.toString();
    }

    @ShellMethod("Terminates an auction")
    public String terminateAuction( @ShellOption() long auctionId) {
        client.terminateAuction(auctionId);
        return "Auction was terminated with success";
    }
    
    @ShellMethod("Returns the id of the bid that closes a specified auction")
    public String getCloseBid( @ShellOption() long auctionId) {
        Bid bid = client.getClosedBid(auctionId);
        return "Auction was by the bid: " + bid.getAuctionId();
    }

    @ShellMethod("Create a bid in a specified auction with a determinated amount from a certrain user")
    public String createBid(@ShellOption() String bidderId, @ShellOption() Long auctionId, @ShellOption() int value){
        client.createBid(bidderId, auctionId, value);
        return "Bid Created";
    }

}
