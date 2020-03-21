package pt.unl.fct.csd.Controller;

import pt.unl.fct.csd.Exceptions.UserDoesNotExistException;
import pt.unl.fct.csd.Model.UserAccount;

public interface UserCommons {

    boolean existsUserAccount(String userId);

    UserAccount getUserAccount(String userId) throws UserDoesNotExistException;

    void saveUserInDB(UserAccount user);

}
