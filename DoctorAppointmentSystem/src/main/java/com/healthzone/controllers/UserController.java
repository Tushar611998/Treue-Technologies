package com.healthzone.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.healthzone.helper.Message;
import com.healthzone.dao.AppointmentRepository;
import com.healthzone.dao.DoctorRepository;
import com.healthzone.dao.TimeSlotRepository;
import com.healthzone.dao.UserRepository;
import com.healthzone.entities.Appointment;
import com.healthzone.entities.Doctor;
import com.healthzone.entities.TimeSlot;
import com.healthzone.entities.User;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	
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
	
	
	@GetMapping("/doctor")
	public String bookApp() {
		return "/normal/doctor";
	}
	
	@GetMapping("/calendar/{id}")
    public String showCalendar(@PathVariable("id") int id,Model model) {
        // Get the current year and month
        YearMonth currentMonth = YearMonth.now();
        LocalDate currentDate = LocalDate.now();

        // Generate a list of all days in the current month
        List<LocalDate> allDaysInMonth = new ArrayList<>();
        LocalDate firstDayOfMonth = currentMonth.atDay(1);
        LocalDate lastDayOfMonth = currentMonth.atEndOfMonth();
        LocalDate currentDay = firstDayOfMonth;

        while (!currentDay.isAfter(lastDayOfMonth)) {
            allDaysInMonth.add(currentDay);
            currentDay = currentDay.plusDays(1);
        }
        model.addAttribute("doctorId",id);
        model.addAttribute("currentDate", currentDate);
        model.addAttribute("currentMonth", currentMonth);
        model.addAttribute("allDaysInMonth", allDaysInMonth);

        return "/normal/calendar"; // Return the Thymeleaf template name
    }
	
	
	 @GetMapping("/appointments/{date}/{doctorId}")
	    public String showAppointmentsForDay(@PathVariable("date") String date, @PathVariable("doctorId")int doctorIOd, Model model) {
	    	System.out.println(date);
	        LocalDate selectedDate = LocalDate.parse(date);
	        System.out.println(selectedDate);
	        List<LocalTime> availableSlots = generateAvailableSlots();
	        List<Appointment> bookedAppointments = appointmentRepository.findByDateAndDoctorId(selectedDate,doctorIOd);

	        Map<LocalTime, Boolean> slotAvailabilityMap = new LinkedHashMap<>();
	        for (LocalTime slot : availableSlots) {
	            slotAvailabilityMap.put(slot, isSlotBooked(slot, bookedAppointments));
	        }

	        model.addAttribute("selectedDate", selectedDate);
	        model.addAttribute("slotAvailabilityMap", slotAvailabilityMap);


	        return "/normal/appointments"; // Return the Thymeleaf template name
	    }
	 
	 private List<LocalTime> generateAvailableSlots() {
	        List<LocalTime> slots = new ArrayList<>();
	        LocalTime startTime = LocalTime.of(10, 0);
	        LocalTime endTime = LocalTime.of(18, 0);

	        while (startTime.isBefore(endTime)) {
	            slots.add(startTime);
	            startTime = startTime.plusHours(1);
	        }
	        return slots;
	    }

	    private boolean isSlotBooked(LocalTime slot, List<Appointment> appointments) {
	        for (Appointment appointment : appointments) {
	            if (appointment.getTimeSlot().getTime().equals(slot)) {
	                return true; // Slot is booked
	            }
	        }
	        return false; // Slot is available
	    }
	    
	    
	    @GetMapping("/book-slot/{date}/{time}/{dId}")
	    public String bookSlot(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
	                           @PathVariable String time,
	                           @PathVariable("dId") Integer dId,
	                           Model model) {
	    	
	    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
	    	LocalTime selectedTime = LocalTime.parse(time, formatter);
	        // Here, you can implement the logic for booking the selected slot
	    	Appointment appointment = new Appointment();
	    	
	  
	    	
	        System.out.println(date);
	        System.out.println(selectedTime);
	        model.addAttribute("appointment",appointment);
	        model.addAttribute("doctorId",dId);
	        model.addAttribute("selectedDate", date);
	        model.addAttribute("selectedTime", selectedTime);
	        
	        return "/normal/book-slot"; // Return the Thymeleaf template name
	    }
	    
	   
	    @PostMapping("/submit-booking")
	    public String submitbooking(@ModelAttribute("appointment") Appointment appointment,
	    		@RequestParam("date") String date,
	    		@RequestParam("time") String time,
	    		@RequestParam("doctorId") int doctorId,
	    		Principal principal,
	    		Model model){
	    	System.out.println(date);
	    	System.out.println(time);
//	    	date
	    	LocalDate selectedDate = LocalDate.parse(date);
	    	
//	    	time
	    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	    	LocalTime selectedTime = LocalTime.parse(time, formatter);
	    	
	    	TimeSlot timeSlot = new TimeSlot();
	    	timeSlot.setBooked(true);
	    	timeSlot.setDate(selectedDate);
	    	timeSlot.setTime(selectedTime);
	    	Doctor doctor = doctorRepository.findById(doctorId);
	    	appointment.setDoctor(doctor);
	    	appointment.setTimeSlot(timeSlot);
	    	String name = principal.getName();
	    	User user = userRepository.getUserByEmail(name);
	    	appointment.setUser(user);
	    	appointmentRepository.save(appointment);
	    	timeSlotRepository.save(timeSlot);
	    			
	    	
	    	System.out.println(selectedDate);
	    	System.out.println(selectedTime);
	    	System.out.println(appointment);
	    	
	    	
	    	return "redirect:/user/bookings";
	    }
	    
	    @GetMapping("/bookings")
	    public String showUserBookings(Principal principal,Model model) {
	        // Retrieve user-specific bookings, e.g., based on user ID or other criteria
	    	String name = principal.getName();
	    	User user = userRepository.getUserByEmail(name);
	        List<Appointment> userBookings = appointmentRepository.findByUserIdOrderByDateAsc(user.getId());
	        
	        model.addAttribute("userBookings", userBookings);
	        return "/normal/user-bookings";
	    }
	    
	    @GetMapping("/cancel-booking/{bookingId}")
	    public String cancelBooking(@PathVariable int bookingId) {
	        // Fetch the appointment by its ID
	        Appointment appointment = appointmentRepository.findById(bookingId);
	        
	        if (appointment != null) {
	            // Delete the appointment or update its status to canceled based on your requirement
	        	TimeSlot timeSlot = timeSlotRepository.findById(appointment.getTimeSlot().getId());
	        	appointment.setUser(null);
	        	appointment.setDoctor(null);
	        	appointment.setTimeSlot(null);
	        	timeSlotRepository.delete(timeSlot);
	            appointmentRepository.delete(appointment);
	        }
	        
	        // Redirect to the user's booking page or any other appropriate page
	        return "redirect:/user/bookings";
	    }
	    
	    @GetMapping("/change-password")
		public String changePassword() {
			return "/normal/change-password";
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
				return "redirect:/user/change-password";
			}
			
			
			return "redirect:/user/doctor";
		}

}
