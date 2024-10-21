package cafe.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cafe.entity.ProductVariant;
import lombok.Data;

@Data
public class OrderdetailDto {
    private Long productVariantId;
    private ProductVariant productVariant;
    private Integer quantity;
    private BigDecimal momentPrice;
    private String note;
    List<OrderDetailToppingDto> orderDetailToppings = new ArrayList<>(); 
    
}
