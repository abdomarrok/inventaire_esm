package com.marrok.inventaire_esm.controller.location;

import com.marrok.inventaire_esm.model.Localisation;
import com.marrok.inventaire_esm.model.Service;
import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.database.LocDbhelper;
import com.marrok.inventaire_esm.util.database.ServiceDbHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LocationController implements Initializable {

    Logger logger = LogManager.getLogger(LocationController.class);
    public TableColumn location_name;
    public TableColumn room_name;
    public TableColumn<Localisation,Integer> floor_id;
    public TableColumn<Localisation,String> service_name;
    public TextField search_location_txt;
    public TableView location_tableView;
    public TableColumn location_id;

    private ObservableList<Localisation> locationsList;
    private FilteredList<Localisation> filteredLocationsList;
    private Localisation selectedLocalisation;

    private ServiceDbHelper serviceDbHelper= new ServiceDbHelper();
    LocDbhelper locDbhelper = new LocDbhelper();

    public LocationController() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("Initializing LocationController");
        loadData();
        initializeColumns();
        setupSearchFilter();
    }

    private void setupSearchFilter() {
        search_location_txt.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredLocationsList.setPredicate(localisation -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return localisation.getLocName().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    private void initializeColumns() {
        logger.info("Initializing columns");
        location_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        location_name.setCellValueFactory(new PropertyValueFactory<>("locName"));
        floor_id.setCellValueFactory(new PropertyValueFactory<>("floor"));
        //service_id.setCellValueFactory(new PropertyValueFactory<>("idService"));
        service_name.setCellValueFactory(cellData->{
            int service_id =cellData.getValue().getIdService();
            try{
                Service currentservice=serviceDbHelper.getServiceById(service_id);
                String service_name= currentservice.getName();
                return new SimpleStringProperty(service_name);
            }catch (Exception e){
                return new SimpleStringProperty("مصلحة غير معرفة");
            }

        });

        location_tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        location_tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedLocalisation = (Localisation) newValue);

    }

    public void loadData() {
        logger.info("Loading data");
        locationsList = FXCollections.observableArrayList(locDbhelper.getLocalisations());
        filteredLocationsList = new FilteredList<>(locationsList, p -> true);
        location_tableView.setItems(filteredLocationsList);
    }

    @FXML
    public void deleteLocation(ActionEvent event) {
        logger.info("Deleting location");
        if (selectedLocalisation == null) {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "لا يوجد اختيار", "لم يتم اختيار موقع. يرجى اختيار موقع للحذف.");
            return;
        }

        boolean success = locDbhelper.deleteLocalisation(selectedLocalisation.getId());

        if (success) {
            logger.info("Location deleted successfully");
            loadData();
        } else {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "فشل في حذف الموقع.");

        }
    }


    @FXML
    public void updateLocation(ActionEvent event) {
        logger.info("Updating location");
        if (selectedLocalisation == null) {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "لا يوجد اختيار", "لم يتم اختيار موقع. يرجى اختيار موقع للتحديث.");

            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/location/Update.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("تحديث المكان");
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));
            UpdateController controller = loader.getController();
            controller.setLocationController(this);
            controller.setLocalisation(selectedLocalisation);

            stage.showAndWait();
        } catch (IOException e) {
            logger.error(e);
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "تعذر فتح نموذج تحديث العنصر.");

        }
    }

    @FXML
    public void addLocation(ActionEvent event) {
        logger.info("Adding location");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/location/Add.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("إظافة مكان");
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));
            AddController controller = loader.getController();
            controller.setLocationController(this);


            stage.showAndWait();
        } catch (IOException e) {
            logger.error(e);
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "تعذر فتح نموذج إضافة العنصر.");
        }
    }

    @FXML
    public void go_Dashboard(ActionEvent event) {
        GeneralUtil.goBackStockDashboard(event);
    }
}
