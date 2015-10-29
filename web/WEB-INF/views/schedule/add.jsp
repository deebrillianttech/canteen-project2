<%-- 
    Document   : add
    Created on : Mar 2, 2015, 12:37:44 PM
    Author     : ataulislam.raihan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<legend>
    <c:choose>
        <c:when test="${action == 'edit'}">Edit</c:when>
        <c:otherwise>Add Schedule</c:otherwise>
    </c:choose>
</legend>

<div class="col-sm-9">
    <form id="addScheduleForm" action="<c:url value="${(action == 'edit')? 'schedule?a=edit' : 'schedule?a=add'}"></c:url>" method="POST" class="form-horizontal">
        <c:if test="${action == 'edit'}"><input type="hidden" id="hdnId" name="hdnId" value="${schedule.id}" /></c:if>

            <div class="form-group">
                <label for="title" class="col-sm-3 control-label">Date</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control input-sm" id="date" name="date" placeholder="Date"
                           value="<fmt:formatDate value="${schedule.date}" pattern="MMM/dd/yyyy" />">
            </div>
        </div>

        <div class="form-group">
            <label for="cats" class="col-sm-3 control-label">Foods</label>
            <div class="col-sm-9">
                <c:choose>
                    <c:when test="${fn:length(allFoods) lt 1}">
                        <i>No food yet</i>
                    </c:when>
                    <c:otherwise>

                        <!-- iterating each category-->
                        <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                            <c:forEach items="${allCategories}" var="cat">
                                <div class="panel panel-default">
                                    <div class="panel-heading" role="tab" id="cat-${cat.id}">
                                        <h4 class="panel-title">
                                            <a data-toggle="collapse" data-parent="#accordion" href="#collapse${cat.id}" aria-controls="collapse${cat.id}" onclick="return false;">
                                                ${cat.title}
                                            </a>
                                        </h4>
                                    </div>
                                    <div id="collapse${cat.id}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="cat-${cat.id}">
                                        <div class="panel-body">
                                            
                                            <!-- iterating each food -->
                                            <c:forEach items="${allFoods}" var="food">
                                                <c:set var="containsCat" value="" />
                                                <c:set var="contains" value="" />

                                                <c:forEach var="schFood" items="${schedule.foods}">
                                                    <c:if test="${schFood.id eq food.id}">
                                                        <c:set var="contains" value='checked="checked"' />
                                                    </c:if>
                                                </c:forEach>
                                                
                                                <c:forEach var="fCat" items="${food.categories}">
                                                    <c:if test="${fCat.id eq cat.id}">
                                                        <c:set var="containsCat" value='true' />
                                                    </c:if>
                                                </c:forEach>

                                                <c:if test="${containsCat == 'true'}">
                                                    <div class="food_item">
                                                        <div class="col-sm-9">
                                                            <label><input type="checkbox" name="foods" class="sch_food" value="${food.id}" ${contains} /> ${food.title}</label>
                                                        </div>
                                                        <div class="col-sm-3">
                                                            <input type="text" class="form-control input-sm food_serving_count" name="serving_${food.id}" placeholder="Servings" <c:if test='${contains == ""}'>style="display: none;"</c:if> value="${food.servingsLeft}" title="Servings" />
                                                        </div>
                                                    </div>
                                                </c:if>
                                                
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                            
                            <!-- foods without any category -->
                            <div class="panel panel-default">
                                <div class="panel-heading" role="tab" id="cat-0">
                                    <h4 class="panel-title">
                                        <a data-toggle="collapse" data-parent="#accordion" href="#collapse0" aria-controls="collapse0" onclick="return false;">
                                            Uncategorized
                                        </a>
                                    </h4>
                                </div>
                                <div id="collapse0" class="panel-collapse collapse" role="tabpanel" aria-labelledby="cat-0">
                                    <div class="panel-body">

                                        <!-- iterating each food -->
                                        <c:forEach items="${allFoods}" var="food">
                                            <c:set var="contains" value="" />

                                            <c:forEach var="schFood" items="${schedule.foods}">
                                                <c:if test="${schFood.id eq food.id}">
                                                    <c:set var="contains" value='checked="checked"' />
                                                </c:if>
                                            </c:forEach>

                                            <c:if test="${fn:length(food.categories) lt 1}">
                                                <div class="food_item">
                                                    <div class="col-sm-9">
                                                        <label><input type="checkbox" name="foods" class="sch_food" value="${food.id}" ${contains} /> ${food.title}</label>
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <input type="text" class="form-control input-sm food_serving_count" name="serving_${food.id}" placeholder="Servings" <c:if test='${contains == ""}'>style="display: none;"</c:if> value="${food.servingsLeft}" title="Servings" />
                                                    </div>
                                                </div>
                                            </c:if>

                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-offset-3 col-sm-9">
                <c:choose>
                    <c:when test="${action == 'edit'}"><button type="submit" class="btn btn-sm btn-primary" name="saveSchedule">Save</button></c:when>
                    <c:otherwise><button type="submit" class="btn btn-sm btn-primary" name="addSchedule">Add</button></c:otherwise>
                </c:choose>

                <a href="<c:url value='schedule'></c:url>" class="btn btn-sm btn-danger">Cancel</a>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        $('#addScheduleForm').validate({
            rules: {
                date: {required: true, date: true}
            }
        });
        
        $(".food_serving_count").each(function (item) {
            $(this).rules("add", { number:true });
        });

        $('#date').datepicker({format: 'M/dd/yyyy', todayHighlight: true, autoclose: true});

        $('.sch_food').change(function(){
            var servingsField = $(this).closest('.food_item').find('.food_serving_count');
            if($(this).is(':checked')){
                $(servingsField).show('fast');
                $(servingsField).rules("add", { required:true });
            }else{
                $(servingsField).hide('fast');
                $(servingsField).rules("remove", 'required');
            }
        });
    });
</script>