package com.product.api.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.product.api.entity.Category;

@Repository
public interface RepoCategory extends JpaRepository<Category, Integer> {
	@Query(value = "SELECT * FROM Category ORDER BY category_id", nativeQuery = true)
    List<Category> getCategories();
}
