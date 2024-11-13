package com.marrok.inventaire_esm.controller.article;

import com.marrok.inventaire_esm.controller.bon_entree.DetailViewController;
import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.model.BonEntree;
import com.marrok.inventaire_esm.model.Entree;
import com.marrok.inventaire_esm.model.StockAdjustment;
import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.database.ArticleDbHelper;
import com.marrok.inventaire_esm.util.database.UserDbHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class StockAdjustmentController implements Initializable {
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
        loadData();
        initializeColumns();
        setupSearchFilter();
        setupTableSelectionListener();
    }

    private void setupTableSelectionListener() {
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedAdjustemnt = newValue);
    }

    private void setupSearchFilter() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<String> articleNameList = new ArrayList<>();

            filtredadjustemntList.setPredicate(Adjustemnt -> {
                // If the search field is empty, return all entries.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                int AdjustemntId = Adjustemnt.getId();
                StockAdjustment adjustment = articleDbhelper.getStockAdjustmentById(AdjustemntId);

                Article article = articleDbhelper.getArticleById(adjustment.getArticleId());



                // Convert the search query to lowercase for case-insensitive matching.
                String lowerCaseFilter = newValue.toLowerCase();
                return Adjustemnt.getAdjustmentType().contains(lowerCaseFilter)
                        || Adjustemnt.getAdjustmentDate().toString().contains(lowerCaseFilter)
                        || String.valueOf(Adjustemnt.getId()).contains(lowerCaseFilter)
                        || String.valueOf(Adjustemnt.getQuantity()).contains(lowerCaseFilter)
                        || String.valueOf(Adjustemnt.getRemarks()).contains(lowerCaseFilter);

            });
        });
    }

    private void initializeColumns() {
        id_adjustment.setCellValueFactory(new PropertyValueFactory<>("id"));
        date.setCellValueFactory(new PropertyValueFactory<>("adjustmentDate"));
        //new PropertyValueFactory<>("userId")
        user.setCellValueFactory(cellData ->{
            int userId = cellData.getValue().getUserId();
            try {
                String userName = userDbHelper.getUserNameById(userId);
                if (userName != null) {
                    return new SimpleStringProperty(userName);
                }
            }catch (Exception e) {
                e.printStackTrace();
                return new SimpleStringProperty("Unknown User");
            }
            return null;
        });
        article.setCellValueFactory(cellData -> {
            int articleId = cellData.getValue().getArticleId();
            try {
                String articleName = articleDbhelper.getArticleById(articleId).getName();

                if (articleName != null && !articleName.isEmpty()) {
                    return new SimpleStringProperty(articleName);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new SimpleStringProperty("Unknown Article");
            }
            return null;
        });
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        operation_type.setCellValueFactory(new PropertyValueFactory<>("adjustmentType"));
        remark.setCellValueFactory(new PropertyValueFactory<>("remarks"));
    }

    private void loadData() {
        List<StockAdjustment> adjustments = articleDbhelper.getAllStockAdjustments();
        for (StockAdjustment adjustment : adjustments) {
            System.out.println("adj"+adjustment.toString());
        }
        adjustmentList = FXCollections.observableArrayList(adjustments);
        filtredadjustemntList = new FilteredList<>(adjustmentList);
        tableView.setItems(filtredadjustemntList);
    }

    public void goAddAdjustemnt(ActionEvent event) {
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
            stage.setTitle("Add adjustemnt");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();

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
