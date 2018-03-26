package com.boresh.servlet;

import com.boresh.dao.AccountDAO;
import com.boresh.model.Account;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author asus
 */
@WebServlet(urlPatterns = "/login")
public class ServletLogin extends HttpServlet{
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String uname = request.getParameter("uname");
        String pwd = request.getParameter("pwd");
        String lastPagePath = request.getParameter("last-page");
        
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        
        AccountDAO accDAO = new AccountDAO();
        Account acc = accDAO.checkLogin(uname, pwd);

        // Check Log-in input and setting up session
        if(acc == null) {
            jsonBuilder.add("loginStat", "failed");
            jsonBuilder.add("msgError", "* Server Error");
        } else if(acc.getId() != null) {
            HttpSession session = request.getSession();
            session.setAttribute("uid", acc.getId());
            session.setAttribute("dname", acc.getDisplayName());

            jsonBuilder.add("loginStat", "succeed");
            jsonBuilder.add("urlRedirect", lastPagePath);
        } else {
            jsonBuilder.add("loginStat", "failed");
            jsonBuilder.add("msgError", "* Invalid log-in field(s)");
        }

        JsonObject objJsonLogin = jsonBuilder.build();
        out.write(objJsonLogin.toString());
            
    }
}
