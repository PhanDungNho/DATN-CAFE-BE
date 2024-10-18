package cafe.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import cafe.entity.Account;
import cafe.entity.OrderDetail;
import cafe.enums.OrderStatus;
import cafe.enums.OrderType;
import cafe.enums.PaymentMethod;
import cafe.enums.PaymentStatus;
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
@JsonInclude(JsonInclude.Include.NON_NULL) 
@Data
public class OrderDto {
	private Long id;
	
	private Account cashier;
	private String cashierid;
    private Date createdtime;
    private BigDecimal totalamount;
    private OrderStatus status;
    private PaymentStatus paymentstatus;
    private PaymentMethod paymentmethod;
    private OrderType ordertype;
    private Boolean active;
    private BigDecimal shippingfee;
    private String fulladdresstext; 
    private Account customer;
    private String customerid;
    private List<OrderdetailDto> orderdetails = new ArrayList<>();
}

