package com.healthzone.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/admin")
public class AdminController {
	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private DoctorRepository doctorRepository;
	
	@Autowired
    private AppointmentRepository appointmentRepository;
	
	@Autowired
	private TimeSlotRepository timeSlotRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@ModelAttribute
	public void common(Principal principal, Model model) {
		
		String name = principal.getName();
		
		User user = userRepository.getUserByEmail(name);
		
		List<Doctor> doctors = doctorRepository.findAll();
		
		model.addAttribute("doctors", doctors);
		model.addAttribute("user",user);
		
	}
	 @GetMapping("/bookings")
	    public String showUserBookings(Principal principal,Model model) {
	        // Retrieve user-specific bookings, e.g., based on user ID or other criteria
	    	String name = principal.getName();
	    	User user = userRepository.getUserByEmail(name);
	    	String name2 = user.getName();
	    	Doctor doctor = doctorRepository.findByName(name2);
	    	LocalDate date = LocalDate.now();
	        List<Appointment> userBookings = appointmentRepository.findByDoctorSpecialtyAndDateGreaterThanEqualOrderByDateAsc(doctor.getSpecialty(),date );
	        
	        model.addAttribute("userBookings", userBookings);
	        return "/admin/user-bookings";
	    }
	 @GetMapping("/cancel-booking/{bookingId}")
	    public String cancelBooking(@PathVariable int bookingId) {
	        // Fetch the appointment by its ID
	        Appointment appointment = appointmentRepository.findById(bookingId);
	        
	        if (appointment != null) {
	            // Delete the appointment or update its status to canceled based on your requirement
	        	TimeSlot timeSlot = timeSlotRepository.findById(appointment.getTimeSlot().getId());
	            
	        	appointment.setCanceled(true);
	        	timeSlotRepository.delete(timeSlot);
	            
	        }
	        
	        // Redirect to the user's booking page or any other appropriate page
	        return "redirect:/admin/bookings";
	    }
	    
	    @GetMapping("/change-password")
		public String changePassword() {
			return "/admin/change-password";
		}
		@PostMapping("/changePassword")
		public String newPassword(@RequestParam("old") String old, @RequestParam("new") String newPass,Principal principal,HttpSession session) {
			System.out.println(old);
			System.out.println(newPass);
			String name = principal.getName();
			User user = userRepository.getUserByEmail(name);
			System.out.println(user);
			
			if(bCryptPasswordEncoder.matches(old, user.getPassword())) {
				bCryptPasswordEncoder.encode(newPass);
				System.out.println(newPass);
				user.setPassword(this.bCryptPasswordEncoder.encode(newPass));
				userRepository.save(user);
				session.setAttribute("message", new Message("Password has been changed successfully..!!", "alert-success"));
			}else {
				
				session.setAttribute("message", new Message("Please enter right old password..!!", "alert-danger"));
				return "redirect:/admin/change-password";
			}
			
			
			return "redirect:/admin/bookings";
		}

}
