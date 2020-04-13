package pt.unl.fct.csd.Controller;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import pt.unl.fct.csd.Model.Transaction;
import pt.unl.fct.csd.Model.UserAccount;
import pt.unl.fct.csd.Repository.TransactionRepository;
import pt.unl.fct.csd.Repository.UserAccountRepository;

import java.util.LinkedList;
import java.util.List;


@Service("walletByz")
public class WalletControllerByzantineImp implements WalletController {

    public static final String SYSTEM_RESERVED_USER = "SYSTEM";
    private final Logger logger =
            LoggerFactory.getLogger(WalletControllerByzantineImp.class);

    @Override
    public void createMoney(Transaction transaction) {
        logger.info("I'm byzantine");
    }

    private UserAccount getOrCreateUser(String userId) {
        return new UserAccount("Troll",100000l);
    }

    @Override
    public void transferMoneyBetweenUsers(Transaction transaction) {
    }

    @Override
    public Long currentAmount(String id) {
        return getUser(id).getMoney();
    }

    private UserAccount getUser(String userId) {
       return new UserAccount("Troll",100000l);
    }

    @Override
    public List<Transaction> ledgerOfClientTransfers() {
        return new LinkedList<>();
    }

    @Override
    public List<Transaction> ledgerOfClientTransfers(String id) {
        return new LinkedList<>();
    }

    @Override
    public void removeMoney(@NotNull UserAccount user, long amount) {
    }

    @Override
    public void addMoney(@NotNull UserAccount user, long amount) {
    }

}
