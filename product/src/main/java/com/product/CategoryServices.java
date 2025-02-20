package com.product;

import java.util.ArrayList;
import java.util.List;

public class CategoryServices {
    private List<Category> categories=new ArrayList<Category>();
    private int id=-1;

    public CategoryServices(){}

    public void createCategory(String category, String tag){
        Category temp_category= new Category(this.id+1, category, tag, 1);
        for(Category c:this.categories){
            if(c.equals(temp_category)){
                System.out.println("No se creó la categoría " + category + " porque ya existe");
                return;
            }
        }
        
        this.categories.add(temp_category);
        System.out.println("\nSe creó la categoría " + category + " con éxito");
        this.id++;
    }

    public void getCategories(){
        List<Category> active_Categories = new ArrayList<Category>();
        if(this.categories.isEmpty()){
            System.out.println("\nNo hay categorías registradas");
            return;
        }
        else{
            for(Category c:this.categories){
                if(c.getStatus()==1)
                    active_Categories.add(c);
            }
        }System.out.println("Las categorías registradas son: \n" + active_Categories.toString());
    }
    
    //Método que regresa la lista de categorias activas
    public List<Category> getActiveCategories() {
        List<Category> activeCategories = new ArrayList<>();
        for(Category c : this.categories) {
            if(c.getStatus() == 1) {
                activeCategories.add(c);
            }
        }
        return activeCategories;
    }

    public void deleteCategory(Integer category_id){
        if(this.categories.isEmpty()){
            System.out.println("\nNo hay categorías registradas");
            return;
        }
        else{
            for(Category c:this.categories){
                if (c.getCategoryId()==category_id){
                    c.setStatus(0);
                    System.out.println("se eliminó la categoría " + category_id + " con éxito");
                    return;
                }
            }
            System.out.println("No existe una categoría con el ID:" + category_id);
        }
    }
}
