package com.bookParadise.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookParadise.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{
	
	@Query("select o from Order o where o.user =:user")
	public List<Order> findByUser(@Param("user") com.bookParadise.entities.User user);
	
	@Query("select o from Order o where o.id =:id")
	public Order findbyOrderId(@Param("id") int id);
}
