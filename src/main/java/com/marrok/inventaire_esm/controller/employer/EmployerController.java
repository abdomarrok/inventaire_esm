package com.marrok.inventaire_esm.controller.employer;


import com.marrok.inventaire_esm.model.Employer;
import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.database.EmployerDbHelper;
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

public class EmployerController implements Initializable {
    Logger logger = LogManager.getLogger(EmployerController.class);

    @FXML
    public TableView<Employer> tableView;

    @FXML
    public TableColumn<Employer, Integer> id_column;
    @FXML
    public TableColumn<Employer, String> firstname_column;
    @FXML
    public TableColumn<Employer, String> lastname_column;
    @FXML
    public TableColumn<Employer, String> title_column;

    @FXML
    public TextField searchField;

    @FXML
    public Button addButton;
    @FXML
    public Button updateButton;
    @FXML
    public Button deleteButton;

    private ObservableList<Employer> employerList;
    private FilteredList<Employer> filteredEmployerList;
    @FXML
    public Button bk_Dashboard_from_employers;

    private Employer selectedEmployer;

    private EmployerDbHelper employerDbHelper=new EmployerDbHelper();

    public EmployerController() throws SQLException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("Initializing EmployerController");
        loadData();
        initializeColumns();
        setupSearchFilter();
        setupTableSelectionListener();
    }

    private void initializeColumns() {
        // Set cell value factories
        logger.info("Initializing EmployerController");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_LAST_COLUMN);
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstname_column.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastname_column.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        title_column.setCellValueFactory(new PropertyValueFactory<>("title"));
    }

    public void loadData() {
        logger.info("Loading EmployerController");
        employerList = FXCollections.observableArrayList(employerDbHelper.getAllEmployers());
        filteredEmployerList = new FilteredList<>(employerList, p -> true);
        tableView.setItems(filteredEmployerList);
    }

    private void setupSearchFilter() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredEmployerList.setPredicate(employer -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return employer.getFirstName().toLowerCase().contains(lowerCaseFilter)
                        || employer.getLastName().toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(employer.getId()).contains(lowerCaseFilter);
            });
        });
    }


    private void setupTableSelectionListener() {
        bk_Dashboard_from_employers.setOnAction(event -> {
          GeneralUtil.goBackStockDashboard(event);
        });
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedEmployer = newValue);
    }

    @FXML
    public void addEmployer(ActionEvent event) {
        logger.info("Adding Employer");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/employer/add_form-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("إضافة موظف");

            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));
            AddController controller = loader.getController();
            controller.setEmployerController(this);

            stage.showAndWait();
        } catch (IOException e) {
            logger.error(e);
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "تعذر فتح نموذج إضافة الموظف.");

        }
    }

    @FXML
    public void updateEmployer(ActionEvent event) {
        logger.info("Updating Employer");
        Employer selectedEmployer = tableView.getSelectionModel().getSelectedItem();
        if (selectedEmployer != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/employer/update_form-view.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.setTitle("تحديث موظف");

                stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));
                UpdateController controller = loader.getController();
                controller.setEmployerData(selectedEmployer);
                controller.setEmployerController(this);

                stage.show();
            } catch (IOException e) {
               logger.error(e);
            }
        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "لا يوجد اختيار", "يرجى اختيار موظف للتحديث.");

        }
    }

    @FXML
    public void deleteEmployer(ActionEvent event) {
        logger.info("Deleting Employer");
        Employer selectedEmployer = tableView.getSelectionModel().getSelectedItem();

        if (selectedEmployer != null) {
            boolean success = employerDbHelper.deleteEmployer(selectedEmployer.getId());

            if (success) {
                logger.info("Deleted Employer");
                employerList.remove(selectedEmployer);
                GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "تم حذف الموظف", "تم حذف الموظف بنجاح.");
            } else {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "فشل حذف الموظف", "فشل في حذف الموظف.");
            }
        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "لا يوجد اختيار", "يرجى اختيار موظف للحذف.");
        }

    }

    public ObservableList<Employer> getEmployerList() {
        return employerList;
    }

    public void refreshTableData() {
        employerList.setAll(employerDbHelper.getEmployers());
        tableView.refresh();
    }

}
