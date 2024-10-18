package cafe.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import cafe.entity.Account;

@Component
public class TemporaryAccountStorage {
    private Map<String, Account> storage = new HashMap<>(); // Sửa cú pháp ở đây

    public void addAccount(Account account) {
        storage.put(account.getOtp(), account);
    }

    public Account getAccountByOtp(String otp) {
        return storage.get(otp);
    }

    public void removeAccountByOtp(String otp) {
        storage.remove(otp);
    }
}
