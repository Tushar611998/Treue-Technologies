package com.bookParadise.controllers;

import java.security.Principal;
import java.util.List;

import org.hibernate.StatelessSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookParadise.dao.BookRepository;
import com.bookParadise.dao.UserRepository;
import com.bookParadise.entities.Book;
import com.bookParadise.entities.User;
import com.bookParadise.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	@ModelAttribute
	public void CommonData(Model model, Principal principal,HttpSession session) {
		
		
		List<Book> Fbooks = bookRepository.findByIsFeaturedTrue();
		model.addAttribute("Fbooks",Fbooks);
		
		List<Book> bbooks = bookRepository.findBestSellingBooks();
		model.addAttribute("bbooks",bbooks);
		
		System.out.println("user added in model");
		
		
	}
	
	
	@GetMapping("/home")
	public String index() {
		
		return "/index";
	}
	
	@GetMapping("/adlogin")
	public String login() {
		
		return "/adlogin";
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		com.bookParadise.entities.User user = new com.bookParadise.entities.User();
		model.addAttribute("user",user);
		return "/register";
	}
	
	@GetMapping("/contactUs")
	public String contactUs() {
		
		return "/contactUs";
	}
	
	@GetMapping("/about")
	public String about() {
		
		return "/about";
	}
	
	@PostMapping("/doRegister")
	public String doRegister(@ModelAttribute User user,@RequestParam(value ="check", defaultValue = "false") boolean checked, Model model,HttpSession session) {
		System.out.println(user);
		try {
			
			if(!checked) {
				throw new Exception("please check the check box..!!!");
			}
			user.setActive(true);
			user.setPassword(encoder.encode(user.getPassword()));
			userRepository.save(user);
			
			session.setAttribute("message", new Message("Registration successfull..!!","alert-success"));
			
		} catch (Exception e) {
			session.setAttribute("message", new Message("Something went wrong..!!" + e.getMessage(),"alert-danger"));
			e.printStackTrace();
		}
		
		
		return "/register";
	}
	
	
	
	@GetMapping("/allBooks")
	public String allBooks(Model model) {
		
		List<Book> books = bookRepository.findAll();
		
		model.addAttribute("books",books);
		return "/allBooks";
	}
	
	
	@GetMapping("/viewProduct/{id}")
	public String viewProduct(@PathVariable("id") Integer id,Model model) {
		
		Book book = bookRepository.getBookById(id);
		
		model.addAttribute("book",book);
		
		return "/viewProduct";
	}
	
	 @PostMapping("/check-delivery/{id}")
	    public String checkDelivery(@RequestParam String pincode,@PathVariable("id") int id, Model model) {
	        // Implement your logic to check delivery at the provided pincode
	        boolean isDeliverable = true;
	        Book book = bookRepository.getBookById(id);
	        model.addAttribute("book",book);
	        model.addAttribute("pincode", pincode);
	        model.addAttribute("isDeliverable", isDeliverable);

	        return "/viewProduct";
	    }
	 
	 
	 @GetMapping({"/fantasy/{caregory}","/horror/{caregory}","/historical/{caregory}","/sci-fi/{caregory}","/biography/{caregory}","/romance/{caregory}","/cooking/{caregory}","/mystry/{caregory}"})
	 public String category(@PathVariable("caregory") String category,Model model) {
		 
		 List<Book> books = bookRepository.findByCategory(category);
		 System.out.println(books);
		 model.addAttribute("category",category);
		 model.addAttribute("books",books);
		 
		 return "/category";
	 }
	

}
