package cafe.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cafe.entity.OrderDetail;
import cafe.entity.Topping;
import lombok.Data;

@Data
public class CartDetailToppingDto {
	private Long id;
	private BigDecimal momentPrice;
	private Integer quantity;
	private ToppingDto topping;
	
	private OrderDetail orderDetail;
}
