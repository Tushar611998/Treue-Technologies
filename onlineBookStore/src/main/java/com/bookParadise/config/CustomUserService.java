package com.bookParadise.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;



public class CustomUserService implements UserDetailsService{
	
	@Autowired
	private com.bookParadise.dao.UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		com.bookParadise.entities.User user = userRepository.getUserByEmail(username);
		
		if(user==null) {
			throw new UsernameNotFoundException("Invalid Credentials..!!");
		}
			CustomUserDetails details = new CustomUserDetails(user);
			return details;
		
	}

}
