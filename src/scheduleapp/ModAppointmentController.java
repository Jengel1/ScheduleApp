/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleapp;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import static scheduleapp.ScheduleApp.connectToDB;
import static scheduleapp.ScheduleApp.getTimestamp;
import static scheduleapp.ScheduleApp.initCustomerDetailsScrn;
import static scheduleapp.ScheduleApp.listOfAppointments;

/**
 * FXML Controller class
 *
 * @author Jared Engelkemier
 */
public class ModAppointmentController {

//    /**
//     * Initializes the controller class.
//     */
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        // TODO
//    }    
    
    /**
     * Main variables for use throughout controller
     */
    private Stage primaryStage;
    private Appointment appointment;
    public static ScheduleApp scheduleApp;

    /**
     * Variables to reference screen items
     */
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    @FXML
    private TextField searchCustomerTextField;
    
    @FXML
    private RadioButton startTimeBtn; 
    @FXML
    private RadioButton endTimeBtn;
    @FXML
    private RadioButton amBtn;
    @FXML
    private RadioButton pmBtn;
    
    @FXML
    private ToggleGroup apptStartEndRadios;
    @FXML
    private ToggleGroup apptAmPmRadios;
    
    @FXML
    private TextField apptStartTimeTextField;
    @FXML
    private TextField apptEndTimeTextField;
    
    @FXML
    private DatePicker apptDatePickerField;
    
    @FXML
    private TextField apptIdTextField;
    @FXML
    private TextField apptTitleTextField;
    @FXML
    private TextField apptLocationTextField;
    @FXML
    private TextField apptContactTextField;
    @FXML
    private TextField apptConsultantTextField;
    @FXML
    private TextField apptTypeTextField;
    @FXML
    private TextField apptCustomerTextField;
    @FXML
    private TextField apptHrTextField;
    @FXML
    private TextField apptMinTextField;

    /**
     * Methods to set up and initialize screen
     * @param appointment 
     */
    public void setAppointmentDetails(Appointment appointment){
        this.appointment = appointment;
        apptIdTextField.setText(Integer.toString(appointment.getApptId()));
        apptTitleTextField.setText(appointment.getApptTitle());
        apptLocationTextField.setText(appointment.getApptLocation());
        apptContactTextField.setText(appointment.getApptContact());
        apptConsultantTextField.setText(appointment.getConsultant());
        apptTypeTextField.setText(appointment.getApptType());
        apptCustomerTextField.setText(appointment.getCustomerName());
        apptStartTimeTextField.setText(getApptDateTime(appointment.getApptStart()));
        apptEndTimeTextField.setText(getApptDateTime(appointment.getApptEnd()));
    }//end setAppointmentDetails
    //method to format time object
    public static String getApptDateTime(LocalDateTime ldt){  //ldt format passed in "2018-01-30T11:30"
        String dateTime = ldt.toString();
            String year = dateTime.substring(0, 4);  //yyyy
            String month = dateTime.substring(5, 7);  //MM
            String day = dateTime.substring(8, 10);
            String hour = dateTime.substring(11, 13);
                int intHr = Integer.parseInt(hour);
                String stringHr = "";
            String min = dateTime.substring(14);
            String am_pm = "";
            if (intHr > 12){
                intHr = intHr - 12;
                if (intHr < 10){
                    DecimalFormat df = new DecimalFormat("00");
                    stringHr = df.format(intHr);
                }//end if
                am_pm = "PM";
            }//end if
            else {
                stringHr = hour;
                am_pm = "AM";
            }//end else
        String formattedDateTime = month + "/" + day + "/" + year + " " + stringHr + ":" + min + " " + am_pm;  
        return formattedDateTime;  //string comming out "MM/dd/yyyy hh:mm a"
    }//end getApptDateTime
    //method to initialize customerTable cells
    public void initialize(){
        //sets customer table
        customerIdColumn.setCellValueFactory(cellData -> cellData.getValue().customerIdProperty().asObject());
        customerNameColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        // set current date into datepicker
        apptDatePickerField.setValue(LocalDate.now());
    }//end initialize
    //method to populate customerTable cells
    public void setCustomerTable(){
        customerTable.setItems(scheduleApp.getListOfCustomers());
    }//end setCustomerTable
    
