package cafe.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cafe.entity.Account;
import cafe.entity.OrderDetail;
import cafe.entity.OrderStatus;
import cafe.entity.PaymentMethod;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderDto {
	private Account cashier;
    private Date createdtime;
    private BigDecimal totalamount;
    private OrderStatus status;
    private PaymentMethod paymentmethod;
    private Boolean active;
    private BigDecimal shippingfee;
    private String fulladdresstext; 
    private Account customer;
    private List<OrderdetailDto> orderdetails = new ArrayList<>();
}

