package com.healthzone.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.healthzone.entities.Appointment;
import com.healthzone.entities.User;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
	
	  List<Appointment> findByDate(LocalDate date);
	  
	  List<Appointment> findByDateAndDoctorId(LocalDate date, int dId);
	  
	  Appointment findById(int id);
	  
	 
	  List<Appointment> findByUserIdOrderByDateAsc(int id);
	  
	  List<Appointment> findByDoctorSpecialtyAndDateGreaterThanEqualOrderByDateAsc(String specialization, LocalDate today);

}
