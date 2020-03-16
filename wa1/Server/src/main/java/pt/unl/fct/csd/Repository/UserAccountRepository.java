package pt.unl.fct.csd.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.unl.fct.csd.Model.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount,String> {

    UserAccount getUserAccountById(String s);

}
