package controllers;

import business.Category;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.CategoryModel;
import models.UserType;

/**
 *
 * @author ataulislam.raihan
 */
public class categoryController extends HttpServlet {

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
        request.setAttribute("pageTitle", "Category");
        
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
                    String addCat = request.getParameter("addCat");
                    if(addCat != null){
                        // POST
                        add(request, response);
                        response.sendRedirect("category");
                    }else{
                        // GET
                        request.getRequestDispatcher("WEB-INF/views/category/add.jsp").forward(request, response);
                    }
                } catch(Exception e){
                    request.setAttribute("error", e.getMessage());
                }
                break;
                // </editor-fold>
            case "edit":
                // <editor-fold defaultstate="collapsed" desc="Edit">
                try{
                    String saveCat = request.getParameter("saveCat");
                    if(saveCat != null){
                        save(request, response);
                        response.sendRedirect("category");
                    }else{
                        loadForEdit(request, response);
                    }
                } catch(Exception e){
                    request.setAttribute("error", e.getMessage());
                }
                break;
                // </editor-fold>
            case "delete":
                // <editor-fold defaultstate="collapsed" desc="Delete">
                try{
                    int catId = Integer.parseInt(request.getParameter("delId"));
                    models.CategoryModel cat = Category.Get(catId);
                    Category.Delete(cat);
                    response.sendRedirect("category");
                }catch(Exception e){
                    request.setAttribute("error", e.getMessage());
                }
                break;
                // </editor-fold>
            default:
                showAll(request, response);
                break;
        }
    }

    private void loadForEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NumberFormatException, Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        models.CategoryModel cat = Category.Get(id);
        request.setAttribute("category", cat);
        request.setAttribute("action", "edit");
        request.getRequestDispatcher("WEB-INF/views/category/add.jsp").forward(request, response);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        models.CategoryModel cat = new CategoryModel();
        cat.setTitle(request.getParameter("title"));
        cat.setDescription(request.getParameter("desc"));
        try{
            Category.Add(cat);
        } catch(Exception e){
            request.setAttribute("error", e.getMessage());
            request.setAttribute("category", cat);
            request.getRequestDispatcher("WEB-INF/views/category/add.jsp").forward(request, response);
        }
    }

    private void showAll(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<CategoryModel> allCats;
        try{
            allCats  = Category.GetAll();
            request.setAttribute("allCats", allCats);
        } catch(Exception e){
            request.setAttribute("error", e.getMessage());
        }
        request.getRequestDispatcher("WEB-INF/views/category/index.jsp").forward(request, response);
    }
    
    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, Exception {
        models.CategoryModel cat = Category.Get(Integer.parseInt(request.getParameter("hdnId")));
        cat.setTitle(request.getParameter("title"));
        cat.setDescription(request.getParameter("desc"));
        
        try{
            Category.Save(cat);
        } catch(Exception e){
            request.setAttribute("error", e.getMessage());
            request.setAttribute("category", cat);
            request.getRequestDispatcher("WEB-INF/views/category/add.jsp").forward(request, response);
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
