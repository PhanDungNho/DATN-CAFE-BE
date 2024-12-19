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

	// Tính tổng doanh thu trong tháng trước
    @Query(value = """
            SELECT SUM(o.total_amount)
            FROM orders o
            WHERE o.active = 1
              AND YEAR(o.created_time) = YEAR(DATEADD(MONTH, -1, GETDATE()))
              AND MONTH(o.created_time) = MONTH(DATEADD(MONTH, -1, GETDATE()))
            """, nativeQuery = true)
    Double calculateTotalRevenueLastMonth();

    // Đếm tổng số đơn hàng trong tháng trước
    @Query(value = """
            SELECT COUNT(*)
            FROM orders o
            WHERE o.active = 1
              AND YEAR(o.created_time) = YEAR(DATEADD(MONTH, -1, GETDATE()))
              AND MONTH(o.created_time) = MONTH(DATEADD(MONTH, -1, GETDATE()))
            """, nativeQuery = true)
    Integer countOrdersLastMonth();

	// Truy vấn để lấy sản phẩm được mua nhiều nhất
	@Query(value = """
		    SELECT TOP 5
		        p.name AS productName,
		        SUM(od.quantity) AS totalQuantity,
		        SUM(od.quantity * od.moment_price) AS totalAmount
		    FROM orders o
		    JOIN order_details od ON o.id = od.order_id
		    JOIN product_variants pv ON od.product_variant_id = pv.id
		    JOIN products p ON pv.product_id = p.id
		    WHERE o.active = 1 
		      AND o.created_time >= DATEADD(MONTH, -2, GETDATE()) -- 2 tháng gần nhất
		    GROUP BY p.name
		    ORDER BY totalQuantity DESC
		""", nativeQuery = true)
		List<Object[]> findTop5MostPurchasedProductsInLast2Months();


	@Query(value = """
		    SELECT TOP 5
		        p.name AS productName,
		        SUM(od.quantity) AS totalQuantity,
		        SUM(od.quantity * od.moment_price) AS totalAmount
		    FROM orders o
		    JOIN order_details od ON o.id = od.order_id
		    JOIN product_variants pv ON od.product_variant_id = pv.id
		    JOIN products p ON pv.product_id = p.id
		    WHERE o.created_time BETWEEN :startDate AND :endDate
		    GROUP BY p.name
		    ORDER BY totalQuantity DESC
		""", nativeQuery = true)
		List<Object[]> findTop5MostPurchasedProductsByDateRange(
		    @Param("startDate") Date startDate,
		    @Param("endDate") Date endDate);




	// Lấy doanh thu theo ngày
	@Query("SELECT new map(DAY(o.createdTime) as day, SUM(o.totalAmount) as revenue) "
			+ "FROM Order o WHERE YEAR(o.createdTime) = :year AND MONTH(o.createdTime) = :month "
			+ "GROUP BY DAY(o.createdTime) ORDER BY DAY(o.createdTime)")
	List<Map<String, Object>> getDailyRevenueByMonth(@Param("year") int year, @Param("month") int month);

	// Lấy doanh thu theo tháng
	@Query("SELECT new map(MONTH(o.createdTime) as month, SUM(o.totalAmount) as revenue) "
			+ "FROM Order o WHERE YEAR(o.createdTime) = :year "
			+ "GROUP BY MONTH(o.createdTime) ORDER BY MONTH(o.createdTime)")
	List<Map<String, Object>> getMonthlyRevenueByYear(@Param("year") int year);

	@Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.createdTime BETWEEN :startDate AND :endDate")
	BigDecimal sumTotalRevenue(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

	@Query("SELECT COUNT(o) FROM Order o WHERE o.createdTime BETWEEN :startDate AND :endDate")
	long countOrders(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

}
