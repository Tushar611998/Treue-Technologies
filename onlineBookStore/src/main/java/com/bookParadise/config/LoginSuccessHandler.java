package com.bookParadise.config;

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
		
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		String redirectUrl = request.getContextPath();
		
		if(userDetails.hasRole("ROLE_ADMIN")) {
			
			redirectUrl+="/admin/dashboard";
			System.out.println(redirectUrl);
		}else if (userDetails.hasRole("ROLE_USER")) {
			redirectUrl+="/user/dashboard";
			System.out.println(redirectUrl);
		}
		response.sendRedirect(redirectUrl);
//		super.onAuthenticationSuccess(request, response, authentication);
	}

	


	
	

}
