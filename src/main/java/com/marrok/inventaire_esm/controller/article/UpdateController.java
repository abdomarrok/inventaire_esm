package com.marrok.inventaire_esm.controller.article;

import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.model.Category;
import com.marrok.inventaire_esm.model.Service;
import com.marrok.inventaire_esm.util.DatabaseHelper;
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

import java.sql.SQLException;
import java.util.List;

public class UpdateController {

    public String chosen_Category = "";
    @FXML
    public TextField nameField;
    @FXML
    public TextField uniteField;
    @FXML
    public TextField quantityField;
    @FXML
    public TextField remarqueField;
    @FXML
    public TextField qrCodeField;
    private Article selectedArticle;
    private ArticleController articleController;

    @FXML
    public TextField descriptionField;
    public ChoiceBox<Service> serviceChoiceBox;

    @FXML
    public ChoiceBox<Category> categoryChoiceBox;
    DatabaseHelper dbhelper=new DatabaseHelper();

    public UpdateController() throws SQLException {
    }


    public void setDashboardController(ArticleController articleController) {
        this.articleController = articleController;
        loadCategories();
        //loadServices();

    }
    public void setArticleData(long article_id) {
        try {
            DatabaseHelper dbHelper = new DatabaseHelper();
            Article article = dbHelper.getArticleById(article_id);
            if (article != null) {
                selectedArticle = article;
                nameField.setText(article.getName());
                uniteField.setText(article.getUnite());
                quantityField.setText(String.valueOf(article.getQuantity()));
                remarqueField.setText(article.getRemarque());
                descriptionField.setText(article.getDescription());

                String chosen_Category = dbhelper.getCategoryById(article.getIdCategory());
                if (chosen_Category != null) {
                    categoryChoiceBox.setValue(new Category(chosen_Category));
                }
            } else {
                // Handle case when article is not found
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "فشل في استرجاع تفاصيل العنصر.");

            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "فشل في استرجاع تفاصيل العنصر.");

        }
    }


    @FXML
    public void updateArticle(ActionEvent event) {
        selectedArticle.setName(nameField.getText());
        //selectedArticle.setIdPlace(localisationChoiceBox.getValue().getId());
        selectedArticle.setUnite(uniteField.getText());
        selectedArticle.setQuantity(Integer.parseInt(quantityField.getText()));
        selectedArticle.setRemarque(remarqueField.getText());
        int id_cat = dbhelper.getCategoryByName(categoryChoiceBox.getSelectionModel().getSelectedItem().getName());
        selectedArticle.setIdCategory(id_cat);

        selectedArticle.setDescription(descriptionField.getText());

        if (dbhelper.updateArticle(selectedArticle)) {
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

//    private void loadServices() {
//        List<Service> services = DatabaseHelper.getServices();
//        ObservableList<Service> servicesObservableList = FXCollections.observableList(services);
//        serviceChoiceBox.setItems(servicesObservableList);
//
//        serviceChoiceBox.setConverter(new StringConverter<Service>() {
//            @Override
//            public String toString(Service service) {
//                return service != null ? service.getName() : "";
//            }
//
//            @Override
//            public Service fromString(String string) {
//                return services.stream()
//                        .filter(service -> service.getName().equals(string))
//                        .findFirst()
//                        .orElse(null);
//            }
//        });
//
//
//    }

    private void loadCategories() {
        // Fetch categories from the database
        List<Category> categories = dbhelper.getCategories();
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

//    private void loadLocalisations(int serviceId) {
//        List<Localisation> localisations = DatabaseHelper.getLocalisationsByServiceId(serviceId);
//        ObservableList<Localisation> localisationsObservableList = FXCollections.observableList(localisations);
//        localisationChoiceBox.setItems(localisationsObservableList);
//
//        localisationChoiceBox.setConverter(new StringConverter<Localisation>() {
//            @Override
//            public String toString(Localisation localisation) {
//                return localisation != null ? localisation.getLocName() : "";
//            }
//
//            @Override
//            public Localisation fromString(String string) {
//                return localisations.stream()
//                        .filter(localisation -> localisation.getLocName().equals(string))
//                        .findFirst()
//                        .orElse(null);
//            }
//        });
//
//        localisationChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                chosenLocalisation = newValue.getLocName();
//            }
//        });
//    }

    @FXML
    public void cancel(ActionEvent event) {
        closeForm();
    }

    private void closeForm() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }



}
