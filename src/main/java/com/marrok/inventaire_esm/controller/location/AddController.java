package com.marrok.inventaire_esm.controller.location;

import com.marrok.inventaire_esm.model.Localisation;
import com.marrok.inventaire_esm.model.Service;
import com.marrok.inventaire_esm.util.database.DatabaseHelper;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddController implements Initializable {

    @FXML
    private TextField locationNameField;

    @FXML
    private TextField roomNameField;

    @FXML
    private TextField floorField;

//    @FXML
//    private TextField serviceField;
    @FXML
    private ChoiceBox<String> serviceField;

    private LocationController locationController;
    private DatabaseHelper dbhlper=new DatabaseHelper();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        load_srv_ch_bx_data();
    }

    private void load_srv_ch_bx_data() {
        List<Service> services = dbhlper.getServices();
        List<String> service_names = new ArrayList<>();
        for (Service service : services) {
            service_names.add(service.getName());
        }
        serviceField.getItems().addAll(service_names);
        serviceField.getSelectionModel().select(0);
    }

    public AddController() throws SQLException {

    }

    @FXML
    private void handleAdd(ActionEvent event) {
        String locName = locationNameField.getText();
        int floor;
        String service = "";
        if (serviceField.getValue() != null) {
            service = serviceField.getValue();
        }else{
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "لا يوجد اختيار", "يجب عليك اختيار مصلحة.");

        }
        try {
            floor = Integer.parseInt(floorField.getText());

        } catch (NumberFormatException e) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "يجب أن يكون رقم الطابق أرقامًا صالحة.");

            return;
        }
        Service selected_service = dbhlper.getServiceByName(service);
        if (selected_service == null) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "المصلحة غير موجودة.");

        }else{
            Localisation newLocation = new Localisation(locName,floor,  selected_service.getId() );
            boolean success = dbhlper.addLocalisation(newLocation);

            if (success) {
                // Reload data in the LocationController
                locationController.loadData();

                // Close the window
                Stage stage = (Stage) locationNameField.getScene().getWindow();
                stage.close();
            } else {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "فشل في إضافة الموقع.");

            }
        }


    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) locationNameField.getScene().getWindow();
        stage.close();
    }



    public void setLocationController(LocationController locationController) {
        this.locationController = locationController;
    }


}
