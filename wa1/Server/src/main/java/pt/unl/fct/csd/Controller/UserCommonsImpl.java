package pt.unl.fct.csd.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.unl.fct.csd.Exceptions.UserDoesNotExistException;
import pt.unl.fct.csd.Model.UserAccount;
import pt.unl.fct.csd.Repository.UserAccountRepository;

import java.util.Optional;

@Component
public class UserCommonsImpl implements UserCommons{

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public boolean existsUserAccount(String userId) {
        try {
            getUserAccount(userId);
            return true;
        } catch (UserDoesNotExistException e) {
            return false;
        }
    }

    @Override
    public UserAccount getUserAccount(String userId) throws UserDoesNotExistException{
        Optional<UserAccount> user = userAccountRepository.findById(userId);
        return user.orElseThrow(() -> new UserDoesNotExistException(userId));
    }

    @Override
    public void saveUserInDB(UserAccount user) {
        userAccountRepository.save(user);
    }
}
