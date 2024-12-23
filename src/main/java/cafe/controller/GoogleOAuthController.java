package cafe.controller;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.util.Value;

import cafe.dto.AccountDto;
import cafe.dto.AuthRequestDTO;
import cafe.dto.JwtResponseDTO;
import cafe.entity.Account;
import cafe.exception.EntityException;
import cafe.repository.AccountRepository;
import cafe.service.AccountService;
import cafe.service.FileStorageService;
import cafe.service.util.JwtService;

@Configuration
@RestController
@CrossOrigin
public class GoogleOAuthController {
	@Autowired
	JwtService jwtService;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	AccountService accountService;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	private JwtDecoder jwtDecoder;
	@Autowired
	FileStorageService fileStorageService;

	@PostMapping("/api/v1/login")
	public JwtResponseDTO AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO) {
	    Authentication authentication = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));

	    if (authentication.isAuthenticated()) {
	        // Lấy thông tin từ UserDetails
	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

	        // Kiểm tra trạng thái active
	        Account account = accountRepository.findByUsername(authRequestDTO.getUsername())
	                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
	        
	        if (!account.getActive()) {
	            throw new RuntimeException("Account is deactivated. Please contact support.");
	        }

	        Set<String> roles = userDetails.getAuthorities().stream()
	                .map(GrantedAuthority::getAuthority)
	                .collect(Collectors.toSet());

	        // Tạo token và trả về response
	        String token = jwtService.GenerateToken(authRequestDTO.getUsername());

	        return JwtResponseDTO.builder()
	                .accessToken(token)
	                .username(authRequestDTO.getUsername())
	                .roles(roles).image(account.getImage())
	                .build();
	    } else {
	        throw new UsernameNotFoundException("Invalid user request..!!");
	    }
	}


	@PostMapping("/api/v1/auth/google")
	public JwtResponseDTO authenticateWithGoogle(@RequestBody Map<String, String> token) {
	    try {
	        // Giải mã token từ Google
	        Jwt jwt = jwtDecoder.decode(token.get("credential"));
	        String email = jwt.getClaimAsString("email");
	        String picture = jwt.getClaimAsString("picture");
	        String name = jwt.getClaimAsString("name");

	        Optional<Account> accountOptional = accountRepository.findByEmail(email);
	        Account account;

	        if (accountOptional.isEmpty()) {
	            String[] parts = email.split("@");
	            AccountDto newAccountDto = new AccountDto();
	            newAccountDto.setUsername(parts[0]);
	            newAccountDto.setFullName(name);
	            newAccountDto.setEmail(email);
	            newAccountDto.setPhone("+84");
	            newAccountDto.setAmountPaid(0D);
	            String image = fileStorageService.storeLogoFileFromUrl(picture);
	            newAccountDto.setImage(image);
	            account = accountService.insertAccountWithGoogle(newAccountDto);
	        } else {
	            account = accountOptional.get();

	            // Kiểm tra trạng thái active
	            if (!account.getActive()) {
	                throw new RuntimeException("Account is deactivated. Please contact support.");
	            }
	        }

	        Set<String> authorities = account.getAuthorities() != null 
	                ? account.getAuthorities().stream()
	                    .map((au) -> "ROLE_" + au.getRole().getRoleName()).collect(Collectors.toSet())
	                : Collections.emptySet();

	        String jwtToken = jwtService.GenerateToken(account.getUsername());

	        return JwtResponseDTO.builder()
	                .accessToken(jwtToken)
	                .username(account.getUsername())
	                .image(account.getImage())
	                .roles(authorities)
	                .build();

	    } catch (Exception e) {
	        throw new RuntimeException("Google authentication failed: " + e.getMessage());
	    }
	}


	private Account toDetails(String email) {
		Account account = accountService.findByEmail(email);
//	        String username= account.getUsername();
//	        String image= account.getImage();
//	    	String password = account.getPassword();
//	    	Set<GrantedAuthority> authorities = account.getAuthorities().stream()
//	                .map((au) -> new SimpleGrantedAuthority("ROLE_" +au.getRole().getRolename()))
//	                .collect(Collectors.toSet());
		return account;
	}

}
