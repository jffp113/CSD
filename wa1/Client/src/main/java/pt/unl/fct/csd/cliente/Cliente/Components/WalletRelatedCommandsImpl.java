package pt.unl.fct.csd.cliente.Cliente.Components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import pt.unl.fct.csd.cliente.Cliente.Anotation.NONAUTO;
import pt.unl.fct.csd.cliente.Cliente.Model.Transaction;
import pt.unl.fct.csd.cliente.Cliente.Services.WalletClient;
import pt.unl.fct.csd.cliente.Cliente.exceptions.ServerAnswerException;

import java.util.List;

@NONAUTO
@ShellComponent
public class WalletRelatedCommandsImpl {
    //TODO improvement include a Terminal class with colors

    private WalletClient client;

    @Autowired
    public WalletRelatedCommandsImpl(WalletClient client) {
        this.client = client;
    }

    @ShellMethod("Create money to a specified user")
    public String createMoney( @ShellOption() String toUser, @ShellOption() Long amount) {
        try {
            return tryToCreateMoney(toUser, amount);
        } catch (ServerAnswerException e) {
            return e.getMessage();
        }
    }

    private String tryToCreateMoney (String toUser, long amount) throws ServerAnswerException {
        client.createMoney(toUser,amount);
        return "Money created successfully";
    }

    @ShellMethod("Transfer money from a provided user to other provided user")
    public String transferMoney(@ShellOption() String fromUser, @ShellOption() String toUser, @ShellOption() Long amount)
    {
        try {
            return tryToTransferMoney(fromUser,toUser,amount);
        } catch (ServerAnswerException e) {
            return e.getMessage();
        }
    }

    private String tryToTransferMoney (String fromUser, String toUser, long amount) throws ServerAnswerException {
        client.transferMoney(fromUser, toUser, amount);
        return "Money was transfer.";
    }

    @ShellMethod("See the provided user money")
    public String currentAmount(@ShellOption() String id) {
        try {
            return tryToCurrentAmount(id);
        } catch (ServerAnswerException e) {
            return e.getMessage();
        }
    }

    private String tryToCurrentAmount(String id) throws ServerAnswerException {
        return "The user has " + client.currentAmount(id);
    }


    @ShellMethod("See the provided user money")
    public String ledger() {
        try {
            return tryToLedger();
        } catch (ServerAnswerException e) {
            return e.getMessage();
        }
    }

    private String tryToLedger() throws ServerAnswerException {
        return transformListOfTransactionInString(
                client.ledgerOfGlobalTransfers());
    }

    @ShellMethod("See the provided user money")
    public String clientLedger(@ShellOption() String id) {
        try {
            return tryToClientLedger(id);
        } catch (ServerAnswerException e) {
            return e.getMessage();
        }
    }

    private String tryToClientLedger(String id) throws ServerAnswerException {
        return transformListOfTransactionInString(
                client.LedgerOfClientTransfers(id));
    }

    private String transformListOfTransactionInString(List<Transaction> transactionsList){
        if (transactionsList.isEmpty())
            return "No transactions performed.";
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Ledger:\n");

        for(Transaction transaction : transactionsList){
            stringBuffer.append(String.format("Transfer from %s to %s of %d\n",
                    transaction.getFrom(),transaction.getTo(),transaction.getAmount()));
        }

        return stringBuffer.toString();
    }

}
