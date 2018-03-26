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
public class PathHandler {
    
    public static String getBookID(String absolutePath) {
        String[] portions = absolutePath.split("/");
        String bookID = portions[portions.length-1];
        
        return bookID;
    }
}
