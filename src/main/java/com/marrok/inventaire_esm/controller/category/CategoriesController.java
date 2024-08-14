package com.marrok.inventaire_esm.controller.category;

import com.marrok.inventaire_esm.model.Category;
import com.marrok.inventaire_esm.util.DatabaseHelper;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class CategoriesController implements Initializable {


    public TableColumn cat_name;
    public TableColumn cat_id;
    public TextField search_cat_txt;
    public TableView cat_tableView;

    private ObservableList<Category> categoryList;
    private FilteredList<Category> filteredCategoryList;

    private Category selectedCategory;
    @FXML
    public ToggleButton switchThemeBtn_category;
    DatabaseHelper dbhelper= new DatabaseHelper();
    private final Properties themeProperties = new Properties();

    public CategoriesController() throws SQLException {
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
        initializeColumns();
        setupSearchFilter();
    }

    private void setupSearchFilter() {
        search_cat_txt.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredCategoryList.setPredicate(category -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return category.getName().toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(category.getId()).contains(lowerCaseFilter);
            });
        });
    }

    public void loadData() {
        categoryList = FXCollections.observableArrayList(dbhelper.getCategories());
        for (Category category : categoryList) {
            System.out.println(category.getId());
        }
        filteredCategoryList = new FilteredList<>(categoryList, p -> true);
        cat_tableView.setItems(filteredCategoryList);

    }

    private void initializeColumns() {

        cat_tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Set cell value factories
        cat_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        cat_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        cat_tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedCategory = (Category) newValue);
    }


    @FXML
    public void updateCategory(ActionEvent actionEvent) {
        // Check if a category is selected
        if (selectedCategory != null) {
            // Open a form for updating the selected category
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/category/update_form-view.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(loader.load());
//                TransitTheme transitTheme = new TransitTheme(Style.LIGHT);
//                transitTheme.setScene(scene);
                stage.setScene(scene);

                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Update Category");
                stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));
                UpdateController controller = loader.getController();
                controller.setCategory(selectedCategory);
                controller.setCategoriesController(this); // You might need this to refresh the category list after updating
                stage.showAndWait();
            } catch (IOException e) {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Could not open the update category form.");
                e.printStackTrace();
            }
        } else {
            // If no category is selected, show a warning message
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a category to update.");
        }
    }


    @FXML
    public void go_Dashboard(ActionEvent event) {

      GeneralUtil.goBackDashboard(event);

    }

    @FXML
    public void deleteCategory(ActionEvent actionEvent) {
        Category selectedCategory = (Category) cat_tableView.getSelectionModel().getSelectedItem();

        if (selectedCategory != null) {
            boolean success = dbhelper.deleteCategory(selectedCategory.getId());

            if (success) {
                categoryList.remove(selectedCategory);
                GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "Category Deleted", "The Category was deleted successfully.");
            } else {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "Delete Category Failed", "Failed to delete the Category.");
            }
        } else {
            GeneralUtil.showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an Category to delete.");
        }
    }

    @FXML
    public void addCategory(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/marrok/inventaire_esm/view/category/add_form-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Category");
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/com/marrok/inventaire_esm/img/esm-logo.png")));
            AddController controller = loader.getController();
            controller.setCategoriesdController(this);

            stage.showAndWait();
        } catch (IOException e) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Error", "Could not open the add article form.");
            e.printStackTrace();
        }
    }

    public ObservableList<Category> getCategoryList() {
        return categoryList;
    }


}
