package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.UserType;

/**
 *
 * @author ataulislam.raihan
 */
public class reportsController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // checking permission
        if(! security.Permission.Check(request, response, Arrays.asList(UserType.ADMIN, UserType.USER))) return;
        
        response.setContentType("text/html;charset=UTF-8");
        request.setAttribute("pageTitle", "Reports");
        models.UserModel user = (models.UserModel) request.getSession().getAttribute("objUser");
        
        switch(user.getType()){
            case ADMIN:
                try {
                    request.setAttribute("sales", business.Report.GetTodaysSalesVsCostForAdmin(new Date()));
                } catch (Exception ex) {
                    request.setAttribute("error", ex.getMessage());
                }
                request.getRequestDispatcher("WEB-INF/views/reports/admin.jsp").forward(request, response);
                break;
            case DELIVERY_MAN:
                break;
            case USER:
                try {
                    request.setAttribute("myPayments", business.Payment.GetAll(user.getPin(), 10));
                    request.setAttribute("paymentResultLimit", 10);
                    
                    request.setAttribute("myCosts", business.Report.GetAllCost(user.getPin(), null, null, 10));
                    request.setAttribute("costResultLimit", 10);
                    
                    request.setAttribute("myBalance", business.Report.GetBalance(user.getPin()));
                } catch (Exception ex) {
                    request.setAttribute("error", ex.getMessage());
                }
                request.getRequestDispatcher("WEB-INF/views/reports/user.jsp").forward(request, response);
                break;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
