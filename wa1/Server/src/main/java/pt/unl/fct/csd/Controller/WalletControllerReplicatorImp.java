package pt.unl.fct.csd.Controller;

import bftsmart.tom.ServiceProxy;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RestController;
import pt.unl.fct.csd.Model.Transaction;
import pt.unl.fct.csd.Model.UserAccount;

@PropertySource("classpath:application.properties")
@RestController
public class WalletControllerReplicatorImp extends WalletControllerImp {
    private final Logger logger =
            LoggerFactory.getLogger(WalletControllerReplicatorImp.class);

    @Autowired
    ServiceProxy serviceProxy;

    @Override
    public void createMoney(Transaction transaction){
        //TODO
        super.createMoney(transaction);
    }

    @Override
    public void transferMoneyBetweenUsers(Transaction transaction) {
        //TODO
        super.transferMoneyBetweenUsers(transaction);
    }

    @Override
    public void removeMoney(@NotNull UserAccount user, long amount) {
        //TODO
        super.removeMoney(user, amount);
    }

    @Override
    public void addMoney(@NotNull UserAccount user, long amount) {
        //TODO
        super.addMoney(user, amount);
    }
}
