<%-- 
    Document   : login
    Created on : Jan 6, 2015, 12:11:45 PM
    Author     : ataulislam.raihan
--%>
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