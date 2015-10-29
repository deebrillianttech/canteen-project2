package models;

import java.util.List;

/**
 *
 * @author ataulislam.raihan
 */
public class OrderModel {
    private long id;
    private int userPin;
    private ScheduleModel schedule;
    private OrderStatus status;
    private List<FoodModel> foods;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUserPin() {
        return userPin;
    }

    public void setUserPin(int userPin) {
        this.userPin = userPin;
    }

    public ScheduleModel getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleModel schedule) {
        this.schedule = schedule;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<FoodModel> getFoods() {
        return foods;
    }

    public void setFoods(List<FoodModel> foods) {
        this.foods = foods;
    }
    
    public double getTotalPrice(){
        double price = 0.0;
        
        for (FoodModel food : foods) {
            price += food.getPrice();
        }
        
        return price;
    }
}
