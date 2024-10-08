package cafe.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cafe.enums.OrderStatus;
import cafe.enums.OrderType;
import cafe.enums.PaymentMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
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
    @JoinColumn(name = "cashier", nullable = true)
    private Account cashier;

	@Column(name = "createdtime", nullable = true)
	private Date createdtime;
	
	@Column(name = "totalamount", nullable = true)
	private BigDecimal totalamount;
	
	@Column(name = "status", nullable = true)
	private OrderStatus status;
	
	@Column(name = "ordertype", nullable = true)
	private OrderType ordertype;
	
	
	@Column(name = "paymentmethod", nullable = false)
	private PaymentMethod paymentmethod; 
	
	@Column(name = "active", nullable = false)
	private Boolean active;
	
	@Column(name = "shippingfee", nullable = true)
	private BigDecimal shippingfee; 
	
 
    @Column(name = "fulladdresstext", columnDefinition = "nvarchar(max)")
	private String fulladdresstext;
	
    @ManyToOne
    @JoinColumn(name = "customer", nullable = true)
    private Account customer;
	
    @JsonIgnore
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<OrderDetail> orderdetails; 

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDetail orderDetail = (OrderDetail) o;
        return Objects.equals(id, orderDetail.getId());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id); // Chỉ sử dụng id hoặc các thuộc tính cơ bản khác
    }
	@PrePersist
	public void prePdersist() {
		createdtime = new Date();
		active=true;
	}
}
