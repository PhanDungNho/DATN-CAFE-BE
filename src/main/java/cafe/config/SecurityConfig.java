package cafe.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.google.api.client.util.Value;

import cafe.service.util.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	JwtAuthFilter jwtAuthFilter;

	@Bean
	// authentication
	public UserDetailsService userDetailsService(PasswordEncoder encoder) {
		return new UserService();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	 @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.cors().and().csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                		.requestMatchers("/api/v1/login", "/api/v1/auth/google", "/api/v1/account/image/**").permitAll()
                		.requestMatchers( "/api/v1/products/images/**").permitAll()
                		.requestMatchers("/api/v1/cart/**","/api/v1/categories/**","/api/v1/sizes/**","/api/v1/products/**","/api/v1/orders/**","/api/v1/toppings/**","/api/v1/account/**", "/api/v1/transactions/**").hasAnyRole("ADMIN","STAFF")

                		.requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/home").permitAll()  
                        .requestMatchers("/cart/**","/order/**").hasAnyRole("ADMIN","STAFF")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form
            			.loginPage("/login") 
            			.permitAll()
            		)
                .logout((logout) -> logout
                        .logoutSuccessUrl("/home")
                        .permitAll()
                    )
                
                .httpBasic(Customizer.withDefaults())
                .build();
    }
	 @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService(passwordEncoder()));
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
	 @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
