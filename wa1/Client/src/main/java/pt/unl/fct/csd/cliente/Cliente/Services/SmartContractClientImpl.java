package pt.unl.fct.csd.cliente.Cliente.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pt.unl.fct.csd.cliente.Cliente.Handlers.RestTemplateHeaderModifierInterceptor;
import pt.unl.fct.csd.cliente.Cliente.Handlers.RestTemplateResponseErrorHandler;
import pt.unl.fct.csd.cliente.Cliente.exceptions.ServerAnswerException;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
@PropertySource("classpath:application.properties")
public class SmartContractClientImpl implements SmartContractClient {

    @Value("${client.server.url}")
    private String BASE;

    @Value("${token}")
    private String token;

    private static String SMART_CONTROLLER =  "/smart";
    private static String CREATE_SMART = SMART_CONTROLLER + "/create/";
    private static String LIST_SMART = SMART_CONTROLLER + "/list";
    private static String REMOVE_SMART = SMART_CONTROLLER + "/remove/";

    private RestTemplate restTemplate;


    static {
        //For localhost testing only
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                (hostname,sslSession) -> {return true;});
    }


    @Autowired
    public SmartContractClientImpl(RestTemplateBuilder restTemplateBuilder, Environment env) {
        System.setProperty("javax.net.ssl.trustStore", env.getProperty("client.ssl.trust-store"));
        System.setProperty("javax.net.ssl.trustStorePassword",env.getProperty("client.ssl.trust-store-password"));
        restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    @PostConstruct
    public void init() {
        List<ClientHttpRequestInterceptor> list = new LinkedList<>();
        list.add(new RestTemplateHeaderModifierInterceptor(token));
        restTemplate.setInterceptors(list);
    }


    @Override
    public void createSmart(String token, List<String> smartContract) throws ServerAnswerException {
        ResponseEntity<String> response = restTemplate.postForEntity(BASE + CREATE_SMART + token,
                createSmartContract(smartContract), String.class);
        new HandleServerAnswer<Void>().processServerAnswer(response, Void.class);
    }

    private byte[] createSmartContract(List<String> smartContract){
        StringBuilder builder = new StringBuilder();

        builder.append("a = []\n");
        for(String path : smartContract) {
            builder.append("a.append(\"");
            builder.append(path);
            builder.append("\")\n");

        }
        builder.append("def auth(path):\n");
        builder.append("\tfor i in a:\n");
        builder.append("\t\tif i == path:\n");
        builder.append("\t\t\treturn True\n");
        builder.append("\treturn False");

        return builder.toString().getBytes();
    }

    @Override
    public List<String> ledgerSmartContracts() throws ServerAnswerException {
        ResponseEntity<String> response = restTemplate.getForEntity(BASE + CREATE_SMART, String.class);

        return Arrays.asList(
                new HandleServerAnswer< String[]>().processServerAnswer(response, String[].class));
    }

    @Override
    public void deleteSmartContract(String token) {
        restTemplate.delete(BASE + REMOVE_SMART + token);
    }

}
