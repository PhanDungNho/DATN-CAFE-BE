package cafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cafe.entity.Role;


public interface RoleRepository extends JpaRepository<Role, Long>{

}
