package com.marrok.inventaire_esm.controller.fournisseur;

import com.marrok.inventaire_esm.model.Fournisseur;
import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.database.FournisseurDbHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class AddController {
    Logger logger = LogManager.getLogger(AddController.class);
    public TextField ribField;
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

    private FournisseurDbHelper fournisseurDbHelper = new FournisseurDbHelper();
    public AddController() throws SQLException {
    }
    public void setFournisseurController(FournisseurController fournisseurController) {
        logger.info("setFournisseurController");
      this.fournisseurController =fournisseurController;
    }



    @FXML
    public void handleAddFournisseur(ActionEvent event) {
        logger.info("handleAddFournisseur");
        String name = nameField.getText();
        String rc = rcField.getText();
        String nif = nifField.getText();
        String ai = aiField.getText();
        String nis = nisField.getText();
        String tel = telField.getText();
        String fax = faxField.getText();
        String address = addressField.getText();
        String email = emailField.getText();
        String rib= ribField.getText();
if(!name.isEmpty()){
    // Create the Fournisseur object
    Fournisseur fournisseur = new Fournisseur(0,name,rc,nif,ai,nis,tel,fax,address,email,rib);

    int success = fournisseurDbHelper.addFournisseur(fournisseur);

    if (success != -1) {
        logger.info("Fournisseur added");
        GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "نجاح", "تمت إضافة المورد بنجاح.");
        fournisseurController.refreshTableData();
        closeWindow();
    } else {
        GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "فشل في إضافة المورد.");
    }
}else {
    GeneralUtil.showAlertWithOutTimelimit(Alert.AlertType.ERROR,"خانة فارغة","الرجاء ملا الخانات الاساسية");
}
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
