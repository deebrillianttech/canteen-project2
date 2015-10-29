<%-- 
    Document   : index
    Created on : Mar 2, 2015, 11:57:43 AM
    Author     : ataulislam.raihan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<legend><i class="fa fa-coffee"></i> All Foods</legend>

<div class='table-responsive'>
    <c:choose>
        <c:when test="${fn:length(allFood) lt 1}">
            <p>No food yet</p>
        </c:when>
        <c:otherwise>
            <table class="table table-bordered table-hover table-condensed">
                <thead>
                    <tr>
                        <th>Title</th>
                        <th>Price</th>
                        <th>Is Active</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${allFood}" var="food">
                        <tr>
                            <td>
                                <a href='#showDetails_${food.id}' title="Show details" data-toggle="modal" data-food-id="${food.id}">${food.title}</a>
                                
                                <div class="modal fade" id="showDetails_${food.id}">
                                    <div class="modal-dialog">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                                <h4 class="modal-title">Details</h4>
                                            </div>
                                            <div class="modal-body">
                                                <b>Title:</b> ${food.title} <br />
                                                <b>Description:</b> ${food.description}
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-sm btn-default" data-dismiss="modal">Close</button>
                                            </div>
                                        </div><!-- /.modal-content -->
                                    </div><!-- /.modal-dialog -->
                                </div><!-- /.modal -->
                            </td>
                            <td>
                                ${food.price}
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${food.isActive}">
                                        <span class="glyphicon glyphicon-ok" style="color:green;"></span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="glyphicon glyphicon-remove"></span>
                                    </c:otherwise>
                              </c:choose>
                            </td>
                            <td class="text-center">
                                <a class="btn btn-xs" title="Edit this food" href="<c:url value='food?a=edit&id=${food.id}'></c:url>"><span class="glyphicon glyphicon-edit"></span></a>
                                
                                <form action="food?a=delete" method="POST" style="display: inline;">
                                    <input type="hidden" name="delId" value="${food.id}" />
                                    <button type="submit" class="btn btn-xs" title="Delete this food"
                                            onclick="return confirm('Are you sure you want to delete?');">
                                        <span class="glyphicon glyphicon-remove" style="color:red;"></span>
                                    </button>
                                </form>
                                
                                <div class="dropdown" style="display:inline;">
                                    <a href="#" title="See Categories" class="btn btn-xs dropdown-toggle" data-toggle="dropdown"><b class="caret"></b></a>

                                    <ul class="dropdown-menu">
                                        <c:choose>
                                            <c:when test="${fn:length(food.categories) lt 1}">
                                                <li><a href="#">No category yet</a></li>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach items="${food.categories}" var="cat">
                                                    <li><a href="#">${cat.title}</a></li>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </ul>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<script type="text/javascript">
    $(document).ready(function(){
        
    });
</script>