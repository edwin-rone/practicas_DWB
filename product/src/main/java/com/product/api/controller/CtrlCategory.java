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

import com.product.api.commons.dto.ApiResponse;
import com.product.api.dto.in.DtoCategoryIn;
import com.product.api.entity.Category;
import com.product.api.service.SvcCategory;
import com.product.exception.ApiException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controlador para gestionar las operaciones relacionadas con las categorías.
 * Define endpoints para consultar, crear, actualizar y habilitar/deshabilitar categorías.
 */
@RestController
@RequestMapping("/category")
@Tag(name = "Category", description = "Administración de categorías")
public class CtrlCategory {

	/**
     * Inyección del servicio que maneja la lógica de negocio de categorías.
     */
	@Autowired
    private SvcCategory svcCategory;

	/**
     * Endpoint GET sin ruta adicional para obtener todas las categorías.
     * 
     * @return Una ResponseEntity con la lista de categorías y el código de estado HTTP.
     */
	@GetMapping
	@Operation(summary = "Consultar categorías", description = "Retorna todas las categorías registradas en el sistema")
    public ResponseEntity<List<Category>> getCategories() {
        return svcCategory.getCategories();
    }
    
	/**
     * Endpoint GET para obtener solo las categorías activas.
     * Se accede mediante la ruta /category/active.
     * 
     * @return Una ResponseEntity con la lista de categorías activas.
     */
    @GetMapping("/active")
    @Operation(summary = "Consultar categorías activas", description = "Retorna todas las categorías registradas en el sistema con status 1")
    public ResponseEntity<List<Category>> getActiveCategories(){
    	return svcCategory.getActiveCategories();
    }
    
    /**
     * Endpoint POST para crear una nueva categoría.
     * Recibe los datos en formato JSON en el cuerpo de la petición, 
     * los valida (usando @Valid) y si hay errores de validación, lanza una ApiException.
     * 
     * @param in Objeto DtoCategoryIn que contiene los datos de entrada para la categoría.
     * @param bindingResult Resultado de la validación, donde se recogen los errores.
     * @return Una ResponseEntity con un ApiResponse indicando el resultado de la operación.
     */
    @PostMapping
	public ResponseEntity<ApiResponse> createCategory(@Valid @RequestBody DtoCategoryIn in, BindingResult bindingResult){
    	// Si existen errores en la validación, se lanza una excepción con el primer error encontrado.
        if(bindingResult.hasErrors())
    		throw new ApiException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
        // Llama al servicio para crear la categoría y retorna la respuesta
        return svcCategory.createCategory(in);
	}
    
    /**
     * Endpoint PUT para actualizar una categoría existente.
     * Recibe el ID de la categoría en la URL y los nuevos datos en el cuerpo de la petición.
     * Valida los datos y, en caso de error, lanza una ApiException.
     * 
     * @param in Objeto DtoCategoryIn con los datos a actualizar.
     * @param id Identificador de la categoría a actualizar.
     * @param bindingResult Resultado de la validación.
     * @return Una ResponseEntity con un ApiResponse indicando el resultado de la operación.
     */
    @PutMapping("/{id}")
	public ResponseEntity<ApiResponse> updateCategory(@Valid @RequestBody DtoCategoryIn in, @PathVariable("id") Integer id, BindingResult bindingResult){
    	// Si hay errores en la validación, lanza una excepción con el mensaje del error.
        if (bindingResult.hasErrors())
			throw new ApiException(HttpStatus.BAD_REQUEST, bindingResult.getFieldError().getDefaultMessage());
        // Llama al servicio para actualizar la categoría y retorna la respuesta
        return svcCategory.updateCategory(in, id);
	}
	
    /**
     * Endpoint PATCH para habilitar (activar) una categoría.
     * Se accede a la ruta /category/{id}/enable y se usa PATCH para indicar una actualización parcial.
     * 
     * @param id Identificador de la categoría a habilitar.
     * @return Una ResponseEntity con un ApiResponse indicando el resultado de la operación.
     */
    @PatchMapping("/{id}/enable")
	public ResponseEntity<ApiResponse> enableCategory(@PathVariable("id") Integer id){
    	return svcCategory.enableCategory(id);
    }
    
    /**
     * Endpoint PATCH para deshabilitar (desactivar) una categoría.
     * Se accede a la ruta /category/{id}/disable.
     * 
     * @param id Identificador de la categoría a deshabilitar.
     * @return Una ResponseEntity con un ApiResponse indicando el resultado de la operación.
     */
    @PatchMapping("/{id}/disable")
	public ResponseEntity<ApiResponse> disableCategory(@PathVariable("id") Integer id){
    	return svcCategory.disableCategory(id);
    }
}
