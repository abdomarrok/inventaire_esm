package com.marrok.inventaire_esm.controller.bon_retour;

import com.dlsc.gemsfx.FilterView;
import com.marrok.inventaire_esm.controller.bon_sortie.AddSortieController;
import com.marrok.inventaire_esm.model.*;
import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.database.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class AddBonRetourController implements Initializable {
    public TextArea returnReason;
    Logger logger = Logger.getLogger(AddBonRetourController.class);
    public DatePicker datePicker;


    private ArticleDbHelper articleDbhelper = new ArticleDbHelper();
    private ServiceDbHelper serviceDbHelper = new ServiceDbHelper();
    private EmployerDbHelper employerDbHelper=new EmployerDbHelper();
    private BonRetourDbHelper bonRetourDbHelper = new BonRetourDbHelper();
    @FXML
    private ChoiceBox<String> serviceField;
    public FilterView<Employer> filterView2;
    public TableView<Employer> tbData2;
    public TableColumn<Employer, Integer> id_E;
    public TableColumn<Employer, String> fullname;

    private ObservableList<Employer> emploerlist;
    public TableView<Retour> retourTable;

    public TableColumn<Retour,String> articleColumn;
    public TableColumn<Retour,Integer> quantityColumn;

    public Button addItemButton;
    public Button removeItemButton;
    public Button saveButton;
    public Button printButton;
    private ObservableList<Retour> retourObservableList = FXCollections.observableArrayList();
    Map<String, Object> parameters = new HashMap<>();
    private int current_br_id = -1;

    public AddBonRetourController() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("Initializing AddBonRetourController");
        printButton.setDisable(true);
        setupTableColumns();
        initTable();
        try {
            loadFilter();
        } catch (SQLException e) {
            logger.error(e);
        }
        loadTableData();
        retourTable.setItems(retourObservableList);
        load_srv_ch_bx_data();

    }

    private void initTable() {
        logger.info("initTable employers");
        id_E.setCellValueFactory(new PropertyValueFactory<>("id"));
        fullname.setCellValueFactory(cellData->{
            String fullname= cellData.getValue().getFirstName()+" "+cellData.getValue().getLastName();
            return new SimpleStringProperty(fullname);
        });

    }

    private void loadTableData() {
        logger.info("loadTableData ");
        List<Employer> employers = employerDbHelper.getEmployers();
        emploerlist =  FXCollections.observableArrayList(employers);
        filterView2.getItems().setAll(emploerlist);
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
                e.printStackTrace();
                return new SimpleStringProperty("Unknown Article");
            }
            return null;
        });
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }
    private void loadFilter() throws SQLException {
        logger.info("loadFilter");
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


    public void removeItem(ActionEvent event) {
        logger.info("removeItem called");
        Retour selectedRetour = retourTable.getSelectionModel().getSelectedItem();
        if (selectedRetour != null) {
            retourObservableList.remove(selectedRetour);
        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an article to remove.");
        }
    }

    public void addItem(ActionEvent event) {
        logger.info("addItem called");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/bon_retour/add_retour.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("إختر عنصر");
            stage.showAndWait();
            AddRetourController controller = loader.getController();
            Retour selectedRetour = controller.getSelectedRetour();
            if(selectedRetour!=null){
                retourObservableList.add(selectedRetour);
            }

        } catch (IOException e) {
            logger.error("IO Error: " + e);
        }
    }

    public void clearBonRetour(ActionEvent event) {
        logger.info("clearBonRetour");
        serviceField.getSelectionModel().clearSelection();
        datePicker.setValue(null);
        retourObservableList.clear();
        addItemButton.setDisable(false);
        removeItemButton.setDisable(false);
        saveButton.setDisable(false);
        printButton.setDisable(true);
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

            if (current_br_id != -1) {

                parameters.put("bon_retour_id", current_br_id);
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


    public void saveBonRetour(ActionEvent event) {
        logger.info("saveBonRetour called");
        Employer selectedEmployer = tbData2.getSelectionModel().getSelectedItem();
        String selectedServiceName = serviceField.getSelectionModel().getSelectedItem();
        Service selectedService = serviceDbHelper.getServiceByName(selectedServiceName);  // Assuming you have a method in DatabaseHelper to get Service by name
        LocalDate selectedDate = datePicker.getValue();
        String returnReason_str =returnReason.getText();
        // Validate inputs
        if (selectedEmployer == null || selectedDate == null || selectedService == null || retourObservableList.isEmpty()) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Missing Information", "Please fill out all fields before saving.");
            return;
        }
        boolean success = saveBonRetourToDatabase(selectedEmployer, selectedService,selectedDate,returnReason_str);
        // Check the result of saving operation and update the UI accordingly
        if (success) {
            logger.info("BonRetour saved to database");
            GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Bon Retour saved successfully.");
            addItemButton.setDisable(true);
            removeItemButton.setDisable(true);
            saveButton.setDisable(true);  // Disable save button after successful save
            printButton.setDisable(false);  // Enable print button after save
        } else {
            logger.info("BonRetour save failed");
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Failure", "Failed to save Bon Retour");
        }
    }

    private boolean saveBonRetourToDatabase(Employer selectedEmployer, Service selectedService, LocalDate selectedDate, String returnReason) {

        logger.info("saveBonRetourToDatabase called");
        BonRetour bonRetour = new BonRetour(0,selectedEmployer.getId(),selectedService.getId(),java.sql.Date.valueOf(selectedDate),returnReason);
        int bonRetourId=bonRetourDbHelper.createBonRetour(bonRetour);
        if(bonRetourId<=0){
            return false;
        }
        current_br_id=bonRetourId;
        for(Retour retour : retourObservableList){
            retour.setIdBr(bonRetourId);
            boolean seccess = bonRetourDbHelper.saveRetour(retour);
            if(!seccess){
                return false;
            }
        }
        return true;
    }

    private void load_srv_ch_bx_data() {
        logger.info("load_srv_ch_bx_data");
        List<Service> services = serviceDbHelper.getServices();
        List<String> service_names = new ArrayList<>();
        for (Service service : services) {
            service_names.add(service.getName());
        }
        serviceField.getItems().addAll(service_names);
        serviceField.getSelectionModel().select(0);
    }

}
