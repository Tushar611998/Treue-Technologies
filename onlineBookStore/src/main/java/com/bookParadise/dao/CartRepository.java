package com.bookParadise.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookParadise.entities.Cart;
import com.bookParadise.entities.Order;

public interface CartRepository extends JpaRepository<Cart, Integer> {
	
	@Query("select c from Cart c where c.user.id=:user")
	public List<Cart> findByUser(@Param("user") int user);
	
	
	@Query("select o from Cart o where o.id =:id")
	public Cart findbyCartId(@Param("id") int id);

}
