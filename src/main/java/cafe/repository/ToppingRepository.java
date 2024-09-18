package cafe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cafe.dto.ToppingDto;
import cafe.entity.Topping;

public interface ToppingRepository extends JpaRepository<Topping, Long> {

	Page<Topping> findByNameContainsIgnoreCase(String name, Pageable pageable);

}
