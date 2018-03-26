/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boresh.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author asus
 */
public class DBContext {
    private static String host = "mysql://localhost";
    private static String dbName = "boresh";
    private static String user = "sa";
    private static String pwd = "sa";
    
    public static Connection getConnection() 
            throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:" +host+ "/" +dbName+ "?user=" +user+ "&password=" +pwd;
        return DriverManager.getConnection(url);
    }
}
