/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boresh.servlet;

import com.boresh.dao.AccountDAO;
import com.boresh.dao.ReviewDAO;
import com.boresh.model.PathHandler;
import com.boresh.model.Review;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author asus
 */
@WebServlet(urlPatterns = "/book/*")
public class ServletViewBook extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        
        ReviewDAO reviewDAO = new ReviewDAO();
        
        String bookID = PathHandler.getBookID(request.getRequestURI());
        String userID = (String) request.getSession().getAttribute("uid");
        int hasReviewedStat = reviewDAO.hasReviewed(bookID, userID);
        boolean written = true;
        List<Review> reviews = reviewDAO.getReviewByBookID(bookID);
                
        if(reviews == null || hasReviewedStat < 0) {
            throw new ServletException();
        }
        
        request.setAttribute("reviews", reviews);
        written = (hasReviewedStat != 0);
        request.setAttribute("written", written);
        request.setAttribute("bookID", bookID);
        
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/book.jsp");
        rd.include(request, response);
    }
}
