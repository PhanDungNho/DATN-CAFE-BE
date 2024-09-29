package cafe.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Bao gồm các trường không null
//DTO là lớp trung gian, thường để bảo mật, này nọ
public class CategoryDto {
    
    private Long id;
    @NotEmpty(message = "Category name is required")
    @Size(max = 255, message = "Category name must not exceed 255 characters")
    private String name;
    

    private Boolean active;
}