/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boresh.dao;

import com.boresh.model.Review;
import com.boresh.util.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author asus
 */
public class ReviewDAO {
    
    private Connection conn;
    private DataSource dataSource;
    private PreparedStatement ps;

    public ReviewDAO() {
        try {
            conn = null;
            Context context = new InitialContext();
            dataSource = (DataSource) context.lookup("java:/MySqlDStwo");
        } catch (NamingException ex) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     *
     * @param start
     * @param total
     * @return
     */
    public List<Review> getReviews(int start, int total, String order) {
        List<Review> reviews = new ArrayList<>();
        
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement("select display_name, book_id, review_id, title, content, date, up, down"
                    + " from review, account "
                    + "where review.user_id=account.user_id "
                    + "order by date " + order + " "
                    + "limit ?, ?");
            ps.setInt(1, start-1);
            ps.setInt(2, total);
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                reviews.add(new Review(rs.getString("review_id"), 
                        rs.getString("book_id"), rs.getString("display_name"), 
                        rs.getString("title"), rs.getString("content"), rs.getString("date"), 
                        rs.getInt("up"), rs.getInt("down")));
            }
            
        } catch (SQLException ex) {
            reviews = null;
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(conn != null) try { conn.close(); } 
        catch (SQLException ex) {
            Logger.getLogger(ReviewDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return reviews;
    }
    
    /**
     * Return a List of reviews on a single book by the book 's id
     * 
     * @param bookID the id of the book which reviews we want to get
     * @return the list contains reviews, null if there is a server error
    */
    public List<Review> getReviewByBookID(String bookID) {
        List<Review> reviews = new ArrayList<>();
        try {
            conn = dataSource.getConnection("sa", "sa");
            
            ps = conn.prepareStatement("select * from review, account where book_id=? and review.user_id=account.user_id");
            ps.setString(1, bookID);
            
            try (ResultSet rs = ps.executeQuery()) {
                Review review;
                while(rs.next()) {
                    review = new Review();
                    review.setBookID(rs.getString("book_id"));
                    review.setOwnerName(rs.getString("display_name"));
                    review.setReviewID(rs.getString("review_id"));
                    review.setTitle(rs.getString("title"));
                    review.setContent(rs.getString("content"));
                    review.setDate(rs.getString("date"));
                    review.setUp(rs.getInt("up"));
                    review.setDown(rs.getInt("down"));
                    reviews.add(review);
                }
            }
            ps.close();
            
        } catch (SQLException ex) {
            reviews = null;
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(conn != null) {
            try { conn.close(); } 
            catch(SQLException ex) { 
                Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return reviews;
    }
    
    public Review getReviewByPrimaryKey(String bookID, String userID) {
        Review review = new Review();
        
        try {
            conn = dataSource.getConnection("sa", "sa");
            ps = conn.prepareStatement("select * from review where book_id=? and user_id=?");
            ps.setString(1, bookID);
            ps.setString(2, userID);
            
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    review.setReviewID(rs.getString("review_id"));
                    review.setTitle(rs.getString("title"));
                    review.setContent(rs.getString("content"));
                }
            }
            
        } catch (SQLException ex) {
            review = null;
            Logger.getLogger(ReviewDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(conn != null) {
            try { conn.close(); } 
            catch(SQLException ex) { 
                Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return review;
    }
    
    /**
     * Add a new review-post to database
     * @param review
     * @param userID
     * @return 0 if no new review is added, -1 if there is a server error, > 0 if review added successfully
     */
    public int addNewReview(Review review, String userID) {
        int addReviewStat = 0;
        
        try {    
            conn = dataSource.getConnection("sa", "sa");
            
            ps = conn.prepareStatement("insert into review values(?,?,?,?,?,?,?,?)");
            ps.setString(1, review.getBookID());
            ps.setString(2, userID);
            ps.setString(3, review.getReviewID());
            ps.setString(4, review.getTitle());
            ps.setString(5, review.getContent());
            ps.setString(6, review.getDate());
            ps.setInt(7, review.getUp());
            ps.setInt(8, review.getDown());
            
            addReviewStat = ps.executeUpdate();
            
            ps.close();
            
        } catch (SQLException ex) {
            addReviewStat = -1;
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(conn != null) {
            try { conn.close(); } 
            catch(SQLException ex) { 
                Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return addReviewStat;
    }
    
    public int updateReviewByID(String reviewID, String title, String content, String date) {
        int stat;
        
        try {
            conn = dataSource.getConnection("sa", "sa");
            ps = conn.prepareStatement("update review set title=?, content=?, date=? "
                    + "where review_id=?");
            ps.setString(1, title);
            ps.setString(2, content);
            ps.setString(3, date);
            ps.setString(4, reviewID);
            
            stat = ps.executeUpdate();
            
            ps.close();
        } catch (SQLException ex) {
            stat = -1;
            Logger.getLogger(ReviewDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(conn != null) {
            try { conn.close(); } 
            catch(SQLException ex) { 
                Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return stat;
    }
    
    public int hasReviewed(String bookID, String userID) {
        int stat = 0;
        
        try {    
            conn = dataSource.getConnection("sa", "sa");
            
            ps = conn.prepareStatement("select * from review where book_id=? and user_id=?");
            ps.setString(1, bookID);
            ps.setString(2, userID);
            
            if(ps.executeQuery().next()) {
                stat = 1;
            } else {
                stat = 0;
            }
            
            ps.close();
            
        } catch (SQLException ex) {
            stat = -1;
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(conn != null) {
            try { conn.close(); } 
            catch(SQLException ex) { 
                Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return stat;
    }
}
