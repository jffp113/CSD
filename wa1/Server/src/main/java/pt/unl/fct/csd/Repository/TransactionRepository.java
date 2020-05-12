package pt.unl.fct.csd.Repository;


import org.springframework.data.repository.CrudRepository;
import pt.unl.fct.csd.Model.Transaction;

import java.util.List;


public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> getByFromOrTo(String from, String to);
    List<Transaction> getAll();
}
