package cafe.entity;

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
@Table(name = "CartDetails")
public class CartDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "account", nullable = true)
	private Account account;

	@ManyToOne
	@JoinColumn(name = "productvariantid", nullable = true)
	private ProductVariant productvariant;

	@Column(name = "quantity")
	private Integer quantity;
	

    @Column(columnDefinition = "TEXT")
    private String note;
	
	   @JsonIgnore
	    @OneToMany(mappedBy = "cartDetail", fetch = FetchType.EAGER)
	    private List<CartDetailTopping> cartDetailToppings;
	    

}
