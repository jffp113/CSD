package pt.unl.fct.csd.Controller;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import pt.unl.fct.csd.Exceptions.InsufficientFundsForTransactionException;
import pt.unl.fct.csd.Exceptions.TransactionAmountNotValidException;
import pt.unl.fct.csd.Exceptions.UserDoesNotExistException;
import pt.unl.fct.csd.Model.Transaction;
import pt.unl.fct.csd.Model.UserAccount;
import pt.unl.fct.csd.Repository.TransactionRepository;
import pt.unl.fct.csd.Repository.UserAccountRepository;
import pt.unl.fct.csd.SmartContract.AuthSmartContract;
import pt.unl.fct.csd.SmartContract.AuthSmartContractAdmin;
import pt.unl.fct.csd.SmartContract.AuthSmartContractImp;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.*;


@PropertySource("classpath:application.properties")
@Service("ImpSmart")
public class SmartContractControllerImp implements SmartContractController {

    public static final String SYSTEM_RESERVED_USER = "SYSTEM";
    private final Logger logger =
            LoggerFactory.getLogger(SmartContractControllerImp.class);


    //Guardar em ficheiro
    Map<String,AuthSmartContract> smartMap = new HashMap<>();

    @Value("${user.admin.token}")
    private String adminToken;

    @PostConstruct
    public void init(){
        smartMap.put(adminToken,new AuthSmartContractAdmin(adminToken));
    }

    @Override
    public void createSmart(String token, AuthSmartContractImp smartContractBytes) {
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
    public AuthSmartContract getSmartContract(String token) {
        return smartMap.getOrDefault(token,(a,b) -> false);
    }

}
