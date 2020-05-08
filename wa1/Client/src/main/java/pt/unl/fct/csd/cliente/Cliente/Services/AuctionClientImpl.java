package pt.unl.fct.csd.cliente.Cliente.Services;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

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
import pt.unl.fct.csd.cliente.Cliente.Model.AsyncReply;
import pt.unl.fct.csd.cliente.Cliente.Model.Auction;
import pt.unl.fct.csd.cliente.Cliente.Model.Bid;
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
		ResponseEntity<String> response =
				restTemplate.postForEntity(urlWithId, null, String.class);
		return new HandleServerAnswer<Long>().processServerAnswer(response, Long.class);
	}

	@Override
	public void terminateAuction(long auctionId) throws ServerAnswerException {
		String urlWithId = String.format(Path.TERMINATE_AUCTION.url, BASE, auctionId);
		//restTemplate.put(urlWithId, null);	//TODO CHECK THIS ONE ERROR HANDLING
		ResponseEntity<String> response =
				restTemplate.exchange(urlWithId, HttpMethod.PUT, null, String.class);
		new HandleServerAnswer<Void>().processServerAnswer(response, Void.class);
	}

	@Override
	public List<Auction> getOpenAuctions() throws ServerAnswerException {
		String urlComplete = String.format(Path.GET_OPEN_AUCTIONS.url, BASE);
		return new ExtractAnswer<List<Auction>>().extractAnswer(urlComplete);
		//return new ListObjects<Auction>().getListFromUrl(urlComplete, Auction[].class);
	}

	@Override
	public List<Auction> getClosedAuctions() throws ServerAnswerException {
		String urlComplete = String.format(Path.GET_CLOSED_AUCTIONS.url, BASE);
		return new ListObjects<Auction>().getListFromUrl(urlComplete, Auction[].class);
	}

	@Override
	public List<Bid> getAuctionBids(long auctionId) throws ServerAnswerException {
		String urlComplete = String.format(Path.GET_AUCTION_BIDS.url, BASE, auctionId);
		return new ListObjects<Bid>().getListFromUrl(urlComplete, Bid[].class);
	}

	@Override
	public List<Bid> getClientBids(String clientId) throws ServerAnswerException {
		String urlComplete = String.format(Path.GET_CLIENT_BIDS.url, BASE, clientId);
		return new ListObjects<Bid>().getListFromUrl(urlComplete, Bid[].class);
		/*Bid[] bids = restTemplate.
				getForEntity(urlComplete, Bid[].class).
				getBody();
		return Arrays.asList(bids);*/
	}

	@Override
	public Bid getClosedBid(long auctionId) throws ServerAnswerException {
		String urlWithId = String.format(Path.GET_CLOSE_BID.url, BASE, auctionId);
		ResponseEntity<String> response = restTemplate.getForEntity(urlWithId, String.class);
		return new HandleServerAnswer<Bid>().processServerAnswer(response, Bid.class);
	}

	@Override
	public Long createBid(String bidderId, Long auctionId, int value) throws ServerAnswerException {
		Bid bid = new Bid(bidderId, auctionId, value);
		String urlComplete = String.format(Path.CREATE_BID.url, BASE);
		ResponseEntity<String> response =
				restTemplate.postForEntity(urlComplete,bid,String.class);
		return new HandleServerAnswer<Long>().processServerAnswer(response, Long.class);
	}

	private class ListObjects<E> {

    	private List<E> getListFromUrl (String url, Class<E[]> klass) throws ServerAnswerException {
    		ResponseEntity<String> response =
					restTemplate.getForEntity(url, String.class);
    		E[] elements = new HandleServerAnswer<E[]>().processServerAnswer(response, klass);
    		return Arrays.asList(elements);
		}
	}

	private class ExtractAnswer<E> {

    	private E extractAnswer (String url) throws ServerAnswerException {
			ResponseEntity<AsyncReply[]> response =
					restTemplate.getForEntity(url, AsyncReply[].class);
			AsyncReply[] replies = response.getBody();
			assert replies != null;
			List<byte[]> verifiedReplies = SignatureVerifyer.getVerifiedReplies(Arrays.asList(replies));
			try {
				return new ReplyProcessor<E>(verifiedReplies).getMostFrequentAnswer();
			} catch (Exception e) {
				throw new ServerAnswerException(e.getMessage());
			}
		}

	}
}
