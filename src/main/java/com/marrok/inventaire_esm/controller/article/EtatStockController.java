package com.marrok.inventaire_esm.controller.article;

import com.marrok.inventaire_esm.controller.bon_entree.AddBonEntreeController;
import com.marrok.inventaire_esm.controller.bon_sortie.AddBonSortieController;
import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.model.Category;
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
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class EtatStockController implements Initializable {

    Logger logger  = Logger.getLogger(EtatStockController.class);


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
    public TableColumn<Article,Integer> retourColumn;
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
    private ChoiceBox<String> categoryFilter;

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
      Map<Integer, String> categoryCache = new HashMap<>();
     Map<Integer, Integer> entreeCache = articleDbhelper.getTotalEntredQuantities();
     Map<Integer, Integer> sortieCache = articleDbhelper.getTotalSortieQuantities();
     Map<Integer,Integer> retourCache = articleDbhelper.getTotalRetourQuantities();
     Map<Integer, Integer> adjustmentCache = articleDbhelper.getTotalAdjustments();
    Map<Integer, Integer> totalQuantities = articleDbhelper.getTotalQuantitiesByArticle();
    private Article selectedArticle;
    @FXML
    private Label titleLabel;




    public EtatStockController() throws SQLException {
    }
    private void preloadData() throws SQLException {
        logger.info("Preloading data");

        // Load categories
        List<Category> categories = categoryDbhelper.getCategories();
        categories.forEach(category -> categoryCache.put(category.getId(), category.getName()));
        populateCategoryFilter();

        // Preload entree data
        entreeCache = articleDbhelper.getTotalEntredQuantities();

        // Preload sortie data
        sortieCache = articleDbhelper.getTotalSortieQuantities();
        retourCache = articleDbhelper.getTotalRetourQuantities();

        // Preload adjustment data
        adjustmentCache = articleDbhelper.getTotalAdjustments();

        // Get total quantities
        totalQuantities = articleDbhelper.getTotalQuantitiesByArticle();
    }
    private void populateCategoryFilter() {
        ObservableList<String> categories = FXCollections.observableArrayList();
        categories.add("All"); // Default option to show all categories
        categories.addAll(categoryCache.values()); // Add all category names
        categoryFilter.setItems(categories);
        categoryFilter.setValue("All"); // Set default value
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("Initializing EtatStockController");
        try {
            preloadData(); // Load categories before initializing columns
        } catch (SQLException e) {
            logger.error("Failed to preload categories", e);
        }
        loadData();
        initializeColumns();
//        setupSearchFilter();
//        setupCategoryFilter();
        setupSearchAndCategoryFilters();
        setupTableSelectionListener();

    }



    public void goBonEntree(ActionEvent event) {
        logger.info("goBonEntree called");
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
            logger.error(e);
//            throw new RuntimeException(e);
        }
    }

    public void goBonSortie(ActionEvent event) {
        logger.info("goBonSortie called");
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
            logger.error(e);
//            throw new RuntimeException(e);
        }
    }





    private void initializeColumns() {
        logger.info("initializeColumns called");
        id_article_v.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unite"));
        remarkColumn.setCellValueFactory(new PropertyValueFactory<>("remarque"));
        //descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Set cell value factory for total quantity
        quantityColumn.setCellValueFactory(cellData -> {
            Article article = cellData.getValue();
            int articleId = article.getId();
            Integer totalQuantity = totalQuantities.get(articleId); // Fetch pre-calculated quantity
            return new SimpleIntegerProperty(totalQuantity != null ? totalQuantity : 0).asObject();
        });
        // Use preloaded data for entree column
        entreeColumn.setCellValueFactory(cellData -> {
            int articleId = cellData.getValue().getId();
            int totalArticleEntree = entreeCache.getOrDefault(articleId, 0);
            return new SimpleIntegerProperty(totalArticleEntree).asObject();
        });

        // Use preloaded data for sortie column
        sortieColumn.setCellValueFactory(cellData -> {
            int articleId = cellData.getValue().getId();
            int totalArticleSortie = sortieCache.getOrDefault(articleId, 0);
            return new SimpleIntegerProperty(totalArticleSortie).asObject();
        });
        retourColumn.setCellValueFactory(cellData->{
            int articleId = cellData.getValue().getId();
            int totalArticleRetour = retourCache.getOrDefault(articleId, 0);
            return new SimpleIntegerProperty(totalArticleRetour).asObject();

        });
        // Use preloaded data for edit column
        editColumn.setCellValueFactory(cellData -> {
            int articleId = cellData.getValue().getId();
            int totalAdjustment = adjustmentCache.getOrDefault(articleId, 0);
            return new SimpleIntegerProperty(totalAdjustment).asObject();
        });


        categoryColmun.setCellValueFactory(cellData -> {
            int categoryId = cellData.getValue().getIdCategory();
            String categoryName = categoryCache.getOrDefault(categoryId, "Unknown Category");
            return new SimpleStringProperty(categoryName);
        });
    }

    public void  loadData() {
            logger.info("loadData called from EtatStockController");
            List<Article> articles = articleDbhelper.getArticles();
            articleList = FXCollections.observableArrayList(articles);
            filteredArticleList = new FilteredList<>(articleList, p -> true);
            tableView.setItems(filteredArticleList);
    }


