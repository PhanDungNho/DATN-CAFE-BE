package cafe.modal;

import java.math.BigDecimal;
import java.util.List;

import cafe.entity.Topping;
import lombok.Data;

@Data
public class ToppingResponse {
	private Long id;
	private String name;
	private BigDecimal price;
	private Boolean active;
	private String image;
	private List<OrderDetailToppingResponse> orderDetailToppingResponse;
	
	public static ToppingResponse convert(Topping entity) {
		ToppingResponse response = new ToppingResponse();
		response.setId(entity.getId());
		response.setName(entity.getName());
		response.setPrice(entity.getPrice());
		response.setActive(entity.getActive());
		response.setImage(entity.getImage());
		response.setOrderDetailToppingResponse(entity.getOrderdetailtopping().stream().map(OrderDetailToppingResponse::convert).toList());
		return response;
	}
}
