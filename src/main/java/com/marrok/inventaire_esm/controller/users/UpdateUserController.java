package com.marrok.inventaire_esm.controller.users;

import com.marrok.inventaire_esm.model.User;
import com.marrok.inventaire_esm.util.DatabaseHelper;
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

public class UpdateUserController implements Initializable {
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private ChoiceBox<String> roleChoiceBox;
    private UsersController usersController;

    private DatabaseHelper dbHelper;
    private User user;  // The user being edited

    public UpdateUserController() {
        try {
            dbHelper = new DatabaseHelper();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeRoleChoiceBox();
    }

    private void initializeRoleChoiceBox() {
        // Initialize the ChoiceBox with roles
        roleChoiceBox.getItems().addAll("Admin", "User", "Manager"); // Add roles as needed

    }

    public void setUser(User user) {
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
        if (user == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No user selected for update.");
            return;
        }

        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String role = roleChoiceBox.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill in all fields.");
            return;
        }

        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);

        try {
            dbHelper.updateUser(user); // Assume updateUser method exists in DatabaseHelper
            showAlert(Alert.AlertType.INFORMATION, "Success", "User updated successfully.");
            usersController.loadData();
            closeForm();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update user: " + e.getMessage());
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

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setUsersController(UsersController usersController) {
        this.usersController=usersController;
    }
}
