package cafe.modal;

import cafe.entity.CartDetail;
import cafe.entity.CartDetailTopping;
import cafe.entity.Topping;
import lombok.Data;

@Data
public class CartDetailToppingResponse {
	private Long id;
	private Long cartDetail;
	private Topping topping;
	private Integer quantity;
	
	public static CartDetailToppingResponse convert(CartDetailTopping entity) {
		CartDetailToppingResponse response = new CartDetailToppingResponse();
		
		response.setId(entity.getId());
		response.setCartDetail(entity.getCartDetail().getId());
		response.setTopping(entity.getTopping());
		response.setQuantity(entity.getQuantity());
		
		return response;
	}
}
