package com.healthzone.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.healthzone.entities.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Integer>{
	
	
	@Query("select d from Doctor d where d.id =:dId")
	public Doctor findById(@Param("dId") int did);
	
	Doctor findByName(String name);

}