    /**
     * Methods to handle drop down menu buttons
     * @param event 
     */
    @FXML
    private void handleConsultantMenuItems(ActionEvent event){
        MenuItem source = (MenuItem) event.getSource();
        apptConsultantTextField.setText(source.getText());
    }//end handleConsultantMenuItems
    @FXML
    private void handleTypeMenuItems(ActionEvent event){
        MenuItem source = (MenuItem) event.getSource();
        apptTypeTextField.setText(source.getText());
    }//end handleTypeMenuItems
    @FXML
    private void handleHrMenuItmes(ActionEvent event){
        MenuItem source = (MenuItem) event.getSource();
        apptHrTextField.setText(source.getText());
    }//end handleHrMenuItems
    @FXML
    private void handleMinMenuItems(ActionEvent event){
        MenuItem source = (MenuItem) event.getSource();
        apptMinTextField.setText(source.getText());
    }//end handleMinMenuItems
    
    /**
     * Methods to handle setCustomerBtn, seeDetailsBtn, setTimeBtn, searchCustomerTableBtn
     * @param event 
     */
    //method to handle set customer btn
    @FXML
    public void handleSetCustomerBtn(ActionEvent event){
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        apptCustomerTextField.setText(selectedCustomer.getCustomerName());
    }//end handleSetCustomerBtn
    //method to hand see details btn
    @FXML
    public void handleSeeDetailsBtn (ActionEvent event){
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        initCustomerDetailsScrn(selectedCustomer);
    }//end handSeeDetailsBtn
    //method to handle setting appt. start and end time fields
    @FXML
    public void handleSetTimeBtn(ActionEvent event){
        if (isDateTimeInputValid()){
            String am_pm = "";
             if (apptAmPmRadios.getSelectedToggle().equals(amBtn)){
                 am_pm = "AM";
                 amBtn.setSelected(false);
             }//end if
             else {
                 am_pm = "PM";
                 pmBtn.setSelected(false);
             }//end else
             LocalDate date = apptDatePickerField.getValue();
             DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
             String formattedDate = formatter.format(date);
             String dateTime = formattedDate + " " + apptHrTextField.getText() + ":" + apptMinTextField.getText() + " " + am_pm;  //final output string  hh:mm PM on MM-dd-yyyy 
             if (apptStartEndRadios.getSelectedToggle().equals(startTimeBtn)){
                 apptStartTimeTextField.setText(dateTime);
                 startTimeBtn.setSelected(false);
                 endTimeBtn.setSelected(true);
             }//end if
             else {
                 apptEndTimeTextField.setText(dateTime);
                 endTimeBtn.setSelected(false);
                 startTimeBtn.setSelected(true);
             }//end if
             apptHrTextField.clear();
             apptMinTextField.clear();
             //apptDatePickerField.getEditor().clear();
        }//end if
    }//end handleSetTimeBtn
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
                alert.setHeaderText("Error!");
                alert.setContentText("Part not found");

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
                alert.setHeaderText("Error!");
                alert.setContentText("Part not found");

