package cafe.repository;

 
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cafe.entity.CartDetail;

public interface CartRepository extends JpaRepository<CartDetail, Long> {
    List<CartDetail> findByAccountUsername(String username);

}
