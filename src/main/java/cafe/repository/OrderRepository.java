package cafe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cafe.entity.Order;
import cafe.enums.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {

	Page<Order> findByStatusContainsIgnoreCase(OrderStatus status, Pageable pageable);
}
