package com.product.api.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.product.api.entity.Category;

import jakarta.transaction.Transactional;

@Repository
public interface RepoCategory extends JpaRepository<Category, Integer> {
	@Query(value = "SELECT * FROM Category ORDER BY category_id", nativeQuery = true)
	List<Category> getCategories();
	
	@Query(value ="SELECT * FROM Category WHERE status = 1 ORDER BY category_id", nativeQuery = true)
	List<Category> getActiveCategories();
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO Category(category, tag, status) VALUES (:category, :tag, 1)", nativeQuery = true)
	void createCategory(String category, String tag);
	
	@Modifying
	@Transactional
	@Query(value ="UPDATE Category SET category = :category, tag = :tag WHERE category_id = :category_id",nativeQuery = true)
	void updateCategory(@Param("category_id") Integer category_id, @Param("category") String category,@Param("tag") String tag);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE Category SET status = 1 WHERE category_id = :category_id;", nativeQuery = true)
	void enableCategory(@Param("category_id") Integer category_id);

	@Modifying
	@Transactional
	@Query(value = "UPDATE Category SET status = 0 WHERE category_id = :category_id;", nativeQuery = true)
	void disableCategory(@Param("category_id") Integer category_id);
	
	@Query(value = "SELECT * FROM Category WHERE category_id = :category_id ORDER BY category", nativeQuery = true)
	Category getCategory(@Param("category_id") Integer category_id);

}


