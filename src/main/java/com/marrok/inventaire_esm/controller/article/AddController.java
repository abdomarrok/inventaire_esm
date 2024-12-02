package com.marrok.inventaire_esm.controller.article;

import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.model.Category;
import com.marrok.inventaire_esm.model.Localisation;
import com.marrok.inventaire_esm.model.Service;
import com.marrok.inventaire_esm.util.database.ArticleDbHelper;
import com.marrok.inventaire_esm.util.database.CategoryDbHelper;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.log4j.Logger;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AddController implements Initializable {
    public TextField min_quantity_txt;
    Logger logger = Logger.getLogger(AddController.class);
    public String chosenCategory = "";
    public TextField nameField;
//    public TextField quantityField;
    public TextField unitField;
    public TextField remarkField;
    public TextField descriptionField;
    public ChoiceBox<Service> serviceChoiceBox;
    public ChoiceBox<Category> categoryChoiceBox;
    public ChoiceBox<Localisation> localisationChoiceBox;
    private ArticleController articleController;



    public AddController() throws SQLException {
    }
    public void setDashboardController(ArticleController articleController ) {
        logger.info("setDashboardController");
        this.articleController = articleController;
        loadCategories();
    }
    private ArticleDbHelper articleDbhelper = new ArticleDbHelper();
    private CategoryDbHelper categoryDbhelper = new CategoryDbHelper();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("initialize");
        // Any required initialization can be done here
        loadCategories();
    }






    private void loadCategories() {
        List<Category> categories = categoryDbhelper.getCategories();
        logger.info("loadCategories");
        logger.info("categories size "+categories.size());
        ObservableList<Category> categoriesObservableList = FXCollections.observableList(categories);
        categoryChoiceBox.setItems(categoriesObservableList);

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

        categoryChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                chosenCategory = newValue.getName();
            }
        });
    }



    public void addArticle(ActionEvent event) {
        logger.info("addArticle");
        String name = nameField.getText().trim();
        String unit = unitField.getText().trim();
        String remark = remarkField.getText().trim();
        String descriptionText = descriptionField.getText().trim();
        int min_qun_txt = Integer.parseInt(min_quantity_txt.getText().trim());

        if (name.isEmpty()) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "مدخلات خاطئة", "اسم العنصر لا يجب ان يكون فارغا");
            return;
        }

        if (chosenCategory.isEmpty()) {
            chosenCategory="autre";
            return;
        }


        int categoryId = categoryDbhelper.getCategoryByName(chosenCategory);

        Article newArticle = new Article(0, name, unit, remark, descriptionText, categoryId,min_qun_txt);
        if (articleDbhelper.addArticle(newArticle)) {
            logger.info("articleDbhelper.addArticle(newArticle) OK");
            articleController.getArticleList().add(newArticle);
            articleController.loadData();
            GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "تمت إضافة العنصر", "تمت إضافة العنصر بنجاح.");
            closeForm();
        } else {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "فشل في إضافة العنصر", "فشل في إضافة العنصر.");

        }
    }


    public void cancel(ActionEvent event) {
        closeForm();
    }

    private void closeForm() {
        Stage stage = (Stage) categoryChoiceBox.getScene().getWindow();
        stage.close();

    }


}
