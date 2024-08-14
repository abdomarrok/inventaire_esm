package com.marrok.inventaire_esm.controller.settings;

import com.marrok.inventaire_esm.controller.login.LoginController;
import com.marrok.inventaire_esm.util.DatabaseConnection;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SettingsView {
    

    @FXML
    private void handleBackupData(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Backup Location");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQL Files", "*.sql"));

        // Open the file chooser dialog
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            try {
                // Get the absolute path of the selected file
                String backupPath = selectedFile.getAbsolutePath();

                // Perform the backup
                DatabaseConnection.backupDatabase(backupPath);

                // Show success alert
                GeneralUtil.showAlert(AlertType.INFORMATION, "Backup Data", "Backup completed successfully.");
            } catch (Exception e) {
                // Show error alert
                GeneralUtil.showAlert(AlertType.ERROR, "Backup Data", "Backup failed: " + e.getMessage());

                // Optionally log the exception
                e.printStackTrace();
            }
        } else {
            // Show warning alert if no file was selected
            GeneralUtil.showAlert(AlertType.WARNING, "Backup Data", "No file selected for backup.");
        }
    }


    @FXML
    void handleLogout(ActionEvent event) {
        // Handle Logout Action
        GeneralUtil.goBackLogin(event);
    }

    @FXML
    public void go_Dashboard(ActionEvent event) {
        GeneralUtil.goBackDashboard(event);
    }


    public void goUsers(ActionEvent event) {
        Scene scene;
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(GeneralUtil.class.getResource("/com/marrok/inventaire_esm/view/users/users-view.fxml"));
            root = loader.load();
            scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
