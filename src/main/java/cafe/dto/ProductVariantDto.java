package cafe.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

 

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import cafe.entity.Product;
import cafe.entity.Size;

@Data
public class ProductVariantDto {
    private Long id;

    @NotNull(message = "Active status is required")
    private Boolean active;

    @JsonIgnore // Bỏ qua thuộc tính product khi tuần tự hóa
    private ProductDto product;

    private SizeDto size;
    
    private Long productid;
    private Long sizeid;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;
}
