package cafe.repository;

 
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cafe.entity.Category;
import cafe.entity.Image;
 

public interface ImageRepository extends JpaRepository<Image, Long> {

	List<Image> findByProductId(Long productId);
	void deleteByFileName(String filename);
}
