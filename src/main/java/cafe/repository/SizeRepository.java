package cafe.repository;

 
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cafe.entity.Category;
import cafe.entity.Image;
import cafe.entity.Size;
 

public interface SizeRepository extends JpaRepository<Size, Long> {
//	List<Category> findByNameStartsWith(String name, Pageable pageable);
	List<Size> findByNameContainsIgnoreCase(String name);
}
