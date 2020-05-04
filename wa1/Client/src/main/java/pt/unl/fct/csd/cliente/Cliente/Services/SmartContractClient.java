package pt.unl.fct.csd.cliente.Cliente.Services;


import pt.unl.fct.csd.cliente.Cliente.exceptions.ServerAnswerException;

import java.util.List;


public interface SmartContractClient {

    void createSmart(String token, List<String> smartContract) throws ServerAnswerException;

    List<String> ledgerSmartContracts() throws ServerAnswerException;

    void deleteSmartContract(String token);


}

