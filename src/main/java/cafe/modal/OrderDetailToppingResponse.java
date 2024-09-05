package cafe.modal;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;

import cafe.entity.OrderDetail;
import cafe.entity.OrderDetailTopping;
import cafe.entity.Topping;
import lombok.Data;

@Data
public class OrderDetailToppingResponse {
	private Long id;
	private OrderDetail orderDetail;
	private Topping topping;
	private Integer quantity;
	private BigDecimal momentprice;
	
	public static OrderDetailToppingResponse convert(OrderDetailTopping entity) {
		OrderDetailToppingResponse response = new OrderDetailToppingResponse();
		response.setId(entity.getId());
		response.setOrderDetail(entity.getOrderdetail());
		response.setTopping(entity.getTopping());
		response.setQuantity(entity.getQuantity());
		response.setMomentprice(entity.getMomentprice());
		return response;
	}
}
