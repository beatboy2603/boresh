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
@WebServlet(urlPatterns = "/write-review/*")
public class ServletWriteReview extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        
        String bookID = PathHandler.getBookID(request.getRequestURI());
        String userID = (String) request.getSession().getAttribute("uid");
        ReviewDAO reviewDAO = new ReviewDAO();
        int reviewed = reviewDAO.hasReviewed(bookID, userID);
        
        if(reviewed < 0) throw new ServletException();
        else if(reviewed == 0) {
            request.setAttribute("action", "post-review");
        } else {
            Review review = reviewDAO.getReviewByPrimaryKey(bookID, userID);
            if(review != null && review.getReviewID() != null) {
                request.setAttribute("action", "update-review");
                request.setAttribute("review", review);
            }
        }
        request.setAttribute("bookID", bookID);
        
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/write-review.jsp");
        rd.include(request, response);
    } 
}
