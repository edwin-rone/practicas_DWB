package com.product;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import com.product.Category;
import com.product.CategoryServices;

@RestController
@RequestMapping("/category")
public class CtrlProduct {

    private CategoryServices categoryServices;

    public CtrlProduct() {
        // Inicializamos el servicio y pre-cargamos algunas categorías para que la lista no esté vacía
        categoryServices = new CategoryServices();
        categoryServices.createCategory("Lentes", "Lts");
        categoryServices.createCategory("Relojes", "Rljs");
    }

    @GetMapping
    public List<Category> getCategories() {
        // Se retorna la lista de categorías activas, la cual Spring Boot serializa a JSON
        return categoryServices.getActiveCategories();
    }
}
