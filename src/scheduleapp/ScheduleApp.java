/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleapp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Jared Engelkemier
 */
public class ScheduleApp extends Application{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }//end main
    
    //method to start program by creating a primary stage
    @Override
    public void start(Stage primaryStage) {
        //Evaluators: uncomment line 50 to change Locale
        //Locale.setDefault(new Locale("fr", "FR"));
        System.out.println(Locale.getDefault());
        this.primaryStage = primaryStage;
        if (Locale.getDefault().getDisplayCountry().equals("France")){
            this.primaryStage.setTitle("Unité De Logiciel");
        }//end if
        else {
            this.primaryStage.setTitle("Schedule Application Login");
        }//end else
        initLoginScreen();  //method to initiate login scrn
    }//end start
    
    private static Stage primaryStage;
    private AnchorPane rootLayout;
    
    public static ObservableList<User> listOfUsers = FXCollections.observableArrayList();
    public static ObservableList<Customer> listOfCustomers = FXCollections.observableArrayList();
    public static ObservableList<Appointment> listOfAppointments = FXCollections.observableArrayList();
    
    public ScheduleApp() throws SQLException {
        loadListOfCustomers();
        loadListOfAppointments();
        loadListOfUsers();
    }//end ScheduleApp
    
    //mehtod to query DB to load listOfAppointments
    public static void loadListOfAppointments() throws SQLException {
        String query = "SELECT appointmentId, title, description, location, contact, c.customerName AS customer,"
                + " a.lastUpdateBy AS consultant, start, end FROM appointment a, customer c"
                + " WHERE a.customerId = c.customerId ORDER BY appointmentId;";
        //System.out.println(query);
        Connection conn = connectToDB();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        //conn.close();
        while (rs.next()){
            listOfAppointments.add(new Appointment(rs.getInt("appointmentId"), rs.getString("title"),
                    rs.getString("description"), rs.getString("location"), rs.getString("contact"), rs.getString("customer"),
                    rs.getString("consultant"), getConvertedApptTime(rs.getTimestamp("start")), getConvertedApptTime(rs.getTimestamp("end"))));
        }//end while
//        for (Appointment a : listOfAppointments){
//            System.out.println(a.getApptId());
//            System.out.println(a.getApptTitle());
//            System.out.println(a.getApptStart());  
//            System.out.println(a.getApptEnd());
//        }//end for
    }//end loadListOfAppointments
    //method to convert Timestamp from DB to LocalDateTime object
    public static LocalDateTime getConvertedApptTime(Timestamp timestamp){
        //System.out.println("Timestamp placed into method: " + timestamp);
        LocalDateTime ldtOfTimestamp = timestamp.toLocalDateTime();
        //System.out.println("ldt object of UTC Timestamp: " + ldtOfTimestamp);
        ZonedDateTime zldtOfTimestamp = ldtOfTimestamp.atZone(ZoneId.of("UTC"));
        //System.out.println("zldt object of UTC Timestamp: " + zldtOfTimestamp);
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime systemLocalDateTime = zldtOfTimestamp.withZoneSameInstant(zid);
        //System.out.println("converted zldt into local zid: " + systemLocalDateTime);
        LocalDateTime convertedTimestamp = systemLocalDateTime.toLocalDateTime();
        //System.out.println("converted ldt object for application: " + convertedTimestamp);
        return convertedTimestamp;
    }//end getConvertedApptTime
    //method to query DB to load listOfUsers
    public static void loadListOfUsers() throws SQLException {
        Connection conn = connectToDB();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT userName, password FROM user;");
        //conn.close();
        while (rs.next()){
            listOfUsers.add(new User(rs.getString("userName"), rs.getString("password")));
        }//end while
//        for (User u : listOfUsers){
//            System.out.println(u.getUsername());
//            System.out.println(u.getPassword());
//        }//end for
    }//end loadListOfUsers
    //method queries DB to load listOfCustomers
    public static void loadListOfCustomers() throws SQLException {
        Connection conn = connectToDB();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT customerId, customerName, address, phone, postalCode, "
                + "city, country, c.lastUpdateBy AS consultant FROM customer c, address a, "
                + "city t, country y WHERE a.addressId = c.addressId AND t.cityId = a.cityId "
                + "AND y.countryId = t.countryId ORDER BY customerId;");
        //conn.close();
        while (rs.next()){
            listOfCustomers.add(new Customer(rs.getInt("customerId"), rs.getString("customerName"), 
                    rs.getString("address"), rs.getString("phone"), rs.getString("postalCode"),
                    rs.getString("city"), rs.getString("country"), rs.getString("consultant")));
        }//end while
