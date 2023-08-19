package com.healthzone.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.healthzone.dao.UserRepository;
import com.healthzone.entities.User;

public class CustomUserService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = userRepository.getUserByEmail(username);
		
		if(user==null) {
			throw new UsernameNotFoundException("Invalid Credentials..!!");
		}
			CustomUserDetails details = new CustomUserDetails(user);
			return details;
		
	}

}
