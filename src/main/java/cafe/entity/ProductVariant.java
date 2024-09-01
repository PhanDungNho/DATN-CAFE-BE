package cafe.entity;

import java.math.BigDecimal;
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
import lombok.Data;

@SuppressWarnings("serial")
@Entity
@Data
@Table(name = "productvariant")
public class ProductVariant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	

	@Column(name = "active")
	private Boolean active;

	@ManyToOne
	@JoinColumn(name = "productid")
	private Product product;
	
	 @Column(nullable = false)
	    private BigDecimal price;
	
	 @ManyToOne
	    @JoinColumn(name = "sizeid")
	    private Size size;
	




	// Getters and Setters
}