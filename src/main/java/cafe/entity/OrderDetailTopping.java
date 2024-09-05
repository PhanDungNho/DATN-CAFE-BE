package cafe.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orderdetailtopping")
public class OrderDetailTopping {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	
	@ManyToOne
	@JoinColumn(name = "orderdetailid")
	private OrderDetail orderdetail;

	@ManyToOne
	@JoinColumn(name = "toppingid")
	private Topping topping;

	@Column(nullable = false)
	private Integer quantity;

	@Column(nullable = false)
	private BigDecimal momentprice;
}
