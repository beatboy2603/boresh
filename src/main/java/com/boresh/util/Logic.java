/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boresh.util;

import java.io.File;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import javax.servlet.ServletContext;

/**
 *
 * @author asus
 */
public class Logic {
    public static String getRandAccID(String startStr) {
        Date dateNow = new Date();
        char randChar = (char) (ThreadLocalRandom.current().nextInt(65, 90 + 1));
        String shopID = startStr + randChar + dateNow.getTime();
        
        return shopID;
    }
    
    public static String getRandNum() {
        Date dateNow = new Date();
        
        return String.valueOf(dateNow.getTime()/100);
    }
    
}
