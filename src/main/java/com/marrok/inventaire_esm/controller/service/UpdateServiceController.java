package com.marrok.inventaire_esm.controller.service;

import com.dlsc.gemsfx.FilterView;
import com.marrok.inventaire_esm.model.Employer;
import com.marrok.inventaire_esm.model.Service;
import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.database.EmployerDbHelper;
import com.marrok.inventaire_esm.util.database.ServiceDbHelper;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class UpdateServiceController implements Initializable {
    Logger logger = LogManager.getLogger(UpdateServiceController.class);

    @FXML
    private TextField nameField;

    public FilterView<Employer> filterView2;
    public TableView<Employer> tbData2;
    public TableColumn<Employer, Integer> id_E;
    public TableColumn<Employer, String> firstname_E;
    public TableColumn<Employer, String> lastname_E;
    private ObservableList<Employer> emploerlist;

    @FXML
    private Button cancelButton;

    private ServicesController servicesController;
    private int serviceId;
    private ServiceDbHelper serviceDbHelper= new ServiceDbHelper();
    private EmployerDbHelper employerDbHelper=new EmployerDbHelper();

    public UpdateServiceController() throws SQLException {
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("Initializing UpdateServiceController");
        initTable();
        loadFilter();
        loadTableData();
        CSSFX.start();
    }
    public void setDashboardController(ServicesController servicesController) {
        this.servicesController = servicesController;
    }
    private void initTable() {
        logger.info("Initializing table");
        id_E.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstname_E.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastname_E.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    }
    private void loadTableData() {
        logger.info("Loading table data");
        List<Employer> employers = employerDbHelper.getEmployers();
        emploerlist =  FXCollections.observableArrayList(employers);
        filterView2.getItems().setAll(emploerlist);
    }
    private void loadFilter() {
        logger.info("Loading filter");
        filterView2.getFilterGroups().clear();

        filterView2.setTextFilterProvider(text -> employer -> {
            if(text==null || text.isEmpty()){
                return true;
            }
            String lowerCase = text.toLowerCase();
            return employer.getFirstName().toLowerCase().contains(lowerCase)||
                    employer.getLastName().toLowerCase().contains(lowerCase)||
                    String.valueOf(employer.getId()).toLowerCase().contains(lowerCase);
        });
        loadTableData();
        SortedList<Employer> sortedList2 = new SortedList<>(filterView2.getFilteredItems());
        tbData2.setItems(sortedList2);
        sortedList2.comparatorProperty().bind(tbData2.comparatorProperty());
    }

    public void setServiceData(int serviceId) {
        logger.info("Setting service data");
        this.serviceId = serviceId;
        Service service = serviceDbHelper.getServiceById(serviceId);


        if (service != null) {
            Employer employer = employerDbHelper.getEmployerById(service.getChef_service_id());
            if(employer!=null){
                tbData2.getSelectionModel().select(employer);
            }
            nameField.setText(service.getName());


        } else {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "لم يتم العثور على المصلحة.");

            closeStage();
        }
    }

    @FXML
    private void updateService(ActionEvent event) {
        logger.info("Updating service");
        String name = nameField.getText().trim();
        int serviceId = this.serviceId;
        Employer selectedEmployer = tbData2.getSelectionModel().getSelectedItem();

        if (name.isEmpty()) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "مدخل غير صحيح", "اسم المصلحة لا يمكن أن يكون فارغًا.");

            return;
        }
        if (selectedEmployer != null) {

            Service service = new Service(serviceId, name, selectedEmployer.getId());

            boolean success = serviceDbHelper.updateService(service);
            if (success) {
                logger.info("Successfully updated service");
                GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "نجاح", "تمت إضافة المصلحة بنجاح.");

                servicesController.refreshTableData();
                closeStage();
            } else {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "فشل في إضافة المصلحة.");

            }


        } else {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "فشل في إضافة المصلحة. حدد موضفا ليكون رئيس المصلحة");
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
