package cafe.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "topping")
public class Topping {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(nullable = false, length = 255)
	private String name;

	@Column(nullable = false)
	private BigDecimal price;

	@Column(name = "active")
	private Boolean active;

	@Column(name = "image", nullable = true, length = 255)
	private String image;

	@JsonIgnore
	@OneToMany(mappedBy = "topping", fetch = FetchType.LAZY)
	private List<OrderDetailTopping> orderdetailtopping;
}
