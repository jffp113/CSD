package pt.unl.fct.csd.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidTransactionException extends RuntimeException {
    public InvalidTransactionException() {
        super();
    }
    public InvalidTransactionException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidTransactionException(String message) {
        super(message);
    }
    public InvalidTransactionException(Throwable cause) {
        super(cause);
    }
}