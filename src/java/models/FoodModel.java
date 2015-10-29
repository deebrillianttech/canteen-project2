package models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ataulislam.raihan
 */
public class FoodModel {
    private int id;
    private String title;
    private String description;
    private List<CategoryModel> categories;
    private Boolean isActive;
    private double price;
    private int servingsLeft;
    private String image;

    public int getServingsLeft() {
        return servingsLeft;
    }

    public void setServingsLeft(int servingsLeft) {
        this.servingsLeft = servingsLeft;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public List<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryModel> categories) {
        this.categories = categories;
    }
    
    public FoodModel(){
        this.categories = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
}
