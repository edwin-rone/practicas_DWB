package com.product.api.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.product.api.commons.dto.ApiResponse;
import com.product.api.commons.mapper.MapperProduct;
import com.product.api.dto.in.DtoProductIn;
import com.product.api.dto.out.DtoProductListOut;
import com.product.api.dto.out.DtoProductOut;
import com.product.api.entity.Product;
import com.product.api.entity.ProductImage;
import com.product.api.repository.RepoProduct;
import com.product.api.repository.RepoProductImage;
import com.product.exception.ApiException;
import com.product.exception.DBAccessException;


/**
 * Implementación del servicio para la gestión de productos.
 * 
 * Esta clase implementa la interfaz SvcProduct y define la lógica de negocio para:
 * - Obtener la lista de productos
 * - Obtener los detalles de un producto, incluyendo sus imágenes (leyendo archivos del sistema)
 * - Crear y actualizar productos
 * - Habilitar o deshabilitar un producto
 * 
 * Se utiliza MapperProduct para transformar DTOs a entidades y viceversa,
 * y se inyectan dos repositorios: uno para Product y otro para ProductImage.
 */@Service
public class SvcProductImp implements SvcProduct{
	
	// Repositorio para la entidad Product
	 @Autowired
	RepoProduct repo;
	
	// Repositorio para la entidad ProductImage, para trabajar con imágenes asociadas a productos
	@Autowired
	RepoProductImage repoProductImage;
	
	// Mapper para convertir entre DTOs y entidades de producto
	@Autowired
	MapperProduct mapper;
	
	// Directorio base definido en application.properties para subir archivos
    @Value("${app.upload.dir}")
	private String uploadDir;

    /**
     * Obtiene la lista de todos los productos.
     * Utiliza el repositorio para obtener todas las entidades Product y luego
     * utiliza el mapper para transformarlas en una lista de DtoProductListOut.
     *
     * @return ResponseEntity que envuelve una lista de DtoProductListOut y el código de estado HTTP 200 OK.
     */
    @Override
	public ResponseEntity<List<DtoProductListOut>> getProducts() {
		try {
			List<Product> products = repo.findAll();
			// Obtiene todos los productos de la base de datos
            return new ResponseEntity<>(mapper.fromProductList(products), HttpStatus.OK);
		}catch (DataAccessException e) {
			throw new DBAccessException(e);// En caso de error en la capa de datos, lanza excepción personalizada
		}
	}

    /**
     * Obtiene los detalles de un producto específico, incluyendo la lectura de imágenes asociadas.
     *
     * @param id Identificador del producto.
     * @return ResponseEntity que envuelve un DtoProductOut con los detalles del producto y las imágenes en formato Base64.
     */
	@Override
	public ResponseEntity<DtoProductOut> getProduct(Integer id) {
		try {
			// Verifica que el producto exista, de lo contrario lanza excepción
			validateProductId(id);
			// Obtiene el producto (transformado a DTO) mediante el mapper
			DtoProductOut product = repo.getProduct(id);
			// Lee las imágenes del producto del sistema de archivos
			// Asigna la lista de imágenes al DTO del producto
			List<String> image = readProductImageFile(id);
			
            product.setImage(image);
			
			return new ResponseEntity<>(product, HttpStatus.OK);
		}catch (DataAccessException e) {
			throw new DBAccessException(e);
		}
	}
	
	/**
     * Lee los archivos de imagen asociados a un producto y los retorna en formato Base64.
     *
     * @param customer_id Identificador del producto.
     * @return Lista de cadenas con la imagen en Base64 para cada registro.
     */
	private List<String> readProductImageFile(Integer customer_id) {
	    try {
	    	// Obtiene la lista de ProductImage asociados al producto
            List<ProductImage> productImage = repoProductImage.findByProductId(customer_id);
            // Si la lista es null, retorna una lista vacía mutable
            if(productImage == null)
				return new ArrayList<>();
			
            // Lista para almacenar las imágenes en formato Base64
            List<String> imageUrls = new ArrayList<>();
			
            // Itera sobre cada imagen encontrada
            for (ProductImage pi : productImage) {
			    String imageUrl = pi.getImage();
			    // Si la URL comienza con "/", se elimina ese carácter para obtener la ruta relativa
                if (imageUrl.startsWith("/")) {
			       	    imageUrl = imageUrl.substring(1);
			   	}
                // Construye el path absoluto utilizando el directorio de subida y la ruta de la imagen
               	Path imagePath = Paths.get(uploadDir, imageUrl);
			  
               	// Verifica si el archivo existe; si no existe, agrega una cadena vacía
                if (!Files.exists(imagePath))
			   		imageUrls.add("");
                // Lee los bytes del archivo de imagen
                byte[] imageBytes = Files.readAllBytes(imagePath);
                // Codifica los bytes a Base64 y los agrega a la lista
                imageUrls.add(Base64.getEncoder().encodeToString(imageBytes));
			}

			return imageUrls;
	    
	    }catch (DataAccessException e) {
	    	throw new DBAccessException(e);
	    }catch (IOException e) {
	    	throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al leer el archivo");
	    }
	}
	

