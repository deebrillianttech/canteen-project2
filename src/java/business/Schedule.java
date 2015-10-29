package business;

import dataAccess.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import models.ScheduleModel;

/**
 *
 * @author ataulislam.raihan
 */
public class Schedule {
    private static final String dbTableName = "cnt_schedule";
    private static final String relationTableName = "cnt_scheduled_food";
    
    public static int Add(models.ScheduleModel schedule) throws Exception{
        try{
            Database db = new Database();
            
            List<Object> lstParams = new ArrayList();
            lstParams.add(schedule.getDate());
            
            // <editor-fold desc="Validation"  defaultstate="collapsed">
            //
            // </editor-fold>
            
            db.Update("INSERT INTO " + dbTableName + "(date) VALUES(?)", lstParams);
            
            // adding categories
            for (models.FoodModel food : schedule.getFoods()) {
                Database dbFood = new Database();
                List<Object> lstFoodParams = new ArrayList();
                lstFoodParams.add(schedule.getDate());
                lstFoodParams.add(food.getId());
                lstFoodParams.add(food.getServingsLeft());
                lstFoodParams.add(food.getServingsLeft()); // total servings
                dbFood.Update(
                        "INSERT INTO " + relationTableName +
                        " (schedule_id, food_id, servings_left, total_servings) VALUES((SELECT id FROM " + dbTableName +
                        " WHERE date = ?),?, ?, ?)", lstFoodParams);
            }
        } catch(Exception e){
            throw e;
        }
        return 1;
    }
    
    public static List<ScheduleModel> GetAll() throws Exception{
        List<ScheduleModel> lst = new ArrayList();
        try{
            Database db = new Database();
            ResultSet rs = db.Select("SELECT * FROM " + dbTableName + " ORDER BY date DESC LIMIT 31", new ArrayList());
            
            while(rs.next()){
                ScheduleModel schedule = fetchObject(rs);
                lst.add(schedule);
            }
        } catch(Exception e){
            throw e;
        }
        return lst;
    }
    
    private static ScheduleModel fetchObject(ResultSet rs) throws SQLException, Exception {
        ScheduleModel schedule = new ScheduleModel();
        schedule.setId(rs.getInt("id"));
        schedule.setDate(rs.getDate("date"));
        schedule.setFoods(Food.GetAllByScheduleId(schedule.getId()));
        
        return schedule;
    }
    
    public static ScheduleModel Get(int id) throws Exception{
        try{
            Database db = new Database();
            List<Object> lstParams = new ArrayList();
            lstParams.add(id);
            ResultSet rs = db.Select("SELECT * FROM " + dbTableName + " WHERE id = ?", lstParams);
            
            rs.next();
            
            ScheduleModel sch = fetchObject(rs);
            
            return sch;
        } catch(Exception e){
            throw e;
        }
    }
    
    public static int GetIdByDate(Date date) throws Exception{
        try{
            Database db = new Database();
            List<Object> lstParams = new ArrayList();
            lstParams.add(date);
            ResultSet rs = db.Select("SELECT id FROM " + dbTableName + " WHERE date = ?", lstParams);
            
            rs.next();
            
            return rs.getInt("id");
        } catch(Exception e){
            throw e;
        }
    }
    
    public static int Save(ScheduleModel schedule) throws Exception{
        try{
            Database db = new Database();
            
            List<Object> lstParams = new ArrayList();
            lstParams.add(schedule.getDate());
            lstParams.add(schedule.getId());
            
            // <editor-fold desc="Validation"  defaultstate="collapsed">
            
            // </editor-fold>
            
            db.Update("UPDATE " + dbTableName + " SET date = ? WHERE id = ?", lstParams);
            
            // updating categories
            Database dbDelSch = new Database();
            List<Object> lstSchDel = new ArrayList();
            lstSchDel.add(schedule.getId());
            dbDelSch.Update("DELETE FROM " + relationTableName + " WHERE schedule_id = ?", lstSchDel);
            
            for (models.FoodModel food : schedule.getFoods()) {
                Database dbSch = new Database();
                List<Object> lstSchParams = new ArrayList();
                lstSchParams.add(schedule.getDate());
                lstSchParams.add(food.getId());
                lstSchParams.add(food.getServingsLeft());
                lstSchParams.add(food.getServingsLeft()); // total servings
                dbSch.Update(
                        "INSERT INTO " + relationTableName +
                        " (schedule_id, food_id, servings_left, total_servings) VALUES((SELECT id FROM " + dbTableName +
                        " WHERE date = ?), ?, ?, ?)", lstSchParams);
            }
        } catch(Exception e){
            throw e;
        }
        return 1;
    }
    
    public static int Delete(ScheduleModel sch) throws Exception{
        try{
            Database db = new Database();
            
            List<Object> lstParams = new ArrayList();
            lstParams.add(sch.getId());
            
            db.Update("DELETE FROM " + dbTableName + " WHERE id = ?", lstParams);
            
            // delete relations with category
            Database dbRel = new Database();
            List<Object> lstParamsDel = new ArrayList();
            lstParamsDel.add(sch.getId());
            dbRel.Update("DELETE FROM " + relationTableName + " WHERE schedule_id = ?", lstParamsDel);
        } catch(Exception e){
            throw e;
        }
        return 1;
    }
    
    public static String GetCalendarData(){
        StringBuilder sb = new StringBuilder();
        try{
                List<ScheduleModel> allSchedules = Schedule.GetAll();
                int size = allSchedules.size();
                
                sb.append("{");
                int i = 0;
                for (ScheduleModel schedule : allSchedules) {
                    i++;
                    sb.append("'").append(schedule.getDate()).append("'");
                    sb.append(":{'number': ").append(schedule.getFoods().size()).append(", 'url': ''}");
                    
                    if(i < size) {
                        sb.append(",");
                    }else{
                        sb.append("");
                    }
                }
                sb.append("}");
            } catch(Exception e){
                
            }
        return sb.toString();
    }
}
