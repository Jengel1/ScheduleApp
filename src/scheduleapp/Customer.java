/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleapp;

import java.util.Date;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Jared Engelkemier
 */
public class Customer {
    
    private IntegerProperty customerId;
    private StringProperty customerName;
    private IntegerProperty addressId;
    
    //customer info for address
    private StringProperty address;
    private StringProperty city;
    private StringProperty country;
    private StringProperty zipcode;
    private StringProperty phone;
    private StringProperty consultant;
            
    private int active;
    private Date createDate;  //timestamp
    private StringProperty createdBy;
    private Date lastUpdate;  //timestamp
    private StringProperty lastUpdateBy;
    
    //constructor for creating a new customer object
    public Customer (){
        this(0,"",0,"","","","","","",0,null,"",null,"");
    }//end constructor for default values
    
    //constructor to create a customer object when pulling data 
    //from DB to populate ObservableList<Customer> listOfCustomers 
    public Customer (int customerId, String customerName, String address, String phone, String zipcode, String city,
            String country, String consultant){          
        this.customerId = new SimpleIntegerProperty(customerId);  
        this.customerName = new SimpleStringProperty(customerName);
        this.address = new SimpleStringProperty(address);
        this.phone = new SimpleStringProperty(phone);
        this.zipcode = new SimpleStringProperty(zipcode);
        this.city = new SimpleStringProperty(city);
        this.country = new SimpleStringProperty(country);
        this.consultant = new SimpleStringProperty(consultant);
    }//end constructor for Id and Name values
    
    //construntor to create a customer object when putting date to DB
    public Customer (int customerId, String customerName, int addressId, 
            String address, String city, String country, String zipcode, String phone, String consultant,
            int active, Date createDate, String createdBy, Date lastUpdate, String lastUpdateBy){
        this.customerId = new SimpleIntegerProperty(customerId);
        this.customerName = new SimpleStringProperty(customerName);
        this.addressId = new SimpleIntegerProperty(addressId);
        
        //customer info for address
        this.address = new SimpleStringProperty(address);
        this.city = new SimpleStringProperty(city);
//        this.state = new SimpleStringProperty(state);
        this.country = new SimpleStringProperty(country);
        this.zipcode = new SimpleStringProperty(zipcode);
        this.phone = new SimpleStringProperty(phone);
        this.consultant = new SimpleStringProperty(consultant);
        
        this.active = active;
        this.createDate = new Date();
        this.createdBy = new SimpleStringProperty(createdBy);
        this.lastUpdate = new Date();
        this.lastUpdateBy = new SimpleStringProperty(lastUpdateBy);
    }//end constructor
    
    //getter and setter methods for customer attributes
    //public void setCount(int count) {this.count = count;}
    
    public int getCustomerId(){return customerId.get();}
    public void setCustomerId(int customerId){this.customerId.set(customerId);}
    public IntegerProperty customerIdProperty(){return customerId;}
    
    public String getCustomerName(){return customerName.get();}
    public void setCustomerName(String customerName){this.customerName.set(customerName);}
    public StringProperty customerNameProperty(){return customerName;}
    
    public int getAddressId(){return addressId.get();}
    public void setAddressId(int customerId){this.addressId.set(customerId);}
    public IntegerProperty addressIdProperty(){return addressId;}
    
    //address attributes
    public String getAddress(){return address.get();}
    public void setAddress(String address){this.address.set(address);}
    public StringProperty addressProperty(){return address;}
    
    public String getCity(){return city.get();}
    public void setCity(String city){this.city.set(city);}
    public StringProperty cityProperty(){return city;}
    
    public String getCountry(){return country.get();}
    public void setCountry(String country){this.country.set(country);}
    public StringProperty countryProperty(){return country;}
    
    public String getZipcode(){return zipcode.get();}
    public void setZipcode(String zipcode){this.zipcode.set(zipcode);}
    public StringProperty zipcodeProperty(){return zipcode;}
    
    public String getPhone(){return phone.get();}
    public void setPhone(String phone){this.phone.set(phone);}
    public StringProperty phoneProperty(){return phone;}
    
    //rest of customer attributes
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
    
    public String getConsultant(){return consultant.get();}
    public void setConsultant(String consultant){this.consultant.set(consultant);}
    public StringProperty consultantProperty(){return consultant;}
        
}//end Customer