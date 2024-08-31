package cafe.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class MapValidationErrorService {
	public ResponseEntity<?> mapValidationField(BindingResult result) {
		// nếu có lỗi xảy ra
		if (result.hasErrors()) {
			// tạo ra mapping
			Map<String, String> errorMap = new HashMap<>();
			// thực hiện ánh xạ với các thông tin tương ứng
			for (FieldError error : result.getFieldErrors()) {
				//tên trường và thông báo lỗi
				errorMap.put(error.getField(), error.getDefaultMessage());
				return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);

			}
		}
		return null;
	}

}
