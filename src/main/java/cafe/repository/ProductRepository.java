package cafe.repository;

 
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cafe.entity.Category;
import cafe.entity.Product;
 

public interface ProductRepository extends JpaRepository<Product, Long> {
//	List<Product> findByNameStartsWith(String name, Pageable pageable);
	List<Product> findByNameContainsIgnoreCase(String name);
}
