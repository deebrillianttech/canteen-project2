package controllers;

import business.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.UserModel;

/**
 *
 * @author ataulislam.raihan
 */
public class loginController extends HttpServlet {

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
        request.setAttribute("pageTitle", "Login");
        
        String action;
        try{
            action = request.getParameter("a").toLowerCase();
        } catch(Exception e){
            action = "";
        }
        
        switch(action){
            case "logout":
                request.getSession().setAttribute("objUser", null);
                response.sendRedirect("");
                break;
            default:
                UserModel sUser = (UserModel)request.getSession().getAttribute("objUser");
                if(sUser != null){
                    response.sendRedirect("");
                    return;
                }

                String loginSubmit = request.getParameter("loginSubmit");

                try {
                    if (loginSubmit != null) {
                        authenticate(request, response);

                        response.sendRedirect("");
                        return;
                    } else {
                        request.getRequestDispatcher("WEB-INF/views/login.jsp").forward(request, response);
                    }
                } catch (Exception e) {
                    request.setAttribute("error", e.getMessage());
                    request.getRequestDispatcher("WEB-INF/views/login.jsp").forward(request, response);
                }
                break;
        }
    }

    private void authenticate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int pin;
        String password;
        try {
            pin = Integer.parseInt(request.getParameter("pin"));
        } catch (Exception e) {
            throw new Exception("Invalid PIN");
        }
        password = request.getParameter("password");
        UserModel user;
        try{
            user = User.Get(pin);
        }catch(Exception e){
            throw new Exception("User not found");
        }
        if(! user.getPassword().equals(password)){
            throw new Exception("Login failed");
        }
        
        request.getSession().setAttribute("objUser", user);
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