//        for (Customer c : listOfCustomers){
//            System.out.println(c.getCustomerId());
//            System.out.println(c.getCustomerName());
//        }//end for
    }//end loadListOfCustomers
    
    /*
    //getter methods for Observable lists and primary stage
    */
    public static ObservableList<User> getListOfUsers(){
        return listOfUsers;
    }//end getListOfUsers
    public static ObservableList<Customer> getListOfCustomers(){
        return listOfCustomers;
    }//end getListOfCustomers
    public static ObservableList<Appointment> getListOfAppointments(){
        return listOfAppointments;
    }//end getListOfAppointments
    public Stage getPrimaryStage(){
        return primaryStage;
    }  //end getPrimaryStage
    
    /*
    //Utility methods to get Timestamps, connectToDB, and write to log file
    */
    //method to get a Timestamp
    public static Timestamp getTimestamp(){
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Timestamp currentTimestamp = new Timestamp(now.getTime());
        return currentTimestamp;
    }//end getTimestamp
   //method to get a connection to DB 
   public static Connection connectToDB() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://52.206.157.109/U047gY", "U047gY", "53688169978");   //52.206.157.109  //U047gY  //53688169978
        return conn;
   }//end connectToDB
   //method to write to the log file
   public static void writeLog(Timestamp time, String user, String io){
       BufferedWriter writer = null;	
       try {
		String s = System.lineSeparator();
		String logToWrite = time + " " + user + " " + io + s;
		System.out.println(logToWrite);
		writer = new BufferedWriter(new FileWriter("logInTracker.txt", true));
		writer.write(logToWrite);
	}//end try
	catch (Exception e){
		e.printStackTrace();
	}//end catch
	finally{
		try{
                    writer.close();
		}//end try
		catch(Exception e){
			e.printStackTrace();
		}//end catch
	}//end finally
   }//end writeLog
    
    /**
     * Methods to return FXML pages
     */
    //method to initiate Login Scrn
    private void initLoginScreen(){        
        try {
            //Load root layout from fxml doc
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ScheduleApp.class.getResource("LoginScrn.fxml"));
            rootLayout = (AnchorPane) loader.load();
            
            //show scene containing root layout
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        }  //end try
        catch (IOException e) {
            e.printStackTrace();
        }  //end catch        
    }//end initLoginScreen
    //method to initiate Main Scrn
    public static boolean initMainAppScrn(){
        try {
            //load fxml file and create new stage for dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ScheduleApp.class.getResource("MainScrn.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            
            //create dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Main Application Screen");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            
            MainScrnController controller = loader.getController();
            controller.setCustomerTable();
            controller.setAppointmentTable();
            
            dialogStage.showAndWait();
            
            return true;
        }  //end try
        catch (IOException e){
            e.printStackTrace();
            return false;
        }  //end catch
    }  //end showMainAppScrn
    //method to initiate Add Customer Scrn
    public static boolean initAddCustomerScrn(Customer customer){
        try {
            //load fxml file and create new stage for dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ScheduleApp.class.getResource("AddCustomer.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            
            //create dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Customer Screen");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            
            AddCustomerController controller = loader.getController();
            controller.handleAddCustomerToController(customer);  //method called from AddCustomerController
            
            dialogStage.showAndWait();
            
            return true;
        }  //end try
        catch (IOException e){
            e.printStackTrace();
            return false;
        }  //end catch
    }  //end initAddCustomerScrn
    //method to initiate Mod Customer Scrn
    public static boolean initModCustomerScrn(Customer customer){
        try {
            //load fxml file and create new stage for dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ScheduleApp.class.getResource("ModCustomer.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            
            //create dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Modify Customer Screen");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            
            ModCustomerController controller = loader.getController();
            controller.setCustomerDetails(customer);  //method called from ModCustomerController
            
            dialogStage.showAndWait();
            
            return true;
        }  //end try
        catch (IOException e){
            e.printStackTrace();
            return false;
        }  //end catch
    }  //end initModCustomerScrn
    //method to initiate add appt scrn
    public static boolean initAddApptScrn(Appointment appointment){
        try {
            //load fxml file and create new stage for dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ScheduleApp.class.getResource("AddAppointment.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            
            //create dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Appointment Screen");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            
            AddAppointmentController controller = loader.getController();
            controller.handleAddApptToController(appointment);   //method called from AddAppointmentController
            controller.setCustomerTable();  //method called from AddAppointmentController
            
            dialogStage.showAndWait();
            
            return true;
        }  //end try
        catch (IOException e){
            e.printStackTrace();
            return false;
        }  //end catch
    }//end initAddApptScrn
    //method to initiate mod appt scrn
    public static boolean initModApptScrn(Appointment appointment){
                try {
            //load fxml file and create new stage for dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ScheduleApp.class.getResource("ModAppointment.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            
            //create dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Modify Appointment Screen");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            
            ModAppointmentController controller = loader.getController();
            controller.setAppointmentDetails(appointment);  //method called from ModCustomerController
            controller.setCustomerTable();  //method called from AddAppointmentController
            
            dialogStage.showAndWait();
            
            return true;
        }  //end try
        catch (IOException e){
            e.printStackTrace();
            return false;
        }  //end catch

    }//end initModApptScrn
    //method to initiate customer details scrn
    public static boolean initCustomerDetailsScrn(Customer customer){
        try {
            //load fxml file and create new stage for dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ScheduleApp.class.getResource("CustomerDetailsScrn.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            
            //create dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Customer Details Screen");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            
            CustomerDetailsScrnController controller = loader.getController();
            controller.setCustomerDetailsScrnToController(customer);   //method called from AddAppointmentController
            
            dialogStage.showAndWait();
            
            return true;
        }  //end try
        catch (IOException e){
            e.printStackTrace();
            return false;
        }  //end catch
    }//end initAddApptScrn
    //method to initiate report scrn
    public static boolean initReportsScrn(){
        try {
            //load fxml file and create new stage for dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ScheduleApp.class.getResource("Reports.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            
            //create dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Report Screen");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            
//            CustomerDetailsScrnController controller = loader.getController();
//            controller.setCustomerDetailsScrnToController(customer);   //method called from AddAppointmentController
            
            dialogStage.showAndWait();
            
            return true;
        }  //end try
        catch (IOException e){
            e.printStackTrace();
            return false;
        }  //end catch
    }//end initReportsScrn
    
}//end Schedule App