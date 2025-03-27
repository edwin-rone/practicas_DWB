package com.product.api.dto.in;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

/**
 * Objeto de Transferencia de Datos (DTO) para recibir la información de la imagen de un producto.
 * Se utiliza en el endpoint de carga de imagen para asegurar que se proporcionen los datos necesarios.
 */
public class DtoProductImageIn {
	
	/**
     * Identificador del producto al que pertenece la imagen.
     * 
     * @JsonProperty("product_id") indica que en el JSON la propiedad se llamará "product_id".
     * @NotNull asegura que este campo no sea nulo y, en caso de serlo, lanza un error de validación con el mensaje indicado.
     */
	@JsonProperty("product_id")
	@NotNull(message="El Id del producto es obligatorio")
	private Integer productId;

	/**
     * La cadena de la imagen, la cual está en formato Base64.
     * 
     * @JsonProperty("image") indica que en el JSON la propiedad se llamará "image".
     * @NotNull asegura que se proporcione la imagen; de lo contrario, se mostrará el mensaje de error.
     */
	@JsonProperty("image")
	@NotNull(message="La imagen es obligatoria")
	private String image;

	// Métodos getter y setter para acceder y modificar productId

    /**
     * Retorna el identificador del producto.
     * @return productId
     */
	public Integer getProductId() {
		return productId;
	}

	/**
     * Asigna el identificador del producto.
     * @param productId El ID del producto.
     */
	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	// Métodos getter y setter para acceder y modificar image

    /**
     * Retorna la imagen (en formato Base64).
     * @return image
     */
	public String getImage() {
		return image;
	}

	/**
     * Asigna la imagen.
     * @param image La cadena de la imagen.
     */
	public void setImage(String image) {
		this.image = image;
	}
	
	
}
