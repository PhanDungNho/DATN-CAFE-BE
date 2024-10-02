package cafe.dto;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoleDto implements Serializable{
	
    private Long id;
    
    @NotBlank(message = "Role name is required")
    @Size(max = 255, message = "Role name must be less than or equal to 255 characters")
    private String rolename;
    
    @NotNull(message = "Active status is required")
    private Boolean active;
    
    private List<AccountDto> accountDtos;

}
