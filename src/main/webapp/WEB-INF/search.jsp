<%-- 
    Document   : index
    Created on : Feb 26, 2018, 1:40:29 PM
    Author     : asus
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <c:set var="contextPath" value="${pageContext.request.contextPath}"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        
        <style>
            #field-search-container {
                width: 100%;
            }
            #field-search-box {
                width: 30%;
                margin: auto;
            }
            #form-search {}
            #form-search input {
                padding: 10px;
                display: block;
            }
            #ipt-keyword {
                width: 100%;
            }
            #btn-search {
                width: 100%;
            }
            
            #field-book-result {
                margin: auto;
                width: 70%;
                border: 1px solid #969696;
                padding: 10px;
                border-radius: 2px;
            }
        </style>
    </head>
    <body>
        <jsp:include page="template/header.jsp"></jsp:include>
        
        <div id="field-search-container">
            <div id="field-search-box">
                <form id="form-search" method="get">
                    <input type="text" id="ipt-keyword" placeholder="Type book keywords"/>
                    <button type="submit" class="button-box"  id="btn-search">Search</button>
                </form>
            </div>
        </div>
        <br/>
        
        <div id="field-book-result">
        </div>
        
        <script src="${contextPath}/js/search.js"></script>
    </body>
</html>
