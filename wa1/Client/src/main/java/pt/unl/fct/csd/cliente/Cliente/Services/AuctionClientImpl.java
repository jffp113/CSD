package pt.unl.fct.csd.cliente.Cliente.Services;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import pt.unl.fct.csd.cliente.Cliente.Handlers.RestTemplateResponseErrorHandler;
import pt.unl.fct.csd.cliente.Cliente.Model.Auction;
import pt.unl.fct.csd.cliente.Cliente.Model.Bid;

@Service
@PropertySource("classpath:application.properties")
public class AuctionClientImpl implements AuctionClient {
	
    @Value("${client.server.url}")
    private String BASE;

    
    //CONSTANTES
    private enum Path {
    	CREATE_AUCTION("/create"),
    	TERMINATE_AUCTION("/terminate/%s"),
    	GET_CLOSE_BID("/%d/closebid"),
    	GET_OPEN_AUCTIONS("/open"),
    	GET_CLOSED_AUCTIONS("/closed"),
    	GET_AUCTION_BIDS("/%d/bids"),
    	GET_CLIENT_BIDS("/client/%s");
    	
    	private static final String BASE_URL = "/auction";
    	private final String url;
    	
    	private Path(String url) {
    		this.url = BASE_URL + url;
    	}
    }
    
    private RestTemplate restTemplate;

    static {
        //For localhost testing only
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                (hostname,sslSession) -> {return true;});
    }
    
    @Autowired
    public AuctionClientImpl(RestTemplateBuilder restTemplateBuilder,Environment env){
        System.setProperty("javax.net.ssl.trustStore", env.getProperty("client.ssl.trust-store"));
        System.setProperty("javax.net.ssl.trustStorePassword",env.getProperty("client.ssl.trust-store-password"));
        restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    @PostConstruct
    public void init() {}


	@Override
	public void createAuction(String clientId) {
		Auction auction = new Auction(clientId);
        restTemplate.postForEntity(Path.CREATE_AUCTION.url, auction, Void.class);
	}

	@Override
	public void terminateAuction(long auctionId) {
		String urlWithId = String.format(Path.TERMINATE_AUCTION.url, auctionId);
		restTemplate.put(urlWithId, null); //TODO CHANGE TO GOOD
	}

	@Override
	public List<Auction> getOpenAuctions() {
		Auction[] auctions = restTemplate.
				getForEntity(Path.GET_OPEN_AUCTIONS.url, Auction[].class).
				getBody();
		return Arrays.asList(auctions);
	}

	@Override
	public List<Auction> getClosedAuctions() {
		Auction[] auctions = restTemplate.
				getForEntity(Path.GET_CLOSED_AUCTIONS.url, Auction[].class).
				getBody();
		return Arrays.asList(auctions);
	}

	@Override
	public List<Bid> getAuctionBids(long auctionId) {
		Bid[] bids = restTemplate.
				getForEntity(Path.GET_AUCTION_BIDS.url, Bid[].class).
				getBody();
		return Arrays.asList(bids);
	}

	@Override
	public List<Bid> getClientBids(String clientId) {
		Bid[] bids = restTemplate.
				getForEntity(Path.GET_CLIENT_BIDS.url, Bid[].class).
				getBody();
		return Arrays.asList(bids);
	}

	@Override
	public Bid getClosedBid(long Auction) {
		return restTemplate.
				getForEntity(Path.GET_CLOSE_BID.url, Bid.class).
				getBody();
	}

}
