<%-- 
    Document   : index
    Created on : Mar 5, 2015, 10:01:24 AM
    Author     : ataulislam.raihan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<legend><i class="fa fa-calendar"></i> Schedule</legend>

<div class='table-responsive'>
    <c:choose>
        <c:when test="${fn:length(allSchedules) lt 1}">
            <p>No schedule yet</p>
        </c:when>
        <c:otherwise>
            <table class="table table-bordered table-hover table-condensed">
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Total Item</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${allSchedules}" var="schedule">
                        <tr>
                            <td>
                                <a href='#showDetails_${schedule.id}' title="Show details" data-toggle="modal"><fmt:formatDate value="${schedule.date}" pattern="MMM-dd-yyyy" /></a>
                                
                                <div class="modal fade" id="showDetails_${schedule.id}">
                                    <div class="modal-dialog">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                                <h4 class="modal-title">Details</h4>
                                            </div>
                                            <div class="modal-body">
                                                <b>Date:</b> <fmt:formatDate value="${schedule.date}" pattern="MMM-dd-yyyy" /><br />
                                                <b>Foods:</b>
                                                <ul>
                                                    <c:forEach items="${schedule.foods}" var="food"><li>${food.title}</li></c:forEach>
                                                </ul>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-sm btn-default" data-dismiss="modal">Close</button>
                                            </div>
                                        </div><!-- /.modal-content -->
                                    </div><!-- /.modal-dialog -->
                                </div><!-- /.modal -->
                            </td>
                            <td>${fn:length(schedule.foods)}</td>
                            <td class="text-center">
                                <a class="btn btn-xs" title="Edit this schedule" href="<c:url value='schedule?a=edit&id=${schedule.id}'></c:url>"><span class="glyphicon glyphicon-edit"></span></a>
                                
                                <form action="schedule?a=delete" method="POST" style="display: inline;">
                                    <input type="hidden" name="delId" value="${schedule.id}" />
                                    <button type="submit" class="btn btn-xs" title="Delete this schedule"
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
<span class="glyphicon glyphicon-info-sign"></span> Showing last 31 records.