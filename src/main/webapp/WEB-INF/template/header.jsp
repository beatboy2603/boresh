<%-- 
    Document   : header
    Created on : Feb 24, 2018, 10:06:52 PM
    Author     : asus
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<c:set var="session" value="${sessionScope.uid}"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="pathName" value="${pageContext.request.servletPath}"/>

<!--<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>-->
<!--Bootstrap source-->
<link rel="stylesheet" href="${contextPath}/bootstrap/css/bootstrap.min.css">
<script src="${contextPath}/jquery/jquery.min.js"></script>
<script src="${contextPath}/bootstrap/js/bootstrap.min.js"></script>
<!--My source: for header-->
<link rel="stylesheet" type="text/css" href="${contextPath}/css/header.css">

<!--Multi-page purpose hidden field-->
<input type="hidden" id="ipt-hidden-uid" value="${sessionScope.uid}"/>
<input type="hidden" id="ipt-hidden-context-path" value="${contextPath}"/>
<!--Navigation header-bar-->
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#mainNavBar">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span> 
            </button>
            <a class="navbar-brand" href="${contextPath}/index">Boresh</a>
        </div>
        <div class="collapse navbar-collapse" id="mainNavBar">
            <ul class="nav navbar-nav">
                <li id="navbar-item-index"><a href="${contextPath}/index">Home</a></li>
                <li id="navbar-item-search"><a href="${contextPath}/search">Search</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <c:choose>
                    <c:when test="${session == null}">
                <li><a href="#" onclick="document.getElementById('field-form-signup').style.display='block'"><span class="glyphicon glyphicon-user"></span> Sign Up</a></li>
                <li><a href="#" onclick="document.getElementById('field-form-login').style.display='block'"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>
                    </c:when>
                    <c:when test="${session != null}">
                <!-- "href" will be set in Javascript -->
                <li><a id="field-url-signout"><span class="glyphicon glyphicon-log-out"></span> Sign Out</a></li>
                    </c:when>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
<!--Log-in and Sign-up form-->
<c:choose>
    <c:when test="${session == null}">
        <!-- Field Log-in -->
        <div class="field-form-container" id="field-form-login">
            <div class="field-form-box">
                <span onclick="document.getElementById('field-form-login').style.display='none'" class="close" title="Close Modal">&times;</span>
                <p class="field-form-title">Login</p>
                <!--Field Message from Loggin-in-->
                <div id="field-msg-login" class="msg-res msg-error">${errorLogin}</div>
                <form id="form-login" action="${contextPath}/login" method="post">
                    <input type="hidden" id="ipt-hidden-url-login" value="${contextPath}/login"/>
                    <label for="ipt-uname" class="ipt-label">Username</label>
                    <input type="text" id="ipt-uname" class="ipt-text" name="uname" placeholder="Enter your username" required/>

                    <label for="ipt-pwd" class="ipt-label">Password</label>
                    <input type="password" id="ipt-pwd" class="ipt-text" name="pwd" placeholder="Enter your password" required/>

                    <input type="hidden" id="ipt-hidden-last-page" name="last-page" value=""/>
                    <button class="button-box" type="submit">Log-in</button>
                </form>
            </div>
        </div>
        
        <!-- Field Sign-up -->
        <div class="field-form-container" id="field-form-signup">
            <div class="field-form-box">
                <span onclick="document.getElementById('field-form-signup').style.display='none'" class="close" title="Close Modal">&times;</span>
                <p class="field-form-title">Sign up</p>
                <!--Field Message from Signing-up -->
                <div id="field-msg-signup-succeed" class="msg-res  msg-succeed">${errorSignup}</div>
                <div id="field-msg-signup-error" class="msg-res msg-error">${errorSignup}</div>
                <form id="form-signup" action="${contextPath}/sign-up" method="post">
                    <input type="hidden" id="ipt-hidden-url-signup" value="${contextPath}/sign-up"/>
                    <label for="ipt-signup-uname" class="ipt-label">Username</label>
                    <input type="text" id="ipt-signup-uname" class="ipt-text" name="uname" placeholder="Enter your username" required/>
                    
                    <label for="ipt-signup-display-name" class="ipt-label">Display Name</label>
                    <input type="text" id="ipt-signup-display-name" class="ipt-text" name="display-name" placeholder="Your display name" required/>

                    <label for="ipt-signup-pwd" class="ipt-label">Password</label>
                    <input type="password" id="ipt-signup-pwd" class="ipt-text" name="pwd" placeholder="Enter your password" required/>

                    <label for="ipt-signup-repwd" class="ipt-label">Re-Password</label>
                    <input type="password" id="ipt-signup-repwd" class="ipt-text" name="re-pwd" placeholder="Confirm password" required/>
                    <button class="button-box" type="submit">Sign-up</button>
                </form>
            </div>
        </div>
    </c:when>
