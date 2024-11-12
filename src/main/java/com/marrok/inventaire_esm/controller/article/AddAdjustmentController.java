package com.marrok.inventaire_esm.controller.article;

import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.util.database.ArticleDbHelper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.Map;


public class AddAdjustmentController {

    @FXML
    private TableView<Article> articleTable;  // Corrected to use Article instead of Entree
    public TableColumn<Article, String> articleUniteColumn;
    public TableColumn<Article, String> articleNameColumn;
    public TableColumn<Article, Integer> articlequantityColumn;
    private ObservableList<Article> articleList = FXCollections.observableArrayList();
    private FilteredList<Article> filteredArticleList;
    private Article selectedArticle;
    @FXML
    private TextField searchField;
    private ArticleDbHelper articleDbhelper = new ArticleDbHelper();
    Map<Integer, Integer> totalQuantities = articleDbhelper.getTotalQuantitiesByArticle();
    public  AddAdjustmentController () throws SQLException {
    }
    @FXML
    public void initialize() {
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
        return FXCollections.observableArrayList(
                articleDbhelper.getArticles()
        );
    }


    public void confirmSelection(ActionEvent event) {
    }

    public void closeDialog(ActionEvent event) {
    }
}
