package com.marrok.inventaire_esm.controller.users;

import com.marrok.inventaire_esm.model.User;
import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.database.UserDbHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateUserController implements Initializable {
    Logger logger = Logger.getLogger(UpdateUserController.class);
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private ChoiceBox<String> roleChoiceBox;
    private UsersController usersController;


    private UserDbHelper udbhlper;
    private User user;  // The user being edited

    public UpdateUserController() {
        try {
            udbhlper = new UserDbHelper();
        } catch (SQLException e) {
           logger.error(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeRoleChoiceBox();
    }

    private void initializeRoleChoiceBox() {
        logger.info("Initializing role choice box");
        // Initialize the ChoiceBox with roles
        roleChoiceBox.getItems().addAll("Admin", "User","Editor"); // Add roles as needed

    }

    public void setUser(User user) {
        logger.info("Setting user");
        this.user = user;
        // Populate fields with user data
        if (user != null) {
            usernameField.setText(user.getUsername());
            passwordField.setText(user.getPassword());
            roleChoiceBox.getSelectionModel().select(user.getRole());
        }
    }

    @FXML
    public void updateUser(ActionEvent event) {
        logger.info("Updating user");
        if (user == null) {
           GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "لم يتم اختيار مستخدم للتحديث.");

            return;
        }

        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String role = roleChoiceBox.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null) {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "خطأ في الإدخال", "يرجى ملء جميع الحقول.");

            return;
        }

        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);

        try {
            udbhlper.updateUser(user); // Assume updateUser method exists in DatabaseHelper
            GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "نجاح", "تم تحديث المستخدم بنجاح.");

            usersController.loadData();
            closeForm();
        } catch (Exception e) {
            logger.error(e);
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "فشل في تحديث المستخدم: " + e.getMessage());

        }
    }

    @FXML
    public void cancel(ActionEvent event) {
        closeForm();
    }

    private void closeForm() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }


    public void setUsersController(UsersController usersController) {
        this.usersController=usersController;
    }
}
