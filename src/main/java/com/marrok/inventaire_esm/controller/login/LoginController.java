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
        // Initialization logic if needed
    }

    @FXML
    public void processLogin(ActionEvent event) {
        handleLogin(event);
    }

    @FXML
    public void processLogin2(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            handleLogin(keyEvent);
        }
    }

    private void handleLogin(Event event) {
        String user = userId.getText().trim();
        String pass = password.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "حقول فارغة", "يرجى إدخال اسم المستخدم وكلمة المرور.");
            return;
        }

        try {
            UserDbHelper dbHelper = new UserDbHelper();
            if (dbHelper.validateLogin(user, pass)) {
                System.out.println("Login successful");
                if (event instanceof ActionEvent) {
                    GeneralUtil.loadScene("/com/marrok/inventaire_esm/view/stock_dashboard/stock_dashboard_view.fxml", (ActionEvent) event, true);
                } else if (event instanceof KeyEvent) {
                    // Handle scene change differently if needed, or provide a fallback
                    Node source = (Node) event.getSource();
                    Stage stage = (Stage) source.getScene().getWindow();
                    GeneralUtil.loadScene("/com/marrok/inventaire_esm/view/stock_dashboard/stock_dashboard_view.fxml", new ActionEvent(source, stage), true);
                }
            } else {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "فشل التسجيل", "كلمة السر أو اسم المستخدم خاطئ.");
            }
        } catch (SQLException e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "فشل في الاتصال", "حدث خطأ أثناء محاولة تسجيل الدخول. يرجى المحاولة لاحقاً.");
        }
    }

}



