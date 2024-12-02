package cafe.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cafe.entity.Account;
import cafe.entity.Address;
import cafe.entity.Category;

public interface AddressRespository extends JpaRepository<Address, Long> {
//	List<Address> findByNameStartsWith(String name, Pageable pageable);
	List<Address> findByAccount(Account account);
    List<Address> findByAccountUsernameAndFullAddressContainingIgnoreCase(String username, String fullAddress);
    List<Address> findByAccountUsernameAndIsDefaultTrue(String username);

}
