package pt.unl.fct.csd.cliente.Cliente.Model;

import java.util.Map;

public class AuthSmartContractImp{

    public Map<String,Boolean> authmap;
    public String userToken;


    public AuthSmartContractImp(){

    }

    public AuthSmartContractImp(Map<String, Boolean> auth, String userToken) {
        this.authmap = auth;
        this.userToken = userToken;
    }

    public boolean canDoOperation(String matchToken, String path) {


        return userToken.equals(matchToken) &&
                authmap.getOrDefault(path,false);
    }

    public Map<String, Boolean> getAuthmap() {
        return authmap;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setAuthmap(Map<String, Boolean> authmap) {
        this.authmap = authmap;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
