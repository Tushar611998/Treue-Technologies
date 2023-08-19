package com.bookParadise.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Orders")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int totalFare;
	private String txn_id;
	private String bookName;
	private LocalDateTime dateTime;
	private String phone;
	@Column(length = 1500)
	private String Address;
	private LocalDate expectedDelivery;
	@Autowired
	@ManyToOne(cascade = CascadeType.ALL)
	private Book book;
	@Autowired
	@ManyToOne(cascade = CascadeType.ALL)
	private User user;
	
	
	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getTotalFare() {
		return totalFare;
	}


	public void setTotalFare(int totalFare) {
		this.totalFare = totalFare;
	}


	public String getTxn_id() {
		return txn_id;
	}


	public void setTxn_id(String txn_id) {
		this.txn_id = txn_id;
	}


	public String getBookName() {
		return bookName;
	}


	public void setBookName(String bookName) {
		this.bookName = bookName;
	}


	public LocalDateTime getDateTime() {
		return dateTime;
	}


	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getAddress() {
		return Address;
	}


	public void setAddress(String address) {
		Address = address;
	}


	public LocalDate getExpectedDelivery() {
		return expectedDelivery;
	}


	public void setExpectedDelivery(LocalDate expectedDelivery) {
		this.expectedDelivery = expectedDelivery;
	}


	public Book getBook() {
		return book;
	}


	public void setBook(Book book) {
		this.book = book;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}
	
	
	
	
	

}
