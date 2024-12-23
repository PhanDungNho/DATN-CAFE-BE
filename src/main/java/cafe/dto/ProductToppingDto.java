package cafe.dto;

import java.util.List;
import java.util.Objects;

import lombok.Data;

@Data
public class ProductToppingDto {
	private Long id;
	private ToppingDto topping;
	private ProductDto product;
	private Long productId;
	private Long toppingId;
	
	@Override
    public int hashCode() {
        return Objects.hash(id); 
    }
}
