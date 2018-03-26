/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boresh.servlet;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author asus
 */
@WebServlet(urlPatterns = "/signout")
public class ServletSignout extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        // Invalidate any available session, redirect to last-view page
        HttpSession session = request.getSession();
        String uname = (String) session.getAttribute("uid");
        if(uname != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getParameter("last-page"));
    }
}
