package cafe.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import cafe.entity.Account;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Bao gồm các trường không null
//DTO là lớp trung gian, thường để bảo mật, này nọ
public class AccountRoleDto implements Serializable{
	
 	private Long id;
 	@NotNull(message = "Account is required")
 	private String account;
 	@NotNull(message = "Role is required")
 	private Long roleid;
 	
    private AccountDto accountDto;
    private RoleDto roleDto;
}
