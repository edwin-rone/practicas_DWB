package com.product.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@JsonPropertyOrder({"categoryId", "category", "tag", "status"})
@Entity
@Table(name = "Category")
public class Category {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("category_id")
	@Column(name="category_id")
    private Integer category_id;
	
	@JsonProperty("category")
	@Column(name="category")
	private String category;
	
	@JsonProperty("tag")
	@Column(name="tag")
    private String tag;
	
	@JsonProperty("status")
	@Column(name="status")
    private Integer status;

    //Constructores
    public Category(){}

    public Category(Integer category_id, String category, String tag, Integer status){
        this.category_id=category_id;
        this.category=category;
        this.tag=tag;
        this.status=status;
    }

    //Geters
    public Integer getCategoryId(){
        return category_id;
    }

    public String getCategory(){
        return category;
    }

    public String getTag(){
        return tag;
    }

    public Integer getStatus(){
        return status;
    }

    //Setters
    public void setCategoryId(Integer category_id){
        this.category_id = category_id;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public void setTag(String tag){
        this.tag = tag;
    }

    public void setStatus(Integer status){
        this.status = status;
    }

    @Override
    public String toString(){
        return "{ID: "+this.getCategoryId()+", Nombre: "+this.getCategory()+", Tag: "+this.getTag()+", Estatus: "+this.getStatus()+"}";
    }

    //Equals
    public boolean equals(Category category){
        if(!(category instanceof Category))
            return false;
        return this.category_id==category.getCategoryId() || this.category.equals(category.getCategory()) || this.tag.equals(category.getTag());
    }
}
