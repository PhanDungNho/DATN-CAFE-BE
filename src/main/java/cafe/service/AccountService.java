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
		}
		return found.get();
	}
	
	public void deleteById(String username) {
		Account existed = findById(username);
		accountRepository.delete(existed);
	}
	
}
