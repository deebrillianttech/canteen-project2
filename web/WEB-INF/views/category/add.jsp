<%-- 
    Document   : add
    Created on : Mar 2, 2015, 12:37:44 PM
    Author     : ataulislam.raihan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<legend>
    <c:choose>
        <c:when test="${action == 'edit'}">Edit "${category.title}"</c:when>
        <c:otherwise>Add Category</c:otherwise>
    </c:choose>
</legend>

<div class="col-sm-6">
    <form id="addCategoryForm" action="<c:url value="${(action == 'edit')? 'category?a=edit' : 'category?a=add'}"></c:url>" method="POST" class="form-horizontal">
        <c:if test="${action == 'edit'}"><input type="hidden" id="hdnId" name="hdnId" value="${category.id}" /></c:if>
        
        <div class="form-group">
            <label for="title" class="col-sm-3 control-label">Title</label>
            <div class="col-sm-9">
                <input type="text" class="form-control input-sm" id="title" name="title" placeholder="Title" value="${category.title}">
            </div>
        </div>
        
        <div class="form-group">
            <label for="desc" class="col-sm-3 control-label">Description</label>
            <div class="col-sm-9">
                <textarea class="form-control input-sm" id="desc" name="desc" placeholder="Description">${category.description}</textarea>
            </div>
        </div>
        
        <div class="form-group">
            <div class="col-sm-offset-3 col-sm-9">
                
                <c:choose>
                    <c:when test="${action == 'edit'}"><button type="submit" class="btn btn-sm btn-primary" name="saveCat">Save</button></c:when>
                    <c:otherwise><button type="submit" class="btn btn-sm btn-primary" name="addCat">Add</button></c:otherwise>
                </c:choose>
                    
                <a href="<c:url value='category'></c:url>" class="btn btn-sm btn-danger">Cancel</a>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        $('#addCategoryForm').validate({
            rules: {
                title: "required"
            }
        });
    });
</script>