package cafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cafe.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
