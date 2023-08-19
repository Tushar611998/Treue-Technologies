package com.healthzone.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthzone.entities.TimeSlot;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Integer>{
	
	TimeSlot findById(long id);

}
