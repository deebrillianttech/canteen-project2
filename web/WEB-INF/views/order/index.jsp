<%-- 
    Document   : index
    Created on : Mar 12, 2015, 11:08:51 AM
    Author     : ataulislam.raihan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<legend><i class="fa fa-pencil-square-o"></i> Today's Orders</legend>

<c:choose>
    <c:when test="${curOrder == null}">
        Please select an order.
    </c:when>
    <c:otherwise>
        <h3>
            <span class="label label-warning">PIN ${curOrder.userPin}</span>
            <span class="label label-primary">Balance ${balance}</span>
            <span class="label label-success">Token ${curOrder.id}</span>
        </h3>
        <c:choose>
            <c:when test="${fn:length(curOrder.foods) lt 1}">
                <p>No food ordered</p>
            </c:when>

            <c:otherwise>
                <ul class="list-group">
                    <c:forEach items="${curOrder.foods}" var="food">
                        <li class="list-group-item">
                            ${food.title}
                            <strong><span class="label label-default pull-right">BDT ${food.price}</span></strong>
                        </li>
                    </c:forEach>
                </ul>

                <h3><span class="label label-danger pull-right">Total ${curOrder.totalPrice}</span></h3>
            </c:otherwise>
        </c:choose>

        <div class="clearfix"></div>
        <br />

        <!-- Payment -->
        <div class="panel panel-primary text-center">
            <div class="panel-body">
                <form id="frmPay" class="form-inline" action="?a=add" method="POST">
                    <div class="form-group">
                        <label class="sr-only" for="exampleInputAmount">Amount (in BDT)</label>
                        <div class="input-group">
                            <div class="input-group-addon"><strong>৳</strong></div>
                            <input type="number" class="form-control" id="amount" name="amount" placeholder="Amount (in BDT)">
                            <div class="input-group-addon">.00</div>
                        </div>
                    </div>
                    <input type="hidden" id="payWhen" name="payWhen" value="" />
                    <input type="hidden" id="orderId" name="orderId" value="${curOrder.id}" />
                    <input type="hidden" id="userId" name="userId" value="${curOrder.userPin}" />
                    <button name="now" class="btn btn-primary" onclick="return submitBtn(this);">Pay Now!</button>
                    <button name="later" class="btn btn-info" onclick="return submitBtn(this);">Pay Later!</button>
                </form>
            </div>
        </div>
        <script type="text/javascript">
            function submitBtn(btn){
                var name = $(btn).attr('name');
                
                var res = confirm('Are you sure you want to submit?');
                
                if(res) {
                    $('#payWhen').val(name);
                    $('#frmPay').submit();
                }
                return false;
            }
            
            $(document).ready(function(){
                $('#frmPay').on('submit', function(e){
                    if($('#payWhen').val() === ''){
                        e.preventDefault();
                    }
                });
            });
        </script>
    </c:otherwise>
</c:choose>