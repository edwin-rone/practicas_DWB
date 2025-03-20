package com.product.api.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.product.api.dto.DtoCategoryIn;
import com.product.api.entity.Category;
import com.product.commons.dto.ApiResponse;

public interface SvcCategory {
	
	public ResponseEntity<List<Category>> getCategories();
	public ResponseEntity<List<Category>> getActiveCategories();
	public ResponseEntity<ApiResponse> createCategory(DtoCategoryIn in);
	public ResponseEntity<ApiResponse> updateCategory(DtoCategoryIn in, Integer id);
	public ResponseEntity<ApiResponse> enableCategory(Integer id);
	public ResponseEntity<ApiResponse> disableCategory(Integer id);
}
