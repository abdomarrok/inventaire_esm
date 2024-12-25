package com.marrok.inventaire_esm.controller.article;

import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.model.Category;
import com.marrok.inventaire_esm.model.Service;
import com.marrok.inventaire_esm.util.database.ArticleDbHelper;
import com.marrok.inventaire_esm.util.database.CategoryDbHelper;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

public class UpdateController {

    Logger logger = LogManager.getLogger(UpdateController.class);

    public String chosen_Category = "";
    @FXML
    public TextField nameField;
    @FXML
    public TextField uniteField;

    @FXML
    public TextField remarqueField;
    public TextField min_quantity_txt;
    private Article selectedArticle;
    private ArticleController articleController;

    @FXML
    public TextField descriptionField;
    public ChoiceBox<Service> serviceChoiceBox;

    @FXML
    public ChoiceBox<Category> categoryChoiceBox;
    private ArticleDbHelper articleDbhelper = new ArticleDbHelper();
    private CategoryDbHelper categoryDbhelper = new CategoryDbHelper();

    public UpdateController() throws SQLException {
    }


    public void setDashboardController(ArticleController articleController) {
        logger.info("setDashboardController");
        this.articleController = articleController;
        loadCategories();

    }
    public void setArticleData(long article_id) {
        logger.info("setArticleData");
            Article article = articleDbhelper.getArticleById(article_id);
            if (article != null) {
                selectedArticle = article;
                nameField.setText(article.getName());
                uniteField.setText(article.getUnite());
                min_quantity_txt.setText(String.valueOf(article.getMin_quantity()));
                remarqueField.setText(article.getRemarque());
                descriptionField.setText(article.getDescription());

                String chosen_Category = categoryDbhelper.getCategoryById(article.getIdCategory());
                if (chosen_Category != null) {
                    categoryChoiceBox.setValue(new Category(chosen_Category));
                }
            } else {
                // Handle case when article is not found
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "فشل في استرجاع تفاصيل العنصر.");

            }

    }


    @FXML
    public void updateArticle(ActionEvent event) {
        logger.info("updateArticle");
        selectedArticle.setName(nameField.getText());

        selectedArticle.setUnite(uniteField.getText());
        selectedArticle.setRemarque(remarqueField.getText());
        int min_qun_txt = Integer.parseInt(min_quantity_txt.getText().trim());
        if(min_qun_txt<0){
            GeneralUtil.showAlert(Alert.AlertType.ERROR,"مدخلات خاطئة", "الكمية الادنى لايمكن ان تكون اقل من 0");
            return;
        }
        selectedArticle.setMin_quantity(min_qun_txt);
        int id_cat = categoryDbhelper.getCategoryByName(categoryChoiceBox.getSelectionModel().getSelectedItem().getName());
        selectedArticle.setIdCategory(id_cat);

        selectedArticle.setDescription(descriptionField.getText());

        if (articleDbhelper.updateArticle(selectedArticle)) {
            // Close the form
            articleController.loadData();
            Alert alertsecsuss = new Alert(Alert.AlertType.INFORMATION);
            GeneralUtil.showAlert(alertsecsuss.getAlertType(), "تحديث العنصر", "تم تحديث العنصر بنجاح.");

            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();
        } else {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "فشل التحديث", "فشل في تحديث العنصر.");

        }
    }


    private void loadCategories() {
        logger.info("loadCategories");
        // Fetch categories from the database
        List<Category> categories = categoryDbhelper.getCategories();
        // Populate the choice box with category names
        ObservableList<Category> categoriesObservableList = FXCollections.observableList(categories);
        categoryChoiceBox.setItems(categoriesObservableList);
        // Set a StringConverter to display the category names
        categoryChoiceBox.setConverter(new StringConverter<Category>() {
            @Override
            public String toString(Category category) {
                return category != null ? category.getName() : "";
            }

            @Override
            public Category fromString(String string) {
                return categories.stream()
                        .filter(category -> category.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });


        // Handle selection
        categoryChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                chosen_Category = newValue.getName();
            }
        });
    }



    @FXML
    public void cancel(ActionEvent event) {
        closeForm();
    }

    private void closeForm() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }



}
