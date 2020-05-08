package pt.unl.fct.csd.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.unl.fct.csd.Model.SystemReply;
import pt.unl.fct.csd.Model.Transaction;
import pt.unl.fct.csd.Model.UserAccount;
import pt.unl.fct.csd.Replication.*;


@PropertySource("classpath:application.properties")
@RestController("ImpWalletReplicator")
@RequestMapping(value = WalletController.BASE_URL)
public class WalletControllerReplicatorImp implements CollectiveWalletController {
    private final Logger logger =
            LoggerFactory.getLogger(WalletControllerReplicatorImp.class);

    @Autowired
    ClientAsyncReplicator clientAsyncReplicator;

    @Override
    public SystemReply createMoney(Transaction transaction) {
        logger.info("Proxy received request createMoney");
        return clientAsyncReplicator.invokeOrderedReplication(transaction, Path.CREATE_MONEY);
    }

    @Override
    public SystemReply transferMoneyBetweenUsers(Transaction transaction) {
        logger.info("Proxy received request transferMoneyBetweenUsers");
        return clientAsyncReplicator.invokeOrderedReplication(transaction, Path.TRANSFER_MONEY);
    }

    @Override
    public SystemReply removeMoney(UserAccount user, long amount) {
        logger.info("Proxy received request removeMoney");
        return clientAsyncReplicator.invokeOrderedReplication(new DualArgReplication<>(user, amount), Path.REMOVE_MONEY);
    }

    @Override
    public SystemReply addMoney(UserAccount user, long amount) {
        logger.info("Proxy received request addMoney");
        return clientAsyncReplicator.invokeOrderedReplication(new DualArgReplication<>(user, amount), Path.ADD_MONEY);
    }

    @Override
    public SystemReply currentAmount(String userId) {
        logger.info("Proxy received request currentAmount");
        return clientAsyncReplicator.invokeUnorderedReplication(userId, Path.GET_MONEY);
    }

    // we should change the name of this method
    @Override
    public SystemReply ledgerOfClientTransfers() {
        logger.info("Proxy received request ledgerOfClientTransfers");
        return clientAsyncReplicator.invokeUnorderedReplication(Path.GET_LEDGER);
    }

    @Override
    public SystemReply ledgerOfClientTransfers(String userId) {
        logger.info("Proxy received request ledgerOfClientTransfers");
        return clientAsyncReplicator.invokeUnorderedReplication(userId,Path.GET_LEDGER);
    }
}
