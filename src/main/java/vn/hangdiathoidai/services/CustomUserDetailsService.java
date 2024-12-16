package vn.hangdiathoidai.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import vn.hangdiathoidai.entity.User;

public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.findByUsername(username);

	    if (user != null) {
	    	return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
		            .password(user.getPassword())
		            .roles(user.getRole().getRoleName().toString())
		            .build();
	    }
	    return null;
	}

}
