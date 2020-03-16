package pt.unl.fct.csd.cliente.Cliente.Services;


import pt.unl.fct.csd.cliente.Cliente.Model.Transaction;

import java.util.List;

public interface WalletClient {

    void createMoney(String toUser, Long amount);

    void transferMoney(String fromUser, String toUser, Long amount);

    Long currentAmount(String userID);

    List<Transaction> ledgerOfGlobalTransfers();

    List<Transaction> LedgerOfClientTransfers(String userId);
}
