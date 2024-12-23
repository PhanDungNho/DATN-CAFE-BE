package cafe.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import cafe.entity.Account;
import org.springframework.stereotype.Service; // Import này

@Service // Thêm annotation này để định nghĩa lớp như một bean của Spring
public class TemporaryAccountStorageqmk {

    // Lưu trữ thông tin tài khoản tạm thời với mã OTP và thời gian tạo
    private final Map<String, AccountWithOtpTimestamp> storage = new HashMap<>();

    // Thời gian mà mã OTP có hiệu lực (ví dụ: 5 phút)
    private static final long OTP_VALIDITY_DURATION_MINUTES = 1;

    // Thêm tài khoản vào bộ nhớ tạm
    public void addAccount(Account account) {
        // Lưu tài khoản cùng với thời gian tạo OTP vào bộ nhớ tạm
        storage.put(account.getEmail(), new AccountWithOtpTimestamp(account, LocalDateTime.now()));
    }

    // Lấy tài khoản dựa vào email
    public AccountWithOtpTimestamp getAccountByEmail(String email) {
        // Trả về thông tin tài khoản dựa vào email
        return storage.get(email);
    }

    // Xóa tài khoản sau khi OTP được xác thực hoặc hết hạn
    public void removeAccount(String email) {
        // Xóa tài khoản khỏi bộ nhớ tạm theo email
        storage.remove(email);
    }

    // Kiểm tra xem mã OTP vẫn còn hiệu lực hay không
    public boolean isOtpValid(String email) {
        // Lấy thông tin tài khoản từ bộ nhớ tạm
        AccountWithOtpTimestamp accountWithOtp = storage.get(email);
        if (accountWithOtp == null) {
            return false; // Tài khoản không tồn tại
        }
        // Kiểm tra xem mã OTP đã hết hạn chưa
        LocalDateTime createdTime = accountWithOtp.getOtpCreatedTime();
        return LocalDateTime.now().isBefore(createdTime.plus(OTP_VALIDITY_DURATION_MINUTES, ChronoUnit.MINUTES));
    }

    // Lớp nội bộ để lưu trữ thông tin tài khoản với mã OTP và thời gian tạo
    public static class AccountWithOtpTimestamp {
        private final Account account; // Thông tin tài khoản
        private final LocalDateTime otpCreatedTime; // Thời gian tạo OTP

        public AccountWithOtpTimestamp(Account account, LocalDateTime otpCreatedTime) {
            this.account = account; // Khởi tạo thông tin tài khoản
            this.otpCreatedTime = otpCreatedTime; // Khởi tạo thời gian tạo OTP
        }

        public Account getAccount() {
            return account; // Trả về thông tin tài khoản
        }

        public LocalDateTime getOtpCreatedTime() {
            return otpCreatedTime; // Trả về thời gian tạo OTP
        }
    }
}
