package cafe.modal;

import java.math.BigDecimal;

import cafe.entity.Product;
import cafe.entity.ProductVariant;
import cafe.entity.Size;
import lombok.Data;

@Data
public class ProductVariantResponse {
	private Long id;
	private Boolean active;
	private BigDecimal price;
	private Size size;
	private Product product;

	public static ProductVariantResponse convert(ProductVariant entity) {
		ProductVariantResponse response = new ProductVariantResponse();

		response.setId(entity.getId());
		response.setActive(entity.getActive());
		response.setPrice(entity.getPrice());
		response.setSize(entity.getSize());
		response.setProduct(entity.getProduct());

		return response;
	}
}
