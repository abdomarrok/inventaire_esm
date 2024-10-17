package com.marrok.inventaire_esm.controller.bon_entree;

import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.model.BonEntree;
import com.marrok.inventaire_esm.model.Entree;
import com.marrok.inventaire_esm.model.Fournisseur;
import com.marrok.inventaire_esm.util.DatabaseHelper;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BonEntreeController {
    DatabaseHelper dbhelper= new DatabaseHelper();

    @FXML
    private ChoiceBox<Fournisseur> fournisseurChoiceBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableView<Entree> entreeTable;

    @FXML
    private TableColumn<Entree, String> articleColumn;

    @FXML
    private TableColumn<Entree, Integer> quantityColumn;

    @FXML
    private TableColumn<Entree, Double> priceColumn;

    @FXML
    private TableColumn<Entree, Double> totalColumn;

    @FXML
    private Button addItemButton;

    @FXML
    private Button removeItemButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;

    private ObservableList<Entree> entreesList = FXCollections.observableArrayList();

    public BonEntreeController() throws SQLException {
    }

    // Initialize the controller
    @FXML
    public void initialize() {
        setupTableColumns();
        populateFournisseurChoiceBox();
        entreeTable.setItems(entreesList);
    }

    // Set up the table columns
    private void setupTableColumns() {
        articleColumn.setCellValueFactory(cellData -> {
            int articleId = cellData.getValue().getIdArticle();
            try {
                DatabaseHelper dbHelper = new DatabaseHelper();
                String articleName = dbHelper.getArticleById(articleId).getName();

                if (articleName != null && !articleName.isEmpty()) {
                    return new SimpleStringProperty(articleName);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new SimpleStringProperty("Unknown Article");
            }
            return null;
        });
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice")); //to get this from the user when add new entree
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalprix_ht")); //to be calculated as (P U)* quantity
    }

    // Populate the ChoiceBox with fournisseur data from the database
    private void populateFournisseurChoiceBox() {
        // Assuming you have a method to fetch fournisseur data from the database
        List<Fournisseur> fournisseurs = dbhelper.getFournisseurs();

         fournisseurChoiceBox.setItems(FXCollections.observableArrayList(fournisseurs));
    }

    // Add a new article (item) to the Bon Entree
    @FXML
    public void addItem(ActionEvent event) {
        try {
            // Load the SelectArticleDialog.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/bon_entree/add_entree.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Select Article");

            // Show the dialog and wait for it to close
            stage.showAndWait();

            // Get the controller of the popup dialog
            AddEntreeController controller = loader.getController();
            Entree selectedEntree = controller.getSelectedEntree();


            if (selectedEntree != null) {
                entreesList.add(selectedEntree);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Remove the selected article (item) from the Bon Entree
    @FXML
    public void removeItem(ActionEvent event) {
        Entree selectedEntree = entreeTable.getSelectionModel().getSelectedItem();
        if (selectedEntree != null) {
            entreesList.remove(selectedEntree);
        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an article to remove.");
        }
    }

    // Save the Bon Entree
    @FXML
    public void saveBonEntree(ActionEvent event) {
        Fournisseur selectedFournisseur = fournisseurChoiceBox.getSelectionModel().getSelectedItem();
        LocalDate selectedDate = datePicker.getValue();

        if (selectedFournisseur == null || selectedDate == null || entreesList.isEmpty()) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Missing Information", "Please fill out all fields before saving.");
            return;
        }

        // Logic to save the Bon Entree (send data to the database)
        boolean success = saveBonEntreeToDatabase(selectedFournisseur, selectedDate, entreesList);

        if (success) {
            GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Bon Entree saved successfully.");
            cancelBonEntree(event);
        } else {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Failure", "Failed to save Bon Entree.");
        }
    }

    // Cancel the Bon Entree process
    @FXML
    public void cancelBonEntree(ActionEvent event) {
        // Clear fields and reset the form
        fournisseurChoiceBox.getSelectionModel().clearSelection();
        datePicker.setValue(null);
        entreesList.clear();
    }


    private boolean saveBonEntreeToDatabase(Fournisseur fournisseur, LocalDate date, ObservableList<Entree> entrees) {
        // Create a BonEntree object
        int tva = 19; // Set this according to your application's logic (or pass it as a parameter)
        BonEntree bonEntree = new BonEntree(0, fournisseur.getId(), java.sql.Date.valueOf(date), tva);

        // Save BonEntree to the database
        int bonEntreeId = dbhelper.createBonEntree(bonEntree); // Adjust your DatabaseHelper method accordingly

        if (bonEntreeId <= 0) {
            return false; // Failed to create Bon Entree
        }

        // Save each Entree associated with the Bon Entree
        for (Entree entree : entrees) {
            entree.setIdBe(bonEntreeId); // Associate the Entree with the newly created Bon Entree
            boolean success = dbhelper.saveEntree(entree); // Assuming you have a saveEntree method in DatabaseHelper
            if (!success) {
                return false; // Failed to save at least one Entree
            }
        }

        return true; // Successfully saved the Bon Entree and all Entree records
    }

}
