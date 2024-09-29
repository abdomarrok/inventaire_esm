package com.marrok.inventaire_esm.controller.inventaire;

import com.dlsc.gemsfx.FilterView;
import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.model.Inventaire_Item;
import com.marrok.inventaire_esm.util.DatabaseHelper;
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
import java.util.List;
import java.util.ResourceBundle;

import static com.marrok.inventaire_esm.util.GeneralUtil.showAlert;

public class UpdateController_test implements Initializable {

    @FXML
    private ChoiceBox<String> locationChoiceBox;

    @FXML
    private ChoiceBox<String> employerChoiceBox;

    @FXML
    private Button updateButton;

    @FXML
    private Button cancelButton;

    public FilterView<Article> filterView;
    public TableView<Article> tbData;
    public TableColumn<Article, Integer> id;
    public TableColumn<Article, String> article_name;

    private InventaireItemController parentController;
    private ObservableList<Article> articleList;
    private Article selectedArticle;
    private Inventaire_Item inventaireItem;
    @FXML
    private TextField employerInventaireCode;

    DatabaseHelper dbhelper = new DatabaseHelper();

    public UpdateController_test() throws SQLException {
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
        filterView.setTextFilterProvider(text -> article -> {
            if (text == null || text.isEmpty()) {
                return true; // Show all articles if text is empty
            }
            String lowerCase = text.toLowerCase();
            return article.getName().toLowerCase().contains(lowerCase) ||
                    String.valueOf(article.getId()).toLowerCase().contains(lowerCase);
        });
        loadTableData();
        SortedList<Article> sortedList = new SortedList<>(filterView.getFilteredItems());
        tbData.setItems(sortedList);
        sortedList.comparatorProperty().bind(tbData.comparatorProperty());



    }
    private void initTable() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        article_name.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    private void loadTableData() throws SQLException {
        DatabaseHelper dbHelper = new DatabaseHelper();
        List<Article> articles = dbHelper.getArticles();
        articleList = FXCollections.observableArrayList(articles);
        filterView.getItems().setAll(articleList); // Set the articles into the table

    }



    public void setInventaireItem(Inventaire_Item inventaireItem) {
        this.inventaireItem = inventaireItem;
        try {
            String selectedLoc = dbhelper.getLocalisationById(inventaireItem.getLocalisation_id()).getLocName();
            String selectedEmployer = dbhelper.getEmployerFullNameById(inventaireItem.getEmployer_id());
            selectedArticle = dbhelper.getArticleById(inventaireItem.getArticle_id());


            if (selectedArticle == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun article sélectionné.");
                return;
            }else {
                tbData.getSelectionModel().select(selectedArticle);
            }
            System.out.println("UpdateController_test.setInventaireItem selectedArticle = " + selectedArticle);

            locationChoiceBox.setValue(selectedLoc);
            employerChoiceBox.setValue(selectedEmployer);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
        }

        employerInventaireCode.setText(inventaireItem.getNum_inventaire());

    }

    @FXML
    private void handleUpdate() {
        try {
            int loc_id = dbhelper.getLocationIdByName(locationChoiceBox.getSelectionModel().getSelectedItem());
            int article_id = selectedArticle.getId(); // Use selectedArticle directly
            int employer_id = dbhelper.getEmployerIdByName(employerChoiceBox.getSelectionModel().getSelectedItem());
            int inv_id = inventaireItem.getId();
            int user_id = SessionManager.getActiveUserId();
            Inventaire_Item updated_item = new Inventaire_Item(inv_id, article_id, loc_id, user_id, employer_id, employerInventaireCode.getText());

            dbhelper.updateInventaireItem(updated_item);
            parentController.refreshTableData(); // Refresh the table data in the parent controller
            closeWindow();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
            closeWindow();
        }
    }

    private void loadChoiceBoxData() {
        List<String> employers = dbhelper.getAllEmployersNames();
        List<String> locations = dbhelper.getAllLocations();
        employerChoiceBox.getItems().addAll(employers);
        locationChoiceBox.getItems().addAll(locations);
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) updateButton.getScene().getWindow();
        stage.close();
    }
}
