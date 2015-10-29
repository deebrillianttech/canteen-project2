<%-- 
    Document   : add
    Created on : Mar 2, 2015, 12:37:44 PM
    Author     : ataulislam.raihan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<legend>
    <c:choose>
        <c:when test="${action == 'edit'}">Edit "${food.title}"</c:when>
        <c:otherwise>Add Food</c:otherwise>
    </c:choose>
</legend>

<div class="col-sm-6">
    <form id="addFoodForm"
          action="<c:url value="${(action == 'edit')? 'food?a=edit' : 'food?a=add'}"></c:url>"
          method="POST" class="form-horizontal" enctype="multipart/form-data">
        <c:if test="${action == 'edit'}"><input type="hidden" id="hdnId" name="hdnId" value="${food.id}" /></c:if>

            <div class="form-group">
                <label for="title" class="col-sm-3 control-label">Title</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control input-sm" id="title" name="title" placeholder="Title" value="${food.title}">
            </div>
        </div>

        <div class="form-group">
            <label for="desc" class="col-sm-3 control-label">Description</label>
            <div class="col-sm-9">
                <textarea class="form-control input-sm" id="desc" name="desc" placeholder="Description">${food.description}</textarea>
            </div>
        </div>

        <div class="form-group">
            <label for="price" class="col-sm-3 control-label">Price</label>
            <div class="col-sm-9">
                <input type="text" class="form-control input-sm" id="price" name="price" placeholder="Price" value="${food.price}">
            </div>
        </div>

        <div class="form-group">
            <label for="price" class="col-sm-3 control-label">Image</label>
            
            <div class="fileinput fileinput-new col-sm-9" data-provides="fileinput">
                <div class="fileinput-preview thumbnail" data-trigger="fileinput" style="width: 200px; height: 150px;"></div>
                <div>
                    <span class="btn btn-default btn-file"><span class="fileinput-new">Select image</span><span class="fileinput-exists">Change</span><input type="file" name="image"></span>
                    <a href="#" class="btn btn-default fileinput-exists" data-dismiss="fileinput">Remove</a>
                </div>
            </div>
        </div>

        <div class="form-group">
            <label for="cats" class="col-sm-3 control-label">Categories</label>
            <div class="col-sm-9 well well-sm">
                <c:choose>
                    <c:when test="${fn:length(allCats) lt 1}">
                        <i>No category yet</i>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${allCats}" var="cat">
                            <c:set var="contains" value="" />

                            <c:forEach var="foodCat" items="${food.categories}">
                                <c:if test="${foodCat.id eq cat.id}">
                                    <c:set var="contains" value='checked="checked"' />
                                </c:if>
                            </c:forEach>

                            <label><input type="checkbox" name="categories" value="${cat.id}" ${contains} /> ${cat.title}</label> <br />
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
            </div>
        </div>

        <c:if test="${action == 'edit'}">
            <div class="form-group">
                <div class="col-sm-offset-3 col-sm-9">
                    <label><input type="checkbox" id="isActive" name="isActive" ${(food.isActive) ? 'checked="checked"' : ''} /> Active</label>
                </div>
            </div>
        </c:if>

        <div class="form-group">
            <div class="col-sm-offset-3 col-sm-9">
                <c:choose>
                    <c:when test="${action == 'edit'}"><button type="submit" class="btn btn-sm btn-primary" name="saveFood">Save</button></c:when>
                    <c:otherwise><button type="submit" class="btn btn-sm btn-primary" name="addFood">Add</button></c:otherwise>
                </c:choose>

                <a href="<c:url value='food'></c:url>" class="btn btn-sm btn-danger">Cancel</a>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        $('#addFoodForm').validate({
            rules: {
                title: "required",
                price: {required: true, number: true}
            }
        });
    });
</script>