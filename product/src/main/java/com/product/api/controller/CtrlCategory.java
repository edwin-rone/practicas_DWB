package com.product.api.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import com.product.api.dto.in.DtoCategoryIn;
import com.product.api.entity.Category;
import com.product.api.service.SvcCategory;
import com.product.commons.dto.ApiResponse;
import com.product.exception.ApiException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/category")
public class CtrlCategory {

    @Autowired
    private SvcCategory svcCategory;

    @GetMapping
    public ResponseEntity<List<Category>> getCategories() {
        return svcCategory.getCategories();
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<Category>> getActiveCategories(){
    	return svcCategory.getActiveCategories();
    }
    
    @PostMapping
	public ResponseEntity<ApiResponse> createCategory(@Valid @RequestBody DtoCategoryIn in, BindingResult bindingResult){
    	if(bindingResult.hasErrors())
    		throw new ApiException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
    	return svcCategory.createCategory(in);
	}
    
    @PutMapping("/{id}")
	public ResponseEntity<ApiResponse> updateCategory(@Valid @RequestBody DtoCategoryIn in, @PathVariable("id") Integer id, BindingResult bindingResult){
    	if (bindingResult.hasErrors())
			throw new ApiException(HttpStatus.BAD_REQUEST, bindingResult.getFieldError().getDefaultMessage());
		return svcCategory.updateCategory(in, id);
	}
	
    @PatchMapping("/{id}/enable")
	public ResponseEntity<ApiResponse> enableCategory(@PathVariable("id") Integer id){
    	return svcCategory.enableCategory(id);
    }
    
    @PatchMapping("/{id}/disable")
	public ResponseEntity<ApiResponse> disableCategory(@PathVariable("id") Integer id){
    	return svcCategory.disableCategory(id);
    }
}
