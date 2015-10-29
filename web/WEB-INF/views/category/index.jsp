<%-- 
    Document   : index
    Created on : Mar 2, 2015, 11:57:43 AM
    Author     : ataulislam.raihan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<legend><i class="fa fa-bars"></i> All Categories</legend>

<div class='table-responsive'>
    <c:choose>
        <c:when test="${fn:length(allCats) lt 1}">
            <p>No category yet</p>
        </c:when>
        <c:otherwise>
            <table class="table table-bordered table-hover table-condensed">
                <thead>
                    <tr>
                        <th>Title</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${allCats}" var="cat">
                        <tr>
                            <td>
                                <a href='#showDetails_${cat.id}' title="Show details" data-toggle="modal" data-cat-id="${cat.id}">${cat.title}</a>
                                
                                <div class="modal fade" id="showDetails_${cat.id}">
                                    <div class="modal-dialog">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                                <h4 class="modal-title">Details</h4>
                                            </div>
                                            <div class="modal-body">
                                                <b>Title:</b> ${cat.title} <br />
                                                <b>Description:</b> ${cat.description}
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-sm btn-default" data-dismiss="modal">Close</button>
                                            </div>
                                        </div><!-- /.modal-content -->
                                    </div><!-- /.modal-dialog -->
                                </div><!-- /.modal -->
                            </td>
                            <td class="text-center">
                                <a class="btn btn-xs" title="Edit this category" href="<c:url value='category?a=edit&id=${cat.id}'></c:url>"><span class="glyphicon glyphicon-edit"></span></a>
                                
                                <form action="category?a=delete" method="POST" style="display: inline;">
                                    <input type="hidden" name="delId" value="${cat.id}" />
                                    <button type="submit" class="btn btn-xs" title="Delete this category"
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