package pt.unl.fct.csd.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BidAmountIsInvalidException extends RuntimeException {

    private static final String BID_AMOUNT_IS_INVALID_DEFAULT_MESSAGE =
            "The value of a bid must be positive.";

    public BidAmountIsInvalidException () {
        super(BID_AMOUNT_IS_INVALID_DEFAULT_MESSAGE);
    }
}
