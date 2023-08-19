package com.parking.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		super.onAuthenticationSuccess(request, response, authentication);
		
		CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
		System.out.println(details);
		
		String rediirectURL = request.getContextPath();
		System.out.println(rediirectURL);
		
		if(details.hasRole("ROLE_ADMIN")) {
			rediirectURL+="/admin/AdminDashboard";
			System.out.println(rediirectURL);
		}
		else if(details.hasRole("ROLE_USER")) {
			rediirectURL+="/user/UserDashboard";
			System.out.println(rediirectURL);
		}
		
		response.sendRedirect(rediirectURL);
	}
	
	

}
