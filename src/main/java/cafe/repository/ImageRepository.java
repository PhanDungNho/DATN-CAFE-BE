package cafe.repository;

 
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cafe.entity.Category;
import cafe.entity.Image;
 

public interface ImageRepository extends JpaRepository<Image, Integer> {
//	List<Category> findByNameStartsWith(String name, Pageable pageable);

}
