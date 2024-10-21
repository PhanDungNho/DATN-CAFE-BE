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
public class AuthorityDto implements Serializable{
	
 	private Long id;
 	private String username;
 	private Long roleId;

}
