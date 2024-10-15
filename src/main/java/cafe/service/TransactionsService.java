package cafe.service;

 



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cafe.entity.Order;
import cafe.entity.Transactions;
import cafe.exception.EntityException;
import cafe.repository.TransactionsRepository;

@Service
public class TransactionsService {
	@Autowired
	private TransactionsRepository transactionsRepository;
 
	public Transactions save(Transactions entity) {
		return transactionsRepository.save(entity);
	}
	
	public Transactions findByOrder(Order order) {
	    return transactionsRepository.findByOrder(order)
	        .orElseThrow(() -> new EntityException("Transaction not found for order ID: " + order.getId()));
	}


}
