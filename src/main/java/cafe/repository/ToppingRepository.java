package cafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cafe.entity.Topping;

public interface ToppingRepository extends JpaRepository<Topping, Long> {

}
