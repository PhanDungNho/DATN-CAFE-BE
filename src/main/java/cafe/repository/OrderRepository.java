package cafe.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cafe.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findBycreatedtimeBetween(Date startDate, Date endDate);
}
