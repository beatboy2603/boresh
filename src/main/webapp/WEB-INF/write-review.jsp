<%-- 
    Document   : write-review
    Created on : Feb 26, 2018, 1:40:29 PM
    Author     : asus
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:set var="review" value="${requestScope.review}"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <jsp:include page="template/header.jsp"></jsp:include>
        
        <input type="hidden" id="ipt-hidden-review-action" value="${requestScope.action}"/>
        <input type="hidden" id="ipt-hidden-book-id" value="${requestScope.bookID}"/>
        <input type="hidden" id="ipt-hidden-review-id" value="${review.reviewID}"/>
        
        <form id="form-do-review" method="post">
            <input type="text" id="ipt-title" placeholder="Enter the title of your review" value="${review.title}"/><br/>
            <textarea id="ipt-textarea-review-content" rows="4" cols="50" placeholder="Enter your review content here">${review.content}</textarea>
            <br/>
            <input type="submit" value="Post Review"/>
        </form>
        
        <script>
            
        $(document).ready(function() {
            var uid = $("#ipt-hidden-uid").val();
            if(!uid) uid = "none";
            
            var bookID = $("#ipt-hidden-book-id").val();
            var action = $("#ipt-hidden-review-action").val();
            var reviewID = $("#ipt-hidden-review-id").val();
            var socket = getWebSocketConnection(uid, "book-do-review", bookID);
            
            socket.onmessage = function(e) {
                var msgObj = JSON.parse(e.data);
                
                if(msgObj.action==="post-review") {
                    alert("Your review has been posted");
                    action = "update-review";
                    reviewID = msgObj.reviewID;
                }else if(msgObj.action==="update-review") {
                    alert("Your review has been update");
                }
            };
                
            $("#form-do-review").submit(function(e) {
                e.preventDefault();
                if(action==="post-review") {
                    doPostReview(bookID, $("#ipt-title").val(), $("#ipt-textarea-review-content").val());
                } else if(action==="update-review") {
                    doUpdateReview(reviewID, $("#ipt-title").val(), $("#ipt-textarea-review-content").val());
                }
            });
                
            function doPostReview(bookID, title, content) {
                var review = {
                   action: action,
                   bookID: bookID,
                   title: title,
                   content: content
                };

                socket.send(JSON.stringify(review));
            }
            
            function doUpdateReview(reviewID, title, content) {
                var review = {
                   action: action,
                   reviewID: reviewID,
                   bookID: bookID,
                   title: title,
                   content: content
                };

                socket.send(JSON.stringify(review));
            }
        });
        </script>
    </body>
</html>
