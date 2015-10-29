package business;

import dataAccess.Database;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.CategoryModel;

/**
 *
 * @author ataulislam.raihan
 */
public class Category {
    private static final String dbTableName = "cnt_category";
    private static final String relationTableName = "cnt_categorize";
    
    public static int Add(models.CategoryModel cat) throws Exception{
        try{
            Database db = new Database();
            
            List<Object> lstParams = new ArrayList();
            lstParams.add(cat.getTitle());
            lstParams.add(cat.getDescription());
            
            // <editor-fold desc="Validation"  defaultstate="collapsed">
            if(cat.getTitle().equals("")){ throw new Exception("Title is required"); }
            // </editor-fold>
            
            db.Update("INSERT INTO " + dbTableName + "(title, description) VALUES(?, ?)", lstParams);
        } catch(Exception e){
            throw e;
        }
        return 1;
    }
    
    public static List<models.CategoryModel> GetAll() throws Exception{
        List<models.CategoryModel> lst = new ArrayList();
        try{
            Database db = new Database();
            List<Object> lstParams = new ArrayList();
            ResultSet rs = db.Select("SELECT * FROM " + dbTableName, lstParams);
            
            while(rs.next()){
                models.CategoryModel cat = new CategoryModel();
                cat.setId(rs.getInt("id"));
                cat.setTitle(rs.getString("title"));
                cat.setDescription(rs.getString("description"));
                
                lst.add(cat);
            }
        } catch(Exception e){
            throw e;
        }
        return lst;
    }
    
    public static List<models.CategoryModel> GetAllByFoodId(int foodId) throws Exception{
        List<models.CategoryModel> lst = new ArrayList();
        try{
            Database db = new Database();
            List<Object> lstParams = new ArrayList();
            lstParams.add(foodId);
            ResultSet rs = db.Select(
                    "SELECT " + dbTableName + ".* FROM " + dbTableName + " RIGHT JOIN " + relationTableName +
                            " ON id = category_id WHERE food_id = ?", lstParams);
            
            while(rs.next()){
                models.CategoryModel cat = new CategoryModel();
                cat.setId(rs.getInt("id"));
                cat.setTitle(rs.getString("title"));
                cat.setDescription(rs.getString("description"));
                
                lst.add(cat);
            }
        } catch(Exception e){
            throw e;
        }
        return lst;
    }
    
    public static models.CategoryModel Get(int id) throws Exception{
        try{
            Database db = new Database();
            List<Object> lstParams = new ArrayList();
            lstParams.add(id);
            ResultSet rs = db.Select("SELECT * FROM " + dbTableName + " WHERE id = ?", lstParams);
            
            rs.next();
            
            models.CategoryModel cat = new CategoryModel();
            cat.setId(rs.getInt("id"));
            cat.setTitle(rs.getString("title"));
            cat.setDescription(rs.getString("description"));

            return cat;
        } catch(Exception e){
            throw e;
        }
    }
    
    public static int Save(models.CategoryModel cat) throws Exception{
        try{
            Database db = new Database();
            
            List<Object> lstParams = new ArrayList();
            lstParams.add(cat.getTitle());
            lstParams.add(cat.getDescription());
            lstParams.add(cat.getId());
            
            // <editor-fold desc="Validation"  defaultstate="collapsed">
            if(cat.getTitle().equals("")){ throw new Exception("Title is required"); }
            // </editor-fold>
            
            db.Update("UPDATE " + dbTableName + " SET title = ?, description = ? WHERE id = ?", lstParams);
        } catch(Exception e){
            throw e;
        }
        return 1;
    }
    
    public static int Delete(models.CategoryModel cat) throws Exception{
        try{
            Database db = new Database();
            
            List<Object> lstParams = new ArrayList();
            lstParams.add(cat.getId());
            
            db.Update("DELETE FROM " + dbTableName + " WHERE id = ?", lstParams);
            
            // delete relations with food
            Database dbRel = new Database();
            List<Object> lstParamsDel = new ArrayList();
            lstParamsDel.add(cat.getId());
            dbRel.Update("DELETE FROM " + relationTableName + " WHERE category_id = ?", lstParamsDel);
        } catch(Exception e){
            throw e;
        }
        return 1;
    }
}
