package com.bookParadise.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/admin")
public class AdminController {
	
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
	
	@GetMapping("/dashboard")
	public String dashboard() {
		
		return "/admin/dashboard";
	}
	
	@GetMapping("/addBook")
	public String addBook(Model model) {
		
		Book book = new Book();
		model.addAttribute("book",book);
		
		return "/admin/addBook";
	}
	
	@PostMapping("/add-book")
	public String bookAdd(@ModelAttribute("book") Book book,@RequestParam(name = "isAvailable", defaultValue = "false") boolean isAvailable,@RequestParam("myimage") MultipartFile file, Model model,HttpSession session) {
		
		System.out.println(book);
		System.out.println(isAvailable);
		try {
			
			book.setAvailable(true);
			if(file!=null) {
				book.setImage(file.getOriginalFilename());
				
				File file2 = new ClassPathResource("/static/img").getFile();
				java.nio.file.Path path = Paths.get(file2.getAbsolutePath() + File.separator + file.getOriginalFilename());
				System.out.println(path);
				
				Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);
				
			}
			
			bookRepository.save(book);
			model.addAttribute("book",book);
			
			return "redirect:/admin/viewProduct/" + book.getId();
			
			
		} catch (Exception e) {
			session.setAttribute("message", new Message("Something Went Wrong..!!"+e.getMessage(),"alert-danger"));
			
			e.printStackTrace();
			return "/admin/addBook";
		}
		
		
		
	}
	
	@GetMapping("/viewProduct/{id}")
	public String viewProduct(@PathVariable("id") Integer id,Model model) {
		
		Book book = bookRepository.getBookById(id);
		
		model.addAttribute("book",book);
		
		return "/admin/viewProduct";
	}
	
	
	@GetMapping("/featured")
	public String featured(Model model) {
		
		List<Book> books = bookRepository.findByIsFeaturedTrue();
		model.addAttribute("books",books);
		
		return "/admin/featured";
	}
	

	@GetMapping({"/addFeatured","addBest","/update","/delete"})
	public String multi(Model model) {
		
		List<Book> books = bookRepository.findAll();
		model.addAttribute("books",books);
		
		return "/admin/multiuse";
	}
	
	@GetMapping("/setFeatured/{id}")
	public String setFeatured(@PathVariable("id") int id,Model model, HttpSession session) {
		
		
		Book book = bookRepository.getBookById(id);
		
		book.setFeatured(true);
		bookRepository.save(book);
		
		session.setAttribute("message", new Message("Featured book added successfully..","alert-success"));
		
		
		
		return "redirect:/admin/featured";
	}
	
	@GetMapping("/removeFeatured/{id}")
	public String removeFeatured(@PathVariable("id") int id,Model model, HttpSession session) {
		
		
		Book book = bookRepository.getBookById(id);
		
		book.setFeatured(false);
		bookRepository.save(book);
		
		session.setAttribute("message", new Message("Featured book Removed successfully..","alert-success"));
		
		
		
		return "redirect:/admin/featured";
	}
	
	
	@GetMapping("/bestselling")
	public String bestselling(Model model) {
		
		List<Book> books = bookRepository.findBestSellingBooks();
		model.addAttribute("books",books);
		
		return "/admin/bestselling";
	}
	
	
	@GetMapping("/setBest/{id}")
	public String setBest(@PathVariable("id") int id,Model model, HttpSession session) {
		
		
		Book book = bookRepository.getBookById(id);
		
		book.setBest_selling(true);
		bookRepository.save(book);
		
		session.setAttribute("message", new Message("Best selling book added successfully..","alert-success"));
		
		
		
		return "redirect:/admin/bestselling";
	}
	
	@GetMapping("/removeBest/{id}")
	public String removeBest(@PathVariable("id") int id,Model model, HttpSession session) {
		
		
		Book book = bookRepository.getBookById(id);
		
		book.setBest_selling(false);
		bookRepository.save(book);
		
		session.setAttribute("message", new Message("Best selling book Removed successfully..","alert-success"));
		
		
		
		return "redirect:/admin/bestselling";
	}
	
	@GetMapping("/deleteBook/{id}")
	public String deleteBook(@PathVariable("id") int id, HttpSession session) throws IOException {
		
		Book book = bookRepository.getBookById(id);
		
		File file = new ClassPathResource("/static/img/" + book.getImage()).getFile();
		
		file.delete();
		
		bookRepository.delete(book);
		session.setAttribute("message", new Message("Book Deleted successfully..","alert-success"));
		return "redirect:/allBooks";
	}
	
	
	@GetMapping("/updateBook/{id}")
	public String updateBook(@PathVariable("id")int id,Model model) {
		
		Book book = bookRepository.getBookById(id);
		model.addAttribute("book",book);
		
		return "/admin/updateBook";
	}
	
	@PostMapping("/doUpdate")
	public String doUpdate(@ModelAttribute("book") Book book,@RequestParam("oldId") Integer oldId, @RequestParam("myimage") MultipartFile file, Model model, HttpSession session) {
		
		
		try {
			
			Book oldBook = bookRepository.getBookById(oldId);
			
			if(!file.isEmpty()) {
				
//				remove old file
			    File file2 = new ClassPathResource("/static/img/"+oldBook.getImage()).getFile();
			    System.out.println(file2);
			    file2.delete();
			    oldBook.setImage(file.getOriginalFilename());
			    File file3 = new ClassPathResource("/static/img/").getFile();
			    System.out.println(file3);
			    Path path = Paths.get(file3.getAbsolutePath()+ File.separator + file.getOriginalFilename());
			    System.out.println(path);
			    
			    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
			}
			
			oldBook.setAuthor(book.getAuthor());
			oldBook.setAvailable(true);
			oldBook.setCategory(book.getCategory());
			oldBook.setDescription(book.getDescription());
			oldBook.setBook_name(book.getBook_name());
			oldBook.setPrice(book.getPrice());
			
			bookRepository.save(oldBook);
			model.addAttribute("book",oldBook);
			
			session.setAttribute("message", new Message("Book Updated successfully..","alert-success"));
			return "redirect:/allBooks";
		} catch (Exception e) {
			// TODO: handle exception
			session.setAttribute("message", new Message("Something went wrong..!!!","alert-danger"));
			e.printStackTrace();
			return "/admin/updateBook";
		}
		 
	}
	
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
		
		
		
		return "redirect:/admin/cart";
	}
	@GetMapping("/removeCart/{id}")
	public String removeCart(@PathVariable("id") int id,Model model, Principal principal) {
		
		
		
		
		Cart cart = cartRepository.findbyCartId(id);
		cart.setBook(null);
		cart.setUser(null);
		cartRepository.delete(cart);
		
		return "redirect:/admin/cart";
	}
	
	@GetMapping("/cart")
	public String cart(Principal principal, Model model) {
		
		String name = principal.getName();
		User user = userRepository.getUserByEmail(name);
		
		List<Cart> carts = cartRepository.findByUser(user.getId());
		
		model.addAttribute("cart", carts);
		
		return "/admin/cart";
	}
	
	@GetMapping("/buyNow/{id}")
	public String buyNow(@PathVariable("id") int id,Principal principal, Model model) {
		
		Book book = bookRepository.getBookById(id);
		String name = principal.getName();
		User user = userRepository.getUserByEmail(name);
		model.addAttribute("user",user);
		model.addAttribute("book",book);
		return"/admin/buyNow";
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
		return "redirect:/admin/receipt/" + order.getId();
	}
	@GetMapping("/receipt/{id}")
	public String receipt(@PathVariable("id") Integer id,Model model) {
		Order order = orderRepository.findbyOrderId(id);
		model.addAttribute("order", order);
		return "/admin/reciept";
	}
	
//	 change password
	
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
		
		
		return "redirect:/admin/dashboard";
	}
	
	@GetMapping("/allOrders")
	public String allBookings(Model model) {
		List<Order> orders = orderRepository.findAll();
		model.addAttribute("orders",orders);
		model.addAttribute("history","All Orders");
		return"/admin/orders";
	}

	@GetMapping("/myOrders")
	public String history(Principal principal,Model model) {
		
		String name = principal.getName();
		User user = userRepository.getUserByEmail(name);
		List<Order> orders = orderRepository.findByUser(user);
		
		
		model.addAttribute("history","Your Orders");
		model.addAttribute("orders", orders);
		return "/admin/orders";
	}
	

}
