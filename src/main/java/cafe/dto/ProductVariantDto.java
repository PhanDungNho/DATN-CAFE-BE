package cafe.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductVariantDto {

	private Long id;

	@NotNull(message = "Active status is required")
	private Boolean active;

	@NotNull(message = "Product is required")
	private ProductDto product;

	@NotNull(message = "Size is required")
	private SizeDto size;

	@NotNull(message = "Price is required")
	@DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
	private BigDecimal price;
}
