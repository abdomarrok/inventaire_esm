package com.marrok.inventaire_esm.controller.article;

import com.marrok.inventaire_esm.model.Article;
import com.marrok.inventaire_esm.model.Category;
import com.marrok.inventaire_esm.model.Localisation;
import com.marrok.inventaire_esm.model.Service;
import com.marrok.inventaire_esm.util.DatabaseHelper;
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

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AddController implements Initializable {

    public String chosenCategory = "";
    public TextField nameField;
    public TextField quantityField;
    public TextField unitField;
    public TextField remarkField;
    public TextField descriptionField;
    public ChoiceBox<Service> serviceChoiceBox;
    public ChoiceBox<Category> categoryChoiceBox;
    public ChoiceBox<Localisation> localisationChoiceBox;
    private ArticleController articleController;
    private DatabaseHelper dbhelper=new DatabaseHelper();

    public AddController() throws SQLException {
    }

    public void setDashboardController(ArticleController articleController) {
        this.articleController = articleController;
        loadCategories();
//        loadServices();
//        serviceChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                loadLocalisations(newValue.getId());
//            }
//        });
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
//    }

    private void loadCategories() {
        List<Category> categories = dbhelper.getCategories();
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

    public void addArticle(ActionEvent event) {
        String name = nameField.getText().trim();
        String quantityText = quantityField.getText().trim();
        String unit = unitField.getText().trim();
        String remark = remarkField.getText().trim();
        String descriptionText = descriptionField.getText().trim();

        if (name.isEmpty()) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Invalid Input", "Name field cannot be empty.");
            return;
        }

        if (chosenCategory.isEmpty()) {
            chosenCategory="autre";
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Please enter a valid quantity.");
            return;
        }

        int categoryId = dbhelper.getCategoryByName(chosenCategory);
        if (categoryId == -1) {

        }

        Article newArticle = new Article(0, name, unit, quantity, remark, descriptionText, categoryId);
        if (dbhelper.addArticle(newArticle)) {
            articleController.getArticleList().add(newArticle);
            articleController.loadData();
            GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "Article Added", "The article was added successfully.");
            closeForm();
        } else {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "Add Article Failed", "Failed to add the article.");
        }
    }


    public void cancel(ActionEvent event) {
        closeForm();
    }

    private void closeForm() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Any required initialization can be done here
    }
}
