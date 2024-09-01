package cafe.dto;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SizeDto {

    private Long id;

    @NotBlank(message = "Size name is required")
    @Size(max = 255, message = "Size name must be less than or equal to 255 characters")
    private String name;

    @NotNull(message = "Active status is required")
    private Boolean active;

//    private List<ProductVariantDto> productVariants;
}
