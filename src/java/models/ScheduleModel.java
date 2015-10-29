package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ataulislam.raihan
 */
public class ScheduleModel {
    private int id;
    private Date date;
    private List<FoodModel> foods;
    
    public ScheduleModel(){
        this.foods = new ArrayList();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<FoodModel> getFoods() {
        return foods;
    }

    public void setFoods(List<FoodModel> foods) {
        this.foods = foods;
    }
}
