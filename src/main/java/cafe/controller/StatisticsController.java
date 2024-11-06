package cafe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cafe.service.OrderService;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin
public class StatisticsController {

    private final OrderService orderService;

    @Autowired
    public StatisticsController(OrderService orderService) {
        this.orderService = orderService;
    }

	@GetMapping("/total-orders")
	public ResponseEntity<Long> getTotalOrders() {
	    long totalOrders = orderService.countTotalOrders(); // Gọi phương thức từ OrderService
	    return ResponseEntity.ok(totalOrders); // Trả về tổng số đơn hàng
	}
}
