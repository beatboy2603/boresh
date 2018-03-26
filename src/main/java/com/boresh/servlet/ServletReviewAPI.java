/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boresh.servlet;

import com.boresh.dao.ReviewDAO;
import com.boresh.model.Review;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author asus
 */
@WebServlet(urlPatterns = "/api/reviews")
public class ServletReviewAPI extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        
        int numStartRw = Integer.parseInt(request.getParameter("start"));
        ReviewDAO reviewDAO = new ReviewDAO();
        
        List<Review> reviews = reviewDAO.getReviews(numStartRw, 10, "ASC");
        
        JsonObjectBuilder result = Json.createObjectBuilder();
        JsonObjectBuilder reviewItem;
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for(Review review : reviews) {
            reviewItem = Json.createObjectBuilder();
            reviewItem.add("bookID", review.getBookID());
            reviewItem.add("reviewID", review.getReviewID());
            reviewItem.add("title", review.getTitle());
            reviewItem.add("content", review.getContent());
            reviewItem.add("up", review.getUp());
            reviewItem.add("down", review.getDown());
            reviewItem.add("ownerName", review.getOwnerName());
            reviewItem.add("date", review.getDate());
            jsonArrayBuilder.add(reviewItem);
        }
        
        result.add("reviews", jsonArrayBuilder);
        
        JsonObject jsonObject = result.build();
        PrintWriter out = response.getWriter();
        out.write(jsonObject.toString());
    }
}
