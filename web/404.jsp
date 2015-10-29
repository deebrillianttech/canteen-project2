<%-- 
    Document   : error
    Created on : Jan 6, 2015, 4:57:34 PM
    Author     : ataulislam.raihan
--%>

<%@page isErrorPage="true" import="java.io.*" contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Canteen Automation System</title>
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <br /><br /><br />
        <div class="col-lg-8 col-lg-offset-2">
            <div class="panel panel-danger">
                  <div class="panel-heading">
                        <h3 class="panel-title"><span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span> <b>Error</b></h3>
                  </div>
                  <div class="panel-body">
                        <h1>Sorry, not found :(</h1>
                        <p style="color:red;">Error 404.</p>
                        
                        <br />
                        <hr />
                        You may want to <button type="button" class="btn btn-xs btn-info" onclick="javascript:history.go(-1);">
                        <span class="glyphicon glyphicon-circle-arrow-left"></span>
                        Go Back</button> or <button type="button" class="btn btn-xs btn-info" onclick="javascript:location.reload();">
                        <span class="glyphicon glyphicon-refresh"></span>
                        Retry</button>
                  </div>
            </div>
        </div>

        <script src="js/jquery.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
    </body>
</html>
