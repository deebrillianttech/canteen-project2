package business;

import dataAccess.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import models.FoodModel;
import models.OrderModel;
import models.OrderStatus;

/**
 *
 * @author ataulislam.raihan
 */
public class Order {

    private static final String dbTableName = "cnt_order";
    private static final String relationTableName = "cnt_ordered_food";
    private static final String scheduleTable = "cnt_schedule";
    private static final String scheduleRelationTable = "cnt_scheduled_food";

    public static long Add(models.OrderModel order) throws Exception {
        long orderId;
        try {
            Database db = new Database();

            List<Object> lstParams = new ArrayList();
            lstParams.add(order.getUserPin());
            lstParams.add(order.getSchedule().getId());
            lstParams.add(order.getStatus().toChar());

            // <editor-fold desc="Validation"  defaultstate="collapsed">
            //
            // </editor-fold>
            orderId = db.Update("INSERT INTO " + dbTableName + "(user_pin, schedule_id, status) VALUES(?, ?, ?)", lstParams);

            String foodName = "";
            
            // adding categories
            for (models.FoodModel food : order.getFoods()) {
                //if(food.getServingsLeft() > 0){
                    Database dbFood = new Database();
                    List<Object> lstFoodParams = new ArrayList();
                    lstFoodParams.add(orderId);
                    lstFoodParams.add(food.getId());
                    lstFoodParams.add(food.getPrice());
                    dbFood.Update(
                            "INSERT INTO " + relationTableName
                            + " (order_id, food_id, price) VALUES(?, ?, ?)", lstFoodParams);

                    // reducing servings left from scheduled food
                    Database dbCalc = new Database();
                    List<Object> lstSchParams = new ArrayList();
                    lstSchParams.add(order.getSchedule().getId());
                    lstSchParams.add(food.getId());
                    dbCalc.Update("UPDATE " + scheduleRelationTable
                            + " SET servings_left = (servings_left - 1) WHERE schedule_id = ? AND food_id = ?", lstSchParams);
                //}else{
                //    foodName += food.getTitle() + " ";
                //}
            }
            
            if(foodName != ""){
                throw new Exception("One or more item with title " + foodName + " was not available.");
            }
        } catch (Exception e) {
            throw e;
        }
        return orderId;
    }

    public static List<OrderModel> GetAll(Date date) throws Exception {
        List<OrderModel> lst = new ArrayList();
        try {
            Database db = new Database();
            List<Object> lstParams = new ArrayList<>();
            ResultSet rs;
            if(date == null){
                rs = db.Select("SELECT * FROM " + dbTableName +
                    " INNER JOIN " + scheduleTable + " ON (" + dbTableName + ".schedule_id = " +
                    scheduleTable + ".id) WHERE status = 'o' ORDER BY " + dbTableName + ".id ASC", lstParams);
            }else{
                lstParams.add(date);
                rs = db.Select("SELECT * FROM " + dbTableName +
                    " INNER JOIN " + scheduleTable + " ON (" + dbTableName + ".schedule_id = " +
                    scheduleTable + ".id) WHERE status = 'o' AND " +
                    scheduleTable + ".date = ? ORDER BY " + dbTableName + ".id ASC", lstParams);
            }

            while (rs.next()) {
                OrderModel order = fetchObject(rs);
                lst.add(order);
            }
        } catch (Exception e) {
            throw e;
        }
        return lst;
    }

    public static List<OrderModel> GetAll(int pin, int limit, OrderStatus status) throws Exception {
        List<OrderModel> lst = new ArrayList();
        try {
            Database db = new Database();
            ResultSet rs = null;
            List<Object> lstParam = new ArrayList();
            lstParam.add(pin);
            
            if(status == null){
                lstParam.add(limit);
                rs = db.Select("SELECT * FROM " + dbTableName + " WHERE user_pin = ?::character varying ORDER BY id DESC LIMIT ?", lstParam);
            }else{
                lstParam.add(status.toChar());
                lstParam.add(limit);
                rs = db.Select("SELECT * FROM " + dbTableName + " WHERE user_pin = ?::character varying WHERE status = ? ORDER BY id DESC LIMIT ?", lstParam);
            }
            while (rs.next()) {
                OrderModel order = fetchObject(rs);
                lst.add(order);
            }
        } catch (Exception e) {
            throw e;
        }
        return lst;
    }

    private static OrderModel fetchObject(ResultSet rs) throws SQLException, Exception {
        OrderModel order = new OrderModel();
        order.setId(rs.getInt("id"));
        order.setUserPin(rs.getInt("user_pin"));
        order.setSchedule(Schedule.Get(rs.getInt("schedule_id")));
        OrderStatus status = null;
        String sts = rs.getString("status").trim();

        switch (sts) {
            case "o":
                status = OrderStatus.ORDERED;
                break;
            case "d":
                status = OrderStatus.DELIVERED;
                break;
        }
        order.setStatus(status);
        order.setFoods(Food.GetAllByOrderId((int) order.getId()));

        return order;
    }

    public static OrderModel Get(int id) throws Exception {
        try {
            Database db = new Database();
            List<Object> lstParams = new ArrayList();
            lstParams.add(id);
            ResultSet rs = db.Select("SELECT * FROM " + dbTableName + " WHERE id = ?", lstParams);

            rs.next();

            OrderModel ord = fetchObject(rs);

            return ord;
        } catch (Exception e) {
            throw e;
        }
    }

    public static boolean ChangeStatus(int orderId, OrderStatus status) throws Exception{
        try {
            Database db = new Database();

            List<Object> lstParams = new ArrayList();
            lstParams.add(status.toChar());
            lstParams.add(orderId);

            db.Update("UPDATE " + dbTableName + " SET status = ? WHERE id = ?", lstParams);
        } catch (Exception e) {
            throw e;
        }
        return false;
    }

    public static int Delete(OrderModel order) throws Exception {
        try {
            Database db = new Database();

            List<Object> lstParams = new ArrayList();
            lstParams.add(order.getId());

            db.Update("DELETE FROM " + dbTableName + " WHERE id = ?", lstParams);

            // delete relations with category
            Database dbRel = new Database();
            List<Object> lstParamsDel = new ArrayList();
            lstParamsDel.add(order.getId());
            dbRel.Update("DELETE FROM " + relationTableName + " WHERE order_id = ?", lstParamsDel);

            // adding back servings left in scheduled food
            for (FoodModel food : order.getFoods()) {
                Database dbCalc = new Database();
                List<Object> lstSchParams = new ArrayList();
                lstSchParams.add(order.getSchedule().getId());
                lstSchParams.add(food.getId());
                dbCalc.Update("UPDATE " + scheduleRelationTable
                        + " SET servings_left = (servings_left + 1) WHERE schedule_id = ? AND food_id = ?", lstSchParams);
            }
        } catch (Exception e) {
            throw e;
        }
        return 1;
    }
}
