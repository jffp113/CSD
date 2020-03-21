package pt.unl.fct.csd.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BidDoesNotExistException extends RuntimeException {

    private static final String BID_DOES_NOT_EXIST_DEFAULT_EXCEPPTION =
            "Auction has no closing bid.";

    public BidDoesNotExistException() {
        super(BID_DOES_NOT_EXIST_DEFAULT_EXCEPPTION);
    }
}
