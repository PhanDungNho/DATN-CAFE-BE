package cafe.exception;

import lombok.Data;

@Data
public class EntityExceptionResponse {
private String message;

public EntityExceptionResponse(String message) {
	this.message = message;
}


}
