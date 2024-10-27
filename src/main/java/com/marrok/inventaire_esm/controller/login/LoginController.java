package com.marrok.inventaire_esm.controller.login;

import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.database.UserDbHelper;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController extends AnchorPane implements Initializable {

    @FXML
    private TextField userId;
    @FXML
    private PasswordField password;
    @FXML
    private Button login;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    @FXML
    public void processLogin(ActionEvent event) {
       login(event);
    }
    @FXML
    public void processLogin2(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            login(keyEvent); // Call the login method when Enter is pressed
        }
    }


    public void login(Event event){
    String user = userId.getText();
    String pass = password.getText();

    try {
        UserDbHelper dbHelper = new UserDbHelper();
        boolean loginTest = dbHelper.validateLogin(user, pass);

        if (loginTest) {
            System.out.println("Login success");
            showDashboard(event);
        } else {
            System.out.println("Login failed");
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "فشل التسجيل", "كلمة السر او اسم المستخدم خاطئ");
        }
    } catch (SQLException e) {
        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
        GeneralUtil.showAlert(Alert.AlertType.ERROR,"فشل في الاتصال",e.getMessage());
        // Handle exception, possibly by showing an alert to the user
    }
}

private void showDashboard(Event event) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/stock_dashboard/stock_dashboard_view.fxml"));
    try {
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.setCursor(Cursor.HAND);

        // Obtain the stage from the event's source
        Stage stage = null;
        if (event.getSource() instanceof Node) {
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        } else {
            System.err.println("Event source is not a Node");
        }

        if (stage != null) {
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setMaximized(true);
            stage.centerOnScreen();
            stage.show();
        }
    } catch (IOException ex) {
        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
    }
}

}
