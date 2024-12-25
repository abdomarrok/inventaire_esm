package com.marrok.inventaire_esm.controller.inventaire;

import com.dlsc.gemsfx.FilterView;
import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.model.Employer;
import com.marrok.inventaire_esm.model.Inventaire_Item;
import com.marrok.inventaire_esm.model.Localisation;
import com.marrok.inventaire_esm.util.database.*;
import com.marrok.inventaire_esm.util.SessionManager;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.marrok.inventaire_esm.util.GeneralUtil.showAlert;

public class UpdateController implements Initializable {
    Logger logger = LogManager.getLogger(UpdateController.class);

    public ChoiceBox<String> status_inventaire;
    public DatePicker calendarPicker1;
    @FXML
    private ChoiceBox<String> locationChoiceBox;

    Map<Integer,String> locations_and_floor = new HashMap<>();
    public TableView<Employer> tbData2;
    public TableColumn<Employer, Integer> id_E;
    public TableColumn<Employer, String> name_E;
//    public TableColumn<Employer, String> firstname_E;
//    public TableColumn<Employer, String> lastname_E;


    @FXML
    private Button cancelButton;

    public FilterView<Article> filterView;
    public FilterView<Employer> filterView2;
    public TableView<Article> tbData;
    public TableColumn<Article, Integer> id;
    public TableColumn<Article, String> article_name;

    private InventaireItemController parentController;
    private ObservableList<Article> articlelist;
    private ObservableList<Employer> emploerlist;
    private Article selectedArticle;
    private Inventaire_Item inventaireItem;
    @FXML
    private TextField employerInventaireCode;
    String[] inv_status = {"BON ETAT","MOYEN","MAUVAIS","VER ANNEXE-HARRACHE","EN PANNE"};

    ArticleDbHelper articleDbhelper = new ArticleDbHelper();
    LocDbhelper locDbhelper = new LocDbhelper();
    InventaireItemDbHelper inventaireItemDbHelper= new InventaireItemDbHelper();
    private EmployerDbHelper employerDbHelper=new EmployerDbHelper();

