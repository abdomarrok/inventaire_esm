package com.marrok.inventaire_esm.controller.inventaire;

import com.dlsc.gemsfx.FilterView;
import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.model.Employer;
import com.marrok.inventaire_esm.model.Inventaire_Item;
import com.marrok.inventaire_esm.model.Localisation;
import com.marrok.inventaire_esm.util.database.*;
import com.marrok.inventaire_esm.util.SessionManager;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.marrok.inventaire_esm.util.GeneralUtil.showAlert;

public class UpdateController implements Initializable {

    public ChoiceBox<String> status_inventaire;
    public DatePicker calendarPicker1;
    @FXML
    private ChoiceBox<String> locationChoiceBox;


    public TableView<Employer> tbData2;
    public TableColumn<Employer, Integer> id_E;
    public TableColumn<Employer, String> firstname_E;
    public TableColumn<Employer, String> lastname_E;


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
        initTable();
        loadChoiceBoxData();
        try {
            loadFilter();
            loadTableData(); // Load data once

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        CSSFX.start();
    }

    public void setParentController(InventaireItemController parentController) {
        this.parentController = parentController;
    }

    private void loadFilter() throws SQLException {
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
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        article_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        /**    EMPLOYER   */
        id_E.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstname_E.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastname_E.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    }

    private void loadTableData() throws SQLException {
        List<Article> articles = articleDbhelper.getArticles();
        List<Employer> employers = employerDbHelper.getEmployers();

        articlelist =  FXCollections.observableArrayList(articles);
        emploerlist =  FXCollections.observableArrayList(employers);
        filterView.getItems().setAll(articlelist);
        filterView2.getItems().setAll(emploerlist);
    }




    public void setInventaireItem(Inventaire_Item inventaireItem) {
        this.inventaireItem = inventaireItem;
        try {
            String selectedLoc = locDbhelper.getLocalisationById(inventaireItem.getLocalisation_id()).getLocName();
           Employer selectedEmployer = employerDbHelper.getEmployerById(inventaireItem.getEmployer_id());
          String selectedstatus=inventaireItem.getStatus();
            selectedArticle = articleDbhelper.getArticleById(inventaireItem.getArticle_id());


            if (selectedArticle == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun article sélectionné.");
                return;
            }else {
                tbData.getSelectionModel().select(selectedArticle);
            }
            System.out.println("UpdateController_test.setInventaireItem selectedArticle = " + selectedArticle);

            locationChoiceBox.setValue(selectedLoc);
            status_inventaire.setValue(selectedstatus);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDate selectedDate = LocalDate.parse(inventaireItem.getFormattedDateTime(), formatter);

            calendarPicker1.setValue(selectedDate);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
        }

        employerInventaireCode.setText(inventaireItem.getNum_inventaire());

    }

    @FXML
    private void handleUpdate() {
        try {
            int loc_id = locDbhelper.getLocationIdByName(locationChoiceBox.getSelectionModel().getSelectedItem());
            int article_id = selectedArticle.getId(); // Use selectedArticle directly
            int employer_id = tbData2.getSelectionModel().getSelectedItem().getId();
            int inv_id = inventaireItem.getId();
            int user_id = SessionManager.getActiveUserId();
            String status = status_inventaire.getValue();
            String inv_status = status_inventaire.getValue();
            LocalDate selectedDate = calendarPicker1.getValue();
            String inv_date = selectedDate != null ? selectedDate.toString() : "";
            System.out.println("AddController.handleAdd selected date= "+inv_date);

            Inventaire_Item updated_item = new Inventaire_Item(inv_id, article_id,
                    loc_id, user_id, employer_id, employerInventaireCode.getText(),inv_date ,status);

            inventaireItemDbHelper.updateInventaireItem(updated_item);
            parentController.refreshTableData(); // Refresh the table data in the parent controller
            closeWindow();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
            closeWindow();
        }
    }

    private void loadChoiceBoxData() {
        List<Localisation> Locations = locDbhelper.getLocalisations();
        List<String> locations_and_floor = new ArrayList<>();
        for (Localisation l : Locations) {
            locations_and_floor.add("الطابق: " + l.getFloor() + "   " + l.getLocName());
        }

        locationChoiceBox.getItems().addAll(locations_and_floor);
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
