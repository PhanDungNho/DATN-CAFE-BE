package cafe.dto;

import java.math.BigDecimal;
import java.util.List;

import cafe.entity.OrderDetailTopping;
import lombok.Data;

@Data
public class ToppingDto {
	private Long id;
	private String name;
	private BigDecimal price;
	private Boolean active;
	private String image;
	private List<OrderDetailTopping> orderdetailtopping;
}
