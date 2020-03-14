package pt.unl.fct.csd.cliente.Cliente.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pt.unl.fct.csd.cliente.Cliente.Handlers.RestTemplateResponseErrorHandler;
import pt.unl.fct.csd.cliente.Cliente.Model.Transaction;

import javax.annotation.PostConstruct;

@Service
@PropertySource("classpath:application.properties")
public class ClientImpl implements Client {

    @Value("${client.server.url}")
    private String BASE;


    private static String WALLET_CONTROLLER =  "/money";
    private static String CREATE_MONEY = WALLET_CONTROLLER + "/create";
    private static String TRANSFER_MONEY = WALLET_CONTROLLER + "/transfer";
    private static String GET_MONEY = WALLET_CONTROLLER + "/current/";

    private RestTemplate restTemplate;

    static {
        //For localhost testing only
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                (hostname,sslSession) -> {return true;});
    }

    @Autowired
    public ClientImpl(RestTemplateBuilder restTemplateBuilder,Environment env){
        System.setProperty("javax.net.ssl.trustStore", env.getProperty("client.ssl.trust-store"));
        System.setProperty("javax.net.ssl.trustStorePassword",env.getProperty("client.ssl.trust-store-password"));
        restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    @PostConstruct
    public void init() {

    }

    @Override
    public void createMoney(String toUser, Long amount) {
        Transaction transaction = new Transaction(toUser,amount);
        restTemplate.postForEntity(BASE + CREATE_MONEY, transaction, Void.class);
    }

    @Override
    public void transferMoney(String fromUser, String toUser, Long amount) {
        Transaction transaction = new Transaction(fromUser,toUser,amount);
        restTemplate.postForEntity(BASE + TRANSFER_MONEY, transaction, Void.class);
    }

    @Override
    public Long currentAmount(String userID) {
        return restTemplate.getForEntity(BASE + GET_MONEY + userID, Long.class).getBody();
    }
}
