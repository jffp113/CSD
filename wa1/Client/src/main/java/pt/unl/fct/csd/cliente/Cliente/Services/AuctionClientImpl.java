package pt.unl.fct.csd.cliente.Cliente.Services;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import pt.unl.fct.csd.cliente.Cliente.Handlers.RestTemplateHeaderModifierInterceptor;
import pt.unl.fct.csd.cliente.Cliente.Handlers.RestTemplateResponseErrorHandler;
import pt.unl.fct.csd.cliente.Cliente.Model.Auction;
import pt.unl.fct.csd.cliente.Cliente.Model.Bid;
import pt.unl.fct.csd.cliente.Cliente.exceptions.ServerAnswerException;

import static pt.unl.fct.csd.cliente.Cliente.Services.Path.*;

@Service
@PropertySource("classpath:application.properties")
public class AuctionClientImpl implements AuctionClient {
	
    @Value("${client.server.url}")
    private String BASE;

	@Value("${token}")
	private String token;
    
    private RestTemplate restTemplate;

    private ExtractAnswer extractor;

    private final Gson g = new Gson();

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

		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(
				Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM}));
		restTemplate.setMessageConverters(Arrays.asList(converter, new FormHttpMessageConverter()));
    }

    @PostConstruct
    public void init() {
		List<ClientHttpRequestInterceptor> list = new LinkedList<>();
		list.add(new RestTemplateHeaderModifierInterceptor(token));
		restTemplate.setInterceptors(list);
	}

	private ExtractAnswer getExtractor() {
    	if(extractor == null)
    		extractor = new ExtractAnswer(BASE, restTemplate);
    	return extractor;
	}

	@Override
	public Long createAuction(String ownerId) throws ServerAnswerException {
		String uid = String.format("{\"ownerId\":\"%s\"}", ownerId);
		String longJson = getExtractor().extractOrderedAnswer(CREATE_AUCTION.name(), uid);
		return Long.valueOf(longJson);
    }

	@Override
	public void terminateAuction(long auctionId) throws ServerAnswerException {
    	String id = String.format("{\"auctionId\":\"%d\"}", auctionId);
		getExtractor().extractOrderedAnswer(TERMINATE_AUCTION.name(), id);
	}

	@Override
	public List<Auction> getOpenAuctions() throws ServerAnswerException {
		String openJson = getExtractor().extractUnorderedAnswer(GET_OPEN_AUCTIONS.name(), "");
		return Arrays.asList(new Gson().fromJson(openJson, Auction[].class));
	}

	@Override
	public List<Auction> getClosedAuctions() throws ServerAnswerException {
		String closedJson = getExtractor().extractUnorderedAnswer(GET_CLOSED_AUCTIONS.name(), "");
		return Arrays.asList(new Gson().fromJson(closedJson, Auction[].class));
	}

	@Override
	public List<Bid> getAuctionBids(long auctionId) throws ServerAnswerException {
		String id = String.format("{\"auctionId\": %d}", auctionId);
		String bidsJson = getExtractor().extractUnorderedAnswer(GET_AUCTION_BIDS.name(), id);
		return Arrays.asList(new Gson().fromJson(bidsJson, Bid[].class));
	}

	@Override
	public List<Bid> getClientBids(String clientId) throws ServerAnswerException {
		String id = String.format("{\"clientId\":\"%s\"}", clientId);
		String bidsJson = getExtractor().extractUnorderedAnswer(GET_CLIENT_BIDS.name(), id);
		return Arrays.asList(new Gson().fromJson(bidsJson, Bid[].class));
	}

	@Override
	public Bid getClosedBid(long auctionId) throws ServerAnswerException {
		String id = String.format("{\"auctionId\": %d}", auctionId);
		String bidJson = getExtractor().extractUnorderedAnswer(GET_CLOSE_BID.name(), id);
		return new Gson().fromJson(bidJson, Bid.class);
	}

	@Override
	public Long createBid(String bidderId, Long auctionId, int value) throws ServerAnswerException {
		String bid = g.toJson(new Bid(bidderId, auctionId, value));
		String longJson = getExtractor().extractOrderedAnswer(CREATE_BID_AUCTION.name(), bid);
		return Long.valueOf(longJson);
	}

}