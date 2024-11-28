package cafe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cafe.dto.AccountDto;
import cafe.entity.Account;
import cafe.entity.Authority;
import cafe.entity.Role;
import cafe.exception.EntityException;
import cafe.repository.AccountRepository;

@Service
public class AccountService {
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private RoleService roleService;
	@Autowired
	private FileStorageService fileStorageService;

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
	
	public Account insertAccountAdmin(AccountDto dto) {

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
	    
	    Authority auth = new Authority();
	    Role adminRole = roleService.findById(2L);
	    auth.setAccount(entity);
	    auth.setRole(adminRole);
	    
	    // Tạo danh sách Authority và thêm auth vào danh sách
	    List<Authority> authorities = new ArrayList();
	    authorities.add(auth);
	    
	    // Thiết lập danh sách authorities cho account
	    entity.setAuthorities(authorities);

	    return accountRepository.save(entity);
	}

	
	public Account insertAccountWithGoogle(AccountDto dto) {
		  Account entity = new Account();
		    String baseUsername = dto.getUsername();
		    entity.setUsername(baseUsername);
	    int count = 0;
	    while (accountRepository.findByUsernameContainsIgnoreCase(entity.getUsername()).size() > 0) {
	        count++;
	        entity.setUsername(baseUsername + count);   
	    }
	    entity.setFullName(dto.getFullName());
	    entity.setEmail(dto.getEmail());
	    entity.setImage(dto.getImage());
	    String uuid = UUID.randomUUID().toString().replace("-", "").substring(0,14);
		entity.setPassword(uuid);

	    Authority auth = new Authority();
	    Role adminRole = roleService.findById(3L);
	    auth.setAccount(entity);
	    auth.setRole(adminRole);
	    System.out.println("12345678910");
	    // Tạo danh sách Authority và thêm auth vào danh sách
	    List<Authority> authorities = new ArrayList();
	    authorities.add(auth);
	    
	    // Thiết lập danh sách authorities cho account
	    entity.setAuthorities(authorities);
		
		return accountRepository.save(entity);
	}

	public Account save(AccountDto accountDto) {
		if (accountRepository.findByUsername(accountDto.getUsername()).isPresent()) {
			throw new EntityException("Username " + accountDto.getUsername() + " is exist");
		}
		Account account = new Account();
		BeanUtils.copyProperties(accountDto, account);
		System.out.println(passwordEncoder.encode(accountDto.getPassword()));
		account.setEmail(passwordEncoder.encode(accountDto.getPassword()));
		
//		 Authority auth = new Authority();
//		    Role adminRole = roleService.findById(2L);
//		    auth.setAccount(account);
//		    auth.setRole(adminRole);
//		    
//		    // Tạo danh sách Authority và thêm auth vào danh sách
//		    List<Authority> authorities = new ArrayList();
//		    authorities.add(auth);
//		    
//		    // Thiết lập danh sách authorities cho account
//		    account.setAuthorities(authorities);

		return accountRepository.save(account);
	}

//	 public Account update(String username, AccountDto dto) {
//		    Optional<Account> existed = accountRepository.findById(username);
//		    if (existed.isEmpty()) {
//		        throw new EntityException("Username " + username + " does not exist");
//		    }
//		    Account existedAccount = existed.get();
//		BeanUtils.copyProperties(existed, existedAccount);
//		    return accountRepository.save(existedAccount);
//		}
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
			throw new EntityException("Account with username " + username + " does not exist");
		}
		return found.get();
	}

	public void deleteById(String username) {
		Account existed = findById(username);
		accountRepository.delete(existed);
	}

	public Account update(String username, AccountDto dto) {
		var found = accountRepository.findById(username);

		if (found.isEmpty()) {
			throw new EntityException("Account not found");
		}
		var prevImage = found.get().getImage();
		Account entity = found.get();
		if (dto.getPassword().isBlank() || dto.getPassword() == null) {
			entity.setPassword(entity.getPassword());
			BeanUtils.copyProperties(dto, entity, "password");
		} else {
			BeanUtils.copyProperties(dto, entity);

			entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		}

		if (dto.getImageFile() != null) {
			String filename = fileStorageService.storeLogoFile(dto.getImageFile());

			entity.setImage(filename);
			dto.setImageFile(null);
		}

		if (entity.getImage() == null) {
			entity.setImage(prevImage);
		}

		return accountRepository.save(entity);
	}

	public List<Account> getAdministrators() {
		return accountRepository.getAdministrators();
	}

	public List<Account> findAccountByName(String name) {
		List<Account> list = accountRepository.findByUsernameContainsIgnoreCase(name);
		return list;
	}
	
	public List<Account> findAccountByNameAdmin(String name) {
		List<Account> list = accountRepository.getAdministratorsByUsernameContains(name);
		return list;
	}
	
	public List<Account> findAccountByPhone(String phone) {
		List<Account> list = accountRepository.findByPhoneContainsIgnoreCase(phone);
		return list;
	}

	public Account findByPhone(String phone) {
		Optional<Account> found = accountRepository.findByPhone(phone);
		if (found.isEmpty()) {
			throw new EntityException("Account with phone " + phone + " does not exist");
		}
		return found.get();
	}

	
	public Account findByEmail(String email) {
		Optional<Account> found = accountRepository.findByEmail(email);
		if (found.isEmpty()) {
			throw new EntityException("Account with email " + email + " does not exist");
		}
		return found.get();
	}

	
	public Account update(Account existingAccount) {
	    // Assuming you have a JPA repository
	    return accountRepository.save(existingAccount);
	}
	
	//hieunguyen
	public boolean usernameExists(String username) {
        return accountRepository.existsByUsername(username);
    }
	
	
	
	///
	
	
	public AccountDto getAccountByUsername(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản với tên đăng nhập: " + username));
        return convertToDto(account);
    }


    /**
     * Cập nhật thông tin tài khoản dựa trên AccountDto và email.
     *
     * @param accountDto Thông tin tài khoản mới
     * @param email      Email của tài khoản cần cập nhật
     * @throws UsernameNotFoundException nếu không tìm thấy tài khoản với email cung cấp
     */
    public void updateProfile(AccountDto accountDto, String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản với email: " + email));

        // Cập nhật các trường từ AccountDto nếu có giá trị mới
        if (accountDto.getFullName() != null) {
            account.setFullName(accountDto.getFullName());
        }
        if (accountDto.getPhone() != null) {
            account.setPhone(accountDto.getPhone());
        }
        if (accountDto.getImage() != null) {
            account.setImage(accountDto.getImage());
        }
        if (accountDto.getPassword() != null) {
            account.setPassword(accountDto.getPassword()); // Nhớ mã hóa mật khẩu trước khi lưu nếu cần
        }

        // Lưu tài khoản đã cập nhật vào cơ sở dữ liệu
        accountRepository.save(account);
    }

    /**
     * Chuyển đổi Account entity sang AccountDto.
     *
     * @param account Đối tượng Account cần chuyển đổi
     * @return Đối tượng AccountDto
     */
    private AccountDto convertToDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setFullName(account.getFullName());
        accountDto.setEmail(account.getEmail());
        accountDto.setPhone(account.getPhone());
        accountDto.setImage(account.getImage());
        return accountDto;
    }
	
	
	

}
