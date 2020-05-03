package pt.unl.fct.csd.SmartContract;


import pt.unl.fct.csd.Replication.Path;

public class AuthSmartContractAdmin implements AuthSmartContract{

    String token;

    public AuthSmartContractAdmin(String token) {
        this.token = token;
    }

    public boolean canDoOperation(String matchToken, String path){
        return token.equals(matchToken);
    }

}
