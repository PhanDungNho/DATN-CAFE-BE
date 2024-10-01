package cafe.service.util;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import cafe.entity.Account;
import cafe.repository.AccountRepository;
 

@Component
public class UserService implements UserDetailsService{
	
	@Autowired
	AccountRepository accountRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Account account = accountRepository.findByUsername(username).get();
		String password = account.getPassword();
		Set<GrantedAuthority> authorities = account.getAuthorities().stream()
	                .map((au) -> new SimpleGrantedAuthority("ROLE_" +au.getRole().getRolename()))
	                .collect(Collectors.toSet());

		return new User(username, password, authorities);
		
	}

}
