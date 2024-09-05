package cafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cafe.entity.Order;
import cafe.entity.OrderDetail;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
