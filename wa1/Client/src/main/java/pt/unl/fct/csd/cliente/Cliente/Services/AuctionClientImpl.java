package pt.unl.fct.csd.cliente.Cliente.Services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import pt.unl.fct.csd.cliente.Cliente.Handlers.RestTemplateResponseErrorHandler;
import pt.unl.fct.csd.cliente.Cliente.Model.Auction;
import pt.unl.fct.csd.cliente.Cliente.Model.Bid;
import pt.unl.fct.csd.cliente.Cliente.Model.InvokerWrapper;
import pt.unl.fct.csd.cliente.Cliente.Model.SystemReply;
import pt.unl.fct.csd.cliente.Cliente.exceptions.NoMajorityAnswerException;
import pt.unl.fct.csd.cliente.Cliente.exceptions.ServerAnswerException;

@Service
@PropertySource("classpath:application.properties")
public class AuctionClientImpl implements AuctionClient {
	
    @Value("${client.server.url}")
    private String BASE;

    
    //CONSTANTES
    private enum Path {
    	CREATE_AUCTION("/create/%s"),
    	CREATE_BID("/create/bid"),
    	TERMINATE_AUCTION("/terminate/%s"),
    	GET_CLOSE_BID("/%d/closebid"),
    	GET_OPEN_AUCTIONS("/open"),
    	GET_CLOSED_AUCTIONS("/closed"),
    	GET_AUCTION_BIDS("/%d/bids"),
    	GET_CLIENT_BIDS("/client/%s");
    	
    	private static final String BASE_URL = "/auctions";
    	private final String url;
    	
    	Path(String url) {
    		this.url = "%s" + BASE_URL + url;
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
	public Long createAuction(String ownerId) throws ServerAnswerException {
		String urlWithId = String.format(Path.CREATE_AUCTION.url, BASE, ownerId);
		return new ExtractAnswer<Double>().extractAnswerPost(urlWithId, null, restTemplate).longValue();
    }

	@Override
	public void terminateAuction(long auctionId) throws ServerAnswerException {
		String urlWithId = String.format(Path.TERMINATE_AUCTION.url, BASE, auctionId);
		new ExtractAnswer<Long>().extractAnswerPut(urlWithId, restTemplate);
	}

	@Override
	public List<Auction> getOpenAuctions() throws ServerAnswerException {
		String urlComplete = String.format(Path.GET_OPEN_AUCTIONS.url, BASE);
		return Arrays.asList(new ExtractAnswer<Auction[]>().extractAnswerGet(urlComplete, restTemplate));
	}

	@Override
	public List<Auction> getClosedAuctions() throws ServerAnswerException {
		String urlComplete = String.format(Path.GET_CLOSED_AUCTIONS.url, BASE);
		return Arrays.asList(new ExtractAnswer<Auction[]>().extractAnswerGet(urlComplete, restTemplate));
	}

	@Override
	public List<Bid> getAuctionBids(long auctionId) throws ServerAnswerException {
		String urlComplete = String.format(Path.GET_AUCTION_BIDS.url, BASE, auctionId);
		return Arrays.asList(new ExtractAnswer<Bid[]>().extractAnswerGet(urlComplete, restTemplate));
	}

	@Override
	public List<Bid> getClientBids(String clientId) throws ServerAnswerException {
		String urlComplete = String.format(Path.GET_CLIENT_BIDS.url, BASE, clientId);
		return Arrays.asList(new ExtractAnswer<Bid[]>().extractAnswerGet(urlComplete, restTemplate));
	}

	@Override
	public Bid getClosedBid(long auctionId) throws ServerAnswerException {
		String urlWithId = String.format(Path.GET_CLOSE_BID.url, BASE, auctionId);
		return new ExtractAnswer<Bid>().extractAnswerGet(urlWithId, restTemplate);
	}

	@Override
	public Long createBid(String bidderId, Long auctionId, int value) throws ServerAnswerException {
		Bid bid = new Bid(bidderId, auctionId, value);
		String urlComplete = String.format(Path.CREATE_BID.url, BASE);
		return new ExtractAnswer<Double>().extractAnswerPost(urlComplete, bid, restTemplate).longValue();
	}

}
