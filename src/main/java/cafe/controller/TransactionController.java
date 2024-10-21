package cafe.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import cafe.dto.AccountDto;
import cafe.dto.SizeDto;
import cafe.entity.Account;
import cafe.entity.Order;
import cafe.entity.Transactions;
import cafe.enums.OrderStatus;
import cafe.exception.EntityException;
import cafe.service.AccountService;
import cafe.service.MapValidationErrorService;
import cafe.service.OrderService;
import cafe.service.TransactionsService;
import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/transactions")
public class TransactionController {
	@Autowired
	OrderService orderService;

	@Autowired
	TransactionsService transactionsService;
	
	@PostMapping()
	public ResponseEntity<?> createTransaction(@RequestBody Transactions transaction) {
		Optional<Order> found = null;

		try {
	
		
				found = orderService.findById(transaction.getOrder().getId());
				if (found.isEmpty()) {
					throw new EntityException("Order id " + transaction.getOrder().getId() + " does not exist");
				
			}
			transaction.setOrder(found.get());
		
			transactionsService.save(transaction);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(transaction, HttpStatus.OK);
	}

	@PostMapping("/ipn")
	public ResponseEntity<?> handleIPN(@RequestBody Transactions transaction) {
		Transactions found= null;
	Order orderFound = null;

		try {
	
			String[] parts = transaction.getOrderInfo().split(": ");
			if (parts.length > 1) {
				String result = parts[1];
				orderFound = orderService.findById(Long.parseLong(result)).get();
				 found = transactionsService.findByOrder(orderFound);
			}
			transaction.setId(found.getId());
			transaction.setOrder(found.getOrder());
			transaction.setPayUrl(found.getPayUrl());
			transactionsService.save(transaction);
			
			if (found.getOrder().getOrderStatus() == OrderStatus.UNCONFIRMED
					&& (transaction.getResultCode() == 0 || transaction.getResultCode() == 900)) {
				found.getOrder().setOrderStatus(OrderStatus.PROCESSING);
				orderService.save(found.getOrder());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return  ResponseEntity.ok("a");
	}
}
