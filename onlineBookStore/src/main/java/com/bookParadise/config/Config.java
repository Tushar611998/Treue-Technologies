package com.bookParadise.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity

public class Config {
	
	@Autowired
	private LoginSuccessHandler handler;
	
	@Bean
	public CustomUserService userService() {
		CustomUserService service = new CustomUserService();
		System.out.println("inside userservice");
		return service;
	}
	
	@Bean
	public BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		System.out.println("in the daoauthenticationprovider");
		provider.setUserDetailsService(userService());
		provider.setPasswordEncoder(getPasswordEncoder());
		return provider;
		
	}
	
	
	@Bean
	
	public SecurityFilterChain app2securityFilterChain(HttpSecurity security) throws Exception {
		System.out.println("in securityFiterChain");
		security.csrf(c->
				c.disable()).authorizeHttpRequests(a->
				a.requestMatchers("/admin/**").hasRole("ADMIN")
				.requestMatchers("/user/**").hasRole("USER")
				.requestMatchers("/**").permitAll()
		)
				.formLogin(f->
				f.loginPage("/adlogin")
				.successHandler(handler)
				
		);
		return security.build();
	}
	
//	@Bean
//	public SecurityFilterChain app1securityFilterChain(HttpSecurity security) throws Exception {
//		
//		security.csrf(c->
//				c.disable()).authorizeHttpRequests(a->
//				a.requestMatchers("/user/**").hasRole("USER")
//				.requestMatchers("/**").permitAll()
//		)
//				.formLogin(f->
//				f.loginPage("/login")
//				.loginProcessingUrl("/dologin")
//				.defaultSuccessUrl("/user/UserDashboard")
//		);
//		return security.build();
//	}
	
	
	
	
}
