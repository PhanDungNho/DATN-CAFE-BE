package cafe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cafe.dto.AccountRoleDto;
import cafe.entity.Account;
import cafe.entity.AccountRole;
import cafe.entity.Role;
import cafe.entity.exception.EntityException;
import cafe.repository.AccountRepository;
import cafe.repository.AccountRoleRepository;
import cafe.repository.CategoryRepository;
import cafe.repository.ProductRepository;
import cafe.repository.RoleRepository;

@Service
public class AccountRoleService {
	@Autowired
	private AccountRoleRepository accountRoleRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private RoleRepository roleRepository ;

	
	 public AccountRole save(AccountRoleDto accountRoleDto) {
	        // Tạo mới một Product entity từ ProductDto
		 	AccountRole accountRole = new AccountRole();

		 	Account account = accountRepository.findById(accountRoleDto.getAccount())
	            .orElseThrow(() -> new EntityException("Account id " + accountRoleDto.getAccount() + " does not exist"));
	        accountRole.setAccount(account);

	        Role role = roleRepository.findById(accountRoleDto.getRoleid())
		            .orElseThrow(() -> new EntityException("Role id " + accountRoleDto.getRoleid() + " does not exist"));
		        accountRole.setRole(role);
	        
	        // Lưu sản phẩm vào cơ sở dữ liệu
	        return accountRoleRepository.save(accountRole);
	}
	 
	
	 public AccountRole update(Long id, AccountRoleDto dto) {
		    Optional<AccountRole> existed = accountRoleRepository.findById(id);
		    if (existed.isEmpty()) {
		        throw new EntityException("AccountRole id " + id + " does not exist");
		    }

		    AccountRole existedAccountRole = existed.get();

		    if (dto.getAccount() != null) {
		        Optional<Account> account = accountRepository.findById(dto.getAccount());
		        if (account.isPresent()) {
		        	existedAccountRole.setAccount(account.get());
		        } else {
		            throw new EntityException("Account id " + dto.getAccount() + " does not exist");
		        }
		    }
		    
		    if (dto.getRoleid() != null) {
		        Optional<Role> role = roleRepository.findById(dto.getRoleid());
		        if (role.isPresent()) {
		        	existedAccountRole.setRole(role.get());
		        } else {
		            throw new EntityException("Role id " + dto.getAccount() + " does not exist");
		        }
		    }

		    return accountRoleRepository.save(existedAccountRole);
		}

	 

	 

	public List<AccountRole> findAll() {
		return accountRoleRepository.findAll();
	}

	public Page<AccountRole> findAll(Pageable pageable) {
		return accountRoleRepository.findAll(pageable);
	}

	public AccountRole findById(Long id) {
		Optional<AccountRole> found = accountRoleRepository.findById(id);
		if (found.isEmpty()) {
			throw new EntityException("Product with id " + id + " does not exist");
		}
		return found.get();
	}
	
	public void deleteById(Long id) {
		AccountRole existed = findById(id);
		accountRoleRepository.delete(existed);
	}
}
