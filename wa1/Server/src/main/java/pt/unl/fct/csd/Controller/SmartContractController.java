package pt.unl.fct.csd.Controller;

import java.util.List;

public interface SmartContractController {

    void createSmart(String token, byte[] smartContract);

    List<String> ledgerSmartContracts();

    void deleteSmartContract(String token);

    byte[] getSmartContract(String token);
}
