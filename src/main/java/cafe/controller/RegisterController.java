package cafe.controller;

import java.util.Map;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import cafe.entity.Account;
import cafe.repository.AccountRepository;
import cafe.service.EmailService;
import cafe.service.TemporaryAccountStorage;
import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RegisterController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TemporaryAccountStorage temporaryAccountStorage;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account) {
        // Check if email or phone already exists
        if (accountRepository.findByEmail(account.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Email đã tồn tại"));
        }
        if (accountRepository.findByPhone(account.getPhone()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Số điện thoại đã tồn tại"));
        }

        // Encrypt password before saving
        String encodedPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(encodedPassword); // Set encrypted password

        // Generate OTP
        String otp = generateOTP();
        account.setOtp(otp);
        account.setEnabled(false); // Account not activated

        // Temporarily store account and send email
        temporaryAccountStorage.addAccount(account);
        try {
        	String subject = "Xác nhận đăng ký";

        	String htmlContent = "a";

            emailService.sendEmail(account.getEmail().toString(), subject, htmlContent);
            return ResponseEntity.ok(Map.of("message", "Đăng ký thành công! Vui lòng kiểm tra email để xác nhận."));
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Lỗi khi gửi email."));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestParam("otp") String otp) {
        Account account = temporaryAccountStorage.getAccountByOtp(otp);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Mã OTP không hợp lệ!"));
        }
        account.setEnabled(true);
        account.setOtp(null); // Clear OTP after verification
        accountRepository.save(account);
        temporaryAccountStorage.removeAccountByOtp(otp);
        return ResponseEntity.ok(Map.of("message", "Xác nhận đăng ký thành công!"));
    }

    private String generateOTP() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000)); // 6-digit OTP
    }
}
