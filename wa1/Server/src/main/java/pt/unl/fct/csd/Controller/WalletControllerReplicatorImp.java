package pt.unl.fct.csd.Controller;

import bftsmart.tom.ServiceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.unl.fct.csd.Model.Transaction;
import pt.unl.fct.csd.Model.UserAccount;

import java.util.List;

@PropertySource("classpath:application.properties")
@RestController("ImpWalletReplicator")
@RequestMapping(value = WalletController.BASE_URL)
public class WalletControllerReplicatorImp implements WalletController {
    private final Logger logger =
            LoggerFactory.getLogger(WalletControllerReplicatorImp.class);

    @Autowired
    ServiceProxy serviceProxy;

    @Qualifier("ImpWallet")
    @Autowired
    WalletController walletController;

    @Override
    public void createMoney(Transaction transaction) {
        walletController.createMoney(transaction);
    }

    @Override
    public void transferMoneyBetweenUsers(Transaction transaction) {
        walletController.transferMoneyBetweenUsers(transaction);
    }

    @Override
    public Long currentAmount(String id) {
        return walletController.currentAmount(id);
    }

    @Override
    public List<Transaction> ledgerOfClientTransfers() {
        return walletController.ledgerOfClientTransfers();
    }

    @Override
    public List<Transaction> ledgerOfClientTransfers(String id) {
        return walletController.ledgerOfClientTransfers(id);
    }

    @Override
    public void removeMoney(UserAccount user, long amount) {
        walletController.removeMoney(user,amount);
    }

    @Override
    public void addMoney(UserAccount user, long amount) {
        walletController.addMoney(user,amount);
    }
}
