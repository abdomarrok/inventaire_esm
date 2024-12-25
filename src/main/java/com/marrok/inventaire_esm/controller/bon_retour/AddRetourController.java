package com.marrok.inventaire_esm.controller.bon_retour;

import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.model.Retour;
import com.marrok.inventaire_esm.util.database.ArticleDbHelper;
import javafx.beans.property.SimpleIntegerProperty;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;

import static com.marrok.inventaire_esm.util.GeneralUtil.showAlert;

public class AddRetourController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("Initializing AddSortieController");
        // Setup the article column
        articleNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        articleUniteColumn.setCellValueFactory(new PropertyValueFactory<>("unite"));
        articlequantityColumn.setCellValueFactory(cellData -> {
            Article article = cellData.getValue();
            int articleId = article.getId();
            Integer totalQuantity = totalQuantities.get(articleId); // Fetch pre-calculated quantity
            return new SimpleIntegerProperty(totalQuantity != null ? totalQuantity : 0).asObject();
        });

        // Load article data (fetch from your database)
        articleList.setAll(fetchArticlesFromDatabase());
        filteredArticleList = new FilteredList<>(articleList, p -> true);
        articleTable.setItems(filteredArticleList);
        setupSearchFilter();
    }

    Logger logger = LogManager.getLogger(AddRetourController.class);

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Article> articleTable;

    @FXML
    private TableColumn<Article, String> articleNameColumn;
    public TableColumn<Article, String>  articleUniteColumn;
    public TableColumn<Article, Integer> articlequantityColumn;

    @FXML
    private TextField quantityField;

    private ObservableList<Article> articleList = FXCollections.observableArrayList();
    private FilteredList<Article> filteredArticleList;
    private Retour selectedRetour;
    private Article selectedArticle;
    private ArticleDbHelper articleDbhelper = new ArticleDbHelper();
    Map<Integer, Integer> totalQuantities = articleDbhelper.getTotalQuantitiesByArticle();


    public AddRetourController() throws SQLException {
    }


    public Retour getSelectedRetour() {
        return selectedRetour;
    }

    // Fetch available articles from the database
    private ObservableList<Article> fetchArticlesFromDatabase() {
        logger.info("Fetching articles from database");
        return FXCollections.observableArrayList(
                articleDbhelper.getArticles()
        );
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

    public void confirmSelection(ActionEvent event) {
        logger.info("Confirm selection");
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
        int availableStock = articleDbhelper.getTotalQuantityByArticleId(selectedArticle.getId());
        if (availableStock < quantity ) {
            showAlert(Alert.AlertType.ERROR, "Insufficient Stock",
                    "Requested quantity exceeds available stock. Available: " + availableStock);
            return;  // Do not proceed if stock is insufficient
        }
        if( quantity <= 0){
            showAlert(Alert.AlertType.ERROR, "Error", "الكمية لا يجدر ان تكون صفرا او اقل من 0");
            return;
        }

        selectedRetour = new Retour();
        selectedRetour.setQuantity(quantity);
        selectedRetour.setIdArticle(selectedArticle.getId());
        selectedRetour.setIdBr(0); // to be set up elsewhere

        closeDialog(event);
    }

    // Getter to retrieve the selected Sortie
    public Retour getselectedRetour() {
        return selectedRetour;
    }

    @FXML
    public void closeDialog(ActionEvent event) {
        // Close the dialog without any action
        Stage stage = (Stage) articleTable.getScene().getWindow();
        stage.close();
    }
}
