package cafe.modal;

import java.util.List;

import cafe.entity.Category;
import cafe.entity.Product;
import cafe.modal.ProductVariantResponse;
import cafe.modal.ImageResponse;
import lombok.Data;

@Data
public class ProductResponse {
	private Long id;
	private String name;
	private Boolean active;
	private String description;
	private Category category;
	private List<ImageResponse> images;
	private List<ProductVariantResponse> productVariant;
	
	public static ProductResponse convert(Product entity) {
		ProductResponse response = new ProductResponse();
		
		response.setId(entity.getId());
		response.setName(entity.getName());
		response.setDescription(entity.getDescription());
		response.setActive(entity.getActive());
		response.setCategory(entity.getCategory());
		response.setProductVariant(entity.getProductvariants().stream().map(ProductVariantResponse::convert).toList());
		response.setImages(entity.getImages().stream().map(ImageResponse::convert).toList());
		
		return response;
	}
}
