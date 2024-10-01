package cafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cafe.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

	List<OrderDetail> findByOrderId(Long orderId);

}
