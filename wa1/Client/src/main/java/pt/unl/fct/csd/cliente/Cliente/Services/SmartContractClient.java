package pt.unl.fct.csd.cliente.Cliente.Services;


import org.springframework.web.bind.annotation.*;
import pt.unl.fct.csd.cliente.Cliente.Model.AuthSmartContract;
import pt.unl.fct.csd.cliente.Cliente.Model.AuthSmartContractImp;
import pt.unl.fct.csd.cliente.Cliente.exceptions.ServerAnswerException;

import java.util.List;


public interface SmartContractClient {

    void createSmart(String token, AuthSmartContractImp smartContract) throws ServerAnswerException;

    List<String> ledgerSmartContracts() throws ServerAnswerException;

    void deleteSmartContract(String token);


}

