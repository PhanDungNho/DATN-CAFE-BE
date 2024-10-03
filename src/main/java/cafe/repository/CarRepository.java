package cafe.repository;

 
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import cafe.entity.Car;

public interface CarRepository extends JpaRepository<Car, Long> {
 

}
