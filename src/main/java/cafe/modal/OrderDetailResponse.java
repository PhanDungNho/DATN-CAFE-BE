package cafe.modal;

import java.math.BigDecimal;
import java.util.List;

import cafe.entity.Order;
import cafe.entity.OrderDetail;
import cafe.entity.ProductVariant;
import lombok.Data;

@Data
public class OrderDetailResponse {
	private Long id;
//	private Order order;
	private ProductVariant productVariant;
	private Integer quantity;
	private BigDecimal momentPrice;
	private String note;
	private List<OrderDetailToppingResponse> orderDetailToppings;
	
	public static OrderDetailResponse convert(OrderDetail entity) {
		OrderDetailResponse response = new OrderDetailResponse();
		response.setId(entity.getId());
//		response.setOrder(entity.getOrder());
		response.setProductVariant(entity.getProductVariant());
		response.setQuantity(entity.getQuantity());
		response.setMomentPrice(entity.getMomentPrice());
		response.setNote(entity.getNote());
		response.setOrderDetailToppings(entity.getOrderDetailToppings().stream().map(OrderDetailToppingResponse::convert).toList());
		return response;
	}
}
