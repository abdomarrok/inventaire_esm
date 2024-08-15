package com.marrok.inventaire_esm.controller.employer;

import com.marrok.inventaire_esm.model.Employer;
import com.marrok.inventaire_esm.util.DatabaseHelper;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AddController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField titleField;

    private EmployerController employerController;
    private DatabaseHelper dbhelper=new DatabaseHelper();

    public AddController() throws SQLException {
    }

    public void setEmployerController(EmployerController employerController) {
        this.employerController = employerController;
    }

    @FXML
    private void handleAddEmployer(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String title = titleField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || title.isEmpty()) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "يرجى ملء جميع الحقول.");
            return;
        }

        Employer newEmployer = new Employer(0, firstName, lastName, title);
        int success = dbhelper.addEmployer(newEmployer.getFirstName(), newEmployer.getLastName(), newEmployer.getTitle());

        if (success != -1) {
            GeneralUtil.showAlert(Alert.AlertType.INFORMATION, "نجاح", "تمت إضافة الموظف بنجاح.");
            employerController.refreshTableData();
            closeWindow();
        } else {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "فشل في إضافة الموظف.");
        }

    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) firstNameField.getScene().getWindow();
        stage.close();
    }
}
