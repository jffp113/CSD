package pt.unl.fct.csd.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AccountDoesNotExistException extends RuntimeException {
    public AccountDoesNotExistException() {
        super();
    }
    public AccountDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
    public AccountDoesNotExistException(String message) {
        super(message);
    }
    public AccountDoesNotExistException(Throwable cause) {
        super(cause);
    }
}