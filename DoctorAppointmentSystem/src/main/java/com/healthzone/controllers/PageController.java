package com.healthzone.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.healthzone.dao.AppointmentRepository;
import com.healthzone.dao.DoctorRepository;
import com.healthzone.dao.TimeSlotRepository;
import com.healthzone.dao.UserRepository;
import com.healthzone.entities.Appointment;
import com.healthzone.entities.Doctor;
import com.healthzone.entities.TimeSlot;
import com.healthzone.entities.User;
import com.healthzone.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	 @Autowired
	    private AppointmentRepository appointmentRepository; // Replace with your repository

	 @Autowired
	 private TimeSlotRepository timeSlotRepository;
	 
	 @Autowired
	 private DoctorRepository doctorRepository;
	
	
	
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
	
	@GetMapping("/DrRegister")
	public String drRegister(@ModelAttribute("user") User user,Model model) {
		Doctor doctor = new Doctor();
		model.addAttribute("user", user);
		
		return "/DrRegister";
	}
	
	
	
	
	@PostMapping("/doRegister")
	
	public String registerfForm(@ModelAttribute User user,@RequestParam(value ="check", defaultValue = "false") boolean checked, Model model,HttpSession session) {
		
		if(user.getRole().equals("ROLE_ADMIN")) {
			System.out.println(user);
			model.addAttribute("user",user);
			return "/drRegister";
		}
		
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
	
	
@PostMapping("/doDrRegister")
	
	public String doDrRegisterfForm(@ModelAttribute User user, @RequestParam("specialty") String specialty,@RequestParam(value ="check", defaultValue = "false") boolean checked, Model model,HttpSession session) {
		
		System.out.println(user);
		System.out.println(specialty);
		
		if(checked) {
			
			user.setActive(true);
			Doctor doctor = new Doctor();
			doctor.setName(user.getName());
			doctor.setSpecialty(specialty);
			doctor.setAbout(user.getAbout());
			user.setPassword(encoder.encode(user.getPassword()));
			userRepository.save(user);
			doctorRepository.save(doctor);
			
			session.setAttribute("message", new Message("Account Created successfully...!!","alert-success" ));
			
		}
		else {
			System.out.println("inside else block");
			session.setAttribute("message", new Message("Please check the checkbox first..!!","alert-danger" ));
		}
		
		return "redirect:/register";
	}
	
	
    
}
 



