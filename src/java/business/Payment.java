package business;

import dataAccess.Database;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.PaymentModel;

/**
 *
 * @author ataulislam.raihan
 */
public class Payment {
    private static final String dbTableName = "cnt_payment";
    
    public static int Add(PaymentModel payment) throws Exception{
        try{
            Database db = new Database();
            
            List<Object> lstParams = new ArrayList();
            lstParams.add(payment.getUserPin());
            lstParams.add(payment.getAmount());
            lstParams.add(payment.getOrder().getId());
            lstParams.add(payment.getComments());
            
            // <editor-fold desc="Validation"  defaultstate="collapsed">
            if(payment.getUserPin() < 1){ throw new Exception("PIN is required"); }
            // </editor-fold>
            
            db.Update("INSERT INTO " + dbTableName + "(user_pin, amount, order_id, comments) VALUES(?::character varying, ?, ?, ?)", lstParams);
        } catch(Exception e){
            throw e;
        }
        return 1;
    }
    
    public static List<models.PaymentModel> GetAll(int userPin, int limit) throws Exception{
        List<models.PaymentModel> lst = new ArrayList();
        try{
            Database db = new Database();
            List<Object> lstParams = new ArrayList();
            lstParams.add(userPin);
            lstParams.add(limit);
            ResultSet rs = db.Select("SELECT * FROM " + dbTableName + " WHERE user_pin = ?::character varying LIMIT ?", lstParams);
            
            while(rs.next()){
                models.PaymentModel pmt = new PaymentModel();
                pmt.setId(rs.getInt("id"));
                pmt.setUserPin(Integer.parseInt(rs.getString("user_pin")));
                pmt.setDate(rs.getDate("date"));
                pmt.setAmount(rs.getDouble("amount"));
                pmt.setOrder(Order.Get(rs.getInt("order_id")));
                pmt.setComments(rs.getString("comments"));
                
                lst.add(pmt);
            }
        } catch(Exception e){
            throw e;
        }
        return lst;
    }
}
