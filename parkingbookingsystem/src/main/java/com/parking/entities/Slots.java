package com.parking.entities;

import java.sql.Time;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Slots {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private boolean isAvailable;
	private int price;
	@Column(unique = true)
	private String name;
	
	
	
	private Slots(boolean isAvailable, int price, String name, User user) {
		super();
		this.isAvailable = isAvailable;
		this.price = price;
		this.name = name;
		
	
	}

	public Slots() {
		super();
		// TODO Auto-generated constructor stub
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public boolean isAvailable() {
		return isAvailable;
	}


	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}


	public int getPrice() {
		return price;
	}


	public void setPrice(int price) {
		this.price = price;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	




	

	
	

	
	

	
	
	
	
	
	
	
	

}
