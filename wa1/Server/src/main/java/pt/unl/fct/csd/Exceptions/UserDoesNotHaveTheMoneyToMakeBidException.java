package pt.unl.fct.csd.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserDoesNotHaveTheMoneyToMakeBidException extends RuntimeException {

    private static final String USER_DOES_NOT_HAVE_MONEY_DEFAULT_MESSAGE =
            "User %s does not have the required amount of money to cover the %d valued bid";

    public UserDoesNotHaveTheMoneyToMakeBidException(String userId, long bidVal) {
        super(String.format(USER_DOES_NOT_HAVE_MONEY_DEFAULT_MESSAGE,userId,bidVal));
    }
}
