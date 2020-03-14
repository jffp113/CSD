package pt.unl.fct.csd.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.unl.fct.csd.Model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
