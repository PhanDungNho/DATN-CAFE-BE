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
import cafe.enums.PaymentStatus;
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
@Table(name = "Orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
    @Column(name = "slug", nullable = false, length = 255)
    private String slug;
    
	@ManyToOne
    @JoinColumn(name = "cashier_id", nullable = true)
    private Account cashier;

	@Column(name = "created_time", nullable = true)
	private Date createdTime;
	
	@Column(name = "total_amount", nullable = true)
	private BigDecimal totalAmount;
	
	@Column(name = "order_status", nullable = true)
	private OrderStatus orderStatus;
	
	@Column(name = "payment_status", nullable = true)
	private PaymentStatus paymentStatus; 
	
	@Column(name = "ordering", nullable = true)
	private Integer ordering; 
 
	
	@Column(name = "order_type", nullable = true)
	private OrderType orderType;
	
	
	@Column(name = "payment_method", nullable = false)
	private PaymentMethod paymentMethod; 
	
	
	@Column(name = "active", nullable = false)
	private Boolean active;
	
	@Column(name = "shipping_fee", nullable = true)
	private BigDecimal shippingFee; 

    @Column(name = "full_address", columnDefinition = "nvarchar(max)")
	private String fullAddress;
	
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = true)
    private Account customer;
	
    @JsonIgnore
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<OrderDetail> orderDetails; 
    
    @JsonIgnore
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<Transactions> transactions;

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
		createdTime = new Date();
		active=true;
	}
}
