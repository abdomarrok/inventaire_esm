package com.marrok.inventaire_esm.controller.bon_sortie;

import com.dlsc.gemsfx.FilterView;
import com.marrok.inventaire_esm.controller.article.EtatStockController;
import com.marrok.inventaire_esm.model.*;
import com.marrok.inventaire_esm.util.database.*;
import com.marrok.inventaire_esm.util.GeneralUtil;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class AddBonSortieController implements  Initializable {
    public DatePicker datePicker;
    public Button clearButton;

    private ArticleDbHelper articleDbhelper = new ArticleDbHelper();
    private ServiceDbHelper serviceDbHelper = new ServiceDbHelper();
    private EmployerDbHelper employerDbHelper=new EmployerDbHelper();
    private BonSortieDbHelper bonSortieDbHelper=new BonSortieDbHelper();
    @FXML
    private ChoiceBox<String> serviceField;
    public FilterView<Employer> filterView2;
    public TableView<Employer> tbData2;
    public TableColumn<Employer, Integer> id_E;
    public TableColumn<Employer, String> fullname;

    private ObservableList<Employer> emploerlist;
    public TableView<Sortie> sortieTable;
    public TableColumn<Sortie, String> articleColumn;
    public TableColumn<Sortie, Integer> quantityColumn;
    public Button addItemButton;
    public Button removeItemButton;
    public Button saveButton;
    public Button printButton;

    private ObservableList<Sortie> sortiesList = FXCollections.observableArrayList();
    Map<String, Object> parameters = new HashMap<>();
    private int current_bs_id = -1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        printButton.setDisable(true);
        setupTableColumns();
        initTable();
        try {
            loadFilter();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        loadTableData();
        sortieTable.setItems(sortiesList);
        load_srv_ch_bx_data();
    }

    private void setupTableColumns() {
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

    private void loadTableData() {
        List<Employer> employers = employerDbHelper.getEmployers();
        emploerlist =  FXCollections.observableArrayList(employers);
        filterView2.getItems().setAll(emploerlist);
    }

    private void initTable() {

        /**    EMPLOYER   */
        id_E.setCellValueFactory(new PropertyValueFactory<>("id"));
        fullname.setCellValueFactory(cellData->{
           String fullname= cellData.getValue().getFirstName()+" "+cellData.getValue().getLastName();
           return new SimpleStringProperty(fullname);
        });

    }


    private void load_srv_ch_bx_data() {
        List<Service> services = serviceDbHelper.getServices();
        List<String> service_names = new ArrayList<>();
        for (Service service : services) {
            service_names.add(service.getName());
        }
        serviceField.getItems().addAll(service_names);
        serviceField.getSelectionModel().select(0);
    }
    public AddBonSortieController() throws SQLException {
    }


    public void clearBonSortie(ActionEvent event) {
        serviceField.getSelectionModel().clearSelection();
        datePicker.setValue(null);
        sortiesList.clear();
        addItemButton.setDisable(false);
        removeItemButton.setDisable(false);
        saveButton.setDisable(false);
        printButton.setDisable(true);
    }

    private boolean saveBonSortieToDatabase(Employer employer,Service service, LocalDate date, ObservableList<Sortie> sorties) {
        // Create a BonSortie object
        BonSortie bonSortie = new BonSortie(0, employer.getId(),service.getId(), java.sql.Date.valueOf(date));
        // Save BonSortie to the database
        int bonSortieId = bonSortieDbHelper.createBonSortie(bonSortie);  // Adjust your DatabaseHelper method accordingly

        if (bonSortieId <= 0) {
            return false; // Failed to create Bon Sortie
        }
        current_bs_id=bonSortieId;
        // Save each Sortie associated with the Bon Sortie
        for (Sortie sortie : sorties) {
            sortie.setIdBs(bonSortieId);  // Associate the Sortie with the newly created Bon Sortie
            boolean success = bonSortieDbHelper.saveSortie(sortie);  // Assuming you have a saveSortie method in DatabaseHelper
            if (!success) {
                return false;  // Failed to save at least one Sortie
            }
        }

        return true;  // Successfully saved the Bon Sortie and all Sortie records
    }

    public void printBonSortie(ActionEvent event) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getInstance().getConnection();
            InputStream reportStream = getClass().getResourceAsStream("/com/marrok/inventaire_esm/reports/Bon_Sortie_Report.jrxml");
            if (reportStream == null) {
                throw new FileNotFoundException("Report file not found.");
            }



            if (current_bs_id != -1) {
                parameters.put("bon_sortie_id", current_bs_id);
                System.out.println("Parameters: bs= " + parameters);
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
            viewer.setTitle("وصل إخراج");
            viewer.setVisible(true);

        } catch (FileNotFoundException fnf) {
            System.out.println("Report file not found: " + fnf.getMessage());
            fnf.printStackTrace();
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Report file not found: " + fnf.getMessage());
        } catch (SQLException sqlEx) {
            System.out.println("SQL Error: " + sqlEx.getMessage());
            sqlEx.printStackTrace();
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "SQL Error", "Error while accessing the database: " + sqlEx.getMessage());
        } catch (Exception ex) {
            System.out.println("Error generating report: " + ex.getMessage());
            ex.printStackTrace();
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Error generating report: " + ex.getMessage());
        }


        GeneralUtil.loadScene("/com/marrok/inventaire_esm/view/bon_sortie/bon_sortie-view.fxml",event,true);
    }




    public void saveBonSortie(ActionEvent event) {
        // Get selected Employer, Service, and Date
        Employer selectedEmployer = tbData2.getSelectionModel().getSelectedItem();
        String selectedServiceName = serviceField.getSelectionModel().getSelectedItem();
        Service selectedService = serviceDbHelper.getServiceByName(selectedServiceName);  // Assuming you have a method in DatabaseHelper to get Service by name
        LocalDate selectedDate = datePicker.getValue();

        // Validate inputs
        if (selectedEmployer == null || selectedDate == null || selectedService == null || sortiesList.isEmpty()) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Missing Information", "Please fill out all fields before saving.");
            return;
        }

        // Save BonSortie to the database
        boolean success = saveBonSortieToDatabase(selectedEmployer, selectedService,selectedDate, sortiesList);

        // Check the result of saving operation and update the UI accordingly
        if (success) {

            GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Bon Sortie saved successfully.");
            addItemButton.setDisable(true);
            removeItemButton.setDisable(true);
            saveButton.setDisable(true);  // Disable save button after successful save
            printButton.setDisable(false);  // Enable print button after save
        } else {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Failure", "Failed to save Bon Sortie.");
        }
    }

    public void removeItem(ActionEvent event) {
        Sortie selectedSortie = sortieTable.getSelectionModel().getSelectedItem();
        if (selectedSortie != null) {
            sortiesList.remove(selectedSortie);
        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an article to remove.");
        }
    }

    public void addItem(ActionEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/bon_sortie/add_sortie.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("إختر عنصر");
            stage.showAndWait();
            AddSortieController controller = loader.getController();
            Sortie selectedSortie = controller.getSelectedSortie();
            if(selectedSortie!=null){
                sortiesList.add(selectedSortie);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
