package pt.unl.fct.csd.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import pt.unl.fct.csd.Model.Transaction;
import pt.unl.fct.csd.Repository.TransactionRepository;

@RestController
public class WalletControllerImp implements WalletController {

    @Autowired
    private TransactionRepository repository;

    @Override
    public void createMoney(Transaction transaction) {
        //TODO this is just a test to see if it was working
        repository.save(transaction);

        System.out.println(repository.getOne(transaction.getId()));
    }

    @Override
    public void transferMoney(Transaction transaction) {
            //TODO stub
    }

    @Override
    public Long currentAmount(String id) {
        //TODO stub
        return null;
    }
}
