package controllers;

import business.Food;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.FoodModel;
import models.UserType;

/**
 *
 * @author ataulislam.raihan
 */
public class restController extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");

        String action;
        try {
            action = request.getParameter("a").toLowerCase();
        } catch (Exception e) {
            action = "";
        }

        switch (action) {
            case "getfoodsbyorderid":
                // checking permission
                if(! security.Permission.Check(request, response, Arrays.asList(UserType.ADMIN, UserType.DELIVERY_MAN, UserType.USER))) return;
        
                int orderId = Integer.parseInt(request.getParameter("i"));
                 {
                    try {
                        List<FoodModel> orderedFoods = Food.GetAllByOrderId(orderId);
                        String json = new Gson().toJson(orderedFoods);
                        response.getWriter().print(json.trim());
                    } catch (Exception ex) {
                        response.getWriter().print("error");
                    }
                }
                break;
            default:
                response.getWriter().print("Hello from webservice");
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
