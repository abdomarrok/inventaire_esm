package com.marrok.inventaire_esm.controller.fournisseur;

import com.marrok.inventaire_esm.model.Fournisseur;
import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.database.FournisseurDbHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class UpdateController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField rcField;
    @FXML
    private TextField nifField;
    @FXML
    private TextField aiField;
    @FXML
    private TextField nisField;
    @FXML
    private TextField telField;
    @FXML
    private TextField faxField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField emailField;

    private FournisseurController fournisseurController;
    private Fournisseur selectedFournisseur;
    private FournisseurDbHelper fournisseurDbHelper = new FournisseurDbHelper();

    public UpdateController() throws SQLException {
    }

    // Method to set the selected Fournisseur and pre-populate the fields
    public void setSelectedFournisseur(Fournisseur fournisseur) {
        this.selectedFournisseur = fournisseur;
        nameField.setText(fournisseur.getName());
        rcField.setText(fournisseur.getRc());
        nifField.setText(fournisseur.getNif());
        aiField.setText(fournisseur.getAi());
        nisField.setText(fournisseur.getNis());
        telField.setText(fournisseur.getTel());
        faxField.setText(fournisseur.getFax());
        addressField.setText(fournisseur.getAddress());
        emailField.setText(fournisseur.getEmail());
    }

    // Link to the main controller
    public void setFournisseurController(FournisseurController fournisseurController) {
        this.fournisseurController = fournisseurController;
    }

    // Handle the update action
    @FXML
    public void handleUpdateFournisseur(ActionEvent event) {
        String name = nameField.getText();
        String rc = rcField.getText();
        String nif = nifField.getText();
        String ai = aiField.getText();
        String nis = nisField.getText();
        String tel = telField.getText();
        String fax = faxField.getText();
        String address = addressField.getText();
        String email = emailField.getText();

        // Update the Fournisseur object
        selectedFournisseur.setName(name);
        selectedFournisseur.setRc(rc);
        selectedFournisseur.setNif(nif);
        selectedFournisseur.setAi(ai);
        selectedFournisseur.setNis(nis);
        selectedFournisseur.setTel(tel);
        selectedFournisseur.setFax(fax);
        selectedFournisseur.setAddress(address);
        selectedFournisseur.setEmail(email);

        try {
            // Update in database
            int result = fournisseurDbHelper.updateFournisseur(selectedFournisseur);
            if (result != -1) {
                GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "نجاح", "تم تحديث المورد بنجاح.");
                fournisseurController.refreshTableData();
                closeWindow();
            } else {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "فشل في تحديث المورد.");
            }
        } catch (SQLException e) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "حدث خطأ أثناء تحديث المورد.");
            e.printStackTrace();
        }
    }

    // Handle the cancel action
    @FXML
    public void handleCancel(ActionEvent event) {
        closeWindow();
    }

    // Close the current window
    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    public void setFournisseurData(Fournisseur selectedFournisseur) {
        this.selectedFournisseur = selectedFournisseur;

        // Populate the fields with the fournisseur data
        if (selectedFournisseur != null) {
            nameField.setText(selectedFournisseur.getName());
            rcField.setText(selectedFournisseur.getRc());
            nifField.setText(selectedFournisseur.getNif());
            aiField.setText(selectedFournisseur.getAi());
            nisField.setText(selectedFournisseur.getNis());
            telField.setText(selectedFournisseur.getTel());
            faxField.setText(selectedFournisseur.getFax());
            addressField.setText(selectedFournisseur.getAddress());
            emailField.setText(selectedFournisseur.getEmail());
        }
    }
}
