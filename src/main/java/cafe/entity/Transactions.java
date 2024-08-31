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
@Table(name = "transactions")
public class Transactions {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@Column(name = "transactiontime", nullable = false)
	private Date transactiontime;
	
	@Column(name = "amount", nullable = false)
	private int amount;
	
	@Column(name = "transactionmethod", nullable = false)
	private String transactionmethod;
	
	@Column(name = "status", nullable = false)
	private Boolean status;
	
//	@ManyToOne
//	@JoinColumn(name = "orderid", nullable = false)
//	private Orders orderid;
	
	@ManyToOne
	@JoinColumn(name = "customer", nullable = false)
	private Accounts customer;
}
