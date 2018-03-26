<%-- 
    Document   : reviews
    Created on : Feb 25, 2018, 9:22:28 PM
    Author     : asus
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/book.css">
        <style>
            .review-box {
                border: 1px solid #000;
            }
        </style>
    </head>
    <body>
        <jsp:include page="template/header.jsp"></jsp:include>
        <c:set var="contextPath" value="${pageContext.request.contextPath}"/>
        
        <div id="container-main">
            <!--Book preview-->
            <div id="wrapper-book">
                <!--Left component-->
                <div class="wrapper-left">
                    <div class="box-btn-write-review">
                    <c:choose>
                        <c:when test="${sessionScope.uid != null}">
                            <c:choose>
                                <c:when test="${requestScope.written}"><a href="${contextPath}/write-review/${requestScope.bookID}">Edit Review</a></c:when>
                                <c:otherwise><a href="${contextPath}/write-review/${requestScope.bookID}">Write Review</a></c:otherwise>
                            </c:choose>
                        </c:when>
                    </c:choose>
                    </div>
                    <div class="wrapper-overview">
                        <div class="box-title"><!--title--></div>
                        <div class="box-thumbnail"><!--thumbnail--></div>
                        <div class="box-rating"><!--rating star--></div>
                    </div>
                </div>
                <!--Right component-->
                <div class="wrapper-right">
                    <div class="wrapper-description">
                        <!--description-->
                        <div class="box-categories"><!--categories list--></div>
                    </div>
                </div>
            </div>
            <!--Reviews listing-->
            <div id="wrapper-reviews">
                <c:forEach items="${requestScope.reviews}" var="review">
                    <div id="${review.reviewID}" class="review-box">
                        <i>${review.ownerName}</i><br/>
                        <h4>${review.title}</h4>
                        <p>${review.content}</p>
                    </div>
                </c:forEach>
            </div>
        </div>
        
        <script>
        $(document).ready(function() {
            
            function firstClass(className) {
                return document.getElementsByClassName(className)[0];
            }
            
            function genStar(parent, num) {
                $(parent).html('<span class="stars">'+parseFloat(num)+'</span>');

                $('span.stars').html(
                    $('<span>').width(
                        Math.max(0, (Math.min(5, parseFloat($('span.stars').html())))) * 16
                    )
                );
            }
            
            function printBook(book) {
                
                $(firstClass("box-title")).html(book.volumeInfo.title);
                $(firstClass("box-thumbnail")).append("<img src=\"" +book.volumeInfo.imageLinks.thumbnail+ "\">");
                genStar(firstClass("box-rating"), book.volumeInfo.averageRating);
                
                var bookDescription;
                if(book.volumeInfo.description) {
                    bookDescription = book.volumeInfo.description;
                } else {
                    bookDescription = "No description was found for this book";
                }
                $(firstClass("wrapper-description")).prepend("<p>" +bookDescription+ "</p>");
                
                var bookCategories;
                if(book.volumeInfo.categories) {
                    bookCategories = book.volumeInfo.categories.join(", ");
                } else {
                    bookDescription = "Cate not found";
                }
                $(firstClass("box-categories")).html(bookDescription);
                
            }

            var path = window.location.pathname;
            var pathArr = path.split("/");
            var bookID = pathArr[pathArr.length-1];
            
            // onload function call
            ajaxGoogleBookSingle(bookID, printBook);

            var socket;
            var uid = $("#ipt-hidden-uid").val();
            if(!uid) uid = "none";
            socket = getWebSocketConnection(uid, "book", bookID);
            socket.onmessage = function(e) {
                var objMsg = JSON.parse(e.data);

                if(objMsg.action==="post-review") {
                    printReview(objMsg);
                } else if(objMsg.action==="update-review") {
                    updateReview(objMsg);
                }
            };
            
            function reviewBoxFormat(review) {
                var format = 
                        "<div id=\""+review.reviewID+"\" class=\"review-box\">\n\
                            <i>"+review.ownerName+"</i>\n\
                            <h4>"+review.title+"</h4>\n\
                            <p>"+review.content+"</p>\n\
                        </div>";
        
                return format;
            }
            function printReview(review) {
                $("#wrapper-reviews").append(reviewBoxFormat(review));
            }
            function updateReview(review) {
                $("#"+review.reviewID+" h4").html(review.title);
                $("#"+review.reviewID+" p").html(review.content);
            }

        });
        </script>
    </body>
</html>
