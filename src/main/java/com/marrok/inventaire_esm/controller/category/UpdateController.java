package com.marrok.inventaire_esm.controller.category;

import com.marrok.inventaire_esm.model.Category;
import com.marrok.inventaire_esm.util.database.CategoryDbHelper;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class UpdateController {
    Logger logger = LogManager.getLogger(UpdateController.class);

    @FXML
    private TextField categoryNameTextField;

    private Category selectedCategory;
    private CategoriesController categoriesController;
    private CategoryDbHelper categoryDbhelper = new CategoryDbHelper();

    public UpdateController() throws SQLException {
    }

    public void setCategory(Category selectedCategory) {
        logger.info("setCategory"+selectedCategory.toString());
        this.selectedCategory = selectedCategory;
        // Set the text field with the current category name
        categoryNameTextField.setText(selectedCategory.getName());
    }

    public void setCategoriesController(CategoriesController categoriesController) {
        logger.info("setCategoriesController"+categoriesController.toString());
        this.categoriesController = categoriesController;
    }

    @FXML
    private void updateCategory(ActionEvent event) {
        logger.info("updateCategory");
        // Get the updated category name from the text field
        String updatedName = categoryNameTextField.getText();

        // Update the selected category with the new name
        selectedCategory.setName(updatedName);

        // Update the category in the database
      try{
          categoryDbhelper.updateCategory(selectedCategory);
          GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "تم تحديث الفئة", "تم تحديث الفئة بنجاح.");

          categoriesController.loadData(); // Refresh the categories list
            closeForm();
        } catch(Exception e) {
            // If update fails, show an error message
          logger.error(e);
          GeneralUtil.showAlert(Alert.AlertType.ERROR, "فشل التحديث", e.getMessage());

      }
    }

    @FXML
    private void cancel(ActionEvent event) {
        // Close the update category form
        closeForm();
    }

    private void closeForm() {
        // Close the stage associated with the form
        Stage stage = (Stage) categoryNameTextField.getScene().getWindow();
        stage.close();
    }
}
