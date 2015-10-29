<%-- 
    Document   : index
    Created on : Jan 6, 2015, 10:06:21 AM
    Author     : ataulislam.raihan
--%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.security.MessageDigest"%>
<%
    if(request.getSession().getAttribute("objUser") != null){
        models.UserModel loggedInUser = (models.UserModel)request.getSession().getAttribute("objUser");
        
        // checking if autologin user...
        String sid = request.getParameter("sid");
        if(sid != null){
            if(Integer.parseInt(request.getParameter("u")) != loggedInUser.getPin()){
                request.getSession().setAttribute("objUser", null);
                response.sendRedirect("./");
            }
        }
        
        switch(loggedInUser.getType()){
            case ADMIN:
                response.sendRedirect("./schedule");
                break;
            case DELIVERY_MAN:
                response.sendRedirect("./order");
                break;
            case USER:
                response.sendRedirect("./menu");
                break;
            default:
                throw new Exception("Please login to continue");
        }
    }else{
        // auto login
        String sid = request.getParameter("sid");
        if(sid != null){
            String pin = request.getParameter("u");
            models.UserModel user = business.User.Get(Integer.parseInt(pin));
            if(user != new models.UserModel()){
                String passwordHash = util.MD5.getHash(user.getPassword());
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = df.format(new Date());
                String dateHash = util.MD5.getHash(dateString);
                String sid_here = dateHash + "$" + passwordHash;

                if(sid.equals(sid_here)){
                    request.getSession().setAttribute("objUser", user);
                    response.sendRedirect("./");
                }
            }
        }
    }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<legend><i class="fa fa-key"></i> Login</legend>

<div class="col-sm-6">
    <form action="<c:url value="login"></c:url>" method="POST" role="form" name="loginForm" id="loginForm">
        <div class="form-group">
            <input type="text" class="form-control input-sm" name="pin" id="pin" placeholder="Pin">
        </div>

        <div class="form-group">
            <input type="password" class="form-control input-sm" name="password" id="password" placeholder="Password">
        </div>

        <button type="submit" class="btn btn-primary btn-sm" name="loginSubmit">Login</button>
        <a href="<c:url value='/'></c:url>" class="btn btn-sm btn-danger">Cancel</a>
    </form>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        $('#pin').focus();
        
        $('#loginForm').validate({
            rules: {
                pin: "required",
                password: "required"
            }
        });
    });
</script>