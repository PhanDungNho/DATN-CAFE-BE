package cafe.repository;

 
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cafe.entity.Category;
import cafe.entity.Product;
 

public interface ProductRepository extends JpaRepository<Product, Long> {
//	List<Product> findByNameStartsWith(String name, Pageable pageable);
	List<Product> findByNameContainsIgnoreCase(String name);
    List<Product> findByName(String name); // Tìm kiếm sản phẩm theo tên chính xác
	Optional<Product> findBySlug(String slug);
	
}
