package cafe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityException extends RuntimeException {
public EntityException(String message) {
	super(message);
}

//hieunguyen 
public EntityException(String message, Throwable cause) {
    super(message, cause);
}
	
}
