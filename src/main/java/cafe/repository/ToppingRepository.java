package cafe.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cafe.entity.Category;
import cafe.entity.Topping;

public interface ToppingRepository extends JpaRepository<Topping, Long> {
	List<Topping> findByNameStartsWith(String name, Pageable pageable);

//	Page<Topping> findByNameContainsIgnoreCase(String name, Pageable pageable);
	List<Topping> findByNameContainsIgnoreCase(String name);

}
