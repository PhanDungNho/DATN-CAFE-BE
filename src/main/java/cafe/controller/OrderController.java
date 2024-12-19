package cafe.controller;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
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
import org.springframework.web.bind.annotation.DeleteMapping;
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
import cafe.enums.PaymentStatus;
import cafe.modal.OrderResponse;
import cafe.repository.AccountRepository;
import cafe.repository.OrderRepository;
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
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private AccountRepository accountRepository;

//	@Autowired
//	private OrderDetailService detailService;

	@Autowired
	private MapValidationErrorService mapValidationErrorService;

	@GetMapping
	public ResponseEntity<?> getAllOrder() {
		return new ResponseEntity<>(orderService.findAll().stream().map(OrderResponse::convert)
				.sorted(Comparator.comparing(OrderResponse::getId).reversed()).toList(), HttpStatus.OK);
	}
	
	@GetMapping("/{username}/getByUser")
	public ResponseEntity<?> getAllOrderByUser(@PathVariable("username") String username) {
		return new ResponseEntity<>(orderService.findByCustomer(username).stream().map(OrderResponse::convert)
				.sorted(Comparator.comparing(OrderResponse::getId).reversed()).toList(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}/get")
	public ResponseEntity<?> getOrder(@PathVariable("id") Long id) {
		return orderService.findById(id).map(order -> ResponseEntity.ok(OrderResponse.convert(order))) // Chuyển đổi
				.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderResponse())); // Trả về một
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
		if (dto.getOrderDetails() == null) {
			dto.setOrderDetails(new ArrayList<>());
		}
		// Tạo Order từ OrderDto
		Order order = orderService.createOrder(dto);
		// Chuyển đổi Order thành OrderDto để trả về cho client
		OrderDto respDto = new OrderDto();
		List<OrderdetailDto> orderDetailDtos = order.getOrderDetails().stream().map(this::convertToOrderdetailDto)
				.collect(Collectors.toList());
		respDto.setOrderDetails(orderDetailDtos);
		BeanUtils.copyProperties(order, respDto);
		return new ResponseEntity<>(respDto, HttpStatus.CREATED);
	}

	private OrderdetailDto convertToOrderdetailDto(OrderDetail orderDetail) {
		OrderdetailDto dto = new OrderdetailDto();
		dto.setProductVariant(orderDetail.getProductVariant());
		dto.setQuantity(orderDetail.getQuantity());
		dto.setMomentPrice(orderDetail.getMomentPrice());
		dto.setNote(orderDetail.getNote());
		// Chuyển đổi danh sách orderdetailtopping sang DTO
		List<OrderDetailToppingDto> toppingDtos = orderDetail.getOrderDetailToppings().stream()
				.map(this::convertToOrderDetailToppingDto).collect(Collectors.toList());
		dto.setOrderDetailToppings(toppingDtos);

		return dto;
	}

	private OrderDetailToppingDto convertToOrderDetailToppingDto(OrderDetailTopping orderDetailTopping) {
		OrderDetailToppingDto dto = new OrderDetailToppingDto();
		dto.setId(orderDetailTopping.getId());
		dto.setQuantity(orderDetailTopping.getQuantity());
		dto.setMomentPrice(orderDetailTopping.getMomentPrice());

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
		entity.setOrderStatus(dto.getOrderStatus());
		if(dto.getOrderStatus()==OrderStatus.CANCELLED) {
			entity.setPaymentStatus(PaymentStatus.REFUND);
		}
		entity = orderService.updateStatus(id, entity);
		if(dto.getOrderStatus()==OrderStatus.CANCELLED) {
			
		}
		dto.setId(entity.getId());
		return new ResponseEntity<>(entity, HttpStatus.CREATED);

	}

	@GetMapping("/between-dates")
	public ResponseEntity<List<OrderResponse>> getOrdersByDates(
	    @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
	    @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
	    List<Order> orders = orderService.getOrdersBetweenDates(startDate, endDate);
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
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
		orderService.deleteOrder(id);
		return new ResponseEntity<>("Order with Id: " + id + " was deleted", HttpStatus.OK);
	}
	@GetMapping("/orderCount")
    public ResponseEntity<Integer> getOrderCount() {
        int orderCount = orderRepository.countOrders();
        return ResponseEntity.ok(orderCount);
    }

    @GetMapping("/revenue")
    public ResponseEntity<Map<String, Object>> getRevenue() {
        double totalRevenue = orderRepository.calculateTotalRevenue(); // Tính tổng doanh thu cho tất cả đơn hàng
        Map<String, Object> response = new HashMap<>();
        response.put("total", totalRevenue);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/accountCount")
    public ResponseEntity<Integer> getAccountCount() {
        int accountCount = accountRepository.countActiveAccounts();
        return ResponseEntity.ok(accountCount);
    }
    @GetMapping("/most-purchased-products")
    public ResponseEntity<List<Map<String, Object>>> getMostPurchasedProductsInLast2Months() {
        // Lấy kết quả từ repository với dữ liệu 2 tháng gần nhất
        List<Object[]> results = orderRepository.findTop5MostPurchasedProductsInLast2Months();

        // Chuẩn bị phản hồi trả về
        List<Map<String, Object>> response = new ArrayList<>();
        for (Object[] result : results) {
            Map<String, Object> productInfo = new HashMap<>();
            productInfo.put("productName", result[0]);       // Tên sản phẩm
            productInfo.put("totalQuantity", result[1]);     // Tổng số lượng
            productInfo.put("totalAmount", 
                ((BigDecimal) result[2]).setScale(2, RoundingMode.HALF_UP)); // Tổng tiền (định dạng 2 chữ số thập phân)
            response.add(productInfo);
        }

        // Trả về danh sách sản phẩm
        return ResponseEntity.ok(response);
    }


    @GetMapping("/most-purchased-products/by-date")
    public ResponseEntity<List<Map<String, Object>>> getMostPurchasedProductsByDate(
        @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
        @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        
        // Lấy dữ liệu từ repository
        List<Object[]> results = orderRepository.findTop5MostPurchasedProductsByDateRange(startDate, endDate);

        // Xử lý kết quả trả về
        List<Map<String, Object>> response = new ArrayList<>();
        for (Object[] result : results) {
            Map<String, Object> productInfo = new HashMap<>();
            productInfo.put("productName", result[0]);       // Tên sản phẩm
            productInfo.put("totalQuantity", result[1]);     // Tổng số lượng
            productInfo.put("totalAmount", 
                ((BigDecimal) result[2]).setScale(2, RoundingMode.HALF_UP)); // Tổng tiền (2 chữ số thập phân)
            response.add(productInfo);
        }

        // Trả về JSON response
        return ResponseEntity.ok(response);
    }

 // Lấy doanh thu theo ngày (theo tháng và năm)	
    @GetMapping("/daily-revenue")
    public ResponseEntity<List<Map<String, Object>>> getDailyRevenue(
        @RequestParam("year") int year,
        @RequestParam("month") int month) {

        List<Map<String, Object>> dailyRevenue = orderService.getDailyRevenue(year, month);
        return ResponseEntity.ok(dailyRevenue);
    }
 // Lấy doanh thu theo tháng (theo năm)
    @GetMapping("/monthly-revenue")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyRevenue(@RequestParam("year") int year) {
        List<Map<String, Object>> monthlyRevenue = orderService.getMonthlyRevenue(year);
        return ResponseEntity.ok(monthlyRevenue);
    }
 
    @GetMapping("/totals")
    public ResponseEntity<?> getTotalRevenueAndOrders(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        BigDecimal totalRevenue = orderService.getTotalRevenue(startDate, endDate);
        long totalOrders = orderService.getOrderCount(startDate, endDate);
        
        Map<String, Object> response = new HashMap<>();
        response.put("totalRevenue", totalRevenue);
        response.put("totalOrders", totalOrders);

        return ResponseEntity.ok(response);
    }
}
