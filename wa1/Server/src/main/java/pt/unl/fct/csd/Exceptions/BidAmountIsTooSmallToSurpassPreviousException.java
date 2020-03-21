package pt.unl.fct.csd.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BidAmountIsTooSmallToSurpassPreviousException extends RuntimeException {

    private static final String BID_AMOUNT_IS_TOO_SMALL_DEFUALT_MESSAGE =
            "A bid with value equal or above %d must be placed in order to be accepted.";

    public BidAmountIsTooSmallToSurpassPreviousException(long currentBidValue) {
        super(String.format(BID_AMOUNT_IS_TOO_SMALL_DEFUALT_MESSAGE, currentBidValue));
    }
}
