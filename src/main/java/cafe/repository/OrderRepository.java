package cafe.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cafe.entity.Account;
import cafe.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByCreatedTimeBetween(Date startDate, Date endDate);
	
	List<Order> findByCustomer(Account customer);
}
