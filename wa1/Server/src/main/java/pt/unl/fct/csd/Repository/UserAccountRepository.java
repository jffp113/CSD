package pt.unl.fct.csd.Repository;


import org.springframework.data.repository.CrudRepository;
import pt.unl.fct.csd.Model.UserAccount;


public interface UserAccountRepository extends CrudRepository<UserAccount,String> {

}
