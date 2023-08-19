package com.bookParadise.controllers;

import java.security.Principal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

import com.bookParadise.dao.BookRepository;
import com.bookParadise.dao.CartRepository;
import com.bookParadise.dao.OrderRepository;
import com.bookParadise.dao.UserRepository;
import com.bookParadise.entities.Book;
import com.bookParadise.entities.Cart;
import com.bookParadise.entities.Order;
import com.bookParadise.entities.User;
import com.bookParadise.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
//	common data
	
	@ModelAttribute
	public void CommonData(Model model, Principal principal,HttpSession session) {
		String name = principal.getName();
		com.bookParadise.entities.User user = userRepository.getUserByEmail(name);
		System.out.println(user);
		model.addAttribute("user",user);
		session.setAttribute("user", user);
		
		List<Book> Fbooks = bookRepository.findByIsFeaturedTrue();
		model.addAttribute("Fbooks",Fbooks);
		
		List<Book> bbooks = bookRepository.findBestSellingBooks();
		model.addAttribute("bbooks",bbooks);
		System.out.println("user added in model");
		
		
	}
	
//	dashboard
	
	@GetMapping("/dashboard")
	public String dashboard() {
		
		return "/user/dashboard";
	}
	
	
	
//	cart operations
	
	@GetMapping("/addToCart/{id}")
	public String addToCart(@PathVariable("id") int id,Model model, Principal principal) {
		
		String name = principal.getName();
		User user = userRepository.getUserByEmail(name);
		
		Book book = bookRepository.getBookById(id);
		
		Cart  cart = new Cart();
		cart.setBook(book);
		cart.setUser(user);
		
		cartRepository.save(cart);
		
		model.addAttribute("addedcart",cart);
		
		
		
		return "redirect:/user/cart";
	}
	@GetMapping("/removeCart/{id}")
	public String removeCart(@PathVariable("id") int id,Model model, Principal principal) {
		
		
		
		
		Cart cart = cartRepository.findbyCartId(id);
		cart.setBook(null);
		cart.setUser(null);
		cartRepository.delete(cart);
		
		return "redirect:/user/cart";
	}
	
	@GetMapping("/cart")
	public String cart(Principal principal, Model model) {
		
		String name = principal.getName();
		User user = userRepository.getUserByEmail(name);
		
		List<Cart> carts = cartRepository.findByUser(user.getId());
		
		model.addAttribute("cart", carts);
		
		return "/user/cart";
	}
	
//	product buying process
	
	@GetMapping("/buyNow/{id}")
	public String buyNow(@PathVariable("id") int id,Principal principal, Model model) {
		
		Book book = bookRepository.getBookById(id);
		String name = principal.getName();
		User user = userRepository.getUserByEmail(name);
		model.addAttribute("user",user);
		model.addAttribute("book",book);
		return"/user/buyNow";
	}
	
	@PostMapping("/placeOrder")
	public String placeOrder(@RequestParam("bookId") int id,
			@RequestParam("add") String add,
			@RequestParam("phone") String phone,
			Principal principal, Model model) {
		
//		book
		Book book = bookRepository.getBookById(id);
		String name = principal.getName();
		
//		user
		User user = userRepository.getUserByEmail(name);
		
//		txn_id
		SecureRandom secureRandom = new SecureRandom();
		long randomNumber=100_000_000L +secureRandom.nextInt(900_000_000);
		String txn_id = "txn_"+ randomNumber;
//		
//		dateTime
		LocalDateTime dateTime = LocalDateTime.now();
		
//		expectedDelivery
		LocalDate deliveryDateTime = LocalDate.now().plusDays(5);
		
		
		Order order = new Order();
		
		
		order.setAddress(add);
		order.setBook(book);
		order.setBookName(book.getBook_name());
		order.setDateTime(dateTime);
		order.setExpectedDelivery(deliveryDateTime);
		order.setPhone(phone);
		order.setTotalFare(book.getPrice());
		order.setTxn_id(txn_id);
		order.setUser(user);
		
		orderRepository.save(order);

		
		model.addAttribute("user",user);
		model.addAttribute("book",book);
		model.addAttribute("add",add);
		model.addAttribute("phone",phone);
		model.addAttribute("order",order);
		return "redirect:/user/receipt/" + order.getId();
	}
	@GetMapping("/receipt/{id}")
	public String receipt(@PathVariable("id") Integer id,Model model) {
		Order order = orderRepository.findbyOrderId(id);
		model.addAttribute("order", order);
		return "/user/reciept";
	}
	
//	 change password
	
	@GetMapping("/change-password")
	public String changePassword() {
		return "/user/change-password";
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
		
		
		return "redirect:/user/dashboard";
	}
	
//	order history
	
	@GetMapping("/myOrders")
	public String history(Principal principal,Model model) {
		
		String name = principal.getName();
		User user = userRepository.getUserByEmail(name);
		List<Order> orders = orderRepository.findByUser(user);
		
		
		model.addAttribute("history","Your Orders");
		model.addAttribute("orders", orders);
		return "/user/orders";
	}
	


}
