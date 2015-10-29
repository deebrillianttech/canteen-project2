package models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ataulislam.raihan
 */
public class TrayModel {
    private List<FoodModel> foods;
    
    public TrayModel(){
        this.foods = new ArrayList();
    }

    public List<FoodModel> getFoods() {
        return foods;
    }

    public void setFoods(List<FoodModel> foods) {
        this.foods = foods;
    }
    
    public void addFood(FoodModel food){
        this.foods.add(food);
    }
    
    public void removeFood(FoodModel food){
        this.foods.remove(food);
    }
    
    public double getTotalPrice(){
        double price = 0.0;
        
        for (FoodModel food : foods) {
            price += food.getPrice();
        }
        
        return price;
    }
}
