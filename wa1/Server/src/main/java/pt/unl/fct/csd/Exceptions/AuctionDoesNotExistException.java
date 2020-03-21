package pt.unl.fct.csd.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AuctionDoesNotExistException extends RuntimeException {

    private static final String AUCTION_DOES_NOT_EXIST_DEFAULT_MESSAGE =
            "The auction with id %d does not exist.";

    public AuctionDoesNotExistException(long auctionId) {
        super(String.format(AUCTION_DOES_NOT_EXIST_DEFAULT_MESSAGE, auctionId));
    }
}
