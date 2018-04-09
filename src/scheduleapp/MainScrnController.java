/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleapp;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import static scheduleapp.LoginScrnController.getCurrentUser;
import static scheduleapp.LoginScrnController.logIn;
import static scheduleapp.ReportsController.getTime;
import static scheduleapp.ScheduleApp.connectToDB;
import static scheduleapp.ScheduleApp.getTimestamp;
import static scheduleapp.ScheduleApp.listOfAppointments;
import static scheduleapp.ScheduleApp.listOfCustomers;
import static scheduleapp.ScheduleApp.writeLog;

/**
 * FXML Controller class
 *
 * @author Jared Engelkemier
 */
public class MainScrnController {

//    /**
//     * Initializes the controller class.
//     */
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        // TODO
//    }    
    
    private Stage primaryStage;
    private Appointment appointment;
    public static ScheduleApp scheduleApp;
    
    public Stage getPrimaryStage(){
        return primaryStage;
    }  //end getPrimaryStage
    
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;
    @FXML
    private TableColumn<Customer, String> customerConsultantColumn;
    @FXML
    private TextField searchCustomerTextField;
    
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, Integer> apptIdColumn;
    @FXML
    private TableColumn<Appointment, String> apptTypeColumn;
    @FXML
    private TableColumn<Appointment, String> apptCustomerColumn;
    @FXML
    private TableColumn<Appointment, String> apptDateColumn;
    @FXML
    private TextField searchApptTextField;
    
    @FXML
    private RadioButton allRadioBtn;
    @FXML
    private RadioButton monthRadioBtn;
    @FXML
    private RadioButton weekRadioBtn;
    @FXML
    private ToggleGroup apptRadioBtns;

