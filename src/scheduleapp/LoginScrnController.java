/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduleapp;

import java.net.URL;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static scheduleapp.ScheduleApp.getTimestamp;
import static scheduleapp.ScheduleApp.listOfUsers;
import static scheduleapp.ScheduleApp.writeLog;

/**
 * FXML Controller class
 *
 * @author JaredEngelkemier
 */
public class LoginScrnController {

    /**
     * Initializes the LoginScrnController class.
     */
    //determines the system Locale
    public void initialize() {
        if (Locale.getDefault().getDisplayCountry().equals("France")){
            displayFrench();
        }//end if
    }//end initialize
    
    @FXML
    private Label scrnTitleLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Button okBtn;
    @FXML
    private Button exitBtn;
    
    
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    private Stage primaryStage;
    public static ScheduleApp scheduleApp;
    
    private static String currentUser;
    public static Timestamp logIn;
   
    //method to change login scrn Labels if Locale is "fr"
    private void displayFrench(){
        scrnTitleLabel.setText("s'identifier écran");  //login screen
        usernameLabel.setText("Nom d'utilisateur");  //username
        passwordLabel.setText("mot de passe");  //password
        okBtn.setText("D'accord");  //ok
        exitBtn.setText("Sortie");  //exit
    }//end displayFrench
    
    //method called when user selects OK btn
    @FXML
    public void handleOKbtn(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        Boolean found = false;
        for (User u : listOfUsers){
            if ((u.getUsername().equals(username)) && (u.getPassword().equals(password))){
                logIn = getTimestamp(); 
                currentUser = u.getUsername();
                setCurrentUser(currentUser);
                writeLog(logIn, currentUser, "In");
                primaryStage = (Stage) usernameField.getScene().getWindow();  //reference to LoginScrn.fxml using usernameField
                primaryStage.close();
                scheduleApp.initMainAppScrn(); //calls initMainScrn method from ScheduleApp class
                found = true;
            }//end if
        }//end for
        //alert in English
        if ((!found) && (Locale.getDefault().getDisplayCountry().equals("United States"))){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("The username or password you entered is incorrect!");
            alert.setContentText("Please enter the correct username and password.");
            alert.showAndWait();
        }//end if
        //alert in French
        if ((!found) && (Locale.getDefault().getDisplayCountry().equals("France"))){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dialogue d'erreur");
            alert.setHeaderText("Le nom d'utilisateur ou le mot de passe que vous avez tapé est faux!");
            alert.setContentText("Veuillez entrer le nom d'utilisateur et le mot de passe exact.");
            alert.showAndWait();
        }//end if

    }//end handleOKbtn
    
    public static String getCurrentUser(){return currentUser;}
    public void setCurrentUser(String currentUser){this.currentUser = currentUser;}

    //method called when user selects the exit btn
    @FXML
    public void handleExit(ActionEvent event) {
        primaryStage = (Stage) usernameField.getScene().getWindow();  //reference to LoginScrn.fxml using usernameField
        primaryStage.close();
    }  //end handleExit
    
}//end LoginScrnController