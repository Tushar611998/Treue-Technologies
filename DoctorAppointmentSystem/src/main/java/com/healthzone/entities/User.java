package com.healthzone.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.UniqueConstraint;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	private String role;
	@Column(length = 1000)
	private String about;
	@Column(unique = true)
	private String email;
	private String password;
	private boolean isActive;
	
	
	
	



	public User() {
		super();
		// TODO Auto-generated constructor stub
	}





	private User(int id, String name, String role, String about, String email, String password, boolean isActive) {
		super();
		this.id = id;
		this.name = name;
		this.role = role;
		this.about = about;
		this.email = email;
		this.password = password;
		this.isActive = isActive;
	}





	private User(String name, String role, String about, String email, String password, boolean isActive) {
		super();
		this.name = name;
		this.role = role;
		this.about = about;
		this.email = email;
		this.password = password;
		this.isActive = isActive;
	}




	public int getId() {
		return id;
	}




	public void setId(int id) {
		this.id = id;
	}




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public String getRole() {
		return role;
	}




	public void setRole(String role) {
		this.role = role;
	}




	public String getAbout() {
		return about;
	}




	public void setAbout(String about) {
		this.about = about;
	}




	public String getEmail() {
		return email;
	}




	public void setEmail(String email) {
		this.email = email;
	}




	public String getPassword() {
		return password;
	}




	public void setPassword(String password) {
		this.password = password;
	}




	public boolean isActive() {
		return isActive;
	}




	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}





	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", role=" + role + ", about=" + about + ", email=" + email
				+ ", password=" + password + ", isActive=" + isActive + "]";
	}




	



	



	
	
	
	
	
	

	
	


	
	
	
	
	

}
