package pt.unl.fct.csd.cliente.Cliente.Services;


import pt.unl.fct.csd.cliente.Cliente.Model.Transaction;
import pt.unl.fct.csd.cliente.Cliente.exceptions.ServerAnswerException;

import java.util.List;

public interface WalletClient {

    void createMoney(String toUser, Long amount) throws ServerAnswerException;

    void transferMoney(String fromUser, String toUser, Long amount) throws ServerAnswerException;

    Long currentAmount(String userID) throws ServerAnswerException;

    List<Transaction> ledgerOfGlobalTransfers() throws ServerAnswerException;

    List<Transaction> LedgerOfClientTransfers(String userId) throws ServerAnswerException;
}
