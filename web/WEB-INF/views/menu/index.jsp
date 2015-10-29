<%-- 
    Document   : index
    Created on : Mar 4, 2015, 10:13:37 AM
    Author     : ataulislam.raihan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<legend><i class="fa fa-cutlery"></i> Menu</legend>

<c:if test="${lastOrder != null}">
    Order created. Your token number is ${lastOrder.id}
</c:if>
    
<div id="menu">
    <c:forEach items="${foods}" var="food">
        <div class="media">
            <div class="media-left">

                <c:choose>
                    <c:when test='${fn:length(food.image) < 1}'>
                        <img data-holder-rendered="true" src="data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/PjxzdmcgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB3aWR0aD0iNjQiIGhlaWdodD0iNjQiIHZpZXdCb3g9IjAgMCA2NCA2NCIgcHJlc2VydmVBc3BlY3RSYXRpbz0ibm9uZSI+PGRlZnMvPjxyZWN0IHdpZHRoPSI2NCIgaGVpZ2h0PSI2NCIgZmlsbD0iI0VFRUVFRSIvPjxnPjx0ZXh0IHg9IjEyLjUiIHk9IjMyIiBzdHlsZT0iZmlsbDojQUFBQUFBO2ZvbnQtd2VpZ2h0OmJvbGQ7Zm9udC1mYW1pbHk6QXJpYWwsIEhlbHZldGljYSwgT3BlbiBTYW5zLCBzYW5zLXNlcmlmLCBtb25vc3BhY2U7Zm9udC1zaXplOjEwcHQ7ZG9taW5hbnQtYmFzZWxpbmU6Y2VudHJhbCI+NjR4NjQ8L3RleHQ+PC9nPjwvc3ZnPg==" style="width: 64px; height: 64px;" class="media-object" data-src="holder.js/64x64" alt="64x64">
                    </c:when>
                    <c:otherwise>
                        <a data-toggle="modal" href='#modal-food-${food.id}'>
                            <img data-holder-rendered="true"
                                src='<c:url value="food-items/${food.image}" />'
                                style="max-width: 64px; max-height: 64px;"
                                class="media-object" data-src="holder.js/64x64" alt="64x64">
                        </a>

                        <div class="modal fade" id="modal-food-${food.id}">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header" style="border: none;">
                                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                    </div>
                                    <div class="modal-body" style="padding: 0; text-align: center;">
                                        <img src='<c:url value="food-items/${food.image}" />' style="width: 100%;"
                                             title="${food.title}"/>
                                    </div>
                                </div><!-- /.modal-content -->
                            </div><!-- /.modal-dialog -->
                        </div><!-- /.modal -->
                    </c:otherwise>
                </c:choose>

            </div>
            <div class="media-body">
                <h4 class="media-heading">${food.title}</h4>
                <span class="label label-info" title="Price">BDT ${food.price}</span>
                <span class="label label-default" title="Servings left">Servings Left ${food.servingsLeft}</span>
                <p>${food.description}</p>
            </div>
            <div class="media-right">
                <c:if test="${food.servingsLeft > 0}">
                    <a href='<c:url value="menu?a=add&i=${food.id}"></c:url>' class="btn btn-sm btn-warning" title="Add to tray">
                        <span class="glyphicon glyphicon-plus-sign"></span> Add
                    </a>
                </c:if>
            </div>
        </div>
    </c:forEach>
</div>