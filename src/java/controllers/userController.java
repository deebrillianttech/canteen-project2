package controllers;

import business.User;
import java.io.IOException;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.UserType;

/**
 *
 * @author ataulislam.raihan
 */
public class userController extends HttpServlet {

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
        if(! security.Permission.Check(request, response, Arrays.asList(UserType.ADMIN))) return;
        
        response.setContentType("text/html;charset=UTF-8");
        request.setAttribute("pageTitle", "User");

        String action;
        try {
            action = request.getParameter("a").toLowerCase();
        } catch (Exception e) {
            action = "";
        }
        switch (action) {
            case "add":
                // <editor-fold defaultstate="collapsed" desc="Add">
                try {
                    request.setAttribute("allPinAndNames", User.GetAllPinAndName());
                    
                    String addPerm = request.getParameter("addPerm");
                    if(addPerm != null){
                        add(request);
                        response.sendRedirect("user");
                    }else{
                        request.getRequestDispatcher("WEB-INF/views/user/add.jsp").forward(request, response);
                    }
                } catch (Exception ex) {
                    request.setAttribute("error", ex.getMessage());
                }
                // </editor-fold>
                break;
            case "delete":
                delete(request, response);
                break;
            default:
                showAll(request, response);
                break;
        }
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        try{
            int pin = Integer.parseInt(request.getParameter("delId"));
            UserType type;
            String sType = request.getParameter("permType");
            switch(sType){
                case "ADMIN":
                    type = UserType.ADMIN;
                    break;
                case "DELIVERY_MAN":
                    type = UserType.DELIVERY_MAN;
                    break;
                default:
                    type = UserType.USER;
                    break;
            }
            User.DeletePermission(pin, type);
            
            // updating currently logged in user's permission
            request.getSession().setAttribute("objUser",
                    User.Get(((models.UserModel)request.getSession().getAttribute("objUser")).getPin()));
            
            response.sendRedirect("user");
        }catch(Exception e){
            request.setAttribute("error", e.getMessage());
        }
    }

    private void add(HttpServletRequest request) throws NumberFormatException, Exception {
        int pin = Integer.parseInt(request.getParameter("pin"));
        String perm = request.getParameter("perm");
        UserType ut = UserType.USER;
        switch(perm){
            case "a":
                ut = UserType.ADMIN;
                break;
            case "d":
                ut = UserType.DELIVERY_MAN;
                break;
        }
        
        User.ChangeType(pin, ut);
        
        // updating currently logged in user's permission
        request.getSession().setAttribute("objUser", 
                User.Get(((models.UserModel)request.getSession().getAttribute("objUser")).getPin()));
    }

    private void showAll(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            request.setAttribute("prevelligedUsers", User.GetAll());
        } catch (Exception ex) {
            request.setAttribute("error", ex.getMessage());
        }

        request.getRequestDispatcher("WEB-INF/views/user/index.jsp").forward(request, response);
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
