package cafe.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cafe.entity.Order;
import cafe.entity.OrderDetail;
import cafe.service.OrderDetailService;
import cafe.service.OrderService;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin
public class OrderController {
	@Autowired
	OrderService orderService;
	
	@Autowired
	OrderDetailService detailService;

	@GetMapping
	public ResponseEntity<?> getAllOrder() {
		return new ResponseEntity<>(orderService.findAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}/details")
	public List<OrderDetail> getOrderDetails(@PathVariable("id") Long id) {
		return detailService.getOrderDetailsByOrderId(id);
	}
	
	@GetMapping("/{id}/get")
	public ResponseEntity<?> getOrder(@PathVariable("id") Long id) {
	    Optional<Order> orderOptional = orderService.findById(id);
	    if (orderOptional.isPresent()) {
	        return ResponseEntity.ok(orderOptional.get());
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                             .body("Order not found with ID: " + id);
	    }
	}

}
