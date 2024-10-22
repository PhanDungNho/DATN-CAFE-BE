package cafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cafe.entity.ProductToppings;

public interface ProductToppingRepository extends JpaRepository<ProductToppings, Long> {

}
