package cafe.controller;

import java.util.Map;
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

import cafe.dto.AuthRequestDTO;
import cafe.dto.JwtResponseDTO;
import cafe.entity.Account;
import cafe.service.AccountService;
import cafe.service.util.JwtService;


@Configuration
@RestController
@CrossOrigin
public class ApiController {
	@Autowired
	JwtService jwtService;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	AccountService accountService;
 
    @Autowired
    private JwtDecoder jwtDecoder;
 
    
	
	@PostMapping("/api/v1/login")
	public JwtResponseDTO AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO) {
	    Authentication authentication = authenticationManager.authenticate(
	        new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword())
	    );

	    if (authentication.isAuthenticated()) {
	        // Lấy thông tin từ UserDetails
	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();  // Sử dụng UserDetails
	        Set<String> roles = userDetails.getAuthorities().stream()
	                .map(GrantedAuthority::getAuthority)
	                .collect(Collectors.toSet());

	        // Tạo token và trả về cả token, username và roles
	        String token = jwtService.GenerateToken(authRequestDTO.getUsername());

	        return JwtResponseDTO.builder()
	                .accessToken(token)
	                .username(authRequestDTO.getUsername())
	                .roles(roles)  // Thêm danh sách roles vào response
	                .build();
	    } else {
	        throw new UsernameNotFoundException("Invalid user request..!!");
	    }
	}

	  @PostMapping("/api/v1/auth/google")
	    public JwtResponseDTO authenticateWithGoogle(@RequestBody Map<String, String> token) {
	        try {
	        	   for (Map.Entry<String, String> entry : token.entrySet()) {
	                   System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
	               }
	        	   System.out.println(token.get("credential"));
	        	   System.out.println("dsds");
	            // Giải mã token từ Google
	        	   Jwt jwt = jwtDecoder.decode(token.get("credential"));
	               String email = jwt.getClaimAsString("email");
	            // Kiểm tra xem người dùng có tồn tại trong hệ thống không
	               System.out.println(email);
	            UserDetails userDetails = toDetails(email); // Tạo một phương thức để lấy thông tin người dùng
	            // Lấy roles từ UserDetails
	            Set<String> roles = userDetails.getAuthorities().stream()
	                .map(GrantedAuthority::getAuthority)
	                .collect(Collectors.toSet());
	            // Tạo token JWT cho người dùng
	            String jwtToken = jwtService.GenerateToken(userDetails.getUsername());
	            return JwtResponseDTO.builder()
	                .accessToken(jwtToken)
	                .username(userDetails.getUsername())
	                .roles(roles)
	                .build();

	        } catch (Exception e) {
	            throw new RuntimeException("Google authentication failed: " + e.getMessage());
	        }
	    }
	
	   private UserDetails toDetails(String email) {
	        Account account = accountService.findByEmail(email);
	        String username= account.getUsername();
	    	String password = account.getPassword();
	    	Set<GrantedAuthority> authorities = account.getAuthorities().stream()
	                .map((au) -> new SimpleGrantedAuthority("ROLE_" +au.getRole().getRolename()))
	                .collect(Collectors.toSet());
	    	return new User(username, password, authorities);
	    }
	  
}