</c:choose>
        
        <!--My source: for other-page-->
        <script src="${contextPath}/js/util-logic.js"></script>
        <script src="${contextPath}/js/util-dom-manipulate.js"></script>
        <script src="${contextPath}/js/util-ajax.js"></script>
        <script src="${contextPath}/js/util-websocket.js"></script>
        <script>
            $(document).ready(function() {
                
                var strPathCurPage = window.location.pathname;
                
                // Add "active" class to current tab
                var arrPagePath = strPathCurPage.split("/");
                if(arrPagePath.length > 0) {
                    var strLastPath = arrPagePath[arrPagePath.length-1];
                    var elemActiveTab;
                    if(strLastPath === "") {
                        elemActiveTab = document.getElementById("navbar-item-index");
                    } else {
                        elemActiveTab = document.getElementById("navbar-item-" + strLastPath);
                    }
                    if(elemActiveTab) {
                        elemActiveTab.setAttribute("class", "active");
                    }
                }
                
                //initial setup
                // For Login purpose
                var elemIptHiddenLastPage = document.getElementById("ipt-hidden-last-page");
                if(elemIptHiddenLastPage) {
                    elemIptHiddenLastPage.value = strPathCurPage;
                }
                // For Signout purpose
                var fieldUrlSignout = document.getElementById("field-url-signout");
                if(fieldUrlSignout) {
                    fieldUrlSignout.setAttribute("href", $("#ipt-hidden-context-path").val() + "/signout?last-page=" + strPathCurPage);
                }
                
                $("#form-login").submit(function(e) {
                    e.preventDefault();
                    resetResMsg();
                    
                    $.ajax({
                        url: $("#form-login").attr("action"),
                        method: "post",
                        data: {
                            "uname": $("#ipt-uname").val(),
                            "pwd": $("#ipt-pwd").val(),
                            "last-page": $("#ipt-hidden-last-page").val()
                        },
                        dataType: "json",
                        success: function(res) {
                            if(res.loginStat === "succeed") {
                                window.location.href = res.urlRedirect;
                            } else if(res.loginStat === "failed") {
                                $("#field-msg-login").html(res.msgError);
                            }
                        }
                    });
                });
                
                $("#form-signup").submit(function(e) {
                    e.preventDefault();
                    resetResMsg();
                    
                    $.ajax({
                        url: $("#form-signup").attr("action"),
                        method: "post",
                        data: {
                            "uname": $("#ipt-signup-uname").val(),
                            "display-name": $("#ipt-signup-display-name").val(),
                            "pwd": $("#ipt-signup-pwd").val(),
                            "re-pwd": $("#ipt-signup-repwd").val()
                        },
                        dataType: "json",
                        success: function(res) {
                            if(res.signupStat === "succeed") {
                                $("#field-msg-signup-succeed").html(res.msgRes);
                            } else if(res.signupStat === "failed") {
                                $("#field-msg-signup-error").html(res.msgError);
                            }
                        }
                    });
                });
                
                function resetResMsg() {
                    var elemsResMsg = document.getElementsByClassName("msg-res");
                    for(var i=0; i<elemsResMsg.length; ++i) {
                        elemsResMsg[i].innerHTML = "";
                    }
                }
            });
        </script>
