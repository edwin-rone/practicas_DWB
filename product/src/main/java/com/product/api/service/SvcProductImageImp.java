package com.product.api.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.product.api.dto.in.DtoProductImageIn;
import com.product.api.entity.ProductImage;
import com.product.api.repository.RepoProductImage;
import com.product.commons.dto.ApiResponse;
import com.product.exception.ApiException;
import com.product.exception.DBAccessException;


/**
 * Servicio para gestionar la subida y desactivación de imágenes de productos.
 */
@Service
public class SvcProductImageImp implements SvcProductImage {
	
	 /**
     * Repositorio para acceder a la tabla product_image en la base de datos.
     */
	@Autowired
	RepoProductImage repo;
	
	/**
     * Directorio base (definido en application.properties)
     * donde se guardarán los archivos de imagen.
     */
	@Value("${app.upload.dir}")
	private String uploadDir;
	
	/**
     * Sube una imagen para un producto, decodificando su contenido Base64,
     * guardándola en el sistema de archivos y registrando la ruta en la base de datos.
     *
     * @param in Objeto con los datos de la imagen en Base64 y el ID del producto.
     * @return ResponseEntity con un ApiResponse indicando éxito o error.
     */
	@Override
	public ResponseEntity<ApiResponse> uploadProductImage(DtoProductImageIn in) {
		try {
			// Si la cadena de imagen contiene un prefijo como "data:image/png;base64,",
            // se elimina esa parte para quedarnos únicamente con la porción Base64.
			if (in.getImage().startsWith("data:image")) {
			int commaIndex = in.getImage().indexOf(",");
				if (commaIndex != -1) {
					in.setImage(in.getImage().substring(commaIndex + 1));
				}
			}
			
			// Decodifica la cadena Base64 a bytes
			byte[] imageBytes = Base64.getDecoder().decode(in.getImage());

			// Genera un nombre único para la imagen (se asume extensión PNG)
			String fileName = UUID.randomUUID().toString() + ".png";

			// Construye la ruta completa donde se guardará la imagen
			Path imagePath = Paths.get(uploadDir, "img", "product", fileName);
		    
			// Asegurarse de que el directorio exista
			Files.createDirectories(imagePath.getParent());

			// Escribir el archivo en el sistema de archivos
			Files.write(imagePath, imageBytes);
			
			//No es necesario verificar si el producto ya tiene una imagen porque puede tener más de una imagen
			// Crear la entidad CustomerImage y guardar la URL en la base de datos
			ProductImage productImage = new ProductImage();
			productImage.setProductId(in.getProductId());
			productImage.setImage("img/product/" + fileName);
			productImage.setStatus(1); 

			// Guardar la ruta de la imagen
			repo.save(productImage);
			
			// Retorna una respuesta exitosa con un mensaje.
		    return new ResponseEntity<>(new ApiResponse("La imagen del producto ha sido agregada"), HttpStatus.OK);
		}catch (DataAccessException e) {
			// Si ocurre un error al acceder a la base de datos, se lanza una excepción personalizada.
		    throw new DBAccessException(e);
		}catch (IOException e) {
			// Si ocurre un error al escribir el archivo, se lanza una excepción personalizada.
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el archivo");
		}
	}
	
	/**
     * Desactiva (pone en estado 0) la imagen de un producto dada por su ID.
     *
     * @param id Identificador de la imagen que se desea desactivar.
     * @return ResponseEntity con un ApiResponse indicando éxito o error.
     */
	@Override
	public ResponseEntity<ApiResponse> disableProductImage(Integer id) {
		try {
			// Verifica que el ID de la imagen exista.
			validateProductImageId(id);
			
			// Recupera la imagen y actualiza su estado a 0 (desactivada).
			ProductImage productImage = repo.findById(id).get();
			productImage.setStatus(0);
			repo.save(productImage);
			
			// Retorna una respuesta exitosa con un mensaje.
			return new ResponseEntity<>(new ApiResponse("La imágen ha sido desactivada"), HttpStatus.OK);
		}catch (DataAccessException e) {
			// Maneja los errores de acceso a la base de datos.
			throw new DBAccessException(e);
		}
	}
	
	/**
     * Valida que el ID de la imagen exista en la base de datos.
     * Si no existe, lanza una ApiException con estado 404 (Not Found).
     *
     * @param id Identificador de la imagen a validar.
     */
	private void validateProductImageId(Integer id) {
		try {
			// Verifica si la imagen existe en el repositorio.
			if(repo.findById(id).isEmpty()) {
				throw new ApiException(HttpStatus.NOT_FOUND, "El id de la imágen no existe");
			}
		}catch (DataAccessException e) {
			// Si ocurre un error al acceder a la base de datos, se lanza una excepción.
			throw new DBAccessException(e);
		}
	}
}