	/**
     * Crea un nuevo producto.
     * Utiliza el mapper para convertir el DTO de entrada a una entidad Product y la guarda en la base de datos.
     * Controla errores de integridad como duplicados o llaves foráneas.
     *
     * @param in Objeto DtoProductIn con los datos del nuevo producto.
     * @return ResponseEntity que envuelve un ApiResponse con un mensaje de confirmación y el código 201 CREATED.
     */
	@Override
	public ResponseEntity<ApiResponse> createProduct(DtoProductIn in) {
		try {
			Product product = mapper.fromDto(in);
			repo.save(product);
			return new ResponseEntity<>(new ApiResponse("El producto ha sido registrado"), HttpStatus.CREATED);
		}catch (DataAccessException e) {
			if (e.getLocalizedMessage().contains("ux_product_gtin"))
				throw new ApiException(HttpStatus.CONFLICT, "El gtin del producto ya está registrado");
			if (e.getLocalizedMessage().contains("ux_product_product"))
				throw new ApiException(HttpStatus.CONFLICT, "El nombre del producto ya está registrado");
			if (e.getLocalizedMessage().contains("fk_product_category"))
				throw new ApiException(HttpStatus.NOT_FOUND, "El id de categoría no existe");

			throw new DBAccessException(e);
		}
	}

	/**
     * Actualiza los datos de un producto existente.
     * Valida que el producto exista, convierte el DTO a entidad (incluyendo el ID) y actualiza el registro.
     *
     * @param id Identificador del producto a actualizar.
     * @param in Objeto DtoProductIn con los nuevos datos.
     * @return ResponseEntity que envuelve un ApiResponse con un mensaje de confirmación.
     */
	@Override
	public ResponseEntity<ApiResponse> updateProduct(Integer id, DtoProductIn in) {
		try {
			validateProductId(id);
			Product product = mapper.fromDto(id, in);
			repo.save(product);
			return new ResponseEntity<>(new ApiResponse("El producto ha sido actualizado"), HttpStatus.OK);
		}catch (DataAccessException e) {
			if (e.getLocalizedMessage().contains("ux_product_gtin"))
				throw new ApiException(HttpStatus.CONFLICT, "El gtin del producto ya está registrado");
			if (e.getLocalizedMessage().contains("ux_product_product"))
				throw new ApiException(HttpStatus.CONFLICT, "El nombre del producto ya está registrado");
			if (e.getLocalizedMessage().contains("fk_product_category"))
				throw new ApiException(HttpStatus.NOT_FOUND, "El id de categoría no existe");

			throw new DBAccessException(e);
		}
	}

	/**
     * Habilita (activa) un producto.
     * Valida que el producto exista, cambia su estado a 1 y lo guarda en la base de datos.
     *
     * @param id Identificador del producto a habilitar.
     * @return ResponseEntity que envuelve un ApiResponse con un mensaje de confirmación.
     */
	@Override
	public ResponseEntity<ApiResponse> enableProduct(Integer id) {
		try {
			validateProductId(id);
			Product product = repo.findById(id).get();
			product.setStatus(1);
			repo.save(product);
			return new ResponseEntity<>(new ApiResponse("El producto ha sido activado"), HttpStatus.OK);
		}catch (DataAccessException e) {
			throw new DBAccessException(e);
		}
	}

	/**
     * Deshabilita (desactiva) un producto.
     * Valida que el producto exista, cambia su estado a 0 y actualiza el registro en la base de datos.
     *
     * @param id Identificador del producto a deshabilitar.
     * @return ResponseEntity que envuelve un ApiResponse con un mensaje confirmando la desactivación.
     */
	@Override
	public ResponseEntity<ApiResponse> disableProduct(Integer id) {
		try {
			validateProductId(id);
			Product product = repo.findById(id).get();
			product.setStatus(0);
			repo.save(product);
			return new ResponseEntity<>(new ApiResponse("El producto ha sido desactivado"), HttpStatus.OK);
		}catch (DataAccessException e) {
			throw new DBAccessException(e);
		}
	}
	
	/**
     * Valida que el producto con el ID proporcionado exista en la base de datos.
     * Si no existe, lanza una ApiException con estado 404 (Not Found).
     *
     * @param id Identificador del producto a validar.
     */
	private void validateProductId(Integer id) {
		try {
			if(repo.findById(id).isEmpty()) {
				throw new ApiException(HttpStatus.NOT_FOUND, "El id del producto no existe");
			}
		}catch (DataAccessException e) {
			throw new DBAccessException(e);
		}
	}

}
