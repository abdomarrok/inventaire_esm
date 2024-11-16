package com.marrok.inventaire_esm.controller.settings;

import com.marrok.inventaire_esm.controller.login.LoginController;
import com.marrok.inventaire_esm.util.database.DatabaseConnection;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;


public class SettingsView {
    Logger logger = Logger.getLogger("SettingsView");
    

    @FXML
    private void handleBackupData(ActionEvent event) {
        logger.log(Level.INFO, "Backup Data");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("اختر موقع النسخ الاحتياطي");

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
                GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "نسخ احتياطي", "اكتمل النسخ الاحتياطي بنجاح.");

            } catch (Exception e) {
                // Show error alert
                logger.error(e);
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "نسخ احتياطي", "فشل النسخ الاحتياطي: " + e.getMessage());
            }
        } else {
            // Show warning alert if no file was selected
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "نسخ احتياطي", "لم يتم اختيار ملف للنسخ الاحتياطي.");

        }
    }


    @FXML
    void handleLogout(ActionEvent event) {
        // Handle Logout Action
        GeneralUtil.goBackLogin(event);
    }

    @FXML
    public void go_Dashboard(ActionEvent event) {
        GeneralUtil.goBackStockDashboard(event);
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
          logger.error(ex);
        }
    }


}
