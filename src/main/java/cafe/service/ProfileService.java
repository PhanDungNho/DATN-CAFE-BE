package cafe.service;

import cafe.dto.AccountDto;
import cafe.entity.Account;
import cafe.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AccountDto getProfile() {
        // Lấy tên đăng nhập từ người dùng đang đăng nhập
        String username = getLoggedInUsername();
        
        Account account = accountRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        // Map Account to AccountDto
        AccountDto accountDto = new AccountDto();
        accountDto.setFullName(account.getFullName());
        accountDto.setEmail(account.getEmail());
        accountDto.setPhone(account.getPhone());
        return accountDto;
    }

    public void updateProfile(AccountDto accountDto, String username) {
        Account account = accountRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        // Kiểm tra email có trùng hay không
        if (!account.getEmail().equals(accountDto.getEmail()) && 
            accountRepository.findByEmail(accountDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại.");
        }
        
        account.setFullName(accountDto.getFullName());
        account.setPhone(accountDto.getPhone());
        account.setEmail(accountDto.getEmail()); // Đảm bảo dòng này có mặt
        
     // Lưu tên tệp hình ảnh cũ
        String oldImage = account.getImage(); // Lấy tên hình ảnh cũ
        
        if (accountDto.getImageFile() != null && !accountDto.getImageFile().isEmpty()) {
            String filename = fileStorageService.storeLogoFile(accountDto.getImageFile());
            account.setImage(filename); // Lưu tên tệp vào thực thể tài khoản
            System.out.println("Cập nhật hình ảnh tài khoản với: " + filename); // Ghi nhật ký

            // Xóa hình ảnh cũ nếu tồn tại
            if (oldImage != null) {
                fileStorageService.deleteFile(oldImage); // Xóa tệp cũ
                System.out.println("Đã xóa hình ảnh cũ: " + oldImage);
            }
        }

        // Cập nhật mật khẩu nếu có
        if (accountDto.getPassword() != null && !accountDto.getPassword().isEmpty()) {
            account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        }
        // Kiểm tra xem tệp hình ảnh có tồn tại không
        if (accountDto.getImageFile() != null && !accountDto.getImageFile().isEmpty()) {
            System.out.println("Tệp hình ảnh đã nhận: " + accountDto.getImageFile().getOriginalFilename());
        } else {
            System.out.println("Không có tệp hình ảnh nào được gửi.");
        }

        accountRepository.save(account);
    }

    private String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return authentication.getPrincipal().toString();
    }
}
