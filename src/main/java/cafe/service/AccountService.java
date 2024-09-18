package cafe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cafe.dto.AccountDto;
import cafe.dto.AccountRoleDto;
import cafe.entity.Account;
import cafe.entity.AccountRole;
import cafe.entity.Product;
import cafe.entity.Role;
import cafe.entity.exception.EntityException;
import cafe.repository.AccountRepository;
import cafe.repository.AccountRoleRepository;
import cafe.repository.RoleRepository;

@Service
public class AccountService {
	
	@Autowired
	private AccountRepository accountRepository;
	
	 public Account save(AccountDto accountDto) {
		 	Account account = new Account();
		 	account.setUsername(accountDto.getUsername());
		 	account.setPassword(accountDto.getPassword());
		 	account.setActive(accountDto.getActive());
		 	account.setAmountpaid(accountDto.getAmountpaid());
		 	account.setEmail(accountDto.getEmail());
		 	account.setPhone(accountDto.getPhone());
		 	account.setFullname(accountDto.getFullname());
	        // Lưu sản phẩm vào cơ sở dữ liệu
	        return accountRepository.save(account);
	}
	 
	
	 public Account update(String username, AccountDto dto) {
		    Optional<Account> existed = accountRepository.findById(username);
		    if (existed.isEmpty()) {
		        throw new EntityException("Username " + username + " does not exist");
		    }
		    Account existedAccount = existed.get();
		    existedAccount.setUsername(dto.getUsername());
		    existedAccount.setActive(dto.getActive());
		    existedAccount.setAmountpaid(dto.getAmountpaid());
		    existedAccount.setEmail(dto.getEmail());
		    existedAccount.setFullname(dto.getFullname());
		    existedAccount.setPassword(dto.getPassword());
		    existedAccount.setPhone(dto.getPhone());
		    return accountRepository.save(existedAccount);
		}
	// để bật tắt active
	   public Account toggleActive(String username) {
	        Optional<Account> optionalAccount = accountRepository.findById(username);
	        if (optionalAccount.isEmpty()) {
	            throw new EntityException("Username " + username + " does not exist");
	        }

	        Account account = optionalAccount.get();
	        account.setActive(!account.getActive()); // Đảo ngược trạng thái active
	        return accountRepository.save(account); // Lưu thay đổi vào cơ sở dữ liệu
	    }
	public List<Account> findAll() {
		return accountRepository.findAll();
	}

	public Page<Account> findAll(Pageable pageable) {
		return accountRepository.findAll(pageable);
	}

	public Account findById(String username) {
		Optional<Account> found = accountRepository.findById(username);
		if (found.isEmpty()) {
			throw new EntityException("Product with id " + username + " does not exist");
		}
		return found.get();
	}
	
	public void deleteById(String username) {
		Account existed = findById(username);
		accountRepository.delete(existed);
	}
	
	public Account update(Account existingAccount) {
	    // Assuming you have a JPA repository
	    return accountRepository.save(existingAccount);
	}
	
	
	
}
