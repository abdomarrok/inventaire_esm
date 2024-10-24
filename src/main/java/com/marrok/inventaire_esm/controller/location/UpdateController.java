package com.marrok.inventaire_esm.controller.location;

import com.marrok.inventaire_esm.model.Localisation;
import com.marrok.inventaire_esm.model.Service;
import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.database.LocDbhelper;
import com.marrok.inventaire_esm.util.database.ServiceDbHelper;
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

public class UpdateController implements Initializable {

    @FXML
    private TextField locationNameField;
    private ServiceDbHelper serviceDbHelper= new ServiceDbHelper();

    @FXML
    private TextField floorField;

//    @FXML
//    private TextField serviceField;
    @FXML
    private ChoiceBox<String> serviceField;

    private Localisation localisation;
    private LocationController locationController;
    LocDbhelper locDbhelper = new LocDbhelper();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        load_srv_ch_bx_data();
    }
    private void load_srv_ch_bx_data() {
        List<Service> services = serviceDbHelper.getServices();
        List<String> service_names = new ArrayList<>();
        for (Service service : services) {
            service_names.add(service.getName());
        }
        serviceField.getItems().addAll(service_names);
        serviceField.getSelectionModel().select(0);
    }
    public UpdateController() throws SQLException {
    }

    public void setLocationController(LocationController locationController) {
        this.locationController = locationController;
    }

    public void setLocalisation(Localisation localisation) {
        this.localisation = localisation;
        locationNameField.setText(localisation.getLocName());
        floorField.setText(String.valueOf(localisation.getFloor()));

       // serviceField.setText(String.valueOf(localisation.getIdService()));
        String service_name = serviceDbHelper.getServiceById(localisation.getIdService()).getName();
        serviceField.setValue(service_name);
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        String locName = locationNameField.getText();

        int floor;

        try {
            floor = Integer.parseInt(floorField.getText());
        } catch (NumberFormatException e) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "رقم الطابق يجب أن يكون أرقامًا صالحة.");

            return;
        }

        localisation.setLocName(locName);
        localisation.setFloor(floor);
        Service selected_service = serviceDbHelper.getServiceByName(serviceField.getValue());
        if (selected_service == null) {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "لا يوجد اختيار", "لم يتم اختيار خدمة.");

        }else{
            localisation.setIdService(selected_service.getId());

            boolean success = locDbhelper.updateLocalisation(localisation);

            if (success) {
                locationController.loadData();
                Stage stage = (Stage) locationNameField.getScene().getWindow();
                stage.close();
            } else {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "فشل في تحديث الموقع.");

            }
        }

    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) locationNameField.getScene().getWindow();
        stage.close();
    }


}
