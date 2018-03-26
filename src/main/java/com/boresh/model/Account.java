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
public class Account {
    private String id;
    private String uname;
    private String pwd;
    private String displayName;
    private String dob;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Account(String id, String uname, String pwd, String displayName, String dob) {
        this.id = id;
        this.uname = uname;
        this.pwd = pwd;
        this.displayName = displayName;
        this.dob = dob;
    }

    public Account() {
        this(null, null, null, null, null);
    }
}
