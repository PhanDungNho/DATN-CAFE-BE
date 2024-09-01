package cafe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ImageDto {

    private Long id;

    @NotBlank(message = "Image name is required")
    @Size(max = 255, message = "Image name must be less than or equal to 255 characters")
    private String name;

    @NotNull(message = "Default status is required")
    private Boolean isDefault;

    @NotNull(message = "Product is required")
    private ProductDto product;
}
