package com.marrok.inventaire_esm.controller.fournisseur;

import com.marrok.inventaire_esm.model.Fournisseur;
import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.SessionManager;
import com.marrok.inventaire_esm.util.database.FournisseurDbHelper;
import com.marrok.inventaire_esm.util.database.UserDbHelper;
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

public class FournisseurController implements Initializable {
    Logger logger = Logger.getLogger(FournisseurController.class);

    public TableView<Fournisseur> fournisseurTableView;
    public TableColumn<Fournisseur,Integer> id_column;
    public TableColumn<Fournisseur,String> name_column;
    public TableColumn<Fournisseur,String> nif_column;
    public TableColumn<Fournisseur,String> ai_column;
    public TableColumn<Fournisseur,String> nis_column;
    public TableColumn<Fournisseur,String> tel_column;
    public TableColumn<Fournisseur,String> fax_column;
    public TableColumn<Fournisseur,String> rib_column;
    public TableColumn<Fournisseur,String> address_column;
    public TableColumn<Fournisseur,String> email_column;
    public TableColumn rc_column;
    public TextField searchField;
    public Button bk_Dashboard_from_fournisseur;
    public Button updateButton;
    public Button addButton;
    public Button deleteButton;
    private int user_id = -1;
    private String user_role = null;

    ObservableList<Fournisseur> fournisseurObservableList;
    private FournisseurDbHelper fournisseurDbHelper = new FournisseurDbHelper();
    private UserDbHelper userDbhelper= new UserDbHelper();
    private FilteredList<Fournisseur> filteredFournisseurList;
    private Fournisseur selectedFournisseur;

    public FournisseurController() throws SQLException {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("Initializing FournisseurController");
        setUser();
        loadData();
        initializeColumns();
        setupSearchFilter();
        setupTableSelectionListener();

    }

    private void setUser() {
        logger.info("FournisseurController.setUser");
        user_id = SessionManager.getActiveUserId();
        if (user_id != -1) {
            user_role = userDbhelper.getUserRoleById(user_id);

            if (user_role != null) {
                customizeFournisseurForRole(user_role);
            }else {
                logger.info("user_role is null");

            }
        }
    }

    private void customizeFournisseurForRole(String userRole) {
        logger.info("FournisseurController.customizeFournisseurForRole"+userRole);
        switch (userRole) {
            case "Admin":
                // Admin sees everything
                break;
            case "Editor":
              updateButton.setDisable(true);
              deleteButton.setDisable(true);
                break;
            case "User":
                // User sees limited options
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
                addButton.setDisable(true);
                break;
            default:

                break;
        }
    }

    private void initializeColumns() {
        // Set cell value factories for the fournisseur table
        logger.info("FournisseurController.initializeColumns");

        fournisseurTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_LAST_COLUMN);

        // Set cell value factories to match the attributes in the Fournisseur class
        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        name_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        rc_column.setCellValueFactory(new PropertyValueFactory<>("rc"));
        nif_column.setCellValueFactory(new PropertyValueFactory<>("nif"));
        ai_column.setCellValueFactory(new PropertyValueFactory<>("ai"));
        nis_column.setCellValueFactory(new PropertyValueFactory<>("nis"));
        tel_column.setCellValueFactory(new PropertyValueFactory<>("tel"));
        fax_column.setCellValueFactory(new PropertyValueFactory<>("fax"));
        address_column.setCellValueFactory(new PropertyValueFactory<>("address"));
        email_column.setCellValueFactory(new PropertyValueFactory<>("email"));
        rib_column.setCellValueFactory(new PropertyValueFactory<>("rib"));
    }


    private void loadData() {
        logger.info("FournisseurController.loadData");
        fournisseurObservableList=FXCollections.observableArrayList(fournisseurDbHelper.getFournisseurs());
        filteredFournisseurList = new FilteredList<>(fournisseurObservableList, p -> true);
        fournisseurTableView.setItems(filteredFournisseurList);
    }

    private void setupSearchFilter() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredFournisseurList.setPredicate(fournisseur -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return fournisseur.getName().toLowerCase().contains(lowerCaseFilter)
                        || fournisseur.getAddress().toLowerCase().contains(lowerCaseFilter)
                        || fournisseur.getAi().toLowerCase().contains(lowerCaseFilter)
                        || fournisseur.getRc().toLowerCase().contains(lowerCaseFilter)
                        || fournisseur.getNis().toLowerCase().contains(lowerCaseFilter)
                        || fournisseur.getTel().toLowerCase().contains(lowerCaseFilter)
                        || fournisseur.getEmail().toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(fournisseur.getId()).contains(lowerCaseFilter);
            });
        });
    }
    private void setupTableSelectionListener() {
        bk_Dashboard_from_fournisseur.setOnAction(event -> {
            GeneralUtil.goBackStockDashboard(event);
        });
        fournisseurTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedFournisseur = newValue);
    }
    public void addFournisseur(ActionEvent event) {
            logger.info("FournisseurController.addFournisseur");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/fournisseur/add_fournisseur_form_view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("إضافة مورد");
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));
            AddController controller = loader.getController();
            controller.setFournisseurController(this);
            stage.showAndWait();
        } catch (IOException e) {
            logger.error(e);
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "تعذر فتح نموذج إضافة المورد.");

        }
    }

    @FXML
    public void updateFournisseur(ActionEvent event) {
        logger.info("FournisseurController.updateFournisseur");
        // Get the selected Fournisseur from the table
        Fournisseur selectedFournisseur = fournisseurTableView.getSelectionModel().getSelectedItem();

        if (selectedFournisseur != null) {
            try {
                // Load the update form FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/fournisseur/update_fournisseur_view.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.setTitle("تحديث مورد");
                // Set the window icon
                stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));
                // Get the controller and set the supplier data
                UpdateController controller = loader.getController();
                controller.setFournisseurData(selectedFournisseur); // Ensure you have this method in UpdateController
                controller.setFournisseurController(this); // Assuming you want to pass the current controller
                // Show the update form
                stage.show();
            } catch (IOException e) {
               logger.error(e);
            }
        } else {
            // Show an alert if no supplier is selected
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "لا يوجد اختيار", "يرجى اختيار مورد للتحديث.");
        }
    }

    @FXML
    public void deleteFournisseur(ActionEvent event) {
        logger.info("FournisseurController.deleteFournisseur");
        Fournisseur selectedFournisseur = fournisseurTableView.getSelectionModel().getSelectedItem(); // Assuming you have a TableView for fournisseurs

        if (selectedFournisseur != null) {
            boolean success = fournisseurDbHelper.deleteFournisseur(selectedFournisseur.getId()); // Call your database helper to delete the fournisseur

            if (success) {
                logger.info("FournisseurController.deleteFournisseur selectedFournisseur successfully deleted");
                fournisseurObservableList.remove(selectedFournisseur); // Assuming you have an ObservableList for fournisseurs
                GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "تم حذف المورد", "تم حذف المورد بنجاح.");
            } else {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "فشل حذف المورد", "فشل في حذف المورد.");
            }
        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "لا يوجد اختيار", "يرجى اختيار مورد للحذف.");
        }
    }



    public void refreshTableData() {
        fournisseurObservableList.setAll(fournisseurDbHelper.getFournisseurs());
        fournisseurTableView.refresh();
    }
}
