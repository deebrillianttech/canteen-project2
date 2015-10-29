<%-- 
    Document   : index
    Created on : Mar 8, 2015, 4:49:39 PM
    Author     : ataulislam.raihan
--%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<legend><i class="fa fa-group"></i> Privileged Users</legend>

<div class='table-responsive'>
    <c:choose>
        <c:when test="${fn:length(prevelligedUsers) lt 1}">
            <p>No privileged users</p>
        </c:when>
        <c:otherwise>
            <table class="table table-bordered table-hover table-condensed">
                <thead>
                    <tr>
                        <th>User</th>
                        <th>Type</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${prevelligedUsers}" var="user">
                        <tr>
                            <td>${user.fullName}</td>
                            <td>${user.type}</td>
                            
                            <td>
                                <form action="user?a=delete" method="POST" style="display: inline;">
                                    <input type="hidden" name="delId" value="${user.pin}" />
                                    <input type="hidden" name="permType" value="${user.type}" />
                                    <button type="submit" class="btn btn-xs" title="Delete this permission"
                                            onclick="return confirm('Are you sure you want to delete?');">
                                        <span class="glyphicon glyphicon-remove" style="color:red;"></span>
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>