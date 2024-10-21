package cafe.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cafe.entity.OrderDetailTopping;
import lombok.Data;

@Data
public class ToppingDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private BigDecimal price;
	private Boolean active;
	private String image;
	@JsonIgnore
	private MultipartFile imageFile;
	@JsonIgnore
	private List<OrderDetailTopping> orderDetailTopping;
}
