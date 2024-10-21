package cafe.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cafe.dto.OrderDto;
import cafe.entity.Account;

import cafe.entity.Category;
import cafe.entity.Order;
import cafe.entity.OrderDetail;
import cafe.entity.OrderDetailTopping;
 
import cafe.enums.OrderStatus;
 
import cafe.enums.OrderStatus;
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
	 
//		order.setCreatedtime(dto.getCreatedtime());
		order.setTotalAmount(dto.getTotalAmount());
		order.setOrderStatus(dto.getOrderStatus());
		order.setPaymentStatus(dto.getPaymentStatus());
		order.setPaymentMethod(dto.getPaymentMethod());
		order.setOrderType(dto.getOrderType());
		order.setOrdering(dto.getOrdering());
		order.setActive(dto.getActive());
		order.setShippingFee(dto.getShippingFee());
		order.setFullAddress(dto.getFullAddress());
	 
		Account cashier = accountRepository.findById(dto.getCashierId())
				.orElseThrow(() -> new EntityException("Cashier not found"));
		order.setCashier(cashier);

	 
		Account customer = accountRepository.findById(dto.getCustomerId())
				.orElseThrow(() -> new EntityException("Customer not found"));
		order.setCustomer(customer);

	 
		Order savedOrder = orderRepository.save(order);

		// Xử lý OrderDetail từ DTO và liên kết với Order vừa lưu
		List<OrderDetail> orderDetails = dto.getOrderDetails().stream().map(detailDto -> {
			OrderDetail detail = new OrderDetail();

			// Tìm và gán ProductVariant
			ProductVariant productVariant = productVariantRepository.findById(detailDto.getProductVariant().getId())
					.orElseThrow(() -> new EntityException("ProductVariant not found"));
			detail.setProductVariant(productVariant);

			detail.setQuantity(detailDto.getQuantity());
			detail.setMomentPrice(detailDto.getMomentPrice());
			detail.setNote(detailDto.getNote());

			// Gán Order cho OrderDetail
			detail.setOrder(savedOrder);

			OrderDetail saveOrderDetail = orderDetailRepository.save(detail);
			// Xử lý các Topping nếu có
			List<OrderDetailTopping> orderDetailToppings = detailDto.getOrderDetailToppings().stream()
					.map(toppingDto -> {
						OrderDetailTopping orderDetailTopping = new OrderDetailTopping();
						// Tìm và gán Topping
						Topping topping = toppingRepository.findById(toppingDto.getTopping().getId())
								.orElseThrow(() -> new EntityException("Topping not found"));
						orderDetailTopping.setTopping(topping);

						orderDetailTopping.setQuantity(toppingDto.getQuantity());
						orderDetailTopping.setMomentPrice(toppingDto.getMomentPrice());

						orderDetailTopping.setOrderDetail(saveOrderDetail);
						
						return orderDetailTopping;
					}).collect(Collectors.toList());

			// Lưu danh sách OrderDetailTopping vào OrderDetail
			detail.setOrderDetailToppings(orderDetailToppings);
			orderDetailToppingRepository.saveAll(orderDetailToppings);

			return detail;
		}).collect(Collectors.toList());

		// Lưu các OrderDetail
		orderDetailRepository.saveAll(orderDetails);
		

		// Cập nhật danh sách OrderDetail vào Order và lưu lại
		savedOrder.setOrderDetails(orderDetails);
		return orderRepository.save(savedOrder);
	}
	

	public Order updateStatus (Long id, Order order) {
		Optional<Order> existed = orderRepository.findById(id);
		if (existed.isEmpty()) {
			throw new EntityException("Order id " + id + " does not exist");
		}
		try {
			Order existedOrder = existed.get();
			existedOrder.setOrderStatus(order.getOrderStatus());
		 
			return orderRepository.save(existedOrder);
		} catch (Exception ex) {
			throw new EntityException("Order is updated failed");
		}
	}
 
	public Order save (Order order) {
		return orderRepository.save(order);
	}
 
	
	public List<Order> getOrdersBetweenDates(Date startDate, Date endDate){
		return orderRepository.findByCreatedTimeBetween(startDate, endDate);
	}
	
	public Order toggleActive(Long id) {
		Optional<Order> optionalOrder = orderRepository.findById(id);
		if(optionalOrder.isEmpty()) {
			throw new EntityException("Order with id " + id + " do not exits");
		}
		Order order = optionalOrder.get();
		order.setActive(!order.getActive());
		return orderRepository.save(order);
	}

 
//	public Page<OrderDto> getOrdersByStatus(OrderStatus status, Pageable pageable) {
//		var list = orderRepository.findByStatusContainsIgnoreCase(status, pageable);
//		
//		var newList = list.getContent().stream()
//				.map(item -> {
//					OrderDto dto = new OrderDto();
//					BeanUtils.copyProperties(item, dto, "orderdetails");
//					
//					dto.setCashier(item.getCashier());
//					dto.setCustomer(item.getCustomer());
//					dto.setPaymentmethod(item.getPaymentmethod());
//					dto.setStatus(item.getStatus());
//					
//					return dto;
//				}).collect(Collectors.toList());
//		
//		var newPage = new PageImpl<>(newList, list.getPageable(), list.getTotalElements());
//		
//		return newPage;
//	}
	
 

//	public Page<OrderDto> getOrdersByStatus(OrderStatus status, Pageable pageable) {
//		var list = orderRepository.findByStatusContainsIgnoreCase(status, pageable);
//		
//		var newList = list.getContent().stream()
//				.map(item -> {
//					OrderDto dto = new OrderDto();
//					BeanUtils.copyProperties(item, dto, "orderdetails");
//					
//					dto.setCashier(item.getCashier());
//					dto.setCustomer(item.getCustomer());
//					dto.setPaymentmethod(item.getPaymentmethod());
//					dto.setStatus(item.getStatus());
//					
//					return dto;
//				}).collect(Collectors.toList());
//		
//		var newPage = new PageImpl<>(newList, list.getPageable(), list.getTotalElements());
//		
//		return newPage;
//	}


}
