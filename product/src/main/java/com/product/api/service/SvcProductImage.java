package com.product.api.service;

import org.springframework.http.ResponseEntity;

import com.product.api.dto.in.DtoProductImageIn;
import com.product.commons.dto.ApiResponse;

/**
 * Interfaz que define los métodos para gestionar operaciones relacionadas con
 * las imágenes de productos.
 * 
 * Los métodos definidos en esta interfaz retornan un objeto ResponseEntity que envuelve
 * un ApiResponse, permitiendo así enviar respuestas HTTP personalizadas (con código de estado
 * y mensaje) al cliente.
 */
public interface SvcProductImage {
	/**
     * Método para subir una imagen de producto.
     * 
     * Este método se encarga de recibir los datos de la imagen en un objeto de transferencia
     * (DTO) de entrada (DtoProductImageIn), que contiene el ID del
     * producto y la imagen codificada (Base64). La lógica de negocio asociada
     * a la subida de la imagen (como decodificarla, guardarla en el sistema de archivos y
     * almacenar la información en la base de datos) se implementará en la clase que implemente
     * esta interfaz.
     *
     * @param in Objeto DtoProductImageIn que contiene la información necesaria para subir la imagen.
     * @return Un ResponseEntity que envuelve un ApiResponse con el resultado de la operación (por ejemplo,
     *         un mensaje indicando que la imagen se ha subido correctamente o un error).
     */
	public ResponseEntity<ApiResponse> uploadProductImage(DtoProductImageIn in);
	
	/**
     * Método para desactivar (o deshabilitar) una imagen de producto.
     * 
     * Este método recibe el identificador de la imagen que se desea desactivar y se encarga
     * de actualizar su estado en la base de datos (estableciendo un status 0 que
     * indique que la imagen ya no está activa). La lógica para realizar dicha actualización
     * se implementará en la clase que implemente esta interfaz.
     *
     * @param id Identificador de la imagen de producto que se desea desactivar.
     * @return Un ResponseEntity que envuelve un ApiResponse con un mensaje que indica el resultado
     *         de la operación (por ejemplo, confirmación de desactivación o mensaje de error).
     */
	public ResponseEntity<ApiResponse> disableProductImage(Integer id);
}
