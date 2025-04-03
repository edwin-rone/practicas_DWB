package com.product.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Representa la entidad de la tabla 'product_image' en la base de datos.
 * Cada instancia de esta clase corresponde a un registro en la tabla, 
 * que almacena información sobre las imágenes asociadas a un producto.
 */
@Entity
@Table(name = "product_image")
public class ProductImage {
	/**
     * Identificador único de la imagen del producto.
     * - @Id indica que es la clave primaria.
     * - @GeneratedValue con estrategia IDENTITY permite que la base de datos asigne 
     *   automáticamente un valor incremental.
     * - @Column especifica el nombre real de la columna en la tabla.
     */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_image_id")
	private Integer productImageId;

	/**
     * Identificador del producto al que pertenece esta imagen.
     * Se espera que este valor haga referencia a la clave primaria de la tabla 'product'.
     */
	@Column(name = "product_id")
	private Integer productId;

	/**
     * Ruta o URL de la imagen almacenada.
     * Este campo contiene la información que permite localizar la imagen en el sistema de archivos.
     */
	@Column(name = "image")
	private String image;

	/**
     * Estado de la imagen.
     * Este valor indica si la imagen está activa (1) o inactiva (0).
     */
	@Column(name = "status")
	private Integer status;

	// Getters y Setters

    /**
     * Retorna el identificador único de la imagen del producto.
     * @return productImageId
     */
	public Integer getProductImageId() {
		return productImageId;
	}

	/**
     * Asigna el identificador único de la imagen del producto.
     * @param productImageId El ID de la imagen.
     */
	public void setProductImageId(Integer productImageId) {
		this.productImageId = productImageId;
	}

	/**
     * Retorna el identificador del producto asociado a esta imagen.
     * @return productId
     */
	public Integer getProductId() {
		return productId;
	}

	/**
     * Asigna el identificador del producto para esta imagen.
     * @param productId El ID del producto.
     */
	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	/**
     * Retorna la ruta o URL de la imagen.
     * @return image
     */
	public String getImage() {
		return image;
	}

	/**
     * Asigna la ruta o URL de la imagen.
     * @param image La ruta de la imagen.
     */
	public void setImage(String image) {
		this.image = image;
	}

	/**
     * Retorna el estado de la imagen.
     * @return status
     */
	public Integer getStatus() {
		return status;
	}

	/**
     * Asigna el estado de la imagen.
     * @param status El estado (por ejemplo, 1 para activa, 0 para inactiva).
     */
	public void setStatus(Integer status) {
		this.status = status;
	}
}
