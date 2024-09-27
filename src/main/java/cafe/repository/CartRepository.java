package cafe.repository;

 
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import cafe.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByAccountUsername(String username);

}
