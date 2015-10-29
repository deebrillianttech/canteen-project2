package business;

import dataAccess.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import models.CategoryModel;
import models.FoodModel;

/**
 *
 * @author ataulislam.raihan
 */
public class Food {
    private static final String dbTableName = "cnt_food";
    private static final String relationTableName = "cnt_categorize";
    private static final String scheduleTable = "cnt_schedule";
    private static final String relationScheduleTable = "cnt_scheduled_food";
    private static final String orderTable = "cnt_order";
    private static final String relationOrderTable = "cnt_ordered_food";
    
    public static int Add(models.FoodModel food) throws Exception{
        try{
            Database db = new Database();
            
            List<Object> lstParams = new ArrayList();
            lstParams.add(food.getTitle());
            lstParams.add(food.getDescription());
            lstParams.add(food.getPrice());
            lstParams.add(food.getImage());
            
            // <editor-fold desc="Validation"  defaultstate="collapsed">
            if(food.getTitle().equals("")){ throw new Exception("Title is required"); }
            // </editor-fold>
            
            db.Update("INSERT INTO " + dbTableName + "(title, description, price, image) VALUES(?, ?, ?, ?)", lstParams);
            
            // adding categories
            for (CategoryModel cat : food.getCategories()) {
                Database dbCat = new Database();
                List<Object> lstCatParams = new ArrayList();
                lstCatParams.add(food.getTitle());
                lstCatParams.add(cat.getId());
                dbCat.Update(
                        "INSERT INTO " + relationTableName +
                        " (food_id, category_id) VALUES((SELECT id FROM " + dbTableName +
                        " WHERE title = ?),?)", lstCatParams);
            }
        } catch(Exception e){
            throw e;
        }
        return 1;
    }
    
    public static List<models.FoodModel> GetAll() throws Exception{
        List<models.FoodModel> lst = new ArrayList();
        try{
            Database db = new Database();
            List<Object> lstParams = new ArrayList();
            ResultSet rs = db.Select("SELECT * FROM " + dbTableName, lstParams);
            
            while(rs.next()){
                FoodModel food = fetchObject(rs);
                lst.add(food);
            }
        } catch(Exception e){
            throw e;
        }
        return lst;
    }
    
    public static List<models.FoodModel> GetAll(Boolean isActive) throws Exception{
        List<models.FoodModel> lst = new ArrayList();
        try{
            Database db = new Database();
            List<Object> lstParams = new ArrayList();
            lstParams.add(isActive);
            ResultSet rs = db.Select("SELECT * FROM " + dbTableName + " WHERE is_active = ? ORDER BY title ASC", lstParams);
            
            while(rs.next()){
                FoodModel food = fetchObject(rs);
                lst.add(food);
            }
        } catch(Exception e){
            throw e;
        }
        return lst;
    }
    
    public static List<models.FoodModel> GetAllTodaysMenu() throws Exception{
        List<models.FoodModel> lst = new ArrayList();
        try{
            Database db = new Database();
            List<Object> lstParams = new ArrayList();
            lstParams.add(new Date());
            ResultSet rs = db.Select(
                    "SELECT " + dbTableName + ".*, " + relationScheduleTable + ".servings_left FROM " + dbTableName + " \n" +
                    "LEFT JOIN " + relationScheduleTable + " ON " + dbTableName + ".id = " + relationScheduleTable + ".food_id \n" +
                    "LEFT JOIN " + scheduleTable + " ON " + scheduleTable + ".id = " + relationScheduleTable + ".schedule_id \n" +
                    "WHERE " + scheduleTable + ".date = ? \n" +
                    "AND " + dbTableName + ".is_active = TRUE ORDER BY title ASC", lstParams);
            
            while(rs.next()){
                FoodModel food = fetchObject(rs);
                lst.add(food);
            }
        } catch(Exception e){
            throw e;
        }
        return lst;
    }
    
    public static List<models.FoodModel> GetAllByScheduleId(int scheduleId) throws Exception{
        List<models.FoodModel> lst = new ArrayList();
        try{
            Database db = new Database();
            List<Object> lstParams = new ArrayList();
            lstParams.add(scheduleId);
            ResultSet rs = db.Select("SELECT " + dbTableName + ".*, " + relationScheduleTable + ".servings_left FROM " + dbTableName +
                    " RIGHT JOIN " + relationScheduleTable + " ON id = food_id WHERE schedule_id = ?", lstParams);
            
            while(rs.next()){
                FoodModel food = fetchObject(rs);
                lst.add(food);
            }
        } catch(Exception e){
            throw e;
        }
        return lst;
    }
    
