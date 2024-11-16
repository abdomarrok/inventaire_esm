package com.marrok.inventaire_esm.controller.article;

import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.model.StockAdjustment;
import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.SessionManager;
import com.marrok.inventaire_esm.util.database.ArticleDbHelper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import static com.marrok.inventaire_esm.util.GeneralUtil.showAlert;
import static com.marrok.inventaire_esm.util.GeneralUtil.showAlertWithOutTimelimit;


public class AddAdjustmentController {
    Logger logger = Logger.getLogger(AddAdjustmentController.class);

    StockAdjustmentController parentController;

    String[] operations={"زيادة","إنقاص"};


    @FXML
    private TableView<Article> articleTable;  // Corrected to use Article instead of Entree
    public TableColumn<Article, String> articleUniteColumn;
    public TableColumn<Article, String> articleNameColumn;
    public TableColumn<Article, Integer> articlequantityColumn;
    public ChoiceBox operation_type;
    private ObservableList<Article> articleList = FXCollections.observableArrayList();
    private FilteredList<Article> filteredArticleList;
    private Article selectedArticle;
    public TextField searchField;
    public TextField quantityField;
    public TextArea edit_reason;
    StockAdjustment selectedStockAdjustment;
    StockAdjustmentController stockAdjustmentController;
    private ArticleDbHelper articleDbhelper = new ArticleDbHelper();
    Map<Integer, Integer> totalQuantities = articleDbhelper.getTotalQuantitiesByArticle();
    public  AddAdjustmentController () throws SQLException {
    }
    @FXML
    public void initialize() {
        logger.info("Initializing AddAdjustmentController");
        initializeColumns();
        loadData();
        setupSearchFilter();
        setupTableSelectionListener();

    }
    public void  setAdjustemnt(StockAdjustment selectedStockAdjustment){
        logger.info("setAdjustment with"+selectedStockAdjustment );
        this.selectedStockAdjustment = selectedStockAdjustment;
    }

    private void loadData() {
       logger.info("loadData");
        operation_type.getItems().addAll(operations);
        operation_type.setValue(operations[0]);
        updateOperationTypeStyle();

        operation_type.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateOperationTypeStyle();
        });
        articleList.setAll(fetchArticlesFromDatabase());
        filteredArticleList = new FilteredList<>(articleList, p -> true);
        articleTable.setItems(filteredArticleList); // Bind to filteredArticleList
    }

    private void updateOperationTypeStyle() {
        logger.info("updateOperationTypeStyle");
        if ("زيادة".equals(operation_type.getValue())) {
            operation_type.setStyle("-fx-background-color: #c8e6c9; -fx-text-fill: #2e7d32;");
        } else {
            operation_type.setStyle("-fx-background-color: #ffcdd2; -fx-text-fill: #b71c1c;");
        }
    }

    private void initializeColumns() {
        logger.info("initialize AddAdjustment article table Columns");
        // Setup the article column
        articleNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        articleUniteColumn.setCellValueFactory(new PropertyValueFactory<>("unite"));
        articlequantityColumn.setCellValueFactory(cellData -> {
            Article article = cellData.getValue();
            int articleId = article.getId();
            Integer totalQuantity = totalQuantities.get(articleId); // Fetch pre-calculated quantity
            return new SimpleIntegerProperty(totalQuantity != null ? totalQuantity : 0).asObject();
        });
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

        articleTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedArticle = newValue);

    }
    // Fetch available articles from the database
    private ObservableList<Article> fetchArticlesFromDatabase() {
        logger.info("fetchArticlesFromDatabase");
        return FXCollections.observableArrayList(
                articleDbhelper.getArticles()
        );
    }

    @FXML
    public void confirmSelection(ActionEvent event) {
        logger.info("confirmSelection");
        selectedArticle = articleTable.getSelectionModel().getSelectedItem();

        if (selectedArticle == null || quantityField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select an article and enter a valid quantity.");
            return;
        }


        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "الكمية لا يجدر ان تكون صفرا او اقل من 0");
                return;
            }
        } catch (NumberFormatException e) {
            logger.error(e);
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter valid numbers for quantity.");
            return;
        }

        // Check if the operation type is 'decrease' and validate stock
        if ("إنقاص".equals(operation_type.getValue())) {
            int availableStock = articleDbhelper.getTotalQuantityByArticleId(selectedArticle.getId());
            if (availableStock < quantity) {
                showAlert(Alert.AlertType.ERROR, "Insufficient Stock",
                        "Requested quantity exceeds available stock. Available: " + availableStock);
                return;
            }
        }
        if(edit_reason.getText().equals(null)||edit_reason.getText().equals("")){
            showAlertWithOutTimelimit(Alert.AlertType.ERROR, "Error", "يجب ادخال سبب التعديل");
            return;
        }

        // Create a new StockAdjustment object and set the data
        selectedStockAdjustment = new StockAdjustment();
        selectedStockAdjustment.setArticleId(selectedArticle.getId()); // Use the article's ID
        selectedStockAdjustment.setQuantity(quantity);
        selectedStockAdjustment.setAdjustmentType("زيادة".equals(operation_type.getValue()) ? "increase" : "decrease");
        selectedStockAdjustment.setAdjustmentDate(new Date());
        selectedStockAdjustment.setRemarks(edit_reason.getText());
       int  user_id = SessionManager.getActiveUserId();
       selectedStockAdjustment.setUserId(user_id);
        if(articleDbhelper.addStockAdjustment(selectedStockAdjustment)){
            stockAdjustmentController.refreshTableData();
            closeDialog(event);
        }else {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "فشل في إضافة التعديل على  المخزون.");
        }

        // Save to database or perform further processing here

        closeDialog(event);
    }

    @FXML
    public void closeDialog(ActionEvent event) {
        // Close the dialog without any action
        Stage stage = (Stage) articleTable.getScene().getWindow();
        stage.close();
    }

    public void setParentController(StockAdjustmentController stockAdjustmentController) {
        logger.info("setParentControlle for stockAdjustmentController");
        this.stockAdjustmentController=stockAdjustmentController;
    }
}
