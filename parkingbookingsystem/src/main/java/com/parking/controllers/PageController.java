package com.parking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.parking.dao.UserRepository;
import com.parking.entities.User;
import com.parking.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("/about")
	public String about() {
		return "about";
	}

	@GetMapping("/contact-us")
	public String contactUs() {
		return "contactUs";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	@GetMapping("/adlogin")
	public String adlogin() {
		return "adlogin";
	}

	@GetMapping("/register")
	public String register(Model model) {
		User user = new User();
		model.addAttribute(user);
		
		return "register";
	}
	
	
	
	
	
	@PostMapping("/doRegister")
	
	public String registerfForm(@ModelAttribute User user,@RequestParam(value ="check", defaultValue = "false") boolean checked, Model model,HttpSession session) {
		
		if(checked) {
			
			User user1 = new User();
			user1.setName(user.getName());
			user1.setEmail(user.getEmail());
			user1.setActive(true);
			user1.setPassword(encoder.encode(user.getPassword()));
			System.out.println(user.getRole());
			user1.setRole(user.getRole());
			user1.setAbout(user.getAbout());
			
			userRepository.save(user1);
			
			session.setAttribute("message", new Message("Account Created successfully...!!","alert-success" ));
			
		}
		else {
			System.out.println("inside else block");
			session.setAttribute("message", new Message("Please check the checkbox first..!!","alert-danger" ));
		}
		
		return "redirect:/register";
	}

	



}
