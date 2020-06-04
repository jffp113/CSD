package pt.unl.fct.csd.cliente.Cliente.Services;

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
import pt.unl.fct.csd.cliente.Cliente.Model.Transaction;
import pt.unl.fct.csd.cliente.Cliente.exceptions.ServerAnswerException;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static pt.unl.fct.csd.cliente.Cliente.Services.Path.*;

@Service
@PropertySource("classpath:application.properties")
public class WalletClientImpl implements WalletClient {

    @Value("${client.server.url}")
    private String BASE;

    @Value("${token}")
    private String token;

    private final Gson g = new Gson();

    private RestTemplate restTemplate;

    private ExtractAnswer extractor;

    static {
        //For localhost testing only
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                (hostname,sslSession) -> {return true;});
    }

    @Autowired
    public WalletClientImpl(RestTemplateBuilder restTemplateBuilder,Environment env) {
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
        if (extractor == null)
            extractor = new ExtractAnswer(BASE, restTemplate);
        return extractor;
    }

    @Override
    public void createMoney(String toUser, Long amount) throws ServerAnswerException {
        String transaction = g.toJson(new Transaction(toUser,amount));
        getExtractor().extractOrderedAnswer(CREATE_MONEY.name(), transaction);
    }

    @Override
    public void transferMoney(String fromUser, String toUser, Long amount) throws ServerAnswerException {
        String transaction = g.toJson(new Transaction(fromUser,toUser,amount));
        getExtractor().extractOrderedAnswer(TRANSFER_MONEY.name(), transaction);
    }

    @Override
    public Long currentAmount(String userID) throws ServerAnswerException{
        String uid = String.format("{\"userId\":\"%s\"}", userID);
        String longJson = getExtractor().extractUnorderedAnswer(GET_MONEY.name(), uid);
        return Long.valueOf(longJson);
    }

    @Override
    public List<Transaction> ledgerOfGlobalTransfers() throws ServerAnswerException{
        return getLedgerFromPath(GET_LEDGER.name(), "");
    }

    @Override
    public List<Transaction> LedgerOfClientTransfers(String userId) throws ServerAnswerException {
        String uid = String.format("{\"userId\":\"%s\"}", userId);
        return getLedgerFromPath(GET_CLIENT_LEDGER.name(), uid);
    }

    private List<Transaction> getLedgerFromPath(String path, String post) throws ServerAnswerException {
        String transactionsJson = getExtractor().extractUnorderedAnswer(path, post);
        return Arrays.asList(new Gson().fromJson(transactionsJson, Transaction[].class));
    }

}