package com.marrok.inventaire_esm.controller.login;

import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.database.UserDbHelper;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController extends AnchorPane implements Initializable {
    Logger logger = LogManager.getLogger(LoginController.class);
    UserDbHelper dbHelper = new UserDbHelper();

    @FXML
    private TextField userId;
    @FXML
    private PasswordField password;


    public LoginController() throws SQLException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("Initializing Login Controller");
    }

    @FXML
    public void processLogin(ActionEvent event) {
        logger.info("Processing Login Event");
        handleLogin(event);
    }

    @FXML
    public void processLogin2(KeyEvent keyEvent) {
        logger.info("Processing Login KeyEvent");
        if (keyEvent.getCode() == KeyCode.ENTER) {
            handleLogin(keyEvent);
        }
    }

    private void handleLogin(Event event) {
        logger.info("Handling Login Event");
        String user = userId.getText().trim();
        String pass = password.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "حقول فارغة", "يرجى إدخال اسم المستخدم وكلمة المرور.");
            return;
        }

        if (dbHelper.validateLogin(user, pass)) {
            logger.info("Login successful");
            if (event instanceof ActionEvent) {
                GeneralUtil.loadScene("/com/marrok/inventaire_esm/view/stock_dashboard/stock_dashboard_view.fxml", (ActionEvent) event, true);
            } else if (event instanceof KeyEvent) {
                // Handle scene change differently if needed, or provide a fallback
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                GeneralUtil.loadScene("/com/marrok/inventaire_esm/view/stock_dashboard/stock_dashboard_view.fxml", new ActionEvent(source, stage), true);
            }
        } else {
            logger.info("Login failed");
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "فشل التسجيل", "كلمة السر أو اسم المستخدم خاطئ.");
        }
    }

}



