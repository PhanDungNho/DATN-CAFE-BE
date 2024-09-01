package cafe.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
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
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "cashier", nullable = false)
	private Account cashier;
	

	@Column(name = "createdtime", nullable = false)
	private Date createdtime;
	
	@Column(name = "totalamount", nullable = false)
	private BigDecimal totalamount;
	
	@Column(name = "status", nullable = false)
	private OrderStatus status;
	
	@Column(name = "paymentmethod", nullable = false)
	private PaymentMethod paymentmethod; 
	
	@Column(name = "active", nullable = false)
	private Boolean active;
	
	@Column(name = "shippingfee", nullable = true)
	private BigDecimal shippingfee; 
	
 
    @Column(name = "fulladdresstext", columnDefinition = "nvarchar(max)")
	private String fulladdresstext;
	
	@ManyToOne
	@JoinColumn(name = "customer", nullable = false)
	private Account customer;
	
    @JsonIgnore
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderdetails;

	
	

}
