package cafe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityException extends RuntimeException {
	
private int status;
	
public EntityException(String message) {
	super(message);
}

//hieunguyen 
public EntityException(String message, Throwable cause) {
    super(message, cause);
}

// Constructor với message và status
public EntityException(String message, int status) {
    super(message);
    this.status = status;
}

//Getter cho status nếu cần
public int getStatus() {
    return this.status;
}
}
