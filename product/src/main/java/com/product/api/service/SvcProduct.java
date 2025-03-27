package com.product.api.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.product.api.dto.in.DtoProductIn;
import com.product.api.dto.out.DtoProductListOut;
import com.product.api.dto.out.DtoProductOut;
import com.product.commons.dto.ApiResponse;

/**
 * Interfaz que define los métodos del servicio para gestionar productos.
 * Cada método retorna un ResponseEntity que permite incluir tanto el cuerpo de la respuesta
 * (datos o mensajes) como el código de estado HTTP correspondiente.
 */
public interface SvcProduct {

	/**
     * Obtiene una lista de productos.
     * Este método se encarga de recuperar todos los productos disponibles y devolverlos
     * en una lista de DtoProductListOut, que contiene la información resumida de cada producto.
     *
     * @return ResponseEntity que envuelve una lista de DtoProductListOut.
     */
	public ResponseEntity<List<DtoProductListOut>> getProducts();
	
	/**
     * Obtiene los detalles de un producto en particular.
     * Se requiere el ID del producto para buscarlo y retornar su información completa en un DtoProductOut.
     *
     * @param id Identificador único del producto.
     * @return ResponseEntity que envuelve un DtoProductOut con los detalles del producto.
     */
	public ResponseEntity<DtoProductOut> getProduct(Integer id);
	
	/**
     * Crea un nuevo producto.
     * Recibe un objeto DtoProductIn con los datos necesarios para la creación del producto,
     * y retorna un ApiResponse que indica el resultado de la operación.
     *
     * @param in Objeto DtoProductIn con los datos de entrada para el nuevo producto.
     * @return ResponseEntity que envuelve un ApiResponse con un mensaje de confirmación o error.
     */
	public ResponseEntity<ApiResponse> createProduct(DtoProductIn in);
	
	/**
     * Actualiza la información de un producto existente.
     * Se requiere el ID del producto a actualizar y un objeto DtoProductIn con los nuevos datos.
     * Devuelve un ApiResponse indicando si la actualización fue exitosa o si ocurrió algún error.
     *
     * @param id Identificador del producto a actualizar.
     * @param in Objeto DtoProductIn con los datos actualizados del producto.
     * @return ResponseEntity que envuelve un ApiResponse con el resultado de la operación.
     */
	public ResponseEntity<ApiResponse> updateProduct(Integer id, DtoProductIn in);
	
	/**
     * Habilita un producto.
     * Este método se utiliza para cambiar el estado de un producto a activo,
     * lo que implica que estará disponible para ser consultado y/o comprado.
     *
     * @param id Identificador del producto a habilitar.
     * @return ResponseEntity que envuelve un ApiResponse indicando el resultado de la operación.
     */
	public ResponseEntity<ApiResponse> enableProduct(Integer id);
	
	/**
     * Deshabilita un producto.
     * Se utiliza para cambiar el estado de un producto a inactivo, 
     * lo que podría significar que el producto ya no está disponible o ha sido descontinuado.
     *
     * @param id Identificador del producto a deshabilitar.
     * @return ResponseEntity que envuelve un ApiResponse indicando el resultado de la operación.
     */
	public ResponseEntity<ApiResponse> disableProduct(Integer id);

}
