package cafe.controller;

import cafe.dto.AccountDto;
import cafe.service.AccountService;

import cafe.service.ProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "http://localhost:3000")
public class ProfileController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ProfileService ProfileService;
    @GetMapping
    public ResponseEntity<AccountDto> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername(); // Lấy tên đăng nhập từ đối tượng UserDetails
        AccountDto accountDto = accountService.getAccountByUsername(username); // Sử dụng phương thức tìm theo tên đăng nhập
        return ResponseEntity.ok(accountDto);
    }
    @PutMapping("/update")
    public ResponseEntity<String> updateProfile(
            @ModelAttribute AccountDto accountDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername(); // Lấy tên đăng nhập từ đối tượng UserDetails
        ProfileService.updateProfile(accountDto, username); // Gọi phương thức cập nhật thông tin
        
        return ResponseEntity.ok("Cập nhật thông tin thành công.");
    }
}
