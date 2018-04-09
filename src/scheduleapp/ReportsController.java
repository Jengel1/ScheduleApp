/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleapp;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import static scheduleapp.ModAppointmentController.getApptDateTime;
import static scheduleapp.ScheduleApp.connectToDB;

/**
 * FXML Controller class
 *
 * @author Jared Engelkemier
 */
public class ReportsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private Stage primaryStage;
    private ScheduleApp scheduleApp;
    private ModAppointmentController modAppointmentController;

    @FXML
    private TextArea textArea;
    
    @FXML
    private RadioButton report1;  //appointment types by month
    @FXML
    private RadioButton report2;  //schedule for each consultant
    @FXML
    private RadioButton report3;  //customer location by country
    @FXML
    private ToggleGroup reportRadios;
    
    //method to handle GenerateReport btn
    @FXML
    public void handleGenerateReportBtn(ActionEvent event) throws SQLException{
        String fullReport = "";
        Connection conn = connectToDB();
        Statement stmt = conn.createStatement();
        if (reportRadios.getSelectedToggle().equals(report1)){  //if report1 radio is selected
            //declare initial variables
            StringBuilder sb = new StringBuilder();  //empty string builder to build report string
            String col1 = "Count";  //char length 5
            String col2 = "Type Of Appointment";  //char length 19
            List<Integer> months = new ArrayList<>();  //empty Array to collect Integers representing months
            //first query to DB
            String query1 = "SELECT DISTINCT MONTH(start) AS month FROM appointment ORDER BY MONTH(start);";
            ResultSet rs1 = stmt.executeQuery(query1);  //1,2,3,...
            while (rs1.next()){
                months.add(rs1.getInt("month"));  //add Integers representing months to Array
            }//end while
            for (int i = 0; i < months.size(); i++){  //for each month in array
                int monthNum = months.get(i);  //Integer of month for query 2 variable
                Month month = Month.of(months.get(i));  //String of month name for report header
                //append strings to string builder object to format report
                sb.append(month + ":\n"); 
                sb.append("\t" + col1 + "\t" + col2 +"\n");  //String for column headers
                //second query to DB
                String query2 = "SELECT COUNT(description) AS count, description AS type FROM appointment "
                        + "WHERE MONTH(start) = " + monthNum + " GROUP BY MONTH(start), description;";
                ResultSet rs2 = stmt.executeQuery(query2);
                while (rs2.next()){
                    String count = String.valueOf(rs2.getInt("count"));
                    String type = rs2.getString("type");
                    String spacing = getSpace(col1, count);
                    sb.append("\t" + spacing + count + "      -      " + type + "\n");  //add and format data from DB to columns
                }//end while
                sb.append("\n");  //add space between months for report
            }//end for
            fullReport = sb.toString();
            textArea.setText(fullReport);
        }//end if
        if (reportRadios.getSelectedToggle().equals(report2)){  //if report2 radio selected
            //declare initial variables
            StringBuilder sb = new StringBuilder();  //empty string builder to build report string
            List<String> consultants = new ArrayList<>();  //empty Array to collect consultantName Strings
            //first query to DB
            String query1 = "SELECT DISTINCT lastUpdateBy AS consultant FROM appointment ORDER BY lastUpdateBy;";
            ResultSet rs1 = stmt.executeQuery(query1);  //String, String,...
            while (rs1.next()){
                consultants.add(rs1.getString("consultant"));  //add Strings representing consultants to Array
            }//end while
            for (int i = 0; i < consultants.size(); i++){  //for each month in array
                String consultantName = consultants.get(i).toString();
                //append strings to string builder object to format report
                sb.append(consultantName + " has the following schedule:\n"); 
                //second query to DB
                String query2 = "SELECT appointmentId, start FROM appointment "
                        + "WHERE lastUpdateBy = '" + consultantName + "' ORDER BY start;";
                ResultSet rs2 = stmt.executeQuery(query2);
                while (rs2.next()){
                    sb.append("\tAppointment " + rs2.getInt("appointmentId") + " on "
                    + getDate(scheduleApp.getConvertedApptTime(rs2.getTimestamp("start"))) + " @ "
                    + getTime(scheduleApp.getConvertedApptTime(rs2.getTimestamp("start"))) + "\n");
                }//end while
                sb.append("\n");  //add space between months for report
            }//end for
            fullReport = sb.toString();
            textArea.setText(fullReport);
        }//end if
        if (reportRadios.getSelectedToggle().equals(report3)){  //if report3 radio selected
            //declare initial variables
            StringBuilder sb = new StringBuilder();  //empty string builder to build report string
            List<String> country = new ArrayList<>();  //empty Array to collect country Strings
            //first query to DB
            String query1 = "SELECT DISTINCT country FROM customer c, address a, city i, country t "
                    + "WHERE c.addressId = a.addressId AND a.cityId = i.cityId AND i.countryId = t.countryId;";
            ResultSet rs1 = stmt.executeQuery(query1);  //String, String,...
            while (rs1.next()){
                country.add(rs1.getString("country"));  //add String representing country to Array
            }//end while
            //System.out.println("Expected size is 3.  Actual size: " + country.size());  //size of array
            for (int i = 0; i < country.size(); i++){  //for each month in array
                String countryName = country.get(i).toString();
                //append strings to string builder object to format report
                sb.append(countryName + ":\n"); 
                //second query to DB
                String query2 = "SELECT customerName, address, city, postalCode, phone "
                        + "FROM customer c, address a, city i, country t "
                        + "WHERE c.addressId = a.addressId AND a.cityId = i.cityId AND i.countryId = t.countryId "
                        + "AND t.countryId = (SELECT countryId FROM country WHERE country = '" + countryName + "');";
                ResultSet rs2 = stmt.executeQuery(query2);
                while (rs2.next()){
                    sb.append("\t" + rs2.getString("customerName") + "\n" 
                            + "\t" + rs2.getString("address") + ", " + rs2.getString("city") + ", " + rs2.getString("postalCode") + "\n" 
                            + "\t" + rs2.getString("phone") + "\n\n");
                }//end while
                sb.append("\n");  //add space between countries for report
            }//end for
            fullReport = sb.toString();
            textArea.setText(fullReport);
        }//end if
        conn.close();    
    }//end handleGenerateReportBtn
    
    /*
    //Utility methods to format reports
    */
    //method to find offset for report1 column data
    public String getSpace(String colName, String value){
        String space = "";
        int col = colName.length();
        int val = value.length();
        int difference = col - val;
            for (int i = 0; i < difference; i++){
                space += " "; //add one space
            }//end for
        return space;
    }//end getSpacing
    //method to return date for report2 data
    public String getDate(LocalDateTime ldt){
        String ldtFormatted = getApptDateTime(ldt);  //new string format "MM/dd/yyyy hh:mm a"
        String date = ldtFormatted.substring(0, 10);
        return date;
    }//end getDate
    //method to return time for report2 data
    public static String getTime(LocalDateTime ldt){
        String ldtFormatted = getApptDateTime(ldt);  //new string format "MM/dd/yyyy hh:mm a"
        String time = ldtFormatted.substring(11);
        return time;
    }//end getTime
    
    //method to handle CancelBtn
    @FXML
    public void handleCancel(ActionEvent event){
        primaryStage = (Stage) textArea.getScene().getWindow();  //reference to this screen
        primaryStage.close();
    }//end handleCancel
    
}//end ReportsController