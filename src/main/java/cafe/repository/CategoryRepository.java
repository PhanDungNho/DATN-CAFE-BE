package cafe.repository;

 
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

 

import cafe.entity.Category;
 

public interface CategoryRepository extends JpaRepository<Category, Long> {
	List<Category> findByNameStartsWith(String name, Pageable pageable);
	List<Category> findByNameContainsIgnoreCase(String name);

}
