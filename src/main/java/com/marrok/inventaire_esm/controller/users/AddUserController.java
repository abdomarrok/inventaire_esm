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

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddUserController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeRoleChoiceBox();
    }

    private UsersController UserController;

    public void  setUsersController(UsersController UserController) {
        this.UserController=UserController;
    }

    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private ChoiceBox<String> roleChoiceBox;

    private UserDbHelper dbHelper;

    {
        try {
            dbHelper = new UserDbHelper();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeRoleChoiceBox() {
        // Initialize the ChoiceBox with roles
        roleChoiceBox.getItems().addAll("Admin", "User","Editor"); // Add roles as needed
        roleChoiceBox.getSelectionModel().selectFirst(); // Select default role
    }

    @FXML
    public void addUser(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String role = roleChoiceBox.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null) {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "خطأ في الإدخال", "يرجى ملء جميع الحقول.");

            return;
        }

        User user = new User(username, password, role);

        try {
            dbHelper.addUser(user);// Assume addUser method exists in DatabaseHelper
            UserController.loadData();
            GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "نجاح", "تم إضافة المستخدم بنجاح.");

            closeForm();
        } catch (Exception e) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "فشل في إضافة المستخدم: " + e.getMessage());

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


}
