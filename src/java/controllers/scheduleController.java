package controllers;

import business.Category;
import business.Food;
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
import models.ScheduleModel;
import models.UserType;

/**
 *
 * @author ataulislam.raihan
 */
public class scheduleController extends HttpServlet {

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
        request.setAttribute("pageTitle", "Schedule");
        String calData = Schedule.GetCalendarData();
        request.setAttribute("calendarData", Schedule.GetCalendarData());
        
        String action;
        try{
            action = request.getParameter("a").toLowerCase();
        } catch(Exception e){
            action = "";
        }
        switch(action){
            case "add":
                // <editor-fold defaultstate="collapsed" desc="Add">
                try{
                    request.setAttribute("allFoods", Food.GetAll(true));
                    request.setAttribute("allCategories", Category.GetAll());
                    
                    String addSchedule = request.getParameter("addSchedule");
                    if(addSchedule != null){
                        // POST
                        add(request, response);
                        response.sendRedirect("schedule");
                    }else{
                        // GET
                        request.getRequestDispatcher("WEB-INF/views/schedule/add.jsp").forward(request, response);
                    }
                } catch(Exception e){
                    request.setAttribute("error", e.getMessage());
                    request.getRequestDispatcher("WEB-INF/views/schedule/add.jsp").forward(request, response);
                }
                // </editor-fold>
                break;
            case "edit":
                // <editor-fold defaultstate="collapsed" desc="Edit">
                try{
                    request.setAttribute("allFoods", Food.GetAll(true));
                    request.setAttribute("allCategories", Category.GetAll());
                    
                    String saveSchedule = request.getParameter("saveSchedule");
                    if(saveSchedule != null){
                        save(request, response);
                        response.sendRedirect("schedule");
                    }else{
                        loadForEdit(request, response);
                    }
                } catch(Exception e){
                    request.setAttribute("error", e.getMessage());
                    request.getRequestDispatcher("WEB-INF/views/schedule/add.jsp").forward(request, response);
                }
                break;
                // </editor-fold>
            case "delete":
                // <editor-fold defaultstate="collapsed" desc="Delete">
                try{
                    int schId = Integer.parseInt(request.getParameter("delId"));
                    models.ScheduleModel sch = Schedule.Get(schId);
                    Schedule.Delete(sch);
                    response.sendRedirect("schedule");
                }catch(Exception e){
                    request.setAttribute("error", e.getMessage());
                }
                // </editor-fold>
                break;
            default:
                showAll(request, response);
                break;
        }
    }
    
    private void add(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        models.ScheduleModel schedule = new ScheduleModel();
        schedule.setDate(new Date(request.getParameter("date")));
        
        List<models.FoodModel> lstFood = new ArrayList();
        String[] foods = request.getParameterValues("foods");
        if(foods != null){
            for (String parameterValue : foods) {
                models.FoodModel food = new FoodModel();
                food.setId(Integer.parseInt(parameterValue));
                
                int servingsLeft = 0;
                try{
                    servingsLeft = Integer.parseInt(request.getParameter("serving_" + food.getId()));
                }catch(Exception e){
                    request.setAttribute("error", "Invalid serving count.");
                }
                food.setServingsLeft(servingsLeft);
                
                lstFood.add(food);
            }
        }
        schedule.setFoods(lstFood);
        
        try{
            Schedule.Add(schedule);
        } catch(Exception e){
            request.setAttribute("error", e.getMessage());
            request.setAttribute("schedule", schedule);
            request.getRequestDispatcher("WEB-INF/views/schedule/add.jsp").forward(request, response);
        }
    }
    
    private void showAll(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<ScheduleModel> allSchedules;
        try{
            allSchedules  = Schedule.GetAll();
            request.setAttribute("allSchedules", allSchedules);
        } catch(Exception e){
            request.setAttribute("error", e.getMessage());
        }
        request.getRequestDispatcher("WEB-INF/views/schedule/index.jsp").forward(request, response);
    }
    
    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, Exception {
        models.ScheduleModel sch = Schedule.Get(Integer.parseInt(request.getParameter("hdnId")));
        sch.setDate(new Date(request.getParameter("date")));
        
        List<models.FoodModel> lstFood = new ArrayList();
        if(request.getParameterValues("foods") != null){
            for (String parameterValue : request.getParameterValues("foods")) {
                models.FoodModel food = new FoodModel();
                food.setId(Integer.parseInt(parameterValue));
                
                int servingsLeft = 0;
                try{
                    servingsLeft = Integer.parseInt(request.getParameter("serving_" + food.getId()));
                }catch(Exception e){
                    request.setAttribute("error", "Invalid serving count.");
                }
                food.setServingsLeft(servingsLeft);
                
                lstFood.add(food);
            }
        }
        sch.setFoods(lstFood);
        
        try{
            Schedule.Save(sch);
        } catch(Exception e){
            request.setAttribute("error", e.getMessage());
            request.setAttribute("schedule", sch);
            request.getRequestDispatcher("WEB-INF/views/schedule/add.jsp").forward(request, response);
        }
    }
    
    private void loadForEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NumberFormatException, Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        models.ScheduleModel sch = Schedule.Get(id);
        request.setAttribute("schedule", sch);
        request.setAttribute("action", "edit");
        request.getRequestDispatcher("WEB-INF/views/schedule/add.jsp").forward(request, response);
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
