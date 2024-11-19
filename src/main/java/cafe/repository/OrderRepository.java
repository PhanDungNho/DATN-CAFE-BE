package cafe.repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cafe.entity.Account;
import cafe.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByCreatedTimeBetween(Date startDate, Date endDate);
	
	List<Order> findByCustomer(Account customer);
	@Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.active = true")
    double calculateTotalRevenue();
	// Truy vấn số lượng đơn hàng
    @Query("SELECT COUNT(o) FROM Order o WHERE o.active = true")
    int countOrders();
    // Truy vấn để lấy sản phẩm được mua nhiều nhất
    @Query("SELECT p.name AS productName, SUM(od.quantity) AS totalQuantity, SUM(od.quantity * od.momentPrice) AS totalAmount " +
    	       "FROM Order o " +
    	       "JOIN o.orderDetails od " +
    	       "JOIN od.productVariant pv " +
    	       "JOIN pv.product p " +
    	       "WHERE o.active = true " +
    	       "GROUP BY p.name " +
    	       "ORDER BY totalQuantity LIMIT 5")
    	List<Object[]> findTop5MostPurchasedProductsWithTotalAmount();
    	@Query(value = """
    		    SELECT TOP 5 
    		        p.product_name AS productName,
    		        SUM(od.quantity) AS totalQuantity,
    		        SUM(od.quantity * od.moment_price) AS totalAmount,
    		        o.created_time AS saleDate
    		    FROM orders o
    		    JOIN order_details od ON o.id = od.order_id
    		    JOIN products p ON od.product_variant_id = p.id
    		    WHERE o.created_time BETWEEN :startDate AND :endDate
    		    GROUP BY p.product_name, o.created_time
    		    ORDER BY totalQuantity DESC
    		""", nativeQuery = true)
    		List<Object[]> findTop5MostPurchasedProductsByDateRange(
    		    @Param("startDate") Date startDate,
    		    @Param("endDate") Date endDate
    		);


    		// Lấy doanh thu theo ngày
    	    @Query("SELECT new map(DAY(o.createdTime) as day, SUM(o.totalAmount) as revenue) " +
    	           "FROM Order o WHERE YEAR(o.createdTime) = :year AND MONTH(o.createdTime) = :month " +
    	           "GROUP BY DAY(o.createdTime) ORDER BY DAY(o.createdTime)")
    	    List<Map<String, Object>> getDailyRevenueByMonth(@Param("year") int year, @Param("month") int month);

    	    // Lấy doanh thu theo tháng
    	    @Query("SELECT new map(MONTH(o.createdTime) as month, SUM(o.totalAmount) as revenue) " +
    	           "FROM Order o WHERE YEAR(o.createdTime) = :year " +
    	           "GROUP BY MONTH(o.createdTime) ORDER BY MONTH(o.createdTime)")
    	    List<Map<String, Object>> getMonthlyRevenueByYear(@Param("year") int year);
    	    
    	    
    	    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.createdTime BETWEEN :startDate AND :endDate")
    	    BigDecimal sumTotalRevenue(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    	    @Query("SELECT COUNT(o) FROM Order o WHERE o.createdTime BETWEEN :startDate AND :endDate")
    	    long countOrders(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);




   
}
