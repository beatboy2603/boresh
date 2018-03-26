/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boresh.servlet;

import com.boresh.dao.AccountDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author asus
 */
@WebServlet(urlPatterns = "/sign-up")
public class ServletSignup extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String uname = request.getParameter("uname");
        String pwd = request.getParameter("pwd");
        String rePwd = request.getParameter("re-pwd");
        String displayName = request.getParameter("display-name");
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        
        // Sign-up input field validation
        if(!rePwd.equals(pwd)) {
            jsonBuilder.add("signupStat", "failed");
            jsonBuilder.add("msgError", "* Password confirmation not matched");
        } else {
            AccountDAO accDAO = new AccountDAO();

            int statSignup = accDAO.checkSignup(uname, pwd, displayName);
            
            if(statSignup < 0) {
                jsonBuilder.add("signupStat", "failed");
                jsonBuilder.add("msgError", "* Server Error");
            } else if(statSignup == 0) {
                jsonBuilder.add("signupStat", "failed");
                jsonBuilder.add("msgError", "* Username's already existed");
            } else {
                jsonBuilder.add("signupStat", "succeed");
                jsonBuilder.add("msgRes", "Signup has succeed");
            }
        }
        
        JsonObject jsonObjResSignup = jsonBuilder.build();
        out.write(jsonObjResSignup.toString());
        
    }
}
