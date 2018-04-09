/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleapp;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static scheduleapp.ScheduleApp.connectToDB;
import static scheduleapp.ScheduleApp.getTimestamp;

/**
 * FXML Controller class
 *
 * @author Jared Engelkemier
 */
public class AddCustomerController implements Initializable {
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    private Stage primaryStage;
    private Customer customer;
    public static ScheduleApp scheduleApp;
    
    @FXML
    private TextField customerIdTextField;
    @FXML
    private TextField customerNameTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField zipcodeTextField;
    @FXML
    private TextField phoneTextField;
    //text fields filled in with menu btns
    @FXML
    public TextField cityTextField;
    @FXML
    private TextField consultantTextField;
    @FXML
    public TextField countryTextField;
    
    /*
    //methods to handle City, Country, and Consultant Menu Button's Menu Item events
    */
    @FXML
    private void handleUserMenuItems(ActionEvent event){
        MenuItem source = (MenuItem) event.getSource();
        consultantTextField.setText(source.getText());
    }//end handleUserMenuItems
    @FXML
    private void handleCityMenuItem(ActionEvent event){
        MenuItem source = (MenuItem) event.getSource();
        cityTextField.setText(source.getText());
    }//end handleCityMenuItems
    @FXML
    private void handleCountryMenuItem(ActionEvent event){
        MenuItem source = (MenuItem) event.getSource();
        countryTextField.setText(source.getText());
    }//end handleCountryMenuItems
    
    /*
    //method places new customer object into the add customer controller
    */
    public void handleAddCustomerToController(Customer customer){
        this.customer = customer;
        customerIdTextField.setText(Integer.toString(customer.getCustomerId()));
    }//end addCustomerToController
    
    /**
     * Methods to handle Save and Cancel buttons
     */
    //Method to handle save btn
    @FXML
    public void handleSaveBtn(ActionEvent event) throws SQLException{
        if (isInputValid()){
            customer.setCustomerId(Integer.parseInt(customerIdTextField.getText()));
            customer.setCustomerName(customerNameTextField.getText());
            customer.setAddress(addressTextField.getText());
            customer.setCity(cityTextField.getText());
            customer.setCountry(countryTextField.getText());
            customer.setZipcode(zipcodeTextField.getText());
            customer.setPhone(phoneTextField.getText());
            customer.setConsultant(consultantTextField.getText());

            scheduleApp.getListOfCustomers().add(customer);

            //add address to DB first then add customer
            String addAddress = "INSERT INTO address (ADDRESS, CITYID, POSTALCODE, PHONE, "
                    + "CREATEDATE, CREATEDBY, LASTUPDATE, LASTUPDATEBY) VALUES ('"
                    //address Id leave blank, let DB assign
                    + addressTextField.getText()
                    //address2 field not used
                    + "', (SELECT cityId FROM city WHERE city = '" + cityTextField.getText() //city Id, pull from DB in subQuery 
                    + "'), '" + zipcodeTextField.getText()
                    + "', '" + phoneTextField.getText()
                    + "', '" + getTimestamp()  //createdate
                    + "', '" + consultantTextField.getText()  //createdby  
                    + "', '" + getTimestamp()  //last update
                    + "', '"  + consultantTextField.getText() //last update by
                    + "');";
            String addCustomer = "INSERT INTO customer (CUSTOMERID, CUSTOMERNAME, ADDRESSID, ACTIVE, "
                    + "CREATEDATE, CREATEDBY, LASTUPDATE, LASTUPDATEBY) VALUES ("
                    + customerIdTextField.getText() 
                    + ", '" + customerNameTextField.getText()
                    + "', (SELECT addressId FROM address WHERE address = '" + addressTextField.getText() //addressId, pull from DB in subQuery
                    + "'), " + 1 //active
                    + ", '" + getTimestamp()  //createdate
                    + "', '"  + consultantTextField.getText()//createdby   
                    + "', '" + getTimestamp() //lastupdate
                    + "', '" + consultantTextField.getText()  //last updated by  
                    + "');";
//            System.out.println("Query to DB:");
//            System.out.println(addAddress);  //print out query for debugging purposes
//            System.out.println(addCustomer);  //print out query for debugging purposes

            primaryStage = (Stage) customerNameTextField.getScene().getWindow();  //reference to this screen
            primaryStage.close();  //close screen on button action 

            Connection conn = connectToDB();
            Statement stmt = conn.createStatement();
            int query1 = stmt.executeUpdate(addAddress);
            int query2 = stmt.executeUpdate(addCustomer);
            conn.close();
        }//end if
    }//end handleSaveBtn
    //method to handle cancel btn
    @FXML
    public void handleCnclBtn(ActionEvent event){
        int newID = (customer.getCustomerId())-1;
        customer.setCustomerId(newID);
        //customer.setCount(newID);
        primaryStage = (Stage) customerNameTextField.getScene().getWindow();  //reference to this screen
        primaryStage.close();
    }//end handleCnclBtn
    
    /**
     * validates user input
     * returns true if input is valid
     */
    private boolean isInputValid(){
        String errorMessage = "";
        
        if (customerNameTextField.getText() == null || customerNameTextField.getText().length() == 0) {
            errorMessage += "Please enter a valid customer name!\n"; 
        }  //end if
        if (addressTextField.getText() == null || addressTextField.getText().length() == 0) {
            errorMessage += "Please enter a valid customer address!\n"; 
        }  //end if
        if (zipcodeTextField.getText() == null || zipcodeTextField.getText().length() == 0) {
            errorMessage += "Please enter a valid customer zipcode!\n"; 
        }  //end if
        if (phoneTextField.getText() == null || phoneTextField.getText().length() == 0) {
            errorMessage += "Please enter a valid customer phone number!\n"; 
        }  //end if
        if (cityTextField.getText() == null || cityTextField.getText().length() == 0) {
            errorMessage += "Please select a customer city!\n"; 
        }  //end if
        if (countryTextField.getText() == null || countryTextField.getText().length() == 0) {
            errorMessage += "Please select a customer country!\n"; 
        }  //end if
        if (consultantTextField.getText() == null || consultantTextField.getText().length() == 0) {
            errorMessage += "Please select a consultant for this customer!\n"; 
        }  //end if
        if (errorMessage.length() == 0) {
            return true;
        }  //end if 
        else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(primaryStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    System.out.println("Alerting!");
                }
            }));
            return false;
        }  //end else
    }//end isInputValid
    
}//end AddCustomerController