                alert.showAndWait();
            }  //end if
        }  //end catch
    }//end handleSrchBtn

    /**
     * method to format and convert Date Time Fields into Timestamp for DB
     * @param dateTime
     * @return Timestamp
     */
    private Timestamp getApptTimestamp(String dateTime){  //String comming in "MM/dd/yyyy hh:mm a"
        String time = dateTime.substring(11);
            String year = dateTime.substring(6, 10);
            String month = dateTime.substring(0, 2);
            String day = dateTime.substring(3, 5);
        String date = year + "-" + month + "-" + day;
        String formattedDateTime = date + " " + time;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");  //new format
        LocalDateTime ldt = LocalDateTime.parse(formattedDateTime, formatter);
        //System.out.println("LocalDateTime from application: " + ldt);
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime localZonedDateTime = ldt.atZone(zid);
        //System.out.println("ZonedDateTime: " + localZonedDateTime);
        ZonedDateTime utcDateTime = localZonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        //System.out.println("UTCDateTime: " + utcDateTime);
        LocalDateTime utcLocalDateTimeObject = utcDateTime.toLocalDateTime();
        //System.out.println("UTC ZonedDateTime converted to LocalDateTime: " + utcLocalDateTimeObject);
        Timestamp valueToDB = Timestamp.valueOf(utcLocalDateTimeObject);
        //System.out.println("Timestamp to DB: " + valueToDB);
        return valueToDB;
    }//end getApptTimestamp
    
    /**
     * Methods to handle Save and Cancel buttons
     */
    //Method to handle save btn
    @FXML
    public void handleModApptBtn(ActionEvent event) throws SQLException {
        if (isInputValid()){
            appointment.setCustomerName(apptCustomerTextField.getText());
            appointment.setApptId(Integer.parseInt(apptIdTextField.getText()));
            appointment.setApptTitle(apptTitleTextField.getText());
            appointment.setApptLocation(apptLocationTextField.getText());
            appointment.setApptContact(apptContactTextField.getText());
            appointment.setConsultant(apptConsultantTextField.getText());
            appointment.setApptType(apptTypeTextField.getText());

            int indexPosition = appointment.getApptId() - 6001;
            scheduleApp.getListOfAppointments().set(indexPosition, appointment);


            String modAppt = "UPDATE appointment SET customerId = (SELECT customerId FROM customer "
                    + "WHERE customerName = '" + apptCustomerTextField.getText()
                    + "'), title = '" + apptTitleTextField.getText()
                    + "', description = '" + apptTypeTextField.getText()
                    + "', location = '" + apptLocationTextField.getText()
                    + "', contact = '" + apptContactTextField.getText()
                    + "', start = '" + getApptTimestamp(apptStartTimeTextField.getText())
                    + "', end = '" + getApptTimestamp(apptEndTimeTextField.getText())
                    + "', lastupdate = '" + getTimestamp()
                    + "', lastupdateby = '" + apptConsultantTextField.getText()
                    + "' WHERE appointmentId = " + apptIdTextField.getText()
                    + ";";
//            System.out.println("Query to DB: ");
//            System.out.println(modAppt);

            primaryStage = (Stage) apptCustomerTextField.getScene().getWindow();  //reference to this screen
            primaryStage.close();  //close screen on button action 

            Connection conn = connectToDB();
            Statement stmt = conn.createStatement();
            int query = stmt.executeUpdate(modAppt);
            conn.close();
        }//end if
    }//end handleSaveBtn
    //method to handle cancel btn
    @FXML
    public void handleCnclBtn(ActionEvent event){
//        int newID = (appointment.getApptId())-1;
//        appointment.setApptId(newID);
//        //customer.setCount(newID);
        primaryStage = (Stage) apptTitleTextField.getScene().getWindow();  //reference to this screen
        primaryStage.close();
    }//end handleCnclBtn
    
    /**
     * methods to validate user input
     * returns true if input is valid
     */
    //validates input for appointment data before saving.
    private boolean isInputValid(){
        String errorMessage = "";
        
        if (apptTitleTextField.getText() == null || apptTitleTextField.getText().length() == 0) {
            errorMessage += "Please enter a valid title!\n"; 
        }  //end if
        if (apptLocationTextField.getText() == null || apptLocationTextField.getText().length() == 0) {
            errorMessage += "Please enter a valid location!\n"; 
        }  //end if
        if (apptContactTextField.getText() == null || apptContactTextField.getText().length() == 0) {
            errorMessage += "Please enter a valid contact!\n"; 
        }  //end if
        if (apptConsultantTextField.getText() == null || apptConsultantTextField.getText().length() == 0) {
            errorMessage += "Please select a consultant!\n"; 
        }  //end if
        if (apptTypeTextField.getText() == null || apptTypeTextField.getText().length() == 0) {
            errorMessage += "Please select an appointment type!\n"; 
        }  //end if
        if (apptCustomerTextField.getText() == null || apptCustomerTextField.getText().length() == 0) {
            errorMessage += "Please select a customer!\n"; 
        }  //end if
        if (apptStartTimeTextField.getText() == null || apptStartTimeTextField.getText().length() == 0) {
            errorMessage += "Please select a start time for this appointment!\n"; 
        }  //end if
        if (apptEndTimeTextField.getText() == null || apptEndTimeTextField.getText().length() == 0) {
            errorMessage += "Please select an end time for this appointment!\n"; 
        }  //end if
        //appointment end time cannot be before appointment start time.
        String startDateTime = apptStartTimeTextField.getText();
        String endDateTime = apptEndTimeTextField.getText();
        if ((!(startDateTime.equals(""))) && (!(endDateTime.equals("")))){
            DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
            LocalDateTime startldt = LocalDateTime.parse(startDateTime, formatterDateTime);
            LocalDateTime endldt = LocalDateTime.parse(endDateTime, formatterDateTime);
            if (startldt.isAfter(endldt)){
                errorMessage += "Appointment end time must come after appointment start time!\n";
            }//end if
            //appointment times cannot be outside of normal business hours 9am - 5pm
            String startTime = startDateTime.substring(11);
            String endTime = endDateTime.substring(11);
            DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("hh:mm a");
            LocalTime startlt = LocalTime.parse(startTime, formatterTime);
            LocalTime endlt = LocalTime.parse(endTime, formatterTime);
            LocalTime startOfBiz = LocalTime.of(9, 0, 0);
            LocalTime endOfBiz = LocalTime.of(17, 0, 0);
            //System.out.println("Before ifs: startlt = " + startlt + ", endlt = " + endlt);
            //System.out.println("startOfBiz = " + startOfBiz + ", endOfBiz = " + endOfBiz);
            if (startlt.isBefore(startOfBiz) || startlt.isAfter(endOfBiz)){
                System.out.println("inside start time if");
                errorMessage += "Appointment start time is currently outside of normal business hours!  Please select "
                        + "a start time between 9:00am and 5:00pm.\n";
            }//end if
            if (endlt.isBefore(startOfBiz) || endlt.isAfter(endOfBiz)){
                System.out.println("inside end time if");
                errorMessage += "Appointment end time is currently outside of normal business hours!  Please select "
                        + "a time between 9:00am and 5:00pm.\n";
            }//end if
            //System.out.println("after ifs");
            //appointment times cannot overlap
            int id = Integer.parseInt(apptIdTextField.getText());
            for (Appointment a : listOfAppointments){
                if (!(a.getApptId() == id)){  
                    if (startldt.equals(a.getApptStart()) || startldt.equals(a.getApptEnd()) ||    //
                        (startldt.isAfter(a.getApptStart()) && startldt.isBefore(a.getApptEnd()))){
                        errorMessage += "This appointment's start time overlaps with appointment " + a.getApptId() 
                            + "!  Please select a different start time.\n";
                    }//end if
                    if (endldt.equals(a.getApptStart()) || endldt.equals(a.getApptEnd()) || 
                        (endldt.isAfter(a.getApptStart()) && endldt.isBefore(a.getApptEnd()))){
                        errorMessage += "This appointment's end time overlaps with appointment " + a.getApptId() 
                            + "!  Please select a different end time.\n";
                    }//end if
                }//end if
            }//end for
        }//end if

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
    //method to validate setTimeDate before added to Start/End textFields
    //returns true if valid
    private boolean isDateTimeInputValid(){
        String errorMessage = "";
        //date selected cannot be before today's date
        if (apptDatePickerField.getValue().isBefore(LocalDate.now())) {
            errorMessage += "Appointment date must be on or after today's date!\n"; 
        }  //end if
        //hr and min fields cannot be left blank
        if (apptHrTextField.getText() == null || apptHrTextField.getText().length() == 0){
            errorMessage += "Please select an hour!\n";
        }//end if
        if (apptMinTextField.getText() == null || apptMinTextField.getText().length() == 0){
            errorMessage += "Please select minutes!\n";
        }//end if
        //radio am pm must be selected
        if (apptAmPmRadios.getSelectedToggle() == null){
            errorMessage += "Please select either AM or PM!\n";
        }//end if
        
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
    }//end isDateTimeInputValid
    
}//end ModAppointmentController