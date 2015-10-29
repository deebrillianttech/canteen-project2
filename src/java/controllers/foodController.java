package controllers;

import business.Category;
import business.Food;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.CategoryModel;
import models.FoodModel;
import models.UserType;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author ataulislam.raihan
 */
public class foodController extends HttpServlet {

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
        request.setAttribute("pageTitle", "Food");
        
        String action;
        try{
            action = request.getParameter("a").toLowerCase();
        } catch(Exception e){
            action = "";
        }
        
        // Check that we have a file upload request
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
                
        switch(action){
            case "add":
                // <editor-fold defaultstate="collapsed" desc="Add">
                try{
                    request.setAttribute("allCats", Category.GetAll());
                    
                    if(isMultipart){
                        // POST
                        add(request, response);
                        response.sendRedirect("food");
                    }else{
                        // GET
                        request.getRequestDispatcher("WEB-INF/views/food/add.jsp").forward(request, response);
                    }
                } catch(Exception e){
                    request.setAttribute("error", e.getMessage());
                    request.getRequestDispatcher("WEB-INF/views/food/add.jsp").forward(request, response);
                }
                break;
                // </editor-fold>
            case "edit":
                // <editor-fold defaultstate="collapsed" desc="Edit">
                try{
                    request.setAttribute("allCats", Category.GetAll());
                    
                    if(isMultipart){
                        save(request, response);
                        response.sendRedirect("food");
                    }else{
                        loadForEdit(request, response);
                    }
                } catch(Exception e){
                    request.setAttribute("error", e.getMessage());
                    request.getRequestDispatcher("WEB-INF/views/food/add.jsp").forward(request, response);
                }
                break;
                // </editor-fold>
            case "delete":
                // <editor-fold defaultstate="collapsed" desc="Delete">
                try{
                    int foodId = Integer.parseInt(request.getParameter("delId"));
                    models.FoodModel food = Food.Get(foodId);
                    Food.Delete(food);
                    // deleting image
                    if(food.getImage() != null){
                        String uploadPath = this.getInitParameter("imageUploadPath");
                        File uploads = new File(uploadPath);
                        File file = new File(uploads, food.getImage());
                        try{
                            Files.delete(file.toPath());
                        }catch(Exception e){}
                    }
                    response.sendRedirect("food");
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
        models.FoodModel food = Food.Get(id);
        request.setAttribute("food", food);
        request.setAttribute("action", "edit");
        request.getRequestDispatcher("WEB-INF/views/food/add.jsp").forward(request, response);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        models.FoodModel food = new FoodModel();
        try {
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            
            for (FileItem item : items) {
                if (item.isFormField()) {
                    // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString();
                    
                    if(!"".equals(fieldValue.trim())){
                        switch(fieldName){
                            case "title":
                                food.setTitle(fieldValue.trim());
                                break;
                            case "desc":
                                food.setDescription(fieldValue.trim());
                                break;
                            case "price":
                                food.setPrice(Double.parseDouble(fieldValue.trim()));
                                break;
                        }
                    }
                } else {
                    // Process form file field (input type="file").
                    String fieldName = item.getFieldName();
                    String fileName = FilenameUtils.getName(item.getName());
                    InputStream fileContent = item.getInputStream();
                    
                    if("image".equals(fieldName)){
                        // uploading file
                        if(!"".equals(fileName)){
                            String ext = FilenameUtils.getExtension(fileName);
                            food.setImage(food.getTitle().replace(' ', '_') + "." + ext);
                            try{
                                String uploadPath = this.getInitParameter("imageUploadPath");
                                File uploads = new File(uploadPath);
                                File file = new File(uploads, food.getImage());
                                Files.copy(fileContent, file.toPath());
                            } catch(NoSuchFileException e){
                                throw new NoSuchFileException("Invalid file upload path");
                            }
                        }
                    }
                }
            }
        } catch (FileUploadException e) {
            throw new ServletException("Cannot parse multipart request.", e);
        }
        
        // adding categories
        List<models.CategoryModel> lstCats = new ArrayList();
        if(request.getParameterValues("categories") != null){
            for (String parameterValue : request.getParameterValues("categories")) {
                models.CategoryModel cat = new CategoryModel();
                cat.setId(Integer.parseInt(parameterValue));
                lstCats.add(cat);
            }
        }
        food.setCategories(lstCats);
        
        try{
            Food.Add(food);
        } catch(Exception e){
            request.setAttribute("error", e.getMessage());
            request.setAttribute("food", food);
            request.getRequestDispatcher("WEB-INF/views/food/add.jsp").forward(request, response);
        }
    }

    private void showAll(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<FoodModel> allFood;
        try{
            allFood  = Food.GetAll();
            request.setAttribute("allFood", allFood);
        } catch(Exception e){
            request.setAttribute("error", e.getMessage());
        }
        request.getRequestDispatcher("WEB-INF/views/food/index.jsp").forward(request, response);
    }
    
    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, Exception {
        models.FoodModel food = new models.FoodModel();
        
        try {
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            
            for (FileItem item : items) {
                if (item.isFormField()) {
                    // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString();
                    
                    switch(fieldName){
                        case "hdnId":
                            food = Food.Get(Integer.parseInt(fieldValue.trim()));
                            break;
                        case "title":
                            food.setTitle(fieldValue.trim());
                            break;
                        case "desc":
                            food.setDescription(fieldValue.trim());
                            break;
                        case "price":
                            food.setPrice(Double.parseDouble(fieldValue.trim()));
                            break;
                        case "isActive":
                            food.setIsActive(fieldValue != null);
                            break;
                    }
                } else {
                    // Process form file field (input type="file").
                    String fieldName = item.getFieldName();
                    String fileName = FilenameUtils.getName(item.getName());
                    InputStream fileContent = item.getInputStream();
                    
                    if("image".equals(fieldName)){
                        String uploadPath = this.getInitParameter("imageUploadPath");
                        File uploads = new File(uploadPath);
                        File file = new File(uploads, food.getImage());

                        // uploading file
                        if(!"".equals(fileName)){
                            String ext = FilenameUtils.getExtension(fileName);
                            food.setImage(food.getTitle().replace(' ', '_') + "." + ext);
                            file = new File(uploads, food.getImage());
                            try{
                                // delete previous file
                                try{
                                    Files.delete(file.toPath());
                                } catch(Exception e){}                                
                                
                                Files.copy(fileContent, file.toPath());
                            } catch(NoSuchFileException e){
                                throw new NoSuchFileException("Invalid file upload path");
                            }
                        }else{
                            food.setImage("");
                            // delete previous file
                            try{
                                Files.delete(file.toPath());
                            } catch(Exception e){} 
                        }
                    }
                }
            }
        } catch (FileUploadException e) {
            throw new ServletException("Cannot parse multipart request.", e);
        }
        
        List<models.CategoryModel> lstCats = new ArrayList();
        if(request.getParameterValues("categories") != null){
            for (String parameterValue : request.getParameterValues("categories")) {
                models.CategoryModel cat = new CategoryModel();
                cat.setId(Integer.parseInt(parameterValue));
                lstCats.add(cat);
            }
        }
        food.setCategories(lstCats);
        
        try{
            Food.Save(food);
        } catch(Exception e){
            request.setAttribute("error", e.getMessage());
            request.setAttribute("food", food);
            request.setAttribute("action", "edit");
            request.getRequestDispatcher("WEB-INF/views/food/add.jsp").forward(request, response);
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
