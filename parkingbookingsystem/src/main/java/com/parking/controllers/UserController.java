package com.parking.controllers;

import java.security.Principal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.parking.dao.OrderRepository;
import com.parking.dao.SlotRepository;
import com.parking.dao.UserRepository;
import com.parking.entities.Order;
import com.parking.entities.Slots;
import com.parking.entities.User;
import com.parking.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SlotRepository slotRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	private final Map<String, LocalDateTime> lockedUsersMap = new HashMap<>();
	
	@ModelAttribute
	public void CommonData(Model model, Principal principal) {
		String name = principal.getName();
		User user = userRepository.getUserByEmail(name);
		System.out.println(user);
		model.addAttribute("user",user);
		Slots slot = slotRepository.findSlotsByname("P6");
		model.addAttribute("slot", slot);
		long count = slotRepository.count();
		model.addAttribute("count",count);
		System.out.println("user added in model");
		
		
	}
	
	@GetMapping("/UserDashboard")
	public String userDashboard() {
		return "normal/UserDashboard";
	}
	
	@GetMapping("/pricing")
	public String price(Model model) {
		
		
		return"/normal/pricing";
	}
	
	@GetMapping("/bookSlot")
	public String bookSlot(Model model) {
		
		List<Slots> availableSlots = slotRepository.findByIsAvailableTrue();
		System.out.println(availableSlots);
		
		model.addAttribute("availableSlots",availableSlots);
		Order order = new Order();
		model.addAttribute("order",order);
		System.out.println(order);
		
//		model.addAttribute("availableSlots",availableSlots);
		return"/normal/bookSlot";
	}
	
	@PostMapping("/confirmBooking")
	public String booknow(@RequestParam("availslot") String slot,
			@RequestParam("clientName") String name,
			@RequestParam("vNumber") String vname,
			@RequestParam("duration") Integer duration,
			@RequestParam(name = "checked", defaultValue = "false") Boolean checked,
			Model model, Principal principal,HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			if(checked) {
				
				long timeInt= 0;
				int t=0;
				Slots p2 = slotRepository.findSlotsByname("p6");
				int price=1;
				switch (duration) {
				case 1: price= 2* p2.getPrice();
						timeInt = 20000;
						t=2;
				break;
				case 2: price= 4* p2.getPrice();
				timeInt = 40000;
				t=4;
				break;
				case 3: price= 8* p2.getPrice();
				timeInt = 80000;
				t=8;
				break;
				case 4: price= 12* p2.getPrice();
				timeInt = 120000;
				t=12;
				break;
				case 5: price= 24* p2.getPrice();
				timeInt = 240000;
				t=24;
				break;
				}
				
				String username = principal.getName();
				User user = userRepository.getUserByEmail(username);
				Order order = new Order();
				SecureRandom secureRandom = new SecureRandom();
				long randomNumber=100_000_000L +secureRandom.nextInt(900_000_000);
				String txn_id = "txn_"+ randomNumber;
				
				

		        String timeString = String.format("%06d", timeInt); // Pad with leading zeros if needed

		        // Parse the time string into a LocalTime object
		        LocalTime localTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HHmmss"));

		        System.out.println("Integer: " + timeInt);
		        
				
				System.out.println(name);
				System.out.println(vname);
				System.out.println(slot);
				System.out.println(duration);
//				System.out.println(checked);
				System.out.println(price);
				System.out.println(txn_id);
				System.out.println("Converted LocalTime: " + localTime);
				
				
				order.setClientName(name);
				order.setvNumber(vname);
				order.setTotalFare(price);
				order.setTransactional_id(txn_id);
				order.setDuration(localTime);
				order.setStatus("Paid");
				Slots slots = slotRepository.findSlotsByname(slot);
				
				slots.setAvailable(false);
				 LocalDateTime lockExpiration = LocalDateTime.now().plusHours(1);
		         lockedUsersMap.put(slot, lockExpiration);
		         
		         LocalDateTime localDateTime = LocalDateTime.now();
		         order.setDateTime(localDateTime);
		      order.setUser(user);
		      order.setSlot(slot);
		      orderRepository.save(order);
		      slotRepository.save(slots);
		      
				model.addAttribute("name",name);
				model.addAttribute("vname",vname);
				model.addAttribute("duration",duration);
				model.addAttribute("price",price);
				model.addAttribute("slot",slot);
				model.addAttribute("order",order);
				
				return"/normal/confirmBooking";
			}
			else {
				session.setAttribute("message", new Message("Please check the check box..!!!","alert-danger"));
				return "/normal/bookSlot";
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			session.setAttribute("message", new Message("something went wrong..!!!","alert-danger"));
			e.printStackTrace();
			return "/normal/bookSlot";
		}
		
		
		
	}
	@GetMapping("/receipt/{id}")
	public String checkout(Principal principal,Model model,@PathVariable("id") int id) {
		
		Order order = orderRepository.findbyOrderId(id);
		
		model.addAttribute(order);
		
		
		
		
		return"/normal/receipt";
	}
	
	@GetMapping("/history")
	public String history(Principal principal,Model model) {
		
		String name = principal.getName();
		User user = userRepository.getUserByEmail(name);
		List<Order> orders = orderRepository.findByUser(user);
		
		
		model.addAttribute("orders", orders);
		return "/normal/history";
	}
	
	   @Transactional
	    @Scheduled(fixedRate = 300000) // Run every minute (adjust as needed)
	    public void unlockUsers() {
	        LocalDateTime currentDateTime = LocalDateTime.now();
	        System.out.println(currentDateTime);
	        
	        // Iterate through the locked users and unlock those whose lock expiration time has passed
	        for (String slot : lockedUsersMap.keySet()) {
	        	System.out.println(slot);
	        	
	            LocalDateTime lockExpiration = lockedUsersMap.get(slot);
	            System.out.println(lockExpiration);
	            if (currentDateTime.isAfter(lockExpiration)) {
	                Slots slots= slotRepository.findSlotsByname(slot);
	                if (slot != null) {
	                    slots.setAvailable(true);
	                    slotRepository.save(slots);
	                    // Remove the user from the map after unlocking
	                    lockedUsersMap.remove(slot);
	                }
	            }
	        }

}
	
	
	
	
}
