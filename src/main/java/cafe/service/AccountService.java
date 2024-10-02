package cafe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cafe.entity.Account;
import cafe.entity.exception.EntityException;
import cafe.repository.AccountRepository;

@Service
public class AccountService {
	
	@Autowired
	private AccountRepository accountRepository;
	
	public Account save(Account entity) {
		return accountRepository.save(entity);
	}
	
	
	public Account update(String username, Account entity) {
		Optional<Account> existed = accountRepository.findById(username);
		if (existed.isEmpty()) {
			// neu k tim thay thi tra ve ngoai le
			throw new EntityException("Account id " + username + " does not exist");

		}
		try {
			Account existedAccount = existed.get();
			existedAccount.setActive(entity.getActive());
			existedAccount.setAmountpaid(entity.getAmountpaid());
			existedAccount.setEmail(entity.getEmail());
			existedAccount.setFullname(entity.getFullname());
			existedAccount.setPassword(entity.getPassword());
			existedAccount.setPhone(entity.getPhone());

			return accountRepository.save(existedAccount);
			// thì tiến hành cập nhật thủ công bth
		} catch (Exception ex) {
			// nếu có lỗi sẽ ném ra ngoại lệ
			throw new EntityException("Account is updated failed");
		}

	}
	
	
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



import cafe.dto.AccountDto;

import cafe.entity.Account;

import cafe.entity.Product;
import cafe.entity.Role;
import cafe.exception.EntityException;
import cafe.repository.AccountRepository;

import cafe.repository.RoleRepository;

@Service
public class AccountService {
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private FileStorageService fileStorageService;
	

public Account save(AccountDto accountDto) {
	if(accountRepository.findByUsername(accountDto.getUsername()).isPresent()) {
		throw new EntityException("Username " + accountDto.getUsername() + " is exist");
	}
    Account account = new Account();
    BeanUtils.copyProperties(accountDto, account);
    System.out.println(passwordEncoder.encode(accountDto.getPassword()));
    account.setEmail(passwordEncoder.encode(accountDto.getPassword()));

    return accountRepository.save(account);
}

public Account insertAccount(AccountDto dto) {

	List<?> foundedList = accountRepository.findByUsernameContainsIgnoreCase(dto.getUsername());
	if (foundedList.size() > 0) {
		throw new EntityException("Username is existed");
	}
	Account entity = new Account();
	BeanUtils.copyProperties(dto, entity);
	entity.setPassword(passwordEncoder.encode(dto.getPassword()));

	if (dto.getImageFile() != null) {
		String filename = fileStorageService.storeLogoFile(dto.getImageFile());
		entity.setImage(filename);
		dto.setImage(filename);
	}
	return accountRepository.save(entity);
}
	 
	
	 public Account update(String username, AccountDto dto) {
		    Optional<Account> existed = accountRepository.findById(username);
		    if (existed.isEmpty()) {
		        throw new EntityException("Username " + username + " does not exist");
		    }
		    Account existedAccount = existed.get();
		BeanUtils.copyProperties(existed, existedAccount);
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
			throw new EntityException("Role with id " + username + " does not exist");
			throw new EntityException("Account with username " + username + " does not exist");
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
	
/////////////////////
	
 
	
 
	 
	 
	public List<Account> getAdministrators() {
		return accountRepository.getAdministrators();
	}
 

	
}
