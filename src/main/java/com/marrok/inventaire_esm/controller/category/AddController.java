package com.marrok.inventaire_esm.controller.category;

import com.marrok.inventaire_esm.model.Category;
import com.marrok.inventaire_esm.util.DatabaseHelper;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;


public class AddController {
    public TextField nameField_cat_add;
    public Label titleLabel_cat_add;

    private CategoriesController categoriesController;
    DatabaseHelper dbhelper=new DatabaseHelper();

    public AddController() throws SQLException {
    }

    public void setCategoriesdController(CategoriesController categoriesController) {
        this.categoriesController = categoriesController;
    }


    public void cancel(ActionEvent actionEvent) {
        closeForm();
    }

    public void addCategory(ActionEvent actionEvent) {
        String name_cat = nameField_cat_add.getText().trim();
        if (name_cat.isEmpty()) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "إدخال غير صالح", "لا يمكن أن يكون حقل الاسم فارغًا.");

            return;
        }
        Category category = new Category(name_cat);
        if (dbhelper.addCategory(category)) {
            categoriesController.getCategoryList().add(category);
            categoriesController.loadData();
            Alert alertsecsuss = new Alert(Alert.AlertType.INFORMATION);
            GeneralUtil.showAlert(alertsecsuss.getAlertType(), "تمت إضافة الفئة", "تمت إضافة الفئة بنجاح.");


            closeForm();
        } else {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "فشل إضافة الفئة", "فشل في إضافة الفئة.");

        }
    }

    private void closeForm() {
        Stage stage = (Stage) nameField_cat_add.getScene().getWindow();
        stage.close();
    }


}
