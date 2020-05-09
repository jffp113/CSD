package pt.unl.fct.csd.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;


@PropertySource("classpath:application.properties")
@Service("ImpSmart")
public class SmartContractControllerImp implements SmartContractController {

    public static final String SYSTEM_RESERVED_USER = "SYSTEM";
    private final Logger logger =
            LoggerFactory.getLogger(SmartContractControllerImp.class);


    //Guardar em ficheiro
    Map<String,byte[]> smartMap = new HashMap<>();

    @Value("${user.admin.token}")
    private String adminToken;

    @PostConstruct
    public void init(){
        StringBuilder builder = new StringBuilder();
        builder.append("def auth(path):\n")
                .append("\treturn True");

        smartMap.put(adminToken,builder.toString().getBytes());
    }

    @Override
    public void createSmart(String token, byte[] smartContractBytes) {
        try {
            smartMap.put(token,smartContractBytes);
        } catch (Exception e) {
           logger.error("Error reading smartContract");
           throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> ledgerSmartContracts() {
        List<String> result = new ArrayList<>(smartMap.size());
        result.addAll(smartMap.keySet());
        return result;
    }

    @Override
    public void deleteSmartContract(String token) {
        smartMap.remove(token);
    }

    @Override
    public byte[] getSmartContract(String token) {
        StringBuilder builder = new StringBuilder();
        builder.append("def auth(path):\n")
                .append("\treturn False");
        return smartMap.getOrDefault(token,builder.toString().getBytes());
    }

}
