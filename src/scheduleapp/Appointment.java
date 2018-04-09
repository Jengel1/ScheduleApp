/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleapp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Jared Engelkemier
 */
public class Appointment {
    
    private IntegerProperty apptId;
    private StringProperty apptTitle;
    private StringProperty apptType;  //appointment description
    private StringProperty apptLocation;
    private StringProperty apptContact;
    private StringProperty apptURL;
    
    private LocalDateTime apptStart;
    private LocalDateTime apptEnd;
    
    private Date createDate;  //timestamp
    private StringProperty createdBy;
    private Date lastUpdate;  //timestamp
    private StringProperty lastUpdateBy;
    private StringProperty consultant;
    private StringProperty customerName;
    
    //constructor for creating new Appointment object
    public Appointment (){
        this(0,"","","","","","",null,null);
    }//end default constructor
    
    //constructor for pulling appointment from DB
    public Appointment(int apptId, String apptTitle, String apptType, String apptLocation, String apptContact,
            String customerName, String consultant, LocalDateTime apptStart, LocalDateTime apptEnd){
        this.apptId = new SimpleIntegerProperty(apptId);
        this.apptTitle = new SimpleStringProperty(apptTitle);
        this.apptType = new SimpleStringProperty(apptType);
        this.apptLocation = new SimpleStringProperty(apptLocation);
        this.apptContact = new SimpleStringProperty(apptContact);
        this.customerName = new SimpleStringProperty(customerName);  //customerName
        this.consultant = new SimpleStringProperty(consultant);
        this.apptStart = apptStart;
        this.apptEnd = apptEnd;
    }//end constructor
    
    //constructor for putting appointment to DB
    public Appointment (int apptId, String apptTitle, String apptType, String apptLocation, String apptContact,
            String apptURL, LocalDate apptStartDate, LocalDate apptEndDate, Date createDate, String createdBy, Date lastUpdate,
            String lastUpdateBy, String consultant){
        this.apptId = new SimpleIntegerProperty(apptId);
        this.apptTitle = new SimpleStringProperty(apptTitle);
        this.apptType = new SimpleStringProperty(apptType);
        this.apptLocation = new SimpleStringProperty(apptLocation);
        this.apptContact = new SimpleStringProperty(apptContact);
        this.apptURL = new SimpleStringProperty(apptURL);
        this.createDate = new Date();
        this.createdBy = new SimpleStringProperty(createdBy);
        this.lastUpdate = new Date();
        this.lastUpdateBy = new SimpleStringProperty(lastUpdateBy);
        this.consultant = new SimpleStringProperty(consultant);
    }//end constructor
    
    //getter and setter methods for appointment attributes
    public int getApptId(){return apptId.get();}
    public void setApptId(int apptId){this.apptId.set(apptId);}
    public IntegerProperty apptIdProperty(){return apptId;}
    
    public String getApptTitle(){return apptTitle.get();}
    public void setApptTitle(String apptTitle){this.apptTitle.set(apptTitle);}
    public StringProperty apptTitleProperty(){return apptTitle;}
    
    public String getApptType(){return apptType.get();}
    public void setApptType(String apptType){this.apptType.set(apptType);}
    public StringProperty apptTypeProperty(){return apptType;}
    
    public String getApptLocation(){return apptLocation.get();}
    public void setApptLocation(String apptLocation){this.apptLocation.set(apptLocation);}
    public StringProperty apptLocationProperty(){return apptLocation;}
    
    public String getApptContact(){return apptContact.get();}
    public void setApptContact(String apptContact){this.apptContact.set(apptContact);}
    public StringProperty apptContactProperty(){return apptContact;}
    
    public String getCustomerName(){return customerName.get();}
    public void setCustomerName(String customerName){this.customerName.set(customerName);}  
    public StringProperty customerNameProperty(){return customerName;}
    
    public String getApptURL(){return apptURL.get();}
    public void setApptURL(String apptURL){this.apptURL.set(apptURL);}
    public StringProperty apptURLProperty(){return apptURL;}
    
    public LocalDateTime getApptStart(){return apptStart;}
    public void setApptStart(LocalDateTime apptStart){this.apptStart = apptStart;}
    public LocalDateTime getApptEnd() {return apptEnd;}
    public void setApptEnd(LocalDateTime apptEnd){this.apptEnd = apptEnd;}
    
    //method called in MainScrn controller to display date property in appointment table
    public StringProperty getDateTimeAsStringProperty(){
        LocalDateTime ldt = getApptStart();
        String stringDateTime = ldt.toString();
            String year = stringDateTime.substring(0, 4);  //yyyy
            String month = stringDateTime.substring(5, 7);  //MM
            String day = stringDateTime.substring(8, 10);
        String formattedDateTime = month + "/" + day + "/" + year;
        StringProperty p = new SimpleStringProperty(formattedDateTime); 
        return p;
    }//end getDateTimeAsString
    
    public Date getCreateDate(){return createDate;}
    
    public String getCreatedBy(){return createdBy.get();}
    public void setCreatedBy(String createdBy){this.createdBy.set(createdBy);}
    public StringProperty createdByProperty(){return createdBy;}
    
    public Date getLastUpdate(){return lastUpdate;}
    
    public String getLastUpdateBy(){return lastUpdateBy.get();}
    public void setLastUpdateBy(String lastUpdateBy){this.lastUpdateBy.set(lastUpdateBy);}
    public StringProperty lastUpdateByProperty(){return lastUpdateBy;}
    
    public String getConsultant(){return consultant.get();}
    public void setConsultant(String consultant){this.consultant.set(consultant);}
    public StringProperty consultantProperty(){return consultant;}
    
}//end Appointment