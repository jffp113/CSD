package pt.unl.fct.csd.SmartContract;

import javax.persistence.Entity;
import java.util.Map;


public class AuthSmartContractImp implements AuthSmartContract {

    public Map<String,Boolean> authmap;
    public String userToken;

    public AuthSmartContractImp() {

    }

    public AuthSmartContractImp(Map<String, Boolean> auth, String userToken) {
        this.authmap = auth;
        this.userToken = userToken;
    }

    @Override
    public boolean canDoOperation(String matchToken, String path) {


        return userToken.equals(matchToken) &&
                authmap.getOrDefault(path,false);
    }

}
