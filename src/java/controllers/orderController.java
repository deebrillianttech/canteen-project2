package controllers;

import business.Order;
import business.Payment;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.OrderModel;
import models.OrderStatus;
import models.PaymentModel;
import models.UserType;

/**
 *
 * @author ataulislam.raihan
 */
public class orderController extends HttpServlet {

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
        if (!security.Permission.Check(request, response, Arrays.asList(UserType.DELIVERY_MAN))) {
            return;
        }

        response.setContentType("text/html;charset=UTF-8");
        request.setAttribute("pageTitle", "Order");

        String action;
        try {
            action = request.getParameter("a").toLowerCase();
        } catch (Exception e) {
            action = "";
        }
        switch (action) {
            case "add":
                if(!"".equals(request.getParameter("payWhen"))){
                    try{
                        String when = request.getParameter("payWhen");
                        PaymentModel pm = new PaymentModel();
                        pm.setUserPin(Integer.parseInt(request.getParameter("userId")));
                        models.OrderModel ord = new models.OrderModel();
                        ord.setId(Integer.parseInt(request.getParameter("orderId")));
                        pm.setOrder(ord);
                        
                        switch(when.trim()){
                            case "now":
                                try{
                                    if(Double.parseDouble(request.getParameter("amount")) < 0.01){
                                        throw new Exception("Please enter an amount to make payment.");
                                    }
                                } catch(Exception ex){
                                    throw new Exception("Please enter an amount to make payment.");
                                }
                                pm.setAmount(Double.parseDouble(request.getParameter("amount")));
                                pm.setComments("Payment by cash");
                                
                                Payment.Add(pm);
                                break;
                            case "later":
                            default:
                                break;
                        }
                        
                        Order.ChangeStatus((int) ord.getId(), OrderStatus.DELIVERED);
                        
                        response.sendRedirect("order");
                        return;
                    } catch(Exception e){
                        request.setAttribute("error", e.getMessage());
                        show(request, response);
                    }
                    
                }else{
                    request.setAttribute("error", "An internal error occurred.");
                }
                request.getRequestDispatcher("WEB-INF/views/order/index.jsp").forward(request, response);
                break;
            case "show":
                show(request, response);
                
                OrderModel curOrder = null;
                List<OrderModel> allOrders = (List<OrderModel>)request.getAttribute("orders");
                for (OrderModel order : allOrders) {
                    if(order.getId() == Integer.parseInt(request.getParameter("i"))){
                        curOrder = order;
                        break;
                    }
                }
                if(curOrder != null){
                    request.setAttribute("curOrder", curOrder);
                    try {
                        request.setAttribute("balance", business.Report.GetBalance(curOrder.getUserPin()).getBalance());
                    } catch (Exception ex) {
                        request.setAttribute("error", ex.getMessage());
                    }
                }else{
                    request.setAttribute("error", "Order not found.");
                }
                
                request.getRequestDispatcher("WEB-INF/views/order/index.jsp").forward(request, response);
                break;
            default:
                show(request, response);
                request.getRequestDispatcher("WEB-INF/views/order/index.jsp").forward(request, response);
                break;
        }
    }

    private void show(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<OrderModel> orders = Order.GetAll(new Date());
            request.setAttribute("orders", orders);
        } catch (Exception ex) {
            request.setAttribute("error", ex.getMessage());
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
