/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleapp;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Jared Engelkemier
 */
public class CustomerDetailsScrnController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private Label Id_Label;
    @FXML
    private Label Name_Label;
    @FXML
    private Label Address_Label;
    @FXML
    private Label Address_Label2;
    @FXML
    private Label Phone_Label;
    @FXML
    private Label Consultant_Label;
    
    private Customer customer;
    private Stage primaryStage;

    public void setCustomerDetailsScrnToController(Customer customer){
        this.customer = customer;
        Id_Label.setText(Integer.toString(customer.getCustomerId()));
        Name_Label.setText(customer.getCustomerName());
        Address_Label.setText(customer.getAddress());
        Address_Label2.setText(customer.getCity() + ", " + customer.getCountry() + "  " + customer.getZipcode());
        Phone_Label.setText(customer.getPhone());
        Consultant_Label.setText(customer.getConsultant());
    }//end setCustomerDetailsScrnToController
    
    public void handleOKBtn(ActionEvent event){
        primaryStage = (Stage) Id_Label.getScene().getWindow();  //reference to this screen
        primaryStage.close();
    }//end handleCnclBtn
    
}//end CustomerDetailsScrnController