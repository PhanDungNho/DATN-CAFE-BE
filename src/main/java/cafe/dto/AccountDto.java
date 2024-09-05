package cafe.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Bao gồm các trường không null
//DTO là lớp trung gian, thường để bảo mật, này nọ
public class AccountDto {
	
	@Size(max = 255, message = "Username must be less than or equal to 255 characters")
	private String username;
	
    @NotNull(message = "Account active status is required")
	private Boolean active;
	
    @NotNull(message = "Amountpaid is required")
	private Double amountpaid;
	
	@NotBlank(message = "Email is required")
    @Size(max = 255, message = "Email must be less than or equal to 255 characters")
	private String email;
	
    @Size(max = 255, message = "Fullname must be less than or equal to 255 characters")
	private String fullname;

    @NotBlank(message = "Password is required")
    @Size(max = 255, message = "Password must be less than or equal to 255 characters")
	private String password;
    
    @NotBlank(message = "Phone is required")
    @Size(max = 15, message = "Phone must be less than or equal to 255 characters")
    private String phone;
}
