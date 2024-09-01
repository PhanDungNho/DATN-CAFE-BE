package cafe.repository;

 
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cafe.entity.Category;
import cafe.entity.ProductVariant;
 

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
//	List<Category> findByNameStartsWith(String name, Pageable pageable);

} 
