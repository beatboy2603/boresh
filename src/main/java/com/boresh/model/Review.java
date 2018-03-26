/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boresh.model;

/**
 *
 * @author asus
 */
public class Review {
    private String reviewID;
    private String bookID;
    private String ownerName;
    private String title;
    private String content;
    private String date;
    private int up;
    private int down;

    public Review() {
        this(null, null, null, null, null, null, 0, 0);
    }

    public Review(String reviewID, String bookID, String ownerName, String title, String content, String date, int up, int down) {
        this.reviewID = reviewID;
        this.bookID = bookID;
        this.ownerName = ownerName;
        this.title = title;
        this.content = content;
        this.date = date;
        this.up = up;
        this.down = down;
    }

    public String getReviewID() {
        return reviewID;
    }

    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUp() {
        return up;
    }

    public void setUp(int up) {
        this.up = up;
    }

    public int getDown() {
        return down;
    }

    public void setDown(int down) {
        this.down = down;
    }
    
    
}
