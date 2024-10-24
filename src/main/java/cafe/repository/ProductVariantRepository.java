package cafe.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cafe.entity.Category;
import cafe.entity.Product;
import cafe.entity.ProductVariant;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
//	List<Category> findByNameStartsWith(String name, Pageable pageable);
	List<ProductVariant> findByProductNameContainsIgnoreCase(String name);

	@Modifying
	@Query("DELETE FROM ProductVariant pv WHERE pv.product.id = :productId")
	void deleteAllByProductId(@Param("productId") Long productId);
}
