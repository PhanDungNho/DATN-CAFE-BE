package cafe.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cafe.dto.OrderDetailToppingDto;
import cafe.dto.OrderDto;
import cafe.dto.OrderdetailDto;
import cafe.dto.ToppingDto;
import cafe.entity.Order;
import cafe.entity.OrderDetail;
import cafe.entity.OrderDetailTopping;
import cafe.enums.OrderStatus;
import cafe.modal.OrderResponse;
import cafe.service.MapValidationErrorService;
//import cafe.service.OrderDetailService;
import cafe.service.OrderService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin
public class OrderController {
	@Autowired
	private OrderService orderService;

//	@Autowired
//	private OrderDetailService detailService;

	@Autowired
	private MapValidationErrorService mapValidationErrorService;

	@GetMapping
	public ResponseEntity<?> getAllOrder() {
		return new ResponseEntity<>(orderService.findAll().stream().map(OrderResponse::convert).toList(),
				HttpStatus.OK);
	}

	@GetMapping("/{id}/get")
	public ResponseEntity<?> getOrder(@PathVariable("id") Long id) {
		return orderService.findById(id).map(order -> ResponseEntity.ok(OrderResponse.convert(order))) // Chuyển đổi
																										// Order thành
																										// OrderResponse
				.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderResponse())); // Trả về một
																											// OrderResponse
																											// rỗng hoặc
																											// null
	}

//	@GetMapping("/find")
//	public ResponseEntity<?> getOrdersByStatus(@RequestParam("query") String query, 
//			@PageableDefault(size = 5, sort = "status", direction = Sort.Direction.ASC) Pageable pageable) {
//		return new ResponseEntity<>(orderService.getOrdersByStatus(query, pageable), HttpStatus.OK);
//	}

	@PostMapping
	public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDto dto, BindingResult result) {
		ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationField(result);
		if (responseEntity != null) {
			return responseEntity;
		}
		// Kiểm tra và khởi tạo danh sách rỗng nếu cần
		if (dto.getOrderdetails() == null) {
			dto.setOrderdetails(new ArrayList<>());
		}
		// Tạo Order từ OrderDto
		Order order = orderService.createOrder(dto);
		// Chuyển đổi Order thành OrderDto để trả về cho client
		OrderDto respDto = new OrderDto();
//		respDto.setId(order.getId());
//		respDto.setActive(order.getActive());
//		respDto.setCashier(order.getCashier());
//		respDto.setOrdertype(order.getOrdertype());
//		respDto.setCreatedtime(order.getCreatedtime());
//		respDto.setCustomer(order.getCustomer());
//		respDto.setFulladdresstext(order.getFulladdresstext());
//		// Chuyển đổi OrderDetail thành OrderdetailDto
		List<OrderdetailDto> orderDetailDtos = order.getOrderdetails().stream().map(this::convertToOrderdetailDto)
				.collect(Collectors.toList());
		respDto.setOrderdetails(orderDetailDtos);
//		respDto.setPaymentmethod(order.getPaymentmethod());
//		respDto.setShippingfee(order.getShippingfee());
//		respDto.setStatus(order.getStatus());
//		respDto.setTotalamount(order.getTotalamount());
		BeanUtils.copyProperties(order, respDto);
		return new ResponseEntity<>(respDto, HttpStatus.CREATED);
	}

	private OrderdetailDto convertToOrderdetailDto(OrderDetail orderDetail) {
		OrderdetailDto dto = new OrderdetailDto();
		dto.setProductvariant(orderDetail.getProductvariant());
		dto.setQuantity(orderDetail.getQuantity());
		dto.setMomentprice(orderDetail.getMomentprice());
		dto.setNote(orderDetail.getNote());
		// Chuyển đổi danh sách orderdetailtopping sang DTO
		List<OrderDetailToppingDto> toppingDtos = orderDetail.getOrderdetailtoppings().stream()
				.map(this::convertToOrderDetailToppingDto).collect(Collectors.toList());
		dto.setOrderdetailtoppings(toppingDtos);

		return dto;
	}

	private OrderDetailToppingDto convertToOrderDetailToppingDto(OrderDetailTopping orderDetailTopping) {
		OrderDetailToppingDto dto = new OrderDetailToppingDto();
		dto.setId(orderDetailTopping.getId());
		dto.setQuantity(orderDetailTopping.getQuantity());
		dto.setMomentprice(orderDetailTopping.getMomentprice());

		// Chuyển đổi thông tin topping
		ToppingDto toppingDto = new ToppingDto();
		BeanUtils.copyProperties(orderDetailTopping.getTopping(), toppingDto);
		dto.setTopping(toppingDto);

		return dto;
	}

	// cập nhật
	@PatchMapping("/{id}")
	public ResponseEntity<?> updateStatusOrder(@PathVariable Long id, @RequestBody OrderDto dto) {
		// chỉ quan tâm đến status thôi hà
		Order entity = new Order();
		entity.setStatus(dto.getStatus());
		entity = orderService.updateStatus(id, entity);
		dto.setId(entity.getId());
		return new ResponseEntity<>(dto, HttpStatus.CREATED);

	}

	@GetMapping("/between-dates")
	public ResponseEntity<List<OrderResponse>> getOrdersByDates(
			@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		List<Order> orders = orderService.getOrdersBetweenDates(startDate, endDate);

		// Chuyển đổi từ Order sang OrderResponse
		List<OrderResponse> orderResponses = orders.stream().map(OrderResponse::convert).toList();

		return ResponseEntity.ok(orderResponses);
	}

	@PatchMapping("/{id}/toggle-active")
	public ResponseEntity<Map<String, String>> updateOrderActive(@PathVariable Long id) {
		Order updateOrder = orderService.toggleActive(id);
		Map<String, String> response = new HashMap<>();
		response.put("message", "Order " + (updateOrder.getActive() ? "activated" : "deactivated") + " successfully.");

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/order-statuses")
	public List<OrderStatus> getOrderStatuses() {
		return Arrays.asList(OrderStatus.values());
	}
}
