package pt.unl.fct.csd.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AuctionIsNotClosedException extends RuntimeException {

    private static final String AUCTION_IS_NOT_CLOSED_DEFAULT_MESSAGE =
            "Auction %d is not yet closed.";

    public AuctionIsNotClosedException(long auctionId) {
        super(String.format(AUCTION_IS_NOT_CLOSED_DEFAULT_MESSAGE, auctionId));
    }
}
