package com.marrok.inventaire_esm.controller.bon_sortie;

import com.marrok.inventaire_esm.model.*;
import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.database.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private ArticleDbHelper articleDbhelper = new ArticleDbHelper();
    private ServiceDbHelper serviceDbHelper= new ServiceDbHelper();
    private EmployerDbHelper employerDbHelper=new EmployerDbHelper();
   private BonSortieDbHelper bonSortieDbHelper = new BonSortieDbHelper();
    private BonSortie bonSortie;
    private Employer currentEmployer;
    private Service currentService;
    private List<Sortie> sortieList;
    public TableView<Sortie> sortieTableView; // TableView to display entree list
    public TableColumn<Sortie, String> articleColumn;

    public TableColumn<Sortie, Integer> quantityColumn;
    Map<String, Object> parameters = new HashMap<>();

    @FXML
    private Label idLabel, employerLabel, serviceLabel, dateLabel, lastEditedLabel;



    public DetailViewController() throws SQLException {
    }

    public void setBonSortie(BonSortie selectedBonSortie) {
        logger.info("setBonSortie");
        this.bonSortie = selectedBonSortie;
        currentEmployer = employerDbHelper.getEmployerById(bonSortie.getIdEmployeur());
        currentService = serviceDbHelper.getServiceById(bonSortie.getIdService());
        populateDetails();
    }

    private void populateDetails() {
        logger.info("populateDetails");
        // Populate the labels with the BonSortie details
        idLabel.setText(String.valueOf(bonSortie.getId()));
        employerLabel.setText(currentEmployer != null ? currentEmployer.getFirstName()+" "+currentEmployer.getLastName() : "N/A");
        serviceLabel.setText(currentService != null ? currentService.getName() : "N/A");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateLabel.setText(dateFormat.format(bonSortie.getDate()));
        sortieList =bonSortieDbHelper.getSortiesByIdBonSortieId(bonSortie.getId());
        sortieTableView.getItems().clear();
        sortieTableView.getItems().addAll(sortieList);
        setupTableColumns();


    }

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
                logger.error(e);
                return new SimpleStringProperty("Unknown Article");
            }
            return null;
        });
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    public void printBonSortie(ActionEvent event) {
        logger.info("printBonSortie");
        Connection connection = null;
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            InputStream reportStream = getClass().getResourceAsStream("/com/marrok/inventaire_esm/reports/Bon_Sortie_Report.jrxml");
            if (reportStream == null) {
                logger.error("Report file not found.");
            }

            if (bonSortie.getId() != -1) {
                parameters.put("bon_sortie_id", bonSortie.getId());
                parameters.put("logo", getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png"));
                logger.info("Parameters: bs= " + parameters);
            } else {
                GeneralUtil.showAlert(Alert.AlertType.WARNING, "Error", "Error with current bon entree ID.");
                return; // Exit if the ID is invalid
            }
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setTitle("وصل إخراج");
            viewer.setVisible(true);

        } catch (SQLException sqlEx) {
           logger.error("SQL Error: " + sqlEx);
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "SQL Error", "Error while accessing the database: " + sqlEx.getMessage());
        } catch (Exception ex) {
           logger.error("Error generating report: " + ex);
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Error generating report: " + ex.getMessage());
        }

    }

}
