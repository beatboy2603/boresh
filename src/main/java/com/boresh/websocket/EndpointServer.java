/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boresh.websocket;

import com.boresh.model.Review;
import java.io.StringReader;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.*;
import javax.websocket.server.*;

/**
 *
 * @author asus
 */
@ApplicationScoped
@ServerEndpoint(value = "/actions/{uid}/{page}/{id}")
public class EndpointServer {
    
    private final HashMap<String, String> accs = new HashMap<>();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    @Inject
    private ReviewHandler reviewHandler;

    @OnOpen
    public void onOpen(Session session, @PathParam("uid") String uid, 
            @PathParam("page") String page, @PathParam("id") String id) {
        accs.put(session.getId(), uid);
        
        if("book".equals(page)) {
            reviewHandler.addBookSession(session, id);
        } else if("book-do-review".equals(page)) {
            reviewHandler.addDoReviewSession(session, id);
        } else if("index".equals(page)) {
            reviewHandler.addPublicView(session);
        }
    }
    @OnClose
    public void onClose(Session session) {
        reviewHandler.removeSession(session);
        accs.remove(session.getId());
    }
    
    @OnMessage
    public void onMessage(String msg, Session session) {
        try(JsonReader reader = 
                Json.createReader(new StringReader(msg))) {
            JsonObject jsonMsg = reader.readObject();
            
            String action = jsonMsg.getString("action");
            if("post-review".equals(action)) {
                Review review = new Review();
                review.setBookID(jsonMsg.getString("bookID"));
                review.setTitle(jsonMsg.getString("title"));
                review.setContent(jsonMsg.getString("content"));
                review.setDate(sdf.format(new Date()));
                review.setUp(0);
                review.setDown(0);
                
                reviewHandler.addReview(review, accs.get(session.getId()));
            } else if("update-review".equals(action)) {
                Review review = new Review();
                review.setReviewID(jsonMsg.getString("reviewID"));
                review.setBookID(jsonMsg.getString("bookID"));
                review.setTitle(jsonMsg.getString("title"));
                review.setContent(jsonMsg.getString("content"));
                review.setDate(sdf.format(new Date()));
                
                reviewHandler.updateReview(review);
            }
        }
    }

}
