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

/**
 * FXML Controller class
 *
 * @author Jared Engelkemier
 */
public class ModCustomerController implements Initializable {
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
    //method to populate text fields with customer data
    */
    public void setCustomerDetails(Customer customer){
        this.customer = customer;  //assigns the customer being passed to the controller to the variable customer declared above
        customerIdTextField.setText(Integer.toString(customer.getCustomerId()));
        customerNameTextField.setText(customer.getCustomerName());
        addressTextField.setText(customer.getAddress());
        cityTextField.setText(customer.getCity());
        countryTextField.setText(customer.getCountry());
        zipcodeTextField.setText(customer.getZipcode());
        phoneTextField.setText(customer.getPhone());
        consultantTextField.setText(customer.getConsultant());
    }//end setCustomerDetails
    
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

            int indexPosition = customer.getCustomerId() - 5001;
            scheduleApp.getListOfCustomers().set(indexPosition, customer);

            String updateAddress = "UPDATE address SET address = '"
                    + addressTextField.getText() 
                    + "', phone = '" + phoneTextField.getText()
                    + "', postalCode = '" + zipcodeTextField.getText()
                    + "', cityId = (SELECT cityId FROM city WHERE city = '" + cityTextField.getText()
                    + "'), lastUpdateBy = '" + consultantTextField.getText()
                    + "' WHERE addressId = (SELECT addressId FROM customer WHERE customerId = " + customerIdTextField.getText()
                    + ");";
            String updateCustomer = "UPDATE customer SET customerName = '"
                    + customerNameTextField.getText()
                    + "', addressId = (SELECT addressId FROM address WHERE address = '" + addressTextField.getText()
                    + "'), lastUpdateBy = '" + consultantTextField.getText()
                    + "' WHERE customerId = " + customerIdTextField.getText()
                    + ";";
//            System.out.println("Query to DB: ");  //print out query for debugging purposes
//            System.out.println(updateAddress);  //print out query for debugging purposes
//            System.out.println(updateCustomer);  //print out query for debugging purposes
            
            primaryStage = (Stage) customerNameTextField.getScene().getWindow();  //reference to this screen
            primaryStage.close();  //close screen on button action 

            Connection conn = connectToDB();
            Statement stmt = conn.createStatement();
            int query1 = stmt.executeUpdate(updateAddress);
            int query2 = stmt.executeUpdate(updateCustomer);
            conn.close();
        }//end if
    }//end handleSaveBtn
    //method to handle cancel btn
    @FXML
    public void handleCnclBtn(ActionEvent event){
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
    
}//end ModCustomerController