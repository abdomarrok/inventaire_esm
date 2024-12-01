package com.marrok.inventaire_esm.controller.bon_entree;

import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.model.Entree;
import com.marrok.inventaire_esm.model.Fournisseur;
import com.marrok.inventaire_esm.util.database.ArticleDbHelper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.Map;

import static com.marrok.inventaire_esm.util.GeneralUtil.showAlert;

public class AddEntreeController {

    Logger logger = Logger.getLogger(AddEntreeController.class);

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Article> articleTable;  // Corrected to use Article instead of Entree
    public TableColumn<Article, String>articleUniteColumn;
    @FXML
    private TableColumn<Article, String> articleNameColumn;
    public TableColumn<Article, Integer> articlequantityColumn;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField priceField;

    private ObservableList<Article> articleList = FXCollections.observableArrayList();
    private FilteredList<Article> filteredArticleList; // Use FilteredList to keep track of filtering
    private ArticleDbHelper articleDbhelper = new ArticleDbHelper();
    Map<Integer, Integer> totalQuantities = articleDbhelper.getTotalQuantitiesByArticle();
    private Entree selectedEntree;
    private Article selectedArticle;

    public AddEntreeController() throws SQLException {
        // Ensure your DatabaseHelper initializes properly
    }

    @FXML
    public void initialize() {
        logger.info("Initializing AddEntreeController");
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
        articleTable.setItems(filteredArticleList); // Bind to filteredArticleList
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
        logger.info("Fetching articles from database");
        return FXCollections.observableArrayList(
                articleDbhelper.getArticles()
        );
    }

    @FXML
    public void confirmSelection(ActionEvent event) {
        logger.info("Confirm selection");
        selectedArticle = articleTable.getSelectionModel().getSelectedItem();

        if (selectedArticle == null || quantityField.getText().isEmpty() || priceField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select an article and enter valid quantity and price.");
            return;
        }

        int quantity;
        double pricePerUnit;

        try {
            quantity = Integer.parseInt(quantityField.getText());
            if( quantity <= 0){
                showAlert(Alert.AlertType.ERROR, "Error", "الكمية لا يجدر ان تكون صفرا او اقل من 0");
                return;
            }
            pricePerUnit = Double.parseDouble(priceField.getText());
        } catch (NumberFormatException e) {
            logger.error(e);
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter valid numbers for quantity and price.");
            return;
        }

        // Create a new Entree object and set the data
        selectedEntree = new Entree();
        selectedEntree.setIdArticle(selectedArticle.getId());  // Use the article's ID
        selectedEntree.setQuantity(quantity);
        selectedEntree.setUnitPrice(pricePerUnit);
        selectedEntree.setIdBe(0); // Set the Bon Entree ID or leave for later if it's determined elsewhere

       closeDialog(event);
    }
    @FXML
    public void closeDialog(ActionEvent event) {
        // Close the dialog without any action
        Stage stage = (Stage) articleTable.getScene().getWindow();
        stage.close();
    }

    // Getter to retrieve the selected Entree
    public Entree getSelectedEntree() {
        return selectedEntree;
    }
}
