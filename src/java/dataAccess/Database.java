package dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import static java.sql.Types.NULL;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ataulislam.raihan
 */
public class Database {
    private Connection con;

    public Database() throws Exception {
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/da", "postgres", "sa");
        } catch (Exception e) {
            throw e;
        }
    }
    
    private void setParameters(PreparedStatement ps, List<Object> params) throws Exception{
        int i = 1;
        for(Object p : params){
            if(p instanceof String){
                ps.setString(i, (String)p);
            }else if(p instanceof Integer){
                ps.setInt(i, (int)p);
            }else if(p instanceof Long){
                ps.setLong(i, (Long)p);
            }else if(p instanceof Date){
                ps.setDate(i, new java.sql.Date(((Date)p).getTime()));
            }else if(p instanceof Boolean){
                ps.setBoolean(i, (Boolean)p);
            }else if(p instanceof Double){
                ps.setDouble(i, (Double)p);
            }else if(p instanceof Character){
                ps.setString(i, "" + p);
            }else if(p == null){
                ps.setNull(i, NULL);
            }else{
                throw new Exception("Parameter type not supported.");
            }
            
            i++;
        }
    }
    
    /**
     *
     * @param query
     * @param params
     * @return
     * @throws Exception
     */
    public ResultSet Select(String query, List<Object> params) throws Exception{
        try {
            PreparedStatement ps = con.prepareStatement(query);
            setParameters(ps, params);
            return ps.executeQuery();
        } catch (Exception e) {
            throw e;
        } finally {
            con.close();
        }
    }
    
    /**
     *
     * @param query
     * @param params
     * @return
     * @throws Exception
     */
    public long Update(String query, List<Object> params) throws Exception{
        try {
            PreparedStatement preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            setParameters(preparedStatement, params);
            preparedStatement.executeUpdate();
            
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            con.close();
        }
        
        return 0;
    }
}
