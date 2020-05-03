package pt.unl.fct.csd.cliente.Cliente.Components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import pt.unl.fct.csd.cliente.Cliente.Anotation.NONAUTO;
import pt.unl.fct.csd.cliente.Cliente.Model.AuthSmartContract;
import pt.unl.fct.csd.cliente.Cliente.Model.AuthSmartContractImp;
import pt.unl.fct.csd.cliente.Cliente.Model.Transaction;
import pt.unl.fct.csd.cliente.Cliente.Services.SmartContractClient;
import pt.unl.fct.csd.cliente.Cliente.Services.WalletClient;
import pt.unl.fct.csd.cliente.Cliente.exceptions.ServerAnswerException;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NONAUTO
@ShellComponent
public class SmartContractRelatedCommandsImpl {

    private SmartContractClient client;

    @Autowired
    public SmartContractRelatedCommandsImpl(SmartContractClient client) {
        this.client = client;
    }


    @ShellMethod("Create smart contract to a token")
    public void createSmart(@ShellOption() String token,
                            @ShellOption( defaultValue = "false") boolean wallet ,
                            @ShellOption( defaultValue = "false") boolean auction,
                            @ShellOption( defaultValue = "false") boolean smartContract){

        Map<String,Boolean> map = new HashMap<>();

        if(smartContract){
            map.put("smart",true);
        }
        if (wallet) {
            map.put("money",true);
        }
        if (auction) {
            map.put("auctions",true);
        }


        try {
            client.createSmart(token,new AuthSmartContractImp(map,token));
        } catch (ServerAnswerException e) {
           throw new RuntimeException(e);
        }


    };

    public List<String> ledgerSmartContracts(){
        return  null;
    }

    public void deleteSmartContract(String token){

    }



}
