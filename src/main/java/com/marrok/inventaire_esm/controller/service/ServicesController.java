package com.marrok.inventaire_esm.controller.service;

import com.marrok.inventaire_esm.model.Service;
import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.database.EmployerDbHelper;
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
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ServicesController implements Initializable {
    Logger logger = Logger.getLogger(ServicesController.class);

    public TableColumn<Service,String> chef_service_Column;
    @FXML
    private TableView<Service>  servicesTable;

    @FXML
    private TableColumn<Service,Integer> idColumn;

    @FXML
    private TableColumn<Service,String> nameColumn;

    @FXML
    private TextField searchField;

    @FXML
    private Button bk_Dashboard_from_services;

    private ObservableList<Service> servicesList;
    private FilteredList<Service> filteredServicesList;
    private Service selectedService;

    private ServiceDbHelper serviceDbHelper= new ServiceDbHelper();
    private EmployerDbHelper employerDbHelper=new EmployerDbHelper();

    public ServicesController() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("Initializing ServicesController");
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
        logger.info("Initializing columns");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        chef_service_Column.setCellValueFactory(cellData ->{
            int empl_id=cellData.getValue().getChef_service_id();

            String employerFullName = employerDbHelper.getEmployerFullNameById(empl_id);
            if (employerFullName != null) {
                return new SimpleStringProperty(employerFullName);
            } else {
                return new SimpleStringProperty("Unknown");
            }
        } );
    }

    @FXML
    private void loadServices() {
        logger.info("Loading services");
        servicesList = FXCollections.observableArrayList(serviceDbHelper.getServices());

        filteredServicesList = new FilteredList<>(servicesList, p -> true);
        servicesTable.setItems(filteredServicesList);
        servicesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }



    private void setupTableSelectionListener() {
        servicesTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedService = (Service) newValue);

        bk_Dashboard_from_services.setOnAction(GeneralUtil::goBackStockDashboard);
    }

    @FXML
    public void addService(ActionEvent event) {
        logger.info("Adding service");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/service/add_form-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("اظافة مصلحة");
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));
            AddServiceController controller = loader.getController();
            controller.setDashboardController(this);

            stage.showAndWait();
        } catch (IOException e) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "تعذر فتح نموذج إضافة المصلحة.");
            logger.error(e);
        }
    }

    @FXML
    public void updateService(ActionEvent event) {
        logger.info("Updating service");
        Service selectedService = (Service) servicesTable.getSelectionModel().getSelectedItem();
        if (selectedService != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/service/update_form-view.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.setTitle("تحديث المصلحة");
                stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));
                UpdateServiceController controller = loader.getController();
                controller.setServiceData(selectedService.getId());
                controller.setDashboardController(this);

                stage.show();
            } catch (IOException e) {
               logger.error(e);
            }
        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "لا يوجد اختيار", "يرجى اختيار مصلحة للتحديث.");

        }
    }
    @FXML
    public void deleteService(ActionEvent event) {
        logger.info("deleteService");
        Service selectedService = (Service) servicesTable.getSelectionModel().getSelectedItem();

        if (selectedService != null) {
            boolean success = serviceDbHelper.deleteService(selectedService.getId());

            if (success) {
                logger.info("deleteService success");
                servicesList.remove(selectedService);
                GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "تم حذف المصلحة", "تم حذف المصلحة بنجاح.");
            } else {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "فشل في حذف المصلحة", "حدث خطأ أثناء حذف المصلحة.");
            }
        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "لا يوجد اختيار", "يرجى اختيار مصلحة للحذف.");
        }
    }




    public void refreshTableData() {
        servicesList.setAll(serviceDbHelper.getServices());
        servicesTable.refresh();
    }
}
