package cafe.modal;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import cafe.entity.Account;
import cafe.entity.Order;
import cafe.entity.OrderDetail;
import cafe.entity.OrderStatus;
import cafe.entity.PaymentMethod;
import lombok.Data;

@Data
public class OrderResponse {
	private Long id;
	private Account cashier;
	private Date createtime;
	private BigDecimal totalalount;
	private OrderStatus status;
	private PaymentMethod paymentmethod;
	private BigDecimal shippingfee;
	private String fulladdresstext;
	private Account customer;
	private List<OrderDetailResponse> orderdetails;
	
	public static OrderResponse convert(Order entity) {
		OrderResponse response = new OrderResponse();
		response.setId(entity.getId());
		response.setCashier(entity.getCashier());
		response.setCreatetime(entity.getCreatedtime());
		response.setTotalalount(entity.getTotalamount());
		response.setStatus(entity.getStatus());
		response.setPaymentmethod(entity.getPaymentmethod());
		response.setShippingfee(entity.getShippingfee());
		response.setFulladdresstext(entity.getFulladdresstext());
		response.setCustomer(entity.getCustomer());
		response.setOrderdetails(entity.getOrderdetails().stream().map(OrderDetailResponse::convert).toList());
		return response;
	}
}
