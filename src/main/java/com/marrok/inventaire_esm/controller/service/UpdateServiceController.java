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

public class UpdateServiceController {

    @FXML
    private TextField nameField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private ServicesController servicesController;
    private int serviceId;
    private DatabaseHelper dbhelper=new DatabaseHelper();

    public UpdateServiceController() throws SQLException {
    }

    public void setDashboardController(ServicesController servicesController) {
        this.servicesController = servicesController;
    }

    public void setServiceData(int serviceId) {
        this.serviceId = serviceId;
        Service service = dbhelper.getServiceById(serviceId);
        if (service != null) {
            nameField.setText(service.getName());
        } else {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "لم يتم العثور على المصلحة.");

            closeStage();
        }
    }

    @FXML
    private void updateService(ActionEvent event) {
        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "مدخل غير صحيح", "اسم المصلحة لا يمكن أن يكون فارغًا.");

            return;
        }
        int serviceId = this.serviceId;
        Service service = new Service(serviceId,name);

try{
            dbhelper.updateService(service);
            servicesController.refreshTableData();
    GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "نجاح", "تم تحديث المصلحة بنجاح.");

    closeStage();
        } catch (Exception e){
    GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", e.getMessage());

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
