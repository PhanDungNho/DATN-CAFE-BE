package cafe.controller;

import cafe.dto.AccountDto;
import cafe.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public ResponseEntity<AccountDto> getProfile() {
        try {
            AccountDto accountDto = profileService.getProfile();
            return ResponseEntity.ok(accountDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateProfile(@ModelAttribute AccountDto accountDto) {
        try {
            profileService.updateProfile(accountDto);
            return ResponseEntity.ok("Cập nhật thông tin thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra: " + e.getMessage());
        }
    }

}
