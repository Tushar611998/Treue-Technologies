package com.parking.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.parking.entities.Slots;

public interface SlotRepository extends JpaRepository<Slots, Integer> {
	
	@Transactional
	@Modifying
	@Query("UPDATE Slots s SET s.price = :newPrice")
	public void updatePrice(@Param("newPrice") int newPrice);
	
	
	@Query("select  s from Slots s where s.name= :sName")
	public Slots findSlotsByname(@Param("sName") String sName);
	
	public long count();
	
	@Query("select count(s) from Slots s where s.isAvailable= :isAvailable")
	public long countByIsAvailable(@Param("isAvailable")Boolean isAvailable);
	
	public List<Slots> findByIsAvailableTrue();

}
