package pt.unl.fct.csd.cliente.Cliente.Components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import pt.unl.fct.csd.cliente.Cliente.Anotation.NONAUTO;
import pt.unl.fct.csd.cliente.Cliente.Services.SmartContractClient;
import pt.unl.fct.csd.cliente.Cliente.exceptions.ServerAnswerException;

import java.util.LinkedList;
import java.util.List;

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

       // Map<String,Boolean> map = new HashMap<>();
        List<String> paths = new LinkedList<>();
        if(smartContract){
            paths.add("smart");
        }
        if (wallet) {
            paths.add("money");
        }
        if (auction) {
            paths.add("auctions");
        }


        try {
            client.createSmart(token,paths);
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
