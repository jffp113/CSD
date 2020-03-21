package pt.unl.fct.csd.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AuctionIsAlreadyClosedException extends RuntimeException {

    private static final String AUCTION_IS_ALREADY_CLOSED_DEFAULT_EXCEPTION =
            "A bid cannot be made to the closed auction %d";

    public AuctionIsAlreadyClosedException(long auctionId) {
        super(String.format(AUCTION_IS_ALREADY_CLOSED_DEFAULT_EXCEPTION, auctionId));
    }
}
