package cafe.modal;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import cafe.entity.Account;
import cafe.entity.Order;
import cafe.entity.OrderDetail;
import cafe.entity.Transactions;
import cafe.enums.OrderStatus;
import cafe.enums.OrderType;
import cafe.enums.PaymentMethod;
import lombok.Data;

@Data
public class OrderResponse {
	private Long id;
	private Account cashier;
	private Date createdTime;
	private BigDecimal totalAmount;
	private OrderStatus orderStatus;
	private PaymentMethod paymentMethod;
	private BigDecimal shippingFee;
	private String fullAddress;
	private Account customer;
	private OrderType ordertype;
	private List<OrderDetailResponse> orderdetails;
	private Boolean active;
	private List<Transactions> transactions; 
	
	public static OrderResponse convert(Order entity) {
		OrderResponse response = new OrderResponse();
		response.setId(entity.getId());
		response.setCashier(entity.getCashier());
		response.setCreatedTime(entity.getCreatedTime());
		response.setTotalAmount(entity.getTotalAmount());
		response.setOrderStatus(entity.getOrderStatus());
		response.setPaymentMethod(entity.getPaymentMethod());
		response.setShippingFee(entity.getShippingFee());
		response.setFullAddress(entity.getFullAddress());
		response.setCustomer(entity.getCustomer());
		response.setActive(entity.getActive());
		response.setOrdertype(entity.getOrderType());
		response.setTransactions(entity.getTransactions());
		response.setOrderdetails(entity.getOrderDetails().stream().map(OrderDetailResponse::convert).toList());
		return response;
	}
}
