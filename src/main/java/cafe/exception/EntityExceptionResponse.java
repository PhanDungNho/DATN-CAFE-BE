package cafe.exception;

import lombok.Data;

@Data
public class EntityExceptionResponse {
private int status; 
private String message;

public EntityExceptionResponse(String message) {
	this.message = message;
}

public EntityExceptionResponse(String message, int status) {
    this.status = status;
    this.message = message;
}
}
