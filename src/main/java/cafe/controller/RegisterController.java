package cafe.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import cafe.entity.Account;
import cafe.entity.Authority;
import cafe.entity.Role;
import cafe.repository.AccountRepository;
import cafe.service.EmailService;
import cafe.service.RoleService;
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
	private RoleService roleService;

	@Autowired
	private PasswordEncoder passwordEncoder; // Inject PasswordEncoder

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody Account account) {

		// Check if the username already exists
		if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Username already exists"));
		}

		// Check if the email or phone number already exists
		if (accountRepository.findByEmail(account.getEmail()).isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Email already exists"));
		}
		if (accountRepository.findByPhone(account.getPhone()).isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Phone number already exists"));
		}

		// Encrypt password before saving
		String encodedPassword = passwordEncoder.encode(account.getPassword());
		account.setPassword(encodedPassword); // Set encrypted password

		// Generate OTP
		String otp = generateOTP();
		account.setOtp(otp);
		account.setEnabled(false); // Account not activated
		account.setActive(false);
		account.setAmountPaid(0D);

		// Temporarily store account and send email
		temporaryAccountStorage.addAccount(account);
		try {
			String subject = "Registration Confirmation";

			String htmlContent = "<div style='font-family:Arial,sans-serif; font-size:14px; color:#333; background-color:#f4f7fa; padding: 20px; border-radius: 8px; border: 1px solid #e0e0e0;'>"
					+ "<div style='text-align:center; margin-bottom:20px;'>"
					+ "<img src='https://img.upanh.tv/2024/10/18/logo2.png' width='200' height='76'>" + "</div>"
					+ "<h2 style='color:#2e6c80; text-align:center;'>Registration Confirmation</h2>" + "<p>Hello,</p>"
					+ "<p>Thank you for registering with WalaCafé!</p>" + "<p>Your OTP code is:</p>"
					+ "<h3 style='color:#2e6c80; text-align:center; font-weight:bold; font-size:24px; border: 2px dashed #2e6c80; padding: 10px; border-radius: 5px; display:inline-block; margin: 20px auto;'>"
					+ otp + "</h3>"
					+ "<p style='text-align:center;'>Please enter this OTP code to complete your registration.</p>"
					+ "<p>Have a great day!</p>"
					+ "<p style='margin-top:40px;'>Best regards,<br/>WalaCafé Support Team</p>"
					+ "<div style='text-align:center; margin-top:40px;'>"
					+ "<a href='https://www.walacafe.io.vn' style='color:#2e6c80; text-decoration:none; font-weight:bold;'>Visit WalaCafé</a>"
					+ "</div>" + "</div>";

			emailService.sendEmail(account.getEmail().toString(), subject, htmlContent);
			return ResponseEntity.ok(Map.of("message", "Registration successful! Please check your email to confirm."));
		} catch (MessagingException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("message", "Error while sending email."));
		}
	}

	@GetMapping("/verify")
	public ResponseEntity<?> verifyOtp(@RequestParam("otp") String otp) {
		Account account = temporaryAccountStorage.getAccountByOtp(otp);
		if (account == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("message", "Invalid or expired OTP code!"));
		}

		// Proceed with enabling the account
		account.setEnabled(true);
		account.setActive(true);
		account.setOtp(null); // Clear OTP after verification

		Authority auth = new Authority();
		Role adminRole = roleService.findById(3L);
		auth.setAccount(account);
		auth.setRole(adminRole);

		// Create an Authority list and add auth to the list
		List<Authority> authorities = new ArrayList();
		authorities.add(auth);

		// Set the authorities list for the account
		account.setAuthorities(authorities);

		accountRepository.save(account);

		temporaryAccountStorage.removeAccountByOtp(otp);
		return ResponseEntity.ok(Map.of("message", "Registration confirmation successful!"));
	}

	private String generateOTP() {
		Random random = new Random();
		return String.format("%06d", random.nextInt(1000000)); // 6-digit OTP
	}
}