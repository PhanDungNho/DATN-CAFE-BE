package cafe.controller;

import java.util.Set;
import java.util.stream.Collectors;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cafe.dto.AuthRequestDTO;
import cafe.dto.JwtResponseDTO;
import cafe.service.util.JwtService;
 

@RestController
@CrossOrigin
public class ApiController {
	@Autowired
	JwtService jwtService;
	@Autowired
	AuthenticationManager authenticationManager;
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


	
	
//	@GetMapping("/api/v1/ping")
//	public String test() {
//	    try {
//	        return "Welcome";
//	    } catch (Exception e){
//	        throw new RuntimeException(e);
//	    }
//	} 
//	@GetMapping("/api/v1/order")
//	public String order() {
//	    try {
//	        return "order";
//	    } catch (Exception e){
//	        throw new RuntimeException(e);
//	    }
//	} 
//	@GetMapping("/api/v1/admin/account")
//	public String account() {
//	    try {
//	        return "account";
//	    } catch (Exception e){
//	        throw new RuntimeException(e);
//	    }
//	} 
}