    public static List<models.FoodModel> GetAllByOrderId(int orderId) throws Exception{
        List<models.FoodModel> lst = new ArrayList();
        try{
            Database db = new Database();
            List<Object> lstParams = new ArrayList();
            lstParams.add(orderId);
            ResultSet rs = db.Select("select " + dbTableName + ".* from " + orderTable + "\n" +
                    "	left join cnt_schedule on " + orderTable + ".schedule_id = cnt_schedule.id\n" +
                    "	right join " + relationOrderTable + " on " + orderTable + ".id = " + relationOrderTable + ".order_id\n" +
                    "	inner join " + dbTableName + " on " + dbTableName + ".id = " + relationOrderTable + ".food_id\n" +
                    "where " + orderTable + ".id = ?", lstParams);
            
            while(rs.next()){
                FoodModel food = fetchObject(rs);
                lst.add(food);
            }
        } catch(Exception e){
            throw e;
        }
        return lst;
    }

    private static FoodModel fetchObject(ResultSet rs) throws SQLException, Exception {
        models.FoodModel food = new FoodModel();
        food.setId(rs.getInt("id"));
        food.setTitle(rs.getString("title"));
        food.setDescription(rs.getString("description"));
        food.setCategories(Category.GetAllByFoodId(food.getId()));
        food.setIsActive(rs.getBoolean("is_active"));
        food.setPrice(rs.getDouble("price"));
        food.setImage(rs.getString("image"));
        
        try{
            food.setServingsLeft(rs.getInt("servings_left"));
        }catch(Exception e){
            //throw(e);
        }
        
        return food;
    }
    
    public static models.FoodModel Get(int id) throws Exception{
        try{
            Database db = new Database();
            List<Object> lstParams = new ArrayList();
            lstParams.add(id);
            ResultSet rs = db.Select("SELECT * FROM " + dbTableName + " WHERE id = ?", lstParams);
            
            rs.next();
            
            models.FoodModel food = fetchObject(rs);
            
            return food;
        } catch(Exception e){
            throw e;
        }
    }
    
    public static int Save(models.FoodModel food) throws Exception{
        try{
            Database db = new Database();
            
            List<Object> lstParams = new ArrayList();
            lstParams.add(food.getTitle());
            lstParams.add(food.getDescription());
            lstParams.add(food.getIsActive());
            lstParams.add(food.getPrice());
            lstParams.add(food.getImage());
            lstParams.add(food.getId());
            
            // <editor-fold desc="Validation"  defaultstate="collapsed">
            if(food.getTitle().equals("")){ throw new Exception("Title is required"); }
            // </editor-fold>
            
            db.Update("UPDATE " + dbTableName + " SET title = ?, description = ?, is_active = ?, price = ?, image = ? WHERE id = ?", lstParams);
            
            // updating categories
            Database dbDelCat = new Database();
            List<Object> lstCatDel = new ArrayList();
            lstCatDel.add(food.getId());
            dbDelCat.Update("DELETE FROM " + relationTableName + " WHERE food_id = ?", lstCatDel);
            
            for (CategoryModel cat : food.getCategories()) {
                Database dbCat = new Database();
                List<Object> lstCatParams = new ArrayList();
                lstCatParams.add(food.getTitle());
                lstCatParams.add(cat.getId());
                dbCat.Update(
                        "INSERT INTO " + relationTableName +
                        " (food_id, category_id) VALUES((SELECT id FROM " + dbTableName +
                        " WHERE title = ?),?)", lstCatParams);
            }
        } catch(Exception e){
            throw e;
        }
        return 1;
    }
    
    public static int Delete(models.FoodModel food) throws Exception{
        try{
            Database db = new Database();
            
            List<Object> lstParams = new ArrayList();
            lstParams.add(food.getId());
            
            db.Update("DELETE FROM " + dbTableName + " WHERE id = ?", lstParams);
            
            // delete relations with category
            Database dbRel = new Database();
            List<Object> lstParamsDel = new ArrayList();
            lstParamsDel.add(food.getId());
            dbRel.Update("DELETE FROM " + relationTableName + " WHERE food_id = ?", lstParamsDel);
        } catch(Exception e){
            throw e;
        }
        return 1;
    }
}
