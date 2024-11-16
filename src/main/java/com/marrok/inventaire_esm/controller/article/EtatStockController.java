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
import javafx.util.Callback;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

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
    public TableColumn<Article,Integer> entreeColumn;
    public TableColumn<Article,Integer> sortieColumn;
    public TableColumn<Article,Integer> editColumn;
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

        // Set cell value factories for basic properties
        id_article_v.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unite"));
        remarkColumn.setCellValueFactory(new PropertyValueFactory<>("remarque"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Pre-fetch total quantities for each article and store in a map
        Map<Integer, Integer> totalQuantities = articleDbhelper.getTotalQuantitiesByArticle();

        // Set cell value factory for total quantity
        quantityColumn.setCellValueFactory(cellData -> {
            Article article = cellData.getValue();
            int articleId = article.getId();
            Integer totalQuantity = totalQuantities.get(articleId); // Fetch pre-calculated quantity
            return new SimpleIntegerProperty(totalQuantity != null ? totalQuantity : 0).asObject();
        });

        // Set cell value factory for entree column
        entreeColumn.setCellValueFactory(cellData -> {
            Article article = cellData.getValue();
            int articleId = article.getId();
            int totalArticleEntree = articleDbhelper.getTotalEntredQuantityByArticleId(articleId);
            return new SimpleIntegerProperty(totalArticleEntree).asObject();
        });

        // Set cell value factory for sortie column
        sortieColumn.setCellValueFactory(cellData -> {
            Article article = cellData.getValue();
            int articleId = article.getId();
            int totalArticleSortie = articleDbhelper.getTotalSortieQuantityByArticleId(articleId);
            return new SimpleIntegerProperty(totalArticleSortie).asObject();
        });
        editColumn.setCellValueFactory(cellData -> {
            Article article = cellData.getValue();
            int articleId = article.getId();
            int totalAdjustemnt = articleDbhelper.getTotalAdjustmentByArticleId(articleId) ;
            return new SimpleIntegerProperty(totalAdjustemnt).asObject();
        });

        // Custom cell value factory for category column to fetch category name by id_category
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
                    return true; // Show all articles if the search field is empty
                }

                // Fetch category name safely
                String categoryName = categoryDbhelper.getCategoryById(article.getIdCategory());
                if (categoryName == null) {
                    categoryName = ""; // Default to empty string if category not found
                }

                // Convert search text to lowercase
                String lowerCaseFilter = newValue.toLowerCase();

                // Perform case-insensitive checks
                return article.getName().toLowerCase().contains(lowerCaseFilter)
                        || article.getUnite().toLowerCase().contains(lowerCaseFilter)
                        || article.getDescription().toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(article.getId()).contains(lowerCaseFilter)
                        || String.valueOf(article.getIdCategory()).contains(lowerCaseFilter)
                        || categoryName.toLowerCase().contains(lowerCaseFilter);
            });
        });
    }


    private void setupTableSelectionListener() {
        bk_Dashboard_from_etat_stock.setOnAction(event -> {
            GeneralUtil.goBackStockDashboard(event);
        });
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedArticle = newValue);



    }
    public void setupSpecificCellEventListener() {
        // Correct type of TableColumn is Integer, not String
        editColumn.setCellFactory(new Callback<TableColumn<Article, Integer>, TableCell<Article, Integer>>() {
            @Override
            public TableCell<Article, Integer> call(TableColumn<Article, Integer> param) {
                return new TableCell<Article, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            TableRow<Article> row = getTableRow();

                            // Check if this is the specific cell you want to target (e.g., first row)
                            if (row != null && row.getIndex() == 0 && getTableColumn() == editColumn) {
                                // Add your specific event handler for this cell
                                this.setOnMouseClicked(event -> {
                                    System.out.println("Specific cell clicked! Row: " + row.getIndex() + ", Column: " + getTableColumn().getText());
                                    // Your event logic here
                                });
                            } else {
                                // Clear event handler for other cells
                                this.setOnMouseClicked(null);
                            }
                        } else {
                            // Reset the cell to default if it's empty or no longer in use
                            this.setOnMouseClicked(null);
                        }
                    }
                };
            }
        });
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





