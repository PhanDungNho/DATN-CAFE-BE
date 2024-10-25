package cafe.service;

import cafe.dto.AccountDto;
import cafe.entity.Account;
import cafe.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Tiêm PasswordEncoder

    public AccountDto getProfile() {
        // Assume a logged-in user; retrieve profile by a unique identifier like email or ID.
        Account account = accountRepository.findByEmail("dohieunguyen01@gmail.com") // Replace with real identifier
            .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        // Map Account to AccountDto
        AccountDto accountDto = new AccountDto();
        accountDto.setFullName(account.getFullName());
        accountDto.setEmail(account.getEmail());
        accountDto.setPhone(account.getPhone());
        return accountDto;
    }

    public void updateProfile(AccountDto accountDto) {
        Account account = accountRepository.findByEmail(accountDto.getEmail())
            .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        account.setFullName(accountDto.getFullName());
        account.setPhone(accountDto.getPhone());
        
        // Giả sử bạn có logic để xử lý lưu trữ và lấy hình ảnh
        if (accountDto.getImage() != null && !accountDto.getImage().isEmpty()) {
            account.setImage(accountDto.getImage());
        }

        if (accountDto.getPassword() != null && !accountDto.getPassword().isEmpty()) {
            account.setPassword(accountDto.getPassword());
        }
        

        accountRepository.save(account);
    }

}

