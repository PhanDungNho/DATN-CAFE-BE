package cafe.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import cafe.dto.OrderDto;
import cafe.dto.OrderdetailDto;
import cafe.dto.ToppingDto;
import cafe.entity.Account;
import cafe.entity.Category;
import cafe.entity.Order;
import cafe.entity.OrderDetail;
import cafe.entity.OrderDetailTopping;
import cafe.entity.OrderStatus;
import cafe.entity.ProductVariant;
import cafe.entity.Topping;
import cafe.exception.EntityException;
import cafe.repository.AccountRepository;
import cafe.repository.OrderDetailRepository;
import cafe.repository.OrderDetailToppingRepository;
import cafe.repository.OrderRepository;
import cafe.repository.ProductVariantRepository;
import cafe.repository.ToppingRepository;

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

	@Autowired
	private ToppingRepository toppingRepository;
	
	@Autowired
	private OrderDetailToppingRepository orderDetailToppingRepository;

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
		order.setOrdertype(dto.getOrdertype());
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
		List<OrderDetail> orderDetails = dto.getOrderdetails().stream().map(detailDto -> {
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

			OrderDetail saveOrderDetail = orderDetailRepository.save(detail);
			// Xử lý các Topping nếu có
			List<OrderDetailTopping> orderDetailToppings = detailDto.getOrderdetailtoppings().stream()
					.map(toppingDto -> {
						OrderDetailTopping orderDetailTopping = new OrderDetailTopping();
						// Tìm và gán Topping
						Topping topping = toppingRepository.findById(toppingDto.getTopping().getId())
								.orElseThrow(() -> new EntityException("Topping not found"));
						orderDetailTopping.setTopping(topping);

						orderDetailTopping.setQuantity(toppingDto.getQuantity());
						orderDetailTopping.setMomentprice(toppingDto.getMomentprice());

						orderDetailTopping.setOrderdetail(saveOrderDetail);
						
						return orderDetailTopping;
					}).collect(Collectors.toList());

			// Lưu danh sách OrderDetailTopping vào OrderDetail
			detail.setOrderdetailtoppings(orderDetailToppings);
			orderDetailToppingRepository.saveAll(orderDetailToppings);

			return detail;
		}).collect(Collectors.toList());

		// Lưu các OrderDetail
		orderDetailRepository.saveAll(orderDetails);
		

		// Cập nhật danh sách OrderDetail vào Order và lưu lại
		savedOrder.setOrderdetails(orderDetails);
		return orderRepository.save(savedOrder);
	}
	

	public Order updateStatus (Long id, Order order) {
		Optional<Order> existed = orderRepository.findById(id);
		if (existed.isEmpty()) {
			throw new EntityException("Order id " + id + " does not exist");
		}
		try {
			Order existedOrder = existed.get();
			existedOrder.setStatus(order.getStatus());
		 
			return orderRepository.save(existedOrder);
		} catch (Exception ex) {
			throw new EntityException("Order is updated failed");
		}
	}
	
 

	public Page<OrderDto> getOrdersByStatus(OrderStatus status, Pageable pageable) {
		var list = orderRepository.findByStatusContainsIgnoreCase(status, pageable);
		
		var newList = list.getContent().stream()
				.map(item -> {
					OrderDto dto = new OrderDto();
					BeanUtils.copyProperties(item, dto, "orderdetails");
					
					dto.setCashier(item.getCashier());
					dto.setCustomer(item.getCustomer());
					dto.setPaymentmethod(item.getPaymentmethod());
					dto.setStatus(item.getStatus());
					
					return dto;
				}).collect(Collectors.toList());
		
		var newPage = new PageImpl<>(newList, list.getPageable(), list.getTotalElements());
		
		return newPage;
	}


}
