package cafe.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
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
import cafe.service.TemporaryAccountStorageqmk;
import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ForgotPasswordController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TemporaryAccountStorageqmk temporaryAccountStorageqmk;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // API gửi OTP cho người dùng
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Account account = accountRepository.findByEmail(email).orElse(null);
        
        // Kiểm tra xem email có tồn tại không
        if (account == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Email không tồn tại!"));
        }

        // Tạo OTP và lưu tạm thời
        String otp = generateOTP();
        account.setOtp(otp);
        account.setEnabled(false);

        // Lưu tài khoản tạm thời và gửi OTP qua email
        temporaryAccountStorageqmk.addAccount(account);
        try {
            String subject = "Yêu cầu đặt lại mật khẩu";
        	String htmlContent = "<div style='font-family:Arial,sans-serif; font-size:14px; color:#333; background-color:#f4f7fa; padding: 20px; border-radius: 8px; border: 1px solid #e0e0e0;'>"
	                   + "<div style='text-align:center; margin-bottom:20px;'>"+
"<img src='https://img.upanh.tv/2024/10/18/logo2.png' width='200' height='76'>"+
	                    "</div>"
	                   + "<h2 style='color:#2e6c80; text-align:center;'>Xác Nhận Đăng Ký</h2>"
	                   + "<p>Chào bạn,</p>"
	                   + "<p>Cảm ơn bạn đã đăng ký tài khoản với WalaCafé!</p>"
	                   + "<p>Mã OTP của bạn là:</p>"
	                   + "<h3 style='color:#2e6c80; text-align:center; font-weight:bold; font-size:24px; border: 2px dashed #2e6c80; padding: 10px; border-radius: 5px; display:inline-block; margin: 20px auto;'>" + otp + "</h3>"
	                   + "<p style='text-align:center;'>Vui lòng nhập mã OTP để hoàn tất quá trình đăng ký.</p>"
	                   + "<p>Chúc bạn một ngày tốt lành!</p>"
	                   + "<p style='margin-top:40px;'>Trân trọng,<br/>Đội ngũ hỗ trợ WalaCafé</p>"
	                   + "<div style='text-align:center; margin-top:40px;'>"
	                   + "<a href='https://www.walacafe.com' style='color:#2e6c80; text-decoration:none; font-weight:bold;'>Truy cập WalaCaFé</a>"
	                   + "</div>"
	                   + "</div>";
            emailService.sendEmail(account.getEmail(), subject, htmlContent);
            return ResponseEntity.ok(Map.of("message", "Mã OTP đã được gửi đến email của bạn."));
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Lỗi khi gửi email."));
        }
    }

    // Hàm tạo mã OTP (6 chữ số ngẫu nhiên)
    private String generateOTP() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
    
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String inputOtp = request.get("otp");

        // Retrieve the account from temporary storage
        TemporaryAccountStorageqmk.AccountWithOtpTimestamp accountWithOtpTimestamp = temporaryAccountStorageqmk.getAccountByEmail(email);

        if (accountWithOtpTimestamp == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Tài khoản không tồn tại!"));
        }

        // Check if the OTP is valid
        if (!temporaryAccountStorageqmk.isOtpValid(email)) {
            temporaryAccountStorageqmk.removeAccount(email); // Clean up expired account
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "OTP đã hết hạn!"));
        }

        Account account = accountWithOtpTimestamp.getAccount();

        // Verify OTP
        if (!account.getOtp().equals(inputOtp)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "OTP không chính xác!"));
        }

        // OTP is valid, proceed to password reset
        temporaryAccountStorageqmk.removeAccount(email); // Clean up after verification
        return ResponseEntity.ok(Map.of("message", "OTP hợp lệ!"));
    }





 // API đặt lại mật khẩu
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");
        String confirmPassword = request.get("confirmPassword");

        // Kiểm tra xem mật khẩu mới và xác nhận mật khẩu có khớp không
        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Mật khẩu không khớp!"));
        }

        // Tìm tài khoản dựa trên email
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        if (optionalAccount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Tài khoản không tồn tại!"));
        }

        Account account = optionalAccount.get();

        // Mã hóa mật khẩu mới
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account); // Lưu tài khoản với mật khẩu mới

        return ResponseEntity.ok(Map.of("message", "Đặt lại mật khẩu thành công!"));
    }
}



