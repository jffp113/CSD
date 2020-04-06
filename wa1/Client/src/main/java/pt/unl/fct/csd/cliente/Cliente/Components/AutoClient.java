package pt.unl.fct.csd.cliente.Cliente.Components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import pt.unl.fct.csd.cliente.Cliente.Anotation.AUTO;
import pt.unl.fct.csd.cliente.Cliente.Services.AuctionClient;
import pt.unl.fct.csd.cliente.Cliente.Services.WalletClient;
import pt.unl.fct.csd.cliente.Cliente.exceptions.ServerAnswerException;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

@AUTO
@Component
public class AutoClient {

    public static final int CREATE_MONEY_REPS = 1000;
    public static final int USERNAME_SIZE = 5000;
    public static final int MAX_MONEY_GEN = 2000;
    public static final int TIME_TO_TEST = 2000;

    @Autowired
    AuctionClient auctionClient;

    @Autowired
    WalletClient walletClient;

    private Map<String,Long> accountsMoney;
    private List<String> usernames;

    private Logger logger = Logger.getLogger(AutoClient.class.getName());

    @Autowired
    ResourceLoader resourceLoader;


    @PostConstruct
    public void init() throws Exception{
        accountsMoney = new HashMap<>();
        usernames = new ArrayList<>(USERNAME_SIZE);
        loadUsername();
        createMoney();
    }

    private void createMoney() throws Exception {
        Random random = new Random(System.currentTimeMillis());
        for(int i = 0; i < CREATE_MONEY_REPS; i++){
            String username = usernames.get(random.nextInt(usernames.size()));
            Long money = Long.valueOf(random.nextInt(MAX_MONEY_GEN));

            Long value = accountsMoney.getOrDefault(username,0l);
            accountsMoney.put(username,value + money);

            logger.info(String.format("[%d/%d] Creating Money for %s amount = %d",i,CREATE_MONEY_REPS,username,money));
            try {
                walletClient.createMoney(username,money);
            } catch (ServerAnswerException e) {
                e.printStackTrace();
            }
        }

        Thread.sleep(TIME_TO_TEST);
        verifyCurrentMoney();
    }

    private void verifyCurrentMoney() {
        logger.info("Verifying Money");
        accountsMoney.forEach((username,amount) -> {
            try {
                Long amountR = walletClient.currentAmount(username);
                if(!amount.equals(amountR)){
                    logger.severe(String.format("Got %d should have gotten %d for %s", amountR,amount,username));
                }
            } catch (ServerAnswerException e) {
                logger.severe("Got server error: " + e.getMessage());
            }
        });

        logger.info("Verified Money");
    }

    private void loadUsername() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:accounts");
        BufferedReader in = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        String username;

        logger.info("Init usernames");

        while((username = in.readLine()) != null){
            usernames.add(username);
        }
    }

}
