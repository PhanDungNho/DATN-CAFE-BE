package cafe.dto;

import java.util.List;
import java.util.Objects;

import lombok.Data;

@Data
public class ProductToppingDto {
	private Long id;
	private List<ToppingDto> toppings;
	private List<ProductDto> products;
	private Long productId;
	private Long toppingId;
	
	@Override
    public int hashCode() {
        return Objects.hash(id);  // Chỉ sử dụng id hoặc trường duy nhất, không sử dụng các quan hệ để tránh vòng lặp
    }
}
