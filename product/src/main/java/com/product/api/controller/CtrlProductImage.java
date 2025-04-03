package com.product.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.product.api.dto.in.DtoProductImageIn;
import com.product.api.service.SvcProductImage;
import com.product.commons.dto.ApiResponse;
import com.product.exception.ApiException;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controlador REST para gestionar operaciones relacionadas con imágenes de productos.
 * Define endpoints para subir una imagen de producto y para deshabilitarla.
 */
@RestController
@RequestMapping("/product-image")
@Tag(name = "ProductImage", description = "Administración de imágenes de productos")
public class CtrlProductImage {
	
	/**
     * Inyección del servicio que maneja la lógica de negocio para las imágenes de producto.
     * Este servicio se utiliza para subir la imagen (almacenándola en el sistema de archivos
     * y registrándola en la base de datos) y para deshabilitar la imagen.
     */
	@Autowired
    SvcProductImage svc;
	
	/**
     * Endpoint POST para subir una imagen de producto.
     * Recibe un objeto DtoProductImageIn en el cuerpo de la petición, lo valida y
     * llama al servicio para subir la imagen.
     *
     * @param in Objeto de entrada que contiene la imagen codificada en Base64 y el ID del producto.
     * @param bindingResult Resultado de la validación del objeto 'in'.
     * @return ResponseEntity con un ApiResponse indicando el resultado de la operación.
     * @throws ApiException en caso de que existan errores de validación.
     */
    @PostMapping
    public ResponseEntity<ApiResponse> createProductImage(@Valid @RequestBody DtoProductImageIn in, BindingResult bindingResult) {
    	// Verifica si existen errores de validación en el objeto de entrada.
        // Si hay algún error, lanza una excepción con el primer mensaje de error encontrado.
        if (bindingResult.hasErrors())
        	throw new ApiException(HttpStatus.BAD_REQUEST, bindingResult.getFieldError().getDefaultMessage());
        // Llama al servicio para subir la imagen y retorna la respuesta.
        return svc.uploadProductImage(in);
    }
    
    /**
     * Endpoint PATCH para deshabilitar (o desactivar) una imagen de producto.
     * La ruta incluye el ID de la imagen a deshabilitar.
     *
     * @param id Identificador de la imagen de producto a desactivar.
     * @return ResponseEntity con un ApiResponse indicando si la operación fue exitosa.
     */
    @PatchMapping("/{id}/disable")
	public ResponseEntity<ApiResponse> disableProductImage(@PathVariable Integer id) {
    	// Llama al servicio para desactivar la imagen, pasando el ID correspondiente.
        return svc.disableProductImage(id);
	}

}
