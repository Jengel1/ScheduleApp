/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleapp;

import java.util.Date;
import java.util.Locale;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Jared Engelkemier
 */
public class User {
    
    private IntegerProperty userID;
    private String username;
    private String password;
    private Locale userLocale;
    private static int count = 100;
    
    private int active;
    private Date createDate;  //timestamp
    private StringProperty createdBy;
    private Date lastUpdate;  //timestamp
    private StringProperty lastUpdateBy;
    
    
    public User (String username, String password){
        this.username = new String(username);
        this.password = new String(password);
    }
    
    //constructor
    public User(int userID, String username, String password, Locale userLocale){
        this.userID = new SimpleIntegerProperty(++count);
        this.username = new String(username);
        this.password = new String(password);
        this.userLocale = new Locale.Builder().setLanguage("en").setRegion("US").build();
    }//end constructor
    
    //getter and setter methods for user attributes
    public int getUserID() {return userID.get();}
    public void setPartID(int userID) {this.userID.set(userID);}
    public IntegerProperty userIDProperty() {return userID;}
    
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    
    //rest of user attributes
    public int getActive(){return active;}
    public void setActive(int active){this.active = active;}
    
    public Date getCreateDate(){return createDate;}
    
    public String getCreatedBy(){return createdBy.get();}
    public void setCreatedBy(String createdBy){this.createdBy.set(createdBy);}
    public StringProperty createdByProperty(){return createdBy;}
    
    public Date getLastUpdate(){return lastUpdate;}
    
    public String getLastUpdateBy(){return lastUpdateBy.get();}
    public void setLastUpdateBy(String lastUpdateBy){this.lastUpdateBy.set(lastUpdateBy);}
    public StringProperty lastUpdateByProperty(){return lastUpdateBy;}
    
}//end user