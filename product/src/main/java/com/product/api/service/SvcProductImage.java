package com.product.api.service;

import org.springframework.http.ResponseEntity;

import com.product.api.dto.in.DtoProductImageIn;
import com.product.commons.dto.ApiResponse;

public interface SvcProductImage {
	public ResponseEntity<ApiResponse> uploadProductImage(DtoProductImageIn in);
}
