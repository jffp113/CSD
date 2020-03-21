package pt.unl.fct.csd.Controller;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import pt.unl.fct.csd.Exceptions.*;
import pt.unl.fct.csd.Model.Transaction;
import pt.unl.fct.csd.Model.UserAccount;
import pt.unl.fct.csd.Repository.TransactionRepository;
import pt.unl.fct.csd.Repository.UserAccountRepository;

import java.util.List;
import java.util.Optional;


@RestController
public class WalletControllerImp implements WalletController {

    public static final String SYSTEM_RESERVED_USER = "SYSTEM";
    private final Logger logger =
            LoggerFactory.getLogger(WalletControllerImp.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public void createMoney(Transaction transaction) {

        if (transaction.getAmount() < 0 || transaction.getTo().equals(SYSTEM_RESERVED_USER)) {
            throw new TransactionAmountNotValidException();
        }

        UserAccount account = getOrCreateUser(transaction.getTo());
        account.addMoney(transaction.getAmount());
        saveUser(account);
        transaction.setFrom(SYSTEM_RESERVED_USER);
        transactionRepository.save(transaction);
    }

    private UserAccount getOrCreateUser (String userId) {
        try {
            //return new UserCommonsImpl().getUserAccount(userId);
            return getUserAccount(userId);
        } catch (UserDoesNotExistException e) {
            return new UserAccount(userId, 0L);
        }
    }

    @Override
    public void transferMoneyBetweenUsers(Transaction transaction) {
        if (!isTransferAmountValid(transaction.getAmount()))
            throw new TransactionAmountNotValidException();

        UserAccount userFrom = getUser(transaction.getFrom());
        if (userFrom.getMoney() < transaction.getAmount())
            throw new InsufficientFundsForTransactionException(userFrom.getId(),
                    userFrom.getMoney(), transaction.getAmount());

        UserAccount userTo = getUser(transaction.getTo());
        userFrom.addMoney(transaction.getAmount() * -1);
        userTo.addMoney(transaction.getAmount());

        saveUser(userFrom);
        saveUser(userTo);
        transactionRepository.save(transaction);
    }

    private boolean isTransferAmountValid(Long amount) {
        return amount != null && amount > 0;
    }

    @Override
    public Long currentAmount(String id) {
        return getUser(id).getMoney();
    }

    private UserAccount getUser(String userId) {
        //return new UserCommonsImpl().getUserAccount(userId);
        return getUserAccount(userId);
    }

    @Override
    public List<Transaction> ledgerOfClientTransfers() {
        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> ledgerOfClientTransfers(String id) {
        if (!existsUserAccount(id))
            throw new UserDoesNotExistException(id);
        return transactionRepository.getByFromOrTo(id, id);
    }

    @Override
    public void removeMoney(@NotNull UserAccount user, long amount) {
        transferMoney(user.getId(), SYSTEM_RESERVED_USER, amount);
        user.addMoney(-1 * amount);
        saveUser(user);
    }

    @Override
    public void addMoney(@NotNull UserAccount user, long amount) {
        transferMoney(SYSTEM_RESERVED_USER, user.getId(), amount);
        user.addMoney(amount);
        saveUser(user);
    }

    private void saveUser(UserAccount user) {
        //new UserCommonsImpl().saveUserInDB(user);
        saveUserInDB(user);
    }

    private void transferMoney(String from, String to, Long amount) {
        Transaction t = new Transaction();
        t.setFrom(from);
        t.setTo(to);
        t.setAmount(amount);
        transactionRepository.save(t);
    }

    /**********************************************************/

    public boolean existsUserAccount(String userId) {
        try {
            getUserAccount(userId);
            return true;
        } catch (UserDoesNotExistException e) {
            return false;
        }
    }

    public UserAccount getUserAccount(String userId) throws UserDoesNotExistException{
        Optional<UserAccount> user = userAccountRepository.findById(userId);
        return user.orElseThrow(() -> new UserDoesNotExistException(userId));
    }

    public void saveUserInDB(UserAccount user) {
        userAccountRepository.save(user);
    }
}
