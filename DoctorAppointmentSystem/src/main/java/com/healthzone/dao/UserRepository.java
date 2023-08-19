package com.healthzone.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.healthzone.entities.User;

public interface UserRepository  extends JpaRepository<User, Integer> {
	
	@Query("select u from User u where u.email=:email")
	public User getUserByEmail(@Param("email")  String email);

}
