package pt.unl.fct.csd.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BidDoesNotExistException extends RuntimeException {

    public BidDoesNotExistException() {
        super();
    }
}
