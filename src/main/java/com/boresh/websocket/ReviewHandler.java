/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boresh.websocket;

import com.boresh.dao.AccountDAO;
import com.boresh.dao.ReviewDAO;
import com.boresh.model.Account;
import com.boresh.model.Review;
import com.boresh.util.Logic;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;
import javax.naming.NamingException;
import javax.websocket.Session;

/**
 *
 * @author asus
 */
@ApplicationScoped
public class ReviewHandler {
    private final ReviewDAO reviewDAO = new ReviewDAO();
    
    private final HashMap<Session, String> reviewMap = new HashMap<>();
    private final Set<Session> publicSet = new HashSet<>();
    private final HashMap<Session, String> doReviewMap = new HashMap<>();

    public ReviewHandler() {
    }
    /**
     * Add a session to our HashMap of a client who view a specific book
     * 
     * @param session a Session object of a client
     * @param bookID the id of the book which client is viewing its' reviews
    */
    public void addBookSession(Session session, String bookID) {
        reviewMap.put(session, bookID);
    }
    public void addDoReviewSession(Session session, String bookID) {
        doReviewMap.put(session, bookID);
    }
    public void addPublicView(Session session) {
        publicSet.add(session);
    }
    
    /**
     * Remove a session from our HashMap of a client
     * 
     * @param session a Session object of a client
    */
    public void removeSession(Session session) {
        if(reviewMap.containsKey(session)) {
            reviewMap.remove(session);
        }
        if(doReviewMap.containsKey(session)) {
            doReviewMap.remove(session);
        }
        if(publicSet.contains(session)) {
            publicSet.remove(session);
        }
    }
    /**
     * Create and return a JSON format message from a review, when posting a new review
     * 
     * @param review a Review object
     * @return a JSON format message of review-posting action
    */
    public JsonObject createReviewMsg(Review review) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject msgJson = provider.createObjectBuilder()
                .add("action", "post-review")
                .add("reviewID", review.getReviewID())
                .add("bookID", review.getBookID())
                .add("ownerName", review.getOwnerName())
                .add("title", review.getTitle())
                .add("content", review.getContent())
                .add("date", review.getDate())
                .add("up", Integer.toString(review.getUp()))
                .add("down", Integer.toString(review.getDown()))
                .build();
        
        return msgJson;
    }
    
    public JsonObject createReviewUpdateMsg(Review review) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject msgJson = provider.createObjectBuilder()
                .add("action", "update-review")
                .add("reviewID", review.getReviewID())
                .add("title", review.getTitle())
                .add("content", review.getContent())
                .add("date", review.getDate())
                .build();
        
        return msgJson;
    }
    
    public JsonObject createNotifyMsg(String action, String msg) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("action", action);
        jsonObjectBuilder.add("msg", msg);
        
        JsonObject jsonObject = jsonObjectBuilder.build();
        return jsonObject;
    }
    
    /**
     * Perform action when a user want to add a review
     * 
     * @param review a Review object
     * @param uid
     * @param uname name of username who post the review
    */
    public void addReview(Review review, String uid) {
        
        AccountDAO accountDAO = new AccountDAO();
        Account acc = accountDAO.getAccountByID(uid);
        
        if(acc != null && acc.getId() != null) {
            review.setOwnerName(acc.getDisplayName());
            review.setReviewID(Logic.getRandAccID("rw"));

            int addReviewStat = reviewDAO.addNewReview(review, uid);

            //Send to session on a specified bookID
            if(addReviewStat > 0) {
                //send to review owner
                for(Map.Entry<Session, String> entry : doReviewMap.entrySet()) {
                    sendToSession(entry.getKey(), createReviewMsg(review));
                }
                
                //send to people viewing the book
                for(Map.Entry<Session, String> entry : reviewMap.entrySet()) {
                    if(review.getBookID().equals(entry.getValue())) {
                        sendToSession(entry.getKey(), createReviewMsg(review));
                    }
                }
                
                //send to people on the index page
                for(Session session : publicSet) {
                    sendToSession(session, createNotifyMsg("update", "post-review"));
                }
            }
        }  
    }
    public void updateReview(Review review) {
        int stat = reviewDAO.updateReviewByID(review.getReviewID(), review.getTitle(), review.getContent(), review.getDate());
    
        if(stat > 0) {
            // send to review owner
            for(Map.Entry<Session, String> entry : reviewMap.entrySet()) {
                if(review.getBookID().equals(entry.getValue())) {
                    sendToSession(entry.getKey(), createReviewUpdateMsg(review));
                }
            }
            
            //send to the people viewing the book
            for(Map.Entry<Session, String> entry : doReviewMap.entrySet()) {
                if(review.getBookID().equals(entry.getValue())) {
                    sendToSession(entry.getKey(), createReviewUpdateMsg(review));
                }
            }
            
            // send to people on the index page
            for(Session session : publicSet) {
                sendToSession(session, createNotifyMsg("update", "update-review"));
            }
        }
    }
    
    /**
     * Send a JSON format message to a specific session
     * 
     * @param session a Session object
     * @param msgJson the message want to send, in JSON format
    */
    public void sendToSession(Session session, JsonObject msgJson) {
        try {
            session.getBasicRemote().sendText(msgJson.toString());
        } catch (IOException ex) {
            Logger.getLogger(ReviewHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
