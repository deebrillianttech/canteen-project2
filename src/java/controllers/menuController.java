package controllers;

import business.Food;
import business.Order;
import business.Schedule;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.FoodModel;
import models.OrderModel;
import models.OrderStatus;
import models.ScheduleModel;
import models.TrayModel;
import models.UserModel;
import models.UserType;

/**
 *
 * @author ataulislam.raihan
 */
public class menuController extends HttpServlet {

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
        if(! security.Permission.Check(request, response, Arrays.asList(UserType.USER, UserType.DELIVERY_MAN))) return;
        
        response.setContentType("text/html;charset=UTF-8");
        request.setAttribute("pageTitle", "Menu");

        String action;
        try {
            action = request.getParameter("a").toLowerCase();
        } catch (Exception e) {
            action = "";
        }
        switch (action) {
            case "add":
                addToTray(request);
                response.sendRedirect("menu");
                break;
            case "remove":
                removeFromTray(request);
                response.sendRedirect("menu");
                break;
            case "order":
                try {
                    addOrder(request);
                } catch (Exception e) {
                    request.setAttribute("error", e.getMessage());
                }
                request.getRequestDispatcher("WEB-INF/views/menu/index.jsp").forward(request, response);
                break;
            case "cancel":
                int orderId = -1;
                try {
                    orderId = Integer.parseInt(request.getParameter("orderId"));
                    OrderModel order;
                
                    order = Order.Get(orderId);
                    Order.Delete(order);
                    response.sendRedirect("menu");
                } catch (Exception ex) {
                    request.setAttribute("error", ex.getMessage());
                    request.getRequestDispatcher("WEB-INF/views/menu/index.jsp").forward(request, response);
                }
                break;
            default:
                showAll(request, response);
                break;
        }
    }

    private void addOrder(HttpServletRequest request) throws Exception {
        UserModel user = (UserModel) request.getSession().getAttribute("objUser");
        if (user != null) {
            OrderModel order = new OrderModel();
            order.setUserPin(user.getPin());

            ScheduleModel sch = new ScheduleModel();
            sch.setId(Schedule.GetIdByDate(new Date()));

            order.setSchedule(sch);
            order.setStatus(OrderStatus.ORDERED);

            TrayModel tray = (TrayModel) request.getSession().getAttribute("tray");

            order.setFoods(tray.getFoods());

            long orderId = Order.Add(order);
            order.setId(orderId);

            // clearing the tray
            request.getSession().setAttribute("tray", null);
            request.setAttribute("lastOrder", order);
            request.setAttribute("myOrders", Order.GetAll(user.getPin(), 10, null));
        } else {
            request.setAttribute("error", "User not found");
        }
    }

    private void addToTray(HttpServletRequest request) {
        try {
            TrayModel myTray = new TrayModel();
            TrayModel sesstionTray = (TrayModel) request.getSession().getAttribute("tray");
            if (sesstionTray != null) {
                myTray = sesstionTray;
            }
            FoodModel chosenFood = Food.Get(Integer.parseInt(request.getParameter("i")));
            myTray.getFoods().add(chosenFood);
            request.getSession().setAttribute("tray", myTray);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
        }
    }

    private void removeFromTray(HttpServletRequest request) {
        try {
            TrayModel sesstionTray = (TrayModel) request.getSession().getAttribute("tray");

            List<FoodModel> removeList = new ArrayList();
            for (FoodModel food : sesstionTray.getFoods()) {
                int chosenId = Integer.parseInt(request.getParameter("i"));
                if (food.getId() == chosenId) {
                    removeList.add(food);
                }
            }
            sesstionTray.getFoods().removeAll(removeList);

            request.getSession().setAttribute("tray", sesstionTray);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
        }
    }

    private void showAll(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<FoodModel> foods;
        try {
            foods = Food.GetAllTodaysMenu();
            // removing items that are already added
            TrayModel sesstionTray = (TrayModel) request.getSession().getAttribute("tray");
            if (sesstionTray != null) {
                List<FoodModel> removedList = new ArrayList();
                for (FoodModel food : foods) {
                    for (FoodModel fSess : sesstionTray.getFoods()) {
                        if (food.getId() == fSess.getId()) {
                            removedList.add(food);
                            break;
                        }
                    }
                }

                foods.removeAll(removedList);
            } else {
                request.getSession().setAttribute("tray", new TrayModel());
            }

            request.setAttribute("foods", foods);
            
            UserModel user = (UserModel) request.getSession().getAttribute("objUser");
            request.setAttribute("myOrders", Order.GetAll(user.getPin(), 10, null));

            TrayModel tray = (TrayModel) request.getSession().getAttribute("tray");
            request.setAttribute("myTray", tray);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
        }

        request.getRequestDispatcher("WEB-INF/views/menu/index.jsp").forward(request, response);
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
