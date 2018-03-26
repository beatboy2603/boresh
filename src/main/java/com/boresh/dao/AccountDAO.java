package com.boresh.dao;

import com.boresh.model.Account;
import com.boresh.util.DBContext;
import com.boresh.util.Logic;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author asus
 */
public class AccountDAO {
    
    private Connection conn;
    private DataSource dataSource;
    private PreparedStatement ps;

    public AccountDAO() {
        try {
            conn = null;
            Context context = new InitialContext();
            dataSource = (DataSource) context.lookup("java:/MySqlDStwo");
        } catch (NamingException ex) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Account getAccountByID(String uid) {
        Account acc = new Account();
        
        try {
            conn = dataSource.getConnection("sa", "sa");
            
            ps = conn.prepareStatement("select * from account where user_id=?");
            ps.setString(1, uid);
            
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    acc.setId(rs.getString("user_id"));
                    acc.setUname("username");
                    acc.setPwd("pwd");
                    acc.setDisplayName(rs.getString("display_name"));
                    acc.setDob("dob");
                }
            }
            
            ps.close();        
        } catch (SQLException ex) { 
            acc = null;
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex); 
        }
        
        // Close requested datasource
        if(conn != null) {
            try { conn.close(); } 
            catch (SQLException ex) {
                Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return acc;
    }
    
    /**
     * Check if the client input username and password is available
     * 
     * @param uname username
     * @param  pwd password
     * @return -1 if there is a server error
     * , 0 if log-in input is not valid
     * , 1 if log-in input valid
    */
    public Account checkLogin(String uname, String pwd) {
        Account acc = new Account();
        
        try {
            conn = dataSource.getConnection("sa", "sa");
            
            ps = conn.prepareStatement("select * from account where username=?"
                    + " and password=?");
            ps.setString(1, uname);
            ps.setString(2, pwd);
            
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    acc.setId(rs.getString("user_id"));
                    acc.setDisplayName(rs.getString("display_name"));
                    acc.setUname("username");
                }
            }
            
            ps.close();        
        } catch (SQLException ex) { 
            acc = null;
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex); 
        }
        
        // Close requested datasource
        if(conn != null) {
            try { conn.close(); } 
            catch (SQLException ex) {
                Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return acc;
    }
    
    /**
     *
     * Check if the sign-up info is valid
     * @param uname sign-up input username
     * @param pwd sign-up input password
     * @param displayName sign-up input display-name
     * @return -1 if there is a server error
     * , 0 if sign-up username has already existed
     * , 1 if sign-up input valid
     */
    public int checkSignup(String uname, String pwd, String displayName) {
        int stat = 0;
        try {
            conn = dataSource.getConnection("sa", "sa");

            ps = conn.prepareStatement("select * from account where username=?");
            ps.setString(1, uname);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                stat = 0;
            } else {
                Account acc = new Account(
                        Logic.getRandAccID("u"), 
                        uname, pwd, 
                        displayName, 
                        new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));

                ps = conn.prepareStatement("insert into account values(?,?,?,?,?)");
                ps.setString(1, acc.getId());
                ps.setString(2, acc.getUname());
                ps.setString(3, acc.getPwd());
                ps.setString(4, acc.getDisplayName());
                ps.setString(5, acc.getDob());

                stat = ps.executeUpdate();
            }

            ps.close();
            
        } catch (SQLException ex) { 
            stat = -1;
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Close requested datasource
        if(conn != null) {
            try { conn.close(); } 
            catch (SQLException ex) {
                Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return stat;
    }
    
}