    //method to set MainScrnController stage
    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }  //end setPrimaryStage
    
    //method to initialize customerTable cells and check for appts within 15min of logIn
    public void initialize(){
        //sets customer table
        customerIdColumn.setCellValueFactory(cellData -> cellData.getValue().customerIdProperty().asObject());
        customerNameColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        phoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        customerConsultantColumn.setCellValueFactory(cellData -> cellData.getValue().consultantProperty());
        
        //sets appointment table
        apptIdColumn.setCellValueFactory(cellData -> cellData.getValue().apptIdProperty().asObject());
        apptTypeColumn.setCellValueFactory(cellData -> cellData.getValue().apptTypeProperty());
        apptCustomerColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        apptDateColumn.setCellValueFactory(cellData -> cellData.getValue().getDateTimeAsStringProperty());

        checkForAppts();
    }//end initialize
    
    //method to populate customerTable cells
    public void setCustomerTable(){
        customerTable.setItems(scheduleApp.getListOfCustomers());
    }//end setCustomerTable
    //method to populate appointmentTable cells
    public void setAppointmentTable(){
        appointmentTable.setItems(scheduleApp.getListOfAppointments());
    }//end setAppointmentTable
    
    /*
    //method to check if there are any appointments within 15min of log in
    */
    public void checkForAppts(){
//        System.out.println("in checkForAppts method");
//        System.out.println("This is logIn time: " + logIn);
//        System.out.println("This is logIn time converted to ldt: " + logIn.toLocalDateTime());
        LocalDateTime ldtPlus15 = logIn.toLocalDateTime().plusMinutes(15);
//        System.out.println("This is logIn time plus 15min: " + ldtPlus15);
        for (Appointment a : listOfAppointments){
            System.out.println("This is appointment start time: " + a.getApptStart() + " for appt: " + a.apptIdProperty());
            if ((a.getApptStart().isAfter(logIn.toLocalDateTime()) &&     
                    (a.getApptStart().isBefore(ldtPlus15)))){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Appointment Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("You have an appointment at " + getTime(a.getApptStart())
                        + " with " + a.getCustomerName() + "!");

                alert.showAndWait();
            }//end if
        }//end for
    }//end checkForApps
    
    /**
     * Methods to handle Add, Mod, Delete, Search Buttons for customer side of split pane
     */
    //method to handle add btn
    @FXML
    public void handleAddCustomerBtn(){
        Customer customer = new Customer();
        if (listOfCustomers.size() == 0){
            customer.setCustomerId(5001);
        }
        else {
            int index = listOfCustomers.size() - 1;
            int newId = listOfCustomers.get(index).getCustomerId() + 1;
            customer.setCustomerId(newId);
        }
        System.out.println(customer.getCustomerId());
        scheduleApp.initAddCustomerScrn(customer);
    }//end handleAddBtn
    //method to handle mod btn
    @FXML
    public void handleModCustomerBtn(){
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        //System.out.println(selectedCustomer.getCustomerName());
        if (selectedCustomer != null){
            scheduleApp.initModCustomerScrn(selectedCustomer);
        }  //end if
        else {
            //if nothing selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(getPrimaryStage());  //null pointer exception
            alert.setTitle("No Selection");
            alert.setHeaderText("No Customer Selected");
            alert.setContentText("Please select a customer from the table");
            
            alert.showAndWait();
        }  //end else
        
    }//end handleModBtn
    //method to handle delete btn
    @FXML
    public void handleDeleteCustomerBtn() throws SQLException {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null){
            //if nothing selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(getPrimaryStage());  //null pointer exception
            alert.setTitle("No Selection");
            alert.setHeaderText("No Customer Selected");
            alert.setContentText("Please select a customer from the table");
            
            alert.showAndWait();
        }  //end if
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Stop and think about it!");
            alert.setContentText("Are you sure you want to delete this customer?");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                String deleteCustomer = "DELETE FROM customer WHERE customerId = "
                        + selectedCustomer.getCustomerId() + ";";
                System.out.println("Query to DB: " + deleteCustomer);  //print query to DB for debugging
                scheduleApp.getListOfCustomers().remove(selectedCustomer);
                //Query to DB
                //Connection conn = DriverManager.getConnection("jdbc:mysql://52.206.157.109/U047gY", "U047gY", "53688169978");   //52.206.157.109  //U047gY  //53688169978
                Connection conn = connectToDB();
                Statement stmt = conn.createStatement();
                int result2 = stmt.executeUpdate(deleteCustomer);
            }  //end if
            else{
                alert.close();
            }  //end else
        }  //end else
    }//end handleDeleteBtn
    //method to handle search btn
    @FXML
    public void handleSrchCustomerBtn(ActionEvent event){
        String searchItem = searchCustomerTextField.getText();
        boolean found = false;
        try {
            int customerID = Integer.parseInt(searchItem);
            for (Customer c : customerTable.getItems()){
                if(c.getCustomerId() == customerID){
                    found = true;
                    customerTable.getSelectionModel().select(c);
                }  //end if
            }  //end for
            if (found == false){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Error! Customer not found.");
                alert.setContentText("Please enter valid Customer Id or Customer Name.");

                alert.showAndWait();
            }  //end if
        }  //end try
        catch (NumberFormatException e){
            for (Customer c : customerTable.getItems()){
                if (c.getCustomerName().equals(searchItem)){
                    found = true;
                    customerTable.getSelectionModel().select(c);
                }  //end if
            }  //end for
            if (found == false){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Error! Customer not found.");
                alert.setContentText("Please enter valid Customer Id or Customer Name.");

                alert.showAndWait();
            }  //end if
        }  //end catch
    }//end handleSrchCustomerBtn
    
    /**
     * Methods to handle Add, Mod, Delete, Search Buttons for appointment side of split pane
     */
    //method to handle add btn
    @FXML
    public void handleAddApptBtn(){
        Appointment appointment = new Appointment();
        if (listOfAppointments.size() == 0){
            appointment.setApptId(6001);
        }
        else {
            int index = listOfAppointments.size() - 1;
            int newId = listOfAppointments.get(index).getApptId() + 1;
            appointment.setApptId(newId);
        }
        System.out.println(appointment.getApptId());
        scheduleApp.initAddApptScrn(appointment);
    }//end handleAddBtn
    @FXML
    public void handleSeeApptDetailsBtn(){
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        //System.out.println(selectedAppointment.getApptTitle());
        if (selectedAppointment != null){
            scheduleApp.initModApptScrn(selectedAppointment);
        }  //end if
        else {
            //if nothing selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(getPrimaryStage());  //null pointer exception
            alert.setTitle("No Selection");
            alert.setHeaderText("No Appointment Selected");
            alert.setContentText("Please select an appointment from the table");
            
            alert.showAndWait();
        }  //end else
    }//end handleSeeApptDetailsBtn
    @FXML
    public void handleDeleteApptBtn() throws SQLException {
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null){
            //if nothing selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(getPrimaryStage());  //null pointer exception
            alert.setTitle("No Selection");
            alert.setHeaderText("No Appointment Selected");
            alert.setContentText("Please select an appointment from the table");
            
            alert.showAndWait();
        }  //end if
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Stop and think about it!");
            alert.setContentText("Are you sure you want to delete this appointment?");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                String deleteCustomer = "DELETE FROM appointment WHERE appointmentId = "
                        + selectedAppointment.getApptId() + ";";
                System.out.println("Query to DB: " + deleteCustomer);  //print query to DB for debugging
                scheduleApp.getListOfAppointments().remove(selectedAppointment);
                //Query to DB
                //Connection conn = DriverManager.getConnection("jdbc:mysql://52.206.157.109/U047gY", "U047gY", "53688169978");   //52.206.157.109  //U047gY  //53688169978
                Connection conn = connectToDB();
                Statement stmt = conn.createStatement();
                int result2 = stmt.executeUpdate(deleteCustomer);
            }  //end if
            else{
                alert.close();
            }  //end else
        }  //end else
    }//end handleDeleteBtn
    //method to handle search btn
    @FXML
    public void handleSrchApptBtn(ActionEvent event){
        String searchItem = searchApptTextField.getText();
        boolean found = false;
        try {
            int apptId = Integer.parseInt(searchItem);
            for (Appointment a : appointmentTable.getItems()){
                if(a.getApptId() == apptId){
                    found = true;
                    appointmentTable.getSelectionModel().select(a);
                }  //end if
            }  //end for
            if (found == false){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Error! Appointment not found.");
                alert.setContentText("Please enter valid Appointment Id.");

                alert.showAndWait();
            }  //end if
        }  //end try
        catch (NumberFormatException e){
            for (Appointment a : appointmentTable.getItems()){
                if (a.getApptTitle().equals(searchItem)){
                    found = true;
                    appointmentTable.getSelectionModel().select(a);
                }  //end if
            }  //end for
            if (found == false){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Error! Appointment not found.");
                alert.setContentText("Please enter valid Appointment Id.");

                alert.showAndWait();
            }  //end if
        }  //end catch
    }//end handleSrchBtn
    
    /**
     * Methods for handling appointment table radio buttons
     * //allRadioBtn, monthRadioBtn, weekRadioBtn, apptRadioBtns-(toggle group)
     * @param event
     */
    @FXML
    public void handleApptRadioBtns(ActionEvent event){
        LocalDateTime ldt = LocalDateTime.now();  //local datetime object for comparison
        if (apptRadioBtns.getSelectedToggle().equals(weekRadioBtn)){
            List<Appointment> filteredApptList = listOfAppointments.stream()
                .filter(a -> a.getApptStart().isAfter(ldt) && a.getApptStart().isBefore(ldt.plusDays(7)))
                .collect(Collectors.toList());  //create filtered list of appointments based on predicate from O
            ObservableList<Appointment> obsApptList = FXCollections.observableArrayList();  //create new observable list
            obsApptList.addAll(filteredApptList);  //fill observable list with list
            appointmentTable.setItems(obsApptList);  //set tableview to filtered observable list
        }//end if
        else if (apptRadioBtns.getSelectedToggle().equals(monthRadioBtn)){
            List<Appointment> filteredApptList = listOfAppointments.stream()
                .filter(a -> a.getApptStart().isAfter(ldt) && a.getApptStart().isBefore(ldt.plusDays(30)))
                .collect(Collectors.toList());  //create filtered list of appointments based on predicate from O
            ObservableList<Appointment> obsApptList = FXCollections.observableArrayList();  //create new observable list
            obsApptList.addAll(filteredApptList);  //fill observable list with list
            appointmentTable.setItems(obsApptList);  //set tableview to filtered observable list
        }//end else if
        else {
            setAppointmentTable();
        }//end else
    }//end handleApptRadioBtns
    
    /**
     * Methods for menu screen buttons below split pane
     * @param event 
     */
    //Method to handle Generate report btn
    @FXML
    public void handleGenerateReports(){
        scheduleApp.initReportsScrn();
    }//end handleGenerateReports
    
    //method to handle Exit Btn
    @FXML
    public void handleExit(ActionEvent event) {
        try{
            String user = getCurrentUser();
            writeLog(getTimestamp(), user, "Out");
            primaryStage = (Stage) searchCustomerTextField.getScene().getWindow();  //reference to LoginScrn.fxml using usernameField
            primaryStage.close();
        }//end try
        catch (Exception e){
            e.printStackTrace();
        }//end catch
    }  //end handleExit
    
}//end MainScrnController