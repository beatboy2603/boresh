<%-- 
    Document   : index
    Created on : Mar 19, 2018, 1:59:53 PM
    Author     : asus
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="session" value="${sessionScope.uid}"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" type="text/css" href="${contextPath}/css/index.css">
    </head>
    <body>
        
        <jsp:include page="./template/header.jsp"></jsp:include>
        
        <div id="container-main">
            <div id="container-left">
                <c:forEach items="${requestScope.reviews}" var="review">
                    <div class="container-fluid container-review">
                        <div class="row box-review">
                            <div class="group-info col-sm-10 col-xs-12 group-info-first">
                                <div class="field-title"><a href="#">${review.title}</a></div>
                                <div class="field-content">${review.content}</div>
                                <div>
                                    <div class="field-stat">${review.up} | ${review.down}</div>
                                    <div class="field-owner-date">
                                      <span class="field-owner-name"><a href="#">${review.ownerName}</a></span>
                                      <span class="seperator">-</span>
                                      <span class="field-date">${review.date}</span>
                                    </div>
                                </div>
                            </div>
                            <div id="wrapper-book-${review.reviewID}" data-book-id="${review.bookID}" class="group-info col-sm-2 col-xs-12 group-info-second">
                                
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            
            <div id="container-right">
                <div id="container-search" class="right-item">
                    <input class="ipt-text" type="text" placeholder="Search review"/>
                    <button class="button-box">Search</button>
                </div>
                <div id="container-refresh" class="right-item">
                    <div id="box-refresh">Refresh</div>
                    <div id="box-new-rw"></div>
                </div>
            </div>
        </div>
        
        <script>
        $(document).ready(function() {

            var arrBoxBookThumbnail = document.getElementsByClassName("group-info-second");
            var numStartRw = 0;
            
            for(var i=0; i<arrBoxBookThumbnail.length; ++i) {
                (function () {
                    var elemBoxBookThumbnail = arrBoxBookThumbnail[i];
                    var bookID = elemBoxBookThumbnail.getAttribute("data-book-id");

                    ajaxGoogleBookSingle(bookID, function(book) {
                        var bookTitle = "<div><a href=\"" +getPageContextPath()+ "/book/" +bookID+ "\">" +book.volumeInfo.title+ "</a></div>";
                        var bookThumbnail = "<img src=\"" + book.volumeInfo.imageLinks.thumbnail + "\">";

                        $(elemBoxBookThumbnail).append(bookTitle, bookThumbnail);
                    });
                })();
                ++numStartRw;
            }
            
            var uid = $("#ipt-hidden-uid").val();
            if(!uid) uid = "none";
            var socket = getWebSocketConnection(uid, "index", "none");
            var objResMsg;
            var numNewRw = 0;
            socket.onmessage = function(e) {
                objResMsg = JSON.parse(e.data);
                
                if(objResMsg.action === "update") {
                    if(objResMsg.msg === "post-review") {
                        ++numNewRw;
                        printNumNew();
                    }
                }
            };
            
            function printNumNew() {
                $("#box-new-rw").html(numNewRw);
            }printNumNew();
            function resetNewRw() {
                numNewRw = numNewRw>10?(numNewRw-10):0;
                printNumNew();
            }
            
            function reviewFormat(review) {
                var format = 
                        "<div class=\"container-fluid container-review\">\n\
                            <div class=\"row box-review\">\n\
                                <div class=\"group-info col-sm-10 col-xs-12 group-info-first\">\n\
                                    <div class=\"field-title\"><a href=\"#\">"+review.title+"</a></div>\n\
                                    <div class=\"field-content\">"+review.content+"</div>\n\
                                    <div>\n\
                                        <div class=\"field-stat\">"+review.up+" | "+review.down+"</div>\n\
                                        <div class=\"field-owner-date\">\n\
                                            <span class=\"field-owner-name\"><a href=\"#\">"+review.ownerName+"</a></span>\n\
                                            <span class=\"seperator\">-</span>\n\
                                            <span class=\"field-date\">"+review.date+"</span>\n\
                                        </div>\n\
                                    </div>\n\
                                </div>\n\
                                <div id=\"wrapper-book-"+review.reviewID+"\" data-book-id=\""+review.bookID+"\" class=\"group-info col-sm-2 col-xs-12 group-info-second\">\n\
                                </div>\n\
                            </div>\n\
                        </div>";
                          
                return format;              
            }
            
            $("#container-refresh").click(function() {
                if(numNewRw > 0) {
                    $.ajax({
                        url: getPageContextPath()+"/api/reviews?start="+(++numStartRw),
                        method: "get",
                        dataType: "json",
                        success: function(data) {
                            var arrReviews = data.reviews;

                            for(var i=0; i<arrReviews.length; ++i) {
                                (function() {
                                    var review = arrReviews[i];

                                    $("#container-left").prepend(reviewFormat(review));
                                    
                                    ajaxGoogleBookSingle(review.bookID, function(book) {
                                        var bookTitle = "<div><a href=\"" +getPageContextPath()+ "/book/" +review.bookID+ "\">" +book.volumeInfo.title+ "</a></div>";
                                        var bookThumbnail = "<img src=\"" + book.volumeInfo.imageLinks.thumbnail + "\">";
                                        $("#wrapper-book-"+review.reviewID).append(bookTitle, bookThumbnail);
                                    });
                                })();
                            }
                        }
                    });

                    resetNewRw(numNewRw);
                }
            });
        });
        </script>
    </body>
</html>
