package cafe.modal;

import java.util.List;

import cafe.dto.ImageDto;
import cafe.entity.Account;
import cafe.entity.CartDetail;
import cafe.entity.ProductVariant;
import lombok.Data;

@Data
public class CartDetailResponse {
	private Long id;
	private Account account;
	private ProductVariant productVariant;
	private Integer quantity;
	private String note;
	private List<CartDetailToppingResponse> cartDetailToppings;
	 private List<ImageDto> images;
	
	public static CartDetailResponse convert(CartDetail entity) {
		CartDetailResponse response = new CartDetailResponse();
		
		response.setId(entity.getId());
		response.setAccount(entity.getAccount());
		response.setProductVariant(entity.getProductVariant());
		response.setQuantity(entity.getQuantity());
		response.setNote(entity.getNote());
		response.setCartDetailToppings(entity.getCartDetailToppings().stream().map(CartDetailToppingResponse::convert).toList());
		List<ImageDto> images = entity.getProductVariant().getProduct().getImages().stream()
                .map(ImageDto::convert)
                .toList();
        response.setImages(images);
        
		return response;
	}
}
