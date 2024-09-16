package cafe.dto;

import java.math.BigDecimal;
import java.util.List;

import cafe.entity.ProductVariant;
import lombok.Data;

@Data
public class OrderdetailDto {
    private ProductVariant productvariant;
    private Integer quantity;
    private BigDecimal momentprice;
    private String note;
    List<OrderDetailToppingDto> orderdetailtoppings;
}
