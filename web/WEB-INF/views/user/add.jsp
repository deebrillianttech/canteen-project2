<%-- 
    Document   : add
    Created on : Mar 8, 2015, 5:32:51 PM
    Author     : ataulislam.raihan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<script src="<c:url value='/js/select2.min.js' />" type="text/javascript"></script>

<legend>Add Permission</legend>

<div class="col-sm-6">
    <form id="addFoodForm" action="<c:url value='user?a=add'></c:url>" method="POST" class="form-horizontal">
            <div class="form-group">
                <label for="pin" class="col-sm-3 control-label">User</label>
                <div class="col-sm-9">
                    <select class="form-control input-sm" id="pin" name="pin" placeholder="Search for PIN...">
                    <c:forEach items="${allPinAndNames}" var="user">
                        <option value="${user.pin}">${user.fullName} (${user.pin})</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="form-group">
            <label for="perm" class="col-sm-3 control-label">Permission</label>
            <div class="col-sm-9">
                <select class="form-control input-sm" id="perm" name="perm">
                    <option value="a">Admin</option>
                    <option value="d">Delivery Man</option>
                </select>
            </div>
        </div>
            
        <div class="form-group">
            <div class="col-sm-offset-3 col-sm-9">
                <button type="submit" class="btn btn-sm btn-primary" name="addPerm">Add</button>
                <a href="<c:url value='user'></c:url>" class="btn btn-sm btn-danger">Cancel</a>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    $(document).ready(function(){
        $('#pin').select2();
    });
</script>