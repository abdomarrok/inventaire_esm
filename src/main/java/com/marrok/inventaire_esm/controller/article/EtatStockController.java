package com.marrok.inventaire_esm.controller.article;

import com.marrok.inventaire_esm.controller.bon_entree.AddBonEntreeController;
import com.marrok.inventaire_esm.controller.bon_sortie.AddBonSortieController;
import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.util.database.ArticleDbHelper;
import com.marrok.inventaire_esm.util.database.CategoryDbHelper;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class EtatStockController implements Initializable {


    public  TableView<Article> tableView;

    @FXML
    public TableColumn<Article, Integer> id_article_v;
    @FXML
    public TableColumn<Article, String> nameColumn;
    @FXML
    public TableColumn<Article, String> unitColumn;
    @FXML
    public TableColumn<Article, Integer> quantityColumn;
    @FXML
    public TableColumn<Article, String> remarkColumn;
    @FXML
    public TableColumn descriptionColumn;
    @FXML
    public TableColumn<Article, String> categoryColmun;
    @FXML
    public TextField searchField;

    @FXML
    public Button addButton;
    @FXML
    public Button updateButton;
    @FXML
    public Button deleteButton;
    public Button bk_Dashboard_from_etat_stock;

    private static ObservableList<Article> articleList;
    private static  FilteredList<Article> filteredArticleList;
    private ArticleDbHelper articleDbhelper = new ArticleDbHelper();
    private CategoryDbHelper categoryDbhelper = new CategoryDbHelper();

    private Article selectedArticle;
    @FXML
    private Label titleLabel;




    public EtatStockController() throws SQLException {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
        initializeColumns();
        setupSearchFilter();
        setupTableSelectionListener();

    }
    public void goBonEntree(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/bon_entree/add_bon_entree-view.fxml"));
            Parent root = loader.load();
            AddBonEntreeController controller = loader.getController();//من اجل ارسال متغيرات عبره
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("وصل ادخال");
            stage.setResizable(false);
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goBonSortie(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/bon_sortie/add_bon_sortie-view.fxml"));
            Parent root = loader.load();
            AddBonSortieController controller = loader.getController();//من اجل ارسال متغيرات عبره
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("وصل اخراج");
            stage.setResizable(false);
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }





    private void initializeColumns() {
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_LAST_COLUMN);

        // Set cell value factories
        id_article_v.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unite"));
        Map<Integer, Integer> totalQuantities = articleDbhelper.getTotalQuantitiesByArticle();

        quantityColumn.setCellValueFactory(cellData -> {
            Article article = cellData.getValue(); // Get the current Article object
            int articleId = article.getId(); // Get the article ID
            Integer totalQuantity = totalQuantities.get(articleId); // Get the total quantity for this article
            return new SimpleIntegerProperty(totalQuantity != null ? totalQuantity : 0).asObject(); // Return the quantity or 0 if none
        });
        remarkColumn.setCellValueFactory(new PropertyValueFactory<>("remarque"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Custom cell value factory for categoryColmun to fetch category name by id_category
        categoryColmun.setCellValueFactory(cellData -> {
            int categoryId = cellData.getValue().getIdCategory();
            String categoryName = categoryDbhelper.getCategoryById(categoryId);
            return new SimpleStringProperty(categoryName != null && !categoryName.isEmpty() ? categoryName : "Unknown Category");
        });
    }

    public void  loadData() {
            List<Article> articles = articleDbhelper.getArticles();
            articleList = FXCollections.observableArrayList(articles);
            filteredArticleList = new FilteredList<>(articleList, p -> true);
            tableView.setItems(filteredArticleList);
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

    private void setupTableSelectionListener() {
        bk_Dashboard_from_etat_stock.setOnAction(event -> {
            GeneralUtil.goBackStockDashboard(event);
        });
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedArticle = newValue);

    }



    public ObservableList<Article> getArticleList() {
        return articleList;
    }

    public  void refreshTableData() {
            List<Article> articles = articleDbhelper.getArticles();
            articleList.setAll(articles);
            tableView.refresh();
    }





}





