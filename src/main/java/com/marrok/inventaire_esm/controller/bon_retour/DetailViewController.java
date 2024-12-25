package com.marrok.inventaire_esm.controller.bon_retour;

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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailViewController {
    private ArticleDbHelper articleDbhelper = new ArticleDbHelper();
    private ServiceDbHelper serviceDbHelper= new ServiceDbHelper();
    private EmployerDbHelper employerDbHelper=new EmployerDbHelper();
    private BonRetourDbHelper bonRetourDbHelper = new BonRetourDbHelper();
    private BonRetour bonRetour;
    private Employer currentEmployer;
    private Service currentService;
    private List<Retour> retourList;
    public TableView<Retour> retourTableView;
    public TableColumn<Retour, String> articleColumn;

    public TableColumn<Retour, Integer> quantityColumn;
    Map<String, Object> parameters = new HashMap<>();

    @FXML
    private Label idLabel, employerLabel, serviceLabel, dateLabel, lastEditedLabel;


    Logger logger = LogManager.getLogger(DetailViewController.class);

    public DetailViewController() throws SQLException {
    }

    public void setBonRetour(int id) {
        logger.info("setBonRetour");
    this.bonRetour = bonRetourDbHelper.getBonRetourById(id);
    int bonRetour_id= bonRetour.getId();
    currentEmployer = employerDbHelper.getEmployerById(bonRetour.getIdEmployeur());
    currentService = serviceDbHelper.getServiceById(bonRetour.getIdService());
        populateDetails();
    }

    private void populateDetails() {
        logger.info("populateDetails");
        idLabel.setText(String.valueOf(bonRetour.getId()));
        employerLabel.setText(currentEmployer != null ? currentEmployer.getFirstName()+" "+currentEmployer.getLastName() : "N/A");
        serviceLabel.setText(currentService != null ? currentService.getName() : "N/A");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateLabel.setText(dateFormat.format(bonRetour.getDate()));
        retourList=bonRetourDbHelper.getRetoursByIdBonRetour(bonRetour.getId());
        retourTableView.getItems().clear();
        retourTableView.getItems().addAll(retourList);
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

    public void printBonRetour(ActionEvent event) {
        logger.info("print bon retour");
        Connection connection = null;
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            InputStream reportStream = getClass().getResourceAsStream("/com/marrok/inventaire_esm/reports/Bon_Retour_Report.jrxml");
            if (reportStream == null) {
                logger.error("Report file not found.");
//                throw new FileNotFoundException("Report file not found.");
            }

            if (bonRetour.getId() != -1) {

                parameters.put("bon_retour_id", bonRetour.getId());
                parameters.put("logo", getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png"));
                logger.info("Parameters: br= " + parameters);

            } else {
                logger.error("Error with current bon entree ID.");
                GeneralUtil.showAlert(Alert.AlertType.WARNING, "Error", "Error with current bon retour ID.");
                return; // Exit if the ID is invalid
            }

            // Compile the report
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Fill the report

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            // View the report
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setTitle("وصل إرجاع");
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
