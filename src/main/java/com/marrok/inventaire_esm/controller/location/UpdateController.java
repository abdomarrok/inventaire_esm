package com.marrok.inventaire_esm.controller.location;

import com.marrok.inventaire_esm.model.Localisation;
import com.marrok.inventaire_esm.model.Service;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UpdateController implements Initializable {

    @FXML
    private TextField locationNameField;


    @FXML
    private TextField floorField;

//    @FXML
//    private TextField serviceField;
    @FXML
    private ChoiceBox<String> serviceField;

    private Localisation localisation;
    private LocationController locationController;
    private  DatabaseHelper dbhlper=new DatabaseHelper();

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
        String service_name = dbhlper.getServiceById(localisation.getIdService()).getName();
        serviceField.setValue(service_name);
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        String locName = locationNameField.getText();

        int floor;

        try {
            floor = Integer.parseInt(floorField.getText());
        } catch (NumberFormatException e) {
            showAlert("Floor must be valid numbers.");
            return;
        }

        localisation.setLocName(locName);
        localisation.setFloor(floor);
        Service selected_service = dbhlper.getServiceByName(serviceField.getValue());
        if (selected_service == null) {
            showAlert("no service selected");
        }else{
            localisation.setIdService(selected_service.getId());

            boolean success = dbhlper.updateLocalisation(localisation);

            if (success) {
                locationController.loadData();
                Stage stage = (Stage) locationNameField.getScene().getWindow();
                stage.close();
            } else {
                showAlert("Failed to update location.");
            }
        }

    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) locationNameField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
