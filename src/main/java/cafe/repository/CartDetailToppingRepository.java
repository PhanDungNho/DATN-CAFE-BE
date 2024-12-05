package cafe.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cafe.entity.CartDetailTopping;
import cafe.entity.Category;
import jakarta.transaction.Transactional;

public interface CartDetailToppingRepository extends JpaRepository<CartDetailTopping, Long> {
	@Modifying
	@Transactional
	@Query("DELETE FROM CartDetailTopping c WHERE c.id = :id")
	void deleteByIdCartDetailTopping(@Param("id") Long id);
}
