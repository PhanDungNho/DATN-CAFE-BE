package cafe.repository;

 
 
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cafe.entity.Order;
import cafe.entity.Transactions;

 

public interface TransactionsRepository extends JpaRepository<Transactions, Long> {
 Optional<Transactions> findByOrder(Order order);
}