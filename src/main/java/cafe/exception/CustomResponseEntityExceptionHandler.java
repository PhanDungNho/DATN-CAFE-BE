package cafe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
//@ExceptionHandler(EntityException.class)
//	public final ResponseEntity<Object> handleCaResponseEntity(EntityException ex, WebRequest request){
//		EntityExceptionResponse exceptionResponse = new EntityExceptionResponse(ex.getMessage());
//		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
//}


@ExceptionHandler(EntityException.class)
public final ResponseEntity<Object> handleEntityExceptionStatus(EntityException ex, WebRequest request) {
    // Truyền mã trạng thái HTTP (400) khi xử lý lỗi
    EntityExceptionResponse exceptionResponse = new EntityExceptionResponse(ex.getMessage(),ex.getStatus());
    return new ResponseEntity<>(exceptionResponse, HttpStatus.valueOf(ex.getStatus()));
}

}
