package cafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cafe.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

	List<OrderDetail> findByOrderId(Long orderId);
	 @Query("SELECT od.productVariant.product.id, SUM(od.quantity) as totalSales " +
	           "FROM OrderDetail od " +
	           "GROUP BY od.productVariant.product.id " +
	           "ORDER BY totalSales DESC")
	    List<Object[]> findTopSellingProducts();
}
