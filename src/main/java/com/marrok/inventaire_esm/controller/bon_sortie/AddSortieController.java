package com.marrok.inventaire_esm.controller.bon_sortie;

import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.model.Sortie;
import com.marrok.inventaire_esm.util.database.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.marrok.inventaire_esm.util.GeneralUtil.showAlert;

public class AddSortieController implements Initializable {
    @FXML
    private TextField searchField;

    @FXML
    private TableView<Article> articleTable;

    @FXML
    private TableColumn<Article, String> articleNameColumn;

    @FXML
    private TextField quantityField;

    private ObservableList<Article> articleList = FXCollections.observableArrayList();
    private FilteredList<Article> filteredArticleList;
    private Sortie selectedSortie;
    private Article selectedArticle;
    private DatabaseHelper dbhelper = new DatabaseHelper();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Setup the article column
        articleNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Load article data (fetch from your database)
        articleList.setAll(fetchArticlesFromDatabase());
        filteredArticleList = new FilteredList<>(articleList, p -> true);
        articleTable.setItems(filteredArticleList);
        setupSearchFilter();
    }

    private void setupSearchFilter() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredArticleList.setPredicate(article -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return article.getName().toLowerCase().contains(lowerCaseFilter)
                        || article.getUnite().toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(article.getId()).contains(lowerCaseFilter)
                        || String.valueOf(article.getIdCategory()).toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    // Fetch available articles from the database
    private ObservableList<Article> fetchArticlesFromDatabase() {
        return FXCollections.observableArrayList(
                dbhelper.getArticles()
        );
    }

    public AddSortieController() throws SQLException {
    }

    public void confirmSelection(ActionEvent event) {
        selectedArticle = articleTable.getSelectionModel().getSelectedItem();

        if (selectedArticle == null || quantityField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select an article and enter a valid quantity.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter valid numbers for quantity.");
            return;
        }

        // Check available stock for the selected article
        int availableStock = dbhelper.getTotalQuantityByArticleId(selectedArticle.getId());
        if (availableStock < quantity || quantity <= 0) {
            showAlert(Alert.AlertType.ERROR, "Insufficient Stock",
                    "Requested quantity exceeds available stock. Available: " + availableStock);
            return;  // Do not proceed if stock is insufficient
        }

        selectedSortie = new Sortie();
        selectedSortie.setQuantity(quantity);
        selectedSortie.setIdArticle(selectedArticle.getId());
        selectedSortie.setIdBs(0); // to be set up elsewhere

        closeDialog(event);
    }

    // Getter to retrieve the selected Sortie
    public Sortie getSelectedSortie() {
        return selectedSortie;
    }

    @FXML
    public void closeDialog(ActionEvent event) {
        // Close the dialog without any action
        Stage stage = (Stage) articleTable.getScene().getWindow();
        stage.close();
    }
}