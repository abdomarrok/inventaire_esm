package com.marrok.inventaire_esm.controller.service;

import com.marrok.inventaire_esm.model.Service;
import com.marrok.inventaire_esm.util.DatabaseHelper;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AddServiceController {

    @FXML
    private TextField nameField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private ServicesController servicesController;
    private DatabaseHelper dbhelper=new DatabaseHelper();

    public AddServiceController() throws SQLException {
    }

    public void setDashboardController(ServicesController servicesController) {
        this.servicesController = servicesController;
    }

    @FXML
    private void saveService(ActionEvent event) {
        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "مدخل غير صحيح", "اسم المصلحة لا يمكن أن يكون فارغًا.");
            return;
        }

        Service service = new Service(name);


        boolean success = dbhelper.addService(service);

        if (success) {
            GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "نجاح", "تمت إضافة المصلحة بنجاح.");

            servicesController.refreshTableData();
            closeStage();
        } else {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "فشل في إضافة المصلحة.");

        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
