package com.marrok.inventaire_esm.controller.article;

import com.marrok.inventaire_esm.model.*;
import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.database.ArticleDbHelper;
import com.marrok.inventaire_esm.util.database.UserDbHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;


public class StockAdjustmentController implements Initializable {
    Logger logger = LogManager.getLogger(StockAdjustmentController.class);
    public TableView<StockAdjustment> tableView;
    public TableColumn<StockAdjustment,Integer> id_adjustment;
    public TableColumn<StockAdjustment,String> date;
    public TableColumn<StockAdjustment,String>  article;
    public TableColumn<StockAdjustment,Integer> quantity;
    public TableColumn<StockAdjustment,String> user;
    public TableColumn<StockAdjustment,String> operation_type;
    public TableColumn<StockAdjustment,String> remark;

    public TextField searchField;
    private ObservableList<StockAdjustment> adjustmentList;
    private FilteredList<StockAdjustment> filtredadjustemntList;
    private StockAdjustment selectedAdjustemnt;
    private ArticleDbHelper articleDbhelper = new ArticleDbHelper();
    private UserDbHelper userDbHelper=new UserDbHelper();

    public StockAdjustmentController() throws SQLException {
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("Initializing StockAdjustmentController");
        loadData();
        initializeColumns();
        setupSearchFilter();
        setupTableSelectionListener();
    }

    private void setupTableSelectionListener() {
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedAdjustemnt = newValue);
    }

    private void setupSearchFilter() {

        // Cache article information to avoid repeated DB calls
        Map<Integer, Article> articleCache = new HashMap<>();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filtredadjustemntList.setPredicate(adjustment -> {
                // If the search field is empty, show all adjustments
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Get article information and handle nulls
                Article article = articleCache.computeIfAbsent(adjustment.getArticleId(), id ->
                        articleDbhelper.getArticleById(id));

                // Null-safe fields
                String adjustmentType = adjustment.getAdjustmentType() != null ? adjustment.getAdjustmentType() : "";
                String adjustmentDate = adjustment.getAdjustmentDate() != null ? adjustment.getAdjustmentDate().toString() : "";
                String remarks = adjustment.getRemarks() != null ? adjustment.getRemarks() : "";
                String articleName = article != null && article.getName() != null ? article.getName() : "";

                // Convert search text to lowercase for case-insensitive filtering
                String lowerCaseFilter = newValue.toLowerCase();

                // Perform filtering
                return adjustmentType.toLowerCase().contains(lowerCaseFilter)
                        || adjustmentDate.contains(lowerCaseFilter)
                        || String.valueOf(adjustment.getId()).contains(lowerCaseFilter)
                        || articleName.toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(adjustment.getQuantity()).contains(lowerCaseFilter)
                        || remarks.toLowerCase().contains(lowerCaseFilter);
            });
        });
    }


    // Cache for user data
    private final Map<Integer, String> userCache = new HashMap<>();

    private void preloadUserData() {
        try {
            // Fetch all users from the database
            List<User> users = userDbHelper.getUsers(); // Assuming this method exists
            for (User user : users) {
                userCache.put(user.getId(), user.getUsername());
            }
            logger.info("User data preloaded successfully.");
        } catch (Exception e) {
            logger.error( "Failed to preload user data", e);
        }
    }

    private void initializeColumns() {
        logger.info("Initializing StockAdjustmentController");

        // Preload user data
        preloadUserData();

        id_adjustment.setCellValueFactory(new PropertyValueFactory<>("id"));
        date.setCellValueFactory(new PropertyValueFactory<>("adjustmentDate"));

        // Use the cache for the user column
        user.setCellValueFactory(cellData -> {
            int userId = cellData.getValue().getUserId();
            String userName = userCache.getOrDefault(userId, "Unknown User");
            return new SimpleStringProperty(userName);
        });

        article.setCellValueFactory(cellData -> {
            int articleId = cellData.getValue().getArticleId();
            try {
                String articleName = articleDbhelper.getArticleById(articleId).getName();
                return new SimpleStringProperty(articleName != null ? articleName : "Unknown Article");
            } catch (Exception e) {
                logger.error( "Error fetching article name", e);
                return new SimpleStringProperty("Unknown Article");
            }
        });

        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        operation_type.setCellValueFactory(new PropertyValueFactory<>("adjustmentType"));
        operation_type.setCellFactory(column -> new TableCell<StockAdjustment, String>() {
            private final Map<String, String> styleMap = Map.of(
                    "increase", "-fx-background-color: #c8e6c9; -fx-text-fill: #2e7d32;",
                    "decrease", "-fx-background-color: #ffcdd2; -fx-text-fill: #b71c1c;"
            );

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle(styleMap.getOrDefault(item.toLowerCase(), ""));
                }
            }
        });
        remark.setCellValueFactory(new PropertyValueFactory<>("remarks"));
    }


    private void loadData() {
        logger.info("load StockAdjustments");
        List<StockAdjustment> adjustments = articleDbhelper.getAllStockAdjustments();
        adjustmentList = FXCollections.observableArrayList(adjustments);
        filtredadjustemntList = new FilteredList<>(adjustmentList);
        tableView.setItems(filtredadjustemntList);
    }

    public void goAddAdjustemnt(ActionEvent event) {
        logger.info("goAddAdjustemnt");
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/article/add_adjustement.fxml"));
            Parent root = loader.load();
            // Get the controller and set the selected Adjustemn
            AddAdjustmentController controller = loader.getController();
            controller.setAdjustemnt(selectedAdjustemnt);
            controller.setParentController(this);
            // Create a new scene and set it to the stage
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));
            stage.setTitle("إضافة تعديل");
            stage.show();

        } catch (IOException e) {
           logger.error(e);

        }
    }

    public void refreshTableData() {
        List<StockAdjustment> stockAdjustments = articleDbhelper.getAllStockAdjustments();
        adjustmentList.setAll(stockAdjustments);
        tableView.refresh();

    }


    public void goStockDashboard(ActionEvent event) {
        GeneralUtil.goBackStockDashboard(event);
    }
}
