package pt.unl.fct.csd.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceDoesNotExistException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3725399550890072063L;
	
	public ResourceDoesNotExistException() {
        super();
    }
    public ResourceDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
    public ResourceDoesNotExistException(String message) {
        super(message);
    }
    public ResourceDoesNotExistException(Throwable cause) {
        super(cause);
    }
}