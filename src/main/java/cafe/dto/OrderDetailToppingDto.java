package cafe.dto;

import java.math.BigDecimal;

import cafe.entity.OrderDetail;
import cafe.entity.Topping;
import lombok.Data;

@Data
public class OrderDetailToppingDto {
	private BigDecimal momentprice;
	private Integer quantity;
	private Topping topping;
	private OrderDetail orderDetail;
}
