package cafe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FileStorageException extends RuntimeException {
public FileStorageException(String message) {
	super(message);
	
}

public FileStorageException() {
	super();
	// TODO Auto-generated constructor stub
}

public FileStorageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
	// TODO Auto-generated constructor stub
}

public FileStorageException(String message, Throwable cause) {
	super(message, cause);
	// TODO Auto-generated constructor stub
}

public FileStorageException(Throwable cause) {
	super(cause);
	// TODO Auto-generated constructor stub
}
	
}
