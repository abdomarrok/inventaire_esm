package com.marrok.inventaire_esm.controller.fournisseur;

import com.marrok.inventaire_esm.model.Fournisseur;
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
import java.util.ResourceBundle;

public class FournisseurController implements Initializable {

    public TableView<Fournisseur> fournisseurTableView;
    public TableColumn<Fournisseur,Integer> id_column;
    public TableColumn<Fournisseur,String> name_column;
    public TableColumn<Fournisseur,String> nif_column;
    public TableColumn<Fournisseur,String> ai_column;
    public TableColumn<Fournisseur,String> nis_column;
    public TableColumn<Fournisseur,String> tel_column;
    public TableColumn<Fournisseur,String> fax_column;
    public TableColumn<Fournisseur,String> address_column;
    public TableColumn<Fournisseur,String> email_column;
    public TableColumn rc_column;
    public TextField searchField;
    public Button bk_Dashboard_from_fournisseur;

    ObservableList<Fournisseur> fournisseurObservableList;
    private DatabaseHelper dbhelper=new DatabaseHelper();
    private FilteredList<Fournisseur> filteredFournisseurList;
    private Fournisseur selectedFournisseur;

    public FournisseurController() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
        initializeColumns();
        setupSearchFilter();
        setupTableSelectionListener();

    }

    private void initializeColumns() {
        // Set cell value factories for the fournisseur table

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
    }


    private void loadData() {
        fournisseurObservableList=FXCollections.observableArrayList(dbhelper.getFournisseurs());
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
            GeneralUtil.goBackDashboard(event);
        });
        fournisseurTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedFournisseur = newValue);
    }
    public void addFournisseur(ActionEvent event) {

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
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "تعذر فتح نموذج إضافة المورد.");
            e.printStackTrace();
        }
    }

    @FXML
    public void updateFournisseur(ActionEvent event) {
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
                e.printStackTrace();
            }
        } else {
            // Show an alert if no supplier is selected
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "لا يوجد اختيار", "يرجى اختيار مورد للتحديث.");
        }
    }

    @FXML
    public void deleteFournisseur(ActionEvent event) {
        Fournisseur selectedFournisseur = fournisseurTableView.getSelectionModel().getSelectedItem(); // Assuming you have a TableView for fournisseurs

        if (selectedFournisseur != null) {
            boolean success = dbhelper.deleteFournisseur(selectedFournisseur.getId()); // Call your database helper to delete the fournisseur

            if (success) {
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
        fournisseurObservableList.setAll(dbhelper.getFournisseurs());
        fournisseurTableView.refresh();
    }
}
