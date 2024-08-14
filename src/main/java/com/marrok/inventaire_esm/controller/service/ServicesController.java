package com.marrok.inventaire_esm.controller.service;

import com.marrok.inventaire_esm.model.Service;
import com.marrok.inventaire_esm.util.DatabaseHelper;
import com.marrok.inventaire_esm.util.GeneralUtil;
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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class ServicesController implements Initializable {

    @FXML
    private TableView  servicesTable;

    @FXML
    private TableColumn idColumn;

    @FXML
    private TableColumn nameColumn;

    @FXML
    private TextField searchField;

    @FXML
    private ToggleButton switchThemeBtn_service;

    @FXML
    private Button bk_Dashboard_from_services;

    private ObservableList<Service> servicesList;
    private FilteredList<Service> filteredServicesList;
    private Service selectedService;

    private final Properties themeProperties = new Properties();
    private DatabaseHelper dbhelper=new DatabaseHelper();

    public ServicesController() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadServices();
        initializeColumns();
        setupSearchFilter();
        setupTableSelectionListener();
    }
    private void setupSearchFilter() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredServicesList.setPredicate(service -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return service.getName().toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(service.getId()).contains(lowerCaseFilter);
            });
        });
    }
    private void initializeColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    @FXML
    private void loadServices() {
        servicesList = FXCollections.observableArrayList(dbhelper.getServices());
        for (Service service : servicesList) {
            System.out.println(service.getServiceDetails());
        }
        filteredServicesList = new FilteredList<>(servicesList, p -> true);
        servicesTable.setItems(filteredServicesList);
        servicesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }



    private void setupTableSelectionListener() {
        servicesTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedService = (Service) newValue);

        bk_Dashboard_from_services.setOnAction(GeneralUtil::goBackDashboard);
    }

    @FXML
    public void addService(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/service/add_form-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
//            TransitTheme transitTheme = new TransitTheme(Style.LIGHT);
//            transitTheme.setScene(scene);
            stage.setScene(scene);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("اظافة مصلحة");
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));
            AddServiceController controller = loader.getController();
            controller.setDashboardController(this);

            stage.showAndWait();
        } catch (IOException e) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Could not open the add service form.");
            e.printStackTrace();
        }
    }

    @FXML
    public void updateService(ActionEvent event) {
        Service selectedService = (Service) servicesTable.getSelectionModel().getSelectedItem();
        if (selectedService != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/service/update_form-view.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.setTitle("Update Service");
                stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));
                UpdateServiceController controller = loader.getController();
                controller.setServiceData(selectedService.getId());
                controller.setDashboardController(this);

                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a service to update.");
        }
    }

    @FXML
    public void deleteService(ActionEvent event) {
        Service selectedService = (Service) servicesTable.getSelectionModel().getSelectedItem();
        if (selectedService != null) {
            boolean success = dbhelper.deleteService(selectedService.getId());
            if (success) {
                servicesList.remove(selectedService);
                GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "Service Deleted", "The service was deleted successfully.");
            } else {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Delete Service Failed", "Failed to delete the service.");
            }
        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a service to delete.");
        }
    }


    public ObservableList<Service> getServicesList() {
        return servicesList;
    }

    public void refreshTableData() {
        servicesList.setAll(dbhelper.getServices());
        servicesTable.refresh();
    }
}
