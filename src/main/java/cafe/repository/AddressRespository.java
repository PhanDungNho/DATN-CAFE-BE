package cafe.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cafe.entity.Address;

public interface AddressRespository extends JpaRepository<Address, Long> {
//	List<Address> findByNameStartsWith(String name, Pageable pageable);
}
