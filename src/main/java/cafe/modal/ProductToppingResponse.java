package cafe.modal;

import cafe.entity.Product;
import cafe.entity.ProductToppings;
import cafe.entity.Topping;
import lombok.Data;

@Data
public class ProductToppingResponse {
	private Long id;
	private Long productId;
	private Long toppingId;
	private Product product;
	private Topping topping;
	
	public static ProductToppingResponse convert(ProductToppings entity) {
		ProductToppingResponse response = new ProductToppingResponse();
		
		response.setId(entity.getId());
		response.setProduct(entity.getProduct());
		response.setTopping(entity.getTopping());
		response.setProductId(entity.getProduct().getId());
		response.setToppingId(entity.getTopping().getId());
		
		return response;
	}
}
