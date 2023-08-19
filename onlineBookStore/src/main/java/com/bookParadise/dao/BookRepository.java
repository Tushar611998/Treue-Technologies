package com.bookParadise.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookParadise.entities.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {
	
	@Query("select b from Book b where b.id=:id")
	public Book getBookById(@Param("id") Integer id);
	
	
	List<Book> findByIsFeaturedTrue();
	
	 @Query("SELECT p FROM Book p WHERE p.Best_selling = true")
	    List<Book> findBestSellingBooks();
	 
	 @Query("select b from Book b where b.category=:category")
	 public List<Book> findByCategory(@Param("category") String category);
}
