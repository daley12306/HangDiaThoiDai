package vn.hangdiathoidai.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import vn.hangdiathoidai.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(authorize -> authorize
                       .requestMatchers("/**", "/home","/admin", "/css/**", "/js/**", "/images/**", "/assets/**").permitAll()
                       .anyRequest().authenticated()
                       
				)
				.formLogin(login -> login
						.loginPage("/login")
						.defaultSuccessUrl("/"))
				.logout(logout -> logout
						.logoutSuccessUrl("/"))
				.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
	    return new CustomUserDetailsService();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(userDetailsService);
	    authProvider.setPasswordEncoder(passwordEncoder);
	    return authProvider;
	}
	
}
