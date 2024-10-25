package cafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cafe.entity.ProductToppings;

public interface ProductToppingRepository extends JpaRepository<ProductToppings, Long> {
//	@Modifying
//	@Query("DELETE FROM ProductToppings pt WHERE pt.product.id = :productId")
//	void deleteAllByProductId(@Param("productId") Long productId);
	
	 List<ProductToppings> findByProductId(Long productId);
	 
	 void deleteByProductId(Long productId);
}
