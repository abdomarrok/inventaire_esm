package com.marrok.inventaire_esm.controller.bon_entree;

import com.marrok.inventaire_esm.model.BonEntree;
import com.marrok.inventaire_esm.model.Entree;
import com.marrok.inventaire_esm.model.Fournisseur;
import com.marrok.inventaire_esm.util.database.*;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailViewController {
    Logger logger = Logger.getLogger(DetailViewController.class);
    @FXML private Label idLabel;
    @FXML private Label fournisseurIdLabel;
    @FXML private Label addressLabel; // New label for address
    @FXML private Label dateLabel;
    @FXML private Label documentNumLabel;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button backButton;
    @FXML private TableView<Entree> entreeTableView; // TableView to display entree list
    public TableColumn<Entree, String> articleColumn;

    public TableColumn<Entree, Integer> quantityColumn;

    public TableColumn<Entree, Double> priceColumn;

    public TableColumn<Entree, Double> totalColumn;

    private BonEntree bonEntree;
    private ArticleDbHelper articleDbhelper = new ArticleDbHelper();
    private FournisseurDbHelper fournisseurDbHelper = new FournisseurDbHelper();
    private BonEntreeDbHelper bonEntreeDbHelper = new BonEntreeDbHelper();
    private Fournisseur currentFournisseur;
    private List<Entree> entreeList;
    Map<String, Object> parameters = new HashMap<>();
    public DetailViewController() throws SQLException {
    }

    public void setBonEntree(BonEntree bonEntree) {
        logger.info( "set this BonEntree"+bonEntree);
        this.bonEntree = bonEntree;
        currentFournisseur = fournisseurDbHelper.getFournisseurById(bonEntree.getIdFournisseur());
        populateDetails();
    }

    private void populateDetails() {
        logger.info("populate details called");
        if (bonEntree != null) {
            idLabel.setText(String.valueOf(bonEntree.getId()));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateLabel.setText(dateFormat.format(bonEntree.getDate())); // Adjust format as needed
            documentNumLabel.setText(bonEntree.getDocumentNum());
            entreeList = bonEntreeDbHelper.getEntreesById_BonEntreeId(bonEntree.getId());

            // Populate the TableView
            entreeTableView.getItems().clear(); // Clear existing items
            entreeTableView.getItems().addAll(entreeList); // Add new items
            setupTableColumns();
        }

        if (currentFournisseur != null) {
            logger.info("current fournisseur is " + currentFournisseur);
            fournisseurIdLabel.setText(currentFournisseur.getName());
            addressLabel.setText(currentFournisseur.getAddress()); // Display address
        }
    }

    // Set up the table columns
    private void setupTableColumns() {
        logger.info("setupTableColumns");
        articleColumn.setCellValueFactory(cellData -> {
            int articleId = cellData.getValue().getIdArticle();
            try {
                String articleName = articleDbhelper.getArticleById(articleId).getName();

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


    @FXML
    private void handleEdit(ActionEvent event) {
        logger.info("Edit button clicked.");
        // Open an edit form or handle it here
    }



    @FXML
    private void handleBack(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    public void printBonEntree(ActionEvent event) {
        logger.info("printBonEntree");
        if (bonEntree != null) {
            Connection connection = null;
            try {
                connection = DatabaseConnection.getInstance().getConnection();
                InputStream reportStream = getClass().getResourceAsStream("/com/marrok/inventaire_esm/reports/Bon_Entree_Report.jrxml");
                if (reportStream == null) {
                    logger.error("Report file not found.");
                }

                if (bonEntree.getId() != -1) {
                    parameters.put("bon_entree_id", bonEntree.getId());
                    parameters.put("logo", getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png"));
                    logger.info("Parameters: " + parameters);

                } else {
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
               logger.error("SQL Error: " + sqlEx.getMessage());
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "SQL Error", "Error while accessing the database: " + sqlEx.getMessage());
            } catch (Exception ex) {
                logger.error("Error generating report: " + ex.getMessage());
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Error generating report: " + ex.getMessage());
            }

        }
    }
}
