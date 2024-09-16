package cafe.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import cafe.dto.OrderDto;
import cafe.dto.OrderdetailDto;
import cafe.entity.Account;
import cafe.entity.AccountRole;
import cafe.entity.Order;
import cafe.entity.OrderDetail;
import cafe.entity.ProductVariant;
import cafe.entity.exception.EntityException;
import cafe.repository.AccountRepository;
import cafe.repository.OrderDetailRepository;
import cafe.repository.OrderRepository;
import cafe.repository.ProductVariantRepository;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	@Autowired
	private ProductVariantRepository productVariantRepository;

	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	public Optional<Order> findById(Long id) {
		return orderRepository.findById(id);
	}

	public Order createOrder(OrderDto dto) {
	    Order order = new Order();
	    
	    // Chuyển đổi các thuộc tính đơn giản từ DTO sang entity Order
	    order.setCreatedtime(dto.getCreatedtime());
	    order.setTotalamount(dto.getTotalamount());
	    order.setStatus(dto.getStatus());
	    order.setPaymentmethod(dto.getPaymentmethod());
	    order.setActive(dto.getActive());
	    order.setShippingfee(dto.getShippingfee());
	    order.setFulladdresstext(dto.getFulladdresstext());

	    // Tìm và gán đối tượng Cashier
	    Account cashier = accountRepository.findById(dto.getCashier().getUsername())
	        .orElseThrow(() -> new EntityException("Cashier not found"));
	    order.setCashier(cashier);

	    // Tìm và gán đối tượng Customer
	    Account customer = accountRepository.findById(dto.getCustomer().getUsername())
	        .orElseThrow(() -> new EntityException("Customer not found"));
	    order.setCustomer(customer);

	    // Lưu Order trước để có ID
	    Order savedOrder = orderRepository.save(order);

	    // Xử lý OrderDetail từ DTO và liên kết với Order vừa lưu
	    List<OrderDetail> orderDetails = dto.getOrderdetails().stream()
	        .map(detailDto -> {
	            OrderDetail detail = new OrderDetail();

	            // Tìm và gán ProductVariant
	            ProductVariant productVariant = productVariantRepository.findById(detailDto.getProductvariant().getId())
	                .orElseThrow(() -> new EntityException("ProductVariant not found"));
	            detail.setProductvariant(productVariant);

	            detail.setQuantity(detailDto.getQuantity());
	            detail.setMomentprice(detailDto.getMomentprice());
	            detail.setNote(detailDto.getNote());

	            // Gán Order cho OrderDetail
	            detail.setOrder(savedOrder);
	            
	            return detail;
	        })
	        .collect(Collectors.toList());

	    // Lưu các OrderDetail
	    orderDetailRepository.saveAll(orderDetails);

	    // Cập nhật danh sách OrderDetail vào Order và lưu lại
	    savedOrder.setOrderdetails(orderDetails);
	    return orderRepository.save(savedOrder);
	}

	private OrderDetail convertToEntity(OrderdetailDto dto) {
		OrderDetail entity = new OrderDetail();
		entity.setProductvariant(dto.getProductvariant());
		entity.setQuantity(dto.getQuantity());
		entity.setMomentprice(dto.getMomentprice());
		entity.setNote(dto.getNote());
		return entity;
	}
}
