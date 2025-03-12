package com.product.api.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import com.product.api.entity.Category;
import com.product.api.service.SvcCategory;

@RestController
@RequestMapping("/category")
public class CtrlCategory {

    @Autowired
    private SvcCategory svcCategory;

    @GetMapping
    public ResponseEntity<List<Category>> getCategories() {
        return svcCategory.getCategories();
    }
}
