package cafe.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import cafe.entity.Account;

@Component
public class TemporaryAccountStorage {
    private Map<String, AccountWithTimestamp> storage = new HashMap<>();
    private static final long OTP_VALIDITY_DURATION_MINUTES = 1; // Duration for OTP validity

    // Inner class to store account with timestamp
    private static class AccountWithTimestamp {
        private final Account account;
        private final LocalDateTime timestamp;

        public AccountWithTimestamp(Account account, LocalDateTime timestamp) {
            this.account = account;
            this.timestamp = timestamp;
        }

        public Account getAccount() {
            return account;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }

    public void addAccount(Account account) {
        storage.put(account.getOtp(), new AccountWithTimestamp(account, LocalDateTime.now()));
    }

    public Account getAccountByOtp(String otp) {
        AccountWithTimestamp accountWithTimestamp = storage.get(otp);
        if (accountWithTimestamp == null || isOtpExpired(accountWithTimestamp)) {
            removeAccountByOtp(otp); // Remove expired OTP
            return null; // OTP is either not found or expired
        }
        return accountWithTimestamp.getAccount();
    }

    public void removeAccountByOtp(String otp) {
        storage.remove(otp);
    }

    private boolean isOtpExpired(AccountWithTimestamp accountWithTimestamp) {
        LocalDateTime expirationTime = accountWithTimestamp.getTimestamp()
            .plusMinutes(OTP_VALIDITY_DURATION_MINUTES);
        return LocalDateTime.now().isAfter(expirationTime);
    }
}
