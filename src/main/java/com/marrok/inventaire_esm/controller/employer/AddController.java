package com.marrok.inventaire_esm.controller.employer;

import com.marrok.inventaire_esm.model.Employer;
import com.marrok.inventaire_esm.util.GeneralUtil;
import com.marrok.inventaire_esm.util.database.EmployerDbHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class AddController {
    Logger logger = LogManager.getLogger(AddController.class);
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField titleField;

    private EmployerController employerController;

    private EmployerDbHelper employerDbHelper=new EmployerDbHelper();

    public AddController() throws SQLException {
    }

    public void setEmployerController(EmployerController employerController) {
        logger.info("setEmployerController");
        this.employerController = employerController;
    }

    @FXML
    private void handleAddEmployer(ActionEvent event) {
        logger.info("handleAddEmployer");
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String title = titleField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || title.isEmpty()) {
            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "يرجى ملء جميع الحقول.");
            return;
        }

        Employer newEmployer = new Employer(0, firstName, lastName, title);
        int success = employerDbHelper.addEmployer(newEmployer.getFirstName(), newEmployer.getLastName(), newEmployer.getTitle());

        if (success != -1) {
            logger.info("success addEmployer");
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
