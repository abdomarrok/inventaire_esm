package com.marrok.inventaire_esm.controller.bon_entree;

import com.marrok.inventaire_esm.model.BonEntree;
import com.marrok.inventaire_esm.model.Entree;
import com.marrok.inventaire_esm.model.Fournisseur;
import com.marrok.inventaire_esm.util.database.*;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddBonEntreeController {
    Logger logger = LogManager.getLogger(AddBonEntreeController.class);

    public TextField document_num;
    public Button clearButton;

    public ChoiceBox<Fournisseur> fournisseurChoiceBox;

    public DatePicker datePicker;

    public TableView<Entree> entreeTable;

    public TableColumn<Entree, String> articleColumn;

    public TableColumn<Entree, Integer> quantityColumn;

    public TableColumn<Entree, Double> priceColumn;

    public TableColumn<Entree, Double> totalColumn;

    public Button addItemButton;

    public Button removeItemButton;

    public Button cancelButton;

    public Button saveButton;
    public Button printButton;

    private ObservableList<Entree> entreesList = FXCollections.observableArrayList();
    private ArticleDbHelper articleDbhelper = new ArticleDbHelper();
    private FournisseurDbHelper fournisseurDbHelper = new FournisseurDbHelper();
    private BonEntreeDbHelper bonEntreeDbHelper = new BonEntreeDbHelper();
    Map<String, Object> parameters = new HashMap<>();
    private int current_be_id = -1;

    public AddBonEntreeController() throws SQLException {

    }

    // Initialize the controller
    @FXML
    public void initialize() {
        logger.info("Initializing AddBonEntreeController");
        printButton.setDisable(true);
        setupTableColumns();
        populateFournisseurChoiceBox();
        entreeTable.setItems(entreesList);
    }

    // Set up the table columns
    private void setupTableColumns() {
        logger.info("Setting up AddBonEntreeController columns");
        articleColumn.setCellValueFactory(cellData -> {
            int articleId = cellData.getValue().getIdArticle();
                String articleName = articleDbhelper.getArticleById(articleId).getName();
                if (articleName != null && !articleName.isEmpty()) {
                    return new SimpleStringProperty(articleName);
                }
                return new SimpleStringProperty("Unknown Article");
        });
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice")); //to get this from the user when add new entree
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalprix_ht")); //to be calculated as (P U)* quantity
    }

    // Populate the ChoiceBox with fournisseur data from the database
    private void populateFournisseurChoiceBox() {
        logger.info("Setting up AddBonEntreeController fournisseurChoiceBox");
        // Assuming you have a method to fetch fournisseur data from the database
        List<Fournisseur> fournisseurs = fournisseurDbHelper.getFournisseurs();

        fournisseurChoiceBox.setItems(FXCollections.observableArrayList(fournisseurs));
    }

    // Add a new article (item) to the Bon Entree
    @FXML
    public void addItem(ActionEvent event) {
        logger.info("Adding item");
        try {
            // Load the SelectArticleDialog.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/bon_entree/add_entree.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("إختر عنصر");

            // Show the dialog and wait for it to close
            stage.showAndWait();

            // Get the controller of the popup dialog
            AddEntreeController controller = loader.getController();
            Entree selectedEntree = controller.getSelectedEntree();


            if (selectedEntree != null) {
                entreesList.add(selectedEntree);
            }

        } catch (IOException e) {
            logger.error(e);
        }
    }

    // Remove the selected article (item) from the Bon Entree
    @FXML
    public void removeItem(ActionEvent event) {
        logger.info("Removing item");
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
        logger.info("Saving BonEntree");
        Fournisseur selectedFournisseur = fournisseurChoiceBox.getSelectionModel().getSelectedItem();
        LocalDate selectedDate = datePicker.getValue();
        String documentNum=document_num.getText();

        if (selectedFournisseur == null || selectedDate == null || entreesList.isEmpty() || documentNum == null) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Missing Information", "Please fill out all fields before saving.");
            return;
        }

        // Logic to save the Bon Entree (send data to the database)
        boolean success = saveBonEntreeToDatabase(selectedFournisseur, selectedDate,documentNum, entreesList);

        if (success) {
            logger.info("BonEntree saved to database");
            GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Bon Entree saved successfully.");
            addItemButton.setDisable(true);
            removeItemButton.setDisable(true);
            saveButton.setDisable(true);
            printButton.setDisable(false);


        } else {
            logger.info("BonEntree save failed");
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Failure", "Failed to save Bon Entree.");
        }
    }


    public void clearBonEntree(ActionEvent event) {
        logger.info("Clearing BonEntree");
        fournisseurChoiceBox.getSelectionModel().clearSelection();
        document_num.setText("");
        datePicker.setValue(null);
        entreesList.clear();
        addItemButton.setDisable(false);
        removeItemButton.setDisable(false);
        saveButton.setDisable(false);
        printButton.setDisable(true);

    }


    private boolean saveBonEntreeToDatabase(Fournisseur fournisseur, LocalDate date, String documentNum, ObservableList<Entree> entrees) {
        logger.info("Saving BonEntree");
        // Create a BonEntree object
        int tva = 19; // Set this according to your application's logic (or pass it as a parameter)
        BonEntree bonEntree = new BonEntree(0, fournisseur.getId(), java.sql.Date.valueOf(date), tva,documentNum);

        // Save BonEntree to the database
        int bonEntreeId = bonEntreeDbHelper.createBonEntree(bonEntree); // Adjust your DatabaseHelper method accordingly

        if (bonEntreeId <= 0) {
            return false; // Failed to create Bon Entree
        }
        current_be_id = bonEntreeId;

        // Save each Entree associated with the Bon Entree
        for (Entree entree : entrees) {
            entree.setIdBe(bonEntreeId); // Associate the Entree with the newly created Bon Entree
            boolean success = bonEntreeDbHelper.saveEntree(entree); // Assuming you have a saveEntree method in DatabaseHelper
            if (!success) {
                logger.error("BonEntree save failed");
                return false; // Failed to save at least one Entree
            }
        }
        logger.info("BonEntree save successful");
        return true; // Successfully saved the Bon Entree and all Entree records
    }

    public void printBonEntree(ActionEvent event) {
        logger.info("Printing BonEntree");
        Connection connection = null;
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            InputStream reportStream = getClass().getResourceAsStream("/com/marrok/inventaire_esm/reports/Bon_Entree_Report.jrxml");
            if (reportStream == null) {
                logger.error("Report file not found.");
//                throw new FileNotFoundException("Report file not found.");
            }

            if (current_be_id != -1) {

                parameters.put("bon_entree_id", current_be_id);
                parameters.put("logo", getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png"));
                logger.info("Parameters: be= " + parameters);

            } else {
                logger.error("Error with current bon entree ID.");
                GeneralUtil.showAlert(Alert.AlertType.WARNING, "Error", "Error with current bon entree ID.");
                return; // Exit if the ID is invalid
            }

            // Compile the report
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Fill the report

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            // View the report
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setTitle("وصل استلام");
            viewer.setVisible(true);

        } catch (SQLException sqlEx) {
            logger.error(sqlEx);
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "SQL Error", "Error while accessing the database: " + sqlEx.getMessage());
        } catch (Exception ex) {
           logger.error(ex);
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Error generating report: " + ex.getMessage());
        }

    }


}
