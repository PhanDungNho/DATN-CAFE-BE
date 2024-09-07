package cafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cafe.entity.Account;

public interface AccountRepository extends JpaRepository<Account, String>{

}