//    private void setupSearchFilter() {
//        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
//            filteredArticleList.setPredicate(article -> {
//                if (newValue == null || newValue.isEmpty()) {
//                    return true; // Show all articles if the search field is empty
//                }
//
//                // Fetch category name safely
//                String categoryName = categoryDbhelper.getCategoryById(article.getIdCategory());
//                if (categoryName == null) {
//                    categoryName = ""; // Default to empty string if category not found
//                }
//
//                // Convert search text to lowercase
//                String lowerCaseFilter = newValue.toLowerCase();
//
//                // Perform case-insensitive checks
//                return article.getName().toLowerCase().contains(lowerCaseFilter)
//                        || article.getUnite().toLowerCase().contains(lowerCaseFilter)
//                        || String.valueOf(article.getId()).contains(lowerCaseFilter)
//                        || String.valueOf(article.getIdCategory()).contains(lowerCaseFilter)
//                        || categoryName.toLowerCase().contains(lowerCaseFilter);
//            });
//        });
//    }
//    private void setupCategoryFilter() {
//        categoryFilter.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            filteredArticleList.setPredicate(article -> {
//                if (newValue == null || newValue.equals("All")) {
//                    return true; // Show all articles if "All" is selected
//                }
//                String categoryName = categoryCache.get(article.getIdCategory());
//                return categoryName != null && categoryName.equals(newValue);
//            });
//        });
//    }
private void setupSearchAndCategoryFilters() {
    // Add listeners for search field and category filter
    searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
    categoryFilter.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> applyFilters());
}

    // Applies both search and category filters
    private void applyFilters() {
        filteredArticleList.setPredicate(article -> {
            // Search filter logic
            String searchText = searchField.getText();
            boolean matchesSearch = true; // Default to true if no search text
            if (searchText != null && !searchText.isEmpty()) {
                String lowerCaseFilter = searchText.toLowerCase();
                String categoryName = categoryDbhelper.getCategoryById(article.getIdCategory());
                if (categoryName == null) categoryName = "";

                matchesSearch = article.getName().toLowerCase().contains(lowerCaseFilter)
                        || article.getUnite().toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(article.getId()).contains(lowerCaseFilter)
                        || String.valueOf(article.getIdCategory()).contains(lowerCaseFilter)
                        || categoryName.toLowerCase().contains(lowerCaseFilter);
            }

            // Category filter logic
            String selectedCategory = categoryFilter.getValue();
            boolean matchesCategory = true; // Default to true if no category selected
            if (selectedCategory != null && !selectedCategory.equals("All")) {
                String categoryName = categoryCache.get(article.getIdCategory());
                matchesCategory = categoryName != null && categoryName.equals(selectedCategory);
            }

            // Combine both filters
            return matchesSearch && matchesCategory;
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
            logger.info("refreshTableData called from EtatStockController");
            List<Article> articles = articleDbhelper.getArticles();
            articleList.setAll(articles);
            tableView.refresh();
    }

}





