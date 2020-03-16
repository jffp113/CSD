package pt.unl.fct.csd.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.unl.fct.csd.Model.Bid;
import pt.unl.fct.csd.Model.Transaction;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, String> {
}
