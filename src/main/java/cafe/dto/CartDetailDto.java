package cafe.dto;

import cafe.entity.Account;
import cafe.entity.ProductVariant;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class CartDetailDto {

	private Long id;
	private String username;
	private Long productVariantId;
	private Integer quantity;
	private Account account;
	private ProductVariant productVariant;

}
