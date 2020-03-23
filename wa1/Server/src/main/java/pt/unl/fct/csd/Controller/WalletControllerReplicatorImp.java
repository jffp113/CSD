package pt.unl.fct.csd.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.unl.fct.csd.Model.Transaction;
import pt.unl.fct.csd.Model.UserAccount;
import pt.unl.fct.csd.Replication.ClientReplicator;
import pt.unl.fct.csd.Replication.DualArgReplication;
import pt.unl.fct.csd.Replication.InvokerWrapper;
import pt.unl.fct.csd.Replication.Path;
import java.util.List;

@PropertySource("classpath:application.properties")
@RestController("ImpWalletReplicator")
@RequestMapping(value = WalletController.BASE_URL)
public class WalletControllerReplicatorImp implements WalletController {
    private final Logger logger =
            LoggerFactory.getLogger(WalletControllerReplicatorImp.class);

    @Autowired
    ClientReplicator clientReplicator;

    @Qualifier("ImpWallet")
    @Autowired
    WalletController walletController;

    @Override
    public void createMoney(Transaction transaction) {
        InvokerWrapper<Exception> result =
                clientReplicator.invokeReplication(transaction,Path.CREATE_AUCTION);
        result.getResultOrThrow();
    }


    @Override
    public void transferMoneyBetweenUsers(Transaction transaction) {
        clientReplicator.invokeReplication(transaction,Path.TRANSFER_MONEY);

    }

    @Override
    public void removeMoney(UserAccount user, long amount) {
        clientReplicator.invokeReplication(new DualArgReplication<UserAccount,Long>(user,amount),Path.REMOVE_MONEY);
    }

    @Override
    public void addMoney(UserAccount user, long amount) {
        clientReplicator.invokeReplication(new DualArgReplication<UserAccount,Long>(user,amount),Path.ADD_MONEY);
    }

    @Override
    public Long currentAmount(String id) {
        //TODO
        return walletController.currentAmount(id);
    }

    @Override
    public List<Transaction> ledgerOfClientTransfers() {
        //TODO
        return walletController.ledgerOfClientTransfers();
    }

    @Override
    public List<Transaction> ledgerOfClientTransfers(String id) {
        //TODO
        return walletController.ledgerOfClientTransfers(id);
    }

}
