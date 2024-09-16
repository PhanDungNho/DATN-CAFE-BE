package cafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cafe.entity.OrderDetailTopping;

public interface OrderDetailToppingRepository extends JpaRepository<OrderDetailTopping, Long> {

}
