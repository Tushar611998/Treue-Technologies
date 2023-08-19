package com.healthzone.entities;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Appointment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private LocalDate date;
	private String phone;
	private int age;
	private String gender;
	@Column(length = 1000)
	private String about;
	@Column(columnDefinition =  "boolean default false")
	private boolean canceled;
	

	@ManyToOne(cascade = CascadeType.ALL)
	 private TimeSlot timeSlot;
	
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Doctor doctor;
	
	@ManyToOne(cascade = CascadeType.ALL)
    private User user;
	

	public Appointment(String name) {
		super();
		this.name = name;
	}

	public Appointment() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TimeSlot getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(TimeSlot timeSlot) {
		this.timeSlot = timeSlot;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	@Override
	public String toString() {
		return "Appointment [id=" + id + ", name=" + name + ", date=" + date + ", phone=" + phone + ", age=" + age
				+ ", gender=" + gender + ", about=" + about + ", canceled=" + canceled + ", timeSlot=" + timeSlot
				+ ", doctor=" + doctor + ", user=" + user + "]";
	}

	
	

	
	
	
	
	
	

	
	
	

}
