package cafe.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Orders {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@Column(name = "createdtime", nullable = false)
	private Date createdtime;
	
	@Column(name = "totalamount", nullable = false)
	private Double totalamount;
	
	@Column(name = "paymentmethod", nullable = false)
	private String paymentmethod;
	
	@Column(name = "fulladdresstext", nullable = false, length = 255)
	private String fulladdresstext;
	
	@Column(name = "shippingfee", nullable = false)
	private Double shippingfee;
	
	@Column(name = "status", nullable = false)
	private Boolean status;
	
	@Column(name = "active", nullable = false)
	private Boolean active;
	
	@ManyToOne
	@JoinColumn(name = "cashier", nullable = false)
	private Accounts cashier;
	
	@ManyToOne
	@JoinColumn(name = "customer", nullable = false)
	private Accounts customer;
}
