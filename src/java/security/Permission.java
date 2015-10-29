package security;

import controllers.orderController;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.UserModel;
import models.UserType;

/**
 *
 * @author ataulislam.raihan
 */
public class Permission {
    public static boolean Check(HttpServletRequest request, HttpServletResponse response, List<UserType> allowedRoles){
        try {
            UserModel sUser = (UserModel) request.getSession().getAttribute("objUser");
            if (sUser == null) {
                response.sendRedirect("login");
                return false;
            }
            boolean allowed = false;
            for (UserType type : allowedRoles) {
                if (sUser.getType() == type) {
                    allowed = true;
                    break;
                }
            }
            if (!allowed) {
                request.setAttribute("error", "You are not authorized to perform this action.");
                request.getRequestDispatcher("index.jsp").forward(request, response);
                return false;
            }
        }catch (Exception e) {
            request.setAttribute("error", "Cannot check permission. " + e.getMessage());
            try {
                request.getRequestDispatcher("index.jsp").forward(request, response);
            } catch (ServletException | IOException ex) {
                Logger.getLogger(orderController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return true;
    }
}
