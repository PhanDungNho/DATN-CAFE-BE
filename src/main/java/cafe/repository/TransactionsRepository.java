package cafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cafe.entity.Transactions;

public interface TransactionsRepository extends JpaRepository<Transactions, Long>{

}