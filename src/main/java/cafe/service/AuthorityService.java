package cafe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cafe.dto.AuthorityDto;
import cafe.entity.Account;
import cafe.entity.Authority;
import cafe.entity.Product;
import cafe.entity.Role;
import cafe.exception.EntityException;
import cafe.repository.AccountRepository;

import cafe.repository.AuthorityRepository;
import cafe.repository.CategoryRepository;
import cafe.repository.ProductRepository;
import cafe.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class AuthorityService {
	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private RoleRepository roleRepository;

	// tìm
	public List<Authority> findAll() {
		return authorityRepository.findAll();
	}

	// cấp quyền
	public Authority create(AuthorityDto authDto) {
		Authority auth = new Authority();
		Role role = roleRepository.findById(authDto.getRoleId()).orElseThrow(() -> new EntityException("Role id " + authDto.getRoleId()+ " does not exist"));
		auth.setRole(role);
		
		Account account = accountRepository.findById(authDto.getUsername()).orElseThrow(() -> new EntityException("Username " + authDto.getUsername() + " does not exist"));
		auth.setRole(role);
		auth.setAccount(account);
		
		return authorityRepository.save(auth);
	}

	public Authority findById(Long id) {
		Optional<Authority> found = authorityRepository.findById(id);
		if (found.isEmpty()) {
			throw new EntityException("Authority with id " + id + " does not exist");
		}
		return found.get();
	}
	
	@Transactional
	public void delete(Long id) {
	    try {
	        // Tìm kiếm Authority
	        Authority existed = findById(id);
	        
	        if (existed != null) {
	            // Nếu có mối quan hệ với Account hay Role, hãy giải phóng chúng/kết thúc nó
	             existed.setAccount(null);
	             existed.setRole(null);

	            // Xóa Authority
	            authorityRepository.delete(existed);
	        }
	    } catch (EntityNotFoundException e) {
	        System.out.println("Authority not found: " + e.getMessage());
	    } catch (Exception e) {
	        System.out.println("Error while deleting authority: " + e.getMessage());
	    }
	}

	public List<Authority> findAuthorityOfAdministrator() {
		List<Account> accounts = accountRepository.getAdministrators();
		for (Account account : accounts) {
			System.out.println(account.getUsername());
		}
		return authorityRepository.authoritiesOf(accounts);
	}

}
