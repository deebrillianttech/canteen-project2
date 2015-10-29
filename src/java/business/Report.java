package business;

import dataAccess.Database;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ataulislam.raihan
 */
public class Report {
    private static final String orderTable = "cnt_order";
    
    public static List<models.CostModel> GetAllCost(int pin, Date startDate, Date endDate, int limit) throws Exception{
        List<models.CostModel> lst = new ArrayList();
        
        Database db = new Database();
        List<Object> lstParams = new ArrayList();
        lstParams.add(pin);
        
        String limitQry = "";
        String startDateQry = "";
        String endDateQry = "";
        
        if(startDate != null){
            startDateQry = "AND date >= ?";
            lstParams.add(startDate);
        }
        if(endDate != null){
            startDateQry = "AND date <= ?";
            lstParams.add(endDate);
        }
        if(limit != 0){
            limitQry = "LIMIT ?";
            lstParams.add(limit);
        }
        
        String qry = "SELECT order_id, date, SUM(price) AS total_cost FROM " +
            orderTable + " INNER JOIN cnt_schedule ON cnt_schedule.id = " +
            orderTable + ".schedule_id RIGHT JOIN cnt_ordered_food ON " +
            orderTable + ".id = order_id WHERE user_pin = ?::character varying " + startDateQry +
            " " + endDateQry + " GROUP BY order_id, date ORDER BY date DESC " + limitQry;
        
        ResultSet rs = db.Select(qry, lstParams);
        
        while(rs.next()){
            models.CostModel cost = new models.CostModel();
            cost.setOrderId(rs.getInt("order_id"));
            cost.setOrderDate(rs.getDate("date"));
            cost.setTotalCost(rs.getDouble("total_cost"));
            lst.add(cost);
        }            
        
        return lst;
    }
    
    public static models.BalanceModel GetBalance(int pin) throws Exception{
        models.BalanceModel balance = new models.BalanceModel();
        
        Database db = new Database();
        List<Object> lstParams = new ArrayList();
        lstParams.add(pin);
        lstParams.add(pin);
        
        String qry = "select * from\n " +
            "(select sum(price) as cost from " + orderTable + "\n " +
            "right join cnt_ordered_food on " + orderTable + ".id = order_id\n " +
            "where user_pin = ?::character varying) as cost,\n " +
            "(select sum(amount) as paid from cnt_payment where user_pin = ?::character varying) as paid";
                
        ResultSet rs = db.Select(qry, lstParams);
        
        rs.next();
        balance.setCost(rs.getDouble("cost"));
        balance.setPaid(rs.getDouble("paid"));
        
        return balance;
    }
    
    public static Map<String, Object> GetTodaysSalesVsCostForAdmin(Date date) throws Exception{
        Map<String, Object> res = new HashMap<>();
        Database db = new Database();
        List<Object> lstParams = new ArrayList();
        lstParams.add(date);
        lstParams.add(date);
        
        String qry = "select * from \n" +
            "-- total cost for today\n" +
            "(select\n" +
            "	count(cnt_scheduled_food.food_id) as total_item,\n" +
            "	sum(cnt_scheduled_food.total_servings * cnt_food.price) as total_cost\n" +
            "from cnt_schedule\n" +
            "left join cnt_scheduled_food on cnt_schedule.id = cnt_scheduled_food.schedule_id\n" +
            "inner join cnt_food on cnt_scheduled_food.food_id = cnt_food.id\n" +
            "where cnt_schedule.date = ?) as a,\n" +
            "\n" +
            "-- total sales(delivery) for today\n" +
            "(select sum(cnt_ordered_food.price) as total_delivery_price from cnt_schedule\n" +
            "right join cnt_order on cnt_schedule.id = cnt_order.schedule_id\n" +
            "right join cnt_ordered_food on cnt_order.id = cnt_ordered_food.order_id\n" +
            "where cnt_order.status = 'd' and cnt_schedule.date = ?) as b";
                
        ResultSet rs = db.Select(qry, lstParams);
        
        rs.next();
        
        res.put("total_item", rs.getInt("total_item"));
        res.put("total_cost", rs.getDouble("total_cost"));
        res.put("total_delivery_price", rs.getDouble("total_delivery_price"));
        
        return res;
    }
}
