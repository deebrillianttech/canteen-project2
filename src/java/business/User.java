package business;

import dataAccess.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.UserModel;
import models.UserType;

/**
 *
 * @author ataulislam.raihan
 */
public class User {
    private static final String dbTableName = "prmst";
    private static final String passwordTableName = "zxusers";
    private static final String permissionTable = "cnt_permission";
        
    public static List<UserModel> GetAll() throws Exception{
        List<UserModel> lst = new ArrayList();
        try{
            Database db = new Database();
            List<Object> lstParams = new ArrayList();
            ResultSet rs = db.Select("SELECT * FROM " + dbTableName + " INNER JOIN " + passwordTableName + " ON(" + dbTableName + ".xemp = " + passwordTableName + ".xemp) RIGHT JOIN " + permissionTable + " ON " + dbTableName + ".xemp = pin::character varying WHERE " + dbTableName + ".xemp <> 'All'", lstParams);
            
            while(rs.next()){
                UserModel user = fetchObject(rs);
                lst.add(user);
            }
        } catch(Exception e){
            throw e;
        }
        return lst;
    }
    
    public static List<UserModel> GetAllPinAndName() throws Exception{
        List<UserModel> lst = new ArrayList();
        try{
            Database db = new Database();
            List<Object> lstParams = new ArrayList();
            ResultSet rs = db.Select("SELECT * FROM " + dbTableName + " WHERE xemp <> 'All' ORDER BY xemp ASC", lstParams);
            
            while(rs.next()){
                UserModel user = new UserModel();
                user.setFullName(rs.getString("xname"));
                user.setPin(rs.getInt("xemp"));
                lst.add(user);
            }
        } catch(Exception e){
            throw e;
        }
        return lst;
    }

    private static UserModel fetchObject(ResultSet rs) throws SQLException, NumberFormatException, Exception {
        UserModel user = new UserModel();
        user.setFullName(rs.getString("xname"));
        user.setPin(rs.getInt("xemp"));
        user.setEmail(rs.getString("xemail"));
        user.setPassword(rs.getString("xpassword"));
        
        try{
            UserType type = UserType.USER;
            switch(rs.getString("type").trim()){
                case "a":
                    type = UserType.ADMIN;
                    break;
                case "d":
                    type = UserType.DELIVERY_MAN;
                    break;
                default:
                    type = UserType.USER;
                    break;
            }
            user.setType(type);
        }catch(Exception e){
            user.setType(getType(user.getPin()));
        }
        
        return user;
    }
    
    private static models.UserType getType(int pin) throws Exception{
        boolean isAdmin = false;
        boolean isDelivery = false;
        boolean isUser = false;
        
        try{
            Database db = new Database();
            List<Object> lstParams = new ArrayList();
            lstParams.add(pin);
            ResultSet rs = db.Select("SELECT * FROM " + permissionTable + " WHERE pin = ?", lstParams);
            
            
            while(rs.next()){
                switch(rs.getString("type")){
                    case "a":
                        isAdmin = true;
                        break;
                    case "d":
                        isDelivery = true;
                        break;
                    default:
                        isUser = true;
                        break;
                }
            }
        } catch(Exception e){
            throw e;
        }
        
        if(isAdmin)
            return UserType.ADMIN;
        if(isDelivery)
            return UserType.DELIVERY_MAN;

        return UserType.USER;
    }
    
    public static models.UserModel Get(int pin) throws Exception{
        try{
            Database db = new Database();
            List<Object> lstParams = new ArrayList();
            lstParams.add(pin);
            ResultSet rs = db.Select("SELECT " + dbTableName + ".*, " + passwordTableName + ".xpassword FROM " + dbTableName + " INNER JOIN " + passwordTableName + " ON(" + dbTableName + ".xemp = " + passwordTableName + ".xemp) WHERE " + dbTableName + ".xemp = ?::character varying", lstParams);
            
            rs.next();
            UserModel user = fetchObject(rs);
            
            return user;
        } catch(Exception e){
            throw e;
        }
    }
    
    public static int ChangeType(int pin, UserType type) throws Exception{
        try{
            String sType;
            switch(type){
                case ADMIN:
                    sType = "a";
                    break;
                case DELIVERY_MAN:
                    sType = "d";
                    break;
                default:
                    sType = "u";
                    break;
            }
            
            Database db = new Database();
            List<Object> lstParams = new ArrayList();
            lstParams.add(pin);
            lstParams.add(sType);
            db.Update("DELETE FROM " + permissionTable + " WHERE pin = ? AND type = ?", lstParams);
            
            Database db2 = new Database();
            List<Object> lstParams2 = new ArrayList();
            lstParams2.add(pin);
            lstParams2.add(sType);
            db2.Update("INSERT INTO " + permissionTable + "(pin, type) VALUES(?, ?)", lstParams2);
            
        } catch(Exception e){
            throw e;
        }
        return 1;
    }
    
    public static int DeletePermission(int pin, UserType type) throws Exception{
        String sType;
        switch(type){
            case ADMIN:
                sType = "a";
                break;
            case DELIVERY_MAN:
                sType = "d";
                break;
            default:
                sType = "u";
                break;
        }
        
        try{
            Database db = new Database();
            
            List<Object> lstParams = new ArrayList();
            lstParams.add(pin);
            lstParams.add(sType);
            
            db.Update("DELETE FROM " + permissionTable + " WHERE pin = ? AND type = ?", lstParams);
        } catch(Exception e){
            throw e;
        }
        return 1;
    }
}
