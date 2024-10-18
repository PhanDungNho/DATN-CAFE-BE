package cafe.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cafe.dto.AccountDto;
import cafe.entity.Account;
import cafe.exception.EntityException;
import cafe.repository.AccountRepository;

@Service
public class RegisterService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Hàm đăng ký tài khoản mới
    public Account register(AccountDto dto) {
        // Kiểm tra username đã tồn tại chưa
        if (accountRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new EntityException("Username is already existed.");
        }

        // Kiểm tra email đã tồn tại chưa
        if (accountRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new EntityException("Email is already existed.");
        }

        // Tạo đối tượng Account từ DTO
        Account account = new Account();
        BeanUtils.copyProperties(dto, account);

        // Mã hóa mật khẩu
        account.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Lưu tài khoản vào cơ sở dữ liệu
        return accountRepository.save(account);
    }
}
