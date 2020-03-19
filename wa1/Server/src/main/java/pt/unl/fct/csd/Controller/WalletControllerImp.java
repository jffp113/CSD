package pt.unl.fct.csd.Controller;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import pt.unl.fct.csd.Exceptions.ResourceDoesNotExistException;
import pt.unl.fct.csd.Exceptions.InvalidOperationException;
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
            throw new InvalidOperationException();
        }

        UserAccount account = userAccountRepository.findById(transaction.getTo())
                .orElse(new UserAccount(transaction.getTo(), 0L));

        account.addMoney(transaction.getAmount());

        userAccountRepository.save(account);
        transaction.setFrom(SYSTEM_RESERVED_USER);
        transactionRepository.save(transaction);
    }

    @Override
    public void transferMoneyBetweenUsers(Transaction transaction) {
        if (!isTransferValid(transaction)) {
            throw new InvalidOperationException();
        }

        UserAccount userFrom = getUserOrException(transaction.getFrom());
        UserAccount userTo = getUserOrException(transaction.getTo());

        if (userFrom.getMoney() < transaction.getAmount()) {
            throw new InvalidOperationException();
        }

        userFrom.addMoney(transaction.getAmount() * -1);
        userTo.addMoney(transaction.getAmount());

        userAccountRepository.save(userFrom);
        userAccountRepository.save(userTo);
        transactionRepository.save(transaction);
    }

    private boolean isTransferValid(Transaction transaction) {
        return isTransferAmountValid(transaction.getAmount()) && !doesTransferInvolveSystem(transaction);
    }

    private boolean isTransferAmountValid(Long amount) {
        return amount != null && amount > 0;
    }

    private boolean doesTransferInvolveSystem(Transaction transaction) {
        return transaction.getTo().equals(SYSTEM_RESERVED_USER) ||
                transaction.getFrom().equals(SYSTEM_RESERVED_USER);
    }

    @Override
    public Long currentAmount(String id) {
        return getUserOrException(id).getMoney();
    }

    private UserAccount getUserOrException(String id) {
        Optional<UserAccount> user = userAccountRepository.findById(id);

        if (!user.isPresent()) {
            throw new ResourceDoesNotExistException();
        }

        return user.get();
    }

    @Override
    public List<Transaction> ledgerOfClientTransfers() {
        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> ledgerOfClientTransfers(String id) {
        return transactionRepository.getByFromOrTo(id, id);
    }

    @Override
    public void removeMoney(@NotNull UserAccount user, long amount) {
        transferMoney(user.getId(), SYSTEM_RESERVED_USER, amount);
        user.addMoney(-1 * amount);
        userAccountRepository.save(user);
    }

    @Override
    public void addMoney(@NotNull UserAccount user, long amount) {
        transferMoney(SYSTEM_RESERVED_USER, user.getId(), amount);
        user.addMoney(amount);
        userAccountRepository.save(user);
    }

    private void transferMoney(String from, String to, Long amount) {
        Transaction t = new Transaction();
        t.setFrom(from);
        t.setTo(to);
        t.setAmount(amount);
        transactionRepository.save(t);
    }
}