    public UpdateController() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("Initialize UpdateController");
        initTable();
        loadChoiceBoxData();
        try {
            loadFilter();
            loadTableData(); // Load data once

        } catch (SQLException e) {
          logger.error(e);
        }
        CSSFX.start();
    }

    public void setParentController(InventaireItemController parentController) {
        this.parentController = parentController;
    }

    private void loadFilter() throws SQLException {
        logger.info("loadFilter");
        filterView.getFilterGroups().clear();
        filterView2.getFilterGroups().clear();
        filterView.setTextFilterProvider(text-> article -> {
            if(text==null||text.isEmpty()){
                return true;
            }

            String lowerCase = text.toLowerCase();
            return article.getName().toLowerCase().contains(lowerCase)||
                    String.valueOf(article.getId()).toLowerCase().contains(lowerCase);

        });

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
        SortedList<Article> sortedList = new SortedList<>(filterView.getFilteredItems());
        SortedList<Employer> sortedList2 = new SortedList<>(filterView2.getFilteredItems());
        tbData.setItems(sortedList);
        tbData2.setItems(sortedList2);
        sortedList.comparatorProperty().bind(tbData.comparatorProperty());
        sortedList2.comparatorProperty().bind(tbData2.comparatorProperty());
    }

    private void initTable() {
        logger.info("initTable");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        article_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        /**    EMPLOYER   */
        id_E.setCellValueFactory(new PropertyValueFactory<>("id"));
        name_E.setCellValueFactory(cellData->{
            String fname=  cellData.getValue().getFirstName();
            String lname=  cellData.getValue().getLastName();
            return new SimpleStringProperty(fname+" "+lname);
        });
//        firstname_E.setCellValueFactory(new PropertyValueFactory<>("firstName"));
//        lastname_E.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    }

    private void loadTableData() throws SQLException {
        logger.info("loadTableData");
        List<Article> articles = articleDbhelper.getArticles();
        List<Employer> employers = employerDbHelper.getEmployers();

        articlelist =  FXCollections.observableArrayList(articles);
        emploerlist =  FXCollections.observableArrayList(employers);
        filterView.getItems().setAll(articlelist);
        filterView2.getItems().setAll(emploerlist);
    }




    public void setInventaireItem(Inventaire_Item inventaireItem) {
        logger.info("setInventaireItem");
        this.inventaireItem = inventaireItem;
        try {


           // String selectedLoc = locDbhelper.getLocalisationById(inventaireItem.getLocalisation_id()).getLocName();
            String selectedLoc =   locations_and_floor.get(inventaireItem.getLocalisation_id());
            Employer selectedEmployer = employerDbHelper.getEmployerById(inventaireItem.getEmployer_id());
            String selectedstatus=inventaireItem.getStatus();
            selectedArticle = articleDbhelper.getArticleById(inventaireItem.getArticle_id());

            if (selectedArticle == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun article sélectionné.");
                return;
            } else {
                // Ensure tbData.getItems() is not empty or null
                if (tbData.getItems() == null || tbData.getItems().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "La liste des articles est vide.");
                    return;
                }

                // Find the index of the selectedArticle in the table
                int index = -1;
                for (int i = 0; i < tbData.getItems().size(); i++) {
                    if (tbData.getItems().get(i).getId() == selectedArticle.getId()) {
                        index = i;
                        break;
                    }
                }

                if (index >= 0) {
                    // Select the article in the table view
                    tbData.getSelectionModel().select(index);
                    tbData.scrollTo(index > 2 ? index - 2 : 0);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Article non trouvé.");
                }
            }

            if (selectedEmployer == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun Employer sélectionné.");
                return;
            } else {
                // Ensure tbData2.getItems() is not empty or null
                if (tbData2.getItems() == null || tbData2.getItems().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "La liste des employeurs est vide.");
                    return;
                }

                // Find the index of the selectedEmployer in the table
                int index = -1;
                for (int i = 0; i < tbData2.getItems().size(); i++) {
                    if (tbData2.getItems().get(i).getId() == selectedEmployer.getId()) {
                        index = i;
                        break;
                    }
                }

                if (index >= 0) {
                    // Select the employer in the table view
                    tbData2.getSelectionModel().select(index);
                    tbData2.scrollTo(index > 2 ? index - 2 : 0);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Employer non trouvé.");
                }
            }
            locationChoiceBox.setValue(selectedLoc);
            status_inventaire.setValue(selectedstatus);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDate selectedDate = LocalDate.parse(inventaireItem.getFormattedDateTime(), formatter);

            calendarPicker1.setValue(selectedDate);

        } catch (Exception e) {
           logger.error(e);
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
        }

        employerInventaireCode.setText(inventaireItem.getNum_inventaire());

    }

    @FXML
    private void handleUpdate() {
        logger.info("handleUpdate");
        try {
            // Get the selected location name from the ChoiceBox
            String selectedLocation = locationChoiceBox.getSelectionModel().getSelectedItem();
            logger.info("Selected location: " + selectedLocation);

            // Validate and extract the location name
            String locName = null;
            if (selectedLocation != null && selectedLocation.contains("   ")) {
                String[] parts = selectedLocation.split("   ");
                if (parts.length > 1) {
                    locName = parts[1]; // Extract the location name
                } else {
                    throw new IllegalArgumentException("Invalid location format: " + selectedLocation);
                }
            } else {
                throw new IllegalArgumentException("No location selected or invalid format.");
            }

            // Retrieve the location ID by its name
            int loc_id = locDbhelper.getLocationIdByName(locName);

            // Retrieve IDs for other entities
            int article_id = tbData.getSelectionModel().getSelectedItem().getId();
            int employer_id = tbData2.getSelectionModel().getSelectedItem().getId();
            int inv_id = inventaireItem.getId();
            int user_id = SessionManager.getActiveUserId();
            String status = status_inventaire.getValue();
            LocalDate selectedDate = calendarPicker1.getValue();
            String inv_date = selectedDate != null ? selectedDate.toString() : "";

            // Create the updated Inventaire_Item object
            Inventaire_Item updated_item = new Inventaire_Item(
                    inv_id, article_id, loc_id, user_id, employer_id,
                    employerInventaireCode.getText(), inv_date, status
            );

            // Update the inventory item
            inventaireItemDbHelper.updateInventaireItem(updated_item);
            // Refresh the parent table and close the window
            parentController.refreshTableData();
            closeWindow();
        } catch (IllegalArgumentException e) {
            logger.error("Validation error: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Validation Error", e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error: ", e);
            showAlert(Alert.AlertType.ERROR, "Erreur", "An error occurred: " + e.getMessage());
            closeWindow();
        }
    }

    private void loadChoiceBoxData() {
        logger.info("loadChoiceBoxData");
        // Retrieve localisations from the database
        List<Localisation> locations = locDbhelper.getLocalisations();
        // Populate the map and ChoiceBox with formatted strings
        for (Localisation loc : locations) {
            String formattedLoc = "الطابق: " + loc.getFloor() + "   " + loc.getLocName();
            locations_and_floor.put(loc.getId(), formattedLoc); // Map ID to formatted string
            locationChoiceBox.getItems().add(formattedLoc); // Add the formatted string to the ChoiceBox
        }

        // Add inventory status options to the ChoiceBox
        status_inventaire.getItems().addAll(FXCollections.observableArrayList(inv_status));
    }


    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) locationChoiceBox.getScene().getWindow();
        stage.close();
    }
}
