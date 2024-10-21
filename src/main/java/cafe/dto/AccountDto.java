package cafe.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Size(max = 255, message = "Username must be less than or equal to 255 characters")
	private String username;

	@NotNull(message = "Account active status is required")
	private Boolean active;

	@NotNull(message = "Amountpaid is required")
	private Double amountPaid;

	@NotBlank(message = "Email is required")
	@Size(max = 255, message = "Email must be less than or equal to 255 characters")
	private String email;

	@Size(max = 255, message = "Fullname must be less than or equal to 255 characters")
	private String fullName;

	private String password;

	@NotBlank(message = "Phone is required")
	@Size(max = 15, message = "Phone must be less than or equal to 255 characters")
	private String phone;

	private String image;

	@JsonIgnore
	private MultipartFile imageFile;
	
	  // Trường OTP để lưu trữ mã OTP gửi qua email
    @Size(max = 6, message = "OTP must be a 6-character code")
    private String otp;
    

    
    private Boolean enabled;

//    private List<AccountRoleDto> accountRoleDtos;

}